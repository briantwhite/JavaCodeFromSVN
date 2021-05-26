package edu.umb.jsAipotu.client.evolution;

import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.FocusPanel;

import edu.umb.jsAipotu.client.preferences.GlobalDefaults;

public class ThinOrganismUI extends FocusPanel {
	private ThinOrganism to;
	private boolean selected;
	private World world;

	public ThinOrganismUI (ThinOrganism to, World world) {
		this.to = to;
		this.world = world;
		selected = false;
		sinkEvents(Event.ONCLICK);
		setStyleName("unselected-thinOrganismUI");
		this.getElement().getStyle().setBackgroundColor(to.getOverallColor().toString());
		if (GlobalDefaults.colorModel.getColorName(to.getOverallColor()) != null) {
			setTitle(GlobalDefaults.colorModel.getColorName(to.getOverallColor()));
		} else {
			// getColorName returns null if it's 'dead color'
			setTitle("dead/empty");
		}
	}

	// need to capture click events for the custom selection model
	public void onBrowserEvent(Event e) {
		if (e.getTypeInt() == Event.ONCLICK) {
			// you can't click on dead organisms (ones with FoldedInACorner proteins)
			if (!to.getOverallColor().equals(GlobalDefaults.DEAD_COLOR)) {
				this.setFocus(false);
				if (selected) {
					// it's a click on an already-selected one; so clear selection
					//  and disable save to GH button
					setSelected(false);
					world.getMolGenExp().setAddToGreenhouseButtonEnabled(false);
				} else {
					// it's a new selection, so select it and enable save to GH
					world.deselectAllOrganismUIs();
					setSelected(true);
					world.getMolGenExp().setAddToGreenhouseButtonEnabled(true);
				}
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
