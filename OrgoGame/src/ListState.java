import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;
import javax.microedition.midlet.MIDletStateChangeException;


public abstract class ListState extends List implements CommandListener {
	
	OrgoGame orgoGame;
	Controller controller;
	ProblemSet problemSet;
	
	public ListState(OrgoGame orgoGame, 
			Controller controller, 
			ProblemSet problemSet, 
			String title, 
			int type) {
		super(title, type);
		this.orgoGame = orgoGame;
		this.controller = controller;
		this.problemSet = problemSet;
	}	

}
