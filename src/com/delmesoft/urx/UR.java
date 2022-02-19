package com.delmesoft.urx;

public interface UR {
	
	void connect() throws URException;
	
	boolean isConnected();
	
	void disconnect();

}
