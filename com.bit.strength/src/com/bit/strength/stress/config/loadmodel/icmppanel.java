package com.bit.strength.stress.config.loadmodel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.xml.soap.Text;

import com.bit.strength.stress.config.SetData;

public class icmppanel {
	private JTable table_1;
	private JTextPane text_1;
	private JButton button1;
	private JButton button2;
	private JLabel label1;
	private JScrollPane js1;
	private JScrollPane js2;

	private byte[] updateCheck(JTable table, String text) {
		DefaultTableModel tableModel = (DefaultTableModel) table_1.getModel();
		commonmethod method = new commonmethod();
		byte[] icmp = new byte[64];
		data indata = new data();
		// String type = order.getSelectedItem().toString()
		// .substring(0, 1);
		int type = Integer.valueOf(tableModel.getValueAt(0, 0).toString()
				.substring(0, 1));
		icmp[0] = (byte) type;
		icmp[1] = 0;

		icmp[2] = 0;
		icmp[3] = 0;

		icmp[4] = 1;
		icmp[5] = 0;

		icmp[6] = 1;
		icmp[7] = 0;
		byte b[] = text.getBytes();
		for (int i = 0; i < 56; i++) {
			if (i < b.length)
				icmp[63 - i] = b[b.length - i - 1];
			else
				icmp[63 - i] = 0;
		}
		indata.IcmpAdd(icmp);
		byte[] data = indata.GetIcmp();
		long cks = method.sumCheck(icmp, 64);
		if (cks < 0) {
			cks = 65536 + cks;
		}
		int high = (int) (cks) / 256;
		int low = (int) (cks) % 256;
		int highh = high / 16;
		int highl = high % 16;
		int lowh = low / 16;
		int lowl = low % 16;
		String[] hex = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A",
				"B", "C", "D", "E", "F" };
		String sum = "0x" + hex[highh] + hex[highl] + hex[lowh] + hex[lowl];
		table.setValueAt(sum, 0, 2);
		return data;

	}

	public JPanel AddIcmpPanel(final icmpPan frame,final SetData fframe) {
		JPanel pan = new JPanel();
		pan.setRequestFocusEnabled(false);
		final commonmethod method = new commonmethod();
		pan.setLayout(null);
		String[] t1name = { "类型(1)", "代码(1)", "校验和(2)", "标示符(2)", "序列号(2)" };
		final String[] t1data1 = { "0(响应应答)", "0", "0x9F85", "0x0100", "0x0100" };
		String[][] t1data = { t1data1 };
		DefaultTableModel t1model = new DefaultTableModel(t1data, t1name) {
			public boolean isCellEditable(int row, int col) {
				if (col > 0)
					return false;
				else
					return true;
			}
		};
		table_1 = new JTable(t1model);
		table_1.setEditingColumn(0);
		table_1.setCellSelectionEnabled(true);
		table_1.setColumnSelectionAllowed(true);
		table_1.getColumnModel().getColumn(0).setWidth(30);
		table_1.setBorder(new LineBorder(new Color(0, 0, 0)));
		table_1.setSize(new Dimension(1, 1));
		table_1.setBounds(6, 23, 557, 48);
		final JComboBox order = new JComboBox();

		order.addItem("0(响应应答)");
		order.addItem("8(响应请求)");
		order.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				;
			}
		});
		order.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				String content = text_1.getText();
				updateCheck(table_1, content);
			}
		});
		table_1.getColumnModel().getColumn(0)
				.setCellEditor(new DefaultCellEditor(order));
		js1 = new JScrollPane(table_1);
		js1.setBounds(6, 23, 557, 48);
		pan.add(js1);

		text_1 = new JTextPane();
		text_1.setBounds(6, 90, 557, 96);
		final String defaultData = "ABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCD";
		text_1.setText(defaultData);
		js2 = new JScrollPane(text_1);
		js2.setBounds(6, 90, 557, 96);
		pan.add(js2);

		text_1.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyReleased(KeyEvent e) {
				byte b[] = text_1.getText().getBytes();
				if(b.length <= 56)
					updateCheck(table_1, text_1.getText());
				else{
					
					text_1.setText(text_1.getText().substring(0,56));
					javax.swing.JOptionPane.showMessageDialog(pan, "最大长度为56个字节");
				}

			}

			public void keyPressed(KeyEvent e) {
				
			}

		});

		label1 = new JLabel("\u9996\u90E8");
		label1.setBounds(6, 6, 213, 16);
		pan.add(label1);

		JLabel label2 = new JLabel("icmp内容");
		label2.setBounds(6, 71, 61, 16);
		pan.add(label2);

		button1 = new JButton("\u786E\u8BA4");
		button1.setBounds(40, 198, 117, 29);

		button2 = new JButton("\u91CD\u7F6E\n");
		button2.setBounds(412, 198, 117, 29);

		button1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				String content = text_1.getText();
				byte[] data = updateCheck(table_1, content);
				DefaultTableModel tableModel = (DefaultTableModel) table_1.getModel();
				//add chceknum to data here
				char[] checkNum = tableModel.getValueAt(0, 2).toString().substring(2, 6).toCharArray();
				int tempInt = 0;
				for (int i = 0; i < 4; i++){
					if (checkNum[i] <= '9')
						tempInt = tempInt * 16 + checkNum[i] - '0';
					else
						tempInt = tempInt * 16 + checkNum[i] - 'A' + 11;
					if (i == 1){
						data[2] = (byte)tempInt;
						tempInt = 0;
					}
				}
				data[3] = (byte)tempInt;
				//arpdata set here 
				fframe.setIcmpData(data, text_1.getText());
				fframe.loadModel(data);
				javax.swing.JOptionPane.showMessageDialog(pan, "保存成功");
				frame.dispose();
			}
		});
		button2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DefaultTableModel tableModel = (DefaultTableModel) table_1
						.getModel();
				tableModel.removeRow(0);
				tableModel.addRow(t1data1);
				text_1.setText(defaultData);
			}
		});

		pan.add(button1);
		pan.add(button2);

		return pan;
	}

	public void keyPressed(KeyEvent e) {
		label1.setText(KeyEvent.getKeyText(e.getKeyCode()));
	}

	
	//ABCDEFGHIJKLMNOPQRSTUVWXYZ
}
