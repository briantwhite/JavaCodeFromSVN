package molGenExp;

import molBiol.ExpressedGene;
import biochem.FoldedProteinWithImages;

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
