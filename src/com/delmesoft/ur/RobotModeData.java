package com.delmesoft.ur;

/*
 * Copyright (c) 2021, Sergio S.- sergi.ss4@gmail.com http://sergiosoriano.com
 */

public class RobotModeData {

	private long timestamp;             // unsigned long long 8
	private boolean realRobotConnected; // _Bool 1
	private boolean realRobotEnabled;   // _Bool 1
	private boolean powerOnRobot;       // _Bool 1
	private boolean emergencyStopped;   // _Bool 1
	private boolean securityStopped;    // _Bool 1
	private boolean programRunning;     // _Bool 1
	private boolean programPaused;      // _Bool 1
	private int robotMode;              // unsigned char 1
	private int controlMode;            // unsigned char 1
	private double speedFraction;       // double 8
	private double speedScalling;       // double 8
	private double speedFractionLimit;  // double 8
	
	public boolean isReady() {
		return realRobotConnected && realRobotEnabled && powerOnRobot && !emergencyStopped && !securityStopped;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public boolean isRealRobotConnected() {
		return realRobotConnected;
	}

	public void setRealRobotConnected(boolean realRobotConnected) {
		this.realRobotConnected = realRobotConnected;
	}

	public boolean isRealRobotEnabled() {
		return realRobotEnabled;
	}

	public void setRealRobotEnabled(boolean realRobotEnabled) {
		this.realRobotEnabled = realRobotEnabled;
	}

	public boolean isPowerOnRobot() {
		return powerOnRobot;
	}

	public void setPowerOnRobot(boolean powerOnRobot) {
		this.powerOnRobot = powerOnRobot;
	}

	public boolean isEmergencyStopped() {
		return emergencyStopped;
	}

	public void setEmergencyStopped(boolean emergencyStopped) {
		this.emergencyStopped = emergencyStopped;
	}

	public boolean isSecurityStopped() {
		return securityStopped;
	}

	public void setSecurityStopped(boolean securityStopped) {
		this.securityStopped = securityStopped;
	}

	public boolean isProgramRunning() {
		return programRunning;
	}

	public void setProgramRunning(boolean programRunning) {
		this.programRunning = programRunning;
	}

	public boolean isProgramPaused() {
		return programPaused;
	}

	public void setProgramPaused(boolean programPaused) {
		this.programPaused = programPaused;
	}

	public int getRobotMode() {
		return robotMode;
	}

	public void setRobotMode(int robotMode) {
		this.robotMode = robotMode;
	}

	public int getControlMode() {
		return controlMode;
	}

	public void setControlMode(int controlMode) {
		this.controlMode = controlMode;
	}

	public double getSpeedFraction() {
		return speedFraction;
	}

	public void setSpeedFraction(double speedFraction) {
		this.speedFraction = speedFraction;
	}

	public double getSpeedScalling() {
		return speedScalling;
	}

	public void setSpeedScalling(double speedScalling) {
		this.speedScalling = speedScalling;
	}

	public double getSpeedFractionLimit() {
		return speedFractionLimit;
	}

	public void setSpeedFractionLimit(double speedFractionLimit) {
		this.speedFractionLimit = speedFractionLimit;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RobotModeData [timestamp=");
		builder.append(timestamp);
		builder.append(", realRobotConnected=");
		builder.append(realRobotConnected);
		builder.append(", realRobotEnabled=");
		builder.append(realRobotEnabled);
		builder.append(", powerOnRobot=");
		builder.append(powerOnRobot);
		builder.append(", emergencyStopped=");
		builder.append(emergencyStopped);
		builder.append(", securityStopped=");
		builder.append(securityStopped);
		builder.append(", programRunning=");
		builder.append(programRunning);
		builder.append(", programPaused=");
		builder.append(programPaused);
		builder.append(", robotMode=");
		builder.append(robotMode);
		builder.append(", controlMode=");
		builder.append(controlMode);
		builder.append(", speedFraction=");
		builder.append(speedFraction);
		builder.append(", speedScalling=");
		builder.append(speedScalling);
		builder.append(", speedFractionLimit=");
		builder.append(speedFractionLimit);
		builder.append("]");
		return builder.toString();
	}

}
