package content;

import gui.enumoption.EnumCorrectAnswer;
import iClickerDriver.ButtonEnum;

/**
 * Response information for one lab: number of responses of each choice.
 * @author Junhao
 *
 */
public class LabInfo {
	private int responseA;
	private int responseB;
	private int responseC;
	private int responseD;
	private int responseE;
	
	public LabInfo() {
		this.responseA = 0;
		this.responseB = 0;
		this.responseC = 0;
		this.responseD = 0;
		this.responseE = 0;
	}

	/**
	 * Someone changes mind.
	 * @param buttonOld old choice.
	 * @param buttonNew new choice.
	 */
	public void modifyResponse (ButtonEnum buttonOld, ButtonEnum buttonNew) {
		switch (buttonOld) {
		case A: this.responseA--; break;
		case B: this.responseB--; break;
		case C: this.responseC--; break;
		case D: this.responseD--; break;
		default: this.responseE--; break;
		}
		
		switch (buttonNew) {
		case A: this.responseA++; break;
		case B: this.responseB++; break;
		case C: this.responseC++; break;
		case D: this.responseD++; break;
		default: this.responseE++; break;
		}
	}
	
	/**
	 * Someone votes for the first time.
	 * @param button choice.
	 */
	public void newResponse(ButtonEnum button) {
		switch (button) {
		case A: this.responseA++; break;
		case B: this.responseB++; break;
		case C: this.responseC++; break;
		case D: this.responseD++; break;
		default: this.responseE++; break;
		}
	}
	
	/**
	 * How many students in total in this lab section have voted.
	 * @return total number of students in this lab section who have voted.
	 */
	public int getTotalResponse() {
		return this.responseA + this.responseB + this.responseC + this.responseD + this.responseE;
	}

	/**
	 * How many students in this lab section get it correct.
	 * @param enumCorrectAnswer the correct choice. 
	 * @return the number of student in this lab section who get it correct.
	 */
	public int getCorrectResponse(EnumCorrectAnswer enumCorrectAnswer) {
		switch (enumCorrectAnswer) {
		case A: return this.responseA; 
		case B: return this.responseB; 
		case C: return this.responseC; 
		case D: return this.responseD; 
		default: return this.responseE;
		}
	}
	
	public int getResponseA() {
		return responseA;
	}

	public int getResponseB() {
		return responseB;
	}

	public int getResponseC() {
		return responseC;
	}

	public int getResponseD() {
		return responseD;
	}

	public int getResponseE() {
		return responseE;
	}
}
