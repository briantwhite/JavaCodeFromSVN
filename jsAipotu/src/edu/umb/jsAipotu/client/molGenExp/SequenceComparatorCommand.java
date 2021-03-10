package edu.umb.jsAipotu.client.molGenExp;
import com.google.gwt.user.client.Command;

import edu.umb.jsAipotu.client.JsAipotu;

public class SequenceComparatorCommand implements Command {

	JsAipotu jsA;
	final int seq1ID;
	final int seq2ID;
	
	public SequenceComparatorCommand(final JsAipotu jsA, final int seq1ID, final int seq2ID) {
		this.jsA = jsA;
		this.seq1ID = seq1ID;
		this.seq2ID = seq2ID;
	}
	
	public void execute() {
		int selectedPane = jsA.getSelectedTabIndex();
		if (selectedPane == MolGenExp.MOLECULAR_BIOLOGY) {
			DNASequenceComparator dsc = jsA.getDNASequenceComparator();
			if (dsc != null) {
				dsc.compareSequences(seq1ID, seq2ID);
			}
			return;
		}
		if (selectedPane == MolGenExp.BIOCHEMISTRY) {
			ProteinSequenceComparator psc = jsA.getProteinSequenceComparator();
			if (psc != null) {
				psc.compareSequences(seq1ID, seq2ID);
			}
			return;
		}
	}

}
