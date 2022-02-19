package com.delmesoft.ur;

import static com.delmesoft.ur.utils.Utils.read;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Arrays;

import com.delmesoft.ur.utils.JointPosition;
import com.delmesoft.ur.utils.Pose;
import com.delmesoft.ur.utils.Vec3;

/*
 * Copyright (c) 2021, Sergio S.- sergi.ss4@gmail.com http://sergiosoriano.com
 */

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
	public static final int ROBOT_STATE_PACKAGE_TYPE_TOOL_MODE_INFO        = 12;
	public static final int MESSAGE_TYPE_ROBOT_STATE                       = 16;
	public static final int MESSAGE_TYPE_ROBOT_MESSAGE                     = 20;
	
	public static final int SO_TIMEOUT     = 10000;
	public static final int DEFAULT_PORT   = 30002;
	public static final int DASHBOARD_PORT = 29999;
	
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
	
	private String command;

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
	public synchronized void connect() throws Exception {
		
		if(!isConnected()) {
			
			try {
				
				this.socket = new Socket(host, port);
				this.socket.setSoTimeout(SO_TIMEOUT);
				
				this.is = socket.getInputStream();
				this.os = socket.getOutputStream();
				
				createURThread();
				
			} catch (Exception e) {
				disconnect();
				throw new Exception("Error connecting", e);
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
		
		synchronized (URImpl.this) {
			sendSyncCmd(command);
			command = null;
		}
		
		// HEADER (5 bytes)
		// -----------------------
		// package_size uint16_t (2 bytes)
		// package_type uint8_t  (1 byte )
		
		read(is, buffer, 0, 5);
		ByteBuffer bb = ByteBuffer.wrap(buffer, 0, 5);
		
		final int packageSize = bb.getInt(0);
		final int packageType = bb.get(4);
		
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
			
			// TODO : use remaining data
			
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
			// This package is used internally by Universal Robots software only and should be skipped.
			read(is, buffer, 0, packageSize - 5);
			/* bb = ByteBuffer.wrap(buffer, 0, packageSize - 5);
			boolean toolCommunicationIsEnabled = bb.get() == 1;
			int baudRate = bb.getInt(1);
			int parity   = bb.getInt(5);
			int stopBits = bb.getInt(9);
			float rxIdleChars = bb.getFloat(13);
			float txIdleChars = bb.getFloat(17); */
			break; // ROBOT_STATE_PACKAGE_TYPE_TOOL_COMM_INFO
		case ROBOT_STATE_PACKAGE_TYPE_TOOL_MODE_INFO:
			read(is, buffer, 0, packageSize - 5);
			break; // ROBOT_STATE_PACKAGE_TYPE_TOOL_MODE_INFO			
		case MESSAGE_TYPE_ROBOT_STATE:
			// ignore
			break; // MESSAGE_TYPE_ROBOT_STATE
		case MESSAGE_TYPE_ROBOT_MESSAGE:
			int n = packageSize - 5;
			read(is, buffer, 0, n);
			
			// TODO : pending implementation
			
			break; // MESSAGE_TYPE_ROBOT_MESSAGE
		default : // Unimplemented 
			read(is, buffer, 0, packageSize - 5);
			break;			
		}	
		
	}
	
	private synchronized void sendSyncCmd(String command) throws IOException {
		if (command != null && isConnected()) {
			// send current command
			byte[] data = command.getBytes("UTF-8");
			os.write(data);
			os.flush();
		}
	}
	
	private synchronized void sendAsyncCmd(String cmd) throws Exception {
		if (isConnected()) {
			command = cmd;
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

	@Override
	public void powerOn() throws Exception {
		powerOn(true);
	}
	
	protected void powerOn(boolean retry) throws Exception {
		
		try (Socket socket = new Socket(host, DASHBOARD_PORT)) { // RTDE
			socket.setSoTimeout(SO_TIMEOUT);
			OutputStream out = socket.getOutputStream();
			InputStream is = socket.getInputStream();
			readResponse(is);
			String message;
			for(;;) {
				
				out.write("close popup\n".getBytes("UTF-8"));
				out.flush();
				Thread.sleep(50);
				message = readResponse(is);
				
				out.write("close safety popup\n".getBytes("UTF-8"));
				out.flush();
				Thread.sleep(50);
				message = readResponse(is);
				
				out.write("unlock protective stop\n".getBytes("UTF-8"));
				out.flush();
				Thread.sleep(50);
				message = readResponse(is);
				if (!message.contains("Cannot unlock protective")) {
					break;
				}
				
				if(!retry) {
					break;
				} else {
					retry = false;
					Thread.sleep(5000); // The unlock protective stop command fails if less than 5 seconds has passed since the protective stop occurred.
				}
				
			}
			
			out.write("power on\n".getBytes("UTF-8"));
			out.flush();
			Thread.sleep(50);
			message = readResponse(is);
			
			out.write("brake release\n".getBytes("UTF-8"));
			out.flush();
			Thread.sleep(50);
			message = readResponse(is);
		}
				
	}
	
	private String readResponse(InputStream is) throws IOException {
		StringBuffer sb = new StringBuffer();
		char c;
		do {
			c = (char) is.read();
			if (c == '\n')
				break;
			sb.append(c);
		} while (c != -1);
		return sb.toString();
	}

	@Override
	public void powerOff() throws Exception {
		try (Socket socket = new Socket(host, DASHBOARD_PORT)) { // RTDE
			socket.setSoTimeout(SO_TIMEOUT);
			OutputStream out = socket.getOutputStream();
			InputStream is = socket.getInputStream();
			out.write("power off\n".getBytes("UTF-8"));
			out.flush();
			readResponse(is);
		}
		
	}

	@Override
	public void setFreedriveMode(boolean b) throws Exception {
		final StringBuilder sb = new StringBuilder();
		sb.append("def myProg():\n");
		if(b) {
			sb.append("\tfreedrive_mode()\n");
			sb.append("\tsleep(60)\n");
		} else {
			sb.append("\tend_freedrive_mode()\n");
		}
		sb.append("end\n");
		String cmd = sb.toString();
		sendAsyncCmd(cmd);
	}

	@Override
	public void stopJ(double a) throws Exception {
		final StringBuilder sb = new StringBuilder();
		sb.append("stopj(")
		.append(DECIMAL_FORMAT.format(a))
		.append(")\n");

		String cmd = sb.toString();
		sendAsyncCmd(cmd);
	}

	@Override
	public void stopL(double a) throws Exception {
		final StringBuilder sb = new StringBuilder();
		sb.append("stopl(")
		.append(DECIMAL_FORMAT.format(a))
		.append(")\n");

		String cmd = sb.toString();
		sendAsyncCmd(cmd);
	}

	@Override
	public void speedl(double[] toolSpeed, double a, double t) throws Exception {
		final StringBuilder sb = new StringBuilder();
		sb.append("speedl(")
		.append(Arrays.toString(toolSpeed)).append(',')
		.append("a=").append(a).append(',')
		.append("t=").append(t)
		.append(")\n");

		String cmd = sb.toString();
		sendAsyncCmd(cmd);
	}

	@Override
	public void speedj(double[] jointSpeeds, double a, double t) throws Exception {
		final StringBuilder sb = new StringBuilder();
		sb.append("speedj(")
		.append(Arrays.toString(jointSpeeds)).append(',')
		.append("a=").append(a).append(',')
		.append("t=").append(t)
		.append(")\n");

		String cmd = sb.toString();
		sendAsyncCmd(cmd);
	}

	@Override
	public void setTcp(Pose pose) throws Exception {
		final StringBuilder sb = new StringBuilder();
		sb.append("set_tcp(");
		sb.append(pose.toString());
		sb.append(")\n");

		String cmd = sb.toString();
		sendAsyncCmd(cmd);
	}

	@Override
	public void setPayload(double mass, Vec3 centerOfMass) throws Exception {
		final StringBuilder sb = new StringBuilder();
		sb.append("set_payload(")
		.append(DECIMAL_FORMAT.format(mass))
		.append(',').append(centerOfMass.toString())
		.append(")\n");

		String cmd = sb.toString();
		sendAsyncCmd(cmd);
	}

	@Override
	public void setGravity(Vec3 gravity) throws Exception {
		final StringBuilder sb = new StringBuilder();
		sb.append("set_gravity([")
		.append(DECIMAL_FORMAT.format(gravity.x)).append(',')
		.append(DECIMAL_FORMAT.format(gravity.y)).append(',')
		.append(DECIMAL_FORMAT.format(gravity.z))
		.append("])\n");

		String cmd = sb.toString();
		sendAsyncCmd(cmd);
	}

	@Override
	public void movec(Pose via, Pose to, double a, double v, double r, int mode) throws Exception {
		final StringBuilder sb = new StringBuilder();
		sb.append("movec(")
		.append(via).append(',')
		.append(to).append(',')
		.append("a=").append(a).append(',')
		.append("v=").append(v).append(',')
		.append("r=").append(r).append(',')
		.append(mode)
		.append(")\n");

		String cmd = sb.toString();
		sendAsyncCmd(cmd);
	}

	@Override
	public void movej(JointPosition q, double a, double v, double t, double r) throws Exception {
		final StringBuilder sb = new StringBuilder();
		sb.append("movej(")
		.append(q).append(',')
		.append("a=").append(a).append(',')
		.append("v=").append(v).append(',')
		.append("t=").append(t).append(',')
		.append("r=").append(r)
		.append(")\n");

		String cmd = sb.toString();
		sendAsyncCmd(cmd);
	}

	@Override
	public void movej(Pose p, double a, double v, double t, double r) throws Exception {
		final StringBuilder sb = new StringBuilder();
		sb.append("movej(")
		.append(p).append(',')
		.append("a=").append(a).append(',')
		.append("v=").append(v).append(',')
		.append("t=").append(t).append(',')
		.append("r=").append(r)
		.append(")\n");

		String cmd = sb.toString();
		sendAsyncCmd(cmd);
	}

	@Override
	public void movel(Pose p, double a, double v, double t, double r) throws Exception {
		final StringBuilder sb = new StringBuilder();
		sb.append("movel(")
		.append(p).append(',')
		.append("a=").append(a).append(',')
		.append("v=").append(v).append(',')
		.append("t=").append(t).append(',')
		.append("r=").append(r)
		.append(")\n");

		String cmd = sb.toString();
		sendAsyncCmd(cmd);
	}

	@Override
	public void movel(JointPosition q, double a, double v, double t, double r) throws Exception {
		final StringBuilder sb = new StringBuilder();
		sb.append("movel(")
		.append(q).append(',')
		.append("a=").append(a).append(',')
		.append("v=").append(v).append(',')
		.append("t=").append(t).append(',')
		.append("r=").append(r)
		.append(")\n");

		String cmd = sb.toString();
		sendAsyncCmd(cmd);
	}

	@Override
	public void movep(Pose p, double a, double v, double r) throws Exception {
		final StringBuilder sb = new StringBuilder();
		sb.append("movep(")
		.append(p).append(',')
		.append("a=").append(a).append(',')
		.append("v=").append(v).append(',')
		.append("r=").append(r)
		.append(")\n");

		String cmd = sb.toString();
		sendAsyncCmd(cmd);
	}

	@Override
	public void movep(JointPosition q, double a, double v, double r) throws Exception {
		final StringBuilder sb = new StringBuilder();
		sb.append("movep(")
		.append(q).append(',')
		.append("a=").append(a).append(',')
		.append("v=").append(v).append(',')
		.append("r=").append(r)
		.append(")\n");

		String cmd = sb.toString();
		sendAsyncCmd(cmd);
	}

	@Override
	public void servoc(Pose pose, double a, double v, double r) throws Exception {
		final StringBuilder sb = new StringBuilder();
		sb.append("servoc(")
		.append(pose).append(',')
		.append("a=").append(a).append(',')
		.append("v=").append(v).append(',')
		.append("r=").append(r)
		.append(")\n");

		String cmd = sb.toString();
		sendAsyncCmd(cmd);
	}

	@Override
	public void servoc(JointPosition q, double a, double v, double r) throws Exception {
		final StringBuilder sb = new StringBuilder();
		sb.append("servoc(")
		.append(q).append(',')
		.append("a=").append(a).append(',')
		.append("v=").append(v).append(',')
		.append("r=").append(r)
		.append(")\n");

		String cmd = sb.toString();
		sendAsyncCmd(cmd);
	}

	@Override
	public void servoj(JointPosition q, double a, double v, double t, double lookahead_time, double gain) throws Exception {
		final StringBuilder sb = new StringBuilder();
		sb.append("servoj(")
		.append(q).append(',')
		.append("a=").append(a).append(',')
		.append("v=").append(v).append(',')
		.append("t=").append(t).append(',')
		.append("lookahead_time=").append(lookahead_time).append(',')
		.append("gain=").append(gain)
		.append(")\n");

		String cmd = sb.toString();
		sendAsyncCmd(cmd);
	}

	@Override
	public void servoj(Pose pose, double a, double v, double t, double lookahead_time, double gain) throws Exception {
		final StringBuilder sb = new StringBuilder();
		sb.append("servoj(get_inverse_kin(")
		.append(pose).append("),")
		.append("a=").append(a).append(',')
		.append("v=").append(v).append(',')
		.append("t=").append(t).append(',')
		.append("lookahead_time=").append(lookahead_time).append(',')
		.append("gain=").append(gain)
		.append(")\n");

		String cmd = sb.toString();
		sendAsyncCmd(cmd);
	}

	@Override
	public Pose getPose() {
		return getPose(null);
	}

	@Override
	public Pose getPose(Pose result) {
		if(result == null) {
			result = new Pose();
		}
		return result.set(cartesianInfo.getPose());
	}

	@Override
	public Pose getTcpOffset() {
		return cartesianInfo.getTcpOffset().copy();
	}

	@Override
	public JointData[] getJointData() {
		return JointData.copy(jointData);
	}

	@Override
	public JointPosition getJointPosition() {
		return getJointPosition(null);
	}

	@Override
	public JointPosition getJointPosition(JointPosition result) {
		if(result == null) {
			result = new JointPosition();
		}
		result.set(jointData[0].getqActual(), jointData[1].getqActual(), jointData[2].getqActual(), jointData[3].getqActual(), jointData[4].getqActual(), jointData[5].getqActual());
		return result;
	}

	@Override
	public ToolData getToolData() {
		return toolData.copy();
	}

	@Override
	public RobotModeData getRobotModeData() {
		return robotModeData;
	}

	@Override
	public boolean waitFor(Pose pose, double threshold, long timeout) throws Exception {
		if(timeout != 0) {
			if(threshold < 0.001) threshold = 0.001;
			Pose currentPose;
			long lastUpdate = System.currentTimeMillis();
			for(;;) {
				currentPose = getPose();
				if(pose.dst(currentPose) <= threshold) {
					return true;
				}
				if(!getRobotModeData().isProgramRunning()) { // if not has motion
					if(System.currentTimeMillis() - lastUpdate > timeout) {
						return false; // timeout
					}
				} else {
					lastUpdate = System.currentTimeMillis();
				}
				Thread.sleep(5);
			}
		}
		return true;
	}

	@Override
	public boolean waitFor(Pose pose, double threshold) throws Exception {
		return waitFor(pose, threshold, 2_000);
	}

	@Override
	public boolean waitFor(Pose pose) throws Exception {
		return waitFor(pose, 0.01);
	}

	@Override
	public boolean waitFor(JointPosition jointPosition, double threshold, long timeout) throws Exception {
		if(timeout != 0) {
			if(threshold < 0.001) threshold = 0.001;
			JointPosition currentJointPosition;
			long lastUpdate = System.currentTimeMillis();
			for(;;) {
				currentJointPosition = getJointPosition();
				if(jointPosition.dst(currentJointPosition) <= threshold) {
					return true;
				}
				if(!getRobotModeData().isProgramRunning()) { // if not has motion
					if(System.currentTimeMillis() - lastUpdate > timeout) {
						return false; // timeout
					}
				} else {
					lastUpdate = System.currentTimeMillis();
				}
				Thread.sleep(5);
			}
		}
		return true;
	}

	@Override
	public boolean waitFor(JointPosition jointPosition, double threshold) throws Exception {
		return waitFor(jointPosition, threshold, 2_000);
	}

	@Override
	public boolean waitFor(JointPosition jointPosition) throws Exception {
		return waitFor(jointPosition, 0.01);
	}
	
}