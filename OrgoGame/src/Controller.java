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
	
	public Controller() {
		selectedReactionInAnswer = 0;
	}
	
	public void setOrgoGame(OrgoGame og) {
		orgoGame = og;
	}
	
	public void setProblemSet(ProblemSet ps) {
		problemSet = ps;
	}
	
	public void updateDisplay() {
		display.setCurrent(currentState);
		if (currentState instanceof Canvas) {
			((Canvas)currentState).repaint();
		}
	}
	
	public void updateTimers(){
		System.out.println(problemSet.getTimerDisplay().getMinutes() 
				+ ":" 
				+ problemSet.getTimerDisplay().getSeconds());
		updateDisplay();
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
		orgoGame.editAnswerState = new EditAnswerState(orgoGame, problemSet, this);
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
		orgoGame.editAnswerState = new EditAnswerState(orgoGame, problemSet, this);
		setCurrentState(orgoGame.editAnswerState);
		updateDisplay();
	}
	
	public void checkAnswer(){
		if (problemSet.isCurrentListCorrect()){
			problemSet.setSuccessfullyCompleted(problemSet.getStartingMaterial(), 
					problemSet.getProduct());
			orgoGame.editAnswerState.showAnswerWasCorrectOrNot(true);
			problemSet.newProblem();
//			switchToStartingMaterialState();
		} else {
			orgoGame.editAnswerState.showAnswerWasCorrectOrNot(false);
		}
	}
}
