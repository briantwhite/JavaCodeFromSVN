package jsVGLReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.TreeMap;

import javax.swing.DefaultListModel;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;



public class WorkFileLoader implements Runnable {

	private File currentDirectory;
	private DefaultListModel<String> workFileNameListModel;
	private TreeMap<String, GradingResult> filenamesAndResults;

	private boolean keepGoing;

	private int progress;
	private String currentFileName;

	public WorkFileLoader(
			File currentDirectory,
			DefaultListModel<String> workFileNameListModel, 
			TreeMap<String, GradingResult> filenamesAndResults) {
		this.currentDirectory = currentDirectory;
		this.workFileNameListModel = workFileNameListModel;
		this.filenamesAndResults = filenamesAndResults;
		keepGoing = true;
		progress = 0;
	}

	public void run() {
		for (int i = 0; i < workFileNameListModel.getSize(); i++) {
			if (!keepGoing) return;
			String fileName = (workFileNameListModel.get(i));
			File workFile = new File(
					currentDirectory.getAbsolutePath() 
					+ System.getProperty("file.separator") 
					+ fileName);
			currentFileName = fileName;

			String studentAnswerHTML = "Could not read this file.";
			String correctAnswerHTML = "Cannot be graded.";
		
			JSONParser parser = new JSONParser();
			try {
				String rawText = new String(Files.readAllBytes(workFile.toPath()));
				String decodedText = new String(Base64.getDecoder().decode(rawText));
				Object obj = parser.parse(decodedText);
				JSONObject jsonObj = (JSONObject)obj;
				JSONObject vglJSON = (JSONObject)jsonObj.get("VglII");
				JSONObject summaryJSON = (JSONObject)vglJSON.get("Summary");
				studentAnswerHTML = (String)summaryJSON.get("_StudentAnswer");
				correctAnswerHTML = (String)summaryJSON.get("_CorrectAnswer");
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			filenamesAndResults.put(fileName, new GradingResult(studentAnswerHTML, correctAnswerHTML));
			
			progress++;
		}
	}

	public void stop() {
		keepGoing = false;
	}

	public int getLengthOfTask() {
		return workFileNameListModel.getSize();
	}

	public int getProgress() {
		return progress;
	}

	public String getCurrentFileName() {
		return currentFileName;
	}
}
