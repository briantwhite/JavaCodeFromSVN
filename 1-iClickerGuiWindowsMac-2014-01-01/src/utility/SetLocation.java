package utility;

import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.JFrame;

/**
 * This class provides different ways of setting the position of a window on the screen.
 * @author Junhao
 *
 */
public class SetLocation {
	/**
	 * Set the frame at the center of the screen
	 * @param frame the frame whose location needs to be set.
	 * @param screenIndex index of the screen, with the main screen being zero, extra screens being one, two, ..., etc.
	 * @throws Exception 
	 */
	public static void setCenterScreen(JFrame frame, int screenIndex){
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] gs = ge.getScreenDevices();
		
		if (gs.length > screenIndex) {
			GraphicsDevice gd = gs[screenIndex];
			GraphicsConfiguration[] gc = gd.getConfigurations();
			Rectangle r = gc[0].getBounds();
			
			Rectangle abounds = frame.getBounds();
			frame.setLocation(r.x + (r.width - abounds.width) / 2, r.y + (r.height - abounds.height) / 2);
		} else {
			try {
				throw new Exception("There are only " + gs.length + " screen(s) available.");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	
	/**
	 * Set the frame at the center of another frame.
	 * @param parent the reference frame.
	 * @param child the frame whose location needs to be set.
	 */
	public static void setCenterParent(JFrame parent, JFrame child) {
		int x;
		int y;
 
		Point topLeft = parent.getLocationOnScreen();
		Dimension parentSize = parent.getSize();

		Dimension childSize = child.getSize();

		if (parentSize.width > childSize.width) 
			x = ((parentSize.width - childSize.width) / 2) + topLeft.x;
		else 
			x = topLeft.x;

		if (parentSize.height > childSize.height) 
			y = ((parentSize.height - childSize.height) / 2) + topLeft.y;
		else 
			y = topLeft.y;

		child.setLocation(x, y);
	}
	
	/**
	 * Set the frame at the top left part of the screen, with margins.
	 * @param frame the frame whose location needs to be set.
	 * @param screenIndex index of the screen, with the main screen being zero, extra screens being one, two, ..., etc.
	 * @param margin margin to the top and to the left.
	 * @throws Exception 
	 */
	public static void setTopLeft(JFrame frame, int screenIndex, int margin) {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] gs = ge.getScreenDevices();
		
		if (gs.length > screenIndex) {
			GraphicsDevice gd = gs[screenIndex];
			GraphicsConfiguration[] gc = gd.getConfigurations();
			Rectangle r = gc[0].getBounds();
			frame.setLocation(r.x + margin, r.y + margin);
		} else {
			try {
				throw new Exception("There are only " + gs.length + " screen(s) available.");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void setTopRight(JFrame frame, int screenIndex, int margin) {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] gs = ge.getScreenDevices();
		
		if (gs.length > screenIndex) {
			GraphicsDevice gd = gs[screenIndex];
			GraphicsConfiguration[] gc = gd.getConfigurations();
			Rectangle r = gc[0].getBounds();
			frame.setLocation(r.x + r.width - frame.getSize().width - margin, 
							  r.y - margin);
		} else {
			try {
				throw new Exception("There are only " + gs.length + " screen(s) available.");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}		
	}
}
