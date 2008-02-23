package utilities;

import molGenExp.MolGenExp;
/*
 * the parameters for setting up a genex
 * Created on May 5, 2005
 *
 */

/**
 * @author brian
 *
  */
public class MolBiolParams {
	String defaultDNA;
	String promoterSequence;
	String terminatorSequence;
	String intronStartSequence;
	String intronEndSequence;
	String polyATail;


	public MolBiolParams() {
		defaultDNA = GlobalDefaults.sampleDNA;
		promoterSequence = "TATAA";
		terminatorSequence = "GGGGG";
		intronStartSequence = "GUGCG";
		intronEndSequence = "CAAAG";
		polyATail = "AAAAAAAAAAAAA";
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
	public String getTerminatorSequence() {
		return terminatorSequence;
	}
	public void setTerminatorSequence(String terminatorSequence) {
		this.terminatorSequence = terminatorSequence;
	}
}
