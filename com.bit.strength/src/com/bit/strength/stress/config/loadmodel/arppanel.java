package com.bit.strength.stress.config.loadmodel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import com.bit.strength.stress.config.SetData;

public class arppanel {
	private JTable table_1;
	private JTable table_2;
	private JButton button1;
	private JButton button2;
	private JLabel label1;
	private JScrollPane js1;
	private JScrollPane js2;

	public JPanel AddArpPanel(final arpPan frame,final SetData fframe) {
		JPanel pan = new JPanel();
		pan.setRequestFocusEnabled(false);
		pan.setLayout(null);

		String[] t1name = { "硬件类型(2)", "协议类型(2)", "硬件地址长度(1)", "协议地址长度(1)",
				"操作类型(2)" };
		final String[] t1data1 = { "1", "0x0800", "6", "4", "1(arp请求)" };
		String[] t1item1 = { "4", "1(arp请求)", "2(arp响应)", "3(rarp请求)",
				"4(rarp响应)" };
		String[][] t1items = { t1item1 };
		commonmethod method = new commonmethod();
		table_1 = method.AddHeadTable(t1name, t1data1, t1items, 4);
		js1 = new JScrollPane(table_1);
		js1.setBounds(6, 23, 557, 48);
		pan.add(js1);

		String[] t2name = { "编号", "发送者硬件地址(6)", "发送者ip地址(4)", "目标硬件地址(6)",
				"目标ip地址(4)" };
		final String[] t2data1 = { "1", "FF-FF-FF-FF-FF-FF", "255.255.255.255",
				"FF-FF-FF-FF-FF-FF", "255.255.255.255" };
		String[][] t2items = {};
		table_2 = method.AddHeadTable(t2name, t2data1, t2items, 1);
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
					if (col % 2 == 0 && !method.AddrCheck(0, con)) {
						ConTable.setValueAt("wrong", row, col);
					}
					if (col % 2 == 1 && !method.AddrCheck(1, con)) {
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

		JLabel label2 = new JLabel("ARP信息");
		label2.setBounds(6, 71, 61, 16);
		pan.add(label2);

		button1 = new JButton("\u786E\u8BA4");
		button1.setBounds(40, 198, 117, 29);

		button2 = new JButton("\u91CD\u7F6E\n");
		button2.setBounds(412, 198, 117, 29);

		button1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 首部获得
				DefaultTableModel table1Model = (DefaultTableModel) table_1
						.getModel();
				byte[] head = new byte[8];
				head[0] = 0;
				head[1] = 1;
				head[2] = 8;
				head[3] = 0;
				head[4] = 6;
				head[5] = 4;
				head[6] = 0;
				head[7] = Byte.valueOf(table1Model.getValueAt(0, 4).toString()
						.substring(0, 1));
				data indata = new data();
				indata.ArpAddHead(head);

				// 内容获得
				DefaultTableModel table2Model = (DefaultTableModel) table_2
						.getModel();
				byte[] content = new byte[20];
				byte[] sendermac = new byte[6];
				byte[] senderip = new byte[4];
				byte[] recmac = new byte[6];
				byte[] recip = new byte[4];
				String smac = new String();
				smac = table2Model.getValueAt(0, 1).toString();
				String srip = new String();
				srip = table2Model.getValueAt(0, 2).toString();
				String rmac = new String();
				rmac = table2Model.getValueAt(0, 3).toString();
				String rrip = new String();
				rrip = table2Model.getValueAt(0, 4).toString();
				commonmethod method = new commonmethod();
				if (smac != "wrong" && srip != "wrong" && rmac != "wrong"
						&& rrip != "wrong") {
					
					
					
					sendermac = method.StrToByte(1, smac);
					recmac = method.StrToByte(1, rmac);
					senderip = method.StrToByte(0, srip);
					recip = method.StrToByte(0, rrip);
					
					
				
					for (int i = 0; i < 6; i++)
						content[i] = sendermac[i];
					for (int i = 0; i < 4; i++)
						content[i + 6] = senderip[i];
					for (int i = 0; i < 6; i++)
						content[i + 10] = recmac[i];
					for (int i = 0; i < 4; i++)
						content[i + 16] = recip[i];
					indata.ArpAddContent(content);
					byte[] data = indata.GetArp();
					short operation = Short.valueOf(table1Model.getValueAt(0, 4).toString()
							.substring(0, 1));
					fframe.setArpData(operation, sendermac, senderip, recmac, recip);
					fframe.loadModel(data);
					javax.swing.JOptionPane.showMessageDialog(pan, "保存成功");
					frame.dispose();
				} else {
					javax.swing.JOptionPane.showMessageDialog(pan, "有非法字符输入");
				}

			}

		});
		button2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DefaultTableModel tableModel = (DefaultTableModel) table_2
						.getModel();
				tableModel.removeRow(0);
				tableModel.addRow(t2data1);
				DefaultTableModel tableModel1 = (DefaultTableModel) table_1
						.getModel();
				tableModel1.removeRow(0);
				tableModel1.addRow(t1data1);
			}
		});

		pan.add(button1);
		pan.add(button2);

		return pan;
	}

}
