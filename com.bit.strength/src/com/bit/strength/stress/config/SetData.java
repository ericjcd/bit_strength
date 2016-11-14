package com.bit.strength.stress.config;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.bit.strength.stress.network.Message;
import com.bit.strength.util.ByteHex;
import com.bit.strength.stress.config.loadmodel.*;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import javax.swing.JTextField;
import javax.swing.JList;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import javax.swing.JCheckBox;
import javax.swing.JLabel;

import jpcap.packet.ARPPacket;

public class SetData extends JFrame {
	
	private JPanel contentPane;
	private JTextArea textArea_content;
	private JScrollPane scrollPane_text;
	private JComboBox comboBox;
	private JList list;
	private DefaultListModel model;
	// private byte[] data;
	private JTextField textField_title;
	private JButton button_confirm;

	private ArrayList<Message> messages;
	private JButton button_modify;
	private JButton button_add;
	private JButton button_delete;
	private JButton button_import;
	private JButton button_export;
	private JCheckBox chckbxReceiveNext;
	private JCheckBox chckbxAutoIncrease;
	private JTextField textFieldRepeat;
	private JTextField textFieldDuration;
	private JTextField textFieldInterval;
	
	private ArrayList<Object> icmpData = new ArrayList();
	private ARPPacket arpData = new ARPPacket();

	public static void main(String[] args) {
		SetData frame = new SetData();
		frame.setVisible(true);
	}

	/**
	 * Create the frame.
	 */
	
	public SetData() {

		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setAlwaysOnTop(true);
		setBounds(100, 100, 544, 362);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(new String[] { "ASCII",
				"UTF8", "Hex", "Base64" }));
		comboBox.setSelectedIndex(0);
		comboBox.setBounds(10, 10, 92, 21);
		contentPane.add(comboBox);

		button_confirm = new JButton("\u786E\u5B9A");
		button_confirm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// confirm
				exit();
			}
		});
		button_confirm.setBounds(313, 10, 64, 21);
		contentPane.add(button_confirm);

		textArea_content = new JTextArea();
		textArea_content.setBounds(0, 0, 434, 230);
		// if (data != null)textArea_content.setText(new String(data));

		scrollPane_text = new JScrollPane(textArea_content);
		scrollPane_text.setBounds(84, 95, 430, 219);

		contentPane.add(scrollPane_text);

		JScrollPane scrollPane_list = new JScrollPane();
		scrollPane_list.setBounds(10, 40, 64, 274);
		contentPane.add(scrollPane_list);

		model = new DefaultListModel();
		list = new JList();
		list.setModel(model);
		scrollPane_list.setViewportView(list);
		list.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				// TODO Auto-generated method stub
				selectedChanged();
			}
		});

		textField_title = new JTextField();
		textField_title.setBounds(84, 42, 293, 21);
		contentPane.add(textField_title);
		textField_title.setColumns(10);

		button_add = new JButton("\u6DFB\u52A0");
		button_add.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// add message
				addMessage();
			}
		});
		button_add.setBounds(112, 9, 64, 23);
		contentPane.add(button_add);

		button_delete = new JButton("\u5220\u9664");
		button_delete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// delete message
				deleteMessage();
			}
		});
		button_delete.setBounds(179, 9, 64, 23);
		contentPane.add(button_delete);

		button_modify = new JButton("\u4FEE\u6539");
		button_modify.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				modify();
			}
		});
		button_modify.setBounds(246, 9, 64, 23);
		contentPane.add(button_modify);

		button_import = new JButton("\u5BFC\u5165");
		button_import.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				importD();
			}
		});
		button_import.setBounds(380, 10, 64, 21);
		contentPane.add(button_import);

		button_export = new JButton("\u5BFC\u51FA");
		button_export.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				exportD();
			}
		});
		button_export.setBounds(447, 10, 64, 21);
		contentPane.add(button_export);

		chckbxReceiveNext = new JCheckBox("\u7B49\u5F85\u56DE\u590D");
		chckbxReceiveNext.setSelected(true);
		chckbxReceiveNext.setBounds(430, 40, 73, 23);
		contentPane.add(chckbxReceiveNext);

		chckbxAutoIncrease = new JCheckBox("\u81EA\u589E");
		chckbxAutoIncrease.setBounds(380, 40, 49, 23);
		contentPane.add(chckbxAutoIncrease);

		JLabel label = new JLabel("\u91CD\u590D");
		label.setBounds(83, 73, 31, 15);
		contentPane.add(label);

		JLabel label_1 = new JLabel("\u6301\u7EED");
		label_1.setBounds(199, 73, 31, 15);
		contentPane.add(label_1);

		JLabel lblS = new JLabel("s");
		lblS.setBounds(295, 73, 17, 15);
		contentPane.add(lblS);

		JLabel label_2 = new JLabel("\u95F4\u9694");
		label_2.setBounds(313, 73, 31, 15);
		contentPane.add(label_2);

		JLabel lblMs = new JLabel("ms");
		lblMs.setBounds(422, 73, 22, 15);
		contentPane.add(lblMs);

		textFieldRepeat = new JTextField();
		textFieldRepeat.setText("-1");
		textFieldRepeat.setBounds(110, 70, 66, 21);
		contentPane.add(textFieldRepeat);
		textFieldRepeat.setColumns(10);

		textFieldDuration = new JTextField();
		textFieldDuration.setText("-1");
		textFieldDuration.setBounds(227, 70, 66, 21);
		contentPane.add(textFieldDuration);
		textFieldDuration.setColumns(10);

		textFieldInterval = new JTextField();
		textFieldInterval.setBounds(346, 70, 66, 21);
		contentPane.add(textFieldInterval);
		textFieldInterval.setColumns(10);
		
	
		

		messages = new ArrayList<>();
		// data = null;
	}

	private void exit() {
		this.setVisible(false);
		if(messages!=null){
			list.setSelectedIndex(0);
			if(messages.size()!=0){
				Message message = messages.get(0);
				setMessageForView(message);
			}
		}
	}

	private Message getMessageFromView() {
		String titleString = textField_title.getText();
		int type = comboBox.getSelectedIndex();
		byte[] data = getContent();
		boolean receiveNext = chckbxReceiveNext.isSelected();
		boolean autoIncrease = chckbxAutoIncrease.isSelected();
		try {
			int interval, repeat, duration;
			if (textFieldInterval.getText().length() == 0)
				interval = 0;
			else
				interval = Integer.parseInt(textFieldInterval.getText());
			if (textFieldRepeat.getText().length() == 0)
				repeat = -1;
			else
				repeat = Integer.parseInt(textFieldRepeat.getText());
			if (textFieldDuration.getText().length() == 0)
				duration = -1;
			else
				duration = Integer.parseInt(textFieldDuration.getText());
			if (duration == -1 && repeat == -1)
				return null;
			if (duration != -1)
				repeat = -1;
			Message message = new Message(titleString, type, data, receiveNext,
					autoIncrease, interval, duration, repeat);
			return message;
		}
		catch (NumberFormatException e) {
			// TODO: handle exception
			return null;
		}
	}

	private void setMessageForView(Message message) {
		String titleString = message.getTitle();
		int type = message.getType();
		byte[] data = message.getData();
		boolean receiveNext = message.isReceiveNext();
		boolean autoIncrease = message.isAutoIncrease();
		int interval = message.getInterval();
		int repeat = message.getRepeat();
		int duration = message.getDuration();
		textField_title.setText(titleString);
		comboBox.setSelectedIndex(type);
		try {
			setContent(data, type);
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		chckbxReceiveNext.setSelected(receiveNext);
		chckbxAutoIncrease.setSelected(autoIncrease);
		textFieldInterval.setText(Integer.toString(interval));
		textFieldDuration.setText(Integer.toString(duration));
		textFieldRepeat.setText(Integer.toString(repeat));
	}

	private void selectedChanged() {
		int cur = list.getSelectedIndex();
		if (cur == -1)
			return;
		if (cur >= messages.size())
			return;
		setMessageForView(messages.get(cur));
	}

	private void modify() {
		int cur = list.getSelectedIndex();
		if (cur == -1)
			return;
		if (cur >= messages.size())
			return;
		byte[] data = getContent();
		if (data == null)
			return;
		if (data.length == 0)
			return;
		Message message = getMessageFromView();
		if (message == null)
			return;
		messages.set(cur, message);
		model.set(cur, message.getTitle());
	}

	private void deleteMessage() {
		int cur = list.getSelectedIndex();
		if (cur == -1)
			return;
		if (cur >= messages.size())
			return;
		model.remove(cur);
		// list.remove(cur);
		messages.remove(cur);
		textField_title.setText("");
		textArea_content.setText("");
	}

	private void addMessage() {
		byte[] data = getContent();
		if (data == null)
			return;
		if (data.length == 0)
			return;
		Message message = getMessageFromView();
		if (message == null)
			return;
		messages.add(message);
		model.addElement(message.getTitle());

		String val = new String(data);
		System.out.println(data);
		System.out.println(val);
		System.out.println(val.length());
	}

	private byte[] getContent() {
		String valString = textArea_content.getText();
		if (valString.length() == 0) {
			return new byte[0];
		}
		byte[] data = null;
		switch (comboBox.getSelectedIndex()) {
		case 0:
			data = valString.getBytes();
			break;
		case 1:
			try {
				data = valString.getBytes("UTF8");
			}
			catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
				data = null;
				showMessage("无效UTF8编码");
			}
			break;
		case 2:
			try {
				data = ByteHex.hexStr2(valString);
				System.out.println(ByteHex.toHexStr(data));
			}
			catch (Exception e1) {
				// TODO Auto-generated catch block
				// e1.printStackTrace();
				data = null;
				showMessage("无效HEX编码");
			}
			break;
		case 3:
			data = ByteHex.Base64Decode(valString.getBytes());
			break;
		default:
			data = null;
			break;
		}
		return data;
	}

	private void setContent(byte[] data, int type) throws Exception {
		String valString;
		if (data == null){
			valString = "";
			textArea_content.setText(valString);
			return ;
		}else if (data.length == 0) {
			valString = "";
			textArea_content.setText(valString);
			return ;
		}
		switch (type) {
		case 0:
			valString = new String(data);
			break;
		case 1:
			valString = new String(data, "UTF8");
			break;
		case 2:
			valString = ByteHex.toHexStr(data);
			break;
		case 3:
			valString = new String(ByteHex.Base64Encode(data));
			break;
		default:
			valString = "";
			break;
		}
		textArea_content.setText(valString);
	}

	private void update() {
		model.clear();
		for (int i = 0; i < messages.size(); i++) {
			model.addElement(messages.get(i).getTitle());
		}
		textField_title.setText("");
		textArea_content.setText("");
	}

	private void importD() {
		JFileChooser jf = new JFileChooser();
		jf.setFileFilter(new FileFilter() {
			@Override
			public boolean accept(File f) {
				if (f.isDirectory())
					return true;
				String name = f.getName();
				int p = name.lastIndexOf('.');
				if (p == -1)
					return false;
				String suffix = name.substring(p + 1).toLowerCase();
				return suffix.compareTo("jsn") == 0;
			}

			@Override
			public String getDescription() {
				return "jsn files";
			}
		});
		jf.setDialogTitle("Open jsn file");
		int result = jf.showOpenDialog(this); // 打开"打开文件"对话框
		// int result = jf.showSaveDialog(this); // 打"开保存文件"对话框
		if (result == JFileChooser.APPROVE_OPTION) {
			File file = jf.getSelectedFile();
			System.out.println("read: " + file.getAbsolutePath());
			BufferedReader reader = null;
			String laststr = "";
			if (file != null) {
				try {
					reader = new BufferedReader(new FileReader(file));
					String tempString = null;
					while ((tempString = reader.readLine()) != null) {
						laststr = laststr + tempString;
					}
					reader.close();
				}
				catch (IOException e) {
					e.printStackTrace();
				}
				System.out.println(laststr);
				JSONArray array = JSONArray.fromObject(laststr);
				messages.clear();
				for (int i = 0; i < array.size(); i++) {
					JSONObject object = array.getJSONObject(i);
					messages.add(new Message(object));
				}
			}
		}
		update();
	}

	private void exportD() {
		JFileChooser jf = new JFileChooser();
		jf.setFileFilter(new FileFilter() {
			@Override
			public boolean accept(File f) {
				if (f.isDirectory())
					return true;
				String name = f.getName();
				int p = name.lastIndexOf('.');
				if (p == -1)
					return false;
				String suffix = name.substring(p + 1).toLowerCase();
				return suffix.compareTo("jsn") == 0;
			}

			@Override
			public String getDescription() {
				return "jsn files";
			}
		});
		jf.setDialogTitle("Save jsn file");
		// int result = jf.showOpenDialog(this); // 打开"打开文件"对话框
		int result = jf.showSaveDialog(this); // 打"开保存文件"对话框
		if (result == JFileChooser.APPROVE_OPTION) {
			File file = jf.getSelectedFile();
			String f = file.getAbsolutePath();
			if (!f.toUpperCase().endsWith(".JSN")) {
				file = new File(f + ".jsn");
				f = file.getAbsolutePath();
			}
			System.out.println("save: " + f);
			try {
				FileWriter out = new FileWriter(f);
				JSONArray array = new JSONArray();
				for (int i = 0; i < messages.size(); i++) {
					JSONObject jsonObject = messages.get(i).toJsonObject();
					array.add(jsonObject);
				}
				out.write(array.toString());
				out.close();
			}
			catch (Exception e) {

			}
		}
	}

	private void showMessage(String message) {
		JOptionPane.showMessageDialog(new JPanel(), message);
	}

	public ArrayList<Message> getData() {
		return messages;
	}
	
	public void loadModel(byte[] modeldata){
		
		String data = "";
		int temp;
		System.out.println("loadmodel");
		for (int i = 0; i < modeldata.length; i++){
			System.out.print(modeldata[i] + " ");
			temp = 0;
			if (modeldata[i] < 0)
				temp = modeldata[i] + 256;
			else
				temp = modeldata[i];
			int high = temp / 16;
			int low = temp % 16;
			
			data = data + (high > 9 ? (char)('A' + high - 10) : (high + "")) + (low > 9 ? (char)('A' + low - 10) : (low + ""));
		}
		System.out.println("\nloadmodelend");
		textArea_content.setText(data);
		comboBox.setSelectedIndex(2);
	}
	JButton button_load_udp = new JButton("udp");
	JButton button_load_arp = new JButton("arp");
	JButton button_load_icmp = new JButton("icmp");

	private udpPanel udp_pan;
	private arpPan arp_panel;
	private icmpPan icmp_panel;
	
	public void setDataType(String type, SetData frame){

		int t = Integer.parseInt(type);
		System.out.println(t);
		button_load_udp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (udp_pan == null){
					udp_pan = new udpPanel(frame);
					//udp_pan.udpPanelShow(frame);
				}else 
					udp_pan.setVisible(true);
				
				
			}
		});
		button_load_udp.setBounds(450, 69, 64, 23);
		
		button_load_arp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (arp_panel == null){
					arp_panel = new arpPan(frame);
					//arp_panel.arpPanelShow(frame);
				}else 
				arp_panel.setVisible(true);
	
			}
		});
		button_load_arp.setBounds(450, 69, 64, 23);
		
		
		button_load_icmp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(icmp_panel == null){
				icmp_panel = new icmpPan(frame);
				//icmp_panel.icmpPanelShow(frame);
			}else 
			icmp_panel.setVisible(true);
			}
		});
		button_load_icmp.setBounds(450, 69, 64, 23);
		
		contentPane.add(button_load_udp);
		contentPane.add(button_load_arp);
		contentPane.add(button_load_icmp);
		
//		if (t == 1){
////			contentPane.add(button_load_udp);
//			button_load_udp.setVisible(false);
//			button_load_arp.setVisible(false);
//			button_load_icmp.setVisible(false);
//			button_load_udp.setEnabled(false);
//			button_load_arp.setEnabled(false);
//			button_load_icmp.setEnabled(false);
//		}
		 if (t == 2){
//			contentPane.add(button_load_arp);
			button_load_udp.setVisible(false);
			button_load_arp.setVisible(true);
			button_load_icmp.setVisible(false);
			button_load_udp.setEnabled(false);
			button_load_arp.setEnabled(true);
			button_load_icmp.setEnabled(false);
			textArea_content.setEnabled(false);
//			textArea_content.disable();
		}
		else if (t == 3){
//			contentPane.add(button_load_icmp);
			button_load_udp.setVisible(false);
			button_load_arp.setVisible(false);
			button_load_icmp.setVisible(true);
			button_load_udp.setEnabled(false);
			button_load_arp.setEnabled(false);
			button_load_icmp.setEnabled(true);
			textArea_content.setEnabled(false);
		}
		else{
			button_load_udp.setVisible(false);
			button_load_arp.setVisible(false);
			button_load_icmp.setVisible(false);
			button_load_udp.setEnabled(false);
			button_load_arp.setEnabled(false);
			button_load_icmp.setEnabled(false);
			textArea_content.setEnabled(true);
		}
	}
	
	
	

	
	
	public void setArpData(short operation, byte[] sendermac, byte[] senderip,
			byte[] recmac, byte[] recip) {
		this.arpData.hardtype = ARPPacket.HARDTYPE_ETHER;
		this.arpData.prototype = ARPPacket.PROTOTYPE_IP;
		this.arpData.operation = operation;
		this.arpData.hlen = 6;
		this.arpData.plen = 4;
		this.arpData.sender_hardaddr = sendermac;
		this.arpData.sender_protoaddr = senderip;
		this.arpData.target_hardaddr = recmac;
		this.arpData.target_protoaddr = recip;
		this.arpData.caplen = 60;
	}
	
	public void setIcmpData(byte[] data, String text){
		//add type, 0:ICMP_ECHOREPLY 8:ICMP_ECHO
		this.icmpData.add(data[0]);
		//add code, 0
		this.icmpData.add(data[1]);
		//add checksum
		int checksum = 0;
		checksum = data[2] < 0 ? ((int)data[2] + 256) : (int)data[2]; 
		checksum *= 256;
		checksum += data[3] < 0 ? ((int)data[3] + 256) : (int)data[3];
		this.icmpData.add((short)checksum);
		//add seq & id 0x0100 
		this.icmpData.add((short)256);
		this.icmpData.add((short)256);
		this.icmpData.add(text);
	}
	
	public ARPPacket getArpData(){
		return arpData;
	}
	
	
	public ArrayList<Object> getIcmpData(){
		return icmpData;
	}
}
