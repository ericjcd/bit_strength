package com.bit.strength.stress;

import java.util.concurrent.locks.ReentrantLock;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TableItem;
import org.jfree.data.time.Millisecond;

import com.bit.strength.stress.network.ViewComponents;

public class ViewUpdater extends Thread {

	private ViewComponents components;

	private int sendCount;
	private int sendByteCount;
	private int receiveCount;
	private int receiveByteCount;

	private ReentrantLock lock = new ReentrantLock();

	private boolean changed;

	public boolean exit;

	public ViewUpdater(ViewComponents components) {
		super();

		this.components = components;
		this.sendCount = 0;
		this.sendByteCount = 0;
		this.receiveCount = 0;
		this.receiveByteCount = 0;
		this.exit = false;

		Millisecond now = new Millisecond();
		components.getSendSeries().clear();
		components.getSendSeries().addOrUpdate(now, 0.5);
		components.getReceiveSeries().clear();
		components.getReceiveSeries().addOrUpdate(now, 0.5);
		components.getSendByteSeries().clear();
		components.getSendByteSeries().addOrUpdate(now, 0.5);
		components.getReceiveByteSeries().clear();
		components.getReceiveByteSeries().addOrUpdate(now, 0.5);

		components.getTable_log().clearAll();
	}

	// add RMI receive
	public void update(long delay, int sendUpdate, int sendByteUpdate,
			int receiveUpdate, int receiveByteUpdate) {
		lock.lock();
		sendCount += sendUpdate;
		sendByteCount += sendByteUpdate;
		receiveCount += receiveUpdate;
		receiveByteCount += receiveByteUpdate;
		changed = true;
		lock.unlock();
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				TableItem item = new TableItem(components.getTable_log(),
						SWT.NONE, 0);
				item.setText(new String[] { Long.toString(delay) + "ms",
						Integer.toString(receiveUpdate),
						Integer.toString(sendUpdate),
						Integer.toString(receiveByteUpdate),
						Integer.toString(sendByteUpdate) });
				if (components.getTable_log().getItems().length > 100) {
					components.getTable_log().remove(100);
				}
				// update table
			}
		});

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		while (!exit) {
			try {
				Thread.sleep(1000);
				//if (!changed)
					//continue;
				lock.lock();
				Display.getDefault().syncExec(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						Millisecond now = new Millisecond();
						components.getSendSeries().add(now, sendCount);
						components.getReceiveSeries().add(now, receiveCount);
						components.getSendByteSeries().add(now, sendByteCount);
						components.getReceiveByteSeries().add(now,
								receiveByteCount);
						System.out.println("Now = " + now.toString() + " have "
								+ sendCount + " " + receiveCount + " "
								+ sendByteCount + " " + receiveByteCount);
						// update table
					}
				});
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
		}
	}
}
