package com.bit.strength.stress.network.RMI;

import java.rmi.Remote;
import java.rmi.RemoteException;

import com.bit.strength.stress.network.NetArgs;
import com.bit.strength.stress.network.IPArgs;

public interface RemoteExecuterI extends Remote {
	public void execute(NetArgs args, String server) throws RemoteException;

	public void stop() throws RemoteException;

	public void exit() throws RemoteException;
}
