package com.bit.strength.capture;

import java.io.IOException;

import jpcap.JpcapCaptor;
import jpcap.PacketReceiver;

public class CaptureRunnable implements Runnable {

	private JpcapCaptor m_pcap;
	private PacketReceiver receiver;

	/*
	 * 配置过滤内容
	 */
	public CaptureRunnable(JpcapCaptor m_pcap, String filter,
			PacketReceiver receiver) {
		this.m_pcap = m_pcap;
		this.receiver = receiver;
		try {
			m_pcap.setFilter(filter, true);
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("invalid filter");
			e.printStackTrace();
		}
	}

	/*
	 * 多线程后台抓包
	 */
	@Override
	public void run() {
		m_pcap.loopPacket(-1, receiver);
	}
}
