package com.bit.strength.stress.network;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;

import jpcap.JpcapCaptor;
import jpcap.JpcapSender;
import jpcap.NetworkInterface;
import jpcap.NetworkInterfaceAddress;
import jpcap.packet.ARPPacket;
import jpcap.packet.EthernetPacket;

import com.bit.strength.stress.network.RMI.RemoteCollector;
import com.bit.strength.util.DataStorage;

public class ARPRunnable extends NetRunnable {

	private ARPPacket packet;
	private ArrayList<Message> messages;
	//get network card's information, which to find network interface. 
	NetworkInterface[] devices;
	public ARPRunnable(ARPArgs args, RemoteCollector collector) {
		super();
		this.packet = args.getPacket();
		this.messages = args.getMessages();
		this.collector = collector;
		this.dataStorage = new DataStorage();
		this.devices = JpcapCaptor.getDeviceList();
		this.exit = false;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("ARP task executed");
		if(packet == null){
			packet = new ARPPacket();
			packet.hardtype = ARPPacket.HARDTYPE_ETHER;
			packet.prototype = ARPPacket.PROTOTYPE_IP;
			packet.hlen = 6;
			packet.plen = 4;
			packet.caplen = 60;
		}
		
		long startTimeForUpdate, endTimeForUpdate;
		byte[] broadcast=new byte[]{(byte)255,(byte)255,(byte)255,(byte)255,(byte)255,(byte)255};
		System.out.println("ARPRunnable message size " + messages.size());
		for(int msg_i = 0; msg_i < messages.size(); msg_i++){
			if(exit){
				break;
			}
			//start
			//Get sender_ip, target_ip and operation from message. 
			byte[] data = messages.get(msg_i).getData().clone();
			System.out.println(data.length);
			if (data.length != 28) {
				throw new NumberFormatException("Illegal message data length.");
			}
			String ret = "";
			for (int j = 0; j < data.length; j++) {
				String hex = Integer.toHexString(data[j] & 0xFF);
				if (hex.length() == 1) {
					hex = '0' + hex;
				}
				ret += hex.toUpperCase();
			}
			System.out.println("In ARPRunnable, message " + (msg_i + 1)
					+ " ip : " + ret.substring(28, 36) + " -> "
					+ ret.substring(48, 56));
			short ops = Short.parseShort(ret.substring(15, 16));
			byte[] sender_ip = new byte[] {
					(byte) Integer.parseInt(ret.substring(28, 30), 16),
					(byte) Integer.parseInt(ret.substring(30, 32), 16),
					(byte) Integer.parseInt(ret.substring(32, 34), 16),
					(byte) Integer.parseInt(ret.substring(34, 36), 16) };
			byte[] target_ip = new byte[] {
					(byte) Integer.parseInt(ret.substring(48, 50), 16),
					(byte) Integer.parseInt(ret.substring(50, 52), 16),
					(byte) Integer.parseInt(ret.substring(52, 54), 16),
					(byte) Integer.parseInt(ret.substring(54, 56), 16) };
			packet.operation = ops;
			packet.sender_protoaddr = sender_ip;
			packet.target_protoaddr = target_ip;
			packet.sender_hardaddr = broadcast;
			packet.target_hardaddr = broadcast;
			//end
			long count = 0, startTime = System.currentTimeMillis(), endTime;
			NetworkInterface device=null;
			while(!exit){
				try{
					InetAddress ip = (InetAddress)packet.getTargetProtocolAddress();
							
					switch (packet.operation) {
					case ARPPacket.ARP_REQUEST:
						ip = (InetAddress)packet.getTargetProtocolAddress();
						break;
					case ARPPacket.ARP_REPLY:
						ip = (InetAddress)packet.getSenderProtocolAddress();
						break;
					case ARPPacket.RARP_REQUEST:
						ip = (InetAddress)packet.getTargetProtocolAddress();
						break;
					case ARPPacket.RARP_REPLY:
						ip = (InetAddress)packet.getSenderProtocolAddress();
						break;
					default:
						throw new IllegalArgumentException("Invalid operation for arp");
					}
					//make sure sender's hardware address.
					for(NetworkInterface d:devices){
						for(NetworkInterfaceAddress addr:d.addresses){
							if(!(addr.address instanceof Inet4Address)) continue;
							byte[] bip = ip.getAddress();
							byte[] subnet = addr.subnet.getAddress();
							byte[] bif = addr.address.getAddress();
							for(int i=0;i<4;i++){
								bip[i]=(byte)(bip[i]&subnet[i]);
								bif[i]=(byte)(bif[i]&subnet[i]);
							}
							if(Arrays.equals(bip,bif)){
								device=d;
								break;
							}
						}
					}
					if(device==null){
						throw new IllegalArgumentException(ip+" is not a local address");
					}
					
					//Open Jpcap, ready to capture packages.
					JpcapCaptor captor = JpcapCaptor.openDevice(device, 65535, false, 3000);
					//set filter to capture arp packages only
					captor.setFilter("arp", true);
					JpcapSender sender=captor.getJpcapSenderInstance();
//					//make sure sender's ip address is instanceof Inet4Address
//					for(NetworkInterfaceAddress addr:device.addresses){
//						if(addr.address instanceof Inet4Address){
//							break;
//						}
//					}
					//set mac address
					packet.sender_hardaddr = device.mac_address;
					packet.target_hardaddr = broadcast;
											
					EthernetPacket ether=new EthernetPacket();
					ether.frametype=EthernetPacket.ETHERTYPE_ARP;
					ether.src_mac = device.mac_address;
					ether.dst_mac = packet.target_hardaddr;
					
					packet.datalink = ether;
					
//					System.out.println(packet.toString());
					endTimeForUpdate = startTimeForUpdate = System.currentTimeMillis();
					switch(packet.operation){
						case ARPPacket.ARP_REQUEST: 
							sender.sendPacket(packet);
							//send msg and wait for returned msg
							while(true){
								ARPPacket reply = (ARPPacket)captor.getPacket();
								if(reply == null){
									System.out.println("reply get packet failed...");
									throw new IllegalArgumentException(packet.getTargetProtocolAddress()+" is not a local address");
								}
								if(Arrays.equals(reply.target_protoaddr, packet.sender_protoaddr)){
									System.out.println("request target hardware addr " + reply.getSenderHardwareAddress());
									System.out.println("captor length: " + reply.caplen);
									endTimeForUpdate = System.currentTimeMillis();
									collector.update(endTimeForUpdate - startTimeForUpdate, 1,
											packet.caplen, 1, reply.caplen);
									if(count == 0){
										dataStorage.storageDataAtLocal(2, reply);
									}
									
									break;
								}
							}
							break;
						case ARPPacket.ARP_REPLY:
							break;
						case ARPPacket.RARP_REQUEST: 
							sender.sendPacket(packet);
							//´¦Àí·¢»ØµÄmsg
							while(true){
								ARPPacket reply = (ARPPacket)captor.getPacket();
								if(reply == null){
									throw new IllegalArgumentException(packet.getTargetProtocolAddress()+" is not a local address");
								}
								if(Arrays.equals(reply.target_protoaddr, packet.sender_protoaddr)){
									System.out.println("request target hardware addr " + reply.getSenderHardwareAddress());
									System.out.println("captor length: " + reply.caplen);
									endTimeForUpdate = System.currentTimeMillis();
									collector.update(endTimeForUpdate - startTimeForUpdate, 1,
											packet.caplen, 1, reply.caplen);
									if(count == 0){
										dataStorage.storageDataAtLocal(2, reply);
									}
									break;
								}
							}
							break;
						case ARPPacket.RARP_REPLY: break;
						default: System.out.println("Invalid operation for arp");break;
					}
					count++;
					System.out.println("count : " + count);
					captor.close();
					// when to stop
					if (messages.get(msg_i).getDuration() == -1) {
						if (count >= messages.get(msg_i).getRepeat()){
							break;
						}
					} else {
						endTime = System.currentTimeMillis();
						if (endTime - startTime > messages.get(msg_i).getDuration() * 1000){
							break;
						}
					}
					
				}catch(Exception e){
					System.out.println("ARP task failed");
					System.out.println(e.toString());
				}
			}
			//end while
		}
		System.out.println("ARP task end");
	}
}
