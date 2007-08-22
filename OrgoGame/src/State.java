import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Graphics;


public abstract class State extends Canvas {
	
	OrgoGame game; 
	
	public State(OrgoGame game) {
		this.game = game;
	}
	
	public void paint(Graphics g) {}
	
	public void handleCommand(Command c) {}

}
