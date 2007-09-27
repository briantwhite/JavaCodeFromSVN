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
	
	private ProblemSet problemSet;
	private Controller controller;
	
	StartingMaterialState startingMaterialState;
	EditAnswerState editAnswerState;
	ProductState productState;
	SelectReactionState selectReactionState;
	ProblemSolvedState problemSolvedState;
	WrongAnswerState wrongAnswerState;
	AllDoneState allDoneState;
	
	public OrgoGame() {
		problemSet = new ProblemSet();
		controller = new Controller(this, problemSet);
		startingMaterialState = new StartingMaterialState(controller, problemSet);
		editAnswerState = new EditAnswerState(controller, problemSet);
		productState = new ProductState(controller, problemSet);
		selectReactionState = new SelectReactionState(controller, problemSet);
		problemSolvedState = new ProblemSolvedState(controller);
		wrongAnswerState = new WrongAnswerState(controller);
		allDoneState = new AllDoneState(controller);
	}
		
	protected void startApp() throws MIDletStateChangeException {
		controller.startGame();
	}

	protected void destroyApp(boolean arg0) throws MIDletStateChangeException {}

	protected void pauseApp() {}
	
}
