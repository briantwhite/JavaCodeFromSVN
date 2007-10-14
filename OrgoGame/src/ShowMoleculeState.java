import java.io.IOException;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.midlet.MIDletStateChangeException;


public abstract class ShowMoleculeState extends Canvas {

	Controller controller;
	ProblemSet problemSet;

	private Image legend;

	protected Command quit;
	protected Command newProblem;

	public ShowMoleculeState(Controller controller, ProblemSet problemSet) {
		this.problemSet = problemSet;
		this.controller = controller;
		quit = new Command(OrgoGame.QUIT, Command.EXIT, 1);
		newProblem = new Command(OrgoGame.NEW_PROBLEM, Command.SCREEN, 2);
		addCommand(quit);
		addCommand(newProblem);
		setCommandListener(controller);
	}
	
	public void doCommmonPainting(Graphics g) {
		int width = getWidth();
		int height = getHeight();
		
		g.setColor(0x808080);
		g.fillRect(0, 0, width, height);

		g.setColor(0x000000);
		g.fillRect(0, 0, width, 30);
		g.setColor(0xffffff);
		g.drawString("Completed " + problemSet.getNumSuccessfullyCompletedProblems()
				+ " of " + problemSet.getTotalNumberOfProblems() 
				+ " problems.", 0, 0, Graphics.TOP|Graphics.LEFT);
		g.setColor(0xff0000);
		g.drawString("Time Elapsed = " + controller.getElapsedTimeString(), 
				0, 15, 
				Graphics.TOP|Graphics.LEFT);
	}

	protected final void keyPressed(int keyCode) {
		controller.respondToKeyPress(getGameAction(keyCode), this);
	}

}


