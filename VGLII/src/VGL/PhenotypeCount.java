package VGL;

public class PhenotypeCount {
	
	private String phenotype;
	private MFTotCounts counts;
	
	public PhenotypeCount(String phenotype, MFTotCounts counts) {
		this.phenotype = phenotype;
		this.counts = counts;
	}

	public String getPhenotype() {
		return phenotype;
	}

	public MFTotCounts getCounts() {
		return counts;
	}

}
