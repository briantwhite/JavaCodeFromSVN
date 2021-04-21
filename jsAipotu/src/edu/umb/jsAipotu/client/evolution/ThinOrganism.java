package edu.umb.jsAipotu.client.evolution;

import java.util.Random;

import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.FocusPanel;

import edu.umb.jsAipotu.client.molGenExp.MolGenExp;

//class with just DNA and color - so it's smaller
//for use with evolution
//don't bother with pictures unless needed
public class ThinOrganism extends FocusPanel {
	private String dna1;
	private String dna2;
	private CssColor color1;
	private CssColor color2;
	private CssColor overallColor;
	private MolGenExp mge;
	private boolean selected;

	protected ThinOrganism(String dna1, String dna2, 
			CssColor color1, CssColor color2, 
			CssColor overallColor, MolGenExp mge) {
		this.dna1 = dna1;
		this.dna2 = dna2;
		this.color1 = color1;
		this.color2 = color2;
		this.overallColor = overallColor;
		this.mge = mge;
		this.selected = false;
		setStyleName("unselected-thinOrganism");
		this.getElement().getStyle().setBackgroundColor(overallColor.toString());
		sinkEvents(Event.ONCLICK);
	}
	
	// empty constructor for starting screen
	protected ThinOrganism() {
		this.setStyleName("unselected-thinOrganism");
		this.getElement().getStyle().setBackgroundColor("lightgray");
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

	public String getDNA1() {
		return dna1;
	}

	public String getDNA2() {
		return dna2;
	}
	
	public CssColor getColor1() {
		return color1;
	}
	
	public CssColor getColor2() {
		return color2;
	}

	public CssColor getOverallColor() {
		return overallColor;
	}
	
	public boolean isSelected() {
		return selected;
	}
		
	public void setSelected(boolean b) {
		selected = b;
		if (selected) {
			setStyleName("selected-thinOrganism");
		} else {
			setStyleName("unselected-thinOrganism");
		}
	}	

	public String getRandomDNASequence() {
		Random r = new Random();
		if (r.nextDouble() > 0.5) {
			return dna1;
		} else {
			return dna2;
		}
	}
}
