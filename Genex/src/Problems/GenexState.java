package Problems;

/*
 * stores the state of the gene in genex
 * used for evaluation in problem sets
 */
public class GenexState {
	
	private String startingDNAsequence;
	private int selectedBase;
	private String currentDNASequence;
	private int numberOfIntrons;
	private String rnaSequence;
	private String proteinSequence;
	
	public GenexState(
			String startingDNAsequence,
			int selectedBase,
			String currentDNASequence,
			int numberOfIntrons,
			String rnaSequence,
			String proteinSequence) {
		this.startingDNAsequence = startingDNAsequence;
		this.selectedBase = selectedBase;
		this.currentDNASequence = currentDNASequence;
		this.numberOfIntrons = numberOfIntrons;
		this.rnaSequence = rnaSequence;
		this.proteinSequence = proteinSequence;
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

	public int getNumberOfIntrons() {
		return numberOfIntrons;
	}

	public String getRnaSequence() {
		return rnaSequence;
	}

	public String getProteinSequence() {
		return proteinSequence;
	}

}
