package GeneticModels;

import java.util.Iterator;
import java.util.List;

import org.jdom.Element;

import VGL.Messages;

/**
 * Brian White Summer 2008
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
 * @version 1.0 $Id: TwoAlleleIncompleteDominanceGeneModel.java,v 1.11 2009-09-22 19:48:48 brian Exp $
 */

public class TwoAlleleIncompleteDominanceGeneModel extends GeneModel {

	private Trait t1;  // one homozygote trait
	private Trait t2;  // other homozygote trait
	private Trait t3;  // heterozygote trait

	public TwoAlleleIncompleteDominanceGeneModel() {
		super();
	}
	
	//build from saved work file
	public TwoAlleleIncompleteDominanceGeneModel(
			List<Element> traitList, int chromo, int gene) {
		Iterator<Element> elIt = traitList.iterator();
		t1 = TraitFactory.getInstance().buildTrait(elIt.next(), chromo, gene, 1);
		t2 = TraitFactory.getInstance().buildTrait(elIt.next(), chromo, gene, 2);
		t3 = TraitFactory.getInstance().buildTrait(elIt.next(), chromo, gene, 3);
		setupGenoPhenoTable();
	}


	public Phenotype getPhenotype(Allele a1, Allele a2) {
		return genoPhenoTable[a1.getIntVal()][a2.getIntVal()];
	}

	public Allele[] getRandomAllelePair() {
		// want equal frequency of each PHENOTYPE
		Allele[] allelePair = new Allele[2];
		switch (rand.nextInt(3)) {

		case 0:
			// 1,1 homozygote
			allelePair[0] = new Allele(t1, 1);
			allelePair[1] = new Allele(t1, 1);
			break;

		case 1:
			// 1,2 heterozygote
			// 2 possibilities: 1,2 and 2,1
			if(rand.nextInt(2) == 0) {
				allelePair[0] = new Allele(t1, 1);
				allelePair[1] = new Allele(t2, 2);								
			} else {
				allelePair[0] = new Allele(t2, 2);
				allelePair[1] = new Allele(t1, 1);								
			}
			break;
			
		case 2:
			// 2,2 homozygote
			allelePair[0] = new Allele(t2, 2);
			allelePair[1] = new Allele(t2, 2);	
			
		}
		return allelePair;
	}
	
	public void pickRandomTraits() {
		//there are two alleles and three possible phenos
		// get the phenos first; then load table
		t1 = traitSet.getRandomTrait();   // homozygote 1
		t2 = traitSet.getRandomTrait();   // homozygote 2
		t3 = traitSet.getRandomTrait();   // heterozygote
	}

	public void setupGenoPhenoTable() {
		genoPhenoTable = new Phenotype[3][3];
		
		genoPhenoTable[0][0] = null;  				//impossible
		genoPhenoTable[0][1] = new Phenotype(t1);  	// 1,Y = 1
		genoPhenoTable[0][2] = new Phenotype(t2);   // 2,Y = 2
		
		genoPhenoTable[1][0] = new Phenotype(t1);  	// 1,Y = 1
		genoPhenoTable[1][1] = new Phenotype(t1);  	// 1,1 = 1
		genoPhenoTable[1][2] = new Phenotype(t3);   // 1,2 = 3 (inc dom)
		
		genoPhenoTable[2][0] = new Phenotype(t2);  	// 2,Y
		genoPhenoTable[2][1] = new Phenotype(t3);   // 1,2 = 3 (inc dom)
		genoPhenoTable[2][2] = new Phenotype(t2);   // 2,2
	}

	public String toString() {
		StringBuffer b = new StringBuffer();
		b.append(Messages.getTranslatedTraitName(t1) + "<br>");
		b.append(Messages.getString("VGLII.TwoAlleleIncompleteDominance") + "<br>");
		b.append("<ul>");
		b.append("<li>" + Messages.getString("VGLII." + t1.getTraitName()) + " " 
				+ Messages.getString("VGLII.And") + " " 
				+ Messages.getString("VGLII." + t2.getTraitName()) 
				+ " " + Messages.getString("VGLII.AreHomozygotes") + "</li>");
		b.append("<li>" + Messages.getString("VGLII." + t3.getTraitName()) + " " 
				+ Messages.getString("VGLII.IsTheHeterozygote") + "</li>");
		b.append("</ul>");
		
		b.append("<table border=1>");
		b.append("<tr><th>" + Messages.getString("VGLII.Genotype") + "</th><th>"
				+ Messages.getString("VGLII.Phenotype") + "</th></tr>");
		b.append("<tr><td>" + Messages.getString("VGLII." + t1.getTraitName())
				+ "/" + Messages.getString("VGLII." + t1.getTraitName()) + "</td>");
		b.append("<td>" + Messages.getString("VGLII." + t1.getTraitName()) +"</td></tr>");
		
		b.append("<tr><td>" + Messages.getString("VGLII." + t1.getTraitName()) + "/" 
				+ Messages.getString("VGLII." + t2.getTraitName()) + "</td>");
		b.append("<td>" + Messages.getString("VGLII." + t3.getTraitName()) +"</td></tr>");
		
		b.append("<tr><td>" + Messages.getString("VGLII." + t2.getTraitName()) + "/" 
				+ Messages.getString("VGLII." + t2.getTraitName()) + "</td>");
		b.append("<td>" + Messages.getString("VGLII." + t2.getTraitName()) +"</td></tr>");
		
		b.append("</table>");
		return b.toString();
	}

	public Element save(int index, float rf) throws Exception {
		Element e = new Element("GeneModel");
		e.setAttribute("Index", String.valueOf(index));
		e.setAttribute("Type", "TwoAlleleIncompleteDominance");
		e.setAttribute("RfToPrevious", String.valueOf(rf));
		e.addContent(t1.save(1));
		e.addContent(t2.save(2));
		e.addContent(t3.save(3));
		return e;
	}
}
