package edu.umb.jsAipotu.molBiol;public class ExpressedGene {	private String htmlString;	private String DNA;	private String protein;	private String proteinForDisplay;	private int headerLength;	public ExpressedGene(String DNA, 			String HTML, 			String protein, 			String proteinForDisplay,			int headerLength) { 		htmlString = HTML;		this.DNA = DNA;		this.protein = protein;		this.proteinForDisplay = proteinForDisplay;		this.headerLength = headerLength;	}	public String getHtmlString() {		return htmlString;	}	public String getDNA() {		return DNA;	}	public String getProtein() {		return protein;	}		//a string with spacers, N and C for the "previous" sequence	public String getProteinForDisplay() {		return proteinForDisplay;	}		public int getHeaderLength() {		return headerLength;	}}