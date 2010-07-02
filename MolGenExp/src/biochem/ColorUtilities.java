package biochem;

import java.awt.Color;

public class ColorUtilities {
	
	public static Color getColorFromString(String c) {
		if (c.equals("Blue")) {
			return Color.BLUE;
		}
		
		if (c.equals("Yellow")) {
			return Color.YELLOW;
		}
		
		if (c.equals("Green")) {
			return Color.green;
		}
		
		if (c.equals("Red")) {
			return Color.RED;
		}
		
		if (c.equals("Purple")) {
			return new Color(255,0,255);
		}
		
		if (c.equals("Orange")) {
			return new Color(255,100,0);
		}
		
		if (c.equals("Black")) {
			return Color.BLACK;
		}
		
		return Color.WHITE;
	}

}
