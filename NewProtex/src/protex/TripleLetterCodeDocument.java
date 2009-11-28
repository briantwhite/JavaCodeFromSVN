package protex;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;

public class TripleLetterCodeDocument extends DefaultStyledDocument {
	final String allowedLetters = "ACDEFGHIKLMNPQRSTVWY";
	private FoldingWindow foldingWindow = null;
	private StandardTable table = StandardTable.getInstance();
	
	public void insertString(int offs, String str, AttributeSet a) 
	throws BadLocationException {
		String ucString = str.toUpperCase();
		char[] chars = ucString.toCharArray();
		for (int i = (chars.length - 1); i >= 0; i--) {
			String s = String.valueOf(chars[i]);
			if (allowedLetters.indexOf(s) != -1) {
				String name = table.getFromAbName(s).getName();
				int newOffset;
				if ((offs % 4) > 1) {
					newOffset = offs - (offs % 4) + 4;
				} else {
					newOffset = offs - (offs % 4);
				}
				super.insertString(newOffset, name + " ", a);
			}
		}
		foldingWindow.aaSeqChanged();
	}
	
	public void remove(int offs, int len) throws BadLocationException {
		foldingWindow.aaSeqChanged();
		int placeInAaName = offs % 4;
		int numOfAAsSelected = ((offs + (len - 1))/4) - (offs/4) + 1;
		super.remove(offs - placeInAaName, 4 * numOfAAsSelected);
	}
	
	public void removeAll() {
		try {
			super.remove(0, this.getLength());
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	public void setLinkedFoldingWindow(FoldingWindow fw) {
		foldingWindow = fw;
	}
	
}
