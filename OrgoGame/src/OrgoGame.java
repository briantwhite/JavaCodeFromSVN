import java.io.IOException;
import java.util.Random;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Image;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

public class OrgoGame extends MIDlet implements CommandListener {

	private Command exitCommand;
	
	public Image[] molecules;
	public Image[] reactions;
	
	public int startingMaterial;
	public int product;
	
	private ReactionList[][] reactionArray;
	
	Displayable currentState;
	
	ReactantState reactantState;
	ReactionChoiceState reactionChoiceState;
	
	public OrgoGame() {
	}
	
	protected void startApp() throws MIDletStateChangeException {
		
		//load in images
		molecules = new Image[4];
		reactions = new Image[7];
		
		try {
			molecules[0] = Image.createImage("/images/mol0.png");
			molecules[1] = Image.createImage("/images/mol1.png");
			molecules[2] = Image.createImage("/images/mol2.png");
			molecules[3] = Image.createImage("/images/mol3.png");
			reactions[0] = Image.createImage("/images/rx0.png");
			reactions[1] = Image.createImage("/images/rx1.png");
			reactions[2] = Image.createImage("/images/rx2.png");
			reactions[3] = Image.createImage("/images/rx3.png");
			reactions[4] = Image.createImage("/images/rx4.png");
			reactions[5] = Image.createImage("/images/rx5.png");
			reactions[6] = Image.createImage("/images/rx6.png");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//set up correct reaction array
		reactionArray = new ReactionList[4][4];
		
		//can't convert mol0 to mol0
		for (int i = 0; i < 4; i++) {
			reactionArray[i][i] = null;
		}
		
		// row, column
		reactionArray[0][1] = new ReactionList("1");
		reactionArray[0][2] = new ReactionList("1,2");
		reactionArray[0][3] = new ReactionList("1,3");
		
		reactionArray[1][0] = new ReactionList("4");
		reactionArray[1][2] = new ReactionList("0,2");
		reactionArray[1][3] = new ReactionList("0,3");
		
		reactionArray[2][0] = new ReactionList("5,4");
		reactionArray[2][1] = new ReactionList("5");
		reactionArray[2][3] = new ReactionList("5,0,3");
		
		reactionArray[3][0] = new ReactionList("4");
		reactionArray[3][1] = new ReactionList("6");
		reactionArray[3][2] = new ReactionList("2");
		
		Random r = new Random();
		startingMaterial = r.nextInt(4);
		product = r.nextInt(4);
			
		reactantState = new ReactantState(this);
		reactionChoiceState = new ReactionChoiceState(this);
		
		currentState = reactionChoiceState;
		
		exitCommand = new Command("Exit", Command.EXIT, 99);
		currentState.addCommand(exitCommand);
		currentState.setCommandListener(this);

		Display.getDisplay(this).setCurrent(currentState);
		
	}

	public void commandAction(Command command, Displayable displayable) {

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

	public int getStartingMaterial() {
		return startingMaterial;
	}
	
	public int getProduct() {
		return product;
	}
	
}
