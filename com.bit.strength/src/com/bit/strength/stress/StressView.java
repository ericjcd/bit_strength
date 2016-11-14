package com.bit.strength.stress;

import java.awt.Color;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import jpcap.packet.ARPPacket;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.experimental.chart.swt.ChartComposite;

import com.bit.strength.stress.config.RMIConfig;
import com.bit.strength.stress.config.SetData;
import com.bit.strength.stress.config.SetServer;
import com.bit.strength.stress.network.ARPArgs;
import com.bit.strength.stress.network.ICMPArgs;
import com.bit.strength.stress.network.NetArgs;
import com.bit.strength.stress.network.IPArgs;
import com.bit.strength.stress.network.ViewComponents;
import com.bit.strength.stress.network.NetTask;
import com.bit.strength.stress.network.RMI.LocalController;

import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.layout.RowData;

public class StressView extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "com.bit.strength.views.StressView";
	private Text text_IP;
	private Combo combo_TCPUDP;
	private SetData dataFrame = null;

	private Table table_log;
	private Table table_statistics;

	private TimeSeries sendSeries;
	private TimeSeries receiveSeries;
	private TimeSeries receiveByteSeries;
	private TimeSeries sendByteSeries;

	private ViewUpdater updater;
	private LocalController controller;

	private NetTask netTask;

	private Button button_addServer;
	private Button button_start;
	private Button button_stop;
	private Button button_data;
	private Button button_removeServer;
	private Spinner spinner_connection;
	private Spinner spinner_port;
	private Spinner spinner_localPort;

	/*
	 * The content provider class is responsible for providing objects to the
	 * view. It can wrap existing objects in adapters or simply return objects
	 * as-is. These objects may be sensitive to the current input of the view,
	 * or ignore it and always show the same content (like Task List, for
	 * example).
	 */

	/**
	 * The constructor.
	 */
	public StressView() {
		try {
			UIManager
					.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	@SuppressWarnings("deprecation")
	public void createPartControl(Composite parent) {
		parent.setLayout(new FillLayout());

		ScrolledComposite scrolledComposite = new ScrolledComposite(parent,
				SWT.H_SCROLL | SWT.V_SCROLL);
		// scrolledComposite.setLayout(new FillLayout(SWT.VERTICAL));
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		// scrolledComposite.setMinWidth(720);
		// scrolledComposite.setMinHeight(580);

		Composite composite = new Composite(scrolledComposite, SWT.NONE);
		scrolledComposite.setContent(composite);
		composite.setLayout(new FillLayout(SWT.VERTICAL));

		Composite composite_text = new Composite(composite, SWT.NONE);
		composite_text.setLayout(new FormLayout());

		Composite composite_panel = new Composite(composite_text, SWT.BORDER);
		composite_panel.setBackground(SWTResourceManager
				.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		FormData fd_composite_panel = new FormData();
		fd_composite_panel.bottom = new FormAttachment(15);
		fd_composite_panel.right = new FormAttachment(100);
		fd_composite_panel.top = new FormAttachment(0);
		fd_composite_panel.left = new FormAttachment(0);
		composite_panel.setLayoutData(fd_composite_panel);
		RowLayout rl_composite_panel = new RowLayout(SWT.HORIZONTAL);
		rl_composite_panel.spacing = 5;
		rl_composite_panel.center = true;
		composite_panel.setLayout(rl_composite_panel);

		Label label_6 = new Label(composite_panel, SWT.NONE);
		label_6.setBackground(SWTResourceManager
				.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		label_6.setText("\u7F51\u7EDC\u538B\u529B\uFF1A");

		combo_TCPUDP = new Combo(composite_panel, SWT.NONE);
		combo_TCPUDP.setItems(new String[] { "TCP", "UDP", "ARP", "ICMP" });
		combo_TCPUDP.select(0);

		Label label = new Label(composite_panel, SWT.NONE);
		label.setBackground(SWTResourceManager
				.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		label.setText("IP");

		text_IP = new Text(composite_panel, SWT.BORDER);

		Label label_1 = new Label(composite_panel, SWT.NONE);
		label_1.setBackground(SWTResourceManager
				.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		label_1.setText("\u7AEF\u53E3");

		spinner_port = new Spinner(composite_panel, SWT.BORDER);
		spinner_port.setMaximum(65536);

		Label label_3 = new Label(composite_panel, SWT.NONE);
		label_3.setBackground(SWTResourceManager
				.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		label_3.setText("\u672C\u5730\u7AEF\u53E3");

		spinner_localPort = new Spinner(composite_panel, SWT.BORDER);
		spinner_localPort.setMaximum(65536);
		spinner_localPort.setMinimum(-1);
		spinner_localPort.setSelection(-1);
		spinner_localPort.setLayoutData(new RowData(38, SWT.DEFAULT));

		Label label_2 = new Label(composite_panel, SWT.NONE);
		label_2.setBackground(SWTResourceManager
				.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		label_2.setText("\u8FDE\u63A5\u6570");

		spinner_connection = new Spinner(composite_panel, SWT.BORDER);

		button_data = new Button(composite_panel, SWT.NONE);
		button_data.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// set data
				int type = combo_TCPUDP.getSelectionIndex();
				System.out.println(type);
				setData(type);
			}
		});
		button_data.setText("\u6570\u636E");

		button_start = new Button(composite_panel, SWT.NONE);
		button_stop = new Button(composite_panel, SWT.NONE);

		button_start.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// start send
				startSend();
			}
		});
		button_start.setText("\u5F00\u59CB");

		button_stop.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// stop send
				stopSend();
				lock(true);
			}
		});
		button_stop.setText("\u505C\u6B62");

		button_addServer = new Button(composite_panel, SWT.NONE);
		button_addServer.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				addserver();
			}
		});
		button_addServer.setText("\u6DFB\u52A0\u670D\u52A1\u5668");

		button_removeServer = new Button(composite_panel, SWT.NONE);
		button_removeServer.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				removeServer();
			}
		});
		button_removeServer.setText("\u79FB\u9664\u670D\u52A1\u5668");

		Composite composite_statistics = new Composite(composite_text,
				SWT.BORDER);
		FormData fd_composite_statistics = new FormData();
		fd_composite_statistics.bottom = new FormAttachment(100);
		fd_composite_statistics.right = new FormAttachment(100);
		fd_composite_statistics.top = new FormAttachment(15);
		fd_composite_statistics.left = new FormAttachment(0);
		composite_statistics.setLayoutData(fd_composite_statistics);
		composite_statistics.setLayout(new FillLayout(SWT.HORIZONTAL));

		table_log = new Table(composite_statistics, SWT.BORDER
				| SWT.FULL_SELECTION | SWT.MULTI);
		table_log.setHeaderVisible(true);
		table_log.setLinesVisible(true);

		TableColumn tblclmnDelay = new TableColumn(table_log, SWT.NONE);
		tblclmnDelay.setWidth(70);
		tblclmnDelay.setText("\u65F6\u5EF6");

		TableColumn tblclmnReceive = new TableColumn(table_log, SWT.NONE);
		tblclmnReceive.setWidth(70);
		tblclmnReceive.setText("\u63A5\u53D7\u4E2A\u6570");

		TableColumn tblclmnSend = new TableColumn(table_log, SWT.NONE);
		tblclmnSend.setWidth(70);
		tblclmnSend.setText("\u53D1\u9001\u4E2A\u6570");

		TableColumn tblclmnReceivebyte = new TableColumn(table_log, SWT.NONE);
		tblclmnReceivebyte.setWidth(90);
		tblclmnReceivebyte.setText("\u63A5\u53D7\u5B57\u8282");

		TableColumn tblclmnSendbyte = new TableColumn(table_log, SWT.NONE);
		tblclmnSendbyte.setWidth(90);
		tblclmnSendbyte.setText("\u53D1\u9001\u5B57\u8282");

		table_statistics = new Table(composite_statistics, SWT.BORDER
				| SWT.FULL_SELECTION);
		table_statistics.setHeaderVisible(true);
		table_statistics.setLinesVisible(true);

		TableColumn tblclmnItem = new TableColumn(table_statistics, SWT.NONE);
		tblclmnItem.setText("slave IP");
		tblclmnItem.setWidth(150);

		TableColumn tblclmnStatic = new TableColumn(table_statistics, SWT.NONE);
		tblclmnStatic.setWidth(120);
		tblclmnStatic.setText("port");

		Composite composite_chart = new Composite(composite, SWT.BORDER);

		sendSeries = new TimeSeries("发送统计", Millisecond.class);
		JFreeChart schart = createChart(sendSeries, "发送统计", "个/s");
		composite_chart.setLayout(new FillLayout(SWT.HORIZONTAL));
		ChartComposite composite_send = new ChartComposite(composite_chart,
				SWT.NONE, schart, true);

		receiveSeries = new TimeSeries("接收统计", Millisecond.class);
		JFreeChart rchart = createChart(receiveSeries, "接收统计", "个/s");
		ChartComposite composite_receive = new ChartComposite(composite_chart,
				SWT.NONE, rchart, true);

		sendByteSeries = new TimeSeries("发送字节统计", Millisecond.class);
		JFreeChart sBchart = createChart(sendByteSeries, "发送字节统计", "Byte/s");
		ChartComposite composite_sendbyte = new ChartComposite(composite_chart,
				SWT.NONE, sBchart, true);

		receiveByteSeries = new TimeSeries("接收字节统计", Millisecond.class);
		JFreeChart rBchart = createChart(receiveByteSeries, "接收字节统计", "Byte/s");
		ChartComposite composite_receivebyte = new ChartComposite(
				composite_chart, SWT.NONE, rBchart, true);

		lock(true);
		// Create the help context id for the viewer's control
	}

	private void setData(int type) {
		/*
		 * 119.75.218.70 GET / HTTP/1.0 HOST: www.baidu.com
		 */
		String t = type + "";
		if (dataFrame == null)
			dataFrame = new SetData();
		dataFrame.setDataType(t, dataFrame);
		dataFrame.setVisible(true);
	}

	private void startSend() {
		System.out.println(spinner_connection.getText());
		if (dataFrame == null) {
			showMessage("no data");
			return;
		} else if (dataFrame.getData() == null) {
			showMessage("no data");
			return;
		}
		lock(false);
		String ip = text_IP.getText();
		int port = Integer.parseInt(spinner_port.getText());
		int localport = Integer.parseInt(spinner_localPort.getText());
		int connection = Integer.parseInt(spinner_connection.getText());

		// int type = (combo_TCPUDP.getSelectionIndex() == 0) ? NetArgs.TCP
		// : NetArgs.UDP;
		ArrayList<Object> IcmpData = new ArrayList();
		IcmpData = dataFrame.getIcmpData();

		int choiceType = combo_TCPUDP.getSelectionIndex();
		int type = -1;
		switch (choiceType) {
		case 0:
			type = NetArgs.TCP;
			break;
		case 1:
			type = NetArgs.UDP;
			break;
		case 2:
			type = NetArgs.ARP;
			break;
		case 3:
			type = NetArgs.ICMP;
			break;
		}
		if ((type == NetArgs.UDP) && (localport != -1))
			connection = 1;
		Object args = null;
		if (type == NetArgs.TCP || type == NetArgs.UDP)
			args = new IPArgs(type, connection, ip, port, localport,
					dataFrame.getData());
		else if (type == NetArgs.ARP) {
			ARPPacket arpData = dataFrame.getArpData();
			args = new ARPArgs(type, connection, arpData, dataFrame.getData());
		} else if (type == NetArgs.ICMP) {
			
			ArrayList<InetAddress> serverAddr = new ArrayList<InetAddress>();
			// following are get server ip from table
			for (int i = 0; i < table_statistics.getItemCount(); i++) {
				try {
					InetAddress tempAddr = InetAddress
							.getByName(table_statistics.getItem(i).getText());
					//I'm not sure what should I do when there're several same serverIp
					//These annotation codes are clear same serverIp
					/*
					for (int j = 0; j < serverAddr.size(); j++)
						if (tempAddr.equals(serverAddr.get(j))) {
							serverAddr.remove(j);
							break;
						}
					*/
					serverAddr.add(tempAddr);
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			args = new ICMPArgs(type, connection, ip, port, localport,
					IcmpData, dataFrame.getData(), serverAddr);

		}//

		ViewComponents components = new ViewComponents(sendSeries,
				receiveSeries, receiveByteSeries, sendByteSeries, table_log,
				table_statistics);
		updater = new ViewUpdater(components);
		netTask = new NetTask(args, components, updater);
		// start update thread
		updater.start();

		// start sending thread
		controller = new LocalController(netTask);
		controller.start();
	}

	private void stopSend() {
		// stop update view
		controller.stop();
		updater.exit = true;
	}

	private void addserver() {
		SetServer frame = new SetServer(table_statistics);
		frame.setVisible(true);
	}

	private void removeServer() {
		int cur = table_statistics.getSelectionIndex();
		if (cur == -1)
			return;
		if (cur >= table_statistics.getItemCount())
			return;
		table_statistics.remove(cur);
		RMIConfig.getConfig().getSlave().remove(cur);
	}

	private void lock(boolean allowSet) {
		button_addServer.setEnabled(allowSet);
		button_removeServer.setEnabled(allowSet);
		button_start.setEnabled(allowSet);
		button_data.setEnabled(allowSet);
		button_stop.setEnabled(!allowSet);
		spinner_port.setEnabled(allowSet);
		spinner_localPort.setEnabled(allowSet);
		spinner_connection.setEnabled(allowSet);
		combo_TCPUDP.setEnabled(allowSet);
		text_IP.setEnabled(allowSet);
	}

	private void showMessage(String message) {
		MessageDialog.openInformation(this.getSite().getShell(), "GenPressure",
				message);
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
	}

	public JFreeChart createChart(TimeSeries series, String title, String value) {
		TimeSeriesCollection dataset = new TimeSeriesCollection(series);
		// create the chart...
		JFreeChart chart = ChartFactory.createTimeSeriesChart(title, "时间",
				value, dataset, true, false, false);
		// NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...

		// set the background color for the chart...
		chart.setBackgroundPaint(Color.white);

		// get a reference to the plot for further customisation...
		XYPlot plot = (XYPlot) chart.getPlot();
		plot.setBackgroundPaint(Color.lightGray);
		plot.setDomainGridlinePaint(Color.white);
		plot.setDomainGridlinesVisible(true);
		plot.setRangeGridlinePaint(Color.white);
		// OPTIONAL CUSTOMISATION COMPLETED.
		return chart;
	}
}