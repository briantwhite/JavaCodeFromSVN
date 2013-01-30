package GeneticModels;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import VGL.EdXServerStrings;

/**
 * Brian White Summer 2008
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
 * @version 1.0 $Id: ProblemTypeSpecification.java,v 1.5 2008-06-24 17:07:51 brian Exp $
 */

public class ProblemTypeSpecification {

	private boolean beginnerMode;

	private boolean fieldPopTrueBreeding;

	private int minOffspring;
	private int maxOffspring;

	// all are probabilities 'ch' is short for 'chance of'
	private float chZZ_ZW;

	private float gene1_chSexLinked;
	private float gene1_ch3Alleles;
	private float gene1_chIncDom;
	private float gene1_chCircDom;

	private float gene2_chPresent;
	private float gene2_chSameChrAsGene1;
	private float gene2_minRfToGene1;
	private float gene2_maxRfToGene1;
	private float gene2_ch3Alleles;
	private float gene2_chIncDom;
	private float gene2_chCircDom;

	private float gene3_chPresent;
	private float gene3_chSameChrAsGene1;
	private float gene3_minRfToPrevGene;
	private float gene3_maxRfToPrevGene;
	private float gene3_ch3Alleles;
	private float gene3_chIncDom;
	private float gene3_chCircDom;

	private float phenotypeInteraction;
	private float epistasis;

	// for edX
	private EdXServerStrings edXServerStrings;


	public ProblemTypeSpecification() {
		beginnerMode = false;

		fieldPopTrueBreeding = false;

		minOffspring = 25;
		maxOffspring = 35;

		chZZ_ZW = 0.0f;

		gene1_chSexLinked = 0.0f;
		gene1_ch3Alleles = 0.0f;
		gene1_chIncDom = 0.0f;
		gene1_chCircDom = 0.5f;

		gene2_chPresent = 0.0f;
		gene2_chSameChrAsGene1 = 0.0f;
		gene2_minRfToGene1 = 0.0f;
		gene2_maxRfToGene1 = 0.0f;
		gene2_ch3Alleles = 0.0f;
		gene2_chIncDom = 0.0f;
		gene2_chCircDom = 0.5f;

		gene3_chPresent = 0.0f;
		gene3_chSameChrAsGene1 = 0.0f;
		gene3_minRfToPrevGene = 0.0f;
		gene3_maxRfToPrevGene = 0.0f;
		gene3_ch3Alleles = 0.0f;
		gene3_chIncDom = 0.0f;
		gene3_chCircDom = 0.5f;

		phenotypeInteraction = 0.0f;
		epistasis = 0.0f;

		// for edX server
		edXServerStrings = new EdXServerStrings();
	}

	public boolean isFieldPopTrueBreeding() {
		return fieldPopTrueBreeding;
	}

	public void setFieldPopTrueBreeding(boolean fieldPopTrueBreeding) {
		this.fieldPopTrueBreeding = fieldPopTrueBreeding;
	}

	public boolean isBeginnerMode() {
		return beginnerMode;
	}

	public void setBeginnerMode(boolean b) {
		beginnerMode = b;
	}

	public int getMinOffspring() {
		return minOffspring;
	}

	public void setMinOffspring(int minOffspring) {
		if (minOffspring > 100) {
			this.minOffspring = 100;
		} else {
			this.minOffspring = minOffspring;
		}
	}

	public int getMaxOffspring() {
		return maxOffspring;
	}

	public void setMaxOffspring(int maxOffspring) {
		if (maxOffspring > 100) {
			this.maxOffspring = 100;
		} else {
			this.maxOffspring = maxOffspring;
		}
	}

	public float getChZZ_ZW() {
		return chZZ_ZW;
	}

	public void setChZZ_ZW(float chZZ_ZW) {
		this.chZZ_ZW = chZZ_ZW;
	}

	public float getGene1_chSexLinked() {
		return gene1_chSexLinked;
	}

	public void setGene1_chSexLinked(float gene1_chSexLinked) {
		this.gene1_chSexLinked = gene1_chSexLinked;
	}

	public float getGene1_ch3Alleles() {
		return gene1_ch3Alleles;
	}

	public void setGene1_ch3Alleles(float gene1_ch3Alleles) {
		this.gene1_ch3Alleles = gene1_ch3Alleles;
	}

	public float getGene1_chIncDom() {
		return gene1_chIncDom;
	}

	public void setGene1_chIncDom(float gene1_chIncDom) {
		this.gene1_chIncDom = gene1_chIncDom;
	}

	public float getGene1_chCircDom() {
		return gene1_chCircDom;
	}

	public void setGene1_chCircDom(float gene1ChCircDom) {
		gene1_chCircDom = gene1ChCircDom;
	}

	public float getGene2_chPresent() {
		return gene2_chPresent;
	}

	public void setGene2_chPresent(float gene2_chPresent) {
		this.gene2_chPresent = gene2_chPresent;
	}

	public float getGene2_chSameChrAsGene1() {
		return gene2_chSameChrAsGene1;
	}

	public void setGene2_chSameChrAsGene1(float gene2_chSameChrAsGene1) {
		this.gene2_chSameChrAsGene1 = gene2_chSameChrAsGene1;
	}

	public float getGene2_minRfToGene1() {
		return gene2_minRfToGene1;
	}

	public void setGene2_minRfToGene1(float gene2_minRfToGene1) {
		this.gene2_minRfToGene1 = gene2_minRfToGene1;
	}

	public float getGene2_maxRfToGene1() {
		return gene2_maxRfToGene1;
	}

	public void setGene2_maxRfToGene1(float gene2_maxRfToGene1) {
		this.gene2_maxRfToGene1 = gene2_maxRfToGene1;
	}

	public float getGene2_ch3Alleles() {
		return gene2_ch3Alleles;
	}

	public void setGene2_ch3Alleles(float gene2_ch2Alleles) {
		this.gene2_ch3Alleles = gene2_ch2Alleles;
	}

	public float getGene2_chIncDom() {
		return gene2_chIncDom;
	}

	public void setGene2_chIncDom(float gene2_chIncDom) {
		this.gene2_chIncDom = gene2_chIncDom;
	}

	public float getGene2_chCircDom() {
		return gene2_chCircDom;
	}

	public void setGene2_chCircDom(float gene2ChCircDom) {
		gene2_chCircDom = gene2ChCircDom;
	}

	public float getGene3_chPresent() {
		return gene3_chPresent;
	}

	public void setGene3_chPresent(float gene3_chPresent) {
		this.gene3_chPresent = gene3_chPresent;
	}

	public float getGene3_chSameChrAsGene1() {
		return gene3_chSameChrAsGene1;
	}

	public void setGene3_chSameChrAsGene1(float gene3_chSameChrAsGene1) {
		this.gene3_chSameChrAsGene1 = gene3_chSameChrAsGene1;
	}

	public float getGene3_minRfToPrevGene() {
		return gene3_minRfToPrevGene;
	}

	public void setGene3_minRfToPrevGene(float gene3_minRfToPrevGene) {
		this.gene3_minRfToPrevGene = gene3_minRfToPrevGene;
	}

	public float getGene3_maxRfToPrevGene() {
		return gene3_maxRfToPrevGene;
	}

	public void setGene3_maxRfToPrevGene(float gene3_maxRfToPrevGene) {
		this.gene3_maxRfToPrevGene = gene3_maxRfToPrevGene;
	}

	public float getGene3_ch3Alleles() {
		return gene3_ch3Alleles;
	}

	public void setGene3_ch3Alleles(float gene3_ch2Alleles) {
		this.gene3_ch3Alleles = gene3_ch2Alleles;
	}

	public float getGene3_chIncDom() {
		return gene3_chIncDom;
	}

	public void setGene3_chIncDom(float gene3_chIncDom) {
		this.gene3_chIncDom = gene3_chIncDom;
	}

	public float getGene3_chCircDom() {
		return gene3_chCircDom;
	}

	public void setGene3_chCircDom(float gene3ChCircDom) {
		gene3_chCircDom = gene3ChCircDom;
	}

	public float getPhenotypeInteraction() {
		return phenotypeInteraction;
	}

	public void setPhenotypeInteraction(float phenotypeInteraction) {
		this.phenotypeInteraction = phenotypeInteraction;
	}

	public float getEpistasis() {
		return epistasis;
	}

	public void setEpistasis(float epistasis) {
		this.epistasis = epistasis;
	}

	public void setEdXCookieURL(String edXCookieURL) {
		this.edXServerStrings.setEdXCookieURL(edXCookieURL);
	}
	
	public void setEdXLoginURL(String edXLoginURL) {
		this.edXServerStrings.setEdXLoginURL(edXLoginURL);
	}
	
	public void setEdXSubmissionURL(String edXSubmissionURL) {
		this.edXServerStrings.setEdXSubmissionURL(edXSubmissionURL);
	}
	
	public void setEdXLocation(String edXLocation) {
		this.edXServerStrings.setEdXLocation(edXLocation);
	}
	
	public EdXServerStrings getEdXServerStrings() {
		// only return if all parts present
		if (
				(edXServerStrings.edXCookieURL == null) || 
				(edXServerStrings.edXLoginURL == null) || 
				(edXServerStrings.edXSubmissionURL == null) || 
				(edXServerStrings.edXLocation == null)
				) return null;
		
		return edXServerStrings;
	}

	public Element save() throws Exception {
		Element ptse = new Element("ProblemTypeSpecification");
		Element e = null;

		e = new Element("FieldPopTrueBreeding");
		e.setText(String.valueOf(fieldPopTrueBreeding));
		ptse.addContent(e);

		e = new Element("BeginnerMode");
		e.setText(String.valueOf(beginnerMode));
		ptse.addContent(e);

		e = new Element("MinOffspring");
		e.setText(String.valueOf(minOffspring));
		ptse.addContent(e);

		e = new Element("MaxOffspring");
		e.setText(String.valueOf(maxOffspring));
		ptse.addContent(e);

		e = new Element("ZZ_ZW");
		e.setText(String.valueOf(chZZ_ZW));
		ptse.addContent(e);

		e = new Element("Gene1_SexLinked");
		e.setText(String.valueOf(gene1_chSexLinked));
		ptse.addContent(e);

		e = new Element("Gene1_3Alleles");
		e.setText(String.valueOf(gene1_ch3Alleles));
		ptse.addContent(e);

		e = new Element("Gene1_IncDom");
		e.setText(String.valueOf(gene1_chIncDom));
		ptse.addContent(e);

		e = new Element("Gene1_CircDom");
		e.setText(String.valueOf(gene1_chCircDom));
		ptse.addContent(e);

		e = new Element("Gene2_Present");
		e.setText(String.valueOf(gene2_chPresent));
		ptse.addContent(e);

		e = new Element("Gene2_SameChrAsGene1");
		e.setText(String.valueOf(gene2_chSameChrAsGene1));
		ptse.addContent(e);

		e = new Element("Gene2_MinRfToGene1");
		e.setText(String.valueOf(gene2_minRfToGene1));
		ptse.addContent(e);

		e = new Element("Gene2_MaxRfToGene1");
		e.setText(String.valueOf(gene2_maxRfToGene1));
		ptse.addContent(e);

		e = new Element("Gene2_3Alleles");
		e.setText(String.valueOf(gene2_ch3Alleles));
		ptse.addContent(e);

		e = new Element("Gene2_IncDom");
		e.setText(String.valueOf(gene2_chIncDom));
		ptse.addContent(e);

		e = new Element("Gene2_CircDom");
		e.setText(String.valueOf(gene2_chCircDom));
		ptse.addContent(e);

		e = new Element("Gene3_Present");
		e.setText(String.valueOf(gene3_chPresent));
		ptse.addContent(e);

		e = new Element("Gene3_SameChrAsGene1");
		e.setText(String.valueOf(gene3_chSameChrAsGene1));
		ptse.addContent(e);

		e = new Element("Gene3_MinRfToPrevGene");
		e.setText(String.valueOf(gene3_minRfToPrevGene));
		ptse.addContent(e);

		e = new Element("Gene3_MaxRfToPrevGene");
		e.setText(String.valueOf(gene3_maxRfToPrevGene));
		ptse.addContent(e);

		e = new Element("Gene3_3Alleles");
		e.setText(String.valueOf(gene3_ch3Alleles));
		ptse.addContent(e);

		e = new Element("Gene3_IncDom");
		e.setText(String.valueOf(gene3_chIncDom));
		ptse.addContent(e);

		e = new Element("Gene3_CircDom");
		e.setText(String.valueOf(gene3_chCircDom));
		ptse.addContent(e);

		e = new Element("PhenotypeInteraction");
		e.setText(String.valueOf(phenotypeInteraction));
		ptse.addContent(e);

		e = new Element("Epistasis");
		e.setText(String.valueOf(epistasis));
		ptse.addContent(e);
		
		e = new Element("edXCookieURL");
		e.setText(edXServerStrings.edXCookieURL);
		ptse.addContent(e);
		
		e = new Element("edXLoginURL");
		e.setText(edXServerStrings.edXLoginURL);
		ptse.addContent(e);
		
		e = new Element("edXSubmissionURL");
		e.setText(edXServerStrings.edXSubmissionURL);
		ptse.addContent(e);
		
		e = new Element("edXLocation");
		e.setText(edXServerStrings.edXLocation);
		ptse.addContent(e);

		return ptse;
	}

	public String toString() {
		Element root = new Element("ProblemSpec");
		try {
			root.addContent(save());
		} catch (Exception e) {
			e.printStackTrace();
		}
		Document doc = new Document(root);
		XMLOutputter outputter = 
				new XMLOutputter(Format.getPrettyFormat());
		return outputter.outputString(doc);
	}

}
