package com.delmesoft.ur;

/*
 * Copyright (c) 2021, Sergio S.- sergi.ss4@gmail.com http://sergiosoriano.com
 */

public class MasterBoardData {

	private int digitalInputBits;
	private int digitalOutputBits;
	private int analogInputRange0;
	private int analogInputRange1;
	private double analogInput0;
	private double analogInput1;
	private int analogInputDomain0;
	private int analogInputDomain1;
	private double analogOutput0;
	private double analogOutput1;
	private double masterBoardTemperature;
	private double robotVoltage48V;
	private double robotCurrent;
	private double masterIOCurrent;

	public int getDigitalInputBits() {
		return digitalInputBits;
	}

	public void setDigitalInputBits(int digitalInputBits) {
		this.digitalInputBits = digitalInputBits;
	}

	public int getDigitalOutputBits() {
		return digitalOutputBits;
	}

	public void setDigitalOutputBits(int digitalOutputBits) {
		this.digitalOutputBits = digitalOutputBits;
	}

	public int getAnalogInputRange0() {
		return analogInputRange0;
	}

	public void setAnalogInputRange0(int analogInputRange0) {
		this.analogInputRange0 = analogInputRange0;
	}

	public int getAnalogInputRange1() {
		return analogInputRange1;
	}

	public void setAnalogInputRange1(int analogInputRange1) {
		this.analogInputRange1 = analogInputRange1;
	}

	public double getAnalogInput0() {
		return analogInput0;
	}

	public void setAnalogInput0(double analogInput0) {
		this.analogInput0 = analogInput0;
	}

	public double getAnalogInput1() {
		return analogInput1;
	}

	public void setAnalogInput1(double analogInput1) {
		this.analogInput1 = analogInput1;
	}

	public int getAnalogInputDomain0() {
		return analogInputDomain0;
	}

	public void setAnalogInputDomain0(int analogInputDomain0) {
		this.analogInputDomain0 = analogInputDomain0;
	}

	public int getAnalogInputDomain1() {
		return analogInputDomain1;
	}

	public void setAnalogInputDomain1(int analogInputDomain1) {
		this.analogInputDomain1 = analogInputDomain1;
	}

	public double getAnalogOutput0() {
		return analogOutput0;
	}

	public void setAnalogOutput0(double analogOutput0) {
		this.analogOutput0 = analogOutput0;
	}

	public double getAnalogOutput1() {
		return analogOutput1;
	}

	public void setAnalogOutput1(double analogOutput1) {
		this.analogOutput1 = analogOutput1;
	}

	public double getMasterBoardTemperature() {
		return masterBoardTemperature;
	}

	public void setMasterBoardTemperature(double masterBoardTemperature) {
		this.masterBoardTemperature = masterBoardTemperature;
	}

	public double getRobotVoltage48V() {
		return robotVoltage48V;
	}

	public void setRobotVoltage48V(double robotVoltage48V) {
		this.robotVoltage48V = robotVoltage48V;
	}

	public double getRobotCurrent() {
		return robotCurrent;
	}

	public void setRobotCurrent(double robotCurrent) {
		this.robotCurrent = robotCurrent;
	}

	public double getMasterIOCurrent() {
		return masterIOCurrent;
	}

	public void setMasterIOCurrent(double masterIOCurrent) {
		this.masterIOCurrent = masterIOCurrent;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MasterBoardData [digitalInputBits=");
		builder.append(digitalInputBits);
		builder.append(", digitalOutputBits=");
		builder.append(digitalOutputBits);
		builder.append(", analogInputRange0=");
		builder.append(analogInputRange0);
		builder.append(", analogInputRange1=");
		builder.append(analogInputRange1);
		builder.append(", analogInput0=");
		builder.append(analogInput0);
		builder.append(", analogInput1=");
		builder.append(analogInput1);
		builder.append(", analogInputDomain0=");
		builder.append(analogInputDomain0);
		builder.append(", analogInputDomain1=");
		builder.append(analogInputDomain1);
		builder.append(", analogOutput0=");
		builder.append(analogOutput0);
		builder.append(", analogOutput1=");
		builder.append(analogOutput1);
		builder.append(", masterBoardTemperature=");
		builder.append(masterBoardTemperature);
		builder.append(", robotVoltage48V=");
		builder.append(robotVoltage48V);
		builder.append(", robotCurrent=");
		builder.append(robotCurrent);
		builder.append(", masterIOCurrent=");
		builder.append(masterIOCurrent);
		builder.append("]");
		return builder.toString();
	}

}