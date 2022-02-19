package com.delmesoft.ur;

public class URException extends Exception {

	private static final long serialVersionUID = 1L;

	public URException(String message, Throwable cause) {
		super(message, cause);
	}

	public URException(String message) {
		super(message);
	}

}