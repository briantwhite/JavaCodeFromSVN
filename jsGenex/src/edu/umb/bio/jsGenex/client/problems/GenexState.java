package edu.umb.bio.jsGenex.client.problems;

/*
 * stores the state of the gene in genex
 * used for evaluation in problem sets
 */
public class GenexState {
	
	private String startingDNAsequence;
	private String startingMatureMRNASequence;
	private String startingProteinSequence;
	private int selectedBase;
	private String currentDNASequence;
	private int numberOfExons;
	private String rnaSequence;
	private String proteinSequence;
	
	public GenexState(
			String startingDNAsequence,
			String startingMatureMRNASequence,
			String startingProteinSequence,
			int selectedBase,
			String currentDNASequence,
			int numberOfExons,
			String rnaSequence,
			String proteinSequence) {
		this.startingDNAsequence = startingDNAsequence;
		this.startingMatureMRNASequence = startingMatureMRNASequence;
		this.startingProteinSequence = startingProteinSequence;
		this.selectedBase = selectedBase;
		this.currentDNASequence = currentDNASequence;
		this.numberOfExons = numberOfExons;
		this.rnaSequence = rnaSequence;
		this.proteinSequence = proteinSequence;
//		System.out.println("GenexState\n" + toString());
	}

	public String getStartingDNAsequence() {
		return startingDNAsequence;
	}
	
	public String getStartingMatureMRNAsequence() {
		return startingMatureMRNASequence;
	}
	
	public String getStartingProteinSequence() {
		return startingProteinSequence;
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
		b.append("State;");
		b.append("Starting_DNA=" + startingDNAsequence + ";");
		b.append("Starting_mRNA=" + startingMatureMRNASequence + ";");
		b.append("Starting_protein=" + startingProteinSequence + ";");
		b.append("Selected_base=" + selectedBase + ";");
		b.append("Current_DNA=" + currentDNASequence + ";");
		b.append("Num_Exons=" + numberOfExons + ";");
		b.append("RNA=" + rnaSequence + ";");
		b.append("Protein=" + proteinSequence);
		return b.toString();
	}

}
