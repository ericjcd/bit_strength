package com.bit.strength.stress.config;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class SetServer extends JFrame {

	private JPanel contentPane;
	private JTextField textFieldServer;
	private JTextField textFieldPort;

	private Table table_statistics;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					// SetServer frame = new SetServer();
					// frame.setVisible(true);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public SetServer(Table table) {
		if (table != null)
			table_statistics = table;
		setAlwaysOnTop(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 384, 170);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblServer = new JLabel("Server IP");
		lblServer.setBounds(10, 35, 54, 15);
		contentPane.add(lblServer);

		JLabel lblPort = new JLabel("Port");
		lblPort.setBounds(199, 35, 54, 15);
		contentPane.add(lblPort);

		textFieldServer = new JTextField();
		textFieldServer.setBounds(74, 32, 101, 21);
		contentPane.add(textFieldServer);
		textFieldServer.setColumns(10);

		textFieldPort = new JTextField();
		textFieldPort.setBounds(238, 32, 92, 21);
		contentPane.add(textFieldPort);
		textFieldPort.setColumns(10);

		JButton buttonAdd = new JButton("\u6DFB\u52A0");
		buttonAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				addServer();
			}
		});
		buttonAdd.setBounds(50, 86, 93, 23);
		contentPane.add(buttonAdd);

		JButton buttonCancel = new JButton("\u53D6\u6D88");
		buttonCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cancel();
			}
		});
		buttonCancel.setBounds(214, 86, 93, 23);
		contentPane.add(buttonCancel);
	}

	private void addServer() {
		if (textFieldServer.getText().length() == 0
				|| textFieldPort.getText().length() == 0)
			return;
		Server newserver = new Server(textFieldServer.getText(),
				Integer.parseInt(textFieldPort.getText()));
		RMIConfig.getConfig().addSlave(newserver);
		System.out.println("add server " + newserver.getIPPortSlaveString()
				+ " " + RMIConfig.getConfig().getSlave().size());
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				int cur = table_statistics.getItemCount();
				TableItem item = new TableItem(table_statistics, SWT.NONE, cur);
				item.setText(new String[] { textFieldServer.getText(),
						textFieldPort.getText() });
				// update table
			}
		});

		dispose();
	}

	private void cancel() {
		dispose();
	}
}
