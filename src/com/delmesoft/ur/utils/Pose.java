package com.delmesoft.ur.utils;

import static com.delmesoft.ur.UR.DECIMAL_FORMAT;

/*
 * Copyright (c) 2021, Sergio S.- sergi.ss4@gmail.com http://sergiosoriano.com
 */

public class Pose {
	
	private Vec3 position; // center point
	private Vec3 orientation; // rotation vector

	public Pose() {
		position = new Vec3();
		orientation = new Vec3();
	}

	public Pose(Pose pose) {
		position = new Vec3(pose.position);
		orientation = new Vec3(pose.orientation);
	}

	public Vec3 getPosition() {
		return position;
	}

	public Pose setPosition(Vec3 position) {
		this.position = position;
		return this;
	}

	public Vec3 getOrientation() {
		return orientation;
	}

	public Pose setOrientation(Vec3 orientation) {
		this.orientation = orientation;
		return this;
	}
	
	public double dst(Pose pose) {
		return Math.sqrt(pose.position.dst2(position) + pose.orientation.dst2Angle(orientation));
	}
	
	public Pose set(Pose pose) {
		final Vec3 position = pose.getPosition();
		final Vec3 orientation = pose.getOrientation();
		return set(position.x, 
				   position.y, 
				   position.z, 
				   orientation.x, 
				   orientation.y, 
				   orientation.z);
	}
	
	public Pose set(double x, double y, double z, double ax, double ay, double az) {
		getPosition().set(x, y, z);
		getOrientation().set(ax, ay, az);
		return this;
	}
	
	public Pose add(Pose value) {
		position.add(value.position);
		orientation.add(value.orientation);
		return this;
	}

	public Pose copy() {
		return new Pose(this);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((orientation == null) ? 0 : orientation.hashCode());
		result = prime * result + ((position == null) ? 0 : position.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pose other = (Pose) obj;
		if (orientation == null) {
			if (other.orientation != null)
				return false;
		} else if (!orientation.equals(other.orientation))
			return false;
		if (position == null) {
			if (other.position != null)
				return false;
		} else if (!position.equals(other.position))
			return false;
		return true;
	}

	@Override
	public String toString() { // DO NOT CHANGE
		StringBuilder sb = new StringBuilder();
		sb.append("p[")
		.append(DECIMAL_FORMAT.format(position.x)).append(',')		
		.append(DECIMAL_FORMAT.format(position.y)).append(',')
		.append(DECIMAL_FORMAT.format(position.z)).append(',')
		.append(DECIMAL_FORMAT.format(orientation.x)).append(',')
		.append(DECIMAL_FORMAT.format(orientation.y)).append(',')
		.append(DECIMAL_FORMAT.format(orientation.z)).append(']');
		return sb.toString();
	}

	public String toStringDeg() {
		StringBuilder sb = new StringBuilder();
		sb.append("p[")
		.append(DECIMAL_FORMAT.format(position.x)).append(',')		
		.append(DECIMAL_FORMAT.format(position.y)).append(',')
		.append(DECIMAL_FORMAT.format(position.z)).append(',')
		.append(DECIMAL_FORMAT.format(Math.toDegrees(orientation.x))).append(',')
		.append(DECIMAL_FORMAT.format(Math.toDegrees(orientation.y))).append(',')
		.append(DECIMAL_FORMAT.format(Math.toDegrees(orientation.z))).append(']');
		return sb.toString();
	}

}