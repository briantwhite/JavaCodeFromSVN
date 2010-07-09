package evolution;

import java.awt.Color;
import java.util.HashMap;

import preferences.GlobalDefaults;


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
	
	public void setAllToZero() {
		for (int i = 0; i < GlobalDefaults.colorList.length; i++) {
			colorCountsMap.put(GlobalDefaults.colorModel.getColorFromString(
					GlobalDefaults.colorList[i]), 0);
		}
	}
	
	public void incrementCount(Color c) {
		if (c.equals(GlobalDefaults.DEAD_COLOR)) return;  // don't count the dead ones
		int oldVal = colorCountsMap.get(c);
		colorCountsMap.put(c, oldVal + 1);
	}

	public int getCount(Color c) {
		return colorCountsMap.get(c);
	}
}
