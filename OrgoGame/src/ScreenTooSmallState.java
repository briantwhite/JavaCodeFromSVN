import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Graphics;


public class ScreenTooSmallState extends Canvas {
	
	private Command quit;
	
	public ScreenTooSmallState(Controller controller) {
		quit = new Command(OrgoGame.QUIT, Command.EXIT, 1);
		this.addCommand(quit);
		this.setCommandListener(controller);
	}

	protected void paint(Graphics g) {
		
		g.setColor(0x000000);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(0xffffff);
		g.drawString("Your screen" , 0, 0, Graphics.TOP|Graphics.LEFT);
		g.drawString("is too small.", 0, 12, Graphics.TOP|Graphics.LEFT);
	}
	
}
