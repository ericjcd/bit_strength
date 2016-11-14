package com.bit.strength.stress.network;

import java.io.Serializable;

import com.bit.strength.util.ByteHex;

import net.sf.json.JSONObject;

public class Message implements Serializable {
	// content
	private String title;
	private int type;
	private byte[] data;
	// control
	private boolean receiveNext;
	private boolean autoIncrease;

	private int interval;
	private int duration;
	private int repeat;

	public Message(String title, int type, byte[] data, boolean receiveNext,
			boolean autoIncrease, int interval, int duration, int repeat) {
		super();
		this.title = title;
		this.type = type;
		this.data = data;
		this.receiveNext = receiveNext;
		this.autoIncrease = autoIncrease;
		this.interval = interval;
		this.duration = duration;
		this.repeat = repeat;
	}

	public Message(JSONObject object) {
		super();
		this.data = ByteHex.Base64Decode(object.getString("data").getBytes());
		this.title = object.getString("title");
		this.type = object.getInt("type");
		this.receiveNext = object.getBoolean("receiveNext");
		this.autoIncrease = object.getBoolean("autoIncrease");
		this.interval = object.getInt("interval");
		this.duration = object.getInt("duration");
		this.repeat = object.getInt("repeat");
	}

	public boolean isReceiveNext() {
		return receiveNext;
	}

	public void setReceiveNext(boolean receiveNext) {
		this.receiveNext = receiveNext;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public boolean isAutoIncrease() {
		return autoIncrease;
	}

	public void setAutoIncrease(boolean autoIncrease) {
		this.autoIncrease = autoIncrease;
	}

	public int getInterval() {
		return interval;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public int getRepeat() {
		return repeat;
	}

	public void setRepeat(int repeat) {
		this.repeat = repeat;
	}

	public JSONObject toJsonObject() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("title", getTitle());
		jsonObject.put("data", new String(ByteHex.Base64Encode(getData())));
		jsonObject.put("type", getType());
		jsonObject.put("receiveNext", isReceiveNext());
		jsonObject.put("autoIncrease", isAutoIncrease());
		jsonObject.put("interval", getInterval());
		jsonObject.put("duration", getDuration());
		jsonObject.put("repeat", getRepeat());
		return jsonObject;
	}
}
