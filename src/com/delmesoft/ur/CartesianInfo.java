package com.delmesoft.ur;

import com.delmesoft.ur.utils.Pose;

/*
 * Copyright (c) 2021, Sergio S.- sergi.ss4@gmail.com http://sergiosoriano.com
 */

public class CartesianInfo {
	
	private Pose pose = new Pose();
	
	private Pose tcpOffset = new Pose();

	public Pose getPose() {
		return pose;
	}

	public void setPose(Pose pose) {
		this.pose = pose;
	}

	public Pose getTcpOffset() {
		return tcpOffset;
	}

	public void setTcpOffset(Pose tcpOffset) {
		this.tcpOffset = tcpOffset;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CartesianInfo [pose=");
		builder.append(pose);
		builder.append(", tcpOffset=");
		builder.append(tcpOffset);
		builder.append("]");
		return builder.toString();
	}	

}
