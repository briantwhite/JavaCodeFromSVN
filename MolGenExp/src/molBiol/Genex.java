package molBiol;/* this is the main class - the genex application * written by Brian White 2004 *  brian.white@umb.edu *   This program is free software; you can redistribute it and/or * modify it under the terms of the GNU General Public License * as published by the Free Software Foundation; either version 2 * of the License, or (at your option) any later version. * * This program is distributed in the hope that it will be useful, * but WITHOUT ANY WARRANTY; without even the implied warranty of * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the * GNU General Public License for more details. * */ import java.awt.GridLayout;import javax.swing.JPanel;public class Genex extends JPanel {	GeneExpressionWindow upperGeneExpWin;	GeneExpressionWindow lowerGeneExpWin;	GenexParams params;	public Genex() {		super();				params = new GenexParams();		upperGeneExpWin = new GeneExpressionWindow("Upper Gene Window", params, this);		lowerGeneExpWin = new GeneExpressionWindow("Lower Gene Window", params, this);		setLayout(new GridLayout(2,1));		add(upperGeneExpWin);		add(lowerGeneExpWin);	}}