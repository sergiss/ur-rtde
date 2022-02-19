package com.delmesoft.urx;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class URImpl implements UR {
	
	public static final int SO_TIMEOUT = 10_000;

	public static final int DEFAULT_PORT = 30020;
	
	private Socket socket;
	private InputStream is;
	private OutputStream os;
	
	private Thread thread;
	
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
			} finally {
				disconnect();
			}
			
		}
		
		
	}

	private void createURThread() throws Exception {
		
		thread = new Thread("UR Thread") {
			
			@Override
			public void run() {
				
				try {
					
					final byte[] buffer = new byte[2048];
					
					// Barrier for synchronization
					synchronized (this) {
						this.notify(); // open barrier
					}
				
					// Loop
					while(!isInterrupted()) {
						
						
						
					}
				
				} catch (Exception e) {
					if(isConnected()) { // still connected 
						
						e.printStackTrace(); // TODO: handle exception
						
					}
				} finally {
					disconnect();
				}
				
			} // run
			
		}; // new Thread
		
		// Barrier for synchronization
		synchronized (thread) {
			thread.start();
			thread.wait();
		}
		
	}

	@Override
	public synchronized boolean isConnected() {
		return socket != null;
	}

	@Override
	public synchronized void disconnect() {

		if (isConnected()) {
			// interrupt UR thread
			if (thread != null) {
				try {
					thread.interrupt();
				} catch (Exception e) {
				} finally {
					thread = null;
				}
			}
			// close socket
			if (socket != null) {
				try {
					socket.close();
				} catch (Exception e) {
				} finally {
					socket = null;
					is = null;
					os = null;
				}
			}
		}

	} // disconnect
	
}
