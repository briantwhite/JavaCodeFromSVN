package molGenExp;

import utilities.ExpressedGene;
import biochem.FoldedPolypeptide;

public class ExpressedAndFoldedGene {
	private ExpressedGene eg;
	private FoldedPolypeptide fp;
	
	public ExpressedAndFoldedGene(ExpressedGene eg, FoldedPolypeptide fp) {
		this.eg = eg;
		this.fp = fp;
	}
	
	public ExpressedGene getExpressedGene() {
		return eg;
	}
	
	public FoldedPolypeptide getFoldedPolypeptide() {
		return fp;
	}
	

}
