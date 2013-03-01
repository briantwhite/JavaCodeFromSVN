package LogAnalyzer;

public class SexLinkageResult extends GeneticResult {

	private boolean sexLinked;

	public SexLinkageResult(boolean sexLinked) {
		this.sexLinked = sexLinked;
	}

	public boolean isSexLinked() {
		return sexLinked;
	}

	public boolean equals(Object o) {
		if ((o instanceof SexLinkageResult) &&
				(((SexLinkageResult)o).isSexLinked() == sexLinked)) return true;
		return false;
	}

	public String toString() {
		if (sexLinked) return "Sex-Linkage Result: XX/XY or ZZ/ZW";
		return "Sex-Linkage Result: Autosomal";
	}

	public int hashCode() {
		if (sexLinked) return 1;
		return 0;
	}

}
