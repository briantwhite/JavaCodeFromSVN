/**
 * Chung Ying Yu cs681~3 Fall 2002- Spring 2003 Project VGL File: XWriter.java
 * 
 * @author Chung Ying Yu $Id: XWriter.java,v 1.1 2004-09-24 15:46:39 brian Exp $
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

import java.io.FileOutputStream;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

public class XWriter {
	/** constructor */
	public XWriter() {
	}

	/**
	 * read the value from data object and input to xml file
	 * 
	 * @param data
	 *            store the values
	 * @param onput
	 *            write to FileOutputStream
	 * @throws <code>Exception</code>- when errors occur writting.
	 */
	public void createXML(AData data, FileOutputStream output) {
		try {
			Element root = new Element("Data");
			Document doc = new Document(root);
			Element dominance = new Element("Dominance");
			dominance.setText(data.getDominance());
			root.addContent(dominance);
			Element sexlinked = new Element("Sexlinked");
			sexlinked.setText(data.getSexlinked());
			root.addContent(sexlinked);
			Element chance = new Element("Chance");
			chance.setText(data.getChance());
			root.addContent(chance);
			Element practicemode = new Element("Practicemode");
			practicemode.setText(data.getPracticemode());
			root.addContent(practicemode);

			XMLOutputter outputter = new XMLOutputter();
			String result = outputter.outputString(doc);
			outputter.output(doc, output);
		} catch (Exception e) {
			System.err.println("Cannot run");
		}
	}
}