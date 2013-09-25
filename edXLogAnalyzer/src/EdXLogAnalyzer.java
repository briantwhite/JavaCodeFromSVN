import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class EdXLogAnalyzer {

	/**
	 * args
	 * - folder with files to analyze
	 * 		each in .csv format
	 * - filename for output
	 */
	public static void main(String[] args) {
		
		if (args.length != 2) {
			System.out.println("not enough args");
			return;
		}
		
		File workingFolder = new File(args[0]);
		if ((!workingFolder.exists()) || (!workingFolder.isDirectory())) {
			System.out.println(workingFolder.getAbsolutePath() + " is not a directory or does not exist; aborting");
		}
		
		File outFile = new File(args[1]);
		
		System.out.println("Reading from directory: " + workingFolder.getAbsolutePath());
		System.out.println("Saving to file: " + outFile.getAbsolutePath());
		
		// get all working files
		File[] possibleFiles = workingFolder.listFiles();
		ArrayList<File>inFiles = new ArrayList<File>();
		for (int i = 0; i < possibleFiles.length; i++) {
			if (possibleFiles[i].getName().endsWith(".csv")) {
				inFiles.add(possibleFiles[i]);
			}
		}
		Collections.sort(inFiles, new Comparator<File>() {
			public int compare(File f1, File f2) {
				return f1.getName().compareTo(f2.getName());
			}
		});
		
		for (int i = 0; i < inFiles.size(); i++) {
			System.out.println(inFiles.get(i).getName());
		}
	}
	
	
}
