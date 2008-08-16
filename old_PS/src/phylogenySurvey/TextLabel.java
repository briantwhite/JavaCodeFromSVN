package phylogenySurvey;

import java.awt.Point;

import org.jdom.Element;

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

	public Element save() {
		Element e = new Element("TextLabel");
		e.setAttribute("Id", String.valueOf(id));
		e.setAttribute("Text", text);
		e.setAttribute("X", String.valueOf(getLocation().x));
		e.setAttribute("Y", String.valueOf(getLocation().y));
		return e;
	}

}
