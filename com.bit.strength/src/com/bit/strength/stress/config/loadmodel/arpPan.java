package com.bit.strength.stress.config.loadmodel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.bit.strength.stress.config.SetData;

public class arpPan extends JFrame {
	private JTabbedPane tabbedPane;
	private JPanel contentPane;

	public void arpPanelShow(final SetData fframe) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					arpPan frame = new arpPan( fframe);
					frame.setVisible(true);
					frame.setAlwaysOnTop(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public arpPan(final SetData fframe) {
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

		arppanel ArpPanel = new arppanel();
		JPanel pan3 = ArpPanel.AddArpPanel(this,fframe);
		tabbedPane.addTab("arp", pan3);
		
		tabbedPane.setEnabledAt(0, true);

		contentPane.add(tabbedPane, BorderLayout.NORTH);
	}
}

