import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;


public class EditAnswerState extends ListState {
	
	Command addRxn;
	Command deleteRxn;
	Command back;
	Command checkAnswer;
		
	public EditAnswerState(OrgoGame orgoGame, 
			Controller controller,
			ProblemSet problemSet) {
		super(orgoGame, controller, problemSet, "Your Answer:", List.IMPLICIT);
		addRxn = new Command("Add", Command.SCREEN, 1);
		deleteRxn = new Command("Delete", Command.SCREEN, 2);
		back = new Command("Back", Command.BACK, 3);
		checkAnswer = new Command("Check", Command.SCREEN, 4);
		this.addCommand(addRxn);
		this.addCommand(deleteRxn);
		this.addCommand(back);
		this.addCommand(checkAnswer);
		setCommandListener(this);
	}
	
	public void updateDisplay() {
		deleteAll();
		int[] answer = problemSet.getStudentsAnswer();
		if (answer.length == 0) {
			append("No reactions yet.", null);
		} else {
			for (int i = 0; i < answer.length; i++){
				append((i + 1) + ") " 
						+ problemSet.getReactionDescription(answer[i]), null);
			}
		}
	}

	public void commandAction(Command command, Displayable arg1) {
		if (command == addRxn) {
			controller.switchToReactionChoiceState();
		}
		if (command == deleteRxn){
			problemSet.deleteReactionFromStudentsAnswer(getSelectedIndex());
			controller.updateDisplay();
		}
		if (command == back) {
			controller.switchToStartingMaterialState();
		}
		if (command == checkAnswer){
			System.out.println("hi there");
		}
	}


}
