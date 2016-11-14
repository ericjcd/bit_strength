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
			// ����һ��Զ�̶���
			RemoteExecuterI executer = new RemoteExecuter();
			// LocateRegistry.createRegistry(8889);
			LocateRegistry.createRegistry(port);
			// ��Զ�̶���ע�ᵽRMIע��������ϣ�������Ϊexecuter
			// Naming.bind("rmi://localhost:8889/Slave", executer);
			Naming.bind(server.getIPPortSlaveString(), executer);
			System.out.println(">>>>>INFO:Զ��executer����󶨳ɹ���");
		}
		catch (RemoteException e) {
			System.out.println("����Զ�̶������쳣��");
			e.printStackTrace();
		}
		catch (AlreadyBoundException e) {
			System.out.println("�����ظ��󶨶����쳣��");
			e.printStackTrace();
		}
		catch (MalformedURLException e) {
			System.out.println("����URL�����쳣��");
			e.printStackTrace();
		}
	}
}
