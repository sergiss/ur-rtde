package com.delmesoft.ur.utils;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

public class Utils {
	
	public static void read(InputStream is, byte[] data, int off, int len) throws IOException {
		int count, n = 0;
		while(n < len) {
			count = is.read(data, off + n, len - n);
			if(count < 0) throw new EOFException();
			n += count;
		}
	}

}