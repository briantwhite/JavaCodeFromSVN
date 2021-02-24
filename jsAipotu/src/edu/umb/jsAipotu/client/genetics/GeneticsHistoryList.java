package edu.umb.jsAipotu.client.genetics;

import java.util.Iterator;

import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;


public class GeneticsHistoryList extends CellList<Tray> {

	private ListDataProvider<Tray> entries;
	private GeneticsWorkbench gwb;
		
	public GeneticsHistoryList(final GeneticsWorkbench gwb, GeneticsHistListItem item) {
		super(item);
		this.gwb = gwb;
		entries = new ListDataProvider<Tray>();
		entries.addDataDisplay(this);
		final SingleSelectionModel<Tray> selectionModel = new SingleSelectionModel<Tray>();
		this.setSelectionModel(selectionModel);
		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			public void onSelectionChange(SelectionChangeEvent event) {
				Iterator<Tray> trIt = entries.getList().iterator();
				while (trIt.hasNext()) {
					trIt.next().setSelected(false);
				}
				Tray tray = selectionModel.getSelectedObject();
				tray.setSelected(true);
				gwb.enableMoveToHistListButtons();
			}
		});
	}
	
	public void add(Tray tray) {
		entries.getList().add(tray);
	}
	
	public Tray getSelectedHistListItem() {
		Iterator<Tray> tit = entries.getList().iterator();
		while (tit.hasNext()) {
			Tray t = tit.next();
			if (t.isSelected()) {
				return t;
			}
		}
		gwb.disableMoveToHistListButtons();
		return null;
	}

}
