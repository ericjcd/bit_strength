package com.bit.strength.card;

import java.awt.Color;
import java.awt.Font;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.SWTResourceManager;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.DialShape;
import org.jfree.chart.plot.MeterInterval;
import org.jfree.chart.plot.MeterPlot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.Range;
import org.jfree.data.general.DefaultValueDataset;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.experimental.chart.swt.ChartComposite;

public class CardView extends ViewPart {
	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "com.bit.strength.views.CardView";

	private Combo combo_card;
	private Button button_start;
	private Button button_stop;
	private Button button_pause;

	private TimeSeries inputData;
	private TimeSeries recData_6208;
	private TimeSeries recData_bst23208;
	private TimeSeries recData_3;
	private MeterPlot meterplot1;
	private MeterPlot meterplot2;

	private CardViewUpdater cv_Updater = null;
	private GetCardData getCardData = null;
	
	public CardView() {
		// TODO Auto-generated constructor stub
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

	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new FillLayout());

		ScrolledComposite scrolledComposite = new ScrolledComposite(parent,
				SWT.H_SCROLL | SWT.V_SCROLL);
		// scrolledComposite.setLayout(new FillLayout(SWT.VERTICAL));
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);

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

		Label label_Blank0 = new Label(composite_panel, SWT.NONE);
		label_Blank0.setSize(600, 0);

		Label label_6 = new Label(composite_panel, SWT.NONE);
		label_6.setBackground(SWTResourceManager
				.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		label_6.setText("板卡监听:");

		Label label_Blank1 = new Label(composite_panel, SWT.NONE);
		label_Blank1.setSize(150, 0);

		combo_card = new Combo(composite_panel, SWT.NONE);
		combo_card.setItems(new String[] { "6208", "BST23208", "341", "1553" });
		combo_card.select(0);

		Label label_Blank2 = new Label(composite_panel, SWT.NONE);
		label_Blank2.setSize(150, 0);

		button_start = new Button(composite_panel, SWT.NONE);

		Label label_Blank3 = new Label(composite_panel, SWT.NONE);
		label_Blank3.setSize(150, 0);

		button_stop = new Button(composite_panel, SWT.NONE);
		button_stop.setEnabled(false);
		// button_pause = new Button(composite_panel, SWT.NONE);

		button_start.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// stop send
				lock(false);
				startListen();
			}

		});
		button_start.setText("开始监听");

		button_stop.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				stopListen();
				lock(true);
			}
		});
		button_stop.setText("停止监听");

		// button_pause.addSelectionListener(new SelectionAdapter(){
		// @Override
		// public void widgetSelected(SelectionEvent e) {
		// // TODO Auto-generated method stub
		// pauseListen();
		// }
		// });

		Composite composite_statistics = new Composite(composite_text,
				SWT.BORDER);
		FormData fd_composite_statistics = new FormData();
		fd_composite_statistics.bottom = new FormAttachment(100);
		fd_composite_statistics.right = new FormAttachment(100);
		fd_composite_statistics.top = new FormAttachment(15);
		fd_composite_statistics.left = new FormAttachment(0);
		composite_statistics.setLayoutData(fd_composite_statistics);
		composite_statistics.setLayout(new FillLayout(SWT.HORIZONTAL));

		//输入实时显示图 控件
//		inputData = new TimeSeries("输入数据", Millisecond.class);
//		JFreeChart recChart_0 = createChart(inputData, "输入数据", "");
//		ChartComposite composite_recChart_0 = new ChartComposite(
//				composite_statistics, SWT.NONE, recChart_0, true);
		
		meterplot1 = new MeterPlot();
		JFreeChart dashboard1 = createChart("风速", meterplot1, "m/s", 5);
		ChartComposite composite_dashboard1 = new ChartComposite(
				composite_statistics, SWT.NONE, dashboard1, true);

		meterplot2 = new MeterPlot();
		JFreeChart dashboard2 = createChart("转速", meterplot2, "r/min", 3000);
		ChartComposite composite_dashboard2 = new ChartComposite(
				composite_statistics, SWT.NONE, dashboard2, true);

		Composite composite_chart = new Composite(composite, SWT.BORDER);
		composite_chart.setLayout(new FillLayout(SWT.HORIZONTAL));

		recData_6208 = new TimeSeries("采样频率", Millisecond.class);
		JFreeChart recChart_1 = createChart(recData_6208, "采样频率", "采样频率（K/s）");
		ChartComposite composite_recChart_1 = new ChartComposite(
				composite_chart, SWT.NONE, recChart_1, true);

		recData_bst23208 = new TimeSeries("湿度", Millisecond.class);
		JFreeChart recChart_2 = createChart(recData_bst23208, "湿度", "湿度（% hPa）");
		ChartComposite composite_recChart_2 = new ChartComposite(
				composite_chart, SWT.NONE, recChart_2, true);

		recData_3 = new TimeSeries("温度", Millisecond.class);
		JFreeChart recChart_3 = createChart(recData_3, "温度", "温度（℃）");
		ChartComposite composite_recChart_3 = new ChartComposite(
				composite_chart, SWT.NONE, recChart_3, true);
	}

	private void startListen() {
		// TODO Auto-generated method stub
		int card_type_index = this.combo_card.getSelectionIndex();
		
		CardViewComponent cv_Component = new CardViewComponent(this.inputData, this.meterplot1,
				this.meterplot2, this.recData_6208, this.recData_bst23208, this.recData_3);
		this.cv_Updater = new CardViewUpdater(cv_Component);
		
		this.getCardData = new GetCardData(this.cv_Updater, card_type_index);
		
		this.cv_Updater.start();
		this.getCardData.start();
		
	}
	
	private void stopListen() {
		this.cv_Updater.exit = true;
		this.getCardData.exit = true;
	}

	private void lock(boolean isEnable) {
		this.combo_card.setEnabled(isEnable);
		this.button_start.setEnabled(isEnable);
		this.button_stop.setEnabled(!isEnable);
	}

	private JFreeChart createChart(TimeSeries series, String title, String value) {
		TimeSeriesCollection dataset = new TimeSeriesCollection(series);
		// create the chart...
		// 创建主题样式
		StandardChartTheme standardChartTheme = new StandardChartTheme("CN");
		// 设置标题字体
		standardChartTheme.setExtraLargeFont(new Font("隶书", Font.BOLD, 16));
		// 设置图例的字体
		standardChartTheme.setRegularFont(new Font("宋书", Font.PLAIN, 12));
		// 设置轴向的字体
		standardChartTheme.setLargeFont(new Font("宋书", Font.PLAIN, 12));
		ChartFactory.setChartTheme(standardChartTheme);
		JFreeChart chart = ChartFactory.createTimeSeriesChart(title, "时间(s)",
				value, dataset, true, true, false);
		// NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
		// set the background color for the chart...
		chart.setBackgroundPaint(Color.white);

		// get a reference to the plot for further customisation...
		XYPlot plot = (XYPlot) chart.getPlot();
		plot.setBackgroundPaint(Color.lightGray);
		plot.setDomainGridlinePaint(Color.white);
		plot.setDomainGridlinesVisible(true);
		plot.setRangeGridlinePaint(Color.white);
		// //纵坐标设定
		// ValueAxis valueaxis = plot.getDomainAxis();
		// //自动设置数据轴数据范围
		// valueaxis.setAutoRange(true);
		// //数据轴固定数据范围 50s
		// valueaxis.setFixedAutoRange(40000D);
		//
		// valueaxis = plot.getRangeAxis();
		// OPTIONAL CUSTOMISATION COMPLETED.
		return chart;
	}

	private JFreeChart createChart(String title, MeterPlot plot, String value,
			int max) {
		DefaultValueDataset data = new DefaultValueDataset(0.0);
		plot.setUnits(value);
		plot.setDataset(data);
		plot.setDialShape(DialShape.CHORD);
		plot.setDialBackgroundPaint(Color.WHITE);
		plot.setRange(new Range(0, max));
		plot.setDialOutlinePaint(Color.GRAY);
		plot.setNeedlePaint(Color.BLACK);
		plot.setTickLabelsVisible(true);
		plot.setTickLabelPaint(Color.BLACK);
		plot.setTickPaint(Color.GRAY);
		plot.setTickLabelFormat(NumberFormat.getNumberInstance());
		plot.setValuePaint(Color.BLACK);
		switch (title) {
		case "风速":
			plot.setTickSize(10);
			plot.addInterval(new MeterInterval("Low", new Range(0, max - 2),
					null, null, new Color(128, 255, 128, 90)));
			plot.addInterval(new MeterInterval("Normal", new Range(max - 2,
					max - 1), null, null, new Color(255, 255, 128, 90)));
			plot.addInterval(new MeterInterval("High", new Range(max - 1, max),
					null, null, new Color(255, 128, 128, 90)));
			break;
		case "转速":
			plot.setTickSize(100);
			plot.addInterval(new MeterInterval("Low", new Range(0, max - 1000),
					null, null, new Color(128, 255, 128, 90)));
			plot.addInterval(new MeterInterval("Normal", new Range(max - 1000,
					max - 500), null, null, new Color(255, 255, 128, 90)));
			plot.addInterval(new MeterInterval("High",
					new Range(max - 500, max), null, null, new Color(255, 128,
							128, 90)));
			break;
		}

		// 创建chart，最后一个参数决定是否显示图例
		JFreeChart chart = new JFreeChart(title, JFreeChart.DEFAULT_TITLE_FONT,
				plot, false);
		return chart;
	}
	
	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
