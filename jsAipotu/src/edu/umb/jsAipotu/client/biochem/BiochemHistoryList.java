package edu.umb.jsAipotu.client.biochem;

import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

public class BiochemHistoryList extends CellList<FoldedProteinWithImages> {

	private ListDataProvider<FoldedProteinWithImages> entries;
	
	public BiochemHistoryList(BiochemHistListItem item) {
		super(item);
		entries = new ListDataProvider<FoldedProteinWithImages>();
		entries.addDataDisplay(this);
		final SingleSelectionModel<FoldedProteinWithImages> selectionModel = new SingleSelectionModel<FoldedProteinWithImages>();
		this.setSelectionModel(selectionModel);
		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			public void onSelectionChange(SelectionChangeEvent event) {
				FoldedProteinWithImages fp = selectionModel.getSelectedObject();
			}
		});
	}

	public void add(FoldedProteinWithImages fp) {
		entries.getList().add(fp);
	}

}
