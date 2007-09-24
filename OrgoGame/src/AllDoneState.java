import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Graphics;
import javax.microedition.midlet.MIDletStateChangeException;


public class AllDoneState extends Canvas implements CommandListener {
	
	OrgoGame orgoGame;
	
	private Command quit;
	
	private String elapsedTimeString = "";
	private String fractionCompletedString = "";

	public AllDoneState(OrgoGame orgoGame) {
		this.orgoGame = orgoGame;
		quit = new Command("Quit", Command.EXIT, 1);
		this.addCommand(quit);
		this.setCommandListener(this);
	}
	
	public void commandAction(Command c, Displayable arg1) {
		try {
			if (c == quit) {
				orgoGame.destroyApp(true);
				orgoGame.notifyDestroyed();
			}
		}
		catch (MIDletStateChangeException e) {}

	}

	protected void paint(Graphics g) {
		int width = getWidth();
		int height = getHeight();
		
		g.setColor(0x208020);
		g.fillRect(0, 0, width, height);

		g.setColor(0x000000);
		
		g.drawString("Congratulations!", 0, 0, Graphics.TOP|Graphics.LEFT);
		g.setColor(0x000000);
		g.drawString("You have solved all the problems!", 
				0, 40, Graphics.TOP|Graphics.LEFT);
		g.setColor(0xff0000);
		g.drawString("It took you  " 
				+ elapsedTimeString, 0, 20, Graphics.TOP|Graphics.LEFT);
	}

	public void setElapsedTimeString(String elapsedTimeString) {
		this.elapsedTimeString = elapsedTimeString;
	}

	public void setFractionCompletedString(String fractionCompletedString) {
		this.fractionCompletedString = fractionCompletedString;
	}

}
