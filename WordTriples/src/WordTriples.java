import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import cern.colt.matrix.impl.SparseDoubleMatrix3D;


public class WordTriples extends JFrame {

	JTabbedPane steps;
	
	SelectHypFileUI selectHypFileUI;
	File hypFile;
	ArrayList hypotheses;
	ShowLoadedHypsUI showLoadedHypsUI;
	ShowWordsUI showWordsUI;
	HashMap wordsAndCounts;
	ScoreHypsByWordsUI scoreHypsByWordsUI;
	SaveSingleWordAsArffUI saveSingleWordAsArffUI;
	CalculateWordDoublesUI calculateWordDoublesUI;
	ShowWordDoublesUI showWordDoublesUI;
	int numCodes;
	TreeMap wordPairHistogram;
	HashMap wordCodeMap;
	int[][] pairs;
	SaveWordPairsAsArffUI saveWordPairsAsArffUI;
	CalculateWordTriplesUI calculateWordTriplesUI;
	SparseDoubleMatrix3D triples;
	ShowWordTriplesUI showWordTriplesUI;
	TreeMap wordTripleHistogram;
	SaveWordTriplesAsArffUI saveWordTriplesAsArffUI;
			
	public WordTriples () {
		super("Word Triples Analyzer");
				
		addWindowListener(new ApplicationCloser());
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());
				
		selectHypFileUI = new SelectHypFileUI();
		hypFile = null;
		
		hypotheses = null;
		wordsAndCounts = null;
		
		showLoadedHypsUI = new ShowLoadedHypsUI();
		showWordsUI = new ShowWordsUI();
		scoreHypsByWordsUI = new ScoreHypsByWordsUI();
		saveSingleWordAsArffUI = new SaveSingleWordAsArffUI();
		calculateWordDoublesUI = new CalculateWordDoublesUI();
		showWordDoublesUI = new ShowWordDoublesUI();
		saveWordPairsAsArffUI = new SaveWordPairsAsArffUI();
		calculateWordTriplesUI = new CalculateWordTriplesUI();
		showWordTriplesUI = new ShowWordTriplesUI();
		saveWordTriplesAsArffUI = new SaveWordTriplesAsArffUI();
		
		steps = new JTabbedPane();
		steps.addTab("(0)Select Input File", selectHypFileUI);
		steps.addTab("(1)Hypotheses found in Input File", showLoadedHypsUI);
		steps.addTab("(2)Form word groups", showWordsUI);
		steps.addTab("(3)Score hyps by single words", scoreHypsByWordsUI);
		steps.addTab("(4)Save single-word scores as ARFF", saveSingleWordAsArffUI);
		steps.addTab("(5)Calculate Word Pairs", calculateWordDoublesUI);
		steps.addTab("(6)Show word pair results", showWordDoublesUI);
		steps.addTab("(7)Save word-pair scores as ARFF", saveWordPairsAsArffUI);
		steps.addTab("(8)Calculate Word Triples", calculateWordTriplesUI);
		steps.addTab("(9)Show word triple results", showWordTriplesUI);
		steps.addTab("(10)Save word-triple scores as ARFF", saveWordTriplesAsArffUI);
		
		contentPane.add(steps, BorderLayout.CENTER);
		
		steps.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				switch (steps.getSelectedIndex()) {
				case 0:
					break;
				
				case 1:
					loadAndShowHyps();
					break;
				
				case 2:
					showAndEditWords();
					break;
					
				case 3:
					scoreBySingleWords();
					break;
				
				case 4:
					saveSinglesAsArff();
					break;
				
				case 5:
					calculateWordPairs();
					break;
				
				case 6:
					showWordPairs();
					
				case 7:
					savePairsAsArff();
					break;
					
				case 8:
					calculateWordTriples();
					break;
					
				case 9:
					showWordTriples();
					break;
					
				case 10:
					saveTriplesAsArff();
					break;
				}
			}
		});
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		WordTriples wordTriples = new WordTriples();
		wordTriples.setSize(1000,600);
		wordTriples.show();

	}

	
	JPanel makeLoadHypsPane() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(new JLabel("hi there"), BorderLayout.NORTH);
		
		return panel;
	}
	
	private void loadAndShowHyps() {
		if (selectHypFileUI.getSelectedHypFile() != null) {
			hypotheses = selectHypFileUI.getLoadedHyps();
			Iterator hypothesisIterator = hypotheses.iterator();
			showLoadedHypsUI.createTable(hypotheses.size());
			int rowNumber = 0;
			while (hypothesisIterator.hasNext()){
				Hypothesis hypothesis = (Hypothesis)hypothesisIterator.next();
					showLoadedHypsUI.addHyp(rowNumber,
					hypothesis.getNumber(), 
					hypothesis.getHypothesisText(),
					hypothesis.getScore());
				rowNumber++;
			}
			showLoadedHypsUI.setInfoLabelText(
					"You selected "
					+ selectHypFileUI.getSelectedHypFile().getName().toString() 
					+ " as the input file."
					+ " I found "
					+ hypotheses.size()
					+ " hypotheses.");

		} else {
			showLoadedHypsUI.setInfoLabelText(
					"No hypothesis file selected.");
		}
	}
	
	private void showAndEditWords() {
		if (hypotheses != null) {
			Iterator hypothesisIterator = hypotheses.iterator();
			wordsAndCounts = new HashMap();
			while(hypothesisIterator.hasNext()){
				Hypothesis hypothesis = (Hypothesis)hypothesisIterator.next();
				ArrayList wordSet = hypothesis.getWordSet();
				Iterator wordIterator = wordSet.iterator();
				while (wordIterator.hasNext()) {
					String wordText = (String)wordIterator.next();
					if (wordsAndCounts.containsKey(wordText)){
						int oldCount = 
							((Integer)wordsAndCounts.get(wordText)).intValue();
						wordsAndCounts.put(wordText, new Integer(oldCount + 1));
					} else {
						wordsAndCounts.put(wordText, new Integer(1));
					}
				}
			}
			Iterator wordListIterator = wordsAndCounts.keySet().iterator();
			showWordsUI.createTable(wordsAndCounts.size());
			int rowNumber = 0;
			while (wordListIterator.hasNext()) {
				String wordText = (String)wordListIterator.next();
				int count = ((Integer)wordsAndCounts.get(wordText)).intValue();
				showWordsUI.addWord(rowNumber, wordText, count, "", 0);
				rowNumber++;
			}
			showWordsUI.setInfoLabelText("I found " + rowNumber	+ " words.");

		} else {
			showWordsUI.setInfoLabelText("No hypotheses loaded.");
			ArrayList allWords = getWordListFromFile();
			
			showWordsUI.createTable(allWords.size());
			for (int i = 0; i < allWords.size(); i++) {
				Word word = (Word)allWords.get(i);
				showWordsUI.addWord(i,
						word.getText(),
						word.getCount(),
						word.getGroup(),
						word.getCode());
			}
			showWordsUI.setInfoLabelText("I found " + allWords.size()
					+ " words.");
			
		}
	}
	
	public ArrayList getHypsFromFile() {
		ArrayList hypotheses = new ArrayList();
		JFileChooser hypFileChooser = new JFileChooser();
		hypFileChooser.setDialogTitle("Select a hypotheses file");
		if (hypFileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			File hypFile = hypFileChooser.getSelectedFile();
			URL hypURL = null;
			
			try {
				hypURL = hypFile.toURL();
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
			}
			StringBuffer hypListBuffer = new StringBuffer();
			InputStream hypInput = null;
			try {
				hypInput = hypURL.openStream();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			BufferedReader hypListStream = 
				new BufferedReader(new InputStreamReader(hypInput));
			String line = null;
			
			try {
				while ((line = hypListStream.readLine()) != null ){
					try {
						hypotheses.add(new Hypothesis(line));
					} catch (BadHypothesisStringException e) {
						
					}
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return hypotheses;
	}
		
	public ArrayList getWordListFromFile(){
		JFileChooser openFileChooser = new JFileChooser();
		openFileChooser.setDialogTitle("Select a word list file");
		ArrayList allWords = new ArrayList();
		if (openFileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			BufferedReader infile = null;
			String line;
			try {
				infile = new BufferedReader(new FileReader(
						openFileChooser.getSelectedFile()));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			
			try {
				while ((line = infile.readLine()) != null) {
					String[] lineParts = line.split("\t");
					allWords.add(new Word(lineParts[0],
							Integer.parseInt(lineParts[1]),
							lineParts[2],
							Integer.parseInt(lineParts[3])));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return allWords;
	}

	private void scoreBySingleWords() {
		if (hypotheses == null) {
			scoreHypsByWordsUI.setInfoLabelText("No hyps loaded!");
			hypotheses = getHypsFromFile();
		}
		if (showWordsUI.getWordListTableModel() == null) {
			scoreHypsByWordsUI.setInfoLabelText("No word list loaded!");
			ArrayList allWords = getWordListFromFile();
			showWordsUI.createTable(allWords.size());
			for (int i = 0; i < allWords.size(); i++) {
				Word word = (Word)allWords.get(i);
				showWordsUI.addWord(i,
						word.getText(),
						word.getCount(),
						word.getGroup(),
						word.getCode());
			}
			showWordsUI.setInfoLabelText("I found " + allWords.size()
					+ " words.");
		} 
		//load the scoring table as a HashMap
		Object[][] wordListTable = 
			showWordsUI.getWordListTableModel().getAllWords();
		int wordCount = showWordsUI.getWordListTableModel().getRowCount();
		wordCodeMap = new HashMap();
		for (int i = 0; i < wordCount; i++) {
			if (((Integer)wordListTable[i][3]).intValue() != 0) {
				wordCodeMap.put(wordListTable[i][0], wordListTable[i][3]);
			}
		}
		//make list of words
		String[] words = new String[wordCodeMap.size() + 1];
		Iterator wordIterator = wordCodeMap.keySet().iterator();
		while (wordIterator.hasNext()){
			String currentWord = (String)wordIterator.next();
			int code = ((Integer)wordCodeMap.get(currentWord)).intValue();
			words[code] = currentWord;
		}
		//score the hyps and display
		scoreHypsByWordsUI.createTable(hypotheses.size(), words);
		for (int i = 0; i < hypotheses.size(); i++) {
			Hypothesis hypothesis = (Hypothesis)hypotheses.get(i);
			int[] singleWordScores = hypothesis.scoreBySingleWords(wordCodeMap);
			scoreHypsByWordsUI.addHyp(i, hypothesis.getHypothesisText(),
					singleWordScores);
		}
		
		scoreHypsByWordsUI.setInfoLabelText("I scored " + hypotheses.size() 
				+ " hypotheses with " + wordCodeMap.size() + " words.");
		
	}
	
	public void saveSinglesAsArff() {
		if (scoreHypsByWordsUI.getScoresTableModel() != null) {
			// tally up all the possible scores
			Iterator hypIterator = hypotheses.iterator();
			TreeMap scoreMap = new TreeMap();
			TreeMap scoreCounts = new TreeMap();
			while (hypIterator.hasNext()){
				Hypothesis hypothesis = (Hypothesis)hypIterator.next();
				Integer score = new Integer(hypothesis.getScore());
				scoreMap.put(score, score);
				if (!scoreCounts.containsKey(score)){
					scoreCounts.put(score, new Integer(1));
				} else {
					int oldCount = ((Integer)scoreCounts.get(score)).intValue();
					scoreCounts.put(score, new Integer(oldCount + 1));
				}
			}
			
			saveSingleWordAsArffUI.createTable(scoreMap.size());
			Iterator scoreIterator = scoreMap.keySet().iterator();
			int rowNumber = 0;
			while (scoreIterator.hasNext()){
				Integer currentScore = (Integer)scoreIterator.next();
				saveSingleWordAsArffUI.addScore(rowNumber, 
						currentScore.intValue(),
						((Integer)scoreCounts.get(currentScore)).intValue());
				rowNumber++;
			}
			
			saveSingleWordAsArffUI.setHypsAndScores(hypotheses, 	
					scoreHypsByWordsUI.getScoresTableModel());
			
			saveSingleWordAsArffUI.setInfoLabelText("Optionally edit the scores and"
					+ " save the file.");
		} else {
			saveSingleWordAsArffUI.setInfoLabelText("No hyps have been scored yet!");			
		}
	}
	
	public void calculateWordPairs() {
		numCodes = wordCodeMap.keySet().size() + 1;
		pairs = new int[numCodes][numCodes];
		Iterator hypIterator = hypotheses.iterator();
		while (hypIterator.hasNext()){
			Hypothesis hyp = (Hypothesis)hypIterator.next();
			int[] codes = hyp.getCodeList(wordCodeMap);
			for (int i = 0; i < (codes.length - 1); i++) {
				int firstWordCode = codes[i];
				int secondWordCode = codes[i + 1];
				pairs[firstWordCode][secondWordCode]++;
			}
		}	
		calculateWordDoublesUI.setPairs(pairs);
	}
	
	public void showWordPairs() {
		wordPairHistogram = calculateWordDoublesUI.getHistogram();
		showWordDoublesUI.createTable(wordPairHistogram.keySet().size(), 
				numCodes, pairs);
		Iterator histoIterator = wordPairHistogram.keySet().iterator();
		int rowNumber = 0;
		while (histoIterator.hasNext()) {
			Integer key = (Integer)histoIterator.next();
			showWordDoublesUI.addData(rowNumber, key.intValue(),
					((Integer)wordPairHistogram.get(key)).intValue());
			rowNumber++;
		}
		showWordDoublesUI.setInfoLabelText("There were "
				+ (numCodes * numCodes) + " possible word pairs.");

	}
	
	public void savePairsAsArff() {
		TreeMap pairMap = new TreeMap();
		if (hypotheses == null) {
			saveWordPairsAsArffUI.setInfoLabelText("No hyps loaded!");
			hypotheses = getHypsFromFile();
		}
		if ((wordCodeMap == null) || (wordCodeMap.size() == 0)) {
			wordCodeMap = new HashMap();
			saveWordPairsAsArffUI.setInfoLabelText("No word list loaded!");
			ArrayList wordList = getWordListFromFile();
			for (int i = 0; i < wordList.size(); i++) {
				Word word = (Word)wordList.get(i);
				wordCodeMap.put(word.getText(), new Integer(word.getCode()));
			}
		}
		if (showWordDoublesUI.getPairMap().size() == 0){
			saveWordPairsAsArffUI.setInfoLabelText("No word pairs loaded!");
			pairMap = getPairsFromFile();
		} else {
			pairMap = showWordDoublesUI.getPairMap();
		}
		
		Iterator hypIterator = hypotheses.iterator();
		TreeMap scoreMap = new TreeMap();
		TreeMap scoreCounts = new TreeMap();
		while (hypIterator.hasNext()){
			Hypothesis hypothesis = (Hypothesis)hypIterator.next();
			Integer score = new Integer(hypothesis.getScore());
			scoreMap.put(score, score);
			if (!scoreCounts.containsKey(score)){
				scoreCounts.put(score, new Integer(1));
			} else {
				int oldCount = ((Integer)scoreCounts.get(score)).intValue();
				scoreCounts.put(score, new Integer(oldCount + 1));
			}
		}
		saveWordPairsAsArffUI.createTable(scoreMap.size());
		Iterator scoreIterator = scoreMap.keySet().iterator();
		int rowNumber = 0;
		while (scoreIterator.hasNext()){
			Integer currentScore = (Integer)scoreIterator.next();
			saveWordPairsAsArffUI.addScore(rowNumber, 
					currentScore.intValue(),
					((Integer)scoreCounts.get(currentScore)).intValue());
			rowNumber++;
		}

		saveWordPairsAsArffUI.setHypsAndScores(hypotheses, wordCodeMap, pairMap);

		saveWordPairsAsArffUI.setInfoLabelText("Optionally edit the scores and"
				+ " save the file.");

	}
	
	public void calculateWordTriples() {
		numCodes = wordCodeMap.keySet().size() + 1;
		triples = new SparseDoubleMatrix3D(numCodes, numCodes, numCodes);
		Iterator hypIterator = hypotheses.iterator();
		while (hypIterator.hasNext()){
			Hypothesis hyp = (Hypothesis)hypIterator.next();
			int[] codes = hyp.getCodeList(wordCodeMap);
			for (int i = 0; i < (codes.length - 2); i++) {
				int firstWordCode = codes[i];
				int secondWordCode = codes[i + 1];
				int thirdWordCode = codes[i + 2];
				double oldVal = triples.getQuick(firstWordCode,
						                         secondWordCode,
						                         thirdWordCode);
				triples.setQuick(firstWordCode,
                                 secondWordCode,
                                 thirdWordCode,
                                 (oldVal + 1));
			}
		}	
		calculateWordTriplesUI.setTriples(triples, numCodes);
	}
	
	public void showWordTriples() {
		wordTripleHistogram = calculateWordTriplesUI.getHistogram();
		showWordTriplesUI.createTable(wordTripleHistogram.keySet().size(), 
				numCodes, triples);
		Iterator histoIterator = wordTripleHistogram.keySet().iterator();
		int rowNumber = 0;
		while (histoIterator.hasNext()) {
			Double key = (Double)histoIterator.next();
			showWordTriplesUI.addData(rowNumber, key.intValue(),
					((Double)wordTripleHistogram.get(key)).intValue());
			rowNumber++;
		}
		showWordTriplesUI.setInfoLabelText("There were "
				+ (numCodes * numCodes * numCodes) + " possible word triples.");


	}
	
	public void saveTriplesAsArff() {
		TreeMap tripleMap = new TreeMap();
		if (hypotheses == null) {
			saveWordTriplesAsArffUI.setInfoLabelText("No hyps loaded!");
			hypotheses = getHypsFromFile();
		}
		if ((wordCodeMap == null) || (wordCodeMap.size() == 0)) {
			wordCodeMap = new HashMap();
			saveWordTriplesAsArffUI.setInfoLabelText("No word list loaded!");
			ArrayList wordList = getWordListFromFile();
			for (int i = 0; i < wordList.size(); i++) {
				Word word = (Word)wordList.get(i);
				wordCodeMap.put(word.getText(), new Integer(word.getCode()));
			}
		}
		if (showWordTriplesUI.getTripleMap().size() == 0){
			saveWordTriplesAsArffUI.setInfoLabelText("No word triples loaded!");
			tripleMap = getTriplesFromFile();
		} else {
			tripleMap = showWordTriplesUI.getTripleMap();
		}
		
		Iterator hypIterator = hypotheses.iterator();
		TreeMap scoreMap = new TreeMap();
		TreeMap scoreCounts = new TreeMap();
		while (hypIterator.hasNext()){
			Hypothesis hypothesis = (Hypothesis)hypIterator.next();
			Integer score = new Integer(hypothesis.getScore());
			scoreMap.put(score, score);
			if (!scoreCounts.containsKey(score)){
				scoreCounts.put(score, new Integer(1));
			} else {
				int oldCount = ((Integer)scoreCounts.get(score)).intValue();
				scoreCounts.put(score, new Integer(oldCount + 1));
			}
		}
		saveWordTriplesAsArffUI.createTable(scoreMap.size());
		Iterator scoreIterator = scoreMap.keySet().iterator();
		int rowNumber = 0;
		while (scoreIterator.hasNext()){
			Integer currentScore = (Integer)scoreIterator.next();
			saveWordTriplesAsArffUI.addScore(rowNumber, 
					currentScore.intValue(),
					((Integer)scoreCounts.get(currentScore)).intValue());
			rowNumber++;
		}

		saveWordTriplesAsArffUI.setHypsAndScores(hypotheses, wordCodeMap, tripleMap);

		saveWordTriplesAsArffUI.setInfoLabelText("Optionally edit the scores and"
				+ " save the file.");

	}
	
	public TreeMap getPairsFromFile() {
		TreeMap pairMap = new TreeMap();
		JFileChooser openFileChooser = new JFileChooser();
		openFileChooser.setDialogTitle("Select a pair list file");
		if (openFileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			BufferedReader infile = null;
			String line;
			try {
				infile = new BufferedReader(new FileReader(
						openFileChooser.getSelectedFile()));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			
			try {
				while ((line = infile.readLine()) != null) {
					String[] lineParts = line.split(",");
					pairMap.put(lineParts[0], new Integer(0));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return pairMap;
	}

	public TreeMap getTriplesFromFile() {
		TreeMap tripleMap = new TreeMap();
		JFileChooser openFileChooser = new JFileChooser();
		openFileChooser.setDialogTitle("Select a triple list file");
		if (openFileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			BufferedReader infile = null;
			String line;
			try {
				infile = new BufferedReader(new FileReader(
						openFileChooser.getSelectedFile()));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			
			try {
				while ((line = infile.readLine()) != null) {
					String[] lineParts = line.split(",");
					tripleMap.put(lineParts[0], new Integer(0));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return tripleMap;
	}

	class ApplicationCloser extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			System.exit(0);
		}
	}

}
