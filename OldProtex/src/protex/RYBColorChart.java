// RYBColor.java
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
 */

package protex;

import java.awt.Color;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Class representing RYBColor chart. Model the standard RYB color model.
 * 
 * @author Namita Singla
 * 
 * -- modified by TJ, makes RYBColorChart singleton
 */
public class RYBColorChart {

	private static final Color BLACK = Color.black;

	private static final Color PURPLE = new Color(204, 0, 204);

	private static final Color GREEN = Color.green;

	private static final Color ORANGE = Color.orange;

	private static final Color YELLOW = Color.yellow;

	private static final Color BLUE = Color.blue;

	private static final Color RED = Color.red;

	private static final Color WHITE = Color.white;
	
	private static RYBColorChart RYBCOLORCHART;

	private Map table;

	/**
	 * Constructor
	 */
	private RYBColorChart() {
		table = new HashMap();
		add(new TwoColors(WHITE, WHITE), WHITE);
		add(new TwoColors(WHITE, RED), RED);
		add(new TwoColors(WHITE, BLUE), BLUE);
		add(new TwoColors(WHITE, YELLOW), YELLOW);
		add(new TwoColors(RED, RED), RED);
		add(new TwoColors(BLUE, BLUE), BLUE);
		add(new TwoColors(YELLOW, YELLOW), YELLOW);
		add(new TwoColors(ORANGE, ORANGE), ORANGE);
		add(new TwoColors(GREEN, GREEN), GREEN);
		add(new TwoColors(PURPLE, PURPLE), PURPLE);
		add(new TwoColors(RED, BLUE), PURPLE);
		add(new TwoColors(RED, YELLOW), ORANGE);
		add(new TwoColors(BLUE, YELLOW), GREEN);
		add(new TwoColors(WHITE, ORANGE), ORANGE);
		add(new TwoColors(WHITE, GREEN), GREEN);
		add(new TwoColors(WHITE, PURPLE), PURPLE);
		add(new TwoColors(RED, ORANGE), ORANGE);
		add(new TwoColors(YELLOW, ORANGE), ORANGE);
		add(new TwoColors(BLUE, GREEN), GREEN);
		add(new TwoColors(YELLOW, GREEN), GREEN);
		add(new TwoColors(RED, PURPLE), PURPLE);
		add(new TwoColors(BLUE, PURPLE), PURPLE);
		add(new TwoColors(RED, GREEN), BLACK);
		add(new TwoColors(YELLOW, PURPLE), BLACK);
		add(new TwoColors(BLUE, ORANGE), BLACK);
		add(new TwoColors(ORANGE, PURPLE), BLACK);
		add(new TwoColors(ORANGE, GREEN), BLACK);
		add(new TwoColors(GREEN, PURPLE), BLACK);
		add(new TwoColors(GREEN, BLACK), BLACK);
		add(new TwoColors(ORANGE, BLACK), BLACK);
		add(new TwoColors(PURPLE, BLACK), BLACK);
		add(new TwoColors(BLACK, BLACK), BLACK);
		add(new TwoColors(BLACK, BLUE), BLACK);
		add(new TwoColors(BLACK, RED), BLACK);
		add(new TwoColors(BLACK, YELLOW), BLACK);
	}

//	/**
//	 * added by TJ -- initialize the color table
//	 *
//	 */
//	private void initializeTable(){
//		table = new HashMap();
//		add(new TwoColors(WHITE, WHITE), WHITE);
//		add(new TwoColors(WHITE, RED), RED);
//		add(new TwoColors(WHITE, BLUE), BLUE);
//		add(new TwoColors(WHITE, YELLOW), YELLOW);
//		add(new TwoColors(RED, RED), RED);
//		add(new TwoColors(BLUE, BLUE), BLUE);
//		add(new TwoColors(YELLOW, YELLOW), YELLOW);
//		add(new TwoColors(ORANGE, ORANGE), ORANGE);
//		add(new TwoColors(GREEN, GREEN), GREEN);
//		add(new TwoColors(PURPLE, PURPLE), PURPLE);
//		add(new TwoColors(RED, BLUE), PURPLE);
//		add(new TwoColors(RED, YELLOW), ORANGE);
//		add(new TwoColors(BLUE, YELLOW), GREEN);
//		add(new TwoColors(WHITE, ORANGE), ORANGE);
//		add(new TwoColors(WHITE, GREEN), GREEN);
//		add(new TwoColors(WHITE, PURPLE), PURPLE);
//		add(new TwoColors(RED, ORANGE), ORANGE);
//		add(new TwoColors(YELLOW, ORANGE), ORANGE);
//		add(new TwoColors(BLUE, GREEN), GREEN);
//		add(new TwoColors(YELLOW, GREEN), GREEN);
//		add(new TwoColors(RED, PURPLE), PURPLE);
//		add(new TwoColors(BLUE, PURPLE), PURPLE);
//		add(new TwoColors(RED, GREEN), BLACK);
//		add(new TwoColors(YELLOW, PURPLE), BLACK);
//		add(new TwoColors(BLUE, ORANGE), BLACK);
//		add(new TwoColors(ORANGE, PURPLE), BLACK);
//		add(new TwoColors(ORANGE, GREEN), BLACK);
//		add(new TwoColors(GREEN, PURPLE), BLACK);
//		add(new TwoColors(GREEN, BLACK), BLACK);
//		add(new TwoColors(ORANGE, BLACK), BLACK);
//		add(new TwoColors(PURPLE, BLACK), BLACK);
//		add(new TwoColors(BLACK, BLACK), BLACK);
//		add(new TwoColors(BLACK, BLUE), BLACK);
//		add(new TwoColors(BLACK, RED), BLACK);
//		add(new TwoColors(BLACK, YELLOW), BLACK);
//	}
	
	public static RYBColorChart getRYBColorChart(){
		if(RYBCOLORCHART == null){
			RYBCOLORCHART = new RYBColorChart();
		}
		return RYBCOLORCHART;
	}
	
	/**
	 * Get iterator over key set
	 * 
	 * @return
	 */
	public Iterator getIterator() {
		return table.keySet().iterator();
	}

	/**
	 * Add TwoColors object and result of mixing them to the table.
	 * 
	 * @param a
	 * @param b
	 */
	private void add(TwoColors a, Color b) {
		table.put(a, b);
	}

//	/**
//	 * added by TJ -- for testing purposes
//	 * @return true if RYBColorChart is initialized
//	 */
//	public boolean isInitialized(){
//		if(table == null){
//			return true;
//		}
//		return false;
//	}
	
	/**
	 * Mix two colors and return the result
	 * @param a
	 * @param b
	 * @return
	 */
	public Color mixTwoColors(Color a, Color b) {
		TwoColors c = new TwoColors(a, b);
		Color result = (Color) table.get(c);
		if (result == null)
			result = (Color) table.get(c.toggleColors());
		return result;
	}

	/**
	 * Mix two colors in TwoColors object
	 * @param a
	 * @return
	 */
	public Color mixTwoColors(TwoColors a) {
		Color result = (Color) table.get(a);
		if (result == null)
			result = (Color) table.get(a.toggleColors());
		return result;
	}
}
