package edu.umb.jsAipotu.molGenExp;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SequenceComparatorMenuItemListener implements ActionListener {

	MolGenExp mge;
	final int seq1ID;
	final int seq2ID;
	
	public SequenceComparatorMenuItemListener(final MolGenExp mge, final int seq1ID, final int seq2ID) {
		this.mge = mge;
		this.seq1ID = seq1ID;
		this.seq2ID = seq2ID;
	}
	
	public void actionPerformed(ActionEvent arg0) {
		String selectedPane = mge.getCurrentWorkingPanel();
		if (selectedPane.equals("class molBiol.MolBiolWorkbench")) {
			DNASequenceComparator dsc = mge.getDNASequences();
			if (dsc != null) {
				dsc.compareSequences(seq1ID, seq2ID);
			}
			return;
		}
		if (selectedPane.equals("class biochem.BiochemistryWorkbench")) {
			ProteinSequenceComparator psc = mge.getProteinSequences();
			if (psc != null) {
				psc.compareSequences(seq1ID, seq2ID);
			}
			return;
		}
	}

}
