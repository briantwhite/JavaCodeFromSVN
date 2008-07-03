package PhenotypeImages;

import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.ImageIcon;

import GeneticModels.Phenotype;

public class PhenotypeImageBank {
	
	private static PhenotypeImageBank instance;
	
	private HashMap<String, ImageIcon> bank;
	
	private PhenotypeImageMaker imageMaker;
	
	private PhenotypeImageBank() {
		bank = new HashMap<String, ImageIcon>();
		imageMaker = new PhenotypeImageMaker();
	}
	
	public static PhenotypeImageBank getInstance() {
		if (instance == null) {
			instance = new PhenotypeImageBank();
		}
		return instance;
	}
	
	public void resetDefaults() {
		imageMaker = new PhenotypeImageMaker();
	}
	
	public ImageIcon getImageForPhenotype(
			ArrayList<Phenotype> phenotypes, 
			String phenoString) {
		if (!bank.containsKey(phenoString)) {
			bank.put(phenoString, imageMaker.makeImage(phenotypes));
		}
		return bank.get(phenoString);
	}
	
}
