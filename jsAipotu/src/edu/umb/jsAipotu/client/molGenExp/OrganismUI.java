package edu.umb.jsAipotu.client.molGenExp;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Image;

import edu.umb.jsAipotu.client.preferences.GlobalDefaults;

public class OrganismUI extends FocusPanel {
	
	private Organism o;
	private boolean selected;
	private MolGenExp mge;
	
	public OrganismUI (Organism o, MolGenExp mge) {
		this.o = o;
		this.mge = mge;
		selected = false;
		CaptionPanel innerPanel = new CaptionPanel(o.getName());
		ImageResource flowerImageResource = GlobalDefaults.colorModel.getImageResourceFromColor(o.getColor());
		Image pic = new Image(flowerImageResource);
		innerPanel.add(pic);
		this.add(innerPanel);
		sinkEvents(Event.ONCLICK);
		setStyleName("unSelectedOrganismUI");
		setTitle(GlobalDefaults.colorModel.getColorName(o.getColor()));
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
			mge.organismWasClicked(this);
		}
	}
	
	public Organism getOrganism() {
		return o;
	}
	
	public boolean isSelected() {
		return selected;
	}
		
	public void setSelected(boolean b) {
		selected = b;
		if (selected) {
			setStyleName("selectedOrganismUI");
		} else {
			setStyleName("unSelectedOrganismUI");
		}
	}	
}
