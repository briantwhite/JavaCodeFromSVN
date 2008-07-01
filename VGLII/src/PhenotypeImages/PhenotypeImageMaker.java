package PhenotypeImages;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import GeneticModels.Phenotype;
import GeneticModels.Trait;

public class PhenotypeImageMaker {

	private static final int IMAGE_SIZE = 150;
	private ArrayList<Phenotype> phenotypes;
	private Graphics2D g2d;

	private Color bodyColor;
	private Color eyeColor;
	private Color antennaColor;

	private String antennaShape;

	private int antennaNumber;

	/**
	 * hard-coded numbers for drawing things
	 */
	private static final int[] ONE_ANT = {125};
	private static final int[] TWO_ANT = {120, 130};
	private static final int[] THREE_ANT = {120, 125, 130};
	private static final int[] FOUR_ANT = {116, 122, 128, 134};
	private static final int[] FIVE_ANT = {115, 120, 125, 130, 135};
	private static final int[] SIX_ANT = {115, 119, 123, 127, 131, 135};

	public PhenotypeImageMaker() {
		bodyColor = Color.LIGHT_GRAY;
		eyeColor = Color.WHITE;
		antennaColor = Color.LIGHT_GRAY;

		antennaShape = "Long";

		antennaNumber = 2;
	}

	public ImageIcon makeImage(ArrayList<Phenotype> phenotypes) {
		this.phenotypes = phenotypes;
		BufferedImage image = 
			new BufferedImage(IMAGE_SIZE, IMAGE_SIZE, BufferedImage.TYPE_INT_RGB);
		g2d = (Graphics2D)image.getGraphics();

		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, IMAGE_SIZE, IMAGE_SIZE);

		drawHead();

		return new ImageIcon(image);
	}

	private void drawHead() {
		Phenotype p = findPhenotypeMatching("Body", "Color");
		if (p != null) {
			bodyColor = getColorFromString(p.getTrait().getTraitName());
		}
		g2d.setColor(bodyColor);

		//the head
		GeneralPath path = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
		path.moveTo(115, 50);
		path.lineTo(135, 50);
		path.quadTo(145, 50, 145, 60);
		path.lineTo(145, 90);
		path.quadTo(145, 100, 135, 100);
		path.lineTo(115, 100);
		path.quadTo(105, 100, 105, 90);
		path.lineTo(105, 60);
		path.quadTo(105, 50, 115, 50);
		path.closePath();
		g2d.draw(path);
		g2d.fill(path);

		//the eye
		p = findPhenotypeMatching("Eye", "Color");
		if (p != null) {
			eyeColor = getColorFromString(p.getTrait().getTraitName());
		}
		g2d.setColor(eyeColor);
		g2d.fillOval(130, 65, 15, 15);

		//antennae
		p = findPhenotypeMatching("Antenna", "Number");
		if (p != null) {
			antennaNumber = getIntFromString(p.getTrait().getTraitName());
		}

		int[] antennaLocations = null;
		switch (antennaNumber) {
		case 0:
			antennaLocations = null;
			break;
		case 1:
			antennaLocations = ONE_ANT;
			break;
		case 2:
			antennaLocations = TWO_ANT;
			break;
		case 3:
			antennaLocations = THREE_ANT;
			break;
		case 4:
			antennaLocations = FOUR_ANT;
			break;
		case 5:
			antennaLocations = FIVE_ANT;
			break;
		case 6:
			antennaLocations = SIX_ANT;
			break;
		}

		p = findPhenotypeMatching("Antenna", "Color");
		if (p != null) {
			antennaColor = getColorFromString(p.getTrait().getTraitName());
		}
		g2d.setColor(antennaColor);

		p = findPhenotypeMatching("Antenna", "Shape");
		if (p != null) {
			antennaShape = p.getTrait().getTraitName();
		}

		//draw the antennae
		if (antennaLocations != null) {
			for (int i = 0; i < antennaLocations.length; i++) {
				int x = antennaLocations[i];
				if (antennaShape.equals("Forked")) {
					g2d.drawLine(x, 50, x, 40);
					g2d.drawLine(x, 40, (x - 1), 30);
					g2d.drawLine(x, 40, (x + 1), 30);
				} else if (antennaShape.equals("Long")) {
					g2d.drawLine(x, 50, x, 20);
				} else if (antennaShape.equals("Short")) {
					g2d.drawLine(x, 50, x, 35);
				} else if (antennaShape.equals("Bent")) {
					g2d.drawLine(x, 50, x, 40);
					g2d.drawLine(x, 40, (x - 4), 30);
				} else if (antennaShape.equals("Pointy")) {
					
				} else if (antennaShape.equals("Knobbed")) {
					
				}
			}
		}
	}

	//see if the organism has a specified phenotype
	// for this body part and type & return it
	// if not, return null
	private Phenotype findPhenotypeMatching(String bodyPart, String type) {
		Phenotype result = null;
		for (int i = 0; i < phenotypes.size(); i++) {
			Phenotype p = phenotypes.get(i);
			Trait t = p.getTrait();
			if (t.getBodyPart().equals(bodyPart) && t.getType().equals(type)) {
				result = p;
				break;
			}
		}
		return result;
	}

	private Color getColorFromString(String s) {
		if (s.equals("Red")) return Color.BLUE;
		if (s.equals("Green")) return Color.GREEN;
		if (s.equals("Blue")) return Color.BLUE;
		if (s.equals("Yellow")) return Color.YELLOW;
		if (s.equals("Purple")) return Color.MAGENTA;
		if (s.equals("Black")) return Color.BLACK;
		if (s.equals("Brown")) return new Color(165, 42, 42);
		else return Color.LIGHT_GRAY;
	}

	private int getIntFromString(String s) {
		if (s.equals("No")) return 0;
		if (s.equals("One")) return 1;
		if (s.equals("Two")) return 2;
		if (s.equals("Three")) return 3;
		if (s.equals("Four")) return 4;
		if (s.equals("Five")) return 5;
		if (s.equals("Six")) return 6;
		else return 0;
	}
}
