package com.bit.strength.card;

import org.jfree.chart.plot.MeterPlot;
import org.jfree.data.time.TimeSeries;

public class CardViewComponent {
	private TimeSeries inputData;
	private TimeSeries recData_6208;
	private TimeSeries recData_bst23208;
	private TimeSeries recData_3;
	private MeterPlot meterplot_1;
	private MeterPlot meterplot_2;

	public CardViewComponent(TimeSeries inputData, MeterPlot meterplot1, MeterPlot meterplot2,
			TimeSeries recData_6208, TimeSeries recData_bst23208, TimeSeries recData_32) {
		// TODO Auto-generated constructor stub
		super();
		//this.inputData = inputData;
		this.meterplot_1 = meterplot1;
		this.meterplot_2 = meterplot2;
		this.recData_6208 = recData_6208;
		this.recData_bst23208 = recData_bst23208;
		this.recData_3 = recData_32;
	}

//	public TimeSeries getInputData() {
//		return inputData;
//	}
//
//	public void setInputData(TimeSeries inputData) {
//		this.inputData = inputData;
//	}

	public TimeSeries getrecData_6208() {
		return recData_6208;
	}

	public void setrecData_6208(TimeSeries recData_6208) {
		this.recData_6208 = recData_6208;
	}

	public TimeSeries getrecData_bst23208() {
		return recData_bst23208;
	}

	public void setrecData_bst23208(TimeSeries recData_bst23208) {
		this.recData_bst23208 = recData_bst23208;
	}

	public TimeSeries getRecData_3() {
		return recData_3;
	}

	public void setRecData_3(TimeSeries recData_3) {
		this.recData_3 = recData_3;
	}

	public MeterPlot getMeterplot_1() {
		return meterplot_1;
	}

	public void setMeterplot_1(MeterPlot meterplot_1) {
		this.meterplot_1 = meterplot_1;
	}

	public MeterPlot getMeterplot_2() {
		return meterplot_2;
	}

	public void setMeterplot_2(MeterPlot meterplot_2) {
		this.meterplot_2 = meterplot_2;
	}

}
