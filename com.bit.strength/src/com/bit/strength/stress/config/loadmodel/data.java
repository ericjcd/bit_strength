package com.bit.strength.stress.config.loadmodel;

public class data {
	private byte[] riphead = new byte[4];
	private byte[][] ripcontent;
	private byte[] arphead = new byte[8];
	private byte[] arpcontent = new byte[20];
	private byte[] Ospfhead = new byte[24];
	private byte[] OspfHellocontent = new byte[16];
	private byte[] OspfDDcontent = new byte[8];
	private byte[] Icmp = new byte[64];

	public void RipAddHead(byte[] inHead) {
		riphead = inHead;
	}

	public void RipAddContent(byte[][] inContent) {

		int num = inContent.length;
		ripcontent = new byte[num][20];
		for (int i = 0; i < num; i++)
			ripcontent[i] = inContent[i];
	}

	public byte[] GetRip() {
		int num = ripcontent.length;
		byte[] rip = new byte[20 * num + 4];
		for (int i = 0; i < 4; i++)
			rip[i] = riphead[i];
		int count = 4;
		for (int i = 0; i < num; i++) {
			for (int j = 0; j < 20; j++) {
				rip[count] = ripcontent[i][j];
				count++;
			}
		}
		 for (int i = 0; i < 20 * num + 4; i++)
		 System.out.print(rip[i] + " ");
		 System.out.println("");
		return rip;
	}

	public void ArpAddHead(byte[] inHead) {
		arphead = inHead;
	}

	public void ArpAddContent(byte[] inContent) {
		arpcontent = inContent;
	}

	public byte[] GetArp() {
		byte[] arp = new byte[28];
		for (int i = 0; i < 8; i++)
			arp[i] = arphead[i];
		for (int j = 0; j < 20; j++)
			arp[j + 8] = arpcontent[j];
		// for (int i = 0; i < 28; i++)
		// System.out.print(arp[i] + " ");
		// System.out.println("");
		return arp;
	}

	public void OspfAddHead(byte[] inHead) {
		Ospfhead = inHead;
	}

	public void OspfHelloAddContent(byte[] inContent) {
		OspfHellocontent = inContent;
	}

	public void OspfDDAddContent(byte[] inContent) {
		OspfDDcontent = inContent;
	}

	public byte[] GetOspfHello() {
		byte[] Ospf = new byte[40];
		for (int i = 0; i < 24; i++)
			Ospf[i] = Ospfhead[i];
		for (int j = 0; j < 16; j++)
			Ospf[j + 24] = OspfHellocontent[j];
		for (int i = 0; i < 40; i++)
			System.out.print(Ospf[i] + " ");
		System.out.println("");
		return Ospf;
	}

	public byte[] GetOspfDD() {
		byte[] Ospf = new byte[32];
		for (int i = 0; i < 24; i++)
			Ospf[i] = Ospfhead[i];
		for (int j = 0; j < 8; j++)
			Ospf[j + 24] = OspfDDcontent[j];
		for (int i = 0; i < 32; i++)
			System.out.print(Ospf[i] + " ");
		System.out.println("");
		return Ospf;
	}
	public void IcmpAdd(byte[] icmp) {
		Icmp = icmp;
	}
	public byte[] GetIcmp() {
		for (int i = 0; i < 64; i++)
			System.out.print(Icmp[i] + " ");
		System.out.println("");
		return Icmp;
	}

}
