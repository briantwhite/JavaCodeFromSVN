/**
 * Chung Ying Yu cs681~3 Fall 2002- Spring 2003 Project VGL File: XReader.java
 * 
 * @author Chung Ying Yu $Id: XReader.java,v 1.1 2004-09-24 15:46:39 brian Exp $
 *         This program is free software; you can redistribute it and/or modify
 *         it under the terms of the GNU General Public License as published by
 *         the Free Software Foundation; either version 2 of the License, or (at
 *         your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 */

import java.io.FileInputStream;
import java.util.Iterator;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

public class XReader {

	public XReader() {
	}

	/**
	 * read the elements form xml file and input to data object
	 * 
	 * @param data
	 *            store the values
	 * @param input
	 *            read xml from FileInputStream
	 * @throws <code>IOException</code>- when errors occur reading.
	 */
	public void setData(AData data, FileInputStream input) {
		try {
			SAXBuilder builder = new SAXBuilder();
			Document doc = builder.build(input);
			loadElements(doc.getRootElement().getChildren(), data);
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	/**
	 * method loads the XML properties from a specific XML elements and set the
	 * data
	 * 
	 * @param elements
	 *            List of elements to load from.
	 * @param data
	 *            store the values
	 */
	public void loadElements(List elements, AData data) {
		// Iterate through each element
		for (Iterator i = elements.iterator(); i.hasNext();) {
			Element current = (Element) i.next();
			String name = current.getName();
			if (name.equals("Dominance"))
				data.setDominance(current.getTextTrim());
			else if (name.equals("Sexlinked"))
				data.setSexlinked(current.getTextTrim());
			else if (name.equals("Chance"))
				data.setChance(current.getTextTrim());
			else if (name.equals("Practicemode"))
				data.setPracticemode(current.getTextTrim());

		}
	}
}

