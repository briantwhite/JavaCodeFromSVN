package ProblemFileReader;
import java.io.File;
import java.util.TreeMap;

import javax.swing.DefaultListModel;

import GeneticModels.GeneticModel;
import GeneticModels.GeneticModelFactory;


public class WorkFileLoader implements Runnable {
	
	private File currentDirectory;
	private DefaultListModel workFileNames;
	private TreeMap<String, GeneticModel> namesAndModels;
	
	private boolean keepGoing;
	
	private int progress;
	
	public WorkFileLoader(
			File currentDirectory,
			DefaultListModel workFileNames, 
			TreeMap<String, GeneticModel> namesAndModels) {
		this.currentDirectory = currentDirectory;
		this.workFileNames = workFileNames;
		this.namesAndModels = namesAndModels;
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
			GeneticModel geneticModel = 
				(GeneticModelFactory.getInstance().readModelFromFile(workFile)).getGeneticModel();
			namesAndModels.put(fileName, geneticModel);
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
