package edu.umb.jsAipotu.client.molGenExp;

import java.util.ArrayList;

import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

import edu.umb.jsAipotu.client.JsAipotu;

public class Greenhouse extends CellList<Organism> {

	private ArrayList<Organism> organisms;
	
	public Greenhouse(GreenhouseCell greenhouseCell) {
		super(greenhouseCell);
		organisms = new ArrayList<Organism>();
		final SingleSelectionModel<Organism> selectionModel = new SingleSelectionModel<Organism>();
		this.setSelectionModel(selectionModel);
		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			public void onSelectionChange(SelectionChangeEvent event) {
				Organism o = selectionModel.getSelectedObject();
				JsAipotu.showImage(o.getGene1().getFoldedProteinWithImages().getFullSizePic());
			}
		});
	}

	public void add(Organism org) {
		organisms.add(org);
		setRowData(0, organisms);
	}
	
}
