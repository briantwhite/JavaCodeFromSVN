/*
 * Created on Oct 12, 2004
 *
 */

/**
 * @author Brian.White
 *
 */
public class HTMLstrings {
	String colorHTMLString;
	String bwHTMLString;
	
	HTMLstrings (String color, String bw) {
		colorHTMLString = color;
		bwHTMLString = bw;
	}
	
	public String getColorHTML() {
		return colorHTMLString;
	}
	
	public String getBwHTML() {
		return bwHTMLString;
	}

}
