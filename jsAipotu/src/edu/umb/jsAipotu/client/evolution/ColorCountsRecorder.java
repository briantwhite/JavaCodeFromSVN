package edu.umb.jsAipotu.client.evolution;

import java.util.HashMap;

import com.google.gwt.canvas.dom.client.CssColor;

import edu.umb.jsAipotu.client.preferences.GlobalDefaults;


//keeps the tallies of the number of organisms in the World
//  with each color
public class ColorCountsRecorder {
	
	private static ColorCountsRecorder instance;
	
	private HashMap<CssColor, Integer> colorCountsMap;
	
	private ColorCountsRecorder() {
		colorCountsMap = new HashMap<CssColor, Integer>();
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
	
	public void incrementCount(CssColor c) {
		if (c.equals(GlobalDefaults.DEAD_COLOR)) return;  // don't count the dead ones
		int oldVal = colorCountsMap.get(c);
		colorCountsMap.put(c, oldVal + 1);
	}

	public int getCount(CssColor c) {
		return colorCountsMap.get(c);
	}
}
