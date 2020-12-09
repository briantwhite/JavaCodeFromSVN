package edu.umb.jsAipotu.client.molGenExp;

import com.google.gwt.canvas.dom.client.CssColor;

public class Organism {
	
	private String name;
	
//	private ExpressedAndFoldedGene gene1;
//	private ExpressedAndFoldedGene gene2;
	private CssColor color;
	private String imageFileName;
	
	public Organism(String name, 
			//ExpressedAndFoldedGene gene1, ExpressedAndFoldedGene gene2,
			CssColor color,
			String imageFileName
			) {
		this.name = name; 
//		this.gene1 = gene1;
//		this.gene2 = gene2;
		this.color = color;
		this.imageFileName = imageFileName;
	}
			
	public String getName() {
		return name;
	}

//	public ExpressedAndFoldedGene getGene1() {
//		return gene1;
//	}
//
//	public ExpressedAndFoldedGene getGene2() {
//		return gene2;
//	}

	public CssColor getColor() {
		return color;
	}
	
	public String getImageFileName() {
		return imageFileName;
	}
	
	public void setName(String name) {
		this.name = name;
	}
}
