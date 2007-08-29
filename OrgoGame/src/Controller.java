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
	
	public Controller(OrgoGame game, ProblemSet problemSet) {
		orgoGame = game;
		this.problemSet = problemSet;
	}
	
	public void updateDisplay() {
		display.setCurrent(currentState);
		if (Canvas.class.isAssignableFrom(currentState.getClass())) {
			((Canvas)currentState).repaint();
		}
		if (ListState.class.isAssignableFrom(currentState.getClass())) {
			((ListState)currentState).updateDisplay();
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
		setCurrentState(orgoGame.editAnswerState);
		updateDisplay();
	}
	
	public void switchToReactionChoiceState() {
		setCurrentState(orgoGame.selectReactionState);
		updateDisplay();
	}
	
	public void addReactionToAnswer(int reaction) {
		problemSet.addReactionToStudentsAnswer(reaction);
		setCurrentState(orgoGame.editAnswerState);
		updateDisplay();
	}
}
