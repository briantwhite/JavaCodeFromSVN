package edu.umb.jsAipotu.molGenExp;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import javax.swing.ImageIcon;

import molBiol.ExpressedGene;
import molBiol.GeneExpresser;
import preferences.GlobalDefaults;
import biochem.FoldedProteinWithImages;
import biochem.FoldingException;
import biochem.FoldingManager;
import evolution.ThinOrganism;

public class OrganismFactory {
	
	private static int imageSize = 50; //size of image for greenhouse
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
		Color color = GlobalDefaults.colorModel.mixTwoColors(
				gene1.getFoldedProteinWithImages().getColor(), 
				gene2.getFoldedProteinWithImages().getColor());
		
		//generate icon
		// see if we've cached one yet
		ImageIcon image = null;
		if (iconCache.containsKey(color.toString())) {
			image = (ImageIcon)iconCache.get(color.toString());
		} else {
			image = makeIcon(color);
			iconCache.put(color.toString(), image);
		}
		return new Organism(name, gene1, gene2, color, image);
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

	
	
	private ImageIcon makeIcon(Color color) {
		BufferedImage pic = new BufferedImage(
				imageSize,
				imageSize,
				BufferedImage.TYPE_INT_RGB);
		Graphics2D g = pic.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
				RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, 0, 50, 50);
		g.setColor(color);
		int[] xPoints = {0, 22, 25, 28, 50,
				36, 50, 36, 50, 28,
				25, 22, 0, 14, 0, 14};
		int[] yPoints = {0, 14, 0, 14, 0,
				22, 25, 28, 50, 36,
				50, 36, 50, 28, 25, 22};
		int nPoints = xPoints.length;
		g.fill(new Polygon(xPoints, yPoints, nPoints));
		g.setColor(Color.LIGHT_GRAY);
		g.drawLine(25, 25, 22, 14);
		g.drawLine(25, 25, 28, 14);
		g.drawLine(25, 25, 36, 22);
		g.drawLine(25, 25, 36, 28);		
		g.drawLine(25, 25, 28, 36);
		g.drawLine(25, 25, 22, 36);
		g.drawLine(25, 25, 14, 28);
		g.drawLine(25, 25, 14, 22);		

		g.dispose();
		pic.flush();
		return new ImageIcon(pic);
	}

}
