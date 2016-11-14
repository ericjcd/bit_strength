package com.bit.strength.stress.network.RMI;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

import com.bit.strength.stress.ViewUpdater;
import com.bit.strength.stress.config.RMIConfig;

public class LocalUpdater extends UnicastRemoteObject implements LocalUpdaterI {
	private static LocalUpdaterI remoteUpdater = null;

	private ViewUpdater viewUpdater = null;
	private LocalController controller = null;

	private LocalUpdater() throws RemoteException {
		super();
	}

	public static LocalUpdater getUpdater() {
		if (remoteUpdater == null) {
			try {
				remoteUpdater = new LocalUpdater();
				LocateRegistry.createRegistry(RMIConfig.getConfig()
						.getMasterPort());
				Naming.bind(RMIConfig.getConfig().getMaster()
						.getIPPortMasterString(), remoteUpdater);
			}
			catch (RemoteException e) {
				System.out.println("创建远程对象发生异常！");
				e.printStackTrace();
			}
			catch (MalformedURLException e) {
				System.out.println("发生URL畸形异常！");
				e.printStackTrace();
			}
			catch (AlreadyBoundException e) {
				System.out.println("发生重复绑定对象异常！");
				e.printStackTrace();
			}
		}
		return (LocalUpdater) remoteUpdater;
	}

	@Override
	public void update(long delay, int sendUpdate, int sendByteUpdate,
			int receiveUpdate, int receiveByteUpdate) throws RemoteException {
		if (viewUpdater == null)
			return;
		viewUpdater.update(delay, sendUpdate, sendByteUpdate, receiveUpdate,
				receiveByteUpdate);
	}

	@Override
	public void stop() throws RemoteException {
		// TODO Auto-generated method stub
	}

	@Override
	public void exit() throws RemoteException {
		// TODO Auto-generated method stub
		try {
			UnicastRemoteObject.unexportObject(this, true);
		}
		catch (NoSuchObjectException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public ViewUpdater getViewupdater() {
		return viewUpdater;
	}

	public void setViewupdater(ViewUpdater viewupdater) {
		this.viewUpdater = viewupdater;
	}

	public LocalController getController() {
		return controller;
	}

	public void setController(LocalController controller) {
		this.controller = controller;
	}

}
