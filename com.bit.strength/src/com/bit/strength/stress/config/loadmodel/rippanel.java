package com.bit.strength.stress.config.loadmodel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
import javax.swing.table.TableCellEditor;

import com.bit.strength.stress.config.SetData;

public class rippanel {
	private JTable table_1;
	private JTable table_2;
	private JButton button3;
	private JButton button1;
	private JButton button2;
	private JLabel label1;
	private JScrollPane js1;
	private JScrollPane js2;
	private JLabel label3; 

	public JPanel AddRipPanel(final udpPanel frame,final SetData fframe) {
		JPanel pan = new JPanel();
		pan.setRequestFocusEnabled(false);
		pan.setLayout(null);

		String[] t1name = { "命令(1)", "版本(1)" };
		String[] t1data1 = { "1(请求)", "1" };
		String[] t1item1 = { "0", "1(请求)", "2(应答)" };
		String[] t1item2 = { "1", "1", "2" };
		String[][] t1items = { t1item1, t1item2 };
		commonmethod method = new commonmethod();
		table_1 = method.AddHeadTable(t1name, t1data1, t1items, 0);
		js1 = new JScrollPane(table_1);
		js1.setBounds(6, 23, 557, 48);
		pan.add(js1);

		String[] t2name = { "编号", "地址族标示符(2)", "路由标记(2)", "IP地址(4)", "子网掩码(4)",
				"下一跳路由器(4)", "距离(4)" };
		final String[] t2data1 = { "1", "2", "0", "192.168.0.1",
				"255.255.255.255", "192.168.0.1", "1" };
		String[][] t2items = {};
		table_2 = method.AddHeadTable(t2name, t2data1, t2items, 3);
		final DefaultTableModel ConTable = (DefaultTableModel) table_2
				.getModel();
		ConTable.addTableModelListener(new TableModelListener() {
			public void tableChanged(TableModelEvent e) {
				int row = e.getFirstRow();
				int col = e.getColumn();
				if (e.getType() == 0
						&& ConTable.getValueAt(row, col) != "wrong") {
					commonmethod method = new commonmethod();
					String con = ConTable.getValueAt(row, col).toString();
					if (col != 6 && !method.AddrCheck(0, con)) {
						ConTable.setValueAt("wrong", row, col);
					}
					if (col == 6
							&& !(method.DisCheck(con)
									&& Integer.valueOf(con) <= 16 && Integer
									.valueOf(con) > 0)) {
						ConTable.setValueAt("wrong", row, col);
					}
				}
			}
		});
		js2 = new JScrollPane(table_2);
		js2.setBounds(6, 90, 557, 96);
		pan.add(js2);

		label1 = new JLabel("\u9996\u90E8");
		label1.setBounds(6, 6, 213, 16);
		pan.add(label1);

		JLabel label2 = new JLabel("\u8DEF\u7531\u6761\u76EE");
		label2.setBounds(6, 71, 61, 16);
		pan.add(label2);

		button1 = new JButton("\u786E\u8BA4");
		button1.setBounds(40, 198, 117, 29);

		button2 = new JButton("\u91CD\u7F6E\n");
		button2.setBounds(412, 198, 117, 29);

		button3 = new JButton("\u589E\u52A0\u8DEF\u7531");
		button3.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
		button3.setBounds(502, 73, 61, 16);

		button1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 首部获得
				DefaultTableModel table1Model = (DefaultTableModel) table_1
						.getModel();
				byte[] head = new byte[4];
				head[0] = Byte.valueOf(table1Model.getValueAt(0, 0).toString()
						.substring(0, 1));
				head[1] = Byte.valueOf(table1Model.getValueAt(0, 1).toString());
				head[2] = 0;
				head[3] = 0;
				data indata = new data();
				indata.RipAddHead(head);

				// 内容获得
				DefaultTableModel table2Model = (DefaultTableModel) table_2
						.getModel();
				int num = table_2.getRowCount();
				int flag = 0;
				for (int i = 0; i < num; i++)
					for (int j = 0; j < 7; j++)
						if (table_2.getValueAt(i, j).toString() == "wrong") {
							flag = 1;
							break;
						}
				if (flag == 1) {
					javax.swing.JOptionPane.showMessageDialog(null, "有非法字符输入");
				} else {
					byte[][] incontent = new byte[num][20];
					for (int i = 0; i < num; i++) {
						// 地址标示符号
						incontent[i][0] = 0;
						incontent[i][1] = 2;
						// 路由标记＝0
						incontent[i][2] = 0;
						incontent[i][3] = 0;

						commonmethod method = new commonmethod();
						// 网络地址
						byte[] ipaddr = new byte[4];
						ipaddr = method.StrToByte(0,
								table2Model.getValueAt(i, 3).toString());
						incontent[i][4] = ipaddr[0];
						incontent[i][5] = ipaddr[1];
						incontent[i][6] = ipaddr[2];
						incontent[i][7] = ipaddr[3];
						// 子网掩码
						byte[] mask = new byte[4];

						mask = method.StrToByte(0, table2Model.getValueAt(i, 4)
								.toString());
						incontent[i][8] = mask[0];
						incontent[i][9] = mask[1];
						incontent[i][10] = mask[2];
						incontent[i][11] = mask[3];

						// 下一条路由地址
						byte[] routeaddr = new byte[4];

						routeaddr = method.StrToByte(0,
								table2Model.getValueAt(i, 5).toString());
						incontent[i][12] = routeaddr[0];
						incontent[i][13] = routeaddr[1];
						incontent[i][14] = routeaddr[2];
						incontent[i][15] = routeaddr[3];

						// 距离
						incontent[i][16] = 0;
						incontent[i][17] = 0;
						incontent[i][18] = 0;
						String dis = table2Model.getValueAt(i, 6).toString();

						incontent[i][19] = Byte.valueOf(table2Model.getValueAt(
								i, 6).toString());
					}
					indata.RipAddContent(incontent);
					byte[] data = indata.GetRip();
					fframe.loadModel(data);

					javax.swing.JOptionPane.showMessageDialog(null, "保存成功");
					frame.dispose();

				}

			}

		});
		button2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DefaultTableModel tableModel = (DefaultTableModel) table_2
						.getModel();
				int num = tableModel.getRowCount();
				for (int i = num - 1; i >= 0; i--) {
					tableModel.removeRow(i);
				}
				tableModel.addRow(t2data1);
				DefaultTableModel tableModel1 = (DefaultTableModel) table_1
						.getModel();
				tableModel1.removeRow(0);
				tableModel1.addRow(new Object[] { "1(请求)", "1" });
				label3.setText("当前路由条目数：1");
				label3.setForeground(Color.black);
			}
		});
		button3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DefaultTableModel tableModel = (DefaultTableModel) table_2
						.getModel();
				int num = tableModel.getRowCount();
				if (num + 1 < 16) {
					tableModel.addRow(new Object[] { num + 1, "2", "0",
							"192.168.0.1", "255.255.255.255", "192.168.0.1",
							"1" });
					label3.setText("当前路由条目数：" + Integer.toString(num + 1));
					label3.setForeground(Color.black);
				} else {
					label3.setText("当前路由条目数：" + Integer.toString(num));
					label3.setForeground(Color.red);
				}
			}
		});

		pan.add(button1);
		pan.add(button2);
		pan.add(button3);

		label3 = new JLabel("\u5F53\u524D\u8DEF\u7531\u6761\u76EE\u6570\uFF1A1");
		label3.setBounds(96, 71, 123, 16);
		pan.add(label3);

		return pan;

	}
}

