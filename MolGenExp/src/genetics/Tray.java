package genetics;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import molGenExp.Organism;

public class Tray {
	
	private int serialNumber;
	private ArrayList organisms;
	private ImageIcon thumbImage;
	
	public Tray(int num) {
		serialNumber = num;
		organisms = new ArrayList();
		thumbImage = null;
	}
	
	public void addOrganism(Organism o) {
		organisms.add(o);
	}
	
	public ArrayList getAllOrganisms() {
		return organisms;
	}
	
	public int getNumOrganisms() {
		return organisms.size();
	}
	
	public Organism getOrganismNumber(int i) {
		return (Organism)organisms.get(i);
	}
	
	public void calculateThumbImage() {
		
	}
	
	public ImageIcon getThumbImage() {
		return thumbImage;
	}

}
