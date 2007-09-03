import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;


public class Controller {

	Display display;
	
	Alert resultAlert;
	
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
		if (problemSet.isCurrentListCorrect()){
			resultAlert = new Alert("Checking Your Answer",
					"You got it right!",
					null,
					AlertType.INFO);
			problemSet.setSuccessfullyCompleted(problemSet.getStartingMaterial(), 
					problemSet.getProduct());
			resultAlert.setTimeout(Alert.FOREVER);
			display.setCurrent(resultAlert);
			problemSet.newProblem();
//			switchToStartingMaterialState();
		} else {
			resultAlert = new Alert("Checking Your Answer",
					"Your answer is incorrect.",
					null,
					AlertType.ERROR);
			resultAlert.setTimeout(Alert.FOREVER);
			display.setCurrent(resultAlert);
		}
	}
}
