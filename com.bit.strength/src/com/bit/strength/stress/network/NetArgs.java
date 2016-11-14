package com.bit.strength.stress.network;

import java.io.Serializable;

import com.bit.strength.util.DataStorage;

public class NetArgs implements Serializable {
	
	public final static int UDP = 0;
	public final static int TCP = 1;
	public final static int ARP = 2;
	public final static int ICMP = 3;
	
	protected int type;
	protected int connection;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getConnection() {
		return connection;
	}

	public void setConnection(int connection) {
		this.connection = connection;
	}
}
