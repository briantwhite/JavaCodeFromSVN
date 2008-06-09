package VGL;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.jdom.Element;

/**
 * Naing Naing Maw cs681-3 Fall 2002 - Spring 2003 Project VGL File:
 * ~nnmaw/VGL/ModelOdd.java
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

public abstract class ModelOdd implements Model {
	protected String character;

	protected String dominant;

	protected String recessive;

	protected Random random;

	/**
	 * Constructor.
	 */
	public ModelOdd() {
		Traits traits = new Traits();
		character = traits.getCharacter();
		dominant = traits.getGenotype1();
		recessive = traits.getGenotype2();
		random = new Random();
	}

	/**
	 * Constructor for reopening the file.
	 * 
	 * @param root
	 *            the model information in JDom Element object
	 */
	public ModelOdd(Element root) {
		List elements = root.getChildren();

		for (Iterator i = elements.iterator(); i.hasNext();) {
			Element current = (Element) i.next();
			String name = current.getName();
			if (name.equals("Character"))
				character = current.getTextTrim();
			else if (name.equals("Dominant"))
				dominant = current.getTextTrim();
			else if (name.equals("Recessive"))
				recessive = current.getTextTrim();
		}
		random = new Random();
	}

	/**
	 * Constructor for testing purpose.
	 * 
	 * @param c
	 *            the character
	 * @param d
	 *            the dominant trait
	 * @param r
	 *            the recessive trait
	 */
	public ModelOdd(String c, String d, String r) {
		this.character = c;
		this.dominant = d;
		this.recessive = r;
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
	 *            the cage's id
	 * @param o1
	 *            the organism to be crossed
	 * @param o2
	 *            the organism to be crossed
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

		Element ed = new Element("Dominant");
		ed.setText(String.valueOf(dominant));
		em.addContent(ed);

		Element er = new Element("Recessive");
		er.setText(String.valueOf(recessive));
		em.addContent(er);

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
		s.append(".\nThe dominant trait is ").append(dominant);
		s.append(".\nThe recessive trait is ").append(recessive);
		if (getModelNo() == 3)
			s.append(".\nXX = Female; XY = Male");
		if (getModelNo() == 5)
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
		s.append("\n\tDominant=").append(dominant);
		s.append("\n\tRecessive=").append(recessive);
		return s.toString();
	}
}