package com.bit.strength.util;

import jpcap.packet.Packet;
/**
 * 
 * @author Ericj
 * Capture中抓取到的每行数据的数据内容。packet存储包的数据具体内容
 * count存储该条内容的序列
 */
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
