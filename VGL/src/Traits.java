
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

/**
 * Naing Naing Maw cs681-3 Fall 2002 - Spring 2003 Project VGL File:
 * ~nnmaw/VGL/Traits.java
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
 * @version 1.0 $Id: Traits.java,v 1.1 2004-09-24 15:30:14 brian Exp $
 */

public class Traits {
	private int character;

	private ArrayList availableTraits;

	private int genotype1;

	private int genotype2;

	private int genotype3;

	private ArrayList characterList;

	private HashMap traitList;

	/**
	 * Constructor for new problem. Initialize all character and trait list and
	 * then choose a character and available traits of such character.
	 */
	public Traits() {
		initialize();

		// choose a character from the list randomly
		Random r = new Random();
		character = r.nextInt(characterList.size());

		availableTraits = (ArrayList) traitList.get(getCharacter());

		// genotype1
		genotype1 = r.nextInt(availableTraits.size());

		// genotype2
		do {
			genotype2 = r.nextInt(availableTraits.size());
		} while (genotype2 == genotype1);

		//genotype3
		do {
			genotype3 = r.nextInt(availableTraits.size());
		} while ((genotype3 == genotype1) || (genotype3 == genotype2));
	}

	/**
	 * Constructor for reopening the file.
	 * 
	 * @param root
	 *            the traits information in JDom Element object
	 */
	public Traits(Element root) {
		initialize();
		List elements = root.getChildren();

		for (Iterator i = elements.iterator(); i.hasNext();) {
			Element current = (Element) i.next();
			String name = current.getName();
			if (name.equals("Character"))
				character = Integer.parseInt(current.getTextTrim());
			else if (name.equals("Genotype1"))
				genotype1 = Integer.parseInt(current.getTextTrim());
			else if (name.equals("Genotype2"))
				genotype2 = Integer.parseInt(current.getTextTrim());
			else if (name.equals("Genotype3"))
				genotype3 = Integer.parseInt(current.getTextTrim());
		}
		availableTraits = (ArrayList) traitList.get(getCharacter());
	}

	/**
	 * Constructor for testing purpose.
	 */
	public Traits(int c, int g1, int g2, int g3) {
		initialize();

		this.character = c;
		availableTraits = (ArrayList) traitList.get(getCharacter());
		this.genotype1 = g1;
		this.genotype2 = g2;
		this.genotype3 = g3;
	}

	/**
	 * Constructor for testing purpose.
	 */
	public Traits(int c, int g1, int g2) {
		this(c, g1, g2, -1);
	}

	/**
	 * Return the character which is randomly chosen.
	 * 
	 * @return the character
	 */
	public String getCharacter() {
		if (character >= 0)
			return (String) characterList.get(character);
		else
			return null;
	}

	/**
	 * Return the first trait which is randomly chosen.
	 * 
	 * @return the first trait
	 */
	public String getGenotype1() {
		if (genotype1 >= 0)
			return (String) availableTraits.get(genotype1);
		else
			return null;
	}

	/**
	 * Return the second trait which is randomly chosen.
	 * 
	 * @return the second trait
	 */
	public String getGenotype2() {
		if (genotype2 >= 0)
			return (String) availableTraits.get(genotype2);
		else
			return null;
	}

	/**
	 * Return the third trait which is randomly chosen.
	 * 
	 * @return the third trait
	 */
	public String getGenotype3() {
		if (genotype3 >= 0)
			return (String) availableTraits.get(genotype3);
		else
			return null;
	}

	/**
	 * Save this traits values in the JDom Element format.
	 * 
	 * @return this traits values in JDom Element format
	 */
	public Element save() throws Exception {
		Element traits = new Element("Traits");

		Element c = new Element("Character");
		c.setText(String.valueOf(character));
		traits.addContent(c);

		Element g1 = new Element("Genotype1");
		g1.setText(String.valueOf(genotype1));
		traits.addContent(g1);

		Element g2 = new Element("Genotype2");
		g2.setText(String.valueOf(genotype2));
		traits.addContent(g2);

		Element g3 = new Element("Genotype3");
		g3.setText(String.valueOf(genotype3));
		traits.addContent(g3);

		return traits;
	}

	/**
	 * Initialize a list of characters and traits.
	 */
	private void initialize() {
		characterList = new ArrayList();
		traitList = new HashMap();
		try {
			URL traitsFileURL = Traits.class.getResource("UIimages/traits.xml");
			InputStream input = traitsFileURL.openStream();

			SAXBuilder builder = new SAXBuilder();
			Document doc = builder.build(input);
			Element root = doc.getRootElement();

			List l = root.getChildren(); // characters
			for (Iterator i = l.iterator(); i.hasNext();) {
				Element c = (Element) i.next();

				String cValue = c.getAttributeValue("value");
				characterList.add(cValue);

				ArrayList temp = new ArrayList();

				List el = c.getChildren();
				for (Iterator j = el.iterator(); j.hasNext();) {
					Element t = (Element) j.next();
					temp.add(t.getTextTrim());
				}
				traitList.put(cValue, temp);
			}
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}
}