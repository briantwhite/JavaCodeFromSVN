package edu.umb.jsAipotu.molGenExp;

import edu.umb.jsAipotu.biochem.FoldedProteinWithImages;
import edu.umb.jsAipotu.molBiol.ExpressedGene;

public class ExpressedAndFoldedGene {
	private ExpressedGene eg;
	private FoldedProteinWithImages fp;
	
	public ExpressedAndFoldedGene(ExpressedGene eg, FoldedProteinWithImages fp) {
		this.eg = eg;
		this.fp = fp;
	}
	
	public ExpressedGene getExpressedGene() {
		return eg;
	}
	
	public FoldedProteinWithImages getFoldedProteinWithImages() {
		return fp;
	}
	

}
