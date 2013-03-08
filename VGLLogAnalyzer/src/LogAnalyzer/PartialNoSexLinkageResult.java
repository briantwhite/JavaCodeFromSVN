package LogAnalyzer;

public class PartialNoSexLinkageResult extends GeneticResult {
	
	private String dadPheno;
	private String momPheno;
	
	public PartialNoSexLinkageResult(String dadPheno, String momPheno) {
		this.dadPheno = dadPheno;
		this.momPheno = momPheno;
	}

	public String getDadPheno() {
		return dadPheno;
	}

	public String getMomPheno() {
		return momPheno;
	}
	
	public boolean isReciprocalOf(PartialNoSexLinkageResult result) {
		if (result.getDadPheno().equals(momPheno) && result.getMomPheno().equals(dadPheno)) {
			return true;
		}
		return false;
	}

	public boolean equals(Object o) {
		if (o instanceof PartialNoSexLinkageResult) {
			PartialNoSexLinkageResult pnsr = (PartialNoSexLinkageResult)o;
			if (pnsr.getDadPheno().equals(dadPheno) 
					&& pnsr.getMomPheno().equals(momPheno))  {
				return true;
			}
		}
		return false;
	}

	public int hashCode() {
		return dadPheno.hashCode() ^ momPheno.hashCode();
	}

	public String toString() {
		return new String("PartialNSLResult: " + dadPheno.toString() + "*" + momPheno.toString());
	}

}
