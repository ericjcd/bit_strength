package com.bit.strength.stress.config.loadmodel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.LineBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import com.bit.strength.stress.config.SetData;

public class ospfpanel {
	private JTable table1_head;
	private JTable table1_con;
	private JTable table2_head;
	private JTable table2_con;
	private JButton t1_button1;
	private JButton t1_button2;
	private JButton t2_button1;
	private JButton t2_button2;
	private JLabel label1_head;
	private JLabel label1_con;
	private JLabel label2_head;
	private JLabel label2_con;
	private JScrollPane js1_head;
	private JScrollPane js1_con;
	private JScrollPane js2_head;
	private JScrollPane js2_con;

	private byte[] getHelloCon() {
		commonmethod method = new commonmethod();
		int flag = 0;
		// 内容获得
		byte[] ospfcon = new byte[20];
		DefaultTableModel table2Model = (DefaultTableModel) table1_con
				.getModel();

		// 子网掩码
		if (method.AddrCheck(0, table2Model.getValueAt(0, 0).toString())) {
			byte[] ipaddr = new byte[4];
			ipaddr = method.StrToByte(0, table2Model.getValueAt(0, 0)
					.toString());
			ospfcon[0] = ipaddr[0];
			ospfcon[1] = ipaddr[1];
			ospfcon[2] = ipaddr[2];
			ospfcon[3] = ipaddr[3];
		} else {
			table2Model.setValueAt("wrong", 0, 0);
			flag = 1;
		}

		// 时间间隔
		String time = table2Model.getValueAt(0, 1).toString();
		if (method.DisCheck(time) && Integer.valueOf(time) < 256
				&& Integer.valueOf(time) > 0) {
			ospfcon[4] = Byte.valueOf((Integer.valueOf(time) / 16) + "");
			ospfcon[5] = Byte.valueOf((Integer.valueOf(time) % 16) + ""); // 时间间隔
		} else {
			table2Model.setValueAt("wrong", 0, 1);
			flag = 1;
		}

		// 可选项
		byte[] t = new byte[1];
		t = method.hexStringToBytes(table2Model.getValueAt(0, 2).toString()
				.substring(2));
		ospfcon[6] = t[0];

		// DR优先级
		String level = table2Model.getValueAt(0, 3).toString();
		if (level.length() == 1 && (level.equals("1") || level.equals("0")))
			ospfcon[7] = Byte.valueOf(level);
		else {
			table2Model.setValueAt("wrong", 0, 3);
			flag = 1;
		}
		// 失效时间
		String losetime = table2Model.getValueAt(0, 4).toString();
		if (method.DisCheck(losetime) && Integer.valueOf(losetime) < 256
				&& Integer.valueOf(losetime) > 0) {
			int a = (Integer.valueOf(losetime) / 256);
			int b = (Integer.valueOf(losetime) % 256);
			ospfcon[8] = Byte.valueOf(a / 16 + "");
			ospfcon[9] = Byte.valueOf(a % 16 + ""); // 失效时间
			ospfcon[10] = Byte.valueOf(b / 16 + "");
			ospfcon[11] = Byte.valueOf(b % 16 + ""); // 失效时间
		} else {
			table2Model.setValueAt("wrong", 0, 4);
			flag = 1;
		}

		// dr接口ip & bdr接口ip
		byte[] drip = new byte[4];
		if (method.AddrCheck(0, table2Model.getValueAt(0, 5).toString())
				&& method.AddrCheck(0, table2Model.getValueAt(0, 6).toString())) {
			drip = method.StrToByte(0, table2Model.getValueAt(0, 5).toString());
			ospfcon[12] = drip[0];
			ospfcon[13] = drip[1];
			ospfcon[14] = drip[2];
			ospfcon[15] = drip[3];

			drip = method.StrToByte(0, table2Model.getValueAt(0, 6).toString());
			ospfcon[16] = drip[0];
			ospfcon[17] = drip[1];
			ospfcon[18] = drip[2];
			ospfcon[19] = drip[3];
		} else {
			table2Model.setValueAt("wrong", 0, 5);
			table2Model.setValueAt("wrong", 0, 6);
			flag = 1;
		}
		if (flag == 0)
			return ospfcon;
		else {
			byte[] n = new byte[1];
			return n;
		}
	}

	private byte[] getDDCon() {
		commonmethod method = new commonmethod();
		int flag = 0;
		// 内容获得
		byte[] con = new byte[8];
		DefaultTableModel table2Model = (DefaultTableModel) table2_con
				.getModel();

		// 可选ip最大长度
		String maxlen = table2Model.getValueAt(0, 1).toString();
		if (method.DisCheck(maxlen) && Integer.valueOf(maxlen) < 256
				&& Integer.valueOf(maxlen) > 0) {
			con[0] = Byte.valueOf((Integer.valueOf(maxlen) / 16) + "");
			con[1] = Byte.valueOf((Integer.valueOf(maxlen) % 16) + "");
		} else {
			table2Model.setValueAt("wrong", 0, 1);
			flag = 1;
		}

		// 可选项
		byte[] t = new byte[1];
		t = method.hexStringToBytes(table2Model.getValueAt(0, 2).toString()
				.substring(2));
		con[2] = t[0];

		// 3位
		int I = Integer.valueOf(table2Model.getValueAt(0, 3).toString()
				.substring(0, 1));
		int M = Integer.valueOf(table2Model.getValueAt(0, 4).toString()
				.substring(0, 1));
		int MS = Integer.valueOf(table2Model.getValueAt(0, 5).toString()
				.substring(0, 1));
		int x = I * 4 + M * 2 + MS * 1;
		con[3] = Byte.valueOf(x + "");

		//
		byte[] ipaddr = new byte[4];
		ipaddr = method.hexStringToBytes("7FFFFFFF");
		con[4] = ipaddr[0];
		con[5] = ipaddr[1];
		con[6] = ipaddr[2];
		con[7] = ipaddr[3];

		// 最终判断
		if (flag == 1) {
			byte[] n = new byte[1];
			return n;
		} else {
			return con;
		}
	}

	private byte[] getOspfHead(JTable table) {
		commonmethod method = new commonmethod();
		// 首部获得
		DefaultTableModel table1Model = (DefaultTableModel) table.getModel();
		byte[] head = method.CheckOspfHead(table1Model);
		return head;
	}

	private void modelAdd(JTable editTable, final JTable headTable) {
		final DefaultTableModel eTable = (DefaultTableModel) editTable
				.getModel();
		final DefaultTableModel hTable = (DefaultTableModel) headTable
				.getModel();
		eTable.addTableModelListener(new TableModelListener() {
			public void tableChanged(TableModelEvent e) {
				if (e.getType() == 0 && e.getColumn() != 5
						&& eTable.getValueAt(0, e.getColumn()) != "wrong") {
					String sum = null;
					data ospf = new data();
					int type = Integer.valueOf(headTable.getValueAt(0, 1)
							.toString().substring(0, 1));
					System.out.println(type);
					// 首部获得
					byte[] head = getOspfHead(headTable);

					// 内容获得
					byte[] ospfcon;
					if (type == 1)
						ospfcon = getHelloCon();
					else if (type == 2)
						ospfcon = getDDCon();
					else
						ospfcon = new byte[1];

					// 最终判断
					if (ospfcon.length == 1 || head.length == 1) {
						sum = "wrong";
					} else {
						byte[] tempOspf;
						if (type == 1) {
							ospf.OspfHelloAddContent(ospfcon);
							ospf.OspfAddHead(head);
							tempOspf = ospf.GetOspfHello();
						} else if (type == 2) {
							ospf.OspfDDAddContent(ospfcon);
							ospf.OspfAddHead(head);
							tempOspf = ospf.GetOspfDD();
						} else {
							tempOspf = new byte[1];
						}
						// 验证字段置0
						for (int i = 16; i < 24; i++)
							tempOspf[i] = 0;
						tempOspf[12] = 0;
						tempOspf[13] = 0;
						commonmethod tempM = new commonmethod();
						long x = tempM.sumCheck(tempOspf, tempOspf.length);
						if (x < 0) {
							x = 65536 + x;
						}
						int high = (int) (x) / 256;
						int low = (int) (x) % 256;
						int highh = high / 16;
						int highl = high % 16;
						int lowh = low / 16;
						int lowl = low % 16;
						String[] hex = { "0", "1", "2", "3", "4", "5", "6",
								"7", "8", "9", "A", "B", "C", "D", "E", "F" };
						sum = "0x" + hex[highh] + hex[highl] + hex[lowh]
								+ hex[lowl];

					}
					hTable.setValueAt(sum, 0, 5);
				}
			}
		});
	}

	public JPanel AddOspfPanel(final udpPanel frame,final SetData fframe) {
		JPanel pan = new JPanel();
		pan.setRequestFocusEnabled(false);
		pan.setLayout(null);
		commonmethod method = new commonmethod();

		label1_head = new JLabel("Hello报文");
		label1_head.setBounds(6, 6, 213, 16);
		pan.add(label1_head);
		table1_head = method.AddOspHead("1(Hello报文)");
		modelAdd(table1_head, table1_head);

		js1_head = new JScrollPane(table1_head);
		js1_head.setBounds(6, 23, 557, 40);
		pan.add(js1_head);

		label1_con = new JLabel("报文内容");
		label1_con.setBounds(6, 71, 61, 16);
		pan.add(label1_con);
		String[] t1name = { "子网掩码(4)", "时间间隔(2)", "可选项(1)", "DR优先级(1)",
				"路由器失效时间(4)", "DR接口IP(2)", "BDR接口IP(2)" };
		final String[] t1data1 = { "255.255.255.255", "10", "0x11", "1", "40",
				"192.168.0.1", "192.168.0.1" };
		// String[] item1 = {"2", "DN", "O", "DC", "L", "NP", "MC", "E"};
		String[] t1item1 = { "3", "0", "1" };
		String[][] t1items = {t1item1};
		table1_con = method.AddHeadTable(t1name, t1data1, t1items, 0);
		method.ospfOption(table1_con);
		modelAdd(table1_con, table1_head);

		js1_con = new JScrollPane(table1_con);
		js1_con.setBounds(6, 90, 557, 40);
		pan.add(js1_con);

		t1_button1 = new JButton("保存");
		t1_button1.setBounds(335, 130, 117, 29);

		t1_button2 = new JButton("重置");
		t1_button2.setBounds(455, 130, 117, 29);

		t1_button1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				data ospf = new data();
				// 首部获得
				byte[] head = getOspfHead(table1_head);
				// 内容获得
				byte[] ospfcon = getHelloCon();
				// 最终判断
				if (ospfcon.length == 0 || head.length == 1) {
					javax.swing.JOptionPane.showMessageDialog(null, "有非法字符输入");
				} else {
					ospf.OspfHelloAddContent(ospfcon);
					ospf.OspfAddHead(head);
					byte[] data = ospf.GetOspfHello();
					fframe.loadModel(data);
					javax.swing.JOptionPane.showMessageDialog(null, "保存成功");
					frame.dispose();
				}

			}

		});
		t1_button2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DefaultTableModel tableModel1 = (DefaultTableModel) table1_head
						.getModel();
				tableModel1.removeRow(0);
				tableModel1.addRow(new Object[] { "4", "1(Hello报文)", "1",
						"192.168.0.1", "10.0.0.1", "0x5D96", "0(不认证)", "" });
				DefaultTableModel tableModel2 = (DefaultTableModel) table1_con
						.getModel();
				tableModel2.removeRow(0);
				tableModel2.addRow(t1data1);

			}
		});

		label2_head = new JLabel("DD报文");
		label2_head.setBounds(6, 160, 213, 16);
		pan.add(label2_head);
		table2_head = method.AddOspHead("2(DD报文)");
		modelAdd(table2_head, table2_head);

		js2_head = new JScrollPane(table2_head);
		js2_head.setBounds(6, 180, 557, 40);
		pan.add(js2_head);

		label2_con = new JLabel("报文内容");
		label2_con.setBounds(6, 220, 61, 16);
		pan.add(label2_con);
		String[] t2name = { "DD报文序列号(4)", "最大IP报文长度(2)", "可选项(1)", "I位(1bit)",
				"M位(1bit)", "M/S位(1bit)", "LSA头部(4)" };
		final String[] t2data1 = { "0x7FFFFFFF", "1", "0x11", "1(是首个dd报文)",
				"0(最后个dd报文)", "1(主)", "" };
		String[] t2item1 = { "3", "1(是首个dd报文)", "0(不是首个dd报文)" };
		String[] t2item2 = { "4", "0(最后个dd报文)", "1(非最后个dd报文)" };
		String[] t2item3 = { "5", "1(主)", "0(从)" };
		String[][] t2items = { t2item1, t2item2, t2item3 };
		table2_con = method.AddHeadTable(t2name, t2data1, t2items, 1);
		method.ospfOption(table2_con);
		modelAdd(table2_con, table2_head);

		js2_con = new JScrollPane(table2_con);
		js2_con.setBounds(6, 240, 557, 40);
		pan.add(js2_con);

		t2_button1 = new JButton("保存");
		t2_button1.setBounds(335, 280, 117, 29);

		t2_button2 = new JButton("重置");
		t2_button2.setBounds(455, 280, 117, 29);

		t2_button1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				data ospf = new data();
				// 首部获得
				byte[] head = getOspfHead(table2_head);
				// 内容获得
				byte[] ospfcon = getDDCon();
				// 最终判断
				if (ospfcon.length == 1 || head.length == 1) {
					javax.swing.JOptionPane.showMessageDialog(null, "有非法字符输入");
				} else {
					ospf.OspfDDAddContent(ospfcon);
					ospf.OspfAddHead(head);
					byte[] data = ospf.GetOspfDD();
					fframe.loadModel(data);
					javax.swing.JOptionPane.showMessageDialog(null, "保存成功");
					frame.dispose();
				}

			}

		});
		t2_button2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DefaultTableModel tableModel1 = (DefaultTableModel) table2_head
						.getModel();
				tableModel1.removeRow(0);
				tableModel1.addRow(new Object[] { "4", "2(DD报文)", "1",
						"192.168.0.1", "10.0.0.1", "0xA04D", "0(不认证)", "" });

				DefaultTableModel tableModel2 = (DefaultTableModel) table2_con
						.getModel();
				tableModel2.removeRow(0);
				tableModel2.addRow(t2data1);
			}
		});

		pan.add(t1_button1);
		pan.add(t1_button2);
		pan.add(t2_button1);
		pan.add(t2_button2);
		// pan.add(button3);

		return pan;

	}
}

