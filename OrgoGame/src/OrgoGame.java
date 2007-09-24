import java.io.IOException;
import java.util.Random;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Image;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

public class OrgoGame extends MIDlet {

	private Command exitCommand;
	
	protected ProblemSet problemSet;
	
	protected Controller controller;
	
	StartingMaterialState startingMaterialState;
	EditAnswerState editAnswerState;
	ProductState productState;
	SelectReactionState selectReactionState;
	ProblemSolvedState problemSolvedState;
	WrongAnswerState wrongAnswerState;
	AllDoneState allDoneState;
	
	public OrgoGame() {
		problemSet = new ProblemSet();
		controller = new Controller();
		problemSet.setOrgoGame(this);
		problemSet.setController(controller);
		controller.setOrgoGame(this);
		controller.setProblemSet(problemSet);
		startingMaterialState = new StartingMaterialState(this, problemSet, controller);
		editAnswerState = new EditAnswerState(this, problemSet, controller);
		productState = new ProductState(this, problemSet, controller);
		selectReactionState = new SelectReactionState(this, problemSet, controller);
		problemSolvedState = new ProblemSolvedState(this);
		wrongAnswerState = new WrongAnswerState(this);
		allDoneState = new AllDoneState(this);
	}
	
	public void allDone() {
		controller.switchToAllDoneState(problemSet.getTimerDisplay().getMinutes() 
				+ ":"
				+ problemSet.getTimerDisplay().getSeconds());
	}
	
	protected void startApp() throws MIDletStateChangeException {
		controller.setCurrentState(startingMaterialState);
		controller.setDisplay(Display.getDisplay(this));
		controller.updateDisplay();
		problemSet.startTimerDisplay();
	}

	protected void destroyApp(boolean arg0) throws MIDletStateChangeException {}

	protected void pauseApp() {}
	
	public void newProblem() {
		problemSet.newProblem();
		controller.setCurrentState(startingMaterialState);
		problemSet.getTimerDisplay().setPaused(false);
		controller.updateDisplay();
	}
}
