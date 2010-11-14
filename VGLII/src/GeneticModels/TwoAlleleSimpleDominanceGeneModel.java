package GeneticModels;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

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
 * @version 1.0 $Id$
 */

public class TwoAlleleSimpleDominanceGeneModel extends GeneModel {

	private Trait t1;  // recessive trait
	private Trait t2;  // dominant trait

	public TwoAlleleSimpleDominanceGeneModel(int index) {
		super(index);
	}
	
	//build from saved work file
	public TwoAlleleSimpleDominanceGeneModel(
			List<Element> traitList, int chromo, int gene) {
		super(gene);
		Iterator<Element> elIt = traitList.iterator();
		t1 = TraitFactory.getInstance().buildTrait(elIt.next(), chromo, gene, 1, true);
		t2 = TraitFactory.getInstance().buildTrait(elIt.next(), chromo, gene, 2, true);
		setupGenoPhenoTable();
	}

	public Phenotype getPhenotype(Allele a1, Allele a2) {
		return genoPhenoTable[a1.getIntVal()][a2.getIntVal()];
	}
	
	public Allele[] getRandomAllelePair(boolean trueBreeding) {
		// want equal frequency of each PHENOTYPE unless true breeding
		Allele[] allelePair = new Allele[2];
		if (rand.nextInt(2) == 0) {
			// recessive pheno (1,1)
			allelePair[0] = new Allele(t1, 1);
			allelePair[1] = new Allele(t1, 1);
		} else {
			// dominant pheno - 2 ways to be this
			if ((rand.nextInt(2) == 0) || trueBreeding) {
				// 2,2 homozygote
				allelePair[0] = new Allele(t2, 2);
				allelePair[1] = new Allele(t2, 2);				
			} else {
				// 1,2 heterozygote; 2 ways (1,2) and (2,1)
				if(rand.nextInt(2) == 0) {
					allelePair[0] = new Allele(t1, 1);
					allelePair[1] = new Allele(t2, 2);								
				} else {
					allelePair[0] = new Allele(t2, 2);
					allelePair[1] = new Allele(t1, 1);								
				}
			}
		}
		return allelePair;
	}

	public void setupTraits() {
		//there are two alleles and two possible phenos
		// get the phenos first; then load table
		charSpecBank = CharacterSpecificationBank.getInstance();
		traitSet = charSpecBank.getRandomTraitSet();
		t1 = traitSet.getRandomTrait();   // recessive
		t2 = traitSet.getRandomTrait();   // dominant	
	}


	public void setupGenoPhenoTable() {
		genoPhenoTable = new Phenotype[3][3];

		genoPhenoTable[0][0] = null;  				//impossible
		genoPhenoTable[0][1] = new Phenotype(t1);  	// 1,Y = 1
		genoPhenoTable[0][2] = new Phenotype(t2);   // 2,Y = 2
		
		genoPhenoTable[1][0] = new Phenotype(t1);  	// 1,Y = 1
		genoPhenoTable[1][1] = new Phenotype(t1);  	// 1,1 = 1
		genoPhenoTable[1][2] = new Phenotype(t2);   // 1,2 = 2 (2 is dom)
		
		genoPhenoTable[2][0] = new Phenotype(t2);  	// 2,Y
		genoPhenoTable[2][1] = new Phenotype(t2);   // 1,2 = 2 (2 is dom)
		genoPhenoTable[2][2] = new Phenotype(t2);   // 2,2
	}

	public String toString() {
		StringBuffer b = new StringBuffer();
		b.append(Messages.getInstance().getTranslatedTraitName(t1) + "<br>");
		b.append(Messages.getInstance().getString("VGLII.TwoAlleleSimpleDominance") + "<br>");
		b.append("<ul>");
		b.append("<li>" + Messages.getInstance().getString("VGLII." + t1.getTraitName()) + " " 
				+ Messages.getInstance().getString("VGLII.IsRecessive") + "</li>");
		b.append("<li>" + Messages.getInstance().getString("VGLII." + t2.getTraitName()) + " " 
				+ Messages.getInstance().getString("VGLII.IsDominant") + "</li>");
		b.append("</ul>");
		
		b.append("<table border=1>");
		b.append("<tr><th>" + Messages.getInstance().getString("VGLII.Genotype") + "</th><th>"
				+ Messages.getInstance().getString("VGLII.Phenotype") + "</th></tr>");
		b.append("<tr><td>" + Messages.getInstance().getString("VGLII." + t1.getTraitName()) + "/" 
				+ Messages.getInstance().getString("VGLII." + t1.getTraitName()) + "</td>");
		b.append("<td>" + Messages.getInstance().getString("VGLII." +t1.getTraitName()) +"</td></tr>");
		
		b.append("<tr><td>" + Messages.getInstance().getString("VGLII." + t1.getTraitName()) + "/" 
				+ Messages.getInstance().getString("VGLII." + t2.getTraitName()) + "</td>");
		b.append("<td>" + Messages.getInstance().getString("VGLII." + t2.getTraitName()) +"</td></tr>");
		
		b.append("<tr><td>" + Messages.getInstance().getString("VGLII." + t2.getTraitName()) 
				+ "/" + Messages.getInstance().getString("VGLII." + t2.getTraitName()) + "</td>");
		b.append("<td>" + Messages.getInstance().getString("VGLII." + t2.getTraitName()) +"</td></tr>");
				
		b.append("</table>");
		return b.toString();
	}

	public Element save(int index, float rf) throws Exception {
		Element e = new Element("GeneModel");
		e.setAttribute("Index", String.valueOf(index));
		e.setAttribute("Type", "TwoAlleleSimpleDominance");
		e.setAttribute("RfToPrevious", String.valueOf(rf));
		e.addContent(t1.save(1));
		e.addContent(t2.save(2));
		return e;
	}

}
