package evolution;

import java.awt.Color;
import java.util.Random;

import molGenExp.MolGenExp;
import molGenExp.Organism;
import biochem.Attributes;
import biochem.FoldingException;
import biochem.FoldingManager;
import biochem.HexCanvas;
import biochem.HexGrid;

// class with just DNA and color - so it's smaller
//  for use with evolution
//  don't bother with pictures unless needed
public class ThinOrganism {
	private String dna1;
	private String dna2;
	private Color color;
	
	//empty organism for testing purposes
	public ThinOrganism(Color color) {
		dna1 = "";
		dna2 = "";
		this.color = color;
	}
	
	public ThinOrganism(String dna1, String dna2) {
		color = MolGenExp.colorModel.mixTwoColors(
				foldAndComputeColor(dna1), 
				foldAndComputeColor(dna2));
	}
	
	public ThinOrganism(Organism o) {
		dna1 = o.getGene1().getGene().getDNASequence();
		dna2 = o.getGene2().getGene().getDNASequence();
		color = o.getColor();
	}
	
	public Color getColor() {
		return color;
	}
	
	public String getRandomDNASequence() {
		Random r = new Random();
		if (r.nextDouble() > 0.5) {
			return dna1;
		} else {
			return dna2;
		}
	}
	
	private Color foldAndComputeColor(String aaSeq) {
			HexGrid grid = foldOntoHexGrid(aaSeq);
			HexCanvas canvas = new HexCanvas();
			canvas.setGrid(grid);
			MolGenExp.colorModel.categorizeAcids(grid);
			return grid.getProteinColor();
	}
	
	public HexGrid foldOntoHexGrid(String aaSeq) {
		FoldingManager manager = FoldingManager.getInstance();
		String ssBondIndex = "0.0";
		Attributes attributes = new Attributes(aaSeq.trim(), 
				1,  "straight");
		try {
			manager.fold(attributes);
		} catch (FoldingException e) {
			e.printStackTrace();
		}
		return (HexGrid)manager.getGrid();
	}


}
