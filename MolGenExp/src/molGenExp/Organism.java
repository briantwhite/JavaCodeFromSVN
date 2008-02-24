package molGenExp;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import javax.swing.ImageIcon;

import utilities.ExpressedGene;
import utilities.GeneExpresser;
import utilities.GlobalDefaults;
import biochem.FoldedPolypeptide;
import evolution.ThinOrganism;

public class Organism {
	
	private static int imageSize = 50; //size of image for greenhouse
	private String name;
	
	private ExpressedAndFoldedGene gene1;
	private ExpressedAndFoldedGene gene2;
	private Color color;
	private ImageIcon image;
	
	private static HashMap iconCache = new HashMap();
	
	private GeneExpresser geneExpresser;

	public Organism(String name, ExpressedAndFoldedGene gene1, ExpressedAndFoldedGene gene2) {
		this.name = name;
		this.gene1 = gene1;
		this.gene2 = gene2;
		
		//calculate color
		color = GlobalDefaults.colorModel.mixTwoColors(
				gene1.getFoldedPolypeptide().getColor(), 
				gene2.getFoldedPolypeptide().getColor());
		
		//generate icon
		// see if we've cached one yet
		if (iconCache.containsKey(color.toString())) {
			image = (ImageIcon)iconCache.get(color.toString());
		} else {
			image = makeIcon(color);
			iconCache.put(color.toString(), image);
		}
		
		geneExpresser = GeneExpresser.getInstance();

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
	
	
	// constructor for making new organism from old organism
	//  with name changed
	public Organism (String name, Organism o) {
		this(name, o.getGene1(), o.getGene2());
	}
	
	public Organism(ThinOrganism thinOrg) {
		ExpressedGene eg1 = geneExpresser.expressGene(thinOrg.getDNA1(), -1);
		FoldedPolypeptide fp1 = GreenhouseLoader.foldProtein(eg1.getProtein());
		gene1 = new ExpressedAndFoldedGene(eg1, fp1);

		ExpressedGene eg2 = geneExpresser.expressGene(thinOrg.getDNA2(), -1);
		FoldedPolypeptide fp2 = GreenhouseLoader.foldProtein(eg2.getProtein());
		gene2 = new ExpressedAndFoldedGene(eg2, fp2);
		
		color = thinOrg.getColor();
	}
	
	public String getName() {
		return name;
	}

	public ExpressedAndFoldedGene getGene1() {
		return gene1;
	}

	public ExpressedAndFoldedGene getGene2() {
		return gene2;
	}

	public Color getColor() {
		return color;
	}
	
	public ImageIcon getImage() {
		return image;
	}
	
	public void setName(String name) {
		this.name = name;
	}
}
