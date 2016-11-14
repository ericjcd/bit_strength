package com.bit.strength.stress.network;

import java.util.ArrayList;

public abstract class IPRunnable extends NetRunnable {
	protected String ip;
	protected int port;
	protected int localPort;
	protected ArrayList<Message> messages;
}
