package Problems;

/*
 * stores the state of the gene in genex
 * used for evaluation in problem sets
 */
public class GenexState {
	
	private String startingDNAsequence;
	private int selectedBase;
	private String currentDNASequence;
	private int numberOfExons;
	private String rnaSequence;
	private String proteinSequence;
	
	public GenexState(
			String startingDNAsequence,
			int selectedBase,
			String currentDNASequence,
			int numberOfExons,
			String rnaSequence,
			String proteinSequence) {
		this.startingDNAsequence = startingDNAsequence;
		this.selectedBase = selectedBase;
		this.currentDNASequence = currentDNASequence;
		this.numberOfExons = numberOfExons;
		this.rnaSequence = rnaSequence;
		this.proteinSequence = proteinSequence;
		System.out.println("GenexState\n" + toString());
	}

	public String getStartingDNAsequence() {
		return startingDNAsequence;
	}

	public int getSelectedBase() {
		return selectedBase;
	}

	public String getCurrentDNASequence() {
		return currentDNASequence;
	}

	public int getNumberOfExons() {
		return numberOfExons;
	}

	public String getRnaSequence() {
		return rnaSequence;
	}

	public String getProteinSequence() {
		return proteinSequence;
	}
	
	public String toString() {
		StringBuffer b = new StringBuffer();
		b.append("State:\n");
		b.append("\tStarting DNA=" + startingDNAsequence + "\n");
		b.append("\tSelected base=" + selectedBase + "\n");
		b.append("\tCurrent DNA=" + currentDNASequence + "\n");
		b.append("\tNum Exons=" + numberOfExons + "\n");
		b.append("\tRNA=" + rnaSequence + "\n");
		b.append("\tProtein=" + proteinSequence + "\n" + "\n");
		return b.toString();
	}

}
