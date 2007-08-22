import java.io.IOException;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Image;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

public class OrgoGame extends MIDlet implements CommandListener {

	PictureCanvas pictureCanvas;
	
	private Command exitCommand;
	
	private ReactionList[][] reactionArray;
	
	public OrgoGame() {
		exitCommand = new Command("Exit", Command.EXIT, 99);
		pictureCanvas = new PictureCanvas();
		pictureCanvas.addCommand(exitCommand);
		pictureCanvas.setCommandListener(this);
	}
	
	protected void startApp() throws MIDletStateChangeException {
		
		//load in images
		try {
			Image mol0 = Image.createImage("/images/mol0.png");
			Image mol1 = Image.createImage("/images/mol1.png");
			Image mol2 = Image.createImage("/images/mol2.png");
			Image mol3 = Image.createImage("/images/mol3.png");
			Image rx0 = Image.createImage("/images/rx0.png");
			Image rx1 = Image.createImage("/images/rx1.png");
			Image rx2 = Image.createImage("/images/rx2.png");
			Image rx3 = Image.createImage("/images/rx3.png");
			Image rx4 = Image.createImage("/images/rx4.png");
			Image rx5 = Image.createImage("/images/rx5.png");
			Image rx6 = Image.createImage("/images/rx6.png");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//set up correct reaction array
		reactionArray = new ReactionList[4][4];
		
		//can't convert mol0 to mol0
		for (int i = 0; i < 4; i++) {
			reactionArray[i][i] = null;
		}
		
		reactionArray[0][1] = new ReactionList("1");
		
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
