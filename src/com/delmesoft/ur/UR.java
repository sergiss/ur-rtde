package com.delmesoft.ur;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public interface UR {
	
	public static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.#################", new DecimalFormatSymbols(Locale.US));
	
	void connect() throws URException;
	
	boolean isConnected();
	
	void disconnect();

}
