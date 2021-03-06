package edu.umb.jsAipotu.client.biochem;

import java.util.Iterator;

import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

public class BiochemHistoryList extends CellList<FoldedProteinWithImages> {

	private ListDataProvider<FoldedProteinWithImages> entries;
	private BiochemistryWorkbench bwb;
		
	public BiochemHistoryList(final BiochemistryWorkbench bwb, BiochemHistListItem item) {
		super(item);
		this.bwb = bwb;
		entries = new ListDataProvider<FoldedProteinWithImages>();
		entries.addDataDisplay(this);
		final SingleSelectionModel<FoldedProteinWithImages> selectionModel = new SingleSelectionModel<FoldedProteinWithImages>();
		this.setSelectionModel(selectionModel);
		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			public void onSelectionChange(SelectionChangeEvent event) {
				Iterator<FoldedProteinWithImages> fpit = entries.getList().iterator();
				while (fpit.hasNext()) {
					fpit.next().setSelected(false);
				}
				FoldedProteinWithImages fp = selectionModel.getSelectedObject();
				fp.setSelected(true);
				bwb.enableMoveToHistListButtons();
			}
		});
	}
	
	public void add(FoldedProteinWithImages fp) {
		entries.getList().add(fp);
	}
	
	public FoldedProteinWithImages getSelectedHistListItem() {
		Iterator<FoldedProteinWithImages> fpit = entries.getList().iterator();
		while (fpit.hasNext()) {
			FoldedProteinWithImages fp = fpit.next();
			if (fp.isSelected()) {
				return fp;
			}
		}
		bwb.disableMoveToHistListButtons();
		return null;
	}

}
