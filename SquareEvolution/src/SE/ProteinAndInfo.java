package SE;

/*
 * used by RandomProteinFitness as a data carrier
 */
class ProteinAndInfo {
	private String protein;
	private String conformation = "None";
	private double dGf = 0.0f;
	private double dGb = 0.0f;
	private int ligRot = 0;
	private int ligX = 0;
	private int ligY = 0;
	private double fitness = 0.0f;

	ProteinAndInfo(String protein, 
			String conformation,
			double dGf,
			double dGb,
			int ligRot,
			int ligX,
			int ligY,
			double fitness) {
		this.protein = protein;
		this.conformation = conformation;
		this.dGf = dGf;
		this.dGb = dGb;
		this.ligRot = ligRot;
		this.ligX = ligX;
		this.ligY = ligY;
		this.fitness = fitness;
	}
	
	/*
	 * constructor for dummy entries with no structure
	 */
	ProteinAndInfo(String protein, double fitness) {
		this.protein = protein;
		this.fitness = fitness;
	}

	public String toString() {
		return protein + "," 
				+ conformation + ","
				+ String.valueOf(dGf) + ","
				+ String.valueOf(dGb) + ","
				+ String.valueOf(ligRot) + ","
				+ String.valueOf(ligX) + ","
				+ String.valueOf(ligY) + ","
				+ String.valueOf(fitness);
	}
}
