package com.delmesoft.ur;

public interface UR {
	
	void connect() throws URException;
	
	boolean isConnected();
	
	void disconnect();

}
