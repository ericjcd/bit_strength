package com.bit.strength.stress.config.loadmodel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.io.UnsupportedEncodingException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.bit.strength.stress.config.SetData;

public class udpPanel extends JFrame {
	private JTabbedPane tabbedPane;
	private JPanel contentPane;
	private SetData fatherFrame;
	public void udpPanelShow(final SetData fframe) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					udpPanel frame = new udpPanel(fframe);
					frame.setVisible(true);
					frame.setAlwaysOnTop(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public udpPanel(final SetData fframe) {
		this.setVisible(true);
		this.setAlwaysOnTop(true);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(100, 100, 600, 306);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setPreferredSize(new Dimension(21, 270));
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

		rippanel RipPanel = new rippanel();
		JPanel pan1 = RipPanel.AddRipPanel(this,fframe);
		tabbedPane.addTab("rip", pan1);

		ospfpanel OspfPanel = new ospfpanel();
		JPanel pan2 = OspfPanel.AddOspfPanel(this,fframe);
		tabbedPane.addTab("ospf", pan2);
		
		tabbedPane.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if (tabbedPane.getSelectedIndex() == 1) {
					setBounds(100, 100, 600, 408);
					tabbedPane.setPreferredSize(new Dimension(21, 370));
				} else {
					setBounds(100, 100, 600, 306);
					tabbedPane.setPreferredSize(new Dimension(21, 270));
				}
			}
		});
		tabbedPane.setEnabledAt(0, true);

		contentPane.add(tabbedPane, BorderLayout.NORTH);
		
	}
	
}
