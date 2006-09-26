package biochem;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;

public class SingleLetterCodeDocument extends DefaultStyledDocument {
	final String allowedLetters = "ACDEFGHIKLMNPQRSTVWY";
	private FoldingWindow foldingWindow = null;
	
	public void insertString(int offs, String str, AttributeSet a) 
	throws BadLocationException {
		String ucString = str.toUpperCase();
		char[] chars = ucString.toCharArray();
		for (int i = (chars.length - 1); i >= 0; i--) {
			String s = String.valueOf(chars[i]);
			if (allowedLetters.indexOf(s) != -1) {
				super.insertString(offs, s, a);
			}
		}
		foldingWindow.aaSeqChanged();
	}
	
	public void remove(int offs, int len) throws BadLocationException {
		super.remove(offs, len);
		foldingWindow.aaSeqChanged();
	}
	
	public void removeAll() {
		try {
			super.remove(0, this.getLength());
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
	
	public void replace(int offset, int length, String text, 
			AttributeSet attrs) throws BadLocationException {
		super.replace(offset, length, text, attrs);
		foldingWindow.aaSeqChanged();
	}
	
	public void setLinkedFoldingWindow(FoldingWindow fw) {
		foldingWindow = fw;
	}
	
}
