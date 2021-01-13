package edu.umb.jsAipotu.client.biochem;

import java.text.ParseException;

import com.google.gwt.text.shared.Parser;

public class AaParser implements Parser<String> {
	
	private static AaParser instance;
	
	public static Parser<String> getInstance() {
		if (instance == null) {
			instance = new AaParser();
		}
		return instance;
	}
	
	protected AaParser() {
		
	}

	public String parse(CharSequence text) throws ParseException {
		return "hello";
	}

}
