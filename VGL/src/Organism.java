
import java.util.Iterator;
import java.util.List;

import org.jdom.Attribute;
import org.jdom.Element;

/**
 * Naing Naing Maw cs681-3 Fall 2002 - Spring 2003 Project VGL File:
 * ~nnmaw/VGL/Organism.java
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
 * @version 1.0 $Id: Organism.java,v 1.1 2004-09-24 15:30:16 brian Exp $
 */

public class Organism {
	private int id; // organism's id

	private int cageId; // cage's id

	private int sex; // 0 for male, 1 for female

	private String allele1; // genotype

	private String allele2; // genotype

	private String phenotype;

	private int modelNo;

	/**
	 * Constructor.
	 * 
	 * @param cid
	 *            the cage's id
	 * @param s
	 *            the sex type
	 * @param a1
	 *            the first allele
	 * @param a2
	 *            the second allele
	 * @param p
	 *            the phenotype
	 * @param m
	 *            the model number
	 */
	public Organism(int cid, int s, String a1, String a2, String p, int m) {
		this.cageId = cid;
		this.sex = s;
		this.allele1 = a1;
		this.allele2 = a2;
		this.phenotype = p;
		this.modelNo = m;
	}

	/**
	 * Constructor for having only one allele.
	 * 
	 * @param cid
	 *            the cage's id
	 * @param s
	 *            the sex type
	 * @param a1
	 *            the first allele
	 * @param p
	 *            the phenotype
	 * @param m
	 *            the model number
	 */
	public Organism(int cid, int s, String a1, String p, int m) {
		this(cid, s, a1, null, p, m);
	}

	/**
	 * Constructor for not having the cage's id, 1 by default. (purposed for
	 * field population)
	 * 
	 * @param s
	 *            the sex type
	 * @param a1
	 *            the first allele
	 * @param a2
	 *            the second allele
	 * @param p
	 *            the phenotype
	 * @param m
	 *            the model number
	 */
	public Organism(int s, String a1, String a2, String p, int m) {
		this(1, s, a1, a2, p, m);
	}

	/**
	 * Constructor for not having the cage's id and having only one allele.
	 * (purposed for field population)
	 * 
	 * @param s
	 *            the sex type
	 * @param a1
	 *            the first allele
	 * @param p
	 *            the phenotype
	 * @param m
	 *            the model number
	 */
	public Organism(int s, String a1, String p, int m) {
		this(1, s, a1, null, p, m);
	}

	/**
	 * Constructor for reopening the file.
	 * 
	 * @param root
	 *            the organism information in JDom Element object
	 */
	public Organism(Element root) {
		List elements = root.getAttributes();

		for (Iterator i = elements.iterator(); i.hasNext();) {
			Attribute current = (Attribute) i.next();
			String name = current.getName();
			if (name.equals("ModelNo"))
				modelNo = Integer.parseInt(current.getValue());
			else if (name.equals("Id"))
				id = Integer.parseInt(current.getValue());
			else if (name.equals("CageId"))
				cageId = Integer.parseInt(current.getValue());
			else if (name.equals("SexType"))
				sex = Integer.parseInt(current.getValue());
			else if (name.equals("Allele1"))
				allele1 = current.getValue();
			else if (name.equals("Allele2"))
				allele2 = current.getValue();
			else if (name.equals("Phenotype"))
				phenotype = current.getValue();
		}
	}

	/**
	 * Set the organism's id.
	 * 
	 * @param i
	 *            the organism's id
	 */
	public void setId(int i) {
		id = i;
	}

	/**
	 * Get the organism's id.
	 * 
	 * @return the organism's id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Set the cage's id.
	 * 
	 * @param i
	 *            the cage's id
	 */
	public void setCageId(int i) {
		cageId = i;
	}

	/**
	 * Return the cage's id which the organism is in.
	 * 
	 * @return the cage's id
	 */
	public int getCageId() {
		return cageId;
	}

	/**
	 * Return the sex type.
	 * 
	 * @return the sex type
	 */
	public int getSexType() {
		return sex;
	}

	/**
	 * Return the sex type in String format. (0 for Male, otherwise Female)
	 * 
	 * @return the sex type in String format
	 */
	public String getSexString() {
		if (sex == 0)
			return "Male";
		else
			return "Female";
	}

	/**
	 * Return the first allele.
	 * 
	 * @return the first allele
	 */
	public String getAllele1() {
		return allele1;
	}

	/**
	 * Return the first allele (genotype).
	 * 
	 * @return the first allele (genotype)
	 */
	public String getGenotype1() {
		if (allele1 == null) {
			if ((modelNo == 3) || (modelNo == 4))
				return "Y";
			else if ((modelNo == 5) || (modelNo == 6))
				return "W";
		}
		return allele1;
	}

	/**
	 * Return the second allele.
	 * 
	 * @return the second allele
	 */
	public String getAllele2() {
		return allele2;
	}

	/**
	 * Return the second allele (genotype).
	 * 
	 * @return the second allele (genotype)
	 */
	public String getGenotype2() {
		if (allele2 == null) {
			if ((modelNo == 3) || (modelNo == 4))
				return "Y";
			else if ((modelNo == 5) || (modelNo == 6))
				return "W";
		}
		return allele2;
	}

	/**
	 * Return the phenotype.
	 * 
	 * @return the phenotype
	 */
	public String getPhenotype() {
		return phenotype;
	}

	/**
	 * Save this organism in the JDom Element format.
	 * 
	 * @return this organism in JDom Element format
	 */
	public Element save() throws Exception {
		Element orga = new Element("Organism");
		orga.setAttribute("ModelNo", String.valueOf(modelNo));
		orga.setAttribute("Id", String.valueOf(id));
		orga.setAttribute("CageId", String.valueOf(cageId));

		orga.setAttribute("SexType", String.valueOf(sex));

		if (allele1 != null)
			orga.setAttribute("Allele1", String.valueOf(allele1));

		if (allele2 != null)
			orga.setAttribute("Allele2", String.valueOf(allele2));

		if (phenotype != null)
			orga.setAttribute("Phenotype", String.valueOf(phenotype));

		return orga;
	}

	/**
	 * For ASCII printing purpose.
	 * 
	 * @return the organism's information in string format
	 */
	public String toString() {
		StringBuffer s = new StringBuffer();
		s.append("ModelNo=").append(modelNo);
		s.append(", Id=").append(id);
		s.append(", Cage=").append(cageId);

		if (sex == 0)
			s.append(", Sex=male, (");
		else
			s.append(", Sex=female, (");

		s.append(getGenotype1()).append(" ");
		s.append(getGenotype2()).append("), ");
		s.append(phenotype).append("\n");
		return s.toString();
	}
}