package edu.umb.jsAipotu.client.evolution;

import com.google.gwt.canvas.dom.client.CssColor;

import edu.umb.jsAipotu.client.preferences.GlobalDefaults;


public class ColorFitnessSpinner extends NumberSpinner {
	
	private CssColor color;
	private String colorString;
	
	public ColorFitnessSpinner(FitnessSettingsPanel fsp, String colorString) {
		super(fsp);
		this.colorString = colorString;
		color = GlobalDefaults.colorModel.getColorFromString(colorString);
	}
		
	public CssColor getColor() {
		return color;
	}
	
	public String getColorString() {
		return colorString;
	}
	
	public String getColorHTML() {
		return colorString;
	}
}
