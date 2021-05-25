package edu.umb.jsAipotu.client.evolution;

import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.FocusPanel;

import edu.umb.jsAipotu.client.molGenExp.MolGenExp;
import edu.umb.jsAipotu.client.preferences.GlobalDefaults;

public class ThinOrganismUI extends FocusPanel {
	private ThinOrganism to;
	private boolean selected;
	private MolGenExp mge;
	
	public ThinOrganismUI (ThinOrganism to, MolGenExp mge) {
		this.to = to;
		this.mge = mge;
		selected = false;
		sinkEvents(Event.ONCLICK);
		setStyleName("unselected-thinOrganismUI");
		this.getElement().getStyle().setBackgroundColor(to.getOverallColor().toString());
		setTitle(GlobalDefaults.colorModel.getColorName(to.getOverallColor()));
	}
	
	// need to capture click events for the custom selection model
	public void onBrowserEvent(Event e) {
		if (e.getTypeInt() == Event.ONCLICK) {
			this.setFocus(false);
			if (selected) {
				setSelected(false);
			} else {
				setSelected(true);
			}
		}
	}
	
	public ThinOrganism getThinOrganism() {
		return to;
	}
	
	public boolean isSelected() {
		return selected;
	}
		
	public void setSelected(boolean b) {
		selected = b;
		if (selected) {
			setStyleName("selected-thinOrganismUI");
		} else {
			setStyleName("unselected-thinOrganismUI");
		}
	}	

}
