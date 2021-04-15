package edu.umb.jsAipotu.client.evolution;

import javax.swing.JLabel;

import com.google.gwt.canvas.dom.client.CssColor;

import edu.umb.jsAipotu.client.preferences.GlobalDefaults;


public class ColorPopulationLabel extends JLabel {
	
	private JLabel label;
	private CssColor color;
	private String colorString;
	
	public ColorPopulationLabel(String colorString) {
		super("0", JLabel.CENTER);
		this.colorString = colorString;
		color = GlobalDefaults.colorModel.getColorFromString(colorString);
		
	}
	
	public JLabel getLabel() {
		return label;
	}
	
	public CssColor getColor() {
		return color;
	}
	
	public String getColorString() {
		return colorString;
	}

}
