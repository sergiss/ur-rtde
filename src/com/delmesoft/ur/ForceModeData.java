package com.delmesoft.ur;

import com.delmesoft.ur.utils.Pose;

/*
 * Copyright (c) 2021, Sergio S.- sergi.ss4@gmail.com http://sergiosoriano.com
 */

public class ForceModeData {
	
	private Pose pose = new Pose();
	
	private double robotDexterity;

	public Pose getPose() {
		return pose;
	}

	public void setPose(Pose pose) {
		this.pose = pose;
	}

	public double getRobotDexterity() {
		return robotDexterity;
	}

	public void setRobotDexterity(double robotDexterity) {
		this.robotDexterity = robotDexterity;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ForceModeData [pose=");
		builder.append(pose);
		builder.append(", robotDexterity=");
		builder.append(robotDexterity);
		builder.append("]");
		return builder.toString();
	}
	
}