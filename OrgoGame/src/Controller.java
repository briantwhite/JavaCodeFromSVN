import java.util.Timer;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;


public class Controller implements CommandListener {

	Display display;
	
	Displayable currentState;
	
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
	
	public void startGame() {
		display = Display.getDisplay(orgoGame);
		problemSet.newProblem();
		display.setCurrent(orgoGame.startingMaterialState);
		orgoGame.startingMaterialState.repaint();
	}

	public void commandAction(Command command, Displayable displayable) {
		
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
		
	public String getElapsedTimeString() {
		return timerDisplay.getMinutes() + ":" + timerDisplay.getSeconds();
	}
	
	public String getFractionCompletedString() {
		return problemSet.getNumSuccessfullyCompletedProblems()
		+ " out of " + problemSet.getTotalNumberOfProblems();
	}
	
	public void checkAnswer(){

	}

}
