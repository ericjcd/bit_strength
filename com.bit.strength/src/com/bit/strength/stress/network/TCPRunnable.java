package com.bit.strength.stress.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

import com.bit.strength.stress.network.RMI.RemoteCollector;

public class TCPRunnable extends IPRunnable {

	public TCPRunnable(IPArgs args, RemoteCollector collector) {
		this.ip = args.getIp();
		this.port = args.getPort();
		this.localPort = args.getLocalPort();
		this.messages = args.getMessages();
		this.collector = collector;
		this.exit = false;
	}

	@Override
	public void run() {
		System.out.println("TCP task executed");
		//loop meslist to send
		for (int i = 0; i < messages.size(); i++) {
			if (exit)
				break;
			System.out.println("TCP task executed " + i);
			//the package to IP address
			InetAddress addr = null;
			Socket socket = null;
			try {
				//getByName: get DNS through IP
				addr = InetAddress.getByName(ip);
				socket = new Socket(addr, port);
				OutputStream outputStream = socket.getOutputStream();
				BufferedReader bufferedReader = new BufferedReader(
						new InputStreamReader(socket.getInputStream()));
				byte[] data = messages.get(i).getData().clone();
				long startTimeForUpdate, endTimeForUpdate;
				long count = 0, startTime = System.currentTimeMillis(), endTime;
				while (!exit) {
					
					// send data
					startTimeForUpdate = System.currentTimeMillis();
					outputStream.write(data);
					outputStream.flush();
					endTimeForUpdate = System.currentTimeMillis();
					collector.update(endTimeForUpdate - startTimeForUpdate, 1,
							data.length, 0, 0);
					
					// wait
					try {
						Thread.sleep(messages.get(i).getInterval());
					}
					catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					// receive data
					if (messages.get(i).isReceiveNext()) {
						String line;
						String recData = "";
						int cnt = 0;
						startTimeForUpdate = System.currentTimeMillis();
						while ((line = bufferedReader.readLine()) != null) {
							cnt += line.length();
							recData += line + "\n";
						}
						endTimeForUpdate = System.currentTimeMillis();
						collector.update(endTimeForUpdate - startTimeForUpdate,
								0, 0, 1, cnt);
						this.dataStorage.storageDataAtLocal(1, addr, port, data, recData);
					}
					count++;
					// when to stop
					if (messages.get(i).getDuration() == -1) {
						if (count >= messages.get(i).getRepeat())
							break;
					} else {
						endTime = System.currentTimeMillis();
						if (endTime - startTime > messages.get(i).getDuration() * 1000)
							break;
					}
				}

				outputStream.close();
				bufferedReader.close();
				socket.close();
				System.out.println("TCP task " + i + " succeed");
			}
			catch (Exception e) {
				System.out.println("TCP task " + i + " failed");
				System.out.println(e.toString());
			}
			finally {
				if (socket != null) {
					try {
						socket.close();
					}
					catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		}
		System.out.println("TCP task finished");
	}
}
