package edu.umb.jsAipotu.client.evolution;

import java.awt.Color;

import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import edu.umb.jsAipotu.client.preferences.GlobalDefaults;


public class ColorFitnessSpinner extends NumberSpinner {
	
	private JSpinner spinner;
	private Color color;
	private String colorString;
	
	public ColorFitnessSpinner(String colorString) {
		super(new SpinnerNumberModel(5, 0, 10, 1));
		this.colorString = colorString;
		color = GlobalDefaults.colorModel.getColorFromString(colorString);
	}
	
	public JSpinner getSpinner() {
		return spinner;
	}
	
	public Color getColor() {
		return color;
	}
	
	public String getColorString() {
		return colorString;
	}
}
