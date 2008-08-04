package phylogenySurvey;

import java.awt.Point;

import javax.swing.ImageIcon;

public class Node extends SelectableLinkableObject {
	
	public static int counter = 0;
	
	private int id;

	public Node(ImageIcon image) {
		super(image);
		id = counter;
		counter++;
	}
	
	public Point getCenter() {
		return new Point(getLocation().x + 5,
				getLocation().y + 5);
	}
	
	public int getID() {
		return id;
	}

}
