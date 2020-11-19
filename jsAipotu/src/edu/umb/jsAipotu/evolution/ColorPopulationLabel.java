package edu.umb.jsAipotu.evolution;

import java.awt.Color;

import javax.swing.JLabel;

import preferences.GlobalDefaults;


public class ColorPopulationLabel extends JLabel {
	
	private JLabel label;
	private Color color;
	private String colorString;
	
	public ColorPopulationLabel(String colorString) {
		super("0", JLabel.CENTER);
		this.colorString = colorString;
		color = GlobalDefaults.colorModel.getColorFromString(colorString);
		
	}
	
	public JLabel getLabel() {
		return label;
	}
	
	public Color getColor() {
		return color;
	}
	
	public String getColorString() {
		return colorString;
	}

}
