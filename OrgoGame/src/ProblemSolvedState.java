import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Graphics;
import javax.microedition.midlet.MIDletStateChangeException;


public class ProblemSolvedState extends Canvas {
	
	private Controller controller;
	
	private Command newProblem;
	private Command quit;
	
	public ProblemSolvedState(Controller controller) {
		this.controller = controller;
		quit = new Command(OrgoGame.QUIT, Command.EXIT, 1);
		this.addCommand(quit);
		newProblem = new Command(OrgoGame.NEW_PROBLEM, Command.SCREEN, 1);
		this.addCommand(newProblem);
		this.setCommandListener(controller);
	}
	
	protected void paint(Graphics g) {
		int width = getWidth();
		int height = getHeight();
		
		g.setColor(0x808080);
		g.fillRect(0, 0, width, height);

		g.setColor(0x000000);
		
		g.drawString("Congratulations!", 
				0, 0, 
				Graphics.TOP|Graphics.LEFT);
		g.setColor(0xff0000);
		g.drawString("The clock is ", 
				0, 12, 
				Graphics.TOP|Graphics.LEFT);
		g.drawString("stopped at  " 
				+ controller.getElapsedTimeString(), 
				12, 24, 
				Graphics.TOP|Graphics.LEFT);
		g.setColor(0x000000);
		g.drawString("You have ", 
				0, 36, 
				Graphics.TOP|Graphics.LEFT);
		g.drawString("solved " 
				+ controller.getFractionCompletedString(), 
				12, 48, 
				Graphics.TOP|Graphics.LEFT);
	}
}
