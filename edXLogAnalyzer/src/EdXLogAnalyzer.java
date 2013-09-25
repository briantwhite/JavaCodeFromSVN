import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


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
		
		String[] columnHeaders = new String[inFiles.size()];
		for (int i = 0; i < inFiles.size(); i++) {
			columnHeaders[i] = inFiles.get(i).getName().replaceAll(".csv", "");
		}
		
		HashMap<String, int[]>data = new HashMap<String, int[]>();
		for (int i = 0; i < columnHeaders.length; i++) {
			try {
				BufferedReader br = new BufferedReader(new FileReader(inFiles.get(i)));
				String line = null;
				while ((line = br.readLine()) != null) {
					if (line.contains("{")) {
						line = line.replaceAll("\"\"", "\"");
						String[] parts = line.split(",", 2);
						String name = parts[0];
						String json = parts[1];
						
						if (!data.containsKey(name)) {
							data.put(name, new int[columnHeaders.length]);
						}
						
						Gson gson = new GsonBuilder().create();					
					}
				}
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	
}
