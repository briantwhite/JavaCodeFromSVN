package SE;

public class ProteinDatabaseEntry {
	String proteinSequence;
	String proteinStructure;
	String ligandSequence;
	String ligandStructure;
	int bestRotamer;
	int bestLigX;
	int bestLigY;
	double dGf;    	// dG of folding
	double dGb;		// dG of binding
	double fitness;
	
	// complete constructor
	public ProteinDatabaseEntry(
			Configuration config,
			String proteinSequence,
			String proteinStructure,
			String ligandSequence,
			String ligandStructure,
			int bestRotamer,
			int bestLigX,
			int bestLigY,
			double dGf,
			double dGb,
			double fitness) {
		this.proteinSequence = proteinSequence;
		this.proteinStructure = proteinStructure;
		this.ligandSequence = ligandSequence;
		this.ligandStructure = ligandStructure;
		this.bestRotamer = bestRotamer;
		this.bestLigX = bestLigX;
		this.bestLigY = bestLigY;
		this.dGf = dGf;
		this.dGb = dGb;
		this.fitness = config.getNeutrality() + (1 - config.getNeutrality()) * fitness;
	}

	// default fitness calculation = fract folded x fract bound
	public ProteinDatabaseEntry(
			Configuration config,
			String proteinSequence,
			String proteinStructure,
			String ligandSequence,
			String ligandStructure,
			int bestRotamer,
			int bestLigX,
			int bestLigY,
			double dGf,
			double dGb) {
		this.proteinSequence = proteinSequence;
		this.proteinStructure = proteinStructure;
		this.ligandSequence = ligandSequence;
		this.ligandStructure = ligandStructure;
		this.bestRotamer = bestRotamer;
		this.bestLigX = bestLigX;
		this.bestLigY = bestLigY;
		this.dGf = dGf;
		this.dGb = dGb;
		
		double proteinFitness;
		if (proteinStructure.equals("None")) {
			if (proteinSequence.equals("")) {
				proteinFitness = SquareEvolution.NO_PROTEIN_FITNESS;
			} else {
				proteinFitness = SquareEvolution.NO_STRUCTURE_FITNESS;
			}
		} else {
			double Kf = Math.exp(-dGf);
			double Kb = Math.exp(-dGb);
			proteinFitness = (Kf/(1 + Kf)) * (Kb/(1 + Kb));
		}
		/*
		 * see discussion of neutrality in "Square Evolution Log 02.docx" pages 14, 17, 25, 26
		 * see updates in "Square Evolution Log 06.docx" page 24
		 */
		fitness = config.getNeutrality() + (1 - config.getNeutrality()) * proteinFitness;
	}
	
	public String toString() {
		StringBuffer b = new StringBuffer();
		b.append("Protein Database Entry: " + proteinSequence + "\n");
		b.append(DisplayStructure.getStructure(
				proteinSequence, 
				proteinStructure, 
				ligandSequence, 
				ligandStructure, 
				bestRotamer, 
				bestLigX, 
				bestLigY) + "\n");
		b.append(String.format(
				"dGf= %.3f dGb= %.3f fitness= %.3f \n", dGf, dGb, fitness));
		b.append("***************\n");
		return b.toString();
	}

}
