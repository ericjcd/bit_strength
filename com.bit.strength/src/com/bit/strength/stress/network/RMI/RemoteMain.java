package com.bit.strength.stress.network.RMI;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import com.bit.strength.stress.config.Server;

public class RemoteMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		if (args.length != 2) {
			System.out.println("RemoteMainIP port");
			return;
		}

		String ipString = args[0];
		int port = Integer.parseInt(args[1]);
		Server server = new Server(ipString, port);
		try {
			// 创建一个远程对象
			RemoteExecuterI executer = new RemoteExecuter();
			// LocateRegistry.createRegistry(8889);
			LocateRegistry.createRegistry(port);
			// 把远程对象注册到RMI注册服务器上，并命名为executer
			// Naming.bind("rmi://localhost:8889/Slave", executer);
			Naming.bind(server.getIPPortSlaveString(), executer);
			System.out.println(">>>>>INFO:远程executer对象绑定成功！");
		}
		catch (RemoteException e) {
			System.out.println("创建远程对象发生异常！");
			e.printStackTrace();
		}
		catch (AlreadyBoundException e) {
			System.out.println("发生重复绑定对象异常！");
			e.printStackTrace();
		}
		catch (MalformedURLException e) {
			System.out.println("发生URL畸形异常！");
			e.printStackTrace();
		}
	}
}
