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
	
	public EditAnswerState(Controller controller, ProblemSet problemSet) {
		super(controller, problemSet, "Your Answer:", List.IMPLICIT);
		addRxn = new Command(OrgoGame.ADD_RXN_TO_END, Command.SCREEN, 1);
		addRxnBefore = new Command(OrgoGame.ADD_BEFORE_SELECTED, Command.SCREEN, 2);
		deleteRxn = new Command(OrgoGame.DELETE_RXN, Command.SCREEN, 3);
		back = new Command(OrgoGame.BACK, Command.BACK, 4);
		checkAnswer = new Command(OrgoGame.CHECK_ANSWER, Command.SCREEN, 5);
		this.addCommand(addRxn);
		this.addCommand(addRxnBefore);
		this.addCommand(deleteRxn);
		this.addCommand(back);
		this.addCommand(checkAnswer);
		setCommandListener(controller);
		
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

}
