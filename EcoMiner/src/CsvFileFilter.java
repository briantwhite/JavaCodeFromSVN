import java.io.File;

import javax.swing.filechooser.FileFilter;

public class CsvFileFilter extends FileFilter {
	
	public boolean accept(File file) {
		if (file.getName().endsWith(".csv")) {
			return true;
		} else {
			return false;
		}
	}
	
	public String getDescription() {
		return "csv files only.";
	}

}
