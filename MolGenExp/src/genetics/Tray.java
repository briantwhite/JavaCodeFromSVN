package genetics;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import molGenExp.Organism;

public class Tray {
	
	private int trayNumber;
	private String parentInfo;
	private Organism[] allOrganisms;
	private ImageIcon thumbImage;
	
	public Tray(int trayNumber, 
			String parentInfo, 
			OffspringList offspringList) {
		this.parentInfo = parentInfo;
		this.trayNumber = trayNumber;
		
		Object[] organisms = offspringList.getAll();
		allOrganisms = new Organism[organisms.length];
		
		BufferedImage pic = new BufferedImage(100, 30, 
				BufferedImage.TYPE_INT_RGB);
		Graphics2D g = pic.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
				RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, 0, 100, 30);

		for (int i = 0; i < organisms.length; i++) {
			//get the organism
			Organism o = (Organism)organisms[i];
			
			//save a copy in the tray with a new location
			allOrganisms[i] = new Organism(-1, o.getName(), o);
			
			//add to the icon
			g.setColor(o.getColor());
			int x = (10*(i%10)) + 1;
			int y = (10*(i/10)) + 1;
			g.fillRect(x, y, 10, 10);
		}
		g.dispose();
		thumbImage = new ImageIcon(pic);
	}
	
	public String getParentInfo() {
		return parentInfo;
	}
	
	public int getNumber() {
		return trayNumber;
	}
		
	public ImageIcon getThumbImage() {
		return thumbImage;
	}

}
