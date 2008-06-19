package GeneticModels;

import org.jdom.Element;

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
 * @version 1.0 $Id: TwoAlleleIncompleteDominanceGeneModel.java,v 1.5 2008-06-19 17:49:57 brian Exp $
 */

public class TwoAlleleIncompleteDominanceGeneModel extends GeneModel {

	private Trait t1;  // one homozygote trait
	private Trait t2;  // other homozygote trait
	private Trait t3;  // heterozygote trait

	public TwoAlleleIncompleteDominanceGeneModel() {
		super();
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

	public void setupGenoPhenoTable() {
		genoPhenoTable = new Phenotype[3][3];

		//there are two alleles and two possible phenos
		// get the phenos first; then load table
		t1 = traitSet.getRandomTrait();   // homozygote 1
		t2 = traitSet.getRandomTrait();   // homozygote 2
		t3 = traitSet.getRandomTrait();   // heterozygote
		
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
		b.append(t1.getBodyPart() + " " + t1.getType() + "<br>");
		b.append("Two Allele Incomplete Dominance<br>");
		b.append("<ul>");
		b.append("<li>" + t1.getTraitName() + " and " + t2.getTraitName() 
				+ " are homozygotes</li>");
		b.append("<li>" + t3.getTraitName() + " is the heterozygote</li>");
		b.append("</ul>");
		
		b.append("<table border=1>");
		b.append("<tr><th>Genotype</th><th>Phenotype</th></tr>");
		b.append("<tr><td>" + t1.getTraitName() + "/" + t1.getTraitName() + "</td>");
		b.append("<td>" + t1.getTraitName() +"</td></tr>");
		
		b.append("<tr><td>" + t1.getTraitName() + "/" + t2.getTraitName() + "</td>");
		b.append("<td>" + t3.getTraitName() +"</td></tr>");
		
		b.append("<tr><td>" + t2.getTraitName() + "/" + t2.getTraitName() + "</td>");
		b.append("<td>" + t2.getTraitName() +"</td></tr>");
		
		b.append("</table>");
		return b.toString();
	}

	public Element save(int index) throws Exception {
		Element e = new Element("GeneModel");
		e.setAttribute("Index", String.valueOf(index));
		e.setAttribute("Type", "TwoAlleleIncompleteDominance");
		e.addContent(new Element("Trait_1").addContent(t1.save()));
		e.addContent(new Element("Trait_2").addContent(t2.save()));
		e.addContent(new Element("Trait_3").addContent(t3.save()));
		return e;
	}

}
