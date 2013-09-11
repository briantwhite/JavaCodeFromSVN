import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;


public class ScoreSaver {

	private String workingDir;

	private static String SCORE_FILE_NAME = "scores.txt";

	private TreeMap<String, Integer> namesAndScoresFromScoresTxt;

	public ScoreSaver(String workingDir) {
		this.workingDir = workingDir;
		namesAndScoresFromScoresTxt = null;
		loadFile();
	}

	private void loadFile() {

		File studentFile = new File(workingDir 
				+ System.getProperty("file.separator") 
				+ SCORE_FILE_NAME);
		
		namesAndScoresFromScoresTxt = new TreeMap<String, Integer>();

		if (studentFile.exists()) {
			BufferedReader reader = null;
			String text = null;
			try {
				reader = new BufferedReader(new FileReader(studentFile));
				while ((text = reader.readLine()) != null) {
					if (text.contains("\t")) {
						String[] parts = text.split("\t");
						String name = parts[0].replaceAll("\"","");
						if (parts.length == 2) {
							namesAndScoresFromScoresTxt.put(name, Integer.parseInt(parts[1]));
						} else {
							namesAndScoresFromScoresTxt.put(name, null);
						}
					}
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (reader != null) {
						reader.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/*
	 * deals with additions to the class list (names added to Roster.txt)
	 * since the last time this was run.  You need to add the new students
	 * to the TreeMap of names and scores.
	 * 
	 * Null score means never called yet
	 */
	public TreeMap<String, Integer> reconcileNamesAndScores(Set<String>allStudentNames) {
		TreeMap<String, Integer> allNamesAndScores = new TreeMap<String, Integer>();
		for (String name : allStudentNames) {
			if (allStudentNames.contains(name)) {
				allNamesAndScores.put(name, namesAndScoresFromScoresTxt.get(name));
			} else {
				allNamesAndScores.put(name, null);
			}
		}
		return allNamesAndScores;
	}

	public void saveScores(TreeMap<String, Integer> namesAndScores) {
		PrintWriter outWriter = null;
		try {
			outWriter = new PrintWriter(new FileWriter(workingDir 
					+ System.getProperty("file.separator") 
					+ SCORE_FILE_NAME));
		} catch (IOException e) {
			e.printStackTrace();
		}

		Iterator<String> nameIt = namesAndScores.keySet().iterator();
		while (nameIt.hasNext()) {
			String name = nameIt.next();
			if (namesAndScores.get(name) != null) {
				outWriter.println("\"" + name + "\"" + "\t" + namesAndScores.get(name));
			} else {
				outWriter.println("\"" + name + "\"" + "\t");
			}
		}
		outWriter.close();
	}

}
