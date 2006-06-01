//ProtexAdminGUI.java
//
//
// Copyright 2004-2005 MGX Team UMB.  All rights reserved.
/* 
 * License Information
 * 
 * This  program  is  free  software;  you can redistribute it and/or modify it 
 * under  the  terms  of  the GNU General Public License  as  published  by the 
 * Free  Software  Foundation; either version 2  of  the  License,  or (at your 
 * option) any later version.
 * created by Tao-Hung Jung
 */
package protex;
import java.awt.Color;

/**
 * @author Tao-Hung Jung
 *
 * Cursor indicates where in the inputPalette the new amino acide will 
 * be added or deleted.
 */
public class Cursor {
	private static Color CURSOR_COLOR = Color.RED;
    // indicate the position of the cursor in inputPalette, cindex = the index of
	// the amino acide to the left of the cursor, -1 if no amino acide is to the left
	// of the cursor
	private int cindex; 
	
	public Cursor(){
		cindex = -1;
	}
	public int getCIndex(){
		return cindex;
	}
	public void incrementCIndex(){
		cindex++;
	}
	public void decrementCIndex(){
		cindex--;
	}
	public void reset(){
		cindex = -1;
	}
	public void setCIndex(int newCIndex){
		cindex = newCIndex;
	}
	public Color getColor(){
		return CURSOR_COLOR;
	}
}
