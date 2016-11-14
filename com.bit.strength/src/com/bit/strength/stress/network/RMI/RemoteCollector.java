package com.bit.strength.stress.network.RMI;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.concurrent.locks.ReentrantLock;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TableItem;
import org.jfree.data.time.Millisecond;

public class RemoteCollector extends Thread {

	private int cnt;
	private long delay;
	private int sendCount;
	private int sendByteCount;
	private int receiveCount;
	private int receiveByteCount;
	private ReentrantLock lock = new ReentrantLock();

	private String updaterName;
	LocalUpdaterI updater;
	private boolean changed;
	public boolean exit;

	public RemoteCollector(String updaterName) throws MalformedURLException,
			RemoteException, NotBoundException {
		super();
		this.updaterName = updaterName;
		this.cnt = 0;
		this.delay = 0;
		this.sendCount = 0;
		this.sendByteCount = 0;
		this.receiveCount = 0;
		this.receiveByteCount = 0;
		this.exit = false;
		this.updater = (LocalUpdaterI) Naming.lookup(updaterName);
	}

	// add RMI receive
	public void update(long delayUpdate, int sendUpdate, int sendByteUpdate,
			int receiveUpdate, int receiveByteUpdate) {
		lock.lock();
		cnt++;
		delay += delayUpdate;
		sendCount += sendUpdate;
		sendByteCount += sendByteUpdate;
		receiveCount += receiveUpdate;
		receiveByteCount += receiveByteUpdate;
		changed = true;
		lock.unlock();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		while (!exit) {
			try {
				Thread.sleep(1000);
				if (!changed)
					continue;
				lock.lock();
				updater.update(delay / cnt, sendCount, sendByteCount,
						receiveCount, receiveByteCount);
				cnt = 0;
				delay = 0;
				sendCount = 0;
				sendByteCount = 0;
				receiveCount = 0;
				receiveByteCount = 0;
				changed = false;
				lock.unlock();
			}
			catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("remote collector exited");
	}

	public String getUpdaterName() {
		return updaterName;
	}

	public void setUpdaterName(String updaterName) {
		this.updaterName = updaterName;
	}
}