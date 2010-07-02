package evolution;

import java.awt.Color;
import java.util.HashMap;

import preferences.GlobalDefaults;

import biochem.ColorUtilities;


//keeps the tallies of the number of organisms in the World
//  with each color
public class ColorCountsRecorder {
	
	private static ColorCountsRecorder instance;
	
	private HashMap<Color, Integer> colorCountsMap;
	
	private ColorCountsRecorder() {
		colorCountsMap = new HashMap<Color, Integer>();
		setAllToZero();
	}
	
	public static ColorCountsRecorder getInstance() {
		if (instance == null) {
			instance = new ColorCountsRecorder();
		}
		return instance;
	}
	
	public synchronized void setAllToZero() {
		for (int i = 0; i < GlobalDefaults.colorList.length; i++) {
			colorCountsMap.put(ColorUtilities.getColorFromString(
					GlobalDefaults.colorList[i]), 0);
		}
	}
	
	public synchronized void incrementCount(Color c) {
		int oldVal = colorCountsMap.get(c);
		colorCountsMap.put(c, oldVal + 1);
	}

	public synchronized int getCount(Color c) {
		return colorCountsMap.get(c);
	}
}
