package molGenExp;

import java.util.HashMap;

import javax.swing.ImageIcon;

public class IconArchive {
	
	private static IconArchive instance;
	
	private HashMap<String, ImageIcon> iconArchive;
	
	private IconArchive() {
		iconArchive = new HashMap<String, ImageIcon>();
	}
	
	public IconArchive getInstance() {
		if (instance == null) {
			instance = new IconArchive();
		}
		return instance;
	}
	
	public synchronized void put(String color, ImageIcon image) {
		iconArchive.put(color, image);
	}
	
	public synchronized boolean containsColor(String color) {
		return iconArchive.containsKey(color);
	}

}
