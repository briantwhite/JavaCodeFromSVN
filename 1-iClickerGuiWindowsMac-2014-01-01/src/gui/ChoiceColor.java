package gui;

import gui.enumoption.EnumCorrectAnswer;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;

import content.enumoption.HistogramDisplayOption;

import app.Test;

/**
 * Define color of different choice, used when all votes are displayed by choice (not by group).
 * @author Junhao
 *
 */
public class ChoiceColor {
	HashMap<EnumCorrectAnswer, ArrayList<Color>> hashMap = new HashMap<EnumCorrectAnswer, ArrayList<Color>>();
	
	Test test;
	
	public ChoiceColor(Test test) {
		ArrayList<Color> choiceColorAll = new ArrayList<Color> ();
		ArrayList<Color> choiceColorA = new ArrayList<Color> ();
		ArrayList<Color> choiceColorB = new ArrayList<Color> ();
		ArrayList<Color> choiceColorC = new ArrayList<Color> ();
		ArrayList<Color> choiceColorD = new ArrayList<Color> ();
		ArrayList<Color> choiceColorE = new ArrayList<Color> ();
		
        choiceColorAll.add(new Color(237, 28, 36));
        choiceColorAll.add(new Color(34, 177, 76));
        choiceColorAll.add(new Color(63, 72, 204));
        choiceColorAll.add(new Color(245, 245, 10));
        choiceColorAll.add(new Color(163, 73, 164));
        
        choiceColorA.add(new Color(237, 28, 36));
        choiceColorA.add(new Color(166, 166, 166));
        choiceColorA.add(new Color(166, 166, 166));
        choiceColorA.add(new Color(166, 166, 166));
        choiceColorA.add(new Color(166, 166, 166));
        
        choiceColorB.add(new Color(166, 166, 166));
        choiceColorB.add(new Color(34, 177, 76));
        choiceColorB.add(new Color(166, 166, 166));
        choiceColorB.add(new Color(166, 166, 166));
        choiceColorB.add(new Color(166, 166, 166));
        
        choiceColorC.add(new Color(166, 166, 166));
        choiceColorC.add(new Color(166, 166, 166));
        choiceColorC.add(new Color(63, 72, 204));
        choiceColorC.add(new Color(166, 166, 166));
        choiceColorC.add(new Color(166, 166, 166));
        
        choiceColorD.add(new Color(166, 166, 166));
        choiceColorD.add(new Color(166, 166, 166));
        choiceColorD.add(new Color(166, 166, 166));
        choiceColorD.add(new Color(245, 245, 10));
        choiceColorD.add(new Color(166, 166, 166));
        
        choiceColorE.add(new Color(166, 166, 166));
        choiceColorE.add(new Color(166, 166, 166));
        choiceColorE.add(new Color(166, 166, 166));
        choiceColorE.add(new Color(166, 166, 166));
        choiceColorE.add(new Color(163, 73, 164));
        
        this.hashMap.put(EnumCorrectAnswer.NA, choiceColorAll);
        this.hashMap.put(EnumCorrectAnswer.A, choiceColorA);
        this.hashMap.put(EnumCorrectAnswer.B, choiceColorB);
        this.hashMap.put(EnumCorrectAnswer.C, choiceColorC);
        this.hashMap.put(EnumCorrectAnswer.D, choiceColorD);
        this.hashMap.put(EnumCorrectAnswer.E, choiceColorE);
        
		this.test = test;
	}
	
	/**
	 * Given the correct answer and histogram display option, return the corresponding color for each choice.
	 * @param enumCorrectAnswer correct answer.
	 * @param histogramDisplayOption whether to display histogram or not, and how histogram is displayed.
	 * @return color for each choice.
	 */
	public ArrayList<Color> getColorByAnswer(EnumCorrectAnswer enumCorrectAnswer, HistogramDisplayOption histogramDisplayOption) {
		if (histogramDisplayOption == HistogramDisplayOption.SHOWWITHCORRECTANSWER) {
			return this.hashMap.get(enumCorrectAnswer);
		} else {
			return this.hashMap.get(EnumCorrectAnswer.NA);
		}
	}
}

