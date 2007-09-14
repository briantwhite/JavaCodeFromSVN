import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;


public class Controller {

	Display display;
	
	Displayable currentState;
	
	OrgoGame orgoGame;
	ProblemSet problemSet;
	
	int selectedReactionInAnswer;
	
	public Controller(OrgoGame game, ProblemSet problemSet) {
		orgoGame = game;
		this.problemSet = problemSet;
		selectedReactionInAnswer = 0;
	}
	
	public void updateDisplay() {
		display.setCurrent(currentState);
		if (currentState instanceof Canvas) {
			((Canvas)currentState).repaint();
		}
	}

	public void setCurrentState(Displayable state) {
		currentState = state;
	}
	
	public Displayable getCurrentState() {
		return currentState;
	}
	
	public Display getDisplay() {
		return display;
	}

	public void setDisplay(Display display) {
		this.display = display;
	}
	
	public void switchToProductState() {
		setCurrentState(orgoGame.productState);
		updateDisplay();
	}
	
	public void switchToStartingMaterialState() {
		setCurrentState(orgoGame.startingMaterialState);
		updateDisplay();
	}
	
	public void switchToEditAnswerState() {
		orgoGame.editAnswerState = null;
		orgoGame.editAnswerState = new EditAnswerState(orgoGame, this, problemSet);
		setCurrentState(orgoGame.editAnswerState);
		updateDisplay();
	}
	
	public void switchToReactionChoiceState(int selectedItem) {
		selectedReactionInAnswer = selectedItem;
		setCurrentState(orgoGame.selectReactionState);
		updateDisplay();
	}
	
	public void addReactionToAnswer(int reaction) {
		problemSet.addReactionToStudentsAnswer(reaction, selectedReactionInAnswer);
		orgoGame.editAnswerState = new EditAnswerState(orgoGame, this, problemSet);
		setCurrentState(orgoGame.editAnswerState);
		updateDisplay();
	}
	
	public void checkAnswer(){
		System.out.println("0 - told to check answer");
		if (problemSet.isCurrentListCorrect()){
			System.out.println("5c- answer was correct");
			problemSet.setSuccessfullyCompleted(problemSet.getStartingMaterial(), 
					problemSet.getProduct());
			orgoGame.editAnswerState.showAnswerWasCorrectOrNot(true);
			System.out.println("6 - asking for new problem");
			problemSet.newProblem();
			System.out.println("7 - got new problem");
//			switchToStartingMaterialState();
		} else {
			System.out.println("5i - answer was incorrect");
			orgoGame.editAnswerState.showAnswerWasCorrectOrNot(false);
		}
	}
}
