package com.delmesoft.ur.utils;

import static com.delmesoft.ur.UR.DECIMAL_FORMAT;

/*
 * Copyright (c) 2021, Sergio S.- sergi.ss4@gmail.com http://sergiosoriano.com
 */

public class Vec3 {
	
public static final Vec3 ZERO = new Vec3();
	
	public double x, y, z;
	
	public Vec3() {}

	public Vec3(Vec3 v) {
		this(v.x, v.y, v.z);
	}

	public Vec3(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vec3 set(Vec3 v) {
		return set(v.x, v.y, v.z);
	}

	public Vec3 set(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
		return this;
	}
	
	public Vec3 add(Vec3 o) {
		return add(o.x, o.y, o.z);
	}
	
	public Vec3 add(double x, double y, double z) {
		this.x += x;
		this.y += y;
		this.z += z;
		return this;
	}

	public double getX() {
		return x;
	}

	public Vec3 setX(double x) {
		this.x = x;
		return this;
	}

	public double getY() {
		return y;
	}

	public Vec3 setY(double y) {
		this.y = y;
		return this;
	}

	public double getZ() {
		return z;
	}

	public Vec3 setZ(double z) {
		this.z = z;
		return this;
	}

	public Vec3 copy() {
		return new Vec3(this);
	}
	
	public double dst(Vec3 v) {
		return this.dst(v.x, v.y, v.z);
	}

	public double dst(double x, double y, double z) {
		double a = x - this.x;
		double b = y - this.y;
		double c = z - this.z;
		return Math.sqrt(a * a + b * b + c * c);
	}

	public double dst2 (Vec3 v) {
		return dst2(v.x, v.y, v.z);
	}

	public double dst2(double x, double y, double z) {
		double a = x - this.x;
		double b = y - this.y;
		double c = z - this.z;
		return a * a + b * b + c * c;
	}
	
	public double dst2Angle (Vec3 v) {
		return dst2Angle(v.x, v.y, v.z);
	}
	
	public double dst2Angle(double x, double y, double z) {
		double a = Utils.normalizeAngle(x - this.x);
		double b = Utils.normalizeAngle(y - this.y);
		double c = Utils.normalizeAngle(z - this.z);
		return a * a + b * b + c * c;
	}

	public Vec3 scl(double v) {
		this.x *= v;
		this.y *= v;
		this.z *= v;
		return this;
	}

	public Vec3 nor() {
		final double len2 = this.len2();
		if (len2 == 0 || len2 == 1)
			return this;
		return this.scl(1.0 / Math.sqrt(len2));
	}

	public double len () {
		return Math.sqrt(x * x + y * y + z * z);
	}

	public static double len2 (final float x, final float y, final float z) {
		return x * x + y * y + z * z;
	}

	public double len2 () {
		return x * x + y * y + z * z;
	}

	public Vec3 addScl(Vec3 v, double d) {
		x += v.x * d;
		y += v.y * d;
		z += v.z * d;
		return this;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(x);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(y);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(z);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		Vec3 other = (Vec3) obj;
		if (Double.doubleToLongBits(x) != Double.doubleToLongBits(other.x))
			return false;
		if (Double.doubleToLongBits(y) != Double.doubleToLongBits(other.y))
			return false;
		if (Double.doubleToLongBits(z) != Double.doubleToLongBits(other.z))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append('(')
		.append(DECIMAL_FORMAT.format(x)).append(',')
		.append(DECIMAL_FORMAT.format(y)).append(',')
		.append(DECIMAL_FORMAT.format(z)).append(')');
		return sb.toString();
	}

}
