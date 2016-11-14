package com.bit.strength.stress.network;

import com.bit.strength.stress.network.RMI.RemoteCollector;
import com.bit.strength.util.DataStorage;

public abstract class NetRunnable implements Runnable {
	protected RemoteCollector collector;
	public boolean exit;
	public DataStorage dataStorage;
}
