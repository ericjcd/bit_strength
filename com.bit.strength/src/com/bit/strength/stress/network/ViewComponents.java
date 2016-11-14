package com.bit.strength.stress.network;

import org.eclipse.swt.widgets.Table;
import org.jfree.data.time.TimeSeries;

public class ViewComponents {
	private TimeSeries sendSeries;
	private TimeSeries receiveSeries;
	private TimeSeries receiveByteSeries;
	private TimeSeries sendByteSeries;

	private Table table_log;
	private Table table_statistics;

	public ViewComponents(TimeSeries sendSeries, TimeSeries receiveSeries,
			TimeSeries receiveByteSeries, TimeSeries sendByteSeries,
			Table table_log, Table table_statistics) {
		super();
		this.sendSeries = sendSeries;
		this.receiveSeries = receiveSeries;
		this.receiveByteSeries = receiveByteSeries;
		this.sendByteSeries = sendByteSeries;
		this.table_log = table_log;
		this.table_statistics = table_statistics;
	}

	public TimeSeries getSendSeries() {
		return sendSeries;
	}

	public void setSendSeries(TimeSeries sendSeries) {
		this.sendSeries = sendSeries;
	}

	public TimeSeries getReceiveSeries() {
		return receiveSeries;
	}

	public void setReceiveSeries(TimeSeries receiveSeries) {
		this.receiveSeries = receiveSeries;
	}

	public TimeSeries getReceiveByteSeries() {
		return receiveByteSeries;
	}

	public void setReceiveByteSeries(TimeSeries receiveByteSeries) {
		this.receiveByteSeries = receiveByteSeries;
	}

	public TimeSeries getSendByteSeries() {
		return sendByteSeries;
	}

	public void setSendByteSeries(TimeSeries sendByteSeries) {
		this.sendByteSeries = sendByteSeries;
	}

	public Table getTable_log() {
		return table_log;
	}

	public void setTable_log(Table table_log) {
		this.table_log = table_log;
	}

	public Table getTable_statistics() {
		return table_statistics;
	}

	public void setTable_statistics(Table table_statistics) {
		this.table_statistics = table_statistics;
	}

}
