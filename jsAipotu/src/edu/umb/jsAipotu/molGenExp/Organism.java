package edu.umb.jsAipotu.molGenExp;

import java.awt.Color;

import javax.swing.ImageIcon;

public class Organism {
	
	private String name;
	
	private ExpressedAndFoldedGene gene1;
	private ExpressedAndFoldedGene gene2;
	private Color color;
	private ImageIcon image;
	
	public Organism(String name, 
			ExpressedAndFoldedGene gene1, ExpressedAndFoldedGene gene2,
			Color color,
			ImageIcon image) {
		this.name = name; 
		this.gene1 = gene1;
		this.gene2 = gene2;
		this.color = color;
		this.image = image;
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

	public Color getColor() {
		return color;
	}
	
	public ImageIcon getImage() {
		return image;
	}
	
	public void setName(String name) {
		this.name = name;
	}
}
