package com.bit.strength.stress.network.RMI;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface LocalUpdaterI extends Remote {
	public void update(long delay, int sendUpdate, int sendByteUpdate,
			int receiveUpdate, int receiveByteUpdate) throws RemoteException;

	public void stop() throws RemoteException;

	public void exit() throws RemoteException;
}
