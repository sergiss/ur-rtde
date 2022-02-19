package com.delmesoft.ur;

/*
 * Copyright (c) 2021, Sergio S.- sergi.ss4@gmail.com http://sergiosoriano.com
 */

public class JointData {
	
	private double qActual;  // position actual
	private double qTarget;  // position target
	private double qdActual; // position actual degree
	private double iActual; 
	private double vActual; 
	private double tMotor;  
	private double tMicro;
	private int jointMode;
	
	public JointData() {}
	
	public JointData(JointData jointData) {
		this.qActual   = jointData.qActual;
		this.qTarget   = jointData.qTarget;
		this.qdActual  = jointData.qdActual;
		this.iActual   = jointData.iActual;
		this.vActual   = jointData.vActual;
		this.tMotor    = jointData.tMotor;
		this.tMicro    = jointData.tMicro;
		this.jointMode = jointData.jointMode;
	}

	public double getqActual() {
		return qActual;
	}

	public void setqActual(double qActual) {
		this.qActual = qActual;
	}

	public double getqTarget() {
		return qTarget;
	}

	public void setqTarget(double qTarget) {
		this.qTarget = qTarget;
	}

	public double getQdActual() {
		return qdActual;
	}

	public void setQdActual(double qdActual) {
		this.qdActual = qdActual;
	}

	public double getiActual() {
		return iActual;
	}

	public void setiActual(double iActual) {
		this.iActual = iActual;
	}

	public double getvActual() {
		return vActual;
	}

	public void setvActual(double vActual) {
		this.vActual = vActual;
	}

	public double gettMotor() {
		return tMotor;
	}

	public void settMotor(double tMotor) {
		this.tMotor = tMotor;
	}

	public double gettMicro() {
		return tMicro;
	}

	public void settMicro(double tMicro) {
		this.tMicro = tMicro;
	}

	public int getJointMode() {
		return jointMode;
	}

	public void setJointMode(int jointMode) {
		this.jointMode = jointMode;
	}
	
	public JointData copy() {
		return new JointData(this);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("JointData [qActual=");
		builder.append(qActual);
		builder.append(", qTarget=");
		builder.append(qTarget);
		builder.append(", qdActual=");
		builder.append(qdActual);
		builder.append(", iActual=");
		builder.append(iActual);
		builder.append(", vActual=");
		builder.append(vActual);
		builder.append(", tMotor=");
		builder.append(tMotor);
		builder.append(", tMicro=");
		builder.append(tMicro);
		builder.append(", jointMode=");
		builder.append(jointMode);
		builder.append("]");
		return builder.toString();
	}

	public static JointData[] copy(JointData[] jointData) {
		JointData[] result = new JointData[6];
		for(int i = 0; i < 6; ++i) {
			result[i] = jointData[i].copy();
		}
		return result;
	}

}