package edu.umb.jsAipotu.client.molBiol;

import edu.umb.jsAipotu.client.biochem.FoldedProteinWithImages;
import edu.umb.jsAipotu.client.molGenExp.ExpressedAndFoldedGene;
import edu.umb.jsAipotu.client.molGenExp.HistListItem;

public class ExpressedGeneWithImages extends HistListItem {
	private ExpressedAndFoldedGene efg;
	private FoldedProteinWithImages fp;
	private String htmlString;
	private String currentDNA;
	
	public ExpressedGeneWithImages(ExpressedAndFoldedGene efg) { 
		this.efg = efg;
		this.fp = efg.getFoldedProteinWithImages();
		htmlString = efg.getExpressedGene().getHtmlString();
		currentDNA = efg.getExpressedGene().getDNA();
		toolTipText = efg.getExpressedGene().getProtein();
	}
	
	public ExpressedAndFoldedGene getEFG() {
		return efg;
	}
	
	public String getHTML() {
		return htmlString;
	}
		
	public FoldedProteinWithImages getFoldedProteinWithImages() {
		return fp;
	}

	public String getToolTipText() {
		return toolTipText;
	}

	public void setToolTipText(String text) {
		toolTipText = text;
	}
	
	public String getDNA() {
		return currentDNA;
	}
	
}
