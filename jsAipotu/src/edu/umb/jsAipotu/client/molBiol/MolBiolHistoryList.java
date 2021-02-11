package edu.umb.jsAipotu.client.molBiol;

import java.util.Iterator;

import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

import edu.umb.jsAipotu.client.biochem.BiochemistryWorkbench;

public class MolBiolHistoryList extends CellList<ExpressedGeneWithImages> {

	private ListDataProvider<ExpressedGeneWithImages> entries;
	private MolBiolWorkbench mbwb;
		
	public MolBiolHistoryList(final MolBiolWorkbench mbwb, MolBiolHistListItem item) {
		super(item);
		this.mbwb = mbwb;
		entries = new ListDataProvider<ExpressedGeneWithImages>();
		entries.addDataDisplay(this);
		final SingleSelectionModel<ExpressedGeneWithImages> selectionModel = new SingleSelectionModel<ExpressedGeneWithImages>();
		this.setSelectionModel(selectionModel);
		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			public void onSelectionChange(SelectionChangeEvent event) {
				Iterator<ExpressedGeneWithImages> fpit = entries.getList().iterator();
				while (fpit.hasNext()) {
					fpit.next().setSelected(false);
				}
				ExpressedGeneWithImages eg = selectionModel.getSelectedObject();
				eg.setSelected(true);
				mbwb.enableMoveToHistListButtons();
			}
		});
	}
	
	public void add(ExpressedGeneWithImages eg) {
		entries.getList().add(eg);
	}
	
	public ExpressedGeneWithImages getSelectedHistListItem() {
		Iterator<ExpressedGeneWithImages> egit = entries.getList().iterator();
		while (egit.hasNext()) {
			ExpressedGeneWithImages eg = egit.next();
			if (eg.isSelected()) {
				return eg;
			}
		}
		mbwb.disableMoveToHistListButtons();
		return null;
	}

}
