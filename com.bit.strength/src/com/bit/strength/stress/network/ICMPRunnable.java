package com.bit.strength.stress.network;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

import com.bit.strength.stress.network.RMI.RemoteCollector;
import com.bit.strength.util.DataStorage;

import jpcap.*;
import jpcap.packet.ARPPacket;
import jpcap.packet.EthernetPacket;
import jpcap.packet.ICMPPacket;
import jpcap.packet.IPPacket;

public class ICMPRunnable extends NetRunnable {

	private ArrayList<ICMPPacket> packet;
	private ArrayList<Message> messages;
	private ArrayList<InetAddress> srcIp;
	private InetAddress tarIp;
	NetworkInterface[] devices;

	public ICMPRunnable(ICMPArgs args, RemoteCollector collector) {
		super();
		this.messages = args.getMessages();
		this.packet = args.getPacket();
		this.collector = collector;
		this.exit = false;
		this.srcIp = args.getSrcIp();
		this.tarIp = args.getTarIp();
		this.devices = JpcapCaptor.getDeviceList();
		this.dataStorage = new DataStorage(); 
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		// loop meslist to send
		for (int msg_i = messages.size() - 1; msg_i >= 0; msg_i--) {
			if (exit)
				break;
			System.out.println("ICMP task executed " + msg_i);
			long startTimeForUpdate, endTimeForUpdate;
			long count = 0, startTime = System.currentTimeMillis(), endTime;
			byte[] data = messages.get(msg_i).getData().clone();
			ICMPPacket packetToSend = packet.get(msg_i);
			
			while (!exit) {

				// send
				startTimeForUpdate = System.currentTimeMillis();
				
				System.out.println("icmp try.");
				try {
					JpcapSender sender = JpcapSender.openDevice(devices[0]);
					for (int j = 0; j < srcIp.size(); j++) {
						packetToSend.setIPv4Parameter(0, false, false, false, 0,
								false, false, false, 0, 1010101, 100,
								IPPacket.IPPROTO_ICMP, srcIp.get(j), tarIp);
						sender.sendPacket(packetToSend);
						try {
							Thread.sleep(messages.get(msg_i).getInterval());
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							System.out.println(e.toString());
						}
					}
					sender.close();
				} catch (IOException e) {
					System.out.println("icmp failed. " + e.toString());
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("icmp succedd.");
				endTimeForUpdate = System.currentTimeMillis();
				collector.update(endTimeForUpdate - startTimeForUpdate, 1,
						data.length, 0, 0);
//				// receive data
//				if (messages.get(i).isReceiveNext()) {
//					;//
//				}
				
				count++;
				System.out.println("count: " + count + ", repeat: " + messages.get(msg_i).getRepeat() + ", duration: " + messages.get(msg_i).getDuration());
				// when to stop
				if (messages.get(msg_i).getDuration() == -1) {
					if (count >= messages.get(msg_i).getRepeat())
						break;
				} else {
					endTime = System.currentTimeMillis();
					if (endTime - startTime > messages.get(msg_i).getDuration() * 1000)
						break;
				}
			}
			try {
				// System.out.println("ICMP task succeed");
				System.out.println("ICMP task " + msg_i + " succeed");
			} catch (Exception e) {
				// System.out.println("ICMP task failed");
				System.out.println("ICMP task " + msg_i + " failed");
				System.out.println(e.toString());
			}

		}

	}
}
