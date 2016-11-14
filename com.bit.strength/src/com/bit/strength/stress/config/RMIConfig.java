package com.bit.strength.stress.config;

import java.util.ArrayList;

public class RMIConfig {
	private Server master;
	private ArrayList<Server> slave;
	private static RMIConfig config = null;

	private RMIConfig() {
		// TODO Auto-generated constructor stub
		// rmi://localhost:8888/Updater
		master = new Server("localhost", 8888);
		slave = new ArrayList<>();
		// slave.add("rmi://localhost:8889/Slave");
	}

	public Server getMaster() {
		return master;
	}

	public void setMaster(Server server) {
		this.master = server;
	}

	public int getMasterPort() {
		return master.getPort();
	}

	public void setMasterPort(int serverPort) {
		this.master.setPort(serverPort);
	}

	public ArrayList<Server> getSlave() {
		return slave;
	}

	public void setSlave(ArrayList<Server> slave) {
		this.slave = slave;
	}

	public void addSlave(Server s) {
		this.slave.add(s);
	}

	public static RMIConfig getConfig() {
		if (config == null) {
			config = new RMIConfig();
		}
		return config;
	}

}
