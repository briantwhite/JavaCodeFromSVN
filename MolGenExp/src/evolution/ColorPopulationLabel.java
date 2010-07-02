package evolution;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import biochem.ColorUtilities;


public class ColorPopulationLabel extends JLabel {
	
	private JLabel label;
	private Color color;
	private String colorString;
	
	public ColorPopulationLabel(String colorString) {
		super("0", JLabel.CENTER);
		this.colorString = colorString;
		color = ColorUtilities.getColorFromString(colorString);
		
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
