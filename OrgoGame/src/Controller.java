import java.util.Timer;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;
import javax.microedition.midlet.MIDletStateChangeException;


public class Controller implements CommandListener {

	Display display;

	OrgoGame orgoGame;
	ProblemSet problemSet;

	private Timer timer;
	private TimerDisplay timerDisplay;

	int selectedReactionInAnswer;

	public Controller(OrgoGame orgoGame, ProblemSet problemSet) {
		this.orgoGame = orgoGame;
		this.problemSet = problemSet;
		selectedReactionInAnswer = 0;

		timer = new Timer();
		timerDisplay = new TimerDisplay(this);
	}
	
	public void showSplashScreen() {
		display = Display.getDisplay(orgoGame);
		display.setCurrent(new SplashScreen(this, 
				"Screen HxW=" +
				orgoGame.screenSizeMeasurer.getHeight() +
				"x" +
				orgoGame.screenSizeMeasurer.getWidth() +
				";" +
				problemSet.scale.toString() + "\n" +
				System.getProperty("microedition.configuration") + " " +
				System.getProperty("microedition.profiles")));
	}

	public void startGame() {
		display = Display.getDisplay(orgoGame);
		if (problemSet.screenTooSmall()) {
			display.setCurrent(orgoGame.screenTooSmallState);
		} else {
			getAndShowNewProblem();
			timer.schedule(timerDisplay, 1000, 1000);
		}
	}

	public void getAndShowNewProblem() {
		problemSet.newProblem();
		display.setCurrent(orgoGame.startingMaterialState);
		orgoGame.startingMaterialState.repaint();	
	}

	public void commandAction(Command command, Displayable displayable) {

		String commandLabel = command.getLabel();

		if (commandLabel.equals(OrgoGame.QUIT)) {
			try {
				orgoGame.destroyApp(true);
			} catch (MIDletStateChangeException e) {
				e.printStackTrace();
			}
			orgoGame.notifyDestroyed();
		}

		if (commandLabel.equals(OrgoGame.NEW_PROBLEM)) {
			getAndShowNewProblem();
			timerDisplay.setPaused(false);
		}

		if (displayable == orgoGame.editAnswerState) {
			if (commandLabel.equals(OrgoGame.ADD_RXN_TO_END)) {
				selectedReactionInAnswer = -1;
				display.setCurrent(orgoGame.selectReactionState);
				timerDisplay.setPaused(false);
			}

			if (commandLabel.equals(OrgoGame.ADD_BEFORE_SELECTED)) {
				selectedReactionInAnswer = 
					orgoGame.editAnswerState.getSelectedIndex();
				display.setCurrent(orgoGame.selectReactionState);
				timerDisplay.setPaused(false);
			}

			if (commandLabel.equals(OrgoGame.DELETE_RXN)){
				orgoGame.editAnswerState.delete(
						orgoGame.editAnswerState.getSelectedIndex());
				display.setCurrent(orgoGame.editAnswerState);
				timerDisplay.setPaused(false);
			}

			if (commandLabel.equals(OrgoGame.BACK)){
				display.setCurrent(orgoGame.startingMaterialState);
				orgoGame.startingMaterialState.repaint();
				timerDisplay.setPaused(false);
			}

			if (commandLabel.equals(OrgoGame.CHECK_ANSWER)) {
				timerDisplay.setPaused(true);
				if (problemSet.isCurrentListCorrect()) {
					problemSet.setSuccessfullyCompleted(
							problemSet.getStartingMaterial(), 
							problemSet.getProduct());
					if (problemSet.getNumSuccessfullyCompletedProblems() == 
						problemSet.getTotalNumberOfProblems()){
						display.setCurrent(orgoGame.allDoneState);
						orgoGame.allDoneState.repaint();
					} else {
						display.setCurrent(orgoGame.problemSolvedState);
						orgoGame.problemSolvedState.repaint();
					}
				} else {
					display.setCurrent(orgoGame.wrongAnswerState);
					orgoGame.wrongAnswerState.repaint();
				}
			}
		}

		if (displayable == orgoGame.selectReactionState){
			if (commandLabel.equals(OrgoGame.SELECT)){
				problemSet.addReactionToStudentsAnswer(
						orgoGame.selectReactionState.getSelectedIndex(), 
						selectedReactionInAnswer);
				orgoGame.editAnswerState = new EditAnswerState(this, problemSet);
				display.setCurrent(orgoGame.editAnswerState);
				timerDisplay.setPaused(false);
			}
		}

		if (displayable == orgoGame.wrongAnswerState){
			if (commandLabel.equals(OrgoGame.BACK)){
				display.setCurrent(orgoGame.editAnswerState);
				timerDisplay.setPaused(false);
			}
		}
	}

	public void respondToKeyPress(int gameAction, Displayable displayable) {
		if (gameAction == Canvas.RIGHT) {
			display.setCurrent(orgoGame.productState);
			orgoGame.productState.repaint();
		}

		if (gameAction == Canvas.LEFT) {
			display.setCurrent(orgoGame.startingMaterialState);
			orgoGame.startingMaterialState.repaint();
		}

		if (gameAction == Canvas.FIRE) {
			orgoGame.editAnswerState = null;
			orgoGame.editAnswerState = new EditAnswerState(this, problemSet);
			display.setCurrent(orgoGame.editAnswerState);
		}
	}

	public void updateVisibleTimers() {
		if (display.getCurrent() instanceof ShowMoleculeState){
			((Canvas)display.getCurrent()).repaint();
		}
	}

	public String getElapsedTimeString() {
		return timerDisplay.getMinutes() + ":" + timerDisplay.getSeconds();
	}

	public String getFractionCompletedString() {
		return problemSet.getNumSuccessfullyCompletedProblems()
		+ " out of " + problemSet.getTotalNumberOfProblems();
	}
	
	public void quit() {
		try {
			orgoGame.destroyApp(true);
		} catch (MIDletStateChangeException e) {
			e.printStackTrace();
		}
		orgoGame.notifyDestroyed();
	}

}
