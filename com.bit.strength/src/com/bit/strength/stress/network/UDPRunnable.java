package com.bit.strength.stress.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import jpcap.packet.EthernetPacket;
import jpcap.packet.ICMPPacket;
import jpcap.packet.IPPacket;



import com.bit.strength.stress.network.RMI.RemoteCollector;

public class UDPRunnable extends IPRunnable {

	public UDPRunnable(IPArgs args, RemoteCollector collector) {
		this.ip = args.getIp();
		this.port = args.getPort();
		this.localPort = args.getLocalPort();
		this.messages = args.getMessages();
		this.collector = collector;
		this.exit = false;
	}

	@Override
	public void run() {
		System.out.println("UDP task executed");
		DatagramSocket client = null;
		byte[] recvBuf = new byte[1024];
		for (int i = 0; i < messages.size(); i++) {
			if (exit)
				break;
			// send data
			try {
				// set port
				if (localPort != -1)
					client = new DatagramSocket(localPort);
				else
					client = new DatagramSocket();
				InetAddress addr = InetAddress.getByName(ip);
				byte[] data = messages.get(i).getData().clone();
				System.out.println(data);
				long startTimeForUpdate, endTimeForUpdate;
				long count = 0, startTime = System.currentTimeMillis(), endTime;
				while (!exit) {
					// send data
					DatagramPacket sendPacket = new DatagramPacket(data,
							data.length, addr, port);
					startTimeForUpdate = System.currentTimeMillis();
					client.send(sendPacket);
					endTimeForUpdate = System.currentTimeMillis();
					collector.update(endTimeForUpdate - startTimeForUpdate, 1,
							data.length, 0, 0);
					// auto increase
					if (messages.get(i).isAutoIncrease() && (data.length >= 2))
						data[1] += 1;

					// wait
					try {
						Thread.sleep(messages.get(i).getInterval());
					}
					catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					// receive data
					if (messages.get(i).isReceiveNext()) {
						DatagramPacket recvPacket = new DatagramPacket(recvBuf,
								recvBuf.length);
						startTimeForUpdate = System.currentTimeMillis();
						client.receive(recvPacket);
						endTimeForUpdate = System.currentTimeMillis();
						String recvStr = new String(recvPacket.getData(), 0,
								recvPacket.getLength());
						collector.update(endTimeForUpdate - startTimeForUpdate,
								0, 0, 1, recvStr.length());
						
						this.dataStorage.storageDataAtLocal(0, recvPacket);
						// System.out.println(recvStr);
						System.out.println("UDP receive: " + 1 + " "
								+ recvStr.length());
					}
					count++;
					// when to stop
					if (messages.get(i).getDuration() == -1) {
						if (count >= messages.get(i).getRepeat())
							break;
					} else {
						endTime = System.currentTimeMillis();
						if (endTime - startTime > messages.get(i).getDuration() * 1000)
							break;
					}
				}
				client.close();
			}
			catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally {
				if (client != null)
					client.close();
			}
			System.out.println("UDP data " + i + " succeed");
		}
		System.out.println("UDP task finished");
	}
}





