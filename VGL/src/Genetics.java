
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

/**
 * Naing Naing Maw cs681-3 Fall 2002 - Spring 2003 Project VGL File:
 * ~nnmaw/VGL/Genetics.java
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
 * @version 1.0 $Id: Genetics.java,v 1.3 2005-06-10 17:07:38 brian Exp $
 */

public class Genetics {
	private Configuration conf;

	private int modelNo;

	private Model model;

	/**
	 * Constructor.
	 */
	public Genetics() {
		conf = null;
		modelNo = -1;
		model = null;
	}

	/**
	 * Constructor with parameter file which contains the genetics information.
	 * 
	 * @param file
	 *            the file which contains the genetics information
	 */
	public Genetics(File file) {
		conf = new Configuration(file);
		modelNo = conf.getModel();

		createModelObj(null);
	}

	/**
	 * Create the Model object.
	 * 
	 * @param root
	 *            the model information in JDom Element object
	 */
	private void createModelObj(Element root) {
		switch (modelNo) {
		case 1:
			if (root == null) // new
				model = new ModelOne();
			else
				model = new ModelOne(root);
			break;
		case 2:
			if (root == null) // new
				model = new ModelTwo();
			else
				model = new ModelTwo(root);
			break;
		case 3:
			if (root == null) // new
				model = new ModelThree();
			else
				model = new ModelThree(root);
			break;
		case 4:
			if (root == null) // new
				model = new ModelFour();
			else
				model = new ModelFour(root);
			break;
		case 5:
			if (root == null) // new
				model = new ModelFive();
			else
				model = new ModelFive(root);
			break;
		case 6:
			if (root == null) // new
				model = new ModelSix();
			else
				model = new ModelSix(root);
			break;
		case 7:
			if (root == null) // new
				model = new ModelSeven();
			else
				model = new ModelSeven(root);
			break;			
		case 8:
			if (root == null) // new
				model = new ModelEight();
			else
				model = new ModelEight(root);
			break;			
		case 9:
			if (root == null) // new
				model = new ModelNine();
			else
				model = new ModelNine(root);
			break;			
		case 10:
			if (root == null) // new
				model = new ModelTen();
			else
				model = new ModelTen(root);
			break;			
		case 11:
			if (root == null) // new
				model = new ModelEleven();
			else
				model = new ModelEleven(root);
			break;			
		case 12:
			if (root == null) // new
				model = new ModelTwelve();
			else
				model = new ModelTwelve(root);
			break;			

		}
	}

	/**
	 * Return the chosen model.
	 * 
	 * @return the chosen model
	 */
	public Model getModel() {
		return model;
	}

	/**
	 * Return the email address.
	 * 
	 * @return the email address
	 */
	public String getEmail() {
		return conf.getEmail();
	}

	/**
	 * Return true if the practice mode is on.
	 * 
	 * @return true if the practice mode is on; false otherwise
	 */
	public boolean getPracticeMode() {
		return conf.getPracticeMode();
	}

	/**
	 * Return the model information.
	 * 
	 * @return the model information
	 */
	public String getModelInfo() {
		return model.getModelInfo();
	}

	/**
	 * Create the field population cage and return it.
	 * 
	 * @return the field population cage
	 */
	public Cage populateFieldPopulation() {
		return model.populate();
	}

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
	public Cage crossTwo(int id, Organism o1, Organism o2) {
		return model.crossTwo(id, o1, o2);
	}

	/**
	 * Save this genetics in the JDom Element format.
	 * 
	 * @param elements
	 *            the list of cages
	 * @param file
	 *            the file where the information to be saved
	 */
	public void save(ArrayList elements, File file) throws Exception {
		FileOutputStream output = new FileOutputStream(file);
		Document doc = getXMLDoc(elements); 
		XMLOutputter outputter = new XMLOutputter();
		outputter.output(doc, output);
	}
	
	/**
	 * generates an XML string representation for use by SaveToServer
	 * @param elements
	 * @return
	 * @throws Exception
	 */
	public String saveAsString(ArrayList elements) throws Exception {
		Document doc = getXMLDoc(elements);
		XMLOutputter outputter = new XMLOutputter();
		return outputter.outputString(doc);
	}
	
	/**
	 * core save method for save and saveToString
	 * @return
	 * @throws Exception
	 */
	private Document getXMLDoc(ArrayList elements) throws Exception {
		// creating the whole tree
		Element root = new Element("Vgl");
		root.addContent(saveMeta());

		Element data = new Element("Data");
		for (int i = 0; i < elements.size(); i++) {
			Cage c = (Cage) elements.get(i);
			data.addContent(c.save());
		}
		root.addContent(data);

		Document doc = new Document(root);
		return doc;
	}

	/**
	 * Save the information of Configuration and Model object.
	 */
	private Element saveMeta() throws Exception {
		Element root = new Element("Meta");

		root.addContent(conf.save());
		root.addContent(model.save());

		return root;
	}

	/**
	 * Get the genetics information from the file and create the list of cages.
	 * 
	 * @param file
	 *            the file containing the information
	 * @return the list of cages
	 */
	
	public ArrayList open(URL url) throws Exception {
		SAXBuilder builder = new SAXBuilder();
		Document doc = builder.build(url);
		return open(doc);
	}
	
	public ArrayList open(File file) throws Exception {
		FileInputStream input = new FileInputStream(file);
		SAXBuilder builder = new SAXBuilder();
		Document doc = builder.build(input);
		return open(doc);
	}
	
	private ArrayList open(Document doc) throws Exception {
		Element root = doc.getRootElement();
		Element meta = root.getChild("Meta");

		// initialize meta part
		initialize(meta);

		// initialize data part
		Element data = root.getChild("Data");
		ArrayList elements = new ArrayList();

		List l = data.getChildren();
		for (Iterator i = l.iterator(); i.hasNext();) {
			Element current = (Element) i.next();
			Cage c = new Cage(current, elements);
			elements.add(c);
		}
		return elements;	
	}

	/**
	 * Get the information of Configuration and Model object.
	 */
	private void initialize(Element root) {
		List elements = root.getChildren();
		for (Iterator i = elements.iterator(); i.hasNext();) {
			Element current = (Element) i.next();
			String name = current.getName();
			if (name.equals("Configuration")) {
				conf = new Configuration(current);
				modelNo = conf.getModel();
			} else if (name.equals("Model"))
				createModelObj(current);
		}
	}

	/**
	 * Print the cages information into the file.
	 * 
	 * @param elements
	 *            the list of cages
	 * @param file
	 *            the file where the information to be saved
	 */
	public void print(ArrayList elements, File file) throws Exception {
		FileWriter output = new FileWriter(file);

		output.write("Character: ");
		output.write(model.getCharacter());
		output.write("\n\n\n");

		for (int i = 0; i < elements.size(); i++) {
			Cage c = (Cage) elements.get(i);
			output.write(c.print());
		}
		output.flush();
		output.close();
	}
}