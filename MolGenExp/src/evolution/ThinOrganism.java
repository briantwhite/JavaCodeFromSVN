package evolution;

import java.awt.Color;
import java.util.Random;

import molGenExp.Organism;
import utilities.ExpressedGene;
import utilities.GeneExpresser;
import utilities.GlobalDefaults;
import utilities.ProteinUtilities;
import biochem.BiochemAttributes;
import biochem.FoldingException;
import biochem.FoldingManager;
import biochem.HexCanvas;
import biochem.HexGrid;

//class with just DNA and color - so it's smaller
//for use with evolution
//don't bother with pictures unless needed
public class ThinOrganism {
	private String dna1;
	private String dna2;
	private Color color1;
	private Color color2;
	private Color overallColor;

	public ThinOrganism(String dna1, String dna2, Color overallColor) {
		if (dna1 == null) {
			this.dna1 = "";
		} else {
			this.dna1 = dna1;
		}

		if (dna2 == null) {
			this.dna2 = "";
		} else {
			this.dna2 = dna2;
		}

		this.overallColor = overallColor;
		color1 = foldAndComputeColor(dna1);
		color2 = foldAndComputeColor(dna2);
	}


	public ThinOrganism(String dna1, String dna2) {
		this.dna1 = dna1;
		this.dna2 = dna2;
		color1 = foldAndComputeColor(dna1);
		color2 = foldAndComputeColor(dna2);		
		overallColor = GlobalDefaults.colorModel.mixTwoColors(color1, color2);
	}

	public ThinOrganism(Organism o) {
		dna1 = o.getGene1().getExpressedGene().getDNA();
		dna2 = o.getGene2().getExpressedGene().getDNA();
		color1 = o.getGene1().getFoldedPolypeptide().getColor();
		color2 = o.getGene2().getFoldedPolypeptide().getColor();
		overallColor = o.getColor();
	}

	public String getDNA1() {
		return dna1;
	}

	public String getDNA2() {
		return dna2;
	}
	
	public Color getColor1() {
		return color1;
	}
	
	public Color getColor2() {
		return color2;
	}

	public Color getOverallColor() {
		return overallColor;
	}

	public String getRandomDNASequence() {
		Random r = new Random();
		if (r.nextDouble() > 0.5) {
			return dna1;
		} else {
			return dna2;
		}
	}
	
	public Color foldAndComputeColor(String DNASeq) {
		ExpressedGene newGene = GeneExpresser.getInstance().expressGene(DNASeq, -1);
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
		FoldingManager manager = FoldingManager.getInstance();
		try {
			manager.fold(aaSeq);
		} catch (FoldingException e) {
			e.printStackTrace();
		}
		return (HexGrid)manager.getGrid();
	}


}
