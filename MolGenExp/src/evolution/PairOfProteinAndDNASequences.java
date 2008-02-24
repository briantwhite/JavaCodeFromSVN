package evolution;

public class PairOfProteinAndDNASequences {
	private String DNA1;
	private String protein1;
	private String DNA2;
	private String protein2;
	
	public PairOfProteinAndDNASequences(
			String DNA1, 
			String protein1, 
			String DNA2, 
			String protein2) {
		this.DNA1 = DNA1;
		this.protein1 = protein1;
		this.DNA2 = DNA2;
		this.protein2 = protein2;
	}

	public String getDNA1() {
		return DNA1;
	}
	
	public String getProtein1() {
		return protein1;
	}

	public String getDNA2() {
		return DNA2;
	}
	
	public String getProtein2() {
		return protein2;
	}

}
