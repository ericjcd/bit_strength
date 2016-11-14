package com.bit.strength.stress.network;

import com.bit.strength.stress.ViewUpdater;

public class NetTask {
	// network input
	NetArgs args;
	// network output
	ViewComponents component;

	private ViewUpdater updater;
	public NetTask(Object args, ViewComponents component, ViewUpdater updater) {
		
		super();
		this.args = (NetArgs) args;
		this.component = component;
		this.updater = updater;
	}
	
//	public NetTask(IPArgs args, ViewComponents component, ViewUpdater updater) {
//		super();
//		this.args = args;
//		this.component = component;
//		this.updater = updater;
//	}
//	
//	public NetTask(ARPArgs args, ViewComponents component, ViewUpdater updater) {
//		super();
//		this.args = args;
//		this.component = component;
//		this.updater = updater;
//	}
//	
//	public NetTask(ICMPArgs args, ViewComponents component, ViewUpdater updater) {
//		super();
//		this.args = args;
//		this.component = component;
//		this.updater = updater;
//	}

	public NetArgs getArgs() {
		return args;
	}

	public void setArgs(NetArgs args) {
		this.args = args;
	}

	public ViewComponents getComponent() {
		return component;
	}

	public void setComponent(ViewComponents component) {
		this.component = component;
	}

	public ViewUpdater getUpdater() {
		return updater;
	}

	public void setUpdater(ViewUpdater updater) {
		this.updater = updater;
	}
}
