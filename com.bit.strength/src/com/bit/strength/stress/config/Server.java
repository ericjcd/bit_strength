package com.bit.strength.stress.config;

public class Server {
	private String iPString;
	private int port;

	public Server(String iPString, int port) {
		super();
		this.iPString = iPString;
		this.port = port;
	}

	public String getiPString() {
		return iPString;
	}

	public void setiPString(String iPString) {
		this.iPString = iPString;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getIPPortSlaveString() {
		return "rmi://" + iPString + ":" + port + "/Slave";
	}

	public String getIPPortMasterString() {
		return "rmi://" + iPString + ":" + port + "/Updater";
	}
}
