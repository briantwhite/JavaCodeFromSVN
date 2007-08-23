import java.io.IOException;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;


public class ReactantState extends State {

	public ReactantState(OrgoGame game) {
		super(game);
	}
	
	public void paint(Graphics g) {
		int width = getWidth();
		int height = getHeight();
		
		g.setColor(0x808080);
		g.fillRect(0, 0, width, height);
		
		g.setColor(0x000000);
		
		g.drawString("Starting Material", 0, 0, Graphics.TOP|Graphics.LEFT);
		
		g.drawImage(game.molecules[game.getStartingMaterial()], 20, 20, 
					Graphics.TOP|Graphics.LEFT);


	}

}
