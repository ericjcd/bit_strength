package com.bit.strength.util;

import java.beans.PropertyVetoException;
import java.io.BufferedReader;
import java.net.DatagramPacket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.sql.Connection;

import org.apache.commons.dbutils.QueryRunner;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import jpcap.packet.ARPPacket;
import jpcap.packet.ICMPPacket;

public class DataStorage {
	private final static String driverClassName = "com.mysql.jdbc.Driver";
	private final static String jdbc_url = "jdbc:mysql://localhost:3306/bit_strength?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true";
	private final static String jdbc_username = "root";
	private final static String jdbc_password = "bitStrengthDB@)!^";
	private final static int jdbc_initPoolSize = 5;
	private final static int jdbc_maxPoolSize = 10;
	// c3p0 connection pool
	private ComboPooledDataSource c3p0ConnectionPool = new ComboPooledDataSource();
	// connection
	private Connection connection;
	{
		try {
			c3p0ConnectionPool.setDriverClass(driverClassName);
			c3p0ConnectionPool.setUser(jdbc_username);
			c3p0ConnectionPool.setPassword(jdbc_password);
			c3p0ConnectionPool.setInitialPoolSize(jdbc_initPoolSize);
			c3p0ConnectionPool.setMaxPoolSize(jdbc_maxPoolSize);
			c3p0ConnectionPool.setJdbcUrl(jdbc_url);
			connection = c3p0ConnectionPool.getConnection();
		} catch (PropertyVetoException e) {
			// TODO Auto-generated catch block
			System.out.println("c3p0ConnectionPool failed to initialize..");
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out
					.println("jdbc connection failed to getConnection from c3p0ConnectionPool..");
			e.printStackTrace();
		}
	}

	// common-dbUtils - sql class: query, delete, update....
	/*
	 * as initialized by combining with DataSource, so we can just only use
	 * funcion update(String sql, Object params), etc. but update(Connection
	 * conn, String sql, Object params), etc.
	 * ------------------------------------
	 * --------------------------------------- And there's no need to close
	 * connection cause QueryRunner has done this work.
	 */
	private QueryRunner queryRunner = new QueryRunner(c3p0ConnectionPool);

	public DataStorage() {
		super();
	}

	String sql = "insert into ";

	// Save UDP received data
	public int storageDataAtLocal(int type, DatagramPacket udpPacket) {
		if (type != UDP) {
			return 0;
		}
		sql += "UDPData_Table(ip, ports, messages) values(?,?,?);";
		Object[] param = { udpPacket.getAddress().toString(),
				String.valueOf(udpPacket.getPort()),
				udpPacket.getData().toString()
				};
		
		try {
			queryRunner.update(sql, param);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Failed to update udp_Data to DB");
			e.printStackTrace();
		}
		return 1;
	}

	// Save TCP received data
	public int storageDataAtLocal(int type, InetAddress addr, int port, byte[] data, String recData) {
		if (type != TCP) {
			return 0;
		}
		sql += "TCPData_Table(ip, ports, messages, recData) values(?,?,?,?);";
		Object[] param = { addr.toString(), String.valueOf(port), data.toString(), recData };
		
		try {
			queryRunner.update(sql, param);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Failed to update tcp_Data to DB");
			e.printStackTrace();
		}
		return 1;
	}

	// Save ARP received data
	public int storageDataAtLocal(int type, ARPPacket arpReplyPacket) {
		if (type != ARP) {
			return 0;
		}
		sql += "ARPData_Table(sender_protoaddr, sender_hardaddr, target_protoaddr, target_hardaddr, hard_type, protocol_type, hlen, plen, captor_len, operation) values(?,?,?,?,?,?,?,?,?,?);";
		Object[] param = { arpReplyPacket.getTargetProtocolAddress().toString(),
				arpReplyPacket.getTargetHardwareAddress().toString(),
				arpReplyPacket.getSenderProtocolAddress().toString(),
				arpReplyPacket.getSenderHardwareAddress().toString(),
				"HARDTYPE_ETHER", "PROTOTYPE_IP", "6", "4",
				String.valueOf(arpReplyPacket.caplen), "REQUEST" };
		
		try {
			queryRunner.update(sql, param);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Failed to update arp_Data to DB");
			e.printStackTrace();
		}
		return 1;
	}

	// Save ICMP received data
//	public void storageDataAtLocal(int type, ICMPPacket icmpPacket) {
//
//	}
	public int storageDataAtLocal(int type, ICMPPacket icmpPacket) {
		if (type != ICMP) {
			return 0;
		}
		sql += "ICMPData_Table(type, id, seq, orig_timestamp, data) values(?,?,?,?,?);";
		Object[] param = { String.valueOf(icmpPacket.type),
				String.valueOf(icmpPacket.id),
				String.valueOf(icmpPacket.seq),
				String.valueOf(icmpPacket.orig_timestamp),
				icmpPacket.data.toString() };
		
		try {
			queryRunner.update(sql, param);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Failed to update icmp_Data to DB");
			e.printStackTrace();
		}
		return 1;
	}

	public final static int UDP = 0;
	public final static int TCP = 1;
	public final static int ARP = 2;
	public final static int ICMP = 3;

	// test main
	public static void main(String[] args) throws UnknownHostException {
		ARPPacket packet = new ARPPacket();
		DataStorage ds = new DataStorage();
		byte[] broadcast = new byte[] { (byte) 255, (byte) 255, (byte) 255,
				(byte) 255, (byte) 255, (byte) 255 };

		packet.sender_hardaddr = broadcast;
		// packet.sender_protoaddr = sender_ip_;
		packet.sender_protoaddr = (Inet4Address.getByName("192.168.1.1"))
				.getAddress();
		packet.target_hardaddr = broadcast;
		packet.target_protoaddr = (Inet4Address.getByName("192.168.1.2"))
				.getAddress();
		packet.hardtype = ARPPacket.HARDTYPE_ETHER;
		packet.prototype = ARPPacket.PROTOTYPE_IP;
		packet.operation = ARPPacket.ARP_REQUEST;
		packet.hlen = 6;
		packet.plen = 4;

//		ds.setARPData(0, packet);
		ds.storageDataAtLocal(ARP, packet);
		
		 
	}
}
