package content;

import gui.enumoption.EnumCorrectAnswer;
import iClickerDriver.ButtonEnum;

import java.util.ArrayList;

/**
 * A list of lab information.
 * @author Junhao
 *
 */
public class LabInfoList {
	private ArrayList<LabInfo> labInfoList;
	
	/**
	 * Constructor.
	 * @param labSectionAmount number of lab sections.
	 */
	LabInfoList(int labSectionAmount) {		
		this.labInfoList = new ArrayList<LabInfo>();
		
		for (int i = 0; i < labSectionAmount; i++) {
			this.labInfoList.add(new LabInfo());
		}
	}
	
	/**
	 * Someone in a certain lab section changes mind. Assume s\he is   
	 * a registered student. Do checking before calling this method.
	 * @param index of lab section he\she belongs to.
	 * @param buttonOld old choice.
	 * @param buttonNew new choice.
	 */
	public void modifyResponse(int labSection, ButtonEnum buttonOld, ButtonEnum buttonNew) {
		labInfoList.get(labSection).modifyResponse(buttonOld, buttonNew);
	}
	
	/**
	 * Someone in a certain lab section voted for the first time. Assume he\she 
	 * is a registered student. Do checking before calling this method. 
	 * @param index of lab section he\she belongs to. 
	 * @param button choice
	 */
	public void newResponse(int labSection, ButtonEnum button) {
		labInfoList.get(labSection).newResponse(button);
	}
	
	/**
	 * Get total number of votes for a certain lab section.
	 * @param labSection index of lab section s\he belongs to. 
	 * @return total number of votes for this lab section.
	 */
	public int getTotalVotes(int labSection) {
		return this.labInfoList.get(labSection).getTotalResponse();
	}

	/**
	 * Get number of correct votes for a certain lab section.
	 * @param labSection index of lab section s\he belongs to.
	 * @param enumCorrectAnswer the correct choice.
	 * @return number of correct votes for this lab section.
	 */
	public int getCorrectVoteAmount(int labSection, EnumCorrectAnswer enumCorrectAnswer) {
		return this.labInfoList.get(labSection).getCorrectResponse(enumCorrectAnswer);
	}
}
