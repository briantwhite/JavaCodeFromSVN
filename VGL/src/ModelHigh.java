
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.jdom.Element;

/**
 * Brian White Summer 2004 Project VGL File:
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
 * @version 1.0 $Id: ModelHigh.java,v 1.1 2004-09-24 15:30:15 brian Exp $
 * 
 * models 7 thru 12 - using three alleles
 */

public abstract class ModelHigh implements Model {
	protected String character;

	protected String homoA;

	protected String homoB;

	protected String homoC;

	protected Random random;

	/**
	 * Constructor.
	 */
	public ModelHigh() {
		Traits traits = new Traits();
		character = traits.getCharacter();
		homoA = traits.getGenotype1();
		homoB = traits.getGenotype2();
		homoC = traits.getGenotype3();
		random = new Random();
	}

	/**
	 * Constructor for reopening the file.
	 * 
	 * @param root
	 *            the model information in JDom Element object
	 */
	public ModelHigh(Element root) {
		List elements = root.getChildren();

		for (Iterator i = elements.iterator(); i.hasNext();) {
			Element current = (Element) i.next();
			String name = current.getName();
			if (name.equals("Character"))
				character = current.getTextTrim();
			else if (name.equals("HomozygoteA"))
				homoA = current.getTextTrim();
			else if (name.equals("HomozygoteB"))
				homoB = current.getTextTrim();
			else if (name.equals("HomozygoteC"))
				homoC = current.getTextTrim();
		}
		random = new Random();
	}

	/**
	 * Constructor for testing purpose.
	 */
	public ModelHigh(String c, String hoA, String hoB, String hoC) {
		this.character = c;
		this.homoA = hoA;
		this.homoB = hoB;
		this.homoC = hoC;
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

		Element ehoA = new Element("HomozygoteA");
		ehoA.setText(String.valueOf(homoA));
		em.addContent(ehoA);

		Element ehoB = new Element("HomozygoteB");
		ehoB.setText(String.valueOf(homoB));
		em.addContent(ehoB);

		Element ehoC = new Element("HomozygoteC");
		ehoC.setText(String.valueOf(homoC));
		em.addContent(ehoC);

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
		s.append(".\nHomozygote trait A is ").append(homoA);
		s.append(".\nHomozygote trait B is ").append(homoB);
		s.append(".\nHomozygote trait C is ").append(homoC);
		if ((getModelNo() == 9) || (getModelNo() == 10))
			s.append(".\nXX = Female; XY = Male");
		if ((getModelNo() == 11) || (getModelNo() == 12))
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
		s.append("\n\tHomozygoteA=").append(homoA);
		s.append("\n\tHomozygoteB=").append(homoB);
		s.append("\n\tHomozygoteC=").append(homoC);
		return s.toString();
	}
}