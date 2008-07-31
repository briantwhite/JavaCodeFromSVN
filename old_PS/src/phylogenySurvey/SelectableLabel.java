package phylogenySurvey;

import java.awt.Point;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public abstract class SelectableLabel extends JLabel {
	
	private boolean selected;
	private Point location;
	private Point adjustment;
	
	public SelectableLabel(String name, ImageIcon image, String type) {
		super(name, image, SwingConstants.LEFT);
	}
	
	public boolean isSelected() {
		return selected;
	}
	
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	public Point getLocation() {
		return location;
	}
	
	public void setLocation(Point location) {
		this.location = location;
	}
	
	public Point getAdjustment() {
		return adjustment;
	}
	
	public void setAdjustment(Point adjustment) {
		this.adjustment = adjustment;
	}
}
