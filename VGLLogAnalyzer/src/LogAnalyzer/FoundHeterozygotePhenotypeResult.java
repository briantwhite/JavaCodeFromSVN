package LogAnalyzer;

public class FoundHeterozygotePhenotypeResult extends GeneticResult {
	
	private boolean male;
	
	public FoundHeterozygotePhenotypeResult(boolean male) {
		this.male = male;
	}
	
	public boolean isMale() {
		return male;
	}

	public boolean equals(Object o) {
		if (o instanceof FoundHeterozygotePhenotypeResult) {
			FoundHeterozygotePhenotypeResult fhpr = (FoundHeterozygotePhenotypeResult)o;
			if (fhpr.isMale() == male) return true;
		}
		return false;
	}

	public int hashCode() {
		if (male) return 1;
		return 0;
	}

	public String toString() {
		return new String("Found heterozygote:" + male);
	}

}
