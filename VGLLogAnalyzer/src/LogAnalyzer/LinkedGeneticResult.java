package LogAnalyzer;

public class LinkedGeneticResult extends GeneticResult {
	
	private GeneticResult resultA;
	private GeneticResult resultB;
	
	public LinkedGeneticResult(GeneticResult resultA, GeneticResult resultB) {
		this.resultA = resultA;
		this.resultB = resultB;
	}

	public GeneticResult getResultA() {
		return resultA;
	}

	public GeneticResult getResultB() {
		return resultB;
	}

	public boolean equals(Object o) {
		LinkedGeneticResult lgr = (LinkedGeneticResult)o;
		if (lgr.getResultA().equals(resultA) 
				&& lgr.getResultB().equals(resultB)) {
			return true;
		}
		return false;
	}
	
	public String toString() {
		return new String("LinkedResult: " + resultA.toString() + "*" + resultB.toString());
	}
	
	public int hashCode() {
		return resultA.hashCode() ^ resultB.hashCode();
	}
}
