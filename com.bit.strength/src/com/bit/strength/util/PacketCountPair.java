package com.bit.strength.util;

import jpcap.packet.Packet;

public class PacketCountPair {
	private Packet packet;
	private int count;

	public Packet getPacket() {
		return packet;
	}

	public void setPacket(Packet packet) {
		this.packet = packet;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public PacketCountPair(Packet packet, int count) {
		this.packet = packet;
		this.count = count;
	}
}
