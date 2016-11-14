package com.bit.strength.card;

import java.util.concurrent.locks.ReentrantLock;

import org.eclipse.swt.widgets.Display;
import org.jfree.data.general.DefaultValueDataset;
import org.jfree.data.time.Millisecond;

public class CardViewUpdater extends Thread {
	private CardViewComponent cv_components;
	
	private int rtc_data_val_input;
	private double rtc_data_val_6208;
	private int rtc_data_val_bst23208;
	private int rtc_data_val_3;
	private int rtc_data_val_4;
	private int rtc_data_val_5;

	private ReentrantLock lock = new ReentrantLock();
	
	public boolean exit;
	private boolean changed;
	
	public boolean isChanged() {
		return this.changed;
	}

	public CardViewUpdater(CardViewComponent components) {
		super();

		this.cv_components = components;
		this.rtc_data_val_input = 0;
		this.rtc_data_val_6208 = 0.0;
		this.rtc_data_val_bst23208 = 0;
		this.rtc_data_val_3 = 0;
		this.rtc_data_val_4 = 0;
		this.rtc_data_val_5 = 0;
		this.exit = false;
		this.changed = false;

		Millisecond now = new Millisecond();
		components.getMeterplot_1().setDataset(new DefaultValueDataset(0.0));
		components.getMeterplot_2().setDataset(new DefaultValueDataset(0.0));
//		components.getInputData().clear();
//		components.getInputData().addOrUpdate(now, 0.0);
		components.getrecData_6208().clear();
		components.getrecData_6208().addOrUpdate(now, 0.0);
		components.getrecData_bst23208().clear();
		components.getrecData_bst23208().addOrUpdate(now, 0.0);
		components.getRecData_3().clear();
		components.getRecData_3().addOrUpdate(now, 0.0);
	}

	public int update(/*int input_val,*/ double data_val_6208, int data_val_bst23208, int data_val3,
			int data_val4, int data_val5, int index) {
		if(this.changed){
			//rtc_data_val_input = input_val;
			rtc_data_val_6208 = data_val_6208;
			rtc_data_val_bst23208 = data_val_bst23208;
			rtc_data_val_3 = data_val3;
			rtc_data_val_4 = data_val4;
			rtc_data_val_5 = data_val5;
			this.changed = false;
			return index + 1;
		}
		return index;
	}
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		while (!exit) {
			try {
				Thread.sleep(1000);
//				if (!this.changed)
//					continue;
				
				lock.lock();
				Display.getDefault().syncExec(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						Millisecond now = new Millisecond();
						System.out.println(rtc_data_val_6208);
						//cv_components.getInputData().add(now, rtc_data_val_input);
						cv_components.getrecData_6208().add(now, rtc_data_val_6208);
						cv_components.getrecData_bst23208().add(now, rtc_data_val_bst23208);
						cv_components.getRecData_3().add(now, rtc_data_val_3);
						cv_components.getMeterplot_1().setDataset(new DefaultValueDataset(rtc_data_val_4));
						cv_components.getMeterplot_2().setDataset(new DefaultValueDataset(rtc_data_val_5));
						// update table
					}
				});
				//rtc_data_val_input = 0;
				rtc_data_val_6208 = 0.0;
				rtc_data_val_bst23208 = 0;
				rtc_data_val_3 = 0;
				rtc_data_val_4 = 0;
				rtc_data_val_5 = 0;
				this.changed = true;
				lock.unlock();
			}
			catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
