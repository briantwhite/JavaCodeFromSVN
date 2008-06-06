package evolution;

import java.awt.Color;

import molGenExp.Organism;

import biochem.FoldingException;
import biochem.FoldingManager;
import biochem.HexCanvas;
import biochem.HexGrid;
import utilities.ExpressedGene;
import utilities.GeneExpresser;
import utilities.GlobalDefaults;

public class ThinOrganismFactory {
	
	private GeneExpresser geneExpresser;
	private FoldingManager foldingManager;
	
	public ThinOrganismFactory() {
		geneExpresser = new GeneExpresser();
		foldingManager = new FoldingManager();
	}
	
	public synchronized ThinOrganism createThinOrganism(String dna1, String dna2, Color overallColor) {
		String newDNA1 = "";
		if (dna1 != null) {
			newDNA1 = dna1;
		} 
		String newDNA2 = "";
		if (dna2 != null) {
			newDNA2 = dna2;
		} 
		Color color1 = foldAndComputeColor(dna1);
		Color color2 = foldAndComputeColor(dna2);

		return new ThinOrganism(newDNA1, newDNA2, 
				color1, color2, 
				overallColor);
	}
	
	public synchronized ThinOrganism createThinOrganism(String dna1, String dna2) {
		String newDNA1 = "";
		if (dna1 != null) {
			newDNA1 = dna1;
		} 
		String newDNA2 = "";
		if (dna2 != null) {
			newDNA2 = dna2;
		} 
		Color color1 = foldAndComputeColor(dna1);
		Color color2 = foldAndComputeColor(dna2);
	
		return new ThinOrganism(newDNA1, newDNA2, 
				color1, color2, 
				GlobalDefaults.colorModel.mixTwoColors(color1, color2));
	}
	
	public synchronized ThinOrganism createThinOrganism(Organism o) {
		return new ThinOrganism(o.getGene1().getExpressedGene().getDNA(),
		o.getGene2().getExpressedGene().getDNA(),
		o.getGene1().getFoldedPolypeptide().getColor(),
		o.getGene2().getFoldedPolypeptide().getColor(),
		o.getColor());
	}
	
	private Color foldAndComputeColor(String DNASeq) {
		ExpressedGene newGene = geneExpresser.expressGene(DNASeq, -1);
		String proteinSequence = newGene.getProtein();
		String aaSeq = "";

		//convert to single-letter code amino acid seq
		if (proteinSequence.equals("")) {
			aaSeq = "";
		} else {
			//insert spaces between amino acid codes
			StringBuffer psBuffer = new StringBuffer(proteinSequence);
			for (int i = 3; i < psBuffer.length(); i = i + 4) {
				psBuffer = psBuffer.insert(i, " ");
			}
			proteinSequence = psBuffer.toString();

			String[] aminoAcidStrings = proteinSequence.split(" ");
			StringBuffer buf = new StringBuffer();
			for (int i = 0; i < aminoAcidStrings.length; i++) {
				buf.append(GlobalDefaults.aaTable.get(aminoAcidStrings[i]).getAbName());
			}

			aaSeq = buf.toString();
		}
		HexGrid grid = foldOntoHexGrid(aaSeq);
		HexCanvas canvas = new HexCanvas();
		canvas.setGrid(grid);
		GlobalDefaults.colorModel.categorizeAcids(grid);
		return grid.getProteinColor();
	}

	private  HexGrid foldOntoHexGrid(String aaSeq) {
		try {
			foldingManager.fold(aaSeq);
		} catch (FoldingException e) {
			e.printStackTrace();
		}
		return (HexGrid)foldingManager.getGrid();
	}


}