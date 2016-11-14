package com.bit.strength.capture;

import java.util.ArrayList;

import jpcap.JpcapCaptor;
import jpcap.PacketReceiver;
import jpcap.packet.ICMPPacket;
import jpcap.packet.Packet;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Display;

import com.bit.strength.util.PacketCountPair;

public class PacketHandler implements PacketReceiver {
	// private static int m_counter = 0;

	private JpcapCaptor m_pcap;

	private TableViewer viewer;
	private Object owner;

	public PacketHandler(TableViewer viewer, Object owner, JpcapCaptor m_pcap) {
		this.viewer = viewer;
		this.owner = owner;
		this.m_pcap = m_pcap;
	}

	/*
	 * 回调更新tableview 根据docapture标记判定是否停止抓取
	 */
	@Override
	public void receivePacket(Packet packet) {
		// TODO Auto-generated method stub
		// if (packet instanceof ICMPPacket) {
		// ICMPPacket icmp = (ICMPPacket) packet;
		// System.out.println("icmp: " + icmp);
		// }else {
		// System.out.println("other: " + packet);
		// }
		if (packet == null)
			return;
		if (owner instanceof Capture) {
			Capture capture = (Capture) owner;
			if ((!capture.getCapture()) && (m_pcap != null))
				m_pcap.breakLoop();
		}

		Display.getDefault().syncExec(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				ArrayList<PacketCountPair> list = (ArrayList<PacketCountPair>) viewer
						.getInput();
				int cur = 0;
				if (list.size() == 0)
					cur = 0;
				else {
					cur = list.get(list.size() - 1).getCount() + 1;
				}
				list.add(new PacketCountPair(packet, cur));
				/*
				 * if (list.size() > 100) { for (int i = 0; i < 10; i++)
				 * list.remove(i); }
				 */
				viewer.refresh();
			}
		});
	}
}
