package edu.umb.jsAipotu.client.evolution;

import com.google.gwt.canvas.dom.client.CssColor;

import edu.umb.jsAipotu.client.biochem.FoldingException;
import edu.umb.jsAipotu.client.biochem.FoldingManager;
import edu.umb.jsAipotu.client.molBiol.ExpressedGene;
import edu.umb.jsAipotu.client.molBiol.GeneExpresser;
import edu.umb.jsAipotu.client.molGenExp.MolGenExp;
import edu.umb.jsAipotu.client.molGenExp.Organism;
import edu.umb.jsAipotu.client.preferences.GlobalDefaults;

public class ThinOrganismFactory {
	private GeneExpresser geneExpresser;
	private FoldingManager foldingManager;
	private MolGenExp mge;

	public ThinOrganismFactory(MolGenExp mge) {
		geneExpresser = new GeneExpresser();
		foldingManager = new FoldingManager();
		this.mge = mge;
	}

	public synchronized ThinOrganism createThinOrganism(
			String dna1, String dna2, CssColor overallColor) 
	throws FoldingException {
		String newDNA1 = "";
		if (dna1 != null) {
			newDNA1 = dna1;
		} 
		String newDNA2 = "";
		if (dna2 != null) {
			newDNA2 = dna2;
		} 
		CssColor color1 = foldAndComputeColor(dna1);
		CssColor color2 = foldAndComputeColor(dna2);

		return new ThinOrganism(newDNA1, newDNA2, 
				color1, color2, 
				overallColor, mge);
	}

	public synchronized ThinOrganism createThinOrganism(String dna1, String dna2) 
	throws FoldingException {
		String newDNA1 = "";
		if (dna1 != null) {
			newDNA1 = dna1;
		} 
		String newDNA2 = "";
		if (dna2 != null) {
			newDNA2 = dna2;
		} 
		CssColor color1 = foldAndComputeColor(dna1);
		CssColor color2 = foldAndComputeColor(dna2);

		return new ThinOrganism(newDNA1, newDNA2, 
				color1, color2, 
				GlobalDefaults.colorModel.mixTwoColors(color1, color2), mge);
	}

	public synchronized ThinOrganism createThinOrganism(Organism o) {
		return new ThinOrganism(o.getGene1().getExpressedGene().getDNA(),
				o.getGene2().getExpressedGene().getDNA(),
				o.getGene1().getFoldedProteinWithImages().getColor(),
				o.getGene2().getFoldedProteinWithImages().getColor(),
				o.getColor(), mge);
	}

	private CssColor foldAndComputeColor(String DNASeq) throws FoldingException {
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
		return foldingManager.foldAndColor(aaSeq).getColor();
	}



}
