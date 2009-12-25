

public class Gene {
	
	private int allele;
	
	public Gene(int startingAllele) {
		allele = startingAllele;
	}
	
	public int getAllele() {
		return allele;
	}
	
	public void mutate() {
		if (allele == 0) {
			allele = 1;
		} else {
			allele = 0;
		}
	}
	

}
