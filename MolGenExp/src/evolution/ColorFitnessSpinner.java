package evolution;

import java.awt.Color;

import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import biochem.ColorUtilities;


public class ColorFitnessSpinner extends JSpinner {
	
	private JSpinner spinner;
	private Color color;
	private String colorString;
	
	public ColorFitnessSpinner(String colorString) {
		super(new SpinnerNumberModel(5, 0, 10, 1));
		this.colorString = colorString;
		color = ColorUtilities.getColorFromString(colorString);
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
