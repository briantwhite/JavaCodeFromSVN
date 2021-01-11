package edu.umb.jsAipotu.client.molGenExp;

import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

public class Greenhouse extends CellList<Organism> {

	private ListDataProvider<Organism> organisms;
	
	public Greenhouse(GreenhouseCell greenhouseCell) {
		super(greenhouseCell);
		organisms = new ListDataProvider<Organism>();
		organisms.addDataDisplay(this);
		final SingleSelectionModel<Organism> selectionModel = new SingleSelectionModel<Organism>();
		this.setSelectionModel(selectionModel);
		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			public void onSelectionChange(SelectionChangeEvent event) {
				Organism o = selectionModel.getSelectedObject();
				//JsAipotu.showImage(o.getGene1().getFoldedProteinWithImages().getThumbnailPic());
			}
		});
	}

	public void add(Organism org) {
		organisms.getList().add(org);
	}
	
}
