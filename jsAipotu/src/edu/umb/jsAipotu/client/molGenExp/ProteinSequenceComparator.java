package edu.umb.jsAipotu.client.molGenExp;

import edu.umb.jsAipotu.client.biochem.AminoAcid;
import edu.umb.jsAipotu.client.biochem.FoldingException;
import edu.umb.jsAipotu.client.biochem.PolypeptideFactory;
import edu.umb.jsAipotu.client.biochem.StandardTable;
import edu.umb.jsAipotu.client.match.Blosum50;
import edu.umb.jsAipotu.client.match.NWSmart;

public class ProteinSequenceComparator extends SequenceComparator {
	
	public ProteinSequenceComparator(String u,
			String l,
			String s,
			String c) {
		super (u, l, s, c);
	}

	public void compareSequences(int seqID1, int seqID2) {
		String seq1 = convert3LetterTo1Letter(getSequence(seqID1));
		String seq2 = convert3LetterTo1Letter(getSequence(seqID2));
		String seq1Label = getSequenceLabel(seqID1);
		String seq2Label = getSequenceLabel(seqID2);

		String[] alignment =
			(new NWSmart(new Blosum50(), 8, seq1, seq2).getMatch());

		String upperAlignedSequence = convert1LetterTo3Letter(alignment[0]);
		String lowerAlignedSequence = convert1LetterTo3Letter(alignment[1]);
		
		//mark the differences
		StringBuffer differenceBuffer = new StringBuffer();
		for (int i = 0; i < alignment[0].length(); i++){
			if (alignment[0].charAt(i) != alignment[1].charAt(i)) {
				differenceBuffer.append("*** ");
			} else {
				differenceBuffer.append("    ");
			}
		}
		String differenceString = differenceBuffer.toString();
		html.setHTML("<pre>"
				+ "<font color=blue>"
				+ seq1Label
				+ "</font> "
				+ upperAlignedSequence
				+ "<br>"
				+ "<font color=red>"
				+ DIFFERENCE_LABEL
				+ differenceString 
				+ "</font><br>"
				+ "<font color=green>"
				+ seq2Label 
				+ "</font> "
				+ lowerAlignedSequence
				+ "</pre>");
		resultDialog.setPopupPosition(300, 300);
		resultDialog.show();
	}
	
	private String convert3LetterTo1Letter(String aaSeq) {
		//get the aa seq as a single letter string with no separators
		AminoAcid[] acids;
		try {
			acids = PolypeptideFactory.getInstance().parseInputStringToAmAcArray(aaSeq);
		} catch (FoldingException e) {
			return null;
		}
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < acids.length; i++) {
			buf.append(acids[i].getAbName());
		}
		return buf.toString();
	}
	
	private String convert1LetterTo3Letter(String abAASeq) {
		StandardTable table = new StandardTable();
		StringBuffer aaSeq = new StringBuffer();
		for (int i = 0; i < abAASeq.length(); i++) {
			String aa = String.valueOf(abAASeq.charAt(i));
			if (aa.equals("-")) {
				aaSeq.append("--- ");
			} else {
				aaSeq.append(table.getFromAbName(aa));
				aaSeq.append(" ");
			}
		}
		return aaSeq.toString();
	}

}
