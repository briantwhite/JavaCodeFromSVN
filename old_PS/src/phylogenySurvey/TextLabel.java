package phylogenySurvey;

import java.awt.Point;

public class TextLabel extends SelectableObject {
	
	private static int counter = 0;
	
	private int id;

	public TextLabel(String text) {
		super(text);
		id = counter;
		counter++;
	}

	public Point getCenter() {
		return null;
	}

}
