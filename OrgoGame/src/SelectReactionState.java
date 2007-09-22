import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Displayable;


public class SelectReactionState extends ListState {
	
	Command back;
	Command select;

	public SelectReactionState(OrgoGame orgoGame, 
			ProblemSet problemSet,
			Controller controller) {
		super(orgoGame, problemSet, controller, "Choose a Reaction", IMPLICIT);
		
		back = new Command("Back", Command.BACK, 1);
		select = new Command("Select", Command.ITEM, 2);
		this.addCommand(back);
		this.addCommand(select);
		setCommandListener(this);
		
		String[] reactions = this.problemSet.getAllReactions();
		for (int i = 0; i < reactions.length; i++) {
			append("* " + reactions[i], null);
		}

	}

	public void commandAction(Command command, Displayable arg1) {
		if (command == back) {
			controller.switchToEditAnswerState();
		}
		if (command == select) {
			controller.addReactionToAnswer(getSelectedIndex());
		}
		
	}

}
