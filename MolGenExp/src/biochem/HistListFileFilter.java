package biochem;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class HistListFileFilter extends FileFilter {
	
	public boolean accept(File file) {
		if (file.getName().endsWith(".histlist")) {
			return true;
		} else {
			return false;
		}
	}
	
	public String getDescription() {
		return "History List (.histlist) files only.";
	}
	
}
