package com.bit.strength.stress.network;

import java.util.ArrayList;

import jpcap.packet.ARPPacket;

public class ARPArgs extends NetArgs {
	// network input
	ARPPacket packet;
	private ArrayList<Message> messages;
	
	public ARPArgs(int type, int connection, ARPPacket packet,ArrayList<Message> messages) {
		super();
		this.type = type;
		this.connection = connection;
		this.packet = packet;
		this.messages = messages;
//		System.out.println("ARPPacket: " + packet.toString());
		//System.out.println("ARPPacket message: " + this.messages.get(0).getData().clone());
	}

	public ARPPacket getPacket() {
		return packet;
	}

	public void setPacket(ARPPacket packet) {
		this.packet = packet;
	}
	
	public ArrayList<Message> getMessages() {
		return messages;
	}
	
	public void setMessages(ArrayList<Message> messages) {
		this.messages = messages;
	}
}
