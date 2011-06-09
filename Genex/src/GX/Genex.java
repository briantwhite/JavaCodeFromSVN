package GX;/* this is the main class - the genex application * written by Brian White 2004 *  brian.white@umb.edu *   This program is free software; you can redistribute it and/or * modify it under the terms of the GNU General Public License * as published by the Free Software Foundation; either version 2 * of the License, or (at your option) any later version. * * This program is distributed in the hope that it will be useful, * but WITHOUT ANY WARRANTY; without even the implied warranty of * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the * GNU General Public License for more details. **/ import java.awt.Dimension;import java.awt.Toolkit;import javax.swing.JFrame;public class Genex extends JFrame {		GenexGUI genexGUI;	GenexParams params;	public Genex() {		super("Gene Explorer");		genexGUI = new GenexGUI();		params = new GenexParams();		params.setAllowPrinting(true);		genexGUI.setupGUI(getContentPane(), params);			    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	    	    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();	    setSize(screenSize.width, 1000);	    setLocation(0,0);	}		public static void main (String[] args) {		Genex myGenex = new Genex();		myGenex.pack();	    myGenex.show();	}	}