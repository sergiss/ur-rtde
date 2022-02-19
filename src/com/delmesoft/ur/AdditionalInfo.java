package com.delmesoft.ur;

/*
 * Copyright (c) 2021, Sergio S.- sergi.ss4@gmail.com http://sergiosoriano.com
 */

public class AdditionalInfo {

	private boolean teachButtonPressed;
	private boolean teachButtonEnabled;
	private boolean ioEnabledFreeDrive;

	public boolean isTeachButtonPressed() {
		return teachButtonPressed;
	}

	public void setTeachButtonPressed(boolean teachButtonPressed) {
		this.teachButtonPressed = teachButtonPressed;
	}

	public boolean isTeachButtonEnabled() {
		return teachButtonEnabled;
	}

	public void setTeachButtonEnabled(boolean teachButtonEnabled) {
		this.teachButtonEnabled = teachButtonEnabled;
	}
	
	public boolean isIoEnabledFreeDrive() {
		return ioEnabledFreeDrive;
	}

	public void setIoEnabledFreeDrive(boolean ioEnabledFreeDrive) {
		this.ioEnabledFreeDrive = ioEnabledFreeDrive;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AdditionalInfo [teachButtonPressed=");
		builder.append(teachButtonPressed);
		builder.append(", teachButtonEnabled=");
		builder.append(teachButtonEnabled);
		builder.append(", ioEnabledFreeDrive=");
		builder.append(ioEnabledFreeDrive);
		builder.append("]");
		return builder.toString();
	}

}
