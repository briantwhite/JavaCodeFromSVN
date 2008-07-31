package phylogenySurvey;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

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

}
