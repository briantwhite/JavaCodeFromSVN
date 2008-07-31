package phylogenySurvey;

import java.awt.Color;
import java.awt.Point;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;

public class OrganismLabel extends SelectableLabel {
	
	private String name;
	private ImageIcon image;
	private String type;
	
	public OrganismLabel(String name, ImageIcon image, String type) {
		super(name, image, type);
		this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		this.name = name;
		this.image = image;
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public ImageIcon getImage() {
		return image;
	}

	public String getType() {
		return type;
	}
	
	public Point getCenter() {
		return new Point(getLocation().x + SurveyUI.LABEL_WIDTH/2,
				getLocation().y + SurveyUI.LABEL_HEIGHT/2);
	}

}
