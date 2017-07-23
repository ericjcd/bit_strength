package com.bit.strength.util;

import java.io.UnsupportedEncodingException;

import jpcap.packet.DatalinkPacket;
import jpcap.packet.EthernetPacket;
import jpcap.packet.IPPacket;
import jpcap.packet.Packet;
import jpcap.packet.TCPPacket;
import jpcap.packet.UDPPacket;
/**
 * 
 * @author Ericj
 * 解析抓的数据包
 *
 */
public class PacketDecode {
	/**
	 * 
	 * @param packet
	 * @return
	 * 获取目标机端口
	 */
	public static String getDesPort(Packet packet) {
		if (packet instanceof TCPPacket) {
			TCPPacket tcpPacket = (TCPPacket) packet;
			return Integer.toString(tcpPacket.dst_port);
		}
		if (packet instanceof UDPPacket) {
			UDPPacket udpPacket = (UDPPacket) packet;
			return Integer.toString(udpPacket.dst_port);
		}
		return "";
	}
	/**
	 * 
	 * @param packet
	 * @return
	 * 获取源机器端口
	 */
	public static String getSrcPort(Packet packet) {
		if (packet instanceof TCPPacket) {
			TCPPacket tcpPacket = (TCPPacket) packet;
			return Integer.toString(tcpPacket.src_port);
		}
		if (packet instanceof UDPPacket) {
			UDPPacket udpPacket = (UDPPacket) packet;
			return Integer.toString(udpPacket.src_port);
		}
		return "";
	}
	/**
	 * 
	 * @param packet
	 * @return
	 * 获取源机器IP地址
	 */
	public static String getSrcIP(Packet packet) {
		if (packet instanceof IPPacket) {
			IPPacket ipPacket = (IPPacket) packet;
			return ipPacket.src_ip.getHostAddress();
		} else
			return "";
	}
	/**
	 * 
	 * @param packet
	 * @return
	 * 获取目标机IP地址
	 */
	public static String getDesIP(Packet packet) {
		if (packet instanceof IPPacket) {
			IPPacket ipPacket = (IPPacket) packet;
			return ipPacket.dst_ip.getHostAddress();
		} else
			return "";
	}
	/**
	 * 
	 * @param packet
	 * @return
	 * 获取源机器Mac地址
	 */
	public static String getSrcMAC(Packet packet) {
		DatalinkPacket datalinkPacket = packet.datalink;
		if (datalinkPacket instanceof EthernetPacket) {
			EthernetPacket ethPacket = (EthernetPacket) datalinkPacket;
			return ethPacket.getSourceAddress();
		} else
			return "";
	}
	/**
	 * 
	 * @param packet
	 * @return
	 * 获取目标机Mac地址
	 */
	public static String getDesMAC(Packet packet) {
		DatalinkPacket datalinkPacket = packet.datalink;
		if (datalinkPacket instanceof EthernetPacket) {
			EthernetPacket ethPacket = (EthernetPacket) datalinkPacket;
			return ethPacket.getDestinationAddress();
		} else
			return "";
	}
	/**
	 * 
	 * @param packet
	 * @return
	 * 获取数据包类型
	 */
	public static String getType(Packet packet) {
		String type = packet.getClass().getName();
		return type.substring(type.lastIndexOf(".") + 1);
	}
	/**
	 * 
	 * @param packet
	 * @return
	 * 获取包内容
	 */
	public static String getContent(Packet packet) {
		// return ByteHex.byte2HexStr(packet.getData());
		try {
			return new String(packet.data, "UTF-8");
		}
		catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new String(packet.data);
	}
	/**
	 * 
	 * @param packet
	 * @return
	 * 获取包内容的16进制显示
	 */
	public static String getHexContent(Packet packet) {
		return ByteHex.toHexStr(packet.data);
	}
	/**
	 * 
	 * @param packet
	 * @return
	 * 获取时间（微秒）
	 */
	public static String getTime(Packet packet) {
		return Long.toString(packet.usec);
	}
}
