// FoldingException.java
//
//
// Copyright 2004, Ethan Bolker and Bogdan Calota
/* 
 * License Information
 * 
 * This  program  is  free  software;  you can redistribute it and/or modify it 
 * under  the  terms  of  the GNU General Public License  as  published  by the 
 * Free  Software  Foundation; either version 2  of  the  License,  or (at your 
 * option) any later version.
 */

package foldingServer;

/**
 * Parent Exception for the folding package.
 */
public class FoldingException extends Exception {
	public FoldingException(String message) {
		super(message);
	}
}

class InputFormatFoldingException extends FoldingException {
	public InputFormatFoldingException(String s) {
		super(s);
	}
}

class IntegerFormatFoldingException extends InputFormatFoldingException {
	public IntegerFormatFoldingException(String s) {
		super(s);
	}
}
