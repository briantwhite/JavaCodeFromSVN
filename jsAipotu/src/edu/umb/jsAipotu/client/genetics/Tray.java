package edu.umb.jsAipotu.client.genetics;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.ImageIcon;

import edu.umb.jsAipotu.client.molGenExp.HistListItem;
import edu.umb.jsAipotu.client.molGenExp.Organism;
import edu.umb.jsAipotu.client.molGenExp.OrganismFactory;
import edu.umb.jsAipotu.client.preferences.GlobalDefaults;

public class Tray extends HistListItem {
	
	private int trayNumber;
	private String parentInfo;
	private Organism[] allOrganisms;
	private ImageIcon thumbImage;
	
	private OrganismFactory organismFactory;
	
	public Tray(int trayNumber, 
			String parentInfo, 
			OffspringList offspringList) {
		super();
		this.parentInfo = parentInfo;
		this.trayNumber = trayNumber;
		toolTipText = parentInfo;
		organismFactory = new OrganismFactory();
		
		Object[] organisms = offspringList.getAll();
		allOrganisms = new Organism[organisms.length];
		
		// make thumbnail
		BufferedImage pic = new BufferedImage(80, 40, 
				BufferedImage.TYPE_INT_RGB);
		Graphics2D g = pic.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
				RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, 0, 80, 40);

		for (int i = 0; i < organisms.length; i++) {
			//get the organism
			Organism o = (Organism)organisms[i];
			
			//save a copy in the tray with a new location
			allOrganisms[i] = organismFactory.createOrganism(o.getName(), o);
			
			//add to the icon
			g.setColor(o.getColor());
			int x = (10*(i%8)) + 1;
			int y = (10*(i/8)) + 1;
			g.fillRect(x, y, 10, 10);
		}
		g.dispose();
		thumbImage = new ImageIcon(pic);
	}
	
	public String getParentInfo() {
		return parentInfo;
	}
		
	public String getToolTipText() {
		return toolTipText;
	}
	
	public void setToolTipText(String text) {
		toolTipText = text;
	}
	
	public int getNumber() {
		return trayNumber;
	}
		
	public ImageIcon getThumbImage() {
		return thumbImage;
	}

	public Organism[] getAllOrganisms() {
		return allOrganisms;
	}
}
