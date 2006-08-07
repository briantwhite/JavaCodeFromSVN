import java.io.File;

import javax.swing.filechooser.FileFilter;

public class ArffFileFilter extends FileFilter {
	
	public boolean accept(File file) {
		if (file.getName().endsWith(".arff")) {
			return true;
		} else {
			return false;
		}
	}
	
	public String getDescription() {
		return "Arff files only.";
	}

}
