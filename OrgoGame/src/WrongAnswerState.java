import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Graphics;
import javax.microedition.midlet.MIDletStateChangeException;


public class WrongAnswerState extends Canvas implements CommandListener {
	
	OrgoGame orgoGame;
	
	private Command back;
	private Command quit;
	
	private String elapsedTimeString = "";

	public WrongAnswerState(OrgoGame orgoGame) {
		this.orgoGame = orgoGame;
		quit = new Command("Quit", Command.EXIT, 1);
		this.addCommand(quit);
		back = new Command("Back", Command.SCREEN, 1);
		this.addCommand(back);
		this.setCommandListener(this);
	}
	
	public void commandAction(Command c, Displayable arg1) {
		try {
			if (c == quit) {
				orgoGame.destroyApp(true);
				orgoGame.notifyDestroyed();
			}
			if (c == back) {
				orgoGame.controller.switchToEditAnswerState();
				return;
			}
		}
		catch (MIDletStateChangeException e) {}

	}

	protected void paint(Graphics g) {
		int width = getWidth();
		int height = getHeight();
		
		g.setColor(0x2020ff);
		g.fillRect(0, 0, width, height);

		g.setColor(0x000000);
		
		g.drawString("Sorry!", 0, 0, Graphics.TOP|Graphics.LEFT);
		g.drawString("Your answer was incorrect."
				, 0, 20, Graphics.TOP|Graphics.LEFT);
		g.drawString("Please try again.", 0, 40, Graphics.TOP|Graphics.LEFT);
		
		g.setColor(0xff0000);
		g.drawString("The timer is stopped at "
				+ elapsedTimeString, 0, 60, Graphics.TOP|Graphics.LEFT);
	
	}

	public void setElapsedTimeString(String elapsedTimeString) {
		this.elapsedTimeString = elapsedTimeString;
	}

}
