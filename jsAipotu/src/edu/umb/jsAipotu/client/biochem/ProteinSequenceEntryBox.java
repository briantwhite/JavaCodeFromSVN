package edu.umb.jsAipotu.client.biochem;

import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.TextBox;

import edu.umb.jsAipotu.client.JsAipotu;


public class ProteinSequenceEntryBox extends TextBox {

	private BiochemistryWorkpanel bwp;
	private StandardTable aaTable;
	private final static String allowedLetters = "ACDEFGHIKLMNPQRSTVWY";

	public ProteinSequenceEntryBox(BiochemistryWorkpanel bwp) {
		this.bwp = bwp;
		aaTable = new StandardTable();
		sinkEvents(Event.ONKEYDOWN);
		sinkEvents(Event.ONCLICK);
	}

	public void onBrowserEvent(Event e) {
		int type = e.getTypeInt();

		if (type == Event.ONCLICK) {
			int cp = getCursorPos();
			if (cp < getText().length()) {
				setCursorPos(cp + (3 - (cp %3)));
			}
		}

		if (type == Event.ONKEYDOWN) {
			e.preventDefault();
			int kc = e.getKeyCode();
			int cp = getCursorPos();

			// left arrow
			if (kc == 37) {
				if (cp >= 3) {
					setCursorPos(cp - 3);
				}
			}

			// right arrow
			if (kc == 39) {
				if (cp <= (getText().length() - 3)) {
					setCursorPos(cp + 3);
				}
			}

			// delete
			if (kc == 8) {
				if (cp >= 2) {
					String leftHalf = getText().substring(0, cp - 3);
					String rightHalf = getText().substring(cp); 
					setText(leftHalf + rightHalf);
					setCursorPos(cp - 3);
				}
			}

			// return
			if (kc == 13) {

			}

			// deal with amino acid entries
			String s = Character.toString((char)e.getKeyCode()).toUpperCase();
			if (allowedLetters.indexOf(s) != -1) {
				String name = aaTable.getFromAbName(s).getName();
				if (getText() == "") {
					// entry into a blank box
					setText(name);
					setCursorPos(3);
				} else {
					String leftHalf = getText().substring(0, cp);
					String rightHalf = getText().substring(cp); 
					setText(leftHalf + name + rightHalf);
					setCursorPos(cp + 3);
				}
				bwp.aaSeqChanged();
			}	
		}

		super.onBrowserEvent(e);
	}

	public void setAminoAcidSequence(String s) {
		// do some safety testing to see if it's one-letter or three
		//  3-letter codes will contain lower case letters
		if (s.equals(s.toUpperCase())) {
			// no lower-case, so it must be 1-letter
			// must convert to 3-letter
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < s.length(); i++) {
				String aa = Character.toString(s.charAt(i));
				sb.append(aaTable.getFromAbName(aa).getName());
			}
			setText(sb.toString()); 
		} else {
			// it is 3-letter
			setText(s.trim());
		}
		bwp.aaSeqChanged();
	}

	public String getAminoAcidSequence() {
		return getText();
	}


}
