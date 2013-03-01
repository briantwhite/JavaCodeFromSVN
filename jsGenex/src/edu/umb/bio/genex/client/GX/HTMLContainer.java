package edu.umb.bio.genex.client.GX;
/*
 * Created on Oct 12, 2004
 *
 */

/**
 * @author Brian.White
 *
 */
public class HTMLContainer {
	final String _colorHTMLString;
	final String _bwHTMLString;
	
	HTMLContainer (String color, String bw) {
		_colorHTMLString = color;
		_bwHTMLString = bw;
	}
	
	public String getColorHTML() {
		return _colorHTMLString;
	}
	
	public String getBwHTML() {
		return _bwHTMLString;
	}

}
