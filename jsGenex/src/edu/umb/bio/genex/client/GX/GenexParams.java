package edu.umb.bio.genex.client.GX;
/*
 * the parameters for setting up a genex
 * Created on May 5, 2005
 *
 */

/**
 * @author brian
 *
  */
public class GenexParams {
	String defaultDNA;
	String promoterSequence;
	int promoterSpacing;
	String terminatorSequence;
	String intronStartSequence;
	String intronEndSequence;
	String polyATail;
	boolean allowPrinting; 
	

	public GenexParams() {
		defaultDNA = new String("CAAGGCTATAACCGAGATTGATGCCTTGTGCG" 
	            + "ATAAGGTGTGTCCCCCCCCAAAGTGTCGGATG"
	            + "TCGAGTGCGCGTGCAAAAAAAAACAAAGGCGA"
	            + "GGACCTTAAGAAGGTGTGAGGGGGCGCTCGAT");
		promoterSequence = "TATAA";
		promoterSpacing = 0;
		terminatorSequence = "GGGGG";
		intronStartSequence = "GUGCG";
		intronEndSequence = "CAAAG";
		polyATail = "AAAAAAAAAAAAA";
		allowPrinting = false; 
	}
	

	public boolean isAllowPrinting() {
		return allowPrinting;
	}
	public void setAllowPrinting(boolean allowPrinting) {
		this.allowPrinting = allowPrinting;
	}
	public String getDefaultDNA() {
		return defaultDNA;
	}
	public void setDefaultDNA(String defaultDNA) {
		this.defaultDNA = defaultDNA;
	}
	public String getIntronEndSequence() {
		return intronEndSequence;
	}
	public void setIntronEndSequence(String intronEndSequence) {
		this.intronEndSequence = intronEndSequence;
	}
	public String getIntronStartSequence() {
		return intronStartSequence;
	}
	public void setIntronStartSequence(String intronStartSequence) {
		this.intronStartSequence = intronStartSequence;
	}
	public String getPolyATail() {
		return polyATail;
	}
	public void setPolyATail(String polyATail) {
		this.polyATail = polyATail;
	}
	public String getPromoterSequence() {
		return promoterSequence;
	}
	public void setPromoterSequence(String promoterSequence) {
		this.promoterSequence = promoterSequence;
	}
	public int getPromoterSpacing() {
		return promoterSpacing;
	}
	public void setPromoterSpacing(int s) {
		promoterSpacing = s;
	}
	public String getTerminatorSequence() {
		return terminatorSequence;
	}
	public void setTerminatorSequence(String terminatorSequence) {
		this.terminatorSequence = terminatorSequence;
	}
}
