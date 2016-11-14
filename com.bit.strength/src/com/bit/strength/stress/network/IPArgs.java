package com.bit.strength.stress.network;

import java.util.ArrayList;

public class IPArgs extends NetArgs {
	// network input
	private String ip;
	private int port;
	private int localPort;
	private ArrayList<Message> messages;

	public IPArgs(int type, int connection, String ip, int port, int localPort,
			ArrayList<Message> messages) {
		super();
		this.type = type;
		this.connection = connection;
		this.ip = ip;
		this.port = port;
		this.localPort = localPort;
		this.messages = messages;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getLocalPort() {
		return localPort;
	}

	public void setLocalPort(int localPort) {
		this.localPort = localPort;
	}

	public ArrayList<Message> getMessages() {
		return messages;
	}

	public void setMessages(ArrayList<Message> messages) {
		this.messages = messages;
	}
}
