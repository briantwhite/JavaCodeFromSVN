package GeneticModels;

import java.util.ArrayList;
import java.util.Iterator;

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
 * @version 1.0 $Id: Chromosome.java,v 1.13 2009-09-22 19:06:36 brian Exp $
 */

public class Chromosome {

	public final static int AUTOSOME = 1;
	public final static int SEX_CHROMOSOME = 0;

	private ArrayList<Allele> alleles;

	public Chromosome(ArrayList<Allele> alleles) {
		this.alleles = alleles;
	}

	public Chromosome(Chromosome c) {
		this(c.getAllAlleles());
	}

	/**
	 * constructor for building from a work file
	 */
	public Chromosome(Element e, int chromoNum) {
		alleles = new ArrayList<Allele>();
		Iterator<Element> alleleIt = e.getChildren().iterator();
		while (alleleIt.hasNext()) {
			Element alleleE = alleleIt.next();
			int geneIndex = 
				Integer.parseInt(alleleE.getAttributeValue("GeneIndex"));
			int traitNum = 
				Integer.parseInt(alleleE.getAttributeValue("TraitNumber"));
			Allele allele = new Allele(
					TraitFactory.getInstance().getTrait(
							chromoNum, geneIndex, traitNum), 
							traitNum);
			alleles.add(geneIndex, allele);
		}

	}

	public Allele getAllele(int i) {
		return alleles.get(i);
	}

	public ArrayList<Allele> getAllAlleles() {
		return alleles;
	}

	public Element save(String id) throws Exception {
		Element e = new Element("Chromosome");

		e.setAttribute("Id", id);

		if (this == NullSexChromosome.getInstance()) {
			e.setAttribute("Size", String.valueOf(-1));
		} else {
			e.setAttribute("Size", String.valueOf(alleles.size()));
		}

		for (int i = 0; i < alleles.size(); i++) {
			e.addContent(alleles.get(i).save(i));
		}
		return e;
	}

	// returns untranslated genotype info for internal use (counting, etc)
	public String toString() {
		StringBuffer b = new StringBuffer();
		for(Allele a: alleles) {
			b.append(a.getTrait().getTraitName().toString()
					+ "-"
					+ a.getTrait().getBodyPart().toString()
					+ ";");
		}
		if (b.length() > 0) {
			b.deleteCharAt(b.length() - 1);
		}
		return b.toString();
	}

	//returns genotype in local language for external use (display, printing)
	public String toTranslatedString() {
		StringBuffer b = new StringBuffer();
		for(Allele a: alleles) {
			b.append(Messages.getTranslatedAlleleName(a) + ";");
		}
		if (b.length() > 0) {
			b.deleteCharAt(b.length() - 1);
		}
		return b.toString();		
	}

}
