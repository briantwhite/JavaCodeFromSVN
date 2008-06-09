package VGL;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.jdom.Element;

/**
 * Naing Naing Maw cs681-3 Fall 2002 - Spring 2003 Project VGL File:
 * ~nnmaw/VGL/ModelEven.java
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

public abstract class ModelEven implements Model {
	protected String character;

	protected String hete;

	protected String homo1;

	protected String homo2;

	protected Random random;

	/**
	 * Constructor.
	 */
	public ModelEven() {
		Traits traits = new Traits();
		character = traits.getCharacter();
		hete = traits.getGenotype1();
		homo1 = traits.getGenotype2();
		homo2 = traits.getGenotype3();
		random = new Random();
	}

	/**
	 * Constructor for reopening the file.
	 * 
	 * @param root
	 *            the model information in JDom Element object
	 */
	public ModelEven(Element root) {
		List elements = root.getChildren();

		for (Iterator i = elements.iterator(); i.hasNext();) {
			Element current = (Element) i.next();
			String name = current.getName();
			if (name.equals("Character"))
				character = current.getTextTrim();
			else if (name.equals("Heterozygote"))
				hete = current.getTextTrim();
			else if (name.equals("Homozygote1"))
				homo1 = current.getTextTrim();
			else if (name.equals("Homozygote2"))
				homo2 = current.getTextTrim();
		}
		random = new Random();
	}

	/**
	 * Constructor for testing purpose.
	 */
	public ModelEven(String c, String he, String ho1, String ho2) {
		this.character = c;
		this.hete = he;
		this.homo1 = ho1;
		this.homo2 = ho2;
		random = new Random();
	}

	/**
	 * Return the character.
	 * 
	 * @return the character
	 */
	public String getCharacter() {
		return character;
	}

	/**
	 * Create the field population cage and return it.
	 * 
	 * @return the field population cage
	 */
	public abstract Cage populate();

	/**
	 * Cross the given two organisms and return the offspring cage.
	 * 
	 * @param id
	 *            cage's id
	 * @param o1
	 *            organism to be crossed
	 * @param o2
	 *            organism to be crossed
	 * @return the offspring cage
	 */
	public abstract Cage crossTwo(int id, Organism o1, Organism o2);

	/**
	 * Save this model in the JDom Element format.
	 * 
	 * @return this model in JDom Element format
	 */
	public Element save() throws Exception {
		Element em = new Element("Model");

		Element ec = new Element("Character");
		ec.setText(String.valueOf(character));
		em.addContent(ec);

		Element ehe = new Element("Heterozygote");
		ehe.setText(String.valueOf(hete));
		em.addContent(ehe);

		Element eho1 = new Element("Homozygote1");
		eho1.setText(String.valueOf(homo1));
		em.addContent(eho1);

		Element eho2 = new Element("Homozygote2");
		eho2.setText(String.valueOf(homo2));
		em.addContent(eho2);

		return em;
	}

	/**
	 * Return the model name.
	 * 
	 * @return the model name
	 */
	public abstract String getModelName();

	/**
	 * Return the model number.
	 * 
	 * @return the model number
	 */
	public abstract int getModelNo();

	/**
	 * Return the model information.
	 * 
	 * @return the model information
	 */
	public String getModelInfo() {
		StringBuffer s = new StringBuffer();
		s.append("The current model is ").append(getModelName());
		s.append(".\nThe character is ").append(character);
		s.append(".\nThe heterozygote trait is ").append(hete);
		s.append(".\nThe first homozygote trait is ").append(homo1);
		s.append(".\nThe second homozygote trait is ").append(homo2);
		if (getModelNo() == 4)
			s.append(".\nXX = Female; XY = Male");
		if (getModelNo() == 6)
			s.append(".\nZZ = Male; ZW = Female");
		s.append(".");

		return s.toString();
	}

	/**
	 * For ASCII printing purpose.
	 * 
	 * @return the model's information in string format
	 */
	public String toString() {
		StringBuffer s = new StringBuffer();
		s.append("\n\tCharacter=").append(character);
		s.append("\n\tHeterozygote=").append(hete);
		s.append("\n\tHomozygote1=").append(homo1);
		s.append("\n\tHomozygote2=").append(homo2);
		return s.toString();
	}
}