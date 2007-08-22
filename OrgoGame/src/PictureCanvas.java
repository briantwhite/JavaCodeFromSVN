import java.io.IOException;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
	
public class PictureCanvas extends Canvas {

	public String text = "Hi";

	protected void paint(Graphics g) {
		int width = getWidth();
		int height = getHeight();
		
		g.setColor(0xff0000);
		
		g.fillRect(0, 0, width, height);
		
		g.setColor(0x00ff00);
		g.drawString(text, 0, 0, g.TOP|g.LEFT);
		
		try {
			g.drawImage(Image.createImage("/images/mol1.png"), 20, 20, 
					Graphics.TOP|Graphics.LEFT);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setString(String s) {
		text = s;
		repaint();
	}
	
	protected void keyPressed(int keyCode) {
		System.out.println("pressed " + keyCode);
		setString("pressed" + keyCode);
	}
}
