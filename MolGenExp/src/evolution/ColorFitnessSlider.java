package evolution;

import java.awt.Color;

import javax.swing.JSlider;

import biochem.ColorUtilities;


public class ColorFitnessSlider extends JSlider {
	
	private JSlider slider;
	private Color color;
	private String colorString;
	
	public ColorFitnessSlider(String colorString) {
		super(JSlider.HORIZONTAL, 0, 10, 5);
		this.colorString = colorString;
		color = ColorUtilities.getColorFromString(colorString);
		setMajorTickSpacing(1);
		setMinorTickSpacing(1);
		setPaintLabels(true);
		setPaintTicks(true);
		setSnapToTicks(true);
	}
	
	public JSlider getSlider() {
		return slider;
	}
	
	public Color getColor() {
		return color;
	}
	
	public String getColorString() {
		return colorString;
	}
	
}
