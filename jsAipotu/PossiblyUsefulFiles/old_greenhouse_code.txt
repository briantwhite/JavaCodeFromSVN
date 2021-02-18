package edu.umb.jsAipotu.client.molGenExp;

import java.util.Set;

import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;

public class Greenhouse extends CellList<Organism> {

	private ListDataProvider<Organism> organisms;
	private MolGenExp mge;
	
	public Greenhouse(MolGenExp mge, GreenhouseCell greenhouseCell) {
		super(greenhouseCell);
		this.mge = mge;
		organisms = new ListDataProvider<Organism>();
		organisms.addDataDisplay(this);
		final MultiSelectionModel<Organism> selectionModel = new MultiSelectionModel<Organism>();
		this.setSelectionModel(selectionModel);
		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			public void onSelectionChange(SelectionChangeEvent event) {
				Set<Organism> oSet = selectionModel.getSelectedSet();
				//mge.handleGreenhouseSelection(oSet);
			}
		});
	}

	public void add(Organism org) {
		organisms.getList().add(org);
	}
	
}
