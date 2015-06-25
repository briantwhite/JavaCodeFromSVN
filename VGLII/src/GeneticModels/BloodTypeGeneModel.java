package GeneticModels;

import java.util.Iterator;
import java.util.List;

import org.jdom.Element;

import VGL.Messages;

/**
 * Brian White Summer 2015
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 * 
 * @author Brian White
 * @version 1.0 $Id: BloodTypeGeneModel.java,v 1.11 2009-09-22 19:48:48 brian Exp $
 */

public class BloodTypeGeneModel extends GeneModel {

	/*
	 * Trait t1: "O"  
	 * Trait t2: "A"
	 * Trait t3: "B"  
	 * Trait t4: "AB"
	 * Trait t5: null
	 * Trait t6: null
	 */

	public BloodTypeGeneModel(int index) {
		super(index);
	}

	//build from saved work file
	public BloodTypeGeneModel(List<Element> traitList, int chromo, int gene) {
		super(gene);
		Iterator<Element> elIt = traitList.iterator();
		t1 = TraitFactory.getInstance().buildTrait(elIt.next(), chromo, gene, 1, true);
		t2 = TraitFactory.getInstance().buildTrait(elIt.next(), chromo, gene, 2, true);
		t3 = TraitFactory.getInstance().buildTrait(elIt.next(), chromo, gene, 3, true);
		t4 = TraitFactory.getInstance().buildTrait(elIt.next(), chromo, gene, 4, true);
		setupGenoPhenoTable();
	}

	public Phenotype getPhenotype(Allele a1, Allele a2) {
		return genoPhenoTable[a1.getIntVal()][a2.getIntVal()];
	}

	public Allele[] getRandomAllelePair(boolean trueBreeding) {
		// want equal frequency of each PHENOTYPE
		Allele[] allelePair = new Allele[2];
		int x;
		switch (rand.nextInt(4)) {

		case 0:
			// type O
			// 1 possibility 1,1
			allelePair[0] = new Allele(t1, 1);
			allelePair[1] = new Allele(t1, 1);
			break;

		case 1:
			// type A
			// 3 possibilities: 1,2 2,1 2,2
			x = rand.nextInt(3);
			switch (x) {
			case 0:
				allelePair[0] = new Allele(t1, 1);
				allelePair[1] = new Allele(t2, 2);
				break;
			case 1:
				allelePair[0] = new Allele(t2, 2);
				allelePair[1] = new Allele(t1, 1);
				break;
			case 2:
				allelePair[0] = new Allele(t2, 2);
				allelePair[1] = new Allele(t2, 2);
				break;
			}
			break;

		case 2:
			// type B
			// 3 possibilities 1,3 3,1 3,3
			x = rand.nextInt(3);
			switch (x) {
			case 0:
				allelePair[0] = new Allele(t1, 1);
				allelePair[1] = new Allele(t3, 3);
			case 1:
				allelePair[0] = new Allele(t3, 3);
				allelePair[1] = new Allele(t1, 1);
			case 2:
				allelePair[0] = new Allele(t3, 3);
				allelePair[1] = new Allele(t3, 3);
			}
			break;
			
		case 3:
			// type AB
			// 2 possibilities 2,3 3,2
			x = rand.nextInt(2);
			switch (x) {
			case 0:
				allelePair[0] = new Allele(t2, 2);
				allelePair[1] = new Allele(t3, 3);
			case 1:
				allelePair[0] = new Allele(t3, 3);
				allelePair[1] = new Allele(t2, 2);
			}
		}
		return allelePair;
	}

	public void setupTraits() {
		//there are three alleles and four possible phenos
		// get the phenos first; then load table
		charSpecBank = CharacterSpecificationBank.getInstance();
		traitSet = charSpecBank.getRandomTraitSet();
		t1 = traitSet.getRandomTrait();   // "O"
		t2 = traitSet.getRandomTrait();   // "A"
		t3 = traitSet.getRandomTrait();   // "B"
		t4 = traitSet.getRandomTrait();	  // "AB"
	}

	public void setupGenoPhenoTable() {
		genoPhenoTable = new Phenotype[4][4];

		genoPhenoTable[0][0] = null;  				//impossible
		genoPhenoTable[0][1] = new Phenotype(t1);  	// 1,Y = 1 (O)
		genoPhenoTable[0][2] = new Phenotype(t2);   // 2,Y = 2 (A)
		genoPhenoTable[0][3] = new Phenotype(t3);   // 3,Y = 3 (B)

		genoPhenoTable[1][0] = new Phenotype(t1);  	// 1,Y = 1 (O)
		genoPhenoTable[1][1] = new Phenotype(t1);  	// 1,1 = 1 (O)
		genoPhenoTable[1][2] = new Phenotype(t2);   // 1,2 = 2 (1 is rec to 2) A
		genoPhenoTable[1][3] = new Phenotype(t3);   // 1,3 = 2 (1 is rec to 3) B

		genoPhenoTable[2][0] = new Phenotype(t2);  	// 2,Y = 2 (A)
		genoPhenoTable[2][1] = new Phenotype(t2);   // 2,1 = 2 (2 dom to 1) A
		genoPhenoTable[2][2] = new Phenotype(t2);   // 2,2 = 2 (A)
		genoPhenoTable[2][3] = new Phenotype(t4);   // 2,3 = 4 (AB)

		genoPhenoTable[3][0] = new Phenotype(t3);   // 3,Y = 3 (B)
		genoPhenoTable[3][1] = new Phenotype(t3);   // 3,1 = 3 (3 is dom to 1) (B)
		genoPhenoTable[3][2] = new Phenotype(t4);   // 3,2 = 4 (AB)
		genoPhenoTable[3][3] = new Phenotype(t3);   // 3,3 = 3 (B)
	}

	public String toString() {
		StringBuffer b = new StringBuffer();
		b.append(Messages.getInstance().getTranslatedCharacterName(t1) + "<br>");
		b.append(Messages.getInstance().getString("VGLII.BloodType") + "<br>");

		b.append("<table border=1>");
		b.append("<tr><th>" + Messages.getInstance().getString("VGLII.Genotype") + "</th><th>"
				+ Messages.getInstance().getString("VGLII.Phenotype") + "</th></tr>");
		
		b.append("<tr><td>" + Messages.getInstance().getString("VGLII." + t1.getTraitName()) + "/" 
				+ Messages.getInstance().getString("VGLII." + t1.getTraitName()) + "</td>");
		b.append("<td>" + Messages.getInstance().getString("VGLII." + t1.getTraitName()) +"</td></tr>");

		b.append("<tr><td>" + Messages.getInstance().getString("VGLII." + t2.getTraitName()) + "/" 
				+ Messages.getInstance().getString("VGLII." + t2.getTraitName()) + "</td>");
		b.append("<td>" + Messages.getInstance().getString("VGLII." + t2.getTraitName()) +"</td></tr>");
		
		b.append("<tr><td>" + Messages.getInstance().getString("VGLII." + t3.getTraitName()) + "/" 
				+ Messages.getInstance().getString("VGLII." + t3.getTraitName()) + "</td>");
		b.append("<td>" + Messages.getInstance().getString("VGLII." + t3.getTraitName()) +"</td></tr>");

		b.append("<tr><td>" + Messages.getInstance().getString("VGLII." + t1.getTraitName()) + "/" + 
				Messages.getInstance().getString("VGLII." + t2.getTraitName()) + "</td>");
		b.append("<td>" + Messages.getInstance().getString("VGLII." + t2.getTraitName()) +"</td></tr>");

		b.append("<tr><td>" + Messages.getInstance().getString("VGLII." + t1.getTraitName()) + "/" 
				+ Messages.getInstance().getString("VGLII." + t3.getTraitName()) + "</td>");
		b.append("<td>" + Messages.getInstance().getString("VGLII." + t3.getTraitName()) +"</td></tr>");
		
		b.append("<tr><td>" + Messages.getInstance().getString("VGLII." + t2.getTraitName()) + "/" 
				+ Messages.getInstance().getString("VGLII." + t3.getTraitName()) + "</td>");
		b.append("<td>" + Messages.getInstance().getString("VGLII." + t4.getTraitName()) +"</td></tr>");

		b.append("</table>");
		return b.toString();
	}

	public Element save(int index, float rf) throws Exception {
		Element e = new Element("GeneModel");
		e.setAttribute("Index", String.valueOf(index));
		e.setAttribute("Type", "BloodType");
		e.setAttribute("RfToPrevious", String.valueOf(rf));
		e.addContent(t1.save(1));
		e.addContent(t2.save(2));
		e.addContent(t3.save(3));
		e.addContent(t4.save(4));
		return e;
	}

	public String getCharacter() {
		return t1.getBodyPart() + " " + t1.getType();
	}

	public Trait[] getTraits() {
		Trait[] t = new Trait[4];
		t[0] = t1;
		t[1] = t2;
		t[2] = t3;
		t[3] = t4;
		return t;
	}

	public String[] getTraitStrings() {
		String[] t = new String[5];
		t[0] = "?";
		t[1] = Messages.getInstance().getTranslatedShortTraitName(t1.getTraitName());
		t[2] = Messages.getInstance().getTranslatedShortTraitName(t2.getTraitName());
		t[3] = Messages.getInstance().getTranslatedShortTraitName(t3.getTraitName());
		t[4] = Messages.getInstance().getTranslatedShortTraitName(t4.getTraitName());
		return t;
	}

	public String getDomTypeText() {
		return "BloodType";
	}

	public String getInteractionHTML() {
		StringBuffer b = new StringBuffer();
		b.append("<ul>");
		b.append("<li>" + t1.getTraitName() + " recessive to all (like type O).</li>");
		b.append("<li>" + t2.getTraitName() + " is dominant to ");
		b.append(t1.getTraitName() + " and co-dominant with " + t3.getTraitName() + " (like type A).</li>");
		b.append("<li>" + t3.getTraitName() + " is dominant to ");
		b.append(t1.getTraitName() + " and co-dominant with " + t2.getTraitName() + " (like type B).</li>");
		b.append(t4.getTraitName() + " is the combination of " + t2.getTraitName() + " and " + t3.getTraitName() + " (like type AB).</li>");
		b.append("</ul>");
		return b.toString();
	}

	public String getNumAlleleText() {
		return "3";
	}

	public int getNumAlleles() {
		return 3;
	}

}
