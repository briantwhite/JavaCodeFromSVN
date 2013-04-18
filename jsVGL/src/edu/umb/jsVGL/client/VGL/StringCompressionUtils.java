package edu.umb.jsVGL.client.VGL;

import java.io.UnsupportedEncodingException;
/*
 * compresses strings from two-byte to one-byte
 * and uncompresses them
 * 
 * only works for chars that can be converted to ASCII
 */

public class StringCompressionUtils {
	
	public static String compress(String input) throws UnsupportedEncodingException {
		// must be even number of characters
		if ((input.length() % 2) == 1) input = input + "*";
		
		StringBuffer out = new StringBuffer();
		byte[] bytes = input.getBytes("US-ASCII");
		int i = 0;
		while (i < input.length()) {
			int code = bytes[i] + (256 * bytes[i + 1]);
			out.append(Character.toChars(code));
			i = i + 2;
		}
		return out.toString();
	}
	
	public static String uncompress(String input) {
		StringBuffer out = new StringBuffer();
		char[] chars = input.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			int x = chars[i];
			out.append(Character.toChars(x & 0x00FF));
			out.append(Character.toChars((x & 0xFF00) >> 8));
		}
		return out.toString();
	}

}
