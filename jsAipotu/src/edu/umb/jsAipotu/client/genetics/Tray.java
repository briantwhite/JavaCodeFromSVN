package edu.umb.jsAipotu.client.genetics;

import java.util.ArrayList;
import java.util.Iterator;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;

import edu.umb.jsAipotu.client.molGenExp.HistListItem;
import edu.umb.jsAipotu.client.molGenExp.Organism;
import edu.umb.jsAipotu.client.molGenExp.OrganismFactory;

public class Tray extends HistListItem {
	
	private static final int thumbnailCellSize = 20;
	
	private int trayNumber;
	private String parentInfo;
	private ArrayList<Organism> allOrganisms;
	private Canvas thumbCanvas;
	private boolean isSelected;
	private OrganismFactory organismFactory;
	
	public Tray(int trayNumber, 
			String parentInfo, 
			ArrayList<Organism> organisms) {
		super();
		this.parentInfo = parentInfo;
		this.trayNumber = trayNumber;
		allOrganisms = new ArrayList<Organism>(); // a local copy of all the organisms
		isSelected = false;
		toolTipText = parentInfo;
		organismFactory = new OrganismFactory();
			
		// make thumbnail
		thumbCanvas = Canvas.createIfSupported();
		thumbCanvas.setCoordinateSpaceHeight(thumbnailCellSize * GeneticsWorkPanel.NUM_ROWS);
		thumbCanvas.setCoordinateSpaceWidth(thumbnailCellSize * GeneticsWorkPanel.NUM_COLS);
		Context2d tg = thumbCanvas.getContext2d();
		tg.setFillStyle(CssColor.make("LightGray"));
		tg.fillRect(0, 0, 160, 80);

		Iterator<Organism> oIt = organisms.iterator();
		int i = 0;
		while (oIt.hasNext()) {
			//get the organism
			Organism o = oIt.next();
			
			//save a copy in the tray with a new location
			allOrganisms.add(organismFactory.createOrganism(o.getName(), o));
			
			//add to the icon
			tg.setFillStyle(o.getColor());
			int x = (thumbnailCellSize * (i % GeneticsWorkPanel.NUM_COLS)) + 1;
			int y = (thumbnailCellSize * (i/GeneticsWorkPanel.NUM_COLS)) + 1;
			tg.fillRect(x, y, thumbnailCellSize, thumbnailCellSize);
			i++;
		}
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
		
	public Canvas getThumbCanvas() {
		return thumbCanvas;
	}

	public ArrayList<Organism> getAllOrganisms() {
		return allOrganisms;
	}

	public boolean isSelected() {
		return isSelected;
	}
	
	public void setSelected(boolean b) {
		isSelected = b;
	}
}
