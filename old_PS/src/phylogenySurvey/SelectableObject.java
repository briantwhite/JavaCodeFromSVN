package phylogenySurvey;

import java.awt.Point;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public abstract class SelectableObject extends JLabel {
	
	private boolean selected;
	
	public SelectableObject(ImageIcon image) {
		super(image);
		selected = false;
	}
	
	public SelectableObject(String text, ImageIcon image) {
		super(text, image, SwingConstants.CENTER);
		selected = false;
	}
	
	public SelectableObject(String text) {
		super(text);
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
