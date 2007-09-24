import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;


public class EditAnswerState extends ListState {

	Command addRxn;
	Command addRxnBefore;
	Command deleteRxn;
	Command back;
	Command checkAnswer;
	
	public EditAnswerState(OrgoGame orgoGame, 
			ProblemSet problemSet,
			Controller controller) {
		super(orgoGame, problemSet, controller, "Your Answer:", List.IMPLICIT);
		addRxn = new Command("Add Rxn to End", Command.SCREEN, 1);
		addRxnBefore = new Command("Add Rxn Before Selected", Command.SCREEN, 2);
		deleteRxn = new Command("Delete Rxn", Command.SCREEN, 3);
		back = new Command("Back", Command.BACK, 4);
		checkAnswer = new Command("Check Answer", Command.SCREEN, 5);
		this.addCommand(addRxn);
		this.addCommand(addRxnBefore);
		this.addCommand(deleteRxn);
		this.addCommand(back);
		this.addCommand(checkAnswer);
		setCommandListener(this);
		
		if (this.problemSet.getSizeOfStudentsAnswer() == 0) {
			append("No reactions yet.", null);
		} else {
			int[] answer = this.problemSet.getStudentsAnswer();
			for (int i = 0; i < answer.length; i++){
				append((i + 1) + ") " 
						+ problemSet.getReactionDescription(answer[i]), null);
			}
		}

	}

	public void commandAction(Command command, Displayable arg1) {
		if (command == addRxn) {
			controller.switchToReactionChoiceState(-1);
		}
		if (command == addRxnBefore){
			controller.switchToReactionChoiceState(getSelectedIndex());
		}
		if (command == deleteRxn){
			problemSet.deleteReactionFromStudentsAnswer(getSelectedIndex());
			controller.switchToEditAnswerState();
		}
		if (command == back) {
			controller.switchToStartingMaterialState();
		}
		if (command == checkAnswer){
			controller.checkAnswer();
		}
	}
}
