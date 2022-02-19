package com.delmesoft.ur;

import static com.delmesoft.ur.utils.Utils.read;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;


public class URImpl implements UR {
	
	// RTDE
	
	public static final int ROBOT_STATE_PACKAGE_TYPE_ROBOT_MODE_DATA       =  0;
	public static final int ROBOT_STATE_PACKAGE_TYPE_JOINT_DATA            =  1;
	public static final int ROBOT_STATE_PACKAGE_TYPE_TOOL_DATA             =  2;
	public static final int ROBOT_STATE_PACKAGE_TYPE_MASTERBOARD_DATA      =  3;
	public static final int ROBOT_STATE_PACKAGE_TYPE_CARTESIAN_INFO        =  4;
	public static final int ROBOT_STATE_PACKAGE_TYPE_KINEMATICS_INFO       =  5;
	public static final int ROBOT_STATE_PACKAGE_TYPE_CONFIGURATION_DATA    =  6;
	public static final int ROBOT_STATE_PACKAGE_TYPE_FORCE_MODE_DATA       =  7;
	public static final int ROBOT_STATE_PACKAGE_TYPE_ADDITIONAL_INFO       =  8;
	public static final int ROBOT_STATE_PACKAGE_TYPE_NEEDED_FOR_CALIB_DATA =  9;
	public static final int ROBOT_STATE_PACKAGE_TYPE_SAFETY_DATA           = 10;
	public static final int ROBOT_STATE_PACKAGE_TYPE_TOOL_COMM_INFO        = 11;
	
	public static final int SO_TIMEOUT = 10_000;
	public static final int DEFAULT_PORT = 30020;
	
	private Socket socket;
	private InputStream is;
	private OutputStream os;
	
	private Thread thread;
	
	private String host;
	private int port;
	private RobotModeData robotModeData;

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
						step(buffer);						
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
	
	private void step(final byte[] buffer) throws IOException {
		
		// TODO : send cmd
		
		// HEADER (5 bytes)
		// -----------------------
		// package_size uint16_t (2 bytes)
		// package_type uint8_t  (1 byte )
		
		read(is, buffer, 0, 5);
		ByteBuffer bb = java.nio.ByteBuffer.wrap(buffer, 0, 5);
		
		final int packageSize = bb.getInt(0);
		final int packageType = bb.getInt(4);
		
		switch (packageType) {
		
		case ROBOT_STATE_PACKAGE_TYPE_ROBOT_MODE_DATA: // 47 bytes
			read(is, buffer, 0, packageSize - 5);
			bb = ByteBuffer.wrap(buffer, 0, packageSize - 5);
				
			final RobotModeData rmd = URImpl.this.robotModeData;
			rmd.setTimestamp(bb.getLong(0));
			rmd.setRealRobotConnected(bb.get(8) == 1);
			rmd.setRealRobotEnabled(bb.get(9) == 1);
			rmd.setPowerOnRobot(bb.get(10) == 1);
			rmd.setEmergencyStopped(bb.get(11) == 1);
			rmd.setSecurityStopped(bb.get(12) == 1);
			rmd.setProgramRunning(bb.get(13) == 1);
			rmd.setProgramPaused(bb.get(14) == 1);
			rmd.setRobotMode(bb.get(15) & 0xFF);
			rmd.setControlMode(bb.get(16) & 0xFF);
			rmd.setSpeedFraction(bb.getDouble(17));
			rmd.setSpeedScalling(bb.getDouble(25));
			rmd.setSpeedFractionLimit(bb.getDouble(33));
			// char reserved
				
			break; // ROBOT_STATE_PACKAGE_TYPE_ROBOT_MODE_DATA
		case ROBOT_STATE_PACKAGE_TYPE_JOINT_DATA: // 41 bytes
			
			for(int i = 0; i < 6; ++i) {
				read(is, buffer, 0, 41);
				bb = ByteBuffer.wrap(buffer, 0, 41);
				
				// TODO :
			}
			
			break; // ROBOT_STATE_PACKAGE_TYPE_JOINT_DATA
		case ROBOT_STATE_PACKAGE_TYPE_TOOL_DATA:
			read(is, buffer, 0, packageSize - 5);
			bb = ByteBuffer.wrap(buffer, 0, packageSize - 5);
			// TODO : 
			break; // ROBOT_STATE_PACKAGE_TYPE_TOOL_DATA
		case ROBOT_STATE_PACKAGE_TYPE_MASTERBOARD_DATA: // 63 bytes
			read(is, buffer, 0, packageSize - 5);
			bb = ByteBuffer.wrap(buffer, 0, packageSize - 5);
			// TODO : 
			break; // ROBOT_STATE_PACKAGE_TYPE_MASTERBOARD_DATA
		case ROBOT_STATE_PACKAGE_TYPE_CARTESIAN_INFO:
			read(is, buffer, 0, packageSize - 5);
			bb = ByteBuffer.wrap(buffer, 0, packageSize - 5);
			// TODO : 
			break; // ROBOT_STATE_PACKAGE_TYPE_CARTESIAN_INFO
		case ROBOT_STATE_PACKAGE_TYPE_KINEMATICS_INFO:
			read(is, buffer, 0, packageSize - 5);
			bb = ByteBuffer.wrap(buffer, 0, packageSize - 5);			
			break; // ROBOT_STATE_PACKAGE_TYPE_KINEMATICS_INFO
		case ROBOT_STATE_PACKAGE_TYPE_CONFIGURATION_DATA:
			read(is, buffer, 0, packageSize - 5);
			break; // ROBOT_STATE_PACKAGE_TYPE_CONFIGURATION_DATA
		case ROBOT_STATE_PACKAGE_TYPE_FORCE_MODE_DATA:
			read(is, buffer, 0, packageSize - 5);
			break; // ROBOT_STATE_PACKAGE_TYPE_FORCE_MODE_DATA
		case ROBOT_STATE_PACKAGE_TYPE_ADDITIONAL_INFO:
			read(is, buffer, 0, packageSize - 5);
			break; // ROBOT_STATE_PACKAGE_TYPE_ADDITIONAL_INFO
		case ROBOT_STATE_PACKAGE_TYPE_NEEDED_FOR_CALIB_DATA:
			read(is, buffer, 0, packageSize - 5);
			break; // ROBOT_STATE_PACKAGE_TYPE_NEEDED_FOR_CALIB_DATA
		case ROBOT_STATE_PACKAGE_TYPE_SAFETY_DATA:
			read(is, buffer, 0, packageSize - 5);
			break; // ROBOT_STATE_PACKAGE_TYPE_SAFETY_DATA
		case ROBOT_STATE_PACKAGE_TYPE_TOOL_COMM_INFO:
			read(is, buffer, 0, packageSize - 5);
			break; // ROBOT_STATE_PACKAGE_TYPE_TOOL_COMM_INFO	
		default : // Unimplemented 
			read(is, buffer, 0, packageSize - 5);
			break;			
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
