package edu.umb.jsAipotu.client.molGenExp;

import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Image;

public class Organism {
	
	private String name;
	
	private ExpressedAndFoldedGene gene1;
	private ExpressedAndFoldedGene gene2;
	private CssColor color;
	private ImageResource imageResource;
	
	public Organism(String name, 
			ExpressedAndFoldedGene gene1, ExpressedAndFoldedGene gene2,
			CssColor color,
			ImageResource imageResource
			) {
		this.name = name; 
		this.gene1 = gene1;
		this.gene2 = gene2;
		this.color = color;
		this.imageResource = imageResource;
	}
			
	public String getName() {
		return name;
	}

	public ExpressedAndFoldedGene getGene1() {
		return gene1;
	}

	public ExpressedAndFoldedGene getGene2() {
		return gene2;
	}

	public CssColor getColor() {
		return color;
	}
	
	public ImageResource getImageResource() {
		return imageResource;
	}
	
	public void setName(String name) {
		this.name = name;
	}
}
