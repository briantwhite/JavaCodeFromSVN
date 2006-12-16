package molGenExp;

import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JOptionPane;

import match.DNAidentity;
import match.NWSmart;

public class DNASequenceComparator extends SequenceComparator {
	
	public DNASequenceComparator(String u,
			String l,
			String s,
			String c) {
		super (u, l, s, c);
	}

	public void compareSequences(int seqID1, int seqID2) {
		String seq1 = getSequence(seqID1);
		String seq2 = getSequence(seqID2);
		String seq1Label = getSequenceLabel(seqID1);
		String seq2Label = getSequenceLabel(seqID2);
		
		String[] alignment =
			(new NWSmart(new DNAidentity(), 10000, seq1, seq2)).getMatch();

		String upperAlignedSequence = alignment[0];
		String lowerAlignedSequence = alignment[1];

		//mark the differences
		ArrayList differences = new ArrayList();
		StringBuffer differenceBuffer = new StringBuffer();
		for (int i = 0; i < upperAlignedSequence.length(); i++){
			if (upperAlignedSequence.charAt(i) 
					!= lowerAlignedSequence.charAt(i)) {
				differenceBuffer.append("*");
				differences.add(new Integer(i));
			} else {
				differenceBuffer.append(" ");
			}
		}
		String differenceString = differenceBuffer.toString();

		//generate the numbers and tick marks
		//first the numbers
		StringBuffer tickMarkBuffer = new StringBuffer();
		tickMarkBuffer.append("                     ");
		for (int i = 0; i < upperAlignedSequence.length(); i = i + 10) {
			String numberLabel = new String("");
			if (i == 0) {
				numberLabel = "";
			} else  if (i < 100 ){
				numberLabel = "        " + i;   
			} else {
				numberLabel = "       " + i;
			}
			tickMarkBuffer.append(numberLabel);
		}
		tickMarkBuffer.append("<br>");

		//then the tick marks
		final String tickMarkString = "    .    |";
		tickMarkBuffer.append("                     ");
		for (int i = 0; i < upperAlignedSequence.length(); i = i + 10) {
			if (i > 0) {
				tickMarkBuffer.append(tickMarkString);
			}
		}
		tickMarkBuffer.append("<br>");
		String numberingString = tickMarkBuffer.toString();

		StringBuffer differenceListBuffer = new StringBuffer();
		differenceListBuffer.append("Differences at positions:  ");
		Iterator it = differences.iterator();
		while (it.hasNext()) {
			differenceListBuffer.append(((Integer)it.next()).toString());
			differenceListBuffer.append(", ");
		}
		differenceListBuffer.delete((differenceListBuffer.length() - 2),
				(differenceListBuffer.length() - 1));
		String differenceListString = differenceListBuffer.toString();

		JOptionPane.showMessageDialog(null, 
				"<html><body><pre>"
				+ numberingString
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
				+ "</pre>"
				+ differenceListString
				+ "</body></html>",
				"Differences between "
				+ seq1Label
				+ " and "
				+ seq2Label
				+ " DNA Sequences.",
				JOptionPane.PLAIN_MESSAGE,
				null);

	}
}
