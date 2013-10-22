package SE;

/*
 * used by RandomProteinFitness as a data carrier
 */
class ProteinAndInfo {
	private String protein;
	private String conformation = "none";
	private double dGf = Double.MIN_VALUE;
	private double dGb = Double.MIN_VALUE;
	private int ligRot = -1;
	private int ligX = Integer.MIN_VALUE;
	private int ligY = Integer.MIN_VALUE;
	private double fitness = Double.MIN_VALUE;

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
