package edu.umb.bio.jsMolCalc.client;

import com.google.gwt.i18n.client.NumberFormat;

public class MoleculeInfo {
	
	public String formula;
	public boolean canMakeHBonds;
	public boolean canMakeIonicBonds;
	public double phobicity;
	public String errorString;
	
	public MoleculeInfo(String formula, boolean hBonds, boolean iBonds, double phobicity, String errorString) {
		this.formula = formula;
		this.canMakeHBonds = hBonds;
		this.canMakeIonicBonds = iBonds;
		this.phobicity = phobicity;
		this.errorString = errorString;
	}
	
	public String toString() {
		NumberFormat nf = NumberFormat.getFormat("0.000");

		StringBuffer b = new StringBuffer();
		b.append("<html><body>");
		if (errorString != null) {
			b.append(errorString);
		} else {
			if (phobicity < 0) {
				b.append("<font color=green>Hydrophobicity index = " + nf.format(phobicity)
						+ "</font><br>");
			} else {
				b.append("<font color=red>Hydrophobicity index = " + nf.format(phobicity)
						+ "</font><br>");
			}
			
			if (phobicity > 1.0f) {
				b.append("It is hydrophobic</br>");
			} else if (phobicity < -1.0f) {
				b.append("It is hydrophilic</br>");
			} else {
				b.append("It is intermediate</br>");
			}

			if (canMakeHBonds) {
				b.append("<font color=green>Can Make Strong Hydrogen Bonds</font><br>");
			} else {
				b.append("<font color=red>Can not Make Strong Hydrogen Bonds</font><br>");
			}

			if (canMakeIonicBonds) {
				b.append("<font color=green>Can Make Ionic Bonds</font><br>");
			} else {
				b.append("<font color=red>Can not Make Ionic Bonds</font><br>");
			}
		}
		b.append("</body></html>");
		return b.toString();
	}

}
