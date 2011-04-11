package Grader;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.swing.DefaultListModel;

import GeneticModels.Cage;
import GeneticModels.GeneticModelFactory;
import ModelBuilder.ModelBuilderUI;
import VGL.GeneticModelAndCageSet;
import VGL.VGLII;

public class WorkFileLoader implements Runnable {
	
	private VGLII vglII;
	
	private File currentDirectory;
	private DefaultListModel workFileNames;
	private TreeMap<String, GradingResult> filenamesAndResults;
	
	private boolean keepGoing;
	
	private int progress;
	
	public WorkFileLoader(
			File currentDirectory,
			DefaultListModel workFileNames, 
			TreeMap<String, GradingResult> filenamesAndResults,
			VGLII vglII) {
		this.currentDirectory = currentDirectory;
		this.workFileNames = workFileNames;
		this.filenamesAndResults = filenamesAndResults;
		this.vglII = vglII;
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
			GeneticModelAndCageSet gmcs = 
				GeneticModelFactory.getInstance().readModelFromFile(workFile);
			
			String correctAnswerHTML = gmcs.getGeneticModel().getHTMLForGrader();
			
			ModelBuilderUI mbui = new ModelBuilderUI(vglII, gmcs);
			mbui.configureFromFile(gmcs.getModelBuilderState());
			String studentAnswerHTML = mbui.getAsHtml(true) + getCageScores(gmcs.getCages(), mbui);
			
			filenamesAndResults.put(
					fileName, new GradingResult(studentAnswerHTML, correctAnswerHTML));
			
			progress++;
		}
	}
	
	private String getCageScores(ArrayList<Cage> cages, ModelBuilderUI mbui) {
		StringBuffer b = new StringBuffer();
		TreeSet<Integer> selectedCages = 
			mbui.getChosenRelevantCages();
		b.append("<hr>");
		b.append("<b>Selected Cages:</b><br>");
		
		if(selectedCages.size() == 0) {
			b.append("<b>No cages were selected.</b>");
		} else {
			Iterator<Integer> cageNumIt = selectedCages.iterator();
			while (cageNumIt.hasNext()) {
				int cageNum = cageNumIt.next();
				b.append(CageScorer.scoreCage(cages.get(cageNum - 1)));
			}
			
		}
		
		b.append("</ul>");
		return b.toString();
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
