package gui;

import java.awt.Color;

import app.Test;

/**
 * Define color for different lab groups. At most 12 groups are supported.
 * @author Junhao
 *
 */
public class LabColor {
	Color labColorCorrect;		// Color for histogram, by section, correct answer.
	Color labColorWrong;		// Color for histogram, by section, wrong answer.
	
	Test test;
	
	public LabColor(Test test) {
		this.labColorCorrect = Color.BLACK;
		this.labColorWrong = Color.GRAY;		
		
		this.test = test;
	}
	
	public Color getLabColorCorrect() {
		return this.labColorCorrect;
	}
	
	public Color getLabColorWrong() {
		return this.labColorWrong;
	}
}
