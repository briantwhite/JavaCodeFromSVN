package edu.umb.bio.jsMolCalc.client;

import com.google.gwt.i18n.client.NumberFormat;

public class Target {

	static final int PHOBICITY = 0;
	static final int NO_HBONDS = 1;
	static final int YES_HBONDS = 2;
	static final int NO_IONIC_BONDS = 3;
	static final int YES_IONIC_BONDS = 4;

	private double hiPhobicLimit = 0.0f;
	private double loPhobicLimit = 0.0f;
	private int type = 0;

	private boolean completed;

	// for bond-making targets
	public Target(int type) {
		completed = false;
		this.type = type;
	}

	// for phobicity targets
	public Target(double loPhobicLimit, double hiPhobicLimit) {
		completed = false;
		this.type = PHOBICITY;
		this.loPhobicLimit = loPhobicLimit;
		this.hiPhobicLimit = hiPhobicLimit;
	}

	public boolean isCorrect(MoleculeInfo info) {
		switch (type) {

		case PHOBICITY:
			if ((info.phobicity > loPhobicLimit) && (info.phobicity < hiPhobicLimit)) {
				completed = true; 
				return true;
			} else {
				return false;
			}
			
		case NO_HBONDS:
			if (!info.canMakeHBonds) {
				completed = true;
				return true;
			} else {
				return false;
			}

		case YES_HBONDS:
			if (info.canMakeHBonds) {
				completed = true;
				return true;
			} else {
				return false;
			}

		case NO_IONIC_BONDS:
			if (!info.canMakeIonicBonds) {
				completed = true;
				return true;
			} else {
				return false;
			}

		case YES_IONIC_BONDS:
			if (info.canMakeIonicBonds) {
				completed = true;
				return true;
			} else {
				return false;
			}

		default:
			return false;

		}
	}
	
	public boolean isCompleted() {
		return completed;
	}

	public String toString() {
		switch (type) {

		case PHOBICITY:
			NumberFormat nf = NumberFormat.getFormat("0.000");
			return "Has a relative hydrophobicity betweeen "
			+ nf.format(loPhobicLimit)
			+ " and "
			+ nf.format(hiPhobicLimit);

		case NO_HBONDS:
			return "Can not make strong Hydrogen Bonds";

		case YES_HBONDS:
			return "Can make strong Hydrogen Bonds";

		case NO_IONIC_BONDS:
			return "Can not make Ionic Bonds";

		case YES_IONIC_BONDS:
			return "Can make Ionic Bonds";

		default:
			return "Error in setting target type";

		}

	}
}

