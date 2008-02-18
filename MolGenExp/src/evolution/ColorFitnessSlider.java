package evolution;

import java.awt.Color;

import javax.swing.JSlider;

public class ColorFitnessSlider extends JSlider {
	
	private JSlider slider;
	private Color color;
	private String colorString;
	
	public ColorFitnessSlider(String colorString) {
		super(JSlider.HORIZONTAL, 0, 10, 5);
		this.colorString = colorString;
		color = getColorFromString(colorString);
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
	
	private Color getColorFromString(String c) {
		if (c.equals("Blue")) {
			return Color.BLUE;
		}
		
		if (c.equals("Yellow")) {
			return Color.YELLOW;
		}
		
		if (c.equals("Green")) {
			return Color.green;
		}
		
		if (c.equals("Red")) {
			return Color.RED;
		}
		
		if (c.equals("Purple")) {
			return new Color(255,0,255);
		}
		
		if (c.equals("Orange")) {
			return new Color(255,100,0);
		}
		
		if (c.equals("Black")) {
			return Color.BLACK;
		}
		
		return Color.WHITE;
	}
	

}
