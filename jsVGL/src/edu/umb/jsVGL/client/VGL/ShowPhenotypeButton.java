package edu.umb.jsVGL.client.VGL;

import java.net.URL;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import GeneticModels.Phenotype;

public class ShowPhenotypeButton extends JButton {

	private ArrayList<Phenotype> phenotypes;
	private String phenotypeString;

	private static URL showPhenoImageURL = 
		CageUI.class.getResource("UIimages/ShowPheno.gif");
	private static ImageIcon showPhenoImage = 
		new ImageIcon(showPhenoImageURL);

	public ShowPhenotypeButton(ArrayList<Phenotype> phenotypes, String phenotypeString) {
		super(showPhenoImage);
		this.phenotypes = phenotypes;
		this.phenotypeString = phenotypeString;
	}
	
	public ArrayList<Phenotype> getPhenotypes() {
		return phenotypes;
	}
	
	public String getPhenotypeString() {
		return phenotypeString;
	}

}
