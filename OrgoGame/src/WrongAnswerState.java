import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Graphics;
import javax.microedition.midlet.MIDletStateChangeException;


public class WrongAnswerState extends Canvas {
	
	private Controller controller;
	
	private Command back;
	private Command quit;
	
	public WrongAnswerState(Controller controller) {
		this.controller = controller;
		quit = new Command(OrgoGame.QUIT, Command.EXIT, 1);
		this.addCommand(quit);
		back = new Command(OrgoGame.BACK, Command.SCREEN, 1);
		this.addCommand(back);
		this.setCommandListener(controller);
	}
	
	protected void paint(Graphics g) {
		int width = getWidth();
		int height = getHeight();
		
		g.setColor(0x2020ff);
		g.fillRect(0, 0, width, height);

		g.setColor(0x000000);
		
		g.drawString("Sorry!", 
				0, 0, 
				Graphics.TOP|Graphics.LEFT);
		g.drawString("Your answer", 
				0, 12, 
				Graphics.TOP|Graphics.LEFT);
		g.drawString("was incorrect.", 
				12, 24, 
				Graphics.TOP|Graphics.LEFT);
		g.drawString("Please try", 
				0, 36, 
				Graphics.TOP|Graphics.LEFT);
		g.drawString("again.", 
				12, 48, 
				Graphics.TOP|Graphics.LEFT);
		
		g.setColor(0xff0000);
		g.drawString("The timer is ", 
				0, 60, 
				Graphics.TOP|Graphics.LEFT);
		g.drawString("stopped at "
				+ controller.getElapsedTimeString(), 
				12, 72, 
				Graphics.TOP|Graphics.LEFT);
	
	}

}
