package GeneticModels;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;

import org.jdom.Element;

import VGL.GeneticsException;

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

public class Cage {

	private int id;

	private int count; // organism's id start from 0
	
	private Organism parent1;

	private Organism parent2;

	private TreeMap<String, OrganismList> children;
	
	/**
	 * Constructor for field population which has no parent.
	 * 
	 * @param id
	 *            the cage's id
	 */
	public Cage(int id) {
		this(id, null, null);
	}

	/**
	 * Constructor for cross two which has parents and cage's id.
	 * 
	 * @param id
	 *            the cage's id
	 * @param p1
	 *            the first parent
	 * @param p2
	 *            the second parent
	 */
	public Cage(int id, Organism p1, Organism p2) {
		this.id = id;
		this.parent1 = p1;
		this.parent2 = p2;
		this.count = 0;
		children = new TreeMap<String, OrganismList>();
	}
	
	public void setParents(Organism p1, Organism p2) {
		parent1 = p1;
		parent2 = p2;
	}

	public void add(Organism o) {
		o.setCageId(id);
		o.setId(count);
		count++;

		//if there isn't a list of organisms with this pheno
		//  make one
		if (!children.containsKey(o.getPhenotypeString())) {
			OrganismList oList = new OrganismList();
			oList.add(o);
			children.put(o.getPhenotypeString(), oList);
		} else {
			// add the org to the list and add to the hash map
			OrganismList oList = children.get(o.getPhenotypeString());
			oList.add(o);
		}
	}
	
	/**
	 * Return the cage which id is the given cid.
	 * 
	 * @param cages
	 *            the list of cages
	 * @param cid
	 *            the cage's id
	 * @return the cage of the given cid
	 */
	private Cage findCage(ArrayList<Cage> cages, int cid) throws Exception {
		for (int i = 0; i < cages.size(); i++) {
			Cage c = cages.get(i);
			if (c.getId() == cid)
				return c;
		}
		throw new GeneticsException("Cannot find the specified cage.");
	}

	/**
	 * Return the cage's id.
	 * 
	 * @return the cage's id
	 */
	public int getId() {
		return id;
	}


	/**
	 * Return the organism at the given phenotype and index.
	 * 
	 * @param p
	 *            the organism's phenotype
	 * @param id
	 *            the organism's id
	 * @return the organism at the given index
	 */
	public Organism getOrganism(String p, int id) throws Exception {
		OrganismList l = children.get(p);
		if (l != null)
			return (Organism) l.find(id);
		throw new GeneticsException("Cannot find the specified phenotype");
	}

	/**
	 * Return the two parents in the ArrayList.
	 * 
	 * @return the two parents
	 */
	public ArrayList<Organism> getParents() {
		ArrayList<Organism> temp = new ArrayList<Organism>();
		temp.add(parent1);
		temp.add(parent2);
		return temp;
	}

	/**
	 * Return the children.
	 * 
	 * @return the children
	 */
	public TreeMap<String, OrganismList> getChildren() {
		return children;
	}

	/**
	 * Save this cage in the JDom Element format.
	 * 
	 * @return this cage in JDom Element format
	 */
	public Element save() throws Exception {
		Element ec = new Element("Cage");
		ec.setAttribute("Id", String.valueOf(id));

		// parents
		if ((parent1 != null) || (parent2 != null)) {
			Element ep = new Element("Parents");
			if (parent1 != null)
				ep.addContent(parent1.save());
			if (parent2 != null)
				ep.addContent(parent2.save());
			ec.addContent(ep);
		}

		// children
		Element echildren = new Element("Children");
		Iterator<String> i = children.keySet().iterator();
		while (i.hasNext()) {
			String phenotype = i.next();
			Element eph = new Element("Phenotype");
			eph.setAttribute("Value", phenotype);

			OrganismList l = children.get(phenotype);
			for (int j = 0; j < l.getTotalNumber(); j++) {
				Organism o = l.get(j);
				eph.addContent(o.save());
			}
			echildren.addContent(eph);
		}
		ec.addContent(echildren);
		
		return ec;
	}


	/**
	 * Return the cage's information in String format for print purpose.
	 * 
	 * @return the cage's information in String format for print purpose
	 */
	public String print() {
		StringBuffer s = new StringBuffer();
		s.append("\t\t\tCage ").append(id + 1).append("\n");

		s.append("Parents\n");
		if (parent1 != null) {
			s.append("\t").append(parent1.getSexString());
			s.append(" ").append(parent1.getPhenotypeString());
			s.append(" from Cage ").append(parent1.getCageId() + 1);
			s.append("\n");
		}
		if (parent2 != null) {
			s.append("\t").append(parent2.getSexString());
			s.append(" ").append(parent2.getPhenotypeString());
			s.append(" from Cage ").append(parent2.getCageId() + 1);
			s.append("\n");
		}

		s.append("Offspring\n");
		s.append("\tPhenotype\tSex\t\tCount(#)\n");
		Iterator<String> i = children.keySet().iterator();
		while (i.hasNext()) {
			String phenotype = i.next();
			OrganismList l = children.get(phenotype);
			s.append("\t").append(phenotype);
			s.append("\t\tMale\t\t").append(l.getNumberOfMales());
			s.append("\n\t").append(phenotype);
			s.append("\t\tFemale\t").append(l.getNumberOfFemales());
			s.append("\n");
		}
		s.append("\n\n");
		return s.toString();
	}

	public String toString() {
		StringBuffer b = new StringBuffer();
		b.append("results:\n");
		for (String phenoString: children.keySet()) {
			OrganismList oList = children.get(phenoString);
			b.append(phenoString + ": " 
					+ oList.getTotalNumber() + ", "
					+ oList.getNumberOfMales() + " males, " 
					+ oList.getNumberOfFemales() + " females\n");
		}
		return b.toString();
	}

}
