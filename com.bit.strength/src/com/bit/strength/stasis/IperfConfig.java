package com.bit.strength.stasis;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JRadioButton;

public class IperfConfig extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JLabel label;
	private JTextField textField_1;
	private JLabel label_1;
	private JTextField textField_2;
	private JLabel label_2;

	/*
	 * public static void main(String[] args) { EventQueue.invokeLater(new
	 * Runnable() { public void run() { try { IperfConfig frame = new
	 * IperfConfig(); frame.setVisible(true); } catch (Exception e) {
	 * e.printStackTrace(); } } }); }
	 */

	/**
	 * Create the frame.
	 */
	public IperfConfig() {
		setAlwaysOnTop(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 253);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		// 目标IP label
		JLabel lblip = new JLabel("\u76EE\u6807IP");
		lblip.setBounds(33, 30, 54, 15);
		contentPane.add(lblip);
		// 目标IP
		textField = new JTextField();
		textField.setText("192.168.31.1");
		textField.setBounds(93, 30, 92, 15);
		contentPane.add(textField);
		textField.setColumns(10);
		// 目标端口 label
		label = new JLabel("\u76EE\u6807\u7AEF\u53E3");
		label.setBounds(211, 30, 54, 15);
		contentPane.add(label);
		// 目标端口
		textField_1 = new JTextField();
		textField_1.setText("12345");
		textField_1.setBounds(273, 29, 59, 16);
		contentPane.add(textField_1);
		textField_1.setColumns(10);
		// 本地端口 label
		label_1 = new JLabel("\u672C\u5730\u7AEF\u53E3");
		label_1.setBounds(211, 68, 54, 15);
		contentPane.add(label_1);
		// 本地端口
		textField_2 = new JTextField();
		textField_2.setText("30000");
		textField_2.setBounds(273, 68, 59, 16);
		contentPane.add(textField_2);
		textField_2.setColumns(10);
		// 显示内容
		label_2 = new JLabel("\u663E\u793A\u5185\u5BB9");
		label_2.setBounds(33, 119, 54, 15);
		contentPane.add(label_2);
		// 带宽
		JRadioButton radiobutton = new JRadioButton("\u5E26\u5BBD");
		radiobutton.setBounds(101, 115, 59, 23);
		contentPane.add(radiobutton);
		// 丢包率
		JRadioButton radiobutton_1 = new JRadioButton("\u4E22\u5305\u7387");
		radiobutton_1.setBounds(273, 115, 68, 23);
		contentPane.add(radiobutton_1);
		// 时延
		JRadioButton radiobutton_2 = new JRadioButton("\u65F6\u5EF6");
		radiobutton_2.setBounds(190, 115, 54, 23);
		contentPane.add(radiobutton_2);

		ButtonGroup bg = new ButtonGroup();
		bg.add(radiobutton);
		bg.add(radiobutton_1);
		bg.add(radiobutton_2);

		// 确定
		JButton button = new JButton("\u786E\u5B9A");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (radiobutton.isSelected()) {
					// 带宽
					BandWidth bandWidth = new BandWidth();
					bandWidth.show(textField.getText(), textField_1.getText(),
							textField_2.getText());
				} else if (radiobutton_1.isSelected()) {
					// 丢包率
					Lost lost = new Lost();
					lost.show(textField.getText(), textField_1.getText(),
							textField_2.getText());
				} else if (radiobutton_2.isSelected()) {
					// 时延
					Jitter jitter = new Jitter();
					jitter.show(textField.getText(), textField_1.getText(),
							textField_2.getText());
				}
				close();
			}
		});
		button.setBounds(93, 162, 93, 23);
		contentPane.add(button);
		// 取消
		JButton button_1 = new JButton("\u53D6\u6D88");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				close();
			}
		});
		button_1.setBounds(239, 162, 93, 23);
		contentPane.add(button_1);

	}

	private void close() {
		this.dispose();
	}
}
