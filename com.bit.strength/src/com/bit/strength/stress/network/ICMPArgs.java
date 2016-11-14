package com.bit.strength.stress.network;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import jpcap.*;
import jpcap.packet.EthernetPacket;
import jpcap.packet.ICMPPacket;
import jpcap.packet.IPPacket;

public class ICMPArgs extends NetArgs {
	// network input
	private ArrayList<ICMPPacket> packet = new ArrayList();
	private ArrayList<Message> messages;
	private ArrayList<InetAddress> srcIp;
	private InetAddress tarIp;

	public ICMPArgs(int type, int connection, String ip, int port,
			int localPort, ArrayList<Object> IcmpData,
			ArrayList<Message> messages, ArrayList<InetAddress> srcIp) {
		super();
		for (int msg_i = messages.size() - 1; msg_i >= 0; msg_i--) {
			ICMPPacket tempPacket = new ICMPPacket();
			System.out.println("packet the " + msg_i + " packet");
			byte[] data = messages.get(msg_i).getData().clone();
			int icmpType = Integer.parseInt(String.valueOf(IcmpData.get(0)));
			
			if (icmpType == 0)
				tempPacket.type = ICMPPacket.ICMP_ECHOREPLY;
			else
				tempPacket.type = ICMPPacket.ICMP_ECHO;
			tempPacket.id = 256;
			tempPacket.seq = 256;
			tempPacket.orig_timestamp = 1;
			String dataText = String.valueOf(IcmpData.get(5));
			tempPacket.data = dataText.getBytes();
			packet.add(tempPacket);
		}
		this.messages = messages;
		this.type = type;
		this.connection = connection;
		this.srcIp = srcIp;
		try {
			this.tarIp = InetAddress.getByName(ip);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

//		following code work for test static srcip
/* 
		byte[] add = { 10, 211 - 256, 55, 7 };

		InetAddress ipAddr;
		try {
			ipAddr = InetAddress.getByAddress(add);

			packet.setIPv4Parameter(0, false, false, false, 0, false, false,
					false, 0, 1010101, 100, IPPacket.IPPROTO_ICMP, serverAddr[0],
					InetAddress.getByName(ip));
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 */
		
		
		//Without checksum, icmp packet still can be sended
		/*
		 set icmp checksum
		 String checkSumString = String.valueOf(IcmpData.get(2));
		 int checkSumInt = Integer.valueOf(checkSumString) > 32768 ?
		 (Integer.valueOf(checkSumString) - 65536) :
		 Integer.valueOf(checkSumString);
		 packet.checksum = Short.parseShort(String.valueOf(IcmpData.get(2))); 
		 */
		
		// id & seq are fixed value


		//test icmp packet send
		/*
		 NetworkInterface[] devices = JpcapCaptor.getDeviceList();
		 try {
		 JpcapSender sender = JpcapSender.openDevice(devices[0]);
		 sender.sendPacket(packet);
		 } catch (IOException e) {
		 // TODO Auto-generated catch block
		 e.printStackTrace();
		 }
		 */
	}

	
	
	public ArrayList<ICMPPacket> getPacket() {
		return packet;
	}
	
	// useless now
	public void setPacket(ArrayList<ICMPPacket> packet) {
		this.packet = packet;
	}
	public void addPacket(ICMPPacket packet) {
		this.packet.add(packet);
	}

	
	
	public ArrayList<Message> getMessages() {
		return messages;
	}
	
	public void setMessages(ArrayList<Message> messages) {
		this.messages = messages;
	}
	
	
	public ArrayList<InetAddress> getSrcIp(){
		return srcIp;
	}

	public void setSrcIp(ArrayList<InetAddress> srcIp){
		this.srcIp = srcIp;
	}
	
	public InetAddress getTarIp(){
		return tarIp;
	}

	public void setTarIp(InetAddress tarIp){
		this.tarIp = tarIp;
	}
	

}
