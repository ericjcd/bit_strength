package com.bit.strength.stress.config.loadmodel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

public class commonmethod {
	public JTable AddHeadTable(String[] name, String[] data, String[][] Item,
			final int inCol) {
		JTable HeadTable;
		String[] HeadTitle = name;
		String[] Data = data;
		String[][] HeadData = { Data };
		DefaultTableModel t1model = new DefaultTableModel(HeadData, HeadTitle) {
			public boolean isCellEditable(int row, int col) {
				if (inCol == 102){
					if (col == 5 || col < 2)
						return false;
					else 
						return true;
				} else{
					if (col < inCol)
						return false;
					else
						return true;
				}
			}
		};
		HeadTable = new JTable(t1model);
		HeadTable.setEditingColumn(0);
		HeadTable.setCellSelectionEnabled(true);
		HeadTable.setColumnSelectionAllowed(true);
		HeadTable.getColumnModel().getColumn(0).setWidth(30);
		HeadTable.setBorder(new LineBorder(new Color(0, 0, 0)));
		HeadTable.setSize(new Dimension(1, 1));
		HeadTable.setBounds(6, 23, 557, 48);
		HeadTable.getTableHeader().setReorderingAllowed( false ) ;
		for (int i = 0; i < Item.length; i++) {
			JComboBox jc = new JComboBox();
			for (int j = 1; j < Item[i].length; j++)
				jc.addItem(Item[i][j]);
			HeadTable.getColumnModel().getColumn(Integer.valueOf(Item[i][0]))
					.setCellEditor(new DefaultCellEditor(jc));
		}

		return HeadTable;

	}

	private byte charToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}

	public byte[] hexStringToBytes(String hexString) {
		if (hexString == null || hexString.equals("")) {
			return null;
		}
		hexString = hexString.toUpperCase();
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
		}
		return d;
	}

	public byte[] StrToByte(int type, String addr) {
		String split[];
		byte IpAddr[] = new byte[4];
		byte MacAddr[][] = new byte[6][2];
		byte ret[];
		if (type == 0) {
			split = new String[4];
			split = addr.split("\\.");
			for (int i = 0; i < 4; i++) {
				int x = Integer.parseInt(split[i]);
				IpAddr[i] = (byte) x;
			}
			ret = new byte[4];
			ret = IpAddr;
		} else if (type == 1) {
			split = new String[6];
			split = addr.split("-");
			for (int i = 0; i < 6; i++)
				MacAddr[i] = hexStringToBytes(split[i]);
			ret = new byte[12];
			for (int i = 0; i < 6; i++)
				ret[i] = MacAddr[i][0];
		} else {
			ret = new byte[0];
		}
		return ret;
	}

	public boolean DisCheck(String dis) {
		Pattern pattern = Pattern.compile("^[0-9]+(.[0-9]*)?$");
		Matcher match = pattern.matcher(dis);
		return match.matches();
	}

	public boolean AddrCheck(int type, String check) {
		Boolean ipAddress = null;
		String com = null;
		if (type == 0) {
			if (check.length() < 7 || check.length() > 15 || "".equals(check)) {
				return false;
			}
			/**
			 * 判断IP格式和范围
			 */
			String rexpIp = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
			com = rexpIp;
		}
		if (type == 1) {
			if (check.length() != 17) {
				return false;
			}
			/**
			 * 判断MAC格式和范围
			 */
			String rexpMac = "^[A-F0-9]{2}(-[A-F0-9]{2}){5}$";
			com = rexpMac;
		}
		Pattern pat = Pattern.compile(com);
		Matcher mat = pat.matcher(check);
		ipAddress = mat.find();
		return ipAddress;
	}

	public JTable AddOspHead(String headtype) {
		int type = Integer.valueOf(headtype.substring(0, 1));
		String[] t1name = { "版本(1)", "类型(1)",  "分组长度(2)", "路由器ID(4)", "校验和(2)","区域ID(4)",
				"鉴别类型(2)", "鉴别(8)" };
		String[] t1data1 = { "4", headtype, "1", "192.168.0.1", "10.0.0.1",
				type == 1 ? "0x5D96" : "0xA04D", "0(不认证)", "" };
		String[] item = { "6", "0(不认证)", "1(简单认证)", "2(MD5认证)" };
		String[][] items = { item };
		JTable jt = AddHeadTable(t1name, t1data1, items, 102);
		return jt;
	}

	public byte[] CheckOspfHead(DefaultTableModel table1Model) {
		int flag = 0;
		byte[] head = new byte[24];
		head[0] = 4; // 版本
		head[1] = 1; // 类型

		String length = table1Model.getValueAt(0, 2).toString();
		// int length = Integer.valueOf(table1Model.getValueAt(0,
		// 2).toString());
		if (DisCheck(length) && Integer.valueOf(length) < 256
				&& Integer.valueOf(length) > 0) {
			head[2] = Byte.valueOf((Integer.valueOf(length) / 16) + "");
			head[3] = Byte.valueOf((Integer.valueOf(length) % 16) + ""); // 分组长度
		} else {
			table1Model.setValueAt("wrong", 0, 2);
			flag = 1;
		}
		if (AddrCheck(0, table1Model.getValueAt(0, 3).toString())
				&& AddrCheck(0, table1Model.getValueAt(0, 4).toString())) {
			byte[] ipaddr = new byte[4];
			ipaddr = StrToByte(0, table1Model.getValueAt(0, 3).toString());
			head[4] = ipaddr[0];
			head[5] = ipaddr[1];
			head[6] = ipaddr[2];
			head[7] = ipaddr[3];
			ipaddr = StrToByte(0, table1Model.getValueAt(0, 4).toString());

			head[8] = ipaddr[0];
			head[9] = ipaddr[1];
			head[10] = ipaddr[2];
			head[11] = ipaddr[3];
			System.out.println(head[8]);
		} else {
			table1Model.setValueAt("wrong", 0, 3);
			table1Model.setValueAt("wrong", 0, 4);
			flag = 1;
		}
		// 校验和
		if (!table1Model.getValueAt(0, 5).toString().equals("wrong")) {
			String ck = table1Model.getValueAt(0, 5).toString().substring(2);
			byte[] ckn = new byte[2];
			ckn = hexStringToBytes(ck);
			head[12] = ckn[0];
			head[13] = ckn[1];
		} else {
			head[12] = 0;
			head[13] = 0;
			flag = 1;
		}

		/*
		 * 检验和字段 head[12] = 0; head[13] = 0;
		 */
		head[14] = 0;
		head[15] = Byte.valueOf(table1Model.getValueAt(0, 6).toString()
				.substring(0, 1));
		byte b[] = table1Model.getValueAt(0, 7).toString().getBytes();
		for (int i = 0; i < 8; i++) {
			if (i < b.length)
				head[23 - i] = b[b.length - i - 1];
			else
				head[23 - i] = 0;
		}
		if (flag == 1) {
			byte[] wrong = new byte[1];
			wrong[0] = 0;// ospf hello 回传错误
			return wrong;
		} else {
			return head;
		}

	}

	public void ospfOption(final JTable t) {
		t.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(MouseEvent e) {// 仅当鼠标单击时响应
				// 得到选中的行列的索引值
				int c = t.getSelectedColumn();
				// 得到选中的单元格的值，表格中都是字符串
				if (c == 2) {
					optionShow newOption = new optionShow();
					newOption.optionShow(t);
				}
			}
		});
	}

	public long sumCheck(byte[] message, int size) {
		long cksum = 0;
		int i = 0;
		for (i = 0; i < size; i = i + 2) {
			long temp = 0;
			if (message[i] < 0)
				temp += message[i] + 256;
			else
				temp += message[i];
			temp = temp * 256;
			if (message[i + 1] < 0)
				temp += message[i + 1] + 256;
			else
				temp += message[i + 1];
			cksum += temp;

		}// 将32位数转换成16

		if (i != size) {
			if (message[size - 1] < 0)
				cksum += message[size - 1] + 256;
			else
				cksum += message[size - 1];
		}

		while (cksum >> 16 != 0)
			cksum = (cksum >> 16) + (cksum & 0xffff);
		// System.out.println(cksum);
		return (~cksum);

	}
}
