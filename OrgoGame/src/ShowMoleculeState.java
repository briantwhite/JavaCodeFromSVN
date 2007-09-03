import java.io.IOException;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.midlet.MIDletStateChangeException;


public abstract class ShowMoleculeState extends Canvas implements CommandListener {

	OrgoGame orgoGame; 
	ProblemSet problemSet;
	Controller controller;
	
	private Image legend;

	protected Command quit;
	protected Command newProblem;

	public ShowMoleculeState(OrgoGame orgoGame, 
			ProblemSet problemSet, 
			Controller controller) {
		this.orgoGame = orgoGame;
		this.problemSet = problemSet;
		this.controller = controller;
		quit = new Command("Quit", Command.EXIT, 1);
		newProblem = new Command("New Problem", Command.SCREEN, 2);
		addCommand(quit);
		addCommand(newProblem);
		setCommandListener(this);
		try {
			legend = Image.createImage("/images/legend.png");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void doCommonCommandActions(Command c) {
		try {
			if (c == quit) {
				orgoGame.destroyApp(true);
				orgoGame.notifyDestroyed();
			}
			if (c == newProblem) {
				orgoGame.newProblem();
				return;
			}
		}
		catch (MIDletStateChangeException e) {}
	}
	
	public void doCommmonPainting(Graphics g) {
		int width = getWidth();
		int height = getHeight();
		
		g.setColor(0x808080);
		g.fillRect(0, 0, width, height);

		g.setColor(0x000000);
		g.fillRect(0, 0, width, 15);
		g.setColor(0xffffff);
		g.drawString("Completed " + problemSet.getNumSuccessfullyCompletedProblems()
				+ " of " + problemSet.getTotalNumberOfProblems() 
				+ " problems.", 0, 0, Graphics.TOP|Graphics.LEFT);
		
		g.drawImage(legend, width/2, height, Graphics.BOTTOM|Graphics.HCENTER);
	}

}


