package Grader;

import java.io.File;
import java.util.TreeMap;

import javax.swing.DefaultListModel;

import GeneticModels.GeneticModelFactory;
import VGL.GeneticModelAndCageSet;

public class WorkFileLoader implements Runnable {
	
	private File currentDirectory;
	private DefaultListModel workFileNames;
	private TreeMap<String, GeneticModelAndCageSet> filenamesAndResults;
	
	private boolean keepGoing;
	
	private int progress;
	
	public WorkFileLoader(
			File currentDirectory,
			DefaultListModel workFileNames, 
			TreeMap<String, GeneticModelAndCageSet> filenamesAndResults) {
		this.currentDirectory = currentDirectory;
		this.workFileNames = workFileNames;
		this.filenamesAndResults = filenamesAndResults;
		keepGoing = true;
		progress = 0;
	}

	public void run() {
		for (int i = 0; i < workFileNames.getSize(); i++) {
			if (!keepGoing) return;
			String fileName = (workFileNames.get(i)).toString();
			File workFile = new File(
					currentDirectory.getAbsolutePath() 
					+ System.getProperty("file.separator") 
					+ fileName);
			filenamesAndResults.put(
					fileName, 
					GeneticModelFactory.getInstance().readModelFromFile(workFile));
			progress++;
		}
	}
	
	public void stop() {
		keepGoing = false;
	}
	
	public int getLengthOfTask() {
		return workFileNames.getSize();
	}

	public int getProgress() {
		return progress;
	}
}
