package phylogenySurvey;

import java.awt.Point;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public abstract class SelectableLabel extends JLabel {
	
	private boolean selected;
	
	public SelectableLabel(String name, ImageIcon image, String type) {
		super(name, image, SwingConstants.LEFT);
		selected = false;
	}
	
	public SelectableLabel(ImageIcon image) {
		super(image);
		selected = false;
	}
	
	public boolean isSelected() {
		return selected;
	}
	
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	public abstract Point getCenter();
}
