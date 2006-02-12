import java.util.ArrayList;
import java.util.HashSet;

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
	
}
