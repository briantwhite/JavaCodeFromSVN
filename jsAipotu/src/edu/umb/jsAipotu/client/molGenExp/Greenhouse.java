package edu.umb.jsAipotu.client.molGenExp;

import java.util.ArrayList;

import com.google.gwt.user.cellview.client.CellList;

public class Greenhouse extends CellList<Organism> {

	private ArrayList<Organism> organisms;
	
	public Greenhouse(GreenhouseCell greenhouseCell) {
		super(greenhouseCell);
		organisms = new ArrayList<Organism>();
	}

	public void add(Organism org) {
		organisms.add(org);
		setRowData(0, organisms);
	}
	
}
