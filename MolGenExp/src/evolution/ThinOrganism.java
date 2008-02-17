package evolution;

import java.awt.Color;

import molGenExp.Organism;

// class with just DNA and color - so it's smaller
//  for use with evolution
//  don't bother with pictures unless needed
public class ThinOrganism {
	private String dna1;
	private String dna2;
	private Color color;
	
	//empty organism for testing purposes
	public ThinOrganism(Color color) {
		dna1 = "";
		dna2 = "";
		this.color = color;
	}
	
	public ThinOrganism(String dna1, String dna2) {
		//need to implement this
	}
	
	public ThinOrganism(Organism o) {
		dna1 = o.getGene1().getGene().getDNASequence();
		dna2 = o.getGene2().getGene().getDNASequence();
		color = o.getColor();
	}
	
	public Color getColor() {
		return color;
	}

}
