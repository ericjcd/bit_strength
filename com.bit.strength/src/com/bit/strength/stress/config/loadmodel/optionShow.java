package com.bit.strength.stress.config.loadmodel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class optionShow {
	private JTable optionTable;

	public void optionShow(final JTable t) {
		final JFrame Option = new JFrame();
		Option.setLayout(null);
		Option.setSize(400, 200);
		Option.setVisible(true);
		optionTable = new JTable();
		commonmethod method = new commonmethod();
		String[] t1name = { "DN", "O(是否支持Opaque LSA)", "DC", "L",
				"N(是否为NSSA区域路由器)", "MC(是否组播扩展OSPF)", "E(是否始发于Stub区域路由器)" };
		String[] t1data1 = { "0", "0", "0", "1", "0", "0", "1" };
		String[][] t1items = {};
		optionTable = method.AddHeadTable(t1name, t1data1, t1items, 3);
		optionTable.setBounds(5, 5, 390, 80);
		JScrollPane optionJs = new JScrollPane(optionTable);
		optionJs.setBounds(5, 5, 390, 80);
		Option.add(optionJs);

		JButton save = new JButton("保存");
		save.setBounds(5, 90, 120, 29);

		JButton cancel = new JButton("取消");
		cancel.setBounds(275, 90, 120, 29);

		Option.add(save);
		Option.add(cancel);

		final char[] Op = new char[2];
		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int temp;
				DefaultTableModel table1Model = (DefaultTableModel) optionTable
						.getModel();
				int flag = 0;
				for (int i = 0; i < 7; i++) {
					String v = table1Model.getValueAt(0, i).toString();
					if (!(v.length() == 1 && (v.equals("1") || v.equals("0")))) {
						optionTable.setValueAt("wrong", 0, i);
						flag = 1;
					}
				}
				if (flag == 0) {
					temp = Integer.valueOf(table1Model.getValueAt(0, 6)
							.toString())
							* 2
							+ Integer.valueOf(table1Model.getValueAt(0, 5)
									.toString())
							* 4
							+ Integer.valueOf(table1Model.getValueAt(0, 4)
									.toString()) * 8;

					if (temp > 9)
						Op[0] = (char) ('A' + temp - 9);
					else
						Op[0] = (char) ('0' + temp);
					Op[1] = '1';
					System.out.println(Op[0]);
					String value = "0x" + String.valueOf(Op[1])
							+ String.valueOf(Op[0]);
					t.setValueAt(value, 0, 2);
					Option.dispose();
				}
			}
		});
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Op[0] = '1';
				Op[1] = '1';
				String value = "0x" + String.valueOf(Op[1])
						+ String.valueOf(Op[0]);
				t.setValueAt(value, 0, 2);
				Option.dispose();
			}
		});

	}
}

