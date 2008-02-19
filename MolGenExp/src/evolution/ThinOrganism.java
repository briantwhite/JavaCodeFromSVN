package evolution;

import java.awt.Color;
import java.util.Random;

import molBiol.Gene;
import molGenExp.MolGenExp;
import molGenExp.Organism;
import biochem.Attributes;
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
	private Color color;

	//empty organism for testing purposes
	public ThinOrganism(Color color) {
		dna1 = "";
		dna2 = "";
		this.color = color;
	}

	public ThinOrganism(String dna1, String dna2) {
		this.dna1 = dna1;
		this.dna2 = dna2;
		color = MolGenExp.colorModel.mixTwoColors(
				foldAndComputeColor(dna1), 
				foldAndComputeColor(dna2));
	}

	public ThinOrganism(Organism o) {
		dna1 = o.getGene1().getGene().getDNASequence();
		dna2 = o.getGene2().getGene().getDNASequence();
		color = o.getColor();
	}
	
	public String getDNA1() {
		return dna1;
	}
	
	public String getDNA2() {
		return dna2;
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

	private Color foldAndComputeColor(String DNASeq) {
		Gene newGene = 
			new Gene(DNASeq, MolGenExp.molBiolParams);
		newGene.transcribe();
		newGene.process();
		newGene.translate();
		newGene.generateHTML(0);
		String proteinSequence = newGene.getProteinString();
		String aaSeq = "";

		//convert to single-letter code amino acid seq
		if (proteinSequence.indexOf("none") != -1) {
			aaSeq = "";
		} else {
			//remove leading/trailing spaces and the N- and C-
			proteinSequence = 
				proteinSequence.replaceAll(" ", "");
			proteinSequence = 
				proteinSequence.replaceAll("N-", "");
			proteinSequence = 
				proteinSequence.replaceAll("-C", "");

			//insert spaces between amino acid codes
			StringBuffer psBuffer = new StringBuffer(proteinSequence);
			for (int i = 3; i < psBuffer.length(); i = i + 4) {
				psBuffer = psBuffer.insert(i, " ");
			}
			proteinSequence = psBuffer.toString();

			String[] aminoAcidStrings = proteinSequence.split(" ");
			StringBuffer buf = new StringBuffer();
			for (int i = 0; i < aminoAcidStrings.length; i++) {
				buf.append(MolGenExp.aaTable.get(aminoAcidStrings[i]).getAbName());
			}

			aaSeq = buf.toString();
		}
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
