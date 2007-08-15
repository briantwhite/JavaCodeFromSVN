import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

public class OrgoGame extends MIDlet implements CommandListener {

	PictureCanvas pictureCanvas;
	
	private Command exitCommand;
	
	
	public OrgoGame() {
		exitCommand = new Command("Exit", Command.EXIT, 99);
		pictureCanvas = new PictureCanvas();
		pictureCanvas.addCommand(exitCommand);
		pictureCanvas.setCommandListener(this);
	}
	
	protected void startApp() throws MIDletStateChangeException {
		Display.getDisplay(this).setCurrent(pictureCanvas);
		pictureCanvas.repaint();
	}

	public void commandAction(Command command, Displayable displayable) {
		pictureCanvas.setString("Fred");

		if (command == exitCommand) {
			try {
				destroyApp(false);
				notifyDestroyed();
			} catch (MIDletStateChangeException e) {
				e.printStackTrace();
			}
		}
	}

	protected void destroyApp(boolean arg0) throws MIDletStateChangeException {}

	protected void pauseApp() {}

}
