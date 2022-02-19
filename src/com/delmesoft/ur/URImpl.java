package com.delmesoft.ur;

import static com.delmesoft.ur.utils.Utils.read;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;

import com.delmesoft.ur.utils.Pose;


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
	
	private final RobotModeData robotModeData;
	private final JointData[] jointData;
	private final ToolData toolData;
	private final MasterBoardData masterBoardData;
	private final CartesianInfo cartesianInfo;
	private final ForceModeData forceModeData;
	private final AdditionalInfo additionalInfo;

	public URImpl(String host) {
		this(host, DEFAULT_PORT);
	}
	
	public URImpl(String host, int port) {
		this.host = host;
		this.port = port;
		
		robotModeData = new RobotModeData();
		jointData     = new JointData[6];
		for(int i = 0; i < jointData.length; ++i) {
			jointData[i] = new JointData();
		}
		toolData        = new ToolData();
		masterBoardData = new MasterBoardData();
		cartesianInfo   = new CartesianInfo();
		forceModeData   = new ForceModeData();
		additionalInfo  = new AdditionalInfo();
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
				
				JointData jointData = URImpl.this.jointData[i];                               
				jointData.setqActual(bb.getDouble(0));
				jointData.setqTarget(bb.getDouble(8));
				jointData.setQdActual(bb.getDouble(16));
				jointData.setiActual(bb.getFloat(24));
				jointData.setvActual(bb.getFloat(28));
				jointData.settMotor(bb.getFloat(32));
				jointData.settMicro(bb.getFloat(36));
				jointData.setJointMode(bb.get(40) & 0xFF);
			}
			
			break; // ROBOT_STATE_PACKAGE_TYPE_JOINT_DATA
		case ROBOT_STATE_PACKAGE_TYPE_TOOL_DATA:
			read(is, buffer, 0, packageSize - 5);
			bb = ByteBuffer.wrap(buffer, 0, packageSize - 5);
			
			ToolData toolData = URImpl.this.toolData;
			toolData.setAnalogInputRange2(bb.get(0) & 0xFF);
			toolData.setAnalogInputRange3(bb.get(1) & 0xFF);
			toolData.setAnalogInput2(bb.getDouble(2));
			toolData.setAnalogInput3(bb.getDouble(10));
			toolData.setToolVoltage48v(bb.getFloat(18));
			toolData.setToolOutputVoltage(bb.get(22) & 0xFF);
			toolData.setToolCurrent(bb.getFloat(23));
			toolData.setToolTemperature(bb.getFloat(27));
			toolData.setToolMode(bb.get(31) & 0xFF);
			
			break; // ROBOT_STATE_PACKAGE_TYPE_TOOL_DATA
		case ROBOT_STATE_PACKAGE_TYPE_MASTERBOARD_DATA: // 63 bytes
			read(is, buffer, 0, packageSize - 5);
			bb = ByteBuffer.wrap(buffer, 0, packageSize - 5);
			
			MasterBoardData masterBoardData = URImpl.this.masterBoardData;
			masterBoardData.setDigitalInputBits(bb.getInt());
			masterBoardData.setDigitalOutputBits(bb.getInt(4));
			masterBoardData.setAnalogInputRange0(bb.get(8) & 0xFF);
			masterBoardData.setAnalogInputRange1(bb.get(9) & 0xFF);
			masterBoardData.setAnalogInput0(bb.getDouble(10));
			masterBoardData.setAnalogInput1(bb.getDouble(18));
			masterBoardData.setAnalogInputDomain0(bb.get(26) & 0xFF);
			masterBoardData.setAnalogInputDomain1(bb.get(27) & 0xFF);
			masterBoardData.setAnalogOutput0(bb.getDouble(28));
			masterBoardData.setAnalogOutput1(bb.getDouble(36));
			masterBoardData.setMasterBoardTemperature(bb.getFloat(44));
			masterBoardData.setRobotVoltage48V(bb.getFloat(48));
			masterBoardData.setRobotCurrent(bb.getFloat(52));
			masterBoardData.setMasterIOCurrent(bb.getFloat(56));
			
			// TODO : fill remaining data
			
			break; // ROBOT_STATE_PACKAGE_TYPE_MASTERBOARD_DATA
		case ROBOT_STATE_PACKAGE_TYPE_CARTESIAN_INFO:
			read(is, buffer, 0, packageSize - 5);
			bb = ByteBuffer.wrap(buffer, 0, packageSize - 5);
			
			Pose pose = URImpl.this.cartesianInfo.getPose();
			pose.getPosition().set(bb.getDouble(0), bb.getDouble(8), bb.getDouble(16));
			pose.getOrientation().set(bb.getDouble(24), bb.getDouble(32), bb.getDouble(40));
			
			Pose tcpOffset = URImpl.this.cartesianInfo.getTcpOffset();
			tcpOffset.getPosition().set(bb.getDouble(48), bb.getDouble(56), bb.getDouble(64));
			tcpOffset.getOrientation().set(bb.getDouble(72), bb.getDouble(80), bb.getDouble(88));
			
			break; // ROBOT_STATE_PACKAGE_TYPE_CARTESIAN_INFO
		case ROBOT_STATE_PACKAGE_TYPE_KINEMATICS_INFO:
			read(is, buffer, 0, packageSize - 5);			
			// This information is sent when leaving initializing mode and/or if the kinematics configuration is changed.			
			break; // ROBOT_STATE_PACKAGE_TYPE_KINEMATICS_INFO
		case ROBOT_STATE_PACKAGE_TYPE_CONFIGURATION_DATA:
			read(is, buffer, 0, packageSize - 5);
			// This information is sent when leaving initializing mode and/or if the kinematics configuration is changed.
			break; // ROBOT_STATE_PACKAGE_TYPE_CONFIGURATION_DATA
		case ROBOT_STATE_PACKAGE_TYPE_FORCE_MODE_DATA:
			read(is, buffer, 0, packageSize - 5);
			bb = ByteBuffer.wrap(buffer, 0, packageSize - 5);
			ForceModeData forceModeData = URImpl.this.forceModeData;
			forceModeData.getPose().getPosition().set(bb.getDouble(0), bb.getDouble(8), bb.getDouble(16));
			forceModeData.getPose().getOrientation().set(bb.getDouble(24), bb.getDouble(32), bb.getDouble(40));
			forceModeData.setRobotDexterity(bb.getDouble(48));
			break; // ROBOT_STATE_PACKAGE_TYPE_FORCE_MODE_DATA
		case ROBOT_STATE_PACKAGE_TYPE_ADDITIONAL_INFO:
			read(is, buffer, 0, packageSize - 5);
			AdditionalInfo additionalInfo = URImpl.this.additionalInfo;
			additionalInfo.setTeachButtonPressed(buffer[0] == 1);
			additionalInfo.setTeachButtonEnabled(buffer[1] == 1);
			additionalInfo.setIoEnabledFreeDrive(buffer[2] == 1);			
			break; // ROBOT_STATE_PACKAGE_TYPE_ADDITIONAL_INFO
		case ROBOT_STATE_PACKAGE_TYPE_NEEDED_FOR_CALIB_DATA:
			// It is used internally by Universal Robots software only and should be skipped.
			read(is, buffer, 0, packageSize - 5);
			break; // ROBOT_STATE_PACKAGE_TYPE_NEEDED_FOR_CALIB_DATA
		case ROBOT_STATE_PACKAGE_TYPE_SAFETY_DATA:
			// This package is used internally by Universal Robots software only and should be skipped.
			read(is, buffer, 0, packageSize - 5);
			break; // ROBOT_STATE_PACKAGE_TYPE_SAFETY_DATA
		case ROBOT_STATE_PACKAGE_TYPE_TOOL_COMM_INFO:
			// // This package is used internally by Universal Robots software only and should be skipped.
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
