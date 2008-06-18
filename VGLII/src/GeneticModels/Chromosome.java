package GeneticModels;

import java.util.ArrayList;

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
 * @version 1.0 $Id: Chromosome.java,v 1.8 2008-06-18 20:16:40 brian Exp $
 */

public class Chromosome {

	private ArrayList<Allele> alleles;

	public Chromosome(ArrayList<Allele> alleles) {
		this.alleles = alleles;
	}

	public Chromosome(Chromosome c) {
		this(c.getAllAlleles());
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
		for (int i = 0; i < alleles.size(); i++) {
			e.addContent(alleles.get(i).save(i));
		}
		return e;
	}

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

}
