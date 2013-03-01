package Pelican;
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

import javax.swing.*;

import org.jdom.Element;

import PE.PedigreeExplorer;


import java.awt.*;
import java.util.*;

public class PelicanPerson extends JPanel
{
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

	/*
	 * the array of genotypes used by the student
	 * as she is working through it
	 * one allele pair for each possible model
	 */
	public String[][] workingGenotypes = new String[4][2]; 

	public boolean laidOut=false;
	public boolean root=false;
	public int generation=0;

	/* {{{ constructors */

	public PelicanPerson(Pelican pelican) {
		super();
		this.pelican = pelican;
		setBackground(Color.white);
		setOpaque(false);
		setPreferredSize(new Dimension(xSize,ySize));
		setSize(xSize,ySize);

		for (int i = 0; i < 4; i++) {
			workingGenotypes[i][0] = "?";
			workingGenotypes[i][1] = "?";
		}
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
		Iterator<PelicanPerson> allPersonIt = pelican.getAllPeople().iterator();
		while (allPersonIt.hasNext()) {
			PelicanPerson possibleChild = allPersonIt.next();
			if ((possibleChild.father == this) || (possibleChild.mother == this)) result.add(possibleChild);
		}
		return result;
	}

	public void setGenotype(String[] newGeno) {
		genotype = newGeno;
	}

	public void setWorkingGenotype(int modelNumber, String[] newGeno) {
		workingGenotypes[modelNumber][0] = newGeno[0];
		workingGenotypes[modelNumber][1] = newGeno[1];
	}

	public String getWorkingGenotypeAsString() {
		return workingGenotypes[pelican.getCurrentModelNumber()][0] 
				+ " " 
				+ workingGenotypes[pelican.getCurrentModelNumber()][1];
	}

	public String getGenotypeAsString() {
		return genotype[0] + " " + genotype[1];
	}

	public String toString() {
		StringBuffer b = new StringBuffer();
		b.append("ID:" + id);
		b.append("\tName:" + name);
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

	public Element save() {
		Element e = new Element("Person");
		e.setAttribute("Id", String.valueOf(id));
		e.setAttribute("Name", name);
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

		e.setAttribute("ARgeno", workingGenotypes[0][0] + " " + workingGenotypes[0][1]);
		e.setAttribute("ADgeno", workingGenotypes[1][0] + " " + workingGenotypes[1][1]);
		e.setAttribute("SLRgeno", workingGenotypes[2][0] + " " + workingGenotypes[2][1]);
		e.setAttribute("SLDgeno", workingGenotypes[3][0] + " " + workingGenotypes[3][1]);

		return e;
	}

	public Pelican getPelican() {
		return pelican;
	}

	public void paintComponent(Graphics g) {
		setSize(xSize,ySize);
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;

		if (PedigreeExplorer.thickLines) {
			// turn this off so they don't look blurry when exported
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		} else {
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		}
		g2.setColor(Color.black);

		if (PedigreeExplorer.thickLines) {
			g2.setStroke(new BasicStroke(2)); 
		} else {
			g2.setStroke(new BasicStroke(1)); 
		}

		if (sex==male) {
			if (PedigreeExplorer.thickLines) {
				g2.drawRect(1,1,symbolSize-2,symbolSize-2);	
			} else {
				g2.drawRect(0,0,symbolSize,symbolSize);
			}

			if (affection==affected) {
				g2.fillRect(0,0,symbolSize,symbolSize);
			}
		}

		if (sex==female) {
			if (PedigreeExplorer.thickLines) {
				g2.drawArc(1,1,symbolSize-2,symbolSize-2,0,360);
			} else {
				g2.drawArc(0,0,symbolSize,symbolSize,0,360);
			}

			if (affection==affected) {
				g2.fillArc(0,0,symbolSize,symbolSize,0,360);
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

