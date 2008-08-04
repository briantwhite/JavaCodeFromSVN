package phylogenySurvey;

import java.awt.Color;
import java.awt.Point;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;

public class OrganismLabel extends SelectableLinkableObject {
	
	private String type;
	
	public OrganismLabel(String name, ImageIcon image, String type) {
		super(name, image);
		this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		this.type = type;
	}

	public String getType() {
		return type;
	}
	
	public Point getCenter() {
		return new Point(getLocation().x + SurveyUI.LABEL_WIDTH/2,
				getLocation().y + SurveyUI.LABEL_HEIGHT/2);
	}

}
