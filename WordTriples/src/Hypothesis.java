import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Hypothesis {
	private String text;
	private String hypothesis;
	private int number;
	private int score;
	private ArrayList wordSet;
	
	public Hypothesis(String text) throws BadHypothesisStringException {
		wordSet = new ArrayList();
		this.text = text;
		try {
			String[] lineParts = text.split("\t");
			number = Integer.parseInt(lineParts[0]);
			hypothesis = lineParts[1];
			score = Integer.parseInt(lineParts[2]);
		} catch (NullPointerException e) {
			throw new BadHypothesisStringException();
		}
		String[] wordArray = hypothesis.split(" ");
		for (int i = 0; i < wordArray.length; i++) {
			wordSet.add(wordArray[i]);
		}
	}

	public String getHypothesisText() {
		return hypothesis;
	}
	
	public ArrayList getWordSet(){
		return wordSet;
	}
	
	public int getNumber() {
		return number;
	}
	
	public int getScore(){
		return score;
	}
	
	public int getWordCount() {
		return wordSet.size();
	}
	
	public int[] scoreBySingleWords(HashMap wordCodeMap){
		int[] scores = new int[wordCodeMap.values().size() + 1];
		Iterator wordIterator = wordSet.iterator();
		while (wordIterator.hasNext()) {
			String currentWord = (String)wordIterator.next();
			if (wordCodeMap.get(currentWord) != null) {
				int score = ((Integer)wordCodeMap.get(currentWord)).intValue();
				scores[score]++;				
			}
		}
		return scores;
	}
	
	public int[] getCodeList(HashMap wordCodeMap) {
		int[] wordCodes = new int[wordSet.size()];
		for (int i = 0; i < wordSet.size(); i++){
			if (wordCodeMap.get(wordSet.get(i)) != null) {
				wordCodes[i] = ((Integer)wordCodeMap.get((wordSet.get(i)))).intValue();
			} else {
				wordCodes[i] = 0;
			}
		}
		return wordCodes;
	}
	
}
