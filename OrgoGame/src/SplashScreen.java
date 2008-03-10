import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.StringItem;


public class SplashScreen extends Form implements CommandListener{
	
	private Controller controller;
	private Command okCommand;
	private Command quitCommand;
	
	public SplashScreen(Controller controller, String infoString) {
		super("Instructions");
		append(new StringItem("This game is intended to help you learn" +
				" your organic chemistry reactions.\n" +
				"You will be presented with a starting material;" +
				" use the right arrow to show the product. " +
				"You can return to the starting material with the" +
				" left arrow.\n" +
				"Pressing the center button takes you to the reaction " +
				"selection screen.\n" +
				"At that point, the two control buttons allow you" +
				" to enter reactions that will convert the starting " +
				"material into the product and to check your answer.\n",
				infoString));
		okCommand = new Command("OK", Command.OK, 1);
		addCommand(okCommand);
		quitCommand = new Command("Quit", Command.EXIT, 1);
		addCommand(quitCommand);
		setCommandListener(this);
		this.controller = controller;
	}

	public void commandAction(Command c, Displayable arg1) {
		if (c == okCommand) {
			controller.startGame();
		}
		if (c == quitCommand) {
			controller.quit();
		}
	}
}
