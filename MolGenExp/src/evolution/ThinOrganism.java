package evolution;

import java.awt.Color;
import java.util.Random;

//class with just DNA and color - so it's smaller
//for use with evolution
//don't bother with pictures unless needed
public class ThinOrganism {
	private String dna1;
	private String dna2;
	private Color color1;
	private Color color2;
	private Color overallColor;

	protected ThinOrganism(String dna1, String dna2, 
			Color color1, Color color2, 
			Color overallColor) {
		this.dna1 = dna1;
		this.dna2 = dna2;
		this.color1 = color1;
		this.color2 = color2;
		this.overallColor = overallColor;
	}
	
	public String getDNA1() {
		return dna1;
	}

	public String getDNA2() {
		return dna2;
	}
	
	public Color getColor1() {
		return color1;
	}
	
	public Color getColor2() {
		return color2;
	}

	public Color getOverallColor() {
		return overallColor;
	}

	public String getRandomDNASequence() {
		Random r = new Random();
		if (r.nextDouble() > 0.5) {
			return dna1;
		} else {
			return dna2;
		}
	}
}
