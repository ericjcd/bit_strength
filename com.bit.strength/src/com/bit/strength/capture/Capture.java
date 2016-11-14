package com.bit.strength.capture;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileFilter;

import jpcap.JpcapCaptor;
import jpcap.JpcapWriter;
import jpcap.NetworkInterface;
import jpcap.packet.Packet;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import com.bit.strength.Activator;
import com.bit.strength.stasis.IperfConfig;
import com.bit.strength.util.PacketCountPair;
import com.bit.strength.util.PacketDecode;

import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Combo;

public class Capture extends ViewPart {
	private TableViewer tableViewer;
	ArrayList<PacketCountPair> contentBakUp = null;
	// ArrayList<RawPacket> rawContent = null;
	private Action statusAction;

	private boolean docapture = false;
	private Thread thread = null;

	private JpcapCaptor m_pcap = null;
	private Text srcIPText;
	private Text desIPText;
	private Text srcMACText;
	private Text desMACText;
	private Text srcPortText;
	private Text desPortText;
	private Button btnIp, btnIcmp, btnIgmp, btnTcp, btnUdp, btnArp;
	private Button buttonStart, buttonStop, buttonFilter;
	private Button buttonExport, buttonImport;
	private Text regixText;
	private Combo combo;
	private NetworkInterface[] devices = JpcapCaptor.getDeviceList();

	// private String[] devs = null;

	class ViewContentProvider implements IStructuredContentProvider {
		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}

		public void dispose() {
		}

		public Object[] getElements(Object parent) {
			if (parent instanceof ArrayList<?>) {
				ArrayList<PacketCountPair> list = (ArrayList<PacketCountPair>) parent;
				return list.toArray();
			}
			return new String[] { "one" };
		}
	}

	class ViewLabelProvider extends LabelProvider implements
			ITableLabelProvider {
		private String label[] = { "\u7F16\u53F7", "\u65F6\u95F4", "\u6E90IP",
				"\u76EE\u7684IP", "\u6E90MAC", "\u76EE\u7684MAC",
				"\u5305\u7C7B\u578B", "\u6e90\u7aef\u53e3",
				"\u76ee\u6807\u7aef\u53e3", "\u5305\u5185\u5BB9", "Hex" };

		public String getColumnText(Object obj, int index) {
			if (obj instanceof PacketCountPair) {
				PacketCountPair packetpair = (PacketCountPair) obj;
				switch (index) {
				case 0:// counter
					return Integer.toString(packetpair.getCount());
				case 1:
					return PacketDecode.getTime(packetpair.getPacket());
				case 2:
					return PacketDecode.getSrcIP(packetpair.getPacket());
				case 3:
					return PacketDecode.getDesIP(packetpair.getPacket());
				case 4:
					return PacketDecode.getSrcMAC(packetpair.getPacket());
				case 5:
					return PacketDecode.getDesMAC(packetpair.getPacket());
				case 6:
					return PacketDecode.getType(packetpair.getPacket());
				case 7:
					return PacketDecode.getSrcPort(packetpair.getPacket());
				case 8:
					return PacketDecode.getDesPort(packetpair.getPacket());
				case 9:
					return PacketDecode.getContent(packetpair.getPacket());
				case 10:
					return PacketDecode.getHexContent(packetpair.getPacket());
				default:
					return "error";
				}
			} else
				return label[index];
		}

		public Image getColumnImage(Object obj, int index) {
			return null;
		}
	}

	class NameSorter extends ViewerSorter {
		public int compare(Viewer viewer, Object e1, Object e2) {
			PacketCountPair packetpair1 = (PacketCountPair) e1;
			PacketCountPair packetpair2 = (PacketCountPair) e2;
			Integer val1 = packetpair1.getCount();
			Integer val2 = packetpair2.getCount();
			return val2.compareTo(val1);
		}
	}

	public Capture() {
		// TODO Auto-generated constructor stub
		// rawContent = new ArrayList<RawPacket>();
		System.out.println("Available network devices on your machine:");
		// devs = PacketCapture.lookupDevices();
		System.out.println(devices.length);
		for (int i = 0; i < devices.length; i++)
			System.out.println(devices[i].description);

		try {
			UIManager
					.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		}
		catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new FillLayout(SWT.HORIZONTAL));

		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new FormLayout());

		Composite composite_panel = new Composite(composite, SWT.NONE);
		composite_panel.setLayout(new FillLayout(SWT.VERTICAL));
		FormData fd_composite_panel = new FormData();
		fd_composite_panel.top = new FormAttachment(0);
		fd_composite_panel.left = new FormAttachment(0);
		fd_composite_panel.right = new FormAttachment(100);
		fd_composite_panel.bottom = new FormAttachment(20);
		composite_panel.setLayoutData(fd_composite_panel);

		Composite composite_1 = new Composite(composite_panel, SWT.NONE);
		composite_1.setBackground(SWTResourceManager
				.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		RowLayout rl_composite_1 = new RowLayout(SWT.HORIZONTAL);
		rl_composite_1.center = true;
		composite_1.setLayout(rl_composite_1);

		Label lblip = new Label(composite_1, SWT.NONE);
		lblip.setBackground(SWTResourceManager
				.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		lblip.setLayoutData(new RowData(40, SWT.DEFAULT));
		lblip.setText("\u6E90IP");

		srcIPText = new Text(composite_1, SWT.BORDER);
		srcIPText.setLayoutData(new RowData(100, SWT.DEFAULT));

		Label lblmac = new Label(composite_1, SWT.NONE);
		lblmac.setBackground(SWTResourceManager
				.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		lblmac.setLayoutData(new RowData(55, SWT.DEFAULT));
		lblmac.setText("\u6E90MAC");

		srcMACText = new Text(composite_1, SWT.BORDER);
		srcMACText.setLayoutData(new RowData(80, SWT.DEFAULT));

		Label label = new Label(composite_1, SWT.NONE);
		label.setBackground(SWTResourceManager
				.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		label.setLayoutData(new RowData(60, SWT.DEFAULT));
		label.setText("\u6E90\u7AEF\u53E3");

		srcPortText = new Text(composite_1, SWT.BORDER);
		srcPortText.setLayoutData(new RowData(70, SWT.DEFAULT));

		buttonStart = new Button(composite_1, SWT.NONE);
		buttonStart.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				startCapture();
			}
		});
		buttonStart.setText("\u5F00\u59CB");

		Label label_3 = new Label(composite_1, SWT.NONE);
		label_3.setBackground(SWTResourceManager
				.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		label_3.setLayoutData(new RowData(40, SWT.DEFAULT));
		label_3.setText("\u6B63\u5219\u5F0F");

		regixText = new Text(composite_1, SWT.BORDER);

		buttonFilter = new Button(composite_1, SWT.NONE);
		buttonFilter.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				filter();
			}
		});
		buttonFilter.setText("\u8FC7\u6EE4");

		buttonExport = new Button(composite_1, SWT.NONE);
		buttonExport.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				exportD();
			}
		});
		buttonExport.setText("\u5BFC\u51FA");

		buttonImport = new Button(composite_1, SWT.NONE);
		buttonImport.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				importD();
			}
		});
		buttonImport.setText("\u5BFC\u5165");

		Composite composite_2 = new Composite(composite_panel, SWT.NONE);
		composite_2.setBackground(SWTResourceManager
				.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		RowLayout rl_composite_2 = new RowLayout(SWT.HORIZONTAL);
		rl_composite_2.center = true;
		composite_2.setLayout(rl_composite_2);

		Label lblip_1 = new Label(composite_2, SWT.NONE);
		lblip_1.setBackground(SWTResourceManager
				.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		lblip_1.setLayoutData(new RowData(40, SWT.DEFAULT));
		lblip_1.setText("\u76EE\u7684IP");

		desIPText = new Text(composite_2, SWT.BORDER);
		desIPText.setLayoutData(new RowData(100, SWT.DEFAULT));
		desIPText.setText("");

		Label lblmac_1 = new Label(composite_2, SWT.NONE);
		lblmac_1.setBackground(SWTResourceManager
				.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		lblmac_1.setLayoutData(new RowData(55, SWT.DEFAULT));
		lblmac_1.setText("\u76EE\u7684MAC");

		desMACText = new Text(composite_2, SWT.BORDER);
		desMACText.setLayoutData(new RowData(80, SWT.DEFAULT));

		Label label_1 = new Label(composite_2, SWT.NONE);
		label_1.setBackground(SWTResourceManager
				.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		label_1.setLayoutData(new RowData(60, SWT.DEFAULT));
		label_1.setText("\u76EE\u7684\u7AEF\u53E3");

		desPortText = new Text(composite_2, SWT.BORDER);
		desPortText.setLayoutData(new RowData(70, SWT.DEFAULT));
		desPortText.setText("");

		buttonStop = new Button(composite_2, SWT.NONE);
		buttonStop.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				stopCapture();
			}
		});
		buttonStop.setText("\u505C\u6B62");

		Label label_4 = new Label(composite_2, SWT.NONE);
		label_4.setBackground(SWTResourceManager
				.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		label_4.setText("\u7F51\u5361");

		combo = new Combo(composite_2, SWT.READ_ONLY);
		if (devices != null) {
			for (int i = 0; i < devices.length; i++) {
				String curString = devices[i].description;
				int cut;
				if ((cut = curString.indexOf('\n')) != -1) {
					curString = curString.substring(0, cut);
				}
				combo.add(curString);
			}
		}

		Composite composite_3 = new Composite(composite_panel, SWT.NONE);
		composite_3.setBackground(SWTResourceManager
				.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		RowLayout rl_composite_3 = new RowLayout(SWT.HORIZONTAL);
		rl_composite_3.center = true;
		composite_3.setLayout(rl_composite_3);

		Label label_2 = new Label(composite_3, SWT.NONE);
		label_2.setBackground(SWTResourceManager
				.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		label_2.setLayoutData(new RowData(55, SWT.DEFAULT));
		label_2.setText("\u6293\u53D6\u534F\u8BAE");

		btnIp = new Button(composite_3, SWT.CHECK);
		btnIp.setSelection(true);
		btnIp.setLayoutData(new RowData(55, SWT.DEFAULT));
		btnIp.setText("IP");

		btnIcmp = new Button(composite_3, SWT.CHECK);
		btnIcmp.setSelection(true);
		btnIcmp.setLayoutData(new RowData(55, SWT.DEFAULT));
		btnIcmp.setText("ICMP");

		btnIgmp = new Button(composite_3, SWT.CHECK);
		btnIgmp.setSelection(true);
		btnIgmp.setLayoutData(new RowData(55, SWT.DEFAULT));
		btnIgmp.setText("IGMP");

		btnTcp = new Button(composite_3, SWT.CHECK);
		btnTcp.setSelection(true);
		btnTcp.setLayoutData(new RowData(55, SWT.DEFAULT));
		btnTcp.setText("TCP");

		btnUdp = new Button(composite_3, SWT.CHECK);
		btnUdp.setSelection(true);
		btnUdp.setLayoutData(new RowData(55, SWT.DEFAULT));
		btnUdp.setText("UDP");

		btnArp = new Button(composite_3, SWT.CHECK);
		btnArp.setSelection(true);
		btnArp.setLayoutData(new RowData(55, SWT.DEFAULT));
		btnArp.setText("ARP");

		Label lblipmac = new Label(composite_3, SWT.NONE);
		lblipmac.setBackground(SWTResourceManager
				.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		lblipmac.setText("\u7A7A\u503C\u4E3A\u4EFB\u610FIP\u6216Mac\u6216\u7AEF\u53E3");

		Composite composite_table = new Composite(composite, SWT.NONE);
		composite_table.setLayout(new FillLayout(SWT.HORIZONTAL));
		FormData fd_composite_table = new FormData();
		fd_composite_table.top = new FormAttachment(20);
		fd_composite_table.bottom = new FormAttachment(100);
		fd_composite_table.left = new FormAttachment(0);
		fd_composite_table.right = new FormAttachment(100);
		composite_table.setLayoutData(fd_composite_table);

		tableViewer = new TableViewer(composite_table, SWT.BORDER
				| SWT.FULL_SELECTION);

		TableViewerColumn tableViewerColumn = new TableViewerColumn(
				tableViewer, SWT.NONE);
		TableColumn tblclmnID = tableViewerColumn.getColumn();
		tblclmnID.setWidth(100);
		tblclmnID.setText("\u7F16\u53F7");

		TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(
				tableViewer, SWT.NONE);
		TableColumn tblclmnTime = tableViewerColumn_1.getColumn();
		tblclmnTime.setWidth(100);
		tblclmnTime.setText("\u65F6\u95F4");

		TableViewerColumn tableViewerColumn_2 = new TableViewerColumn(
				tableViewer, SWT.NONE);
		TableColumn tblclmnSip = tableViewerColumn_2.getColumn();
		tblclmnSip.setWidth(100);
		tblclmnSip.setText("\u6E90IP");

		TableViewerColumn tableViewerColumn_3 = new TableViewerColumn(
				tableViewer, SWT.NONE);
		TableColumn tblclmnDip = tableViewerColumn_3.getColumn();
		tblclmnDip.setWidth(100);
		tblclmnDip.setText("\u76EE\u7684IP");

		TableViewerColumn tableViewerColumn_4 = new TableViewerColumn(
				tableViewer, SWT.NONE);
		TableColumn tblclmnSmac = tableViewerColumn_4.getColumn();
		tblclmnSmac.setWidth(100);
		tblclmnSmac.setText("\u6E90MAC");

		TableViewerColumn tableViewerColumn_5 = new TableViewerColumn(
				tableViewer, SWT.NONE);
		TableColumn tblclmnDmac = tableViewerColumn_5.getColumn();
		tblclmnDmac.setWidth(100);
		tblclmnDmac.setText("\u76EE\u7684MAC");

		TableViewerColumn tableViewerColumn_6 = new TableViewerColumn(
				tableViewer, SWT.NONE);
		TableColumn tblclmnType = tableViewerColumn_6.getColumn();
		tblclmnType.setWidth(100);
		tblclmnType.setText("\u5305\u7C7B\u578B");

		TableViewerColumn tableViewerColumn_7 = new TableViewerColumn(
				tableViewer, SWT.NONE);
		TableColumn tblclmnSport = tableViewerColumn_7.getColumn();
		tblclmnSport.setWidth(100);
		tblclmnSport.setText("\u6E90\u7AEF\u53E3");

		TableViewerColumn tableViewerColumn_8 = new TableViewerColumn(
				tableViewer, SWT.NONE);
		TableColumn tblclmnDport = tableViewerColumn_8.getColumn();
		tblclmnDport.setWidth(100);
		tblclmnDport.setText("\u76EE\u7684\u7AEF\u53E3");

		TableViewerColumn tableViewerColumn_9 = new TableViewerColumn(
				tableViewer, SWT.NONE);
		TableColumn tblclmnContent = tableViewerColumn_9.getColumn();
		tblclmnContent.setWidth(100);
		tblclmnContent.setText("\u5305\u5185\u5BB9");

		TableViewerColumn tableViewerColumn_10 = new TableViewerColumn(
				tableViewer, SWT.NONE);
		TableColumn tblclmnHex = tableViewerColumn_10.getColumn();
		tblclmnHex.setWidth(100);
		tblclmnHex.setText("Hex");
		// TODO Auto-generated method stub

		tableViewer.setContentProvider(new ViewContentProvider());
		tableViewer.setLabelProvider(new ViewLabelProvider());
		tableViewer.setSorter(new NameSorter());
		tableViewer.setInput(new ArrayList<>());
		tableViewer.getTable().setHeaderVisible(true);
		tableViewer.getTable().setLinesVisible(true);

		Table table = tableViewer.getTable();
		TableEditor editor = new TableEditor(table);
		editor.horizontalAlignment = SWT.LEFT;
		editor.grabHorizontal = true;
		table.addMouseListener(new MouseAdapter() {
			public void mouseDown(MouseEvent event) {
				Control old = editor.getEditor();
				if (old != null)
					old.dispose();

				Point pt = new Point(event.x, event.y);
				final TableItem item = table.getItem(pt);
				if (item == null) {
					return;
				}
				int column = -1;
				for (int i = 0, n = table.getColumnCount(); i < n; i++) {
					Rectangle rect = item.getBounds(i);
					if (rect.contains(pt)) {
						column = i;
						break;
					}
				}
				final Text text = new Text(table, SWT.NONE);
				text.setForeground(item.getForeground());

				text.setText(item.getText(column));
				text.setForeground(item.getForeground());
				text.selectAll();
				text.setFocus();

				editor.minimumWidth = text.getBounds().width;
				editor.setEditor(text, item, column);

				final int col = column;
				text.addModifyListener(new ModifyListener() {
					public void modifyText(ModifyEvent event) {
						// item.setText(col, text.getText());
						// System.out.println("Text modified to " +
						// text.getText());
					}
				});
			}
		});

		lock(true);

		PlatformUI.getWorkbench().getHelpSystem()
				.setHelp(tableViewer.getControl(), "com.bit.strength.viewer");
		makeActions();
		hookContextMenu();
		contributeToActionBars();
	}

	private void lock(boolean statue) {
		Display.getDefault().syncExec(new Runnable() {

			@Override
			public void run() {
				srcIPText.setEnabled(statue);
				srcMACText.setEnabled(statue);
				srcPortText.setEnabled(statue);
				desIPText.setEnabled(statue);
				desMACText.setEnabled(statue);
				desPortText.setEnabled(statue);
				btnArp.setEnabled(statue);
				btnIcmp.setEnabled(statue);
				btnIgmp.setEnabled(statue);
				btnIp.setEnabled(statue);
				btnTcp.setEnabled(statue);
				btnUdp.setEnabled(statue);
				buttonStart.setEnabled(statue);
				buttonFilter.setEnabled(statue);
				buttonImport.setEnabled(statue);
				buttonExport.setEnabled(statue);
				regixText.setEnabled(statue);
				buttonStop.setEnabled(!statue);
				combo.setEnabled(statue);
			}
		});
	}

	private String getFilter() {
		// filter;
		boolean andflag = false;
		String filter = "";
		if (!srcIPText.getText().equals("")) {
			filter += " src host " + srcIPText.getText();
			andflag = true;
		}
		if (!desIPText.getText().equals("")) {
			if (andflag)
				filter += " and";
			filter += " dst host " + desIPText.getText();
			andflag = true;
		}
		if (!srcMACText.getText().equals("")) {
			if (andflag)
				filter += " and";
			filter += " ether src " + srcMACText.getText();
			andflag = true;
		}
		if (!desMACText.getText().equals("")) {
			if (andflag)
				filter += " and";
			filter += " ether dst " + desMACText.getText();
			andflag = true;
		}
		if (!srcPortText.getText().equals("")) {
			if (andflag)
				filter += " and";
			filter += " src port " + srcPortText.getText();
			andflag = true;
		}
		if (!desPortText.getText().equals("")) {
			if (andflag)
				filter += " and";
			filter += " dst port " + desPortText.getText();
			andflag = true;
		}

		if (btnArp.getSelection() || btnIcmp.getSelection()
				|| btnIgmp.getSelection() || btnIp.getSelection()
				|| btnTcp.getSelection() || btnUdp.getSelection()) {
			if (andflag) {
				filter += " and ";
			}
			filter += " ( ";
			boolean orflag = false;
			if (btnIp.getSelection()) {
				filter += " ip ";
				orflag = true;
			}
			if (btnIcmp.getSelection()) {
				if (orflag)
					filter += " or ";
				filter += " icmp ";
				orflag = true;
			}
			if (btnIgmp.getSelection()) {
				if (orflag)
					filter += " or ";
				filter += " igmp ";
				orflag = true;
			}
			if (btnTcp.getSelection()) {
				if (orflag)
					filter += " or ";
				filter += " tcp ";
				orflag = true;
			}
			if (btnUdp.getSelection()) {
				if (orflag)
					filter += " or ";
				filter += " udp ";
				orflag = true;
			}
			if (btnArp.getSelection()) {
				if (orflag)
					filter += " or ";
				filter += " arp ";
				orflag = true;
			}
			filter += " ) ";
		}
		return filter;
	}

	private void startCapture() {
		if (combo.getSelectionIndex() == -1)
			return;
		m_pcap = configPacketCapture(devices[combo.getSelectionIndex()]);
		// showMessage("capture package");
		if (docapture) {
			docapture = false;
			return;
		}
		((ArrayList<?>) tableViewer.getInput()).clear();
		tableViewer.refresh();
		// rawContent.clear();
		// lock component
		lock(false);
		// filter
		String filter = getFilter();
		System.out.println("filter:"+filter);
		// start
		contentBakUp = null;
		docapture = true;
		thread = new Thread(new CaptureRunnable(m_pcap, filter,
				new PacketHandler(tableViewer, this, m_pcap)));
		thread.start();
	}

	private void stopCapture() {
		if (docapture) {
			m_pcap.breakLoop();
			docapture = false;
			// unlock
			lock(true);
		}
		contentBakUp = (ArrayList<PacketCountPair>) tableViewer.getInput();
	}

	private void filter() {
		if (contentBakUp == null)
			return;
		String pat = regixText.getText();
		Pattern pattern = Pattern.compile(pat);
		ArrayList<PacketCountPair> curContent = new ArrayList<>();
		for (int i = 0; i < contentBakUp.size(); i++) {
			Packet packet = contentBakUp.get(i).getPacket();
			Matcher matcher = pattern.matcher(PacketDecode.getContent(packet));
			if (matcher.matches()) {
				curContent.add(contentBakUp.get(i));
			}
		}
		tableViewer.setInput(curContent);
		tableViewer.refresh();
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
				return suffix.compareTo("pcap") == 0;
			}

			@Override
			public String getDescription() {
				return "pcap files";
			}
		});
		jf.setDialogTitle("Save pcap file");
		// int result = jf.showOpenDialog(this); // 打开"打开文件"对话框
		int result = jf.showSaveDialog(null); // 打"开保存文件"对话框
		if (result == JFileChooser.APPROVE_OPTION) {
			File file = jf.getSelectedFile();
			String f = file.getAbsolutePath();
			if (!f.toUpperCase().endsWith(".PCAP")) {
				file = new File(f + ".pcap");
				f = file.getAbsolutePath();
			}
			System.out.println("save " + contentBakUp.size() + ": " + f);
			try {
				JpcapCaptor captor = JpcapCaptor.openDevice(devices[0], 65535, false, 5000);
				captor.setNonBlockingMode(false);
				JpcapWriter writer = JpcapWriter.openDumpFile(captor, f);
				/*
				 * for (int i = 0; i < 10; i++)
				 * writer.writePacket(captor.getPacket()); writer.close();
				 */
				for (int i = 0; i < contentBakUp.size(); i++) {
					writer.writePacket(contentBakUp.get(i).getPacket());
				}
				writer = null;
				/*
				 * TcpdumpWriter.writeHeader(f, TcpdumpWriter.LITTLE_ENDIAN,
				 * 96); for (int i = 0; i < contentBakUp.size(); i++) {
				 * TcpdumpWriter.appendPacket(f, rawContent.get(i),
				 * TcpdumpWriter.LITTLE_ENDIAN); }
				 */
			}
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void importD() {
		((ArrayList<?>) tableViewer.getInput()).clear();
		tableViewer.refresh();
		// rawContent.clear();
		docapture = true;
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
				return suffix.compareTo("pcap") == 0;
			}

			@Override
			public String getDescription() {
				return "pcap files";
			}
		});
		jf.setDialogTitle("Open pcap file");
		int result = jf.showOpenDialog(null); // 打开"打开文件"对话框
		// int result = jf.showSaveDialog(this); // 打"开保存文件"对话框
		if (result == JFileChooser.APPROVE_OPTION) {
			File file = jf.getSelectedFile();
			String f = file.getAbsolutePath();
			System.out.println("read: " + file.getAbsolutePath());

			Thread thread = new Thread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					lock(false);
					try {
						JpcapCaptor captor = JpcapCaptor.openFile(f);
						captor.setNonBlockingMode(false);
						PacketHandler handler = new PacketHandler(tableViewer,
								this, m_pcap);
						captor.loopPacket(-1, handler);
						captor.close();
					}
					catch (IOException e) {
						System.out.println("fail to open file");
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					finally {
						lock(true);
					}
				}
			});
			thread.start();
			/*
			 * PacketCapture pcap = new PacketCapture(); try {
			 * pcap.openOffline(f); pcap.setFilter("", true);
			 * pcap.addPacketListener(new PacketHandler(tableViewer, this,
			 * m_pcap)); pcap.addRawPacketListener(new
			 * RawPacketHandler(rawContent)); pcap.capture(-1); } catch
			 * (CaptureFileOpenException | InvalidFilterException |
			 * CapturePacketException e) { // TODO Auto-generated catch block
			 * e.printStackTrace(); }
			 */
		}
		contentBakUp = (ArrayList<PacketCountPair>) tableViewer.getInput();
		docapture = false;
	}

	private void makeActions() {
		statusAction = new Action() {
			public void run() {
				// showMessage("statusAction executed");
				IperfConfig frame = new IperfConfig();
				frame.setVisible(true);
				frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			}
		};
		statusAction.setText("查看网络状态");
		statusAction.setToolTipText("查看网络状态");

		URL statusimageURL = Platform.getBundle(Activator.PLUGIN_ID).getEntry(
				"icons\\status.png");
		statusAction.setImageDescriptor(ImageDescriptor
				.createFromURL(statusimageURL));
	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				Capture.this.fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(tableViewer.getControl());
		tableViewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, tableViewer);
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalPullDown(IMenuManager manager) {
		// manager.add(action1);
		// manager.add(new Separator());
		// manager.add(action2);
	}

	private void fillContextMenu(IMenuManager manager) {
		manager.add(statusAction);
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(statusAction);
	}

	private JpcapCaptor configPacketCapture(NetworkInterface m_device) {
		JpcapCaptor m_pcap = null;
		try {
			m_pcap = JpcapCaptor.openDevice(m_device, 65535, false, 2000);
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("the devices cannot be open");
			e.printStackTrace();
		}
		return m_pcap;
	}

	private void showMessage(String message) {
		MessageDialog.openInformation(tableViewer.getControl().getShell(),
				"Capture", message);
	}

	public boolean getCapture() {
		return docapture;
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}
}
