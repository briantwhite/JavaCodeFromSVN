import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
	
public class PictureCanvas extends Canvas {

	public String text = "Hi";

	protected void paint(Graphics g) {
		int width = getWidth();
		int height = getHeight();
		
		g.setColor(0xff0000);
		
		g.fillRect(0, 0, width, height);
		
		g.setColor(0x00ff00);
		g.drawString(text, 0, 0, g.TOP|g.LEFT);
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
