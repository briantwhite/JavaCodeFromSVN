package edu.umb.jsPedigrees.client.Pelican;
/********************************************************************
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Library General Public
 *  License as published by the Free Software Foundation; either
 *  version 2 of the License, or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Library General Public License for more details.
 *
 *  You should have received a copy of the GNU Library General Public
 *  License along with this library; if not, write to the
 *  Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 *  Boston, MA  02111-1307, USA.
 *
 *  @author Copyright (C) Frank Dudbridge
 *
 ********************************************************************/

//package uk.ac.mrc.rfcgr;

import java.util.Iterator;
import java.util.Vector;

import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.XMLParser;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.event.dom.client.ContextMenuEvent;
import com.google.gwt.event.dom.client.ContextMenuHandler;
import com.google.gwt.user.client.ui.SimplePanel;

public class PelicanPerson extends SimplePanel {

	private Canvas canvas;

	private Pelican pelican;
	public static final int male=1;
	public static final int female=2;
	public static final int affected=2;
	public static final int unaffected=1;
	public static final int carrier=4;
	public static final int unknown=0;
	public static final int unknownID = 0;
	public static final int with_dna=2;
	public static final int without_dna=0;

	// size of clickable area
	private static final int xSizeDefault=30;
	private static final int ySizeDefault=30;
	// total size of a symbol
	private static final int xSpaceDefault=60;
	private static final int ySpaceDefault=60;
	// size of a pedigree symbol
	private static final int symbolSizeDefault=28;

	// working values
	public static int xSize=xSizeDefault;
	public static int ySize=ySizeDefault;
	public static int xSpace=xSpaceDefault;
	public static int ySpace=ySpaceDefault;
	public static int symbolSize=symbolSizeDefault;

	public int sex=male;
	public int affection=unaffected;
	public int id = 0;
	public PelicanPerson father=null;
	public PelicanPerson mother=null;
	public PelicanPerson partner=null;
	public String name="";

	/*
	 * the genotype used by the program
	 * while solving the pedigree
	 */
	public String[] genotype = new String[2];  

	public boolean laidOut=false;
	public boolean root=false;
	public int generation=0;

	/* {{{ constructors */

	public PelicanPerson(Pelican pelican) {
		super();
		this.pelican = pelican;
		canvas = Canvas.createIfSupported();
		canvas.setCoordinateSpaceWidth(xSize);
		canvas.setCoordinateSpaceHeight(ySize);
		setWidget(canvas);
	}

	public PelicanPerson(Pelican pelican, 
			int id, 
			int sex, 
			int generation) {
		this(pelican);
		this.id = id;
		this.sex=sex;
		this.generation=generation;
		genotype[0] = "?";
		genotype[1] = "?";
	}

	//	public PelicanPerson(Pelican pelican, 
	//			String id, 
	//			int sex, 
	//			int generation) {
	//		this(pelican);
	//		this.id = Integer.valueOf(id);
	//		this.sex=sex;
	//		this.generation=generation;
	//		genotype[0] = "?";
	//		genotype[1] = "?";
	//	}


	public PelicanPerson(Pelican pelican, 
			int id, 
			PelicanPerson father, 
			PelicanPerson mother, 
			int sex, 
			int generation) {
		this(pelican);
		this.id = id;
		this.father=father;
		this.mother=mother;
		this.sex=sex;
		this.generation=generation;
		genotype[0] = "?";
		genotype[1] = "?";
	}

	//	public PelicanPerson(Pelican pelican, 
	//			String id, 
	//			PelicanPerson father, 
	//			PelicanPerson mother, 
	//			int sex, 
	//			int generation) {
	//		this(pelican);
	//		this.id = Integer.valueOf(id);
	//		this.father=father;
	//		this.mother=mother;
	//		this.sex=sex;
	//		this.generation=generation;
	//		genotype[0] = "?";
	//		genotype[1] = "?";
	//	}

	public PelicanPerson(Pelican pelican, 
			int id, 
			PelicanPerson father, 
			PelicanPerson mother, 
			int sex, 
			int affection,
			String name,
			int generation,
			String[] genotype) {
		this(pelican);
		this.id = id;
		this.father=father;
		this.mother=mother;
		this.sex=sex;
		this.affection=affection;
		this.name=name;
		this.generation=generation;
		this.genotype=genotype;
	}

	//	public PelicanPerson(Pelican pelican, 
	//			String id,
	//			PelicanPerson father,
	//			PelicanPerson mother,
	//			int sex,
	//			int affection,
	//			String name,
	//			int generation,
	//			String[] genotype) {
	//		this(pelican);
	//		this.id = Integer.valueOf(id);;
	//		this.father=father;
	//		this.mother=mother;
	//		this.sex=sex;
	//		this.affection=affection;
	//		this.name=name;
	//		this.generation=generation;
	//		this.genotype=genotype;
	//	}

	public PelicanPerson(PelicanPerson p) {
		this(p.getPelican());
		this.id = p.id;
		this.father = p.father;
		this.mother = p.mother;
		this.sex = p.sex;
		this.affection = p.affection;
		this.name = p.name;
		this.generation = p.generation;
		this.genotype[0] = p.genotype[0];
		this.genotype[1] = p.genotype[1];
	}

	/* }}} */

	public boolean isOrphan() {
		return(father==null && mother==null);
	}

	public boolean hasMother() {
		return(mother!=null);
	}

	public boolean hasFather() {
		return(father!=null);
	}

	public boolean isRoot() {
		return root;
	}

	public Vector<PelicanPerson> getChildren() {
		Vector<PelicanPerson> result = new Vector<PelicanPerson>();
		PelicanPerson[] allPeople = pelican.getAllPeople();
		for (int i = 0; i < allPeople.length; i++) {
			if ((allPeople[i].father == this) || (allPeople[i].mother == this)) result.add(allPeople[i]);
		}
		return result;
	}

	public void setGenotype(String[] newGeno) {
		genotype = newGeno;
	}

	public String getGenotypeAsString() {
		return genotype[0] + " " + genotype[1];
	}

	public Element save() {
		Document d = XMLParser.createDocument();
		Element e = d.createElement("Person");

		e.setAttribute("Id", String.valueOf(id));
		e.setAttribute("Sex", String.valueOf(sex));
		e.setAttribute("Affection", String.valueOf(affection));

		if (father == null) {
			e.setAttribute("Father", "0");
		} else {
			e.setAttribute("Father", String.valueOf(father.id));			
		}

		if (mother == null) {
			e.setAttribute("Mother", "0");			
		} else {
			e.setAttribute("Mother", String.valueOf(mother.id));
		}
		return e;
	}

	public String toString() {
		StringBuffer b = new StringBuffer();
		b.append("ID:" + id);
		b.append("\tGeneration:" + generation);
		b.append("\tSex:" + sex + "\n");
		b.append("\tAffected:" + affection + "\n");

		if (mother == null) {
			b.append("\tMother: null");
		} else {
			b.append("\tMother:" + mother.id);
		}

		if (father == null) {
			b.append("\tFather: null");
		} else {
			b.append("\tFather:" + father.id);
		}

		b.append("\tChildren:");
		Vector<PelicanPerson> children = getChildren();
		if (children.size() > 0) {
			Iterator<PelicanPerson> it = children.iterator();
			while (it.hasNext()) {
				PelicanPerson p = it.next();
				b.append(p.id + ",");
			}
			b.deleteCharAt(b.length() - 1);
		} else {
			b.append("none");
		}
		b.append("\n");
		return b.toString();
	}


	public Pelican getPelican() {
		return pelican;
	}

	public void drawSymbol() {

		Context2d ctx = canvas.getContext2d();
		
		// clear old symbol
		ctx.clearRect(0, 0, symbolSize + 1, symbolSize + 1);
		
		ctx.setStrokeStyle(CssColor.make("0,0,0"));
		ctx.setLineWidth(1.0f);

		if (sex==male) {
			ctx.strokeRect(0,0,symbolSize,symbolSize);
			if (affection==affected) {
				ctx.fillRect(0,0,symbolSize,symbolSize);
			}
		}

		if (sex==female) {
			// g2.drawArc(0,0,symbolSize,symbolSize,0,360);
			ctx.beginPath();
			ctx.arc(symbolSize/2, symbolSize/2, (symbolSize/2) - 1, 0, 360);

			if (affection==affected) {
				ctx.fill();
			} else {
				ctx.stroke();
			}
		}
	}

	public static void setScale(double s) {
		xSize=(int)(s*xSizeDefault+0.5);
		ySize=(int)(s*ySizeDefault+0.5);
		xSpace=(int)(s*xSpaceDefault+0.5);
		ySpace=(int)(s*ySpaceDefault+0.5);
		symbolSize=(int)(s*symbolSizeDefault+0.5);
	}

	public static void changeScale(double s) {
		xSize=(int)(s*xSize+0.5);
		ySize=(int)(s*ySize+0.5);
		xSpace=(int)(s*xSpace+0.5);
		ySpace=(int)(s*ySpace+0.5);
		symbolSize=(int)(s*symbolSize+0.5);
	}

	public static void changeVspace(int s) {
		if (ySpace+s>ySize) ySpace+=s;
	}

	public static void changeHspace(int s) {
		if (xSpace+s>xSize) xSpace+=s;
	}
}

