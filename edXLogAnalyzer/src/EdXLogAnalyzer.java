import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;


public class EdXLogAnalyzer {

	/**
	 * arg
	 * - folder with files to analyze
	 * 		each in .csv format
	 * 
	 */
	public static void main(String[] args) {

		if (args.length != 1) {
			System.out.println("not enough args");
			return;
		}

		File workingFolder = new File(args[0]);
		if ((!workingFolder.exists()) || (!workingFolder.isDirectory())) {
			System.out.println(workingFolder.getAbsolutePath() + " is not a directory or does not exist; aborting");
		}

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

		/*
		 * read in all the data into monster hash<by name> of arrays[indexed by column #]
		 * 
		 * if you find a real value, enter it
		 * 	 if it's blank or -1, save -1 as flag for missing/bad data
		 */
		HashMap<String, double[]> data = new HashMap<String, double[]>();
		for (int i = 0; i < columnHeaders.length; i++) {
			try {
				BufferedReader br = new BufferedReader(new FileReader(inFiles.get(i)));
				String line = null;
				while ((line = br.readLine()) != null) {
					if (line.contains("{")) {
						line = line.replaceAll("\"\"", "\"");
						String[] parts = line.split(",", 2);
						String name = parts[0];
						String json = parts[1].substring(1, parts[1].length() - 1);		// remove leading and trailing quotes

						if (!data.containsKey(name)) {
							double[] x = new double[columnHeaders.length];
							for (int j = 0; j < x.length; j++) {
								x[j] = -1.0f;
							}
							data.put(name, x);
						}

						Map<String, Object> jsonJavaRootObject = new Gson().fromJson(json, Map.class);	
						Set<String> keys = jsonJavaRootObject.keySet();
						double reportedTime = -1.0f;
						if (keys.contains("student_answers")) {
							LinkedTreeMap<String, String> answer = (LinkedTreeMap<String, String>)jsonJavaRootObject.get("student_answers");
							if (answer.keySet().size() > 0) {
								String probName = answer.keySet().iterator().next();
								try {
									reportedTime = Double.parseDouble(answer.get(probName).trim());
								} catch (NumberFormatException e) {}	// if unreadable, give -1
							}
						}
						data.get(name)[i] = reportedTime;
					}
				}
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		// print and summarize data
		System.out.print("Name,");
		for (int i = 0; i < columnHeaders.length; i++) {
			System.out.print(columnHeaders[i] + ",");
		}
		System.out.print("\n");
		
		DataPoint[] summaries = new DataPoint[columnHeaders.length];
		for (int i = 0; i < columnHeaders.length; i++) {
			summaries[i] = new DataPoint();
		}
		
		Set<String> names = data.keySet();
		Iterator<String> nameIt = names.iterator();
		while (nameIt.hasNext()) {
			String name = nameIt.next();
			double[] values = data.get(name);
			System.out.print(name + ",");
			for (int i = 0; i < values.length; i++) {
				double value = values[i];
				System.out.print(value + ",");
				if (value == -1.0f) {
					summaries[i].numBad++;
				} else {
					summaries[i].numValid++;
					summaries[i].totalValid += value;
				}
			}
			System.out.print("\n");	
		}
		
		System.out.print("TotalBad,");
		for (int i = 0; i < columnHeaders.length; i++) {
			System.out.print(summaries[i].numBad + ",");
		}
		System.out.print("\n");

		System.out.print("TotalValid,");
		for (int i = 0; i < columnHeaders.length; i++) {
			System.out.print(summaries[i].numValid + ",");
		}
		System.out.print("\n");

		System.out.print("AverageValid,");
		for (int i = 0; i < columnHeaders.length; i++) {
			System.out.print(summaries[i].getAverageValid() + ",");
		}
		System.out.print("\n");

	}


}
