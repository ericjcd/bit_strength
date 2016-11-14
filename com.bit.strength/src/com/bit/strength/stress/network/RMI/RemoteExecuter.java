package com.bit.strength.stress.network.RMI;

import java.net.MalformedURLException;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import com.bit.strength.stress.network.ARPArgs;
import com.bit.strength.stress.network.ARPRunnable;
import com.bit.strength.stress.network.ICMPArgs;
import com.bit.strength.stress.network.ICMPRunnable;
import com.bit.strength.stress.network.NetArgs;
import com.bit.strength.stress.network.IPArgs;
import com.bit.strength.stress.network.IPRunnable;
import com.bit.strength.stress.network.NetRunnable;
import com.bit.strength.stress.network.TCPRunnable;
import com.bit.strength.stress.network.UDPRunnable;

public class RemoteExecuter extends UnicastRemoteObject implements
		RemoteExecuterI {

	private ArrayList<Thread> threadPool;
	private ArrayList<NetRunnable> runPool;
	private RemoteCollector collector = null;

	public RemoteExecuter() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
		threadPool = new ArrayList<>();
		runPool = new ArrayList<>();
	}

	private void send(NetArgs args, int con) {
		// start TCP/UDP thread
		for (int i = 0; i < con; i++) {
			NetRunnable runnable = null;
			if (args instanceof IPArgs) {
				IPArgs ipArgs = (IPArgs) args;
				if (args.getType() == NetArgs.TCP) {
					runnable = new TCPRunnable(ipArgs, collector);
				} else if (args.getType() == NetArgs.UDP) {
					runnable = new UDPRunnable(ipArgs, collector);
				}
			}else{
				if (args instanceof ICMPArgs) {
					ICMPArgs icmpArgs = (ICMPArgs) args;
					runnable = new ICMPRunnable(icmpArgs, collector);
					
				}else if (args instanceof ARPArgs) {
//					System.out.println("______________RemoteExecuter---send arp");
					ARPArgs arpArgs = (ARPArgs) args;
					runnable = new ARPRunnable(arpArgs, collector);
				}
			}
			if(runnable!=null){
				runPool.add(runnable);
				Thread thread = new Thread(runnable);
				threadPool.add(thread);
				thread.start();
			}
		}
	}

	@Override
	public void execute(NetArgs args, String server) throws RemoteException {
		// TODO Auto-generated method stub
		// start collector thread
		System.out.println("remote task executed");
		try {
			collector = new RemoteCollector(server);
		}
		catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			System.out.println("发生URL畸形异常！");
			e.printStackTrace();
		}
		catch (NotBoundException e) {
			// TODO Auto-generated catch block
			System.out.println("找不到绑定的远程对象！");
			e.printStackTrace();
		}
		collector.exit = false;
		collector.start();
		
		threadPool.clear();
		runPool.clear();
		int con = args.getConnection();
		
		// start send thread
		send(args, con);
	}

	@Override
	public void stop() throws RemoteException {
		// TODO Auto-generated method stub
		System.out.println("remote stop called");
		for (int i = 0; i < runPool.size(); i++) {
			runPool.get(i).exit = true;
		}
		runPool.clear();
		threadPool.clear();
		System.out.println("thread stopped");
		if (collector != null) {
			collector.exit = true;
		}
		System.out.println("collector stopped");
	}

	@Override
	public void exit() throws RemoteException {
		// TODO Auto-generated method stub
		// kill binding
		try {
			UnicastRemoteObject.unexportObject(this, true);
		}
		catch (NoSuchObjectException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
