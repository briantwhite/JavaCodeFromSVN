package edu.umb.jsAipotu.client.molGenExp;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import javax.swing.ImageIcon;

import com.google.gwt.canvas.dom.client.CssColor;

import edu.umb.jsAipotu.client.biochem.FoldedProteinWithImages;
import edu.umb.jsAipotu.client.biochem.FoldingException;
import edu.umb.jsAipotu.client.biochem.FoldingManager;
import edu.umb.jsAipotu.client.evolution.ThinOrganism;
import edu.umb.jsAipotu.client.molBiol.ExpressedGene;
import edu.umb.jsAipotu.client.molBiol.GeneExpresser;
import edu.umb.jsAipotu.client.preferences.GlobalDefaults;

public class OrganismFactory {
	
	private static HashMap<String, ImageIcon> iconCache;
	
	private GeneExpresser geneExpresser;
	private FoldingManager foldingManager;
	
	public OrganismFactory() {
		geneExpresser = new GeneExpresser();
		foldingManager = new FoldingManager();
		iconCache = new HashMap<String, ImageIcon>();
	}
	
	// method for making new organism from old organism
	//  with name changed
	public Organism createOrganism(String name, Organism o) {
		return createOrganism(name, o.getGene1(), o.getGene2());
	}
	
	public Organism createOrganism(String name, String DNA1, String DNA2) 
	throws FoldingException {

		ExpressedGene eg1 = geneExpresser.expressGene(DNA1, -1);
		FoldedProteinWithImages fp1 = foldingManager.foldWithPix(eg1.getProtein());

		ExpressedGene eg2 = geneExpresser.expressGene(DNA2, -1);
		FoldedProteinWithImages fp2 = foldingManager.foldWithPix(eg2.getProtein());
		
		return createOrganism(name,
				new ExpressedAndFoldedGene(eg1, fp1),
				new ExpressedAndFoldedGene(eg2, fp2));
	}

	public Organism createOrganism(
			String name, 
			ExpressedAndFoldedGene gene1, 
			ExpressedAndFoldedGene gene2) {
		
		//calculate color
		CssColor color = GlobalDefaults.colorModel.mixTwoColors(
				gene1.getFoldedProteinWithImages().getColor(), 
				gene2.getFoldedProteinWithImages().getColor());
		
		String imageFileName = GlobalDefaults.colorModel.getImageFileNameFromColor(color);
		return new Organism(name, gene1, gene2, color, imageFileName);
	}
	
	public Organism createOrganism(ThinOrganism thinOrg) throws FoldingException {

		ExpressedGene eg1 = geneExpresser.expressGene(thinOrg.getDNA1(), -1);
		FoldedProteinWithImages fp1 = foldingManager.foldWithPix(eg1.getProtein());

		ExpressedGene eg2 = geneExpresser.expressGene(thinOrg.getDNA2(), -1);
		FoldedProteinWithImages fp2 = foldingManager.foldWithPix(eg2.getProtein());
		
		return createOrganism("",
				new ExpressedAndFoldedGene(eg1, fp1),
				new ExpressedAndFoldedGene(eg2, fp2));
	}
}
