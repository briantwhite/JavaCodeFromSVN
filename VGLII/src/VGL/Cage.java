package VGL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.jdom.Attribute;
import org.jdom.Element;

/**
 * Naing Naing Maw cs681-3 Fall 2002 - Spring 2003 Project VGL File:
 * ~nnmaw/VGL/Cage.java
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
 * @author Naing Naing Maw
 * @version 1.0 $Id$
 */

public class Cage {
	private int id;

	private int count; // organism's id start from 0

	private Organism parent1;

	private Organism parent2;

	private HashMap children;

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
		this.children = new HashMap();
	}

	/**
	 * Constructor for reopening the file.
	 * 
	 * @param root
	 *            the cage information in JDom Element object
	 * @param cages
	 *            the list of cages
	 */
	public Cage(Element root, ArrayList cages) throws Exception {
		this(-1);
		count = 0;
		children.clear();

		List elements1 = root.getAttributes();
		Iterator i1 = elements1.iterator();
		Attribute current1 = (Attribute) i1.next();
		String name1 = current1.getName();
		if (name1.equals("Id"))
			id = Integer.parseInt(current1.getValue());

		List elements = root.getChildren(); // Parents and Phenotype
		for (Iterator i = elements.iterator(); i.hasNext();) {
			Element current = (Element) i.next();
			String name = current.getName();
			if (name.equals("Parents")) {
				List innerE = current.getChildren(); // Organism
				Iterator innerI = innerE.iterator();

				// get parent1
				Element innerC = (Element) innerI.next();
				Organism o = new Organism(innerC);
				Cage tempC = findCage(cages, o.getCageId());
				parent1 = tempC.getOrganism(o.getPhenotype(), o.getId());

				// get parent2
				innerC = (Element) innerI.next();
				o = new Organism(innerC);
				tempC = findCage(cages, o.getCageId());
				parent2 = tempC.getOrganism(o.getPhenotype(), o.getId());
			} else if (name.equals("Phenotype")) {
				String phe = current.getAttributeValue("Value");
				OList a = new OList();
				List innerE = current.getChildren(); // Organism
				for (Iterator innerI = innerE.iterator(); innerI.hasNext();) {
					Element innerC = (Element) innerI.next();
					Organism o = new Organism(innerC);
					count++;
					a.add(o);
				}
				children.put(phe, a);
			}
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
	private Cage findCage(ArrayList cages, int cid) throws Exception {
		for (int i = 0; i < cages.size(); i++) {
			Cage c = (Cage) cages.get(i);
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
	 * Add the given organism to the cage according to its phenotype.
	 * 
	 * @param o
	 *            an organism to be added
	 * @param phenotype
	 *            the organism's phenotype
	 */
	public void addOrganism(Organism o, String phenotype) {
		o.setCageId(id);
		o.setId(count);
		count++;

		OList l = (OList) children.get(phenotype);
		if (l == null) // no such phenotype, so create new array
			l = new OList();
		l.add(o);
		children.put(phenotype, l);
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
		OList l = (OList) children.get(p);
		if (l != null)
			return (Organism) l.find(id);
		throw new GeneticsException("Cannot find the specified phenotype");
	}

	/**
	 * Return the two parents in the ArrayList.
	 * 
	 * @return the two parents
	 */
	public ArrayList getParents() {
		ArrayList temp = new ArrayList();
		temp.add(parent1);
		temp.add(parent2);
		return temp;
	}

	/**
	 * Return the children.
	 * 
	 * @return the children
	 */
	public HashMap getChildren() {
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
		Iterator i = children.keySet().iterator();
		while (i.hasNext()) {
			String phenotype = (String) i.next();
			Element eph = new Element("Phenotype");
			eph.setAttribute("Value", phenotype);

			OList l = (OList) children.get(phenotype);
			for (int j = 0; j < l.size(); j++) {
				Organism o = (Organism) l.get(j);
				eph.addContent(o.save());
			}
			ec.addContent(eph);
		}
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
			s.append(" ").append(parent1.getPhenotype());
			s.append(" from Cage ").append(parent1.getCageId() + 1);
			s.append("\n");
		}
		if (parent2 != null) {
			s.append("\t").append(parent2.getSexString());
			s.append(" ").append(parent2.getPhenotype());
			s.append(" from Cage ").append(parent2.getCageId() + 1);
			s.append("\n");
		}

		s.append("Offspring\n");
		s.append("\tPhenotype\tSex\t\tCount(#)\n");
		Iterator i = children.keySet().iterator();
		while (i.hasNext()) {
			String phenotype = (String) i.next();
			OList l = (OList) children.get(phenotype);
			s.append("\t").append(phenotype);
			s.append("\t\tMale\t\t").append(l.getMaleCount());
			s.append("\n\t").append(phenotype);
			s.append("\t\tFemale\t").append(l.getFemaleCount());
			s.append("\n");
		}
		s.append("\n\n");
		return s.toString();
	}

	/**
	 * For ASCII printing purpose
	 * 
	 * @return the cage's information in string format
	 */
	public String toString() {
		StringBuffer s = new StringBuffer();
		s.append("Cage ").append(id).append("\n");

		// parents
		s.append("Parents\n");
		if (parent1 != null)
			s.append(parent1.toString());
		if (parent2 != null)
			s.append(parent2.toString()).append("\n");

		s.append("Children\n");
		Iterator i = children.keySet().iterator();
		while (i.hasNext()) {
			String phenotype = (String) i.next();
			s.append("Phenotype=").append(phenotype).append("\n");
			int male = 0;
			int female = 0;

			OList l = (OList) children.get(phenotype);
			for (int j = 0; j < l.size(); j++) {
				try {
					Organism o = (Organism) l.get(j);
					s.append(o.toString());
				} catch (Exception e) {
				}
			}
			s.append(l.getMaleCount()).append(" male, ");
			s.append(l.getFemaleCount()).append(" female\n");
		}
		return s.toString();
	}
}