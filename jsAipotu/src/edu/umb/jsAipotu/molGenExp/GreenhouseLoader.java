package edu.umb.jsAipotu.molGenExp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class GreenhouseLoader implements Runnable {

	private File greenhouseDir;
	private ArrayList<String> organismFiles;
	private Greenhouse greenhouse;
	private int i;
	private OrganismFactory organismFactory;

	public GreenhouseLoader(File greenhouseDir, Greenhouse greenhouse) {
		this.greenhouseDir = greenhouseDir;
		this.greenhouse = greenhouse;
		String[] files = greenhouseDir.list();
		organismFiles = new ArrayList<String>();
		for (int counter = 0; counter < files.length; counter++) {
			String fileName = files[counter];
			if (fileName.endsWith(".organism")) {
				organismFiles.add(fileName);
			}
		}
		organismFactory = new OrganismFactory();
	}

	public int getLengthOfTask() {
		return organismFiles.size();
	}

	public int getCurrent() {
		return i;
	}

	public void stop() {
		i = organismFiles.size();
	}

	boolean done() {
		if (i >= organismFiles.size()) {
			return true;
		} else {
			return false;
		}
	}


	public void run() {
		for (i = 0; i < organismFiles.size(); i++){
			String fileString = (String)organismFiles.get(i);

			ArrayList<String> geneSequences = new ArrayList<String>();

			String organismName = 
					fileString.replaceAll(".organism", "");
			String orgFileName = greenhouseDir.toString() 
					+ System.getProperty("file.separator") 
					+ fileString;

			BufferedReader input = null;
			try {
				input = new BufferedReader(new FileReader(orgFileName));
				String line = null;
				while ((line = input.readLine()) != null) {
					Pattern p = Pattern.compile("[^AGCT]+");
					if (!p.matcher(line).find()) {
						geneSequences.add(line);
					} 
				}

				// be sure there are only 2 DNA sequences in the organism
				if (geneSequences.size() == 2) {
					greenhouse.add(
							organismFactory.createOrganism(
									organismName, 
									geneSequences.get(0),
									geneSequences.get(1)));
				}
				input.close();
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
			finally {
				try {
					if (input!= null) {
						input.close();
					}
				}
				catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}
	}

}
