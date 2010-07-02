package molBiol;

import java.util.ArrayList;

import preferences.GlobalDefaults;


import biochem.ColorSequencer;


public class GeneExpresser {

	//common variables for the several steps
	private String DNASequence;
	private int promoterStart;
	private int terminatorStart;
	private int numSpacesBeforeRNA_Start;
	private ArrayList<Nucleotide> DNANucleotides;
	private String premRNASequence;
	private int numberOfExons;
	private int numCharsInDisplayBeforeFirstDNA_Base;
	private String mRNASequence;
	private String proteinSequence;
	private String markedUpProteinSequence;

	public GeneExpresser() {
	}

	public ExpressedGene expressGene(String DNA, int selectedDNABase) {
		DNANucleotides = new ArrayList<Nucleotide>();
		DNASequence = DNA;
		//read in the gene
		for (int i = 0; i < DNASequence.length(); i++) {
			DNANucleotides.add(new Nucleotide(DNASequence.charAt(i), i ));
		}
		transcribe();
		process();
		translate();
		return new ExpressedGene(
				DNASequence, 
				generateHTML(DNASequence, selectedDNABase), 
				proteinSequence, 
				getProteinStringForDisplay(),
				numCharsInDisplayBeforeFirstDNA_Base);
	}

	private void transcribe() {
		promoterStart = -1;
		terminatorStart = -1;
		
		// find promoter
		int promoterSite = DNASequence.indexOf(
				GlobalDefaults.molBiolParams.promoterSequence);
		
		//find terminator even if no promoter
		int terminatorSite = -1;
		if (promoterSite == -1) {
			terminatorSite = DNASequence.indexOf(
					GlobalDefaults.molBiolParams.terminatorSequence);	
		} else {
			// if there's a promoter, look for term AFTER it
			promoterStart = promoterSite;
			terminatorSite = DNASequence.indexOf(
					GlobalDefaults.molBiolParams.terminatorSequence, promoterSite);
		}
		if (terminatorSite != -1) {
			terminatorStart = terminatorSite;
		}
		
		StringBuffer pre_mRNAbuffer = new StringBuffer();
		if ((promoterSite != -1) && (terminatorSite != -1)) {  // if we have a gene
			int mRNANucleotideNumber = 0;
			//set the values for this gene
			numSpacesBeforeRNA_Start = promoterStart 
			+ GlobalDefaults.molBiolParams.promoterSequence.length();

			//mark nucleotides in the pre mRNA from promoter to terminator
			for (int i = numSpacesBeforeRNA_Start; i < terminatorSite; i++) {
				Nucleotide nucleotide = (Nucleotide)DNANucleotides.get(i);
				nucleotide.setInPremRNA();
				nucleotide.setPremRNABaseNum(mRNANucleotideNumber);
				mRNANucleotideNumber++;
			}

			//here, pre_mRNA buffer includes leading & trailing whitespace for
			// correct alignment with the DNA
			// count up to DNASequence.length() beacuse this is based on the DNA without the
			//    poly A tail
			for (int i = 0; i < DNASequence.length(); i++) {
				Nucleotide nucleotide = (Nucleotide)DNANucleotides.get(i);
				pre_mRNAbuffer.append(nucleotide.getRNABase());
			}
			//strip off leading & trailing whitespace
			premRNASequence = pre_mRNAbuffer.toString().trim();
		} else {
			premRNASequence = "";
		}
	}

	private void process() {
		//only process if there's an mRNA
		if (!premRNASequence.equals("")) {
			int currentNucleotideNum = 0;  //start with first nuc in mRNA
			StringBuffer mRNAbuffer = new StringBuffer();
			int currentmRNABase = 0;

			while (currentNucleotideNum != -1) {
				Exon currentExon = findNextExon(currentNucleotideNum);
				numberOfExons++;
				currentNucleotideNum = currentExon.getStartOfNext();

				//mark the nucleotides in the exons as being in mRNA
				//and build up the mature mRNA sequence
				for (int i = currentExon.getStart();
				i < currentExon.getEnd();
				i++) {
					Nucleotide nucleotide = 
						(Nucleotide)DNANucleotides.get(i + numSpacesBeforeRNA_Start);
					nucleotide.setInmRNA();
					nucleotide.setmRNABaseNum(currentmRNABase);
					currentmRNABase++;
					mRNAbuffer.append(nucleotide.getRNABase());
				}
			}

			//add on the poly A tail here
			for (int i = terminatorStart; i < (terminatorStart + 
					GlobalDefaults.molBiolParams.polyATail.length()); i++) {
				//see if it's after the end of the gene
				if (i >= DNANucleotides.size()) {
					//if it is after the end, add another one
					Nucleotide nucleotide = new Nucleotide('A', i);
					//it's in the mRNA but not the pre mRNA
					nucleotide.setInmRNA();
					//if it is after the end of the DNA sequence, add a DNA Nucleotide
					DNANucleotides.add(nucleotide);
				} else {
					//mark it as in the mRNA but not the pre mRNA
					Nucleotide nucleotide = (Nucleotide)DNANucleotides.get(i);
					nucleotide.setInmRNA();
				}
			}           
			// add the poly A tail
			mRNASequence = mRNAbuffer.toString() + GlobalDefaults.molBiolParams.polyATail;
		} else {
			// if no pre-mRNA, there's no mRNA
			mRNASequence = "";
		}
	}

	private Exon findNextExon(int currentPosition) {
		//see if there's a start intron sequence up ahead
		int startSite = premRNASequence.indexOf(
				GlobalDefaults.molBiolParams.intronStartSequence, currentPosition);

		//if not, we're done
		if (startSite == -1) return new Exon(currentPosition, premRNASequence.length(), -1);

		//since there's a start, look for an end
		int endSite = premRNASequence.indexOf(
				GlobalDefaults.molBiolParams.intronEndSequence, startSite);

		//if not, we're done also
		if (endSite == -1) return new Exon(currentPosition, premRNASequence.length(), -1);

		//so we have an intron
		// mark the exon & move on
		return new Exon(currentPosition, 
				startSite, 
				endSite + GlobalDefaults.molBiolParams.intronEndSequence.length());
	}

	private void translate() {
		//if no mRNA, no protein
		if (!mRNASequence.equals("")) {
			int aaNum = 0;              //the number of the amino acid in the sequence
			// the stop codon has no number
			int baseInCodon = 0;        //0 = first base; 1 = 2nd; 2 = third base in codon
			int currentmRNABase = 0;    //number of base in mRNA strand
			String currentAA = new String("");     //the amino acid
			StringBuffer proteinBuffer = new StringBuffer();

			//look for start codon
			for (int i = 0; i < DNANucleotides.size(); i++) {
				Nucleotide base = (Nucleotide)DNANucleotides.get(i);
				//find the next 3 mRNA bases (even looking across splice junctions)
				if (base.getInmRNA()) {
					Nucleotide base1 = getNextmRNANucleotide(i);
					Nucleotide base2 = getNextmRNANucleotide(base1.getDNABaseNum() + 1);
					Nucleotide base3 = getNextmRNANucleotide(base2.getDNABaseNum() + 1);
					String codon = base1.getRNABase() + base2.getRNABase() + base3.getRNABase();
					currentmRNABase = base3.getDNABaseNum();
					if (codon.equals("AUG")) {
						//found it - now start translation
						setCodonValues(0, base1, base2, base3);
						proteinBuffer.append(Codon.getAA(codon));
						break;
					}
				}
			}
			//now we're in a protein
			int codonNum = 1;                //next codon
			int j = (currentmRNABase + 1);   //next base
			while (j <= DNANucleotides.size()) {
				//get next codon
				Nucleotide first = getNextmRNANucleotide(j);
				Nucleotide second = getNextmRNANucleotide(first.getDNABaseNum() + 1);
				Nucleotide third = getNextmRNANucleotide(second.getDNABaseNum() + 1);
				String codon = first.getRNABase() + second.getRNABase() + third.getRNABase();

				//if we are going to read after end of mRNA, stop before doing so
				if ((j + 2) >= DNANucleotides.size()) break;

				//point to next codon
				j = third.getDNABaseNum() + 1;

				//get the amino acid for this codon
				proteinBuffer.append(Codon.getAA(codon));
				setCodonValues(codonNum, first, second, third);

				//see if stop codon aaNum = -2 if a stop codon (-1 means not in protein)
				if (Codon.getAA(codon).equals("")) {
					setCodonValues(-2, first, second, third);
					break;
				}
				codonNum++;
			}                   
			proteinSequence = proteinBuffer.toString();
		} else {
			//if no mRNA, no protein
			proteinSequence = "";
		}
	}

	private Nucleotide getNextmRNANucleotide(int DNABaseNum) {

		//be sure it's not out of range
		if (DNABaseNum >= DNANucleotides.size()) {
			DNABaseNum = DNANucleotides.size() - 1;
		}

		//see if the current DNA base corresponds to a base in the mRNA
		Nucleotide nucleotide = (Nucleotide)DNANucleotides.get(DNABaseNum);

		// if not, then loop until you find one
		while (!nucleotide.getInmRNA() && (DNABaseNum < DNANucleotides.size())) {
			nucleotide = (Nucleotide)DNANucleotides.get(DNABaseNum);
			DNABaseNum++;
		}
		return nucleotide;
	}

	//set the values for the Nucleotides in the codon
	private void setCodonValues(int AANum, Nucleotide first, Nucleotide second, Nucleotide third) {
		first.setInProtein();
		first.setAANum(AANum);
		first.setCodonPosition(0);
		second.setInProtein();
		second.setAANum(AANum);
		second.setCodonPosition(1);
		third.setInProtein();
		third.setAANum(AANum);
		third.setCodonPosition(2);
	}

	private String generateHTML(String DNASequence, int selectedDNABase) {
		//mark the selected base (-1 means no base selected)
		if ((selectedDNABase != -1) && (DNANucleotides.size() > 0)) {
			Nucleotide nuc = (Nucleotide)DNANucleotides.get(selectedDNABase);
			nuc.setSelected();
		}

		StringBuffer htmlBuffer = new StringBuffer();

		htmlBuffer.append(generateHTMLHeader());

		htmlBuffer.append(generateDNA_HTML(selectedDNABase));

		numCharsInDisplayBeforeFirstDNA_Base = calcCharsBeforeFirstDNA_Base(
				generateDNA_HTML(selectedDNABase));

		htmlBuffer.append(generatepremRNA_HTML(selectedDNABase));

		htmlBuffer.append(generatemRNA_HTML(selectedDNABase));

		htmlBuffer.append(generateProteinHTML(selectedDNABase));

		return htmlBuffer.toString();
	}

	private String generateHTMLHeader() {
		//the html header that sets up the styles for display
		StringBuffer headerBuffer = new StringBuffer();
		headerBuffer.append("<html><head>");
		headerBuffer.append("<style type=\"text/css\">");
		headerBuffer.append("EM.selected {font-style: normal; background: blue; color: red}");
		headerBuffer.append("EM.promoter {font-style: normal; background: #90FF90; color: black}");
		headerBuffer.append("EM.terminator {font-style: normal; background: #FF9090; color: black}");
		headerBuffer.append("EM.exon {font-style: normal; background: #FF90FF; color: black}");
		headerBuffer.append("EM.next {font-style: normal; background: #90FFFF; color: black}");
		headerBuffer.append("EM.another {font-style: normal; background: #FFFF50; color: black}");
		headerBuffer.append("</style></head><body>");
		return headerBuffer.toString();
	}

	private String generateDNA_HTML(int selectedBase){
		StringBuffer htmlBuffer = new StringBuffer();
		boolean highlighted = false;

		String fivePrimeSpaces = "    ";

		htmlBuffer.append("<html><pre><b>DNA: <EM class=promoter>Promoter</EM>");
		htmlBuffer.append("<EM class=terminator>Terminator</EM></b>\n");

		// then, set up the DNA numbering bars
		//insert some blank spaces for the "5'-"
		htmlBuffer.append(fivePrimeSpaces);

		//first the numbers
		for (int i = 0; i < DNASequence.length(); i = i + 10) {
			String numberLabel = new String("");
			if (i == 0) {
				numberLabel = "";
			} else  if (i < 100 ){
				numberLabel = "        " + i;   
			} else {
				numberLabel = "       " + i;
			}
			htmlBuffer.append(numberLabel);
		}
		htmlBuffer.append("\n");

		//then the tick marks
		final String tickMarkString = "    .    |";
		htmlBuffer.append(fivePrimeSpaces);
		for (int i = 0; i < DNASequence.length(); i = i + 10) {
			if (i > 0) {
				htmlBuffer.append(tickMarkString);
			}
		}
		htmlBuffer.append("\n");

		//do the three strands in color and B/W in parallel
		StringBuffer ColorTopStrandBuffer = new StringBuffer();
		StringBuffer ColorBasePairBuffer = new StringBuffer();
		StringBuffer ColorBottomStrandBuffer = new StringBuffer();

		for (int i = 0; i < DNASequence.length(); i++) {
			Nucleotide n = DNANucleotides.get(i);
			if (i == promoterStart) {
				highlighted = true;
			}            
			if (i == promoterStart 
					+ GlobalDefaults.molBiolParams.promoterSequence.length()) {
				highlighted = false;
			}            
			if (i == terminatorStart) {
				highlighted = true;
			}            
			if (i == terminatorStart 
					+ GlobalDefaults.molBiolParams.terminatorSequence.length()) {
				highlighted = false;
			}

			ColorTopStrandBuffer.append(
					markUpNucleotideSymbol(
							i, n.getBase(), n.getSelected(), highlighted));
			ColorBasePairBuffer.append(
					markUpNucleotideSymbol(
							i, "|", n.getSelected(), highlighted));
			ColorBottomStrandBuffer.append(
					markUpNucleotideSymbol(
							i, n.getComplementBase(), n.getSelected(), highlighted));

		}

		htmlBuffer.append("5\'-");
		htmlBuffer.append(ColorTopStrandBuffer.toString() + "</EM>-3\'\n"
				+ "   " + ColorBasePairBuffer.toString() + "</EM>\n"
				+ "3\'-" + ColorBottomStrandBuffer.toString());
		htmlBuffer.append("</EM>-5\'\n");     //the added </b> is if the last base 
		//  is the end of the terminator

		return htmlBuffer.toString();
	}

	private String generatepremRNA_HTML(int selectedDNABase){
		StringBuffer htmlBuffer = new StringBuffer();
		boolean highlighted = false;
		ColorSequencer exonColorSequencer = new ColorSequencer();

		//see if a prokaryote
		if (!(GlobalDefaults.molBiolParams.intronStartSequence.equals("none") 
				|| GlobalDefaults.molBiolParams.intronEndSequence.equals("none"))) {
			//then the label for the pre-mRNA
			htmlBuffer.append("<hr><b>pre-mRNA: <EM class=exon>Ex</EM><EM class=next>o</EM>"
					+ "<EM class=another>n</EM> Intron</b>\n");

			//then the pre mRNA
			//first the leading spaces
			if (!premRNASequence.equals("")) {
				for (int i = 0; i < numSpacesBeforeRNA_Start; i++ ) {
					htmlBuffer.append(" ");
				}
				htmlBuffer.append("5\'-");	 

				//print out bases and mark exons as you go 
				for (int i = 0; i < DNASequence.length(); i++) {
					//get this & the previous nucleotide
					Nucleotide current = (Nucleotide)DNANucleotides.get(i);
					Nucleotide prev;
					if (i != 0) {
						prev = (Nucleotide)DNANucleotides.get(i - 1);
					} else {
						prev = (Nucleotide)DNANucleotides.get(i);
					}

					//see if it's in the pre mRNA
					if (current.getInPremRNA()) {
						//see if start of exon
						if (!prev.getInmRNA() && current.getInmRNA()) {
							htmlBuffer.append("<EM class=" + exonColorSequencer.getNextColor() + ">");
							highlighted = true;
						}

						//see if end of exon
						if (prev.getInmRNA() && !current.getInmRNA()) {
							htmlBuffer.append("</EM>");
							highlighted = false;
						}

						//see if selected
						if (current.getSelected()) {
							htmlBuffer.append("<EM class=selected>");
							htmlBuffer.append(current.getRNABase());
							htmlBuffer.append("</EM>");
						} else {
							htmlBuffer.append(current.getRNABase());
						}
					}
				}
				htmlBuffer.append("</EM>-3\'\n");    //needs the </em> for the end of the last exon
			} else {
				htmlBuffer.append("<font color=red>none</font>\n");
			}
		}
		return htmlBuffer.toString();
	}

	private String generatemRNA_HTML(int selectedDNABase){
		StringBuffer htmlBuffer = new StringBuffer();
		boolean highlighted = false;
		boolean inPolyA = false;

		ColorSequencer exonColorSequencer = new ColorSequencer();

		//then the label for the mature mRNA & the protein
		htmlBuffer.append("<hr><b>");
		if (!(GlobalDefaults.molBiolParams.intronStartSequence.equals("none") 
				|| GlobalDefaults.molBiolParams.intronEndSequence.equals("none"))) {
			htmlBuffer.append("mature-");
		}
		htmlBuffer.append("mRNA and Protein (<font color=blue>previous</font>):</b>\n");

		//if it's a prokaryote, add the leading spaces
		if (GlobalDefaults.molBiolParams.intronStartSequence.equals("none") || 
				GlobalDefaults.molBiolParams.intronEndSequence.equals("none")) {
			for (int i = 0; i < numSpacesBeforeRNA_Start; i++ ) {
				htmlBuffer.append(" ");
			}
		}

		//then the mature mRNA itself with exons, start, & stop codons marked
		if (!mRNASequence.equals("")) {
			htmlBuffer.append("5\'-");
			for (int i = 0; i < DNANucleotides.size(); i++) {
				//get this & the previous nucleotide
				Nucleotide current = (Nucleotide)DNANucleotides.get(i);
				Nucleotide prev;
				if (i != 0) {
					prev = (Nucleotide)DNANucleotides.get(i - 1);
				} else {
					prev = (Nucleotide)DNANucleotides.get(i);
				}

				Nucleotide next;
				if (i != (DNANucleotides.size() - 1)) {
					next = (Nucleotide)DNANucleotides.get(i + 1);
				} else {
					next = (Nucleotide)DNANucleotides.get(i);
				}

				//see if it's in the mRNA and the pre mRNA (to avoid the poly A tail)
				if (current.getInmRNA()) {
					//see if start of exon
					if (!prev.getInmRNA() && current.getInmRNA()) {
						htmlBuffer.append("<EM class=" + exonColorSequencer.getNextColor() + ">");
						if (highlighted) {
							htmlBuffer.append("<u>");  // do this because sometimes the exon boundaries
							// mess up the underlining
						}
					}

					//see if end of last exon (start of poly A)
					if (!current.getInPremRNA() && current.getInmRNA() && !inPolyA) {
						htmlBuffer.append("</EM>");
						inPolyA = true;
					}

					//see if start of start or stop codon
					if (((current.getAANum() == 0) || (current.getAANum() == -2))
							&& ((current.getCodonPosition() == 0) && (current.getInPremRNA()))) {
						htmlBuffer.append("<u>");
						highlighted = true;
					}

					//see is end of start codon
					if ((current.getAANum() == 1) && (current.getCodonPosition() == 0)) {
						htmlBuffer.append("</u>");
						highlighted = false;
					}

					//see if end of stop codon
					if ((current.getAANum() == -1) && (prev.getAANum() == -2)) {
						htmlBuffer.append("</u>");
						highlighted = false;
					}

					//see if selected (& in pre mRNA - to avoid selecting poly A tail)
					if (current.getSelected() && current.getInPremRNA()) {
						htmlBuffer.append("<EM class=selected>");
						htmlBuffer.append(current.getRNABase());
						htmlBuffer.append("</EM>");
					} else {
						htmlBuffer.append(current.getRNABase());
					}

					// see if its at the end of an exon
					if (current.getInmRNA() && !next.getInmRNA()){
						htmlBuffer.append("</EM>");
					}

				}
			}
			htmlBuffer.append("-3\'\n");    
		} else {
			htmlBuffer.append("<font color=red>none</font>\n");
		}
		return htmlBuffer.toString();
	}

	private String generateProteinHTML(int selectedDNABase){
		StringBuffer htmlBuffer = new StringBuffer();
		boolean highlighted = false;

		StringBuffer proteinStringBuffer = new StringBuffer();

		//if a prokaryote, need more leader to align with top DNA
		if (GlobalDefaults.molBiolParams.intronStartSequence.equals("none") 
				|| GlobalDefaults.molBiolParams.intronEndSequence.equals("none")) {
			for (int i = 0; i < numSpacesBeforeRNA_Start; i++ ) {
				proteinStringBuffer.append(" ");
			}
		}

		if (!proteinSequence.equals("")) {
			for (int i = 0; i < DNASequence.length(); i++) {
				Nucleotide n = (Nucleotide)DNANucleotides.get(i);
				if (n.getInmRNA()) {
					if (n.getAANum() == 0) {
						break;
					}
					proteinStringBuffer.append(" ");
				}
			}
			proteinStringBuffer.append(" N-");

			if (selectedDNABase != -1) {
				//mark the selected amino acid
				// do this by marking up the protein sequence string
				StringBuffer proteinBuffer = new StringBuffer(proteinSequence);
				Nucleotide n = (Nucleotide)DNANucleotides.get(selectedDNABase);

				//see if it is in an amino acid
				if (n.getAANum() >= 0) {
					//first, the end of the selected AA
					proteinBuffer = proteinBuffer.insert(((n.getAANum() * 3) + 3), "</EM>");
					//then, the end of the part of the codon
					proteinBuffer = proteinBuffer.insert(((n.getAANum() * 3) + n.getCodonPosition() + 1), "</u>");
					//then, the start of the psrt of the codon
					proteinBuffer = proteinBuffer.insert(((n.getAANum() * 3) + n.getCodonPosition()), "<u>");
					//then the start
					proteinBuffer = proteinBuffer.insert((n.getAANum() * 3), "<EM class=selected>");
				}
				proteinStringBuffer.append(proteinBuffer.toString() + "-C");
			} else {
				proteinStringBuffer.append(proteinSequence + "-C");
			}
		} else {
			proteinStringBuffer.append("<font color=red>none</font>\n");
		}
		markedUpProteinSequence = proteinStringBuffer.toString();
		htmlBuffer.append(markedUpProteinSequence + "\n");

		return htmlBuffer.toString();
	}

	private String markUpNucleotideSymbol(int nucleotideNum, String nucleotideSymbol,
			boolean selected, boolean highlighted){
		StringBuffer htmlBuffer = new StringBuffer();

		if (nucleotideNum == promoterStart) {
			htmlBuffer.append("<EM class=promoter>");
		}        
		if (nucleotideNum == promoterStart 
				+ GlobalDefaults.molBiolParams.promoterSequence.length()) {
			htmlBuffer.append("</EM>");
		}       
		if (nucleotideNum == terminatorStart) {
			htmlBuffer.append("<EM class=terminator>");
		}  
		if (nucleotideNum == terminatorStart 
				+ GlobalDefaults.molBiolParams.terminatorSequence.length()) {
			htmlBuffer.append("</EM>");
		}

		if (selected) {
			htmlBuffer.append("<EM class=selected>");
			htmlBuffer.append(nucleotideSymbol);
			htmlBuffer.append("</EM>");
		} else {
			htmlBuffer.append(nucleotideSymbol);
		}	  
		return htmlBuffer.toString();
	}

	private int calcCharsBeforeFirstDNA_Base(String HTMLstring){
		String noHTMLstring = HTMLstring.replaceAll("<[^<]*>","");
		return noHTMLstring.indexOf("5\'-") + 4;
	}

	//remove html tags from protein string so it can be displayed as previous sequence
	private String getProteinStringForDisplay() {
		String s = new String(markedUpProteinSequence);
		s = s.replaceAll("<EM class=selected>", "");
		s = s.replaceAll("</EM>","");
		s = s.replaceAll("<u>","");
		return s.replaceAll("</u>", "");
	}


}
