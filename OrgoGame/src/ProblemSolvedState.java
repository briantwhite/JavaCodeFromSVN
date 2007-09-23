import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Graphics;
import javax.microedition.midlet.MIDletStateChangeException;


public class ProblemSolvedState extends Canvas implements CommandListener {
	
	OrgoGame orgoGame;
	
	private Command newProblem;
	private Command quit;
	
	private String elapsedTimeString = "";
	private String fractionCompletedString = "";

	public ProblemSolvedState(OrgoGame orgoGame) {
		this.orgoGame = orgoGame;
		quit = new Command("Quit", Command.EXIT, 1);
		this.addCommand(quit);
		newProblem = new Command("New Problem", Command.SCREEN, 1);
		this.addCommand(newProblem);
		this.setCommandListener(this);
	}
	
	public void commandAction(Command c, Displayable arg1) {
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

	protected void paint(Graphics g) {
		int width = getWidth();
		int height = getHeight();
		
		g.setColor(0x808080);
		g.fillRect(0, 0, width, height);

		g.setColor(0x000000);
		
		g.drawString("Congratulations!", 0, 0, Graphics.TOP|Graphics.LEFT);
		g.drawString("You solved it in " 
				+ elapsedTimeString, 0, 20, Graphics.TOP|Graphics.LEFT);
		g.drawString("You have solved " 
				+ fractionCompletedString, 0, 40, Graphics.TOP|Graphics.LEFT);
	}

	public void setElapsedTimeString(String elapsedTimeString) {
		this.elapsedTimeString = elapsedTimeString;
	}

	public void setFractionCompletedString(String fractionCompletedString) {
		this.fractionCompletedString = fractionCompletedString;
	}

}
