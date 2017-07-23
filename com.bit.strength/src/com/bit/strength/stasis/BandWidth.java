package com.bit.strength.stasis;

import java.awt.Font;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

/**
 * 
 * @author Ericj 由IperfConfig.java调用，显示当前Capture网络下的带宽。
 */
public class BandWidth {
	private TimeSeries series;
	private ChartPanel chartPanel;
	private Timer timer;
	private boolean show;

	public BandWidth() {
		show = true;
		timer = new Timer();

		series = new TimeSeries("网络带宽统计", Millisecond.class);
		TimeSeriesCollection dataset = new TimeSeriesCollection(this.series);
		JFreeChart chart = createChart(dataset);

		chart.getLegend().setItemFont(new Font("黑体", Font.BOLD, 15));
		chart.getTitle().setFont(new Font("宋体", Font.BOLD, 20));// 设置标题字体

		chartPanel = new ChartPanel(chart);
	}

	private JFreeChart createChart(final XYDataset dataset) {
		final JFreeChart result = ChartFactory.createTimeSeriesChart("网络带宽统计",
				"时间", "带宽（Mbits/sec）", dataset, true, true, false);
		final XYPlot plot = result.getXYPlot();
		ValueAxis axis = plot.getDomainAxis();
		axis.setAutoRange(true);
		axis.setFixedAutoRange(60000.0); // 60 seconds
		axis.setLabelFont(new Font("黑体", Font.BOLD, 14)); // 水平底部标题
		axis.setTickLabelFont(new Font("宋体", Font.BOLD, 12)); // 垂直标题

		axis = plot.getRangeAxis();
		axis.setRange(0.0, 20.0);
		axis.setLabelFont(new Font("黑体", Font.BOLD, 15));
		return result;
	}

	/**
	 * 
	 * @param desip
	 * @param desport
	 * @param locport
	 * @throws IOException
	 * 测试UDP的双向传输
	 * 客户端使用参数-d以运行双测试模式，客户端会与服务端进行udp往返测试。可以使用-L参数指定本端双测试监听的端口。
	 */
	private void exec(String desip, String desport, String locport)
			throws IOException {
		String[] cmd = new String[] {
				"cmd.exe",
				"/c",
				"iperf -c " + desip + " -p " + desport
						+ " -i 1 -t 5 -u -b 10M -d -L " + locport };
		System.out.println(cmd[2]);
		Process process = Runtime.getRuntime().exec(cmd);
		BufferedReader br = new BufferedReader(new InputStreamReader(
				process.getInputStream()));
		String info = "";
		while ((info = br.readLine()) != null) {
			if (!show)
				break;
			if (!"".equals(info)) {
				String[] word = info.split(" ");
				for (int i = 0; i < word.length; i++) {
					// if(word[i].endsWith("%)")){
					// if(word[i].equals("ms")){
					if (word[i].equals("Mbits/sec")) {
						Millisecond now = new Millisecond();
						// System.out.println("Now = " + now.toString() +
						// " have " + word[i - 1]);
						double factor = Double.parseDouble(word[i - 1]);
						series.addOrUpdate(new Millisecond(), factor);
					}
				}
			}
		}
	}

	/**
	 * 
	 * @param desip
	 * @param desport
	 * @param locport
	 *            向目标机请求数据，获得数据并进行状态显示。
	 */
	public void show(String desip, String desport, String locport) {
		JFrame frame = new JFrame("网络带宽统计") {
			@Override
			public void dispose() {
				// TODO Auto-generated method stub
				super.dispose();
				timer.cancel();
				show = false;
			}
		};
		frame.add(chartPanel); // 添加折线图
		frame.setBounds(50, 50, 800, 600);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		TimerTask tt = new TimerTask() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					exec(desip, desport, locport);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		timer.schedule(tt, 0, 5000);
	}

	public static void main(String[] args) {
		(new BandWidth()).show("192.168.236.128", "5116", "50236");
	}
}
