
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

/**
 * Naing Naing Maw cs681-3 Fall 2002 - Spring 2003 Project VGL File:
 * ~nnmaw/VGL/Configuration.java
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
 * @version 1.0 $Id: Configuration.java,v 1.1 2005-06-01 13:30:44 brian Exp $
 */

public class Configuration {
	private int dominance; //chance of simple dominance

	private int sexLinkage; //chance of autosomal inheritance

	private int chance; //chance of XX/XY inheritance

	private int threeAlleles; //chance of three alleles

	private int hierarchical; // chance of hierarchical dominance

	private boolean practiceMode;

	private boolean logging;

	private String email;

	private boolean suppress;

	private int model;

	/**
	 * Constructor for opening the new problem type with the parameter file.
	 * 
	 * @param file
	 *            the file which contains the information to determine the model
	 */
	public Configuration(File file) {
		try {
			FileInputStream input = new FileInputStream(file);
			SAXBuilder builder = new SAXBuilder();
			Document doc = builder.build(input);
			loadElements(doc.getRootElement().getChildren());
		} catch (IOException e1) {
			System.err.println(e1.getMessage());
		} catch (JDOMException e2) {
			System.err.println(e2.getMessage());
		}
	}

	public Configuration(URL url) {
		try {
			InputStream input = url.openStream();
			SAXBuilder builder = new SAXBuilder();
			Document doc = builder.build(input);
			loadElements(doc.getRootElement().getChildren());
		} catch (IOException e1) {
			System.err.println(e1.getMessage());
		} catch (JDOMException e2) {
			System.err.println(e2.getMessage());
		}
	}

	/**
	 * Constructor for reopening the file.
	 * 
	 * @param root
	 *            the configuration information in JDom Element object
	 */
	public Configuration(Element root) {
		loadElements(root.getChildren());
	}

	/**
	 * Constructor for the testing purpose.
	 * 
	 * @param d
	 *            the dominance probability
	 * @param s
	 *            the sexLinkage probability
	 * @param c
	 *            the chance probability
	 * @param t
	 *            the three allele probability
	 * @param h
	 *            the hierarchical probability
	 */
	public Configuration(int d, int s, int c, int t, int h) {
		dominance = d;
		sexLinkage = s;
		chance = c;
		threeAlleles = t;
		hierarchical = h;
	}

	/**
	 * Retrieve the value from the given JDom Element object and assign the
	 * fields.
	 * 
	 * @param elements
	 *            the configuration information in JDom Element object
	 */
	private void loadElements(List elements) {
		for (Iterator i = elements.iterator(); i.hasNext();) {
			Element current = (Element) i.next();
			String name = current.getName();
			if (name.equals("Dominance"))
				dominance = Integer.parseInt(current.getTextTrim());
			else if (name.equals("Sexlinked"))
				sexLinkage = Integer.parseInt(current.getTextTrim());
			else if (name.equals("Chance"))
				chance = Integer.parseInt(current.getTextTrim());
			else if (name.equals("ThreeAlleles"))
				threeAlleles = Integer.parseInt(current.getTextTrim());
			else if (name.equals("Hierarchical"))
				hierarchical = Integer.parseInt(current.getTextTrim());
			else if (name.equals("Practicemode")) {
				String s = current.getTextTrim();
				if (s.equals("0"))
					practiceMode = false;
				else
					practiceMode = true;
			} else if (name.equals("Logging")) {
				String s = current.getTextTrim();
				if (s.equals("0"))
					logging = false;
				else
					logging = true;
			} else if (name.equals("Email"))
				email = current.getTextTrim();
			else if (name.equals("Suppress")) {
				String s = current.getTextTrim();
				if (s.equals("0"))
					suppress = false;
				else
					suppress = true;
			} else if (name.equals("Model"))
				model = Integer.parseInt(current.getTextTrim());
		}
	}

	/**
	 * Return the dominance probability.
	 * 
	 * @return the dominance probability
	 */
	public int getDominance() {
		return dominance;
	}

	/**
	 * Return the sex-linkage probability.
	 * 
	 * @return the sex-linkage probability
	 */
	public int getSexLinkage() {
		return sexLinkage;
	}

	/**
	 * Return the chance of XY probability.
	 * 
	 * @return the chance of XY probability
	 */
	public int getChance() {
		return chance;
	}

	/**
	 * Return the chance of three alleles probability.
	 * 
	 * @return the chance of three alleles probability
	 */
	public int getThreeAlleles() {
		return threeAlleles;
	}

	/**
	 * Return the chance of hierarchical dominance probability.
	 * 
	 * @return the chance of hierarchical dominance probability
	 */
	public int getHierarchical() {
		return hierarchical;
	}

	/**
	 * Return the practice mode value.
	 * 
	 * @return the practice mode value
	 */
	public boolean getPracticeMode() {
		return practiceMode;
	}

	/**
	 * Return the logging value.
	 * 
	 * @return the logging value
	 */
	public boolean getLogging() {
		return logging;
	}

	/**
	 * Return the email address.
	 * 
	 * @return the email address
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Return the suppress value.
	 * 
	 * @return the suppress value
	 */
	public boolean getSuppress() {
		return suppress;
	}

	/**
	 * Return the model number. If doesn't exist yet, determine the model number
	 * according to probabilities and return it.
	 * 
	 * @return the model number
	 */
	public int getModel() {
		if (model > 0)
			return model; // already defined (1, 2, 3, 4, 5, 6)

		Random r = new Random();
		int temp = 0;
		model = 0;

		int ran = r.nextInt(100); // generate random value: ran
		if (ran < threeAlleles) { //it has 3-alleles (#7-12)
			ran = r.nextInt(100);
			if (ran >= hierarchical) { // circular dominance 8, 10, 12
				temp = 2;
			} else { // hierarchical dominance 7, 9, 11
				temp = 1;
			}
			ran = r.nextInt(100);
			if (ran < sexLinkage) { // autosomal (7, 8)
				model = temp + 6;
			} else { //sex-linked (9-12)
				ran = r.nextInt(100);
				if (ran >= chance) { // ZZ/ZW (11, 12)
					model = temp + 10;
				} else { // XX/XY (9, 10)
					model = temp + 8;
				}
			}
		} else { // two alleles (1-6)
			ran = r.nextInt(100);
			if (ran >= dominance)
				temp = 2; // incomplete dominance
			else
				temp = 1; // simple dominance

			ran = r.nextInt(100); // generate random value
			if (ran < sexLinkage)
				model = temp; // autosomal (model 1 0r 2)
			else { // sex-linkage
				ran = r.nextInt(100); //%100; // generate random value
				if (ran >= chance)
					model = 4 + temp; // (model 5 or 6)
				else
					model = 2 + temp; // (model 3 or 4)
			}
		}
		return model;
	}

	/**
	 * Save this configuration value in the JDom Element format.
	 * 
	 * @return this configuration value in JDom Element format
	 */
	public Element save() {
		try {
			Element conf = new Element("Configuration");

			Element d = new Element("Dominance");
			d.setText(String.valueOf(dominance));
			conf.addContent(d);

			Element s = new Element("Sexlinked");
			s.setText(String.valueOf(sexLinkage));
			conf.addContent(s);

			Element c = new Element("Chance");
			c.setText(String.valueOf(chance));
			conf.addContent(c);

			Element t = new Element("ThreeAlleles");
			t.setText(String.valueOf(threeAlleles));
			conf.addContent(t);

			Element h = new Element("Hierarchical");
			h.setText(String.valueOf(hierarchical));
			conf.addContent(h);

			Element p = new Element("Practicemode");
			if (practiceMode)
				p.setText("1");
			else
				p.setText("0");
			conf.addContent(p);

			Element l = new Element("Logging");
			if (logging)
				l.setText("1");
			else
				l.setText("0");
			conf.addContent(l);

			Element e = new Element("Email");
			e.setText(email);
			conf.addContent(e);

			Element su = new Element("Suppress");
			if (suppress)
				su.setText("1");
			else
				su.setText("0");
			conf.addContent(su);

			Element m = new Element("Model");
			m.setText(String.valueOf(model));
			conf.addContent(m);

			return conf;
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return null;
	}

	/**
	 * For ASCII printing purpose.
	 * 
	 * @return the configuration's information in string format
	 */
	public String toString() {
		StringBuffer s = new StringBuffer();
		s.append("\n\tDominance=").append(dominance);
		s.append("\n\tSex Linkage=").append(sexLinkage);
		s.append("\n\tChance=").append(chance);
		s.append("\n\tPractics Mode=").append(practiceMode);
		s.append("\n\tLogging=").append(logging);
		s.append("\n\tEmail=").append(email);
		s.append("\n\tSuppress=").append(suppress);
		s.append("\n\tModel No=").append(model).append("\n");
		return s.toString();
	}
}