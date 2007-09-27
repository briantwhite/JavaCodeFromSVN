import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;
import javax.microedition.midlet.MIDletStateChangeException;


public abstract class ListState extends List {
	
	Controller controller;
	ProblemSet problemSet;
	
	public ListState(Controller controller, 
			ProblemSet problemSet,
			String title, 
			int type) {
		super(title, type);
		this.controller = controller;
		this.problemSet = problemSet;
	}	

}
