package com.delmesoft.ur;

/*
 * Copyright (c) 2021, Sergio S.- sergi.ss4@gmail.com http://sergiosoriano.com
 */

public class ToolData {

	private int analogInputRange2;
	private int analogInputRange3;
	private double analogInput2;
	private double analogInput3;
	private double toolVoltage48v;
	private int toolOutputVoltage;
	private double toolCurrent;
	private double toolTemperature;
	private int toolMode;
	
	public ToolData() {}
	
	public ToolData(ToolData toolData) {
		analogInputRange2 = toolData.analogInputRange2;
		analogInputRange3 = toolData.analogInputRange3;
		analogInput2      = toolData.analogInput2;
		analogInput3      = toolData.analogInput3;
		toolVoltage48v    = toolData.toolVoltage48v;
		toolOutputVoltage = toolData.toolOutputVoltage;
		toolCurrent       = toolData.toolCurrent;
		toolTemperature   = toolData.toolTemperature;
		toolMode          = toolData.toolMode;
	}

	public int getAnalogInputRange2() {
		return analogInputRange2;
	}

	public void setAnalogInputRange2(int analogInputRange2) {
		this.analogInputRange2 = analogInputRange2;
	}

	public int getAnalogInputRange3() {
		return analogInputRange3;
	}

	public void setAnalogInputRange3(int analogInputRange3) {
		this.analogInputRange3 = analogInputRange3;
	}

	public double getAnalogInput2() {
		return analogInput2;
	}

	public void setAnalogInput2(double analogInput2) {
		this.analogInput2 = analogInput2;
	}

	public double getAnalogInput3() {
		return analogInput3;
	}

	public void setAnalogInput3(double analogInput3) {
		this.analogInput3 = analogInput3;
	}

	public double getToolVoltage48v() {
		return toolVoltage48v;
	}

	public void setToolVoltage48v(double toolVoltage48v) {
		this.toolVoltage48v = toolVoltage48v;
	}

	public int getToolOutputVoltage() {
		return toolOutputVoltage;
	}

	public void setToolOutputVoltage(int toolOutputVoltage) {
		this.toolOutputVoltage = toolOutputVoltage;
	}

	public double getToolCurrent() {
		return toolCurrent;
	}

	public void setToolCurrent(double toolCurrent) {
		this.toolCurrent = toolCurrent;
	}

	public double getToolTemperature() {
		return toolTemperature;
	}

	public void setToolTemperature(double toolTemperature) {
		this.toolTemperature = toolTemperature;
	}

	public int getToolMode() {
		return toolMode;
	}

	public void setToolMode(int toolMode) {
		this.toolMode = toolMode;
	}
	
	public ToolData copy() {
		return new ToolData(this);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ToolData [analogInputRange2=");
		builder.append(analogInputRange2);
		builder.append(", analogInputRange3=");
		builder.append(analogInputRange3);
		builder.append(", analogInput2=");
		builder.append(analogInput2);
		builder.append(", analogInput3=");
		builder.append(analogInput3);
		builder.append(", toolVoltage48v=");
		builder.append(toolVoltage48v);
		builder.append(", toolOutputVoltage=");
		builder.append(toolOutputVoltage);
		builder.append(", toolCurrent=");
		builder.append(toolCurrent);
		builder.append(", toolTemperature=");
		builder.append(toolTemperature);
		builder.append(", toolMode=");
		builder.append(toolMode);
		builder.append("]");
		return builder.toString();
	}
	
}