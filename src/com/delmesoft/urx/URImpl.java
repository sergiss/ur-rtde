package com.delmesoft.urx;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class URImpl implements UR {
	
	public static final long SO_TIMEOUT = 10_000;

	public static final int DEFAULT_PORT = 30020;
	
	private Socket socket;
	private InputStream is;
	private OutputStream os;
	
	private String host;
	private int port;

	public URImpl(String host) {
		this(host, DEFAULT_PORT);
	}
	
	public URImpl(String host, int port) {
		this.host = host;
		this.port = port;
	}

	@Override
	public synchronized void connect() throws URException {
		
		if(!isConnected()) {
			
			try {
				
				this.socket = new Socket(host, port);
				this.socket.setSoTimeout(SO_TIMEOUT);
				
				this.is = socket.getInputStream();
				this.os = socket.getOutputStream();
				
				createURThread();
				
			} catch (Exception e) {
				throw new URException("Errorr connecting", e);
			}
			
		}
		
		
	}

	private void createURThread() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public synchronized boolean isConnected() {
		return socket != null;
	}

	@Override
	public synchronized void  disconnect() {
		// TODO Auto-generated method stub
		
	}
	
	

}
