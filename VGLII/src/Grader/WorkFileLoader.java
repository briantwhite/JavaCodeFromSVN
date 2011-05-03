package Grader;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import javax.swing.DefaultListModel;

import org.jdom.Document;
import org.jdom.Element;

import VGL.EncryptionTools;
import VGL.VGLII;

public class WorkFileLoader implements Runnable {
	
	private VGLII vglII;
	
	private File currentDirectory;
	private DefaultListModel workFileNames;
	private TreeMap<String, GradingResult> filenamesAndResults;
	
	private boolean keepGoing;
	
	private int progress;
	private String currentFileName;
	
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
			currentFileName = fileName;

			Document doc = 
				EncryptionTools.getInstance().readRSAEncrypted(workFile, vglII.getGradingKey());
			List<Element> els = doc.getRootElement().getChildren();
			String studentAnswerHTML = "";
			String correctAnswerHTML = "";
			Iterator<Element> elIt = els.iterator();
			while (elIt.hasNext()) {
				Element e = elIt.next();
				if (e.getName().equals("StudentAnswer")) studentAnswerHTML = e.getText();
				if (e.getName().equals("CorrectAnswer")) correctAnswerHTML = e.getText();
			}
			filenamesAndResults.put(
					fileName, new GradingResult(studentAnswerHTML, correctAnswerHTML));
			
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
	
	public String getCurrentFileName() {
		return currentFileName;
	}
}
