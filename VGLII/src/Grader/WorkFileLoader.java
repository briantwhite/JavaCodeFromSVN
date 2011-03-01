package Grader;

import java.io.File;
import java.util.TreeMap;

import javax.swing.DefaultListModel;

import GeneticModels.GeneticModelFactory;
import VGL.GeneticModelAndCageSet;

public class WorkFileLoader implements Runnable {
	
	private File currentDirectory;
	private DefaultListModel workFileNames;
	private TreeMap<String, GradedResult> filenamesAndResults;
	
	private boolean keepGoing;
	
	private int progress;
	
	public WorkFileLoader(
			File currentDirectory,
			DefaultListModel workFileNames, 
			TreeMap<String, GradedResult> filenamesAndResults) {
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
			GeneticModelAndCageSet set = 
				GeneticModelFactory.getInstance().readModelFromFile(workFile);
			filenamesAndResults.put(fileName, 
					new GradedResult(set.getGeneticModel(), 
							set.getModelBuilderState()));
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
