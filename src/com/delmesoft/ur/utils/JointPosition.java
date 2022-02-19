package com.delmesoft.ur.utils;

/*
 * Copyright (c) 2021, Sergio S.- sergi.ss4@gmail.com http://sergiosoriano.com
 */

public class JointPosition {

	private double base;
	private double shoulder;
	private double elbow;
	private double wrist1;
	private double wrist2;
	private double wrist3;

	public JointPosition() {}
	
	public JointPosition(JointPosition jp) {
		set(jp);
	}

	public JointPosition(double base, double shoulder, double elbow, double wrist1, double wrist2, double wrist3) {
		set(base, shoulder, elbow, wrist1, wrist2, wrist3);
	}
	
	public JointPosition set(JointPosition jp) {
		return set(jp.base, jp.shoulder, jp.elbow, jp.wrist1, jp.wrist2, jp.wrist3);
	}

	public JointPosition set(double base, double shoulder, double elbow, double wrist1, double wrist2, double wrist3) {
		this.base = base;
		this.shoulder = shoulder;
		this.elbow = elbow;
		this.wrist1 = wrist1;
		this.wrist2 = wrist2;
		this.wrist3 = wrist3;
		return this;
	}
	
	public JointPosition add(JointPosition jp) {
		this.base += jp.base;
		this.shoulder += jp.shoulder;
		this.elbow += jp.elbow;
		this.wrist1 += jp.wrist1;
		this.wrist2 += jp.wrist2;
		this.wrist3 += jp.wrist3;
		return this;
	}
	
	public double dst(JointPosition o) {
		double d1 = Utils.normalizeAngle(o.base - base);
		double d2 = Utils.normalizeAngle(o.shoulder - shoulder);
		double d3 = Utils.normalizeAngle(o.elbow - elbow);
		double d4 = Utils.normalizeAngle(o.wrist1 - wrist1);
		double d5 = Utils.normalizeAngle(o.wrist2 - wrist2);
		double d6 = Utils.normalizeAngle(o.wrist3 - wrist3);
		/*double d1 = Utils.normalizeAngle(o.base) - Utils.normalizeAngle(base);
		double d2 = Utils.normalizeAngle(o.shoulder) - Utils.normalizeAngle(shoulder);
		double d3 = Utils.normalizeAngle(o.elbow) - Utils.normalizeAngle(elbow);
		double d4 = Utils.normalizeAngle(o.wrist1) - Utils.normalizeAngle(wrist1);
		double d5 = Utils.normalizeAngle(o.wrist2) - Utils.normalizeAngle(wrist2);
		double d6 = Utils.normalizeAngle(o.wrist3) - Utils.normalizeAngle(wrist3);*/
		return Math.sqrt(d1 * d1 + d2 * d2 + d3 * d3 + d4 * d4 + d5 * d5 + d6 * d6);
	}

	public double getBase() {
		return base;
	}

	public void setBase(double base) {
		this.base = base;
	}

	public double getShoulder() {
		return shoulder;
	}

	public void setShoulder(double shoulder) {
		this.shoulder = shoulder;
	}

	public double getElbow() {
		return elbow;
	}

	public void setElbow(double elbow) {
		this.elbow = elbow;
	}

	public double getWrist1() {
		return wrist1;
	}

	public void setWrist1(double wrist1) {
		this.wrist1 = wrist1;
	}

	public double getWrist2() {
		return wrist2;
	}

	public void setWrist2(double wrist2) {
		this.wrist2 = wrist2;
	}

	public double getWrist3() {
		return wrist3;
	}

	public void setWrist3(double wrist3) {
		this.wrist3 = wrist3;
	}

	@Override
	public String toString() { // DO NOT CHANGE
		StringBuilder builder = new StringBuilder();
		builder.append('[');
		builder.append(base);
		builder.append(',');
		builder.append(shoulder);
		builder.append(',');
		builder.append(elbow);
		builder.append(',');
		builder.append(wrist1);
		builder.append(',');
		builder.append(wrist2);
		builder.append(',');
		builder.append(wrist3);
		builder.append(']');
		return builder.toString();
	}
	
	public String toStringDeg() {
		StringBuilder builder = new StringBuilder();
		builder.append('[');
		builder.append(Math.toDegrees(base));
		builder.append(',');
		builder.append(Math.toDegrees(shoulder));
		builder.append(',');
		builder.append(Math.toDegrees(elbow));
		builder.append(',');
		builder.append(Math.toDegrees(wrist1));
		builder.append(',');
		builder.append(Math.toDegrees(wrist2));
		builder.append(',');
		builder.append(Math.toDegrees(wrist3));
		builder.append(']');
		return builder.toString();
	}


}
