package com.bit.strength.stress.network.RMI;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import com.bit.strength.stress.ViewUpdater;
import com.bit.strength.stress.config.RMIConfig;
import com.bit.strength.stress.network.NetTask;

public class LocalController {

	private NetTask netTask;

	public LocalController(NetTask netTask) {
		super();
		this.netTask = netTask;
	}

	public void start() {
		Thread.currentThread().setContextClassLoader(
				this.getClass().getClassLoader());
		LocalUpdater updater = LocalUpdater.getUpdater();
		updater.setViewupdater(netTask.getUpdater());
		updater.setController(this);
		// start execute
		for (int i = 0; i < RMIConfig.getConfig().getSlave().size(); i++) {
			String slaveString = RMIConfig.getConfig().getSlave().get(i)
					.getIPPortSlaveString();
			RemoteExecuterI executer;
			try {
				executer = (RemoteExecuterI) Naming.lookup(slaveString);
				executer.execute(netTask.getArgs(), RMIConfig.getConfig()
						.getMaster().getIPPortMasterString());
			}
			catch (MalformedURLException e) {
				System.out.println("发生URL畸形异常！");
				e.printStackTrace();
			}
			catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (NotBoundException e) {
				System.out.println("找不到绑定的远程对象！");
				e.printStackTrace();
			}
		}
	}

	public void stop() {
		for (int i = 0; i < RMIConfig.getConfig().getSlave().size(); i++) {
			String slaveString = RMIConfig.getConfig().getSlave().get(i)
					.getIPPortSlaveString();
			RemoteExecuterI executer;
			try {
				executer = (RemoteExecuterI) Naming.lookup(slaveString);
				executer.stop();
			}
			catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				System.out.println("发生URL畸形异常！");
				e.printStackTrace();
			}
			catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (NotBoundException e) {
				// TODO Auto-generated catch block
				System.out.println("找不到绑定的远程对象！");
				e.printStackTrace();
			}
		}
		ViewUpdater updater = netTask.getUpdater();
		if (updater != null)
			updater.exit = true;
	}
}
