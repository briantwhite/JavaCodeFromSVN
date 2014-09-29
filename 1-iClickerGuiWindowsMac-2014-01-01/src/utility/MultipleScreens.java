package utility;
import java.awt.AWTException;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * MultipleScreens: a utility class that counts the number of screens, 
 * calculates their resolutions, and takes screenshots as required.
 * @author Junhao
 *
 */
public class MultipleScreens {
	/**
	 * Get number of screens.
	 * @return number of screens.
	 */
	public static int getNumberOfScreens () {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] gs = ge.getScreenDevices();
		
		return gs.length;
	}
	
	/**
	 * Get graphics device by index.
	 * @param index index of graphics device.
	 * @return Graphics device at certain index.
	 * @throws Exception
	 */
	public static GraphicsDevice getGraphicsDevice(int index) throws Exception {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] gs = ge.getScreenDevices();
		
		if (index >= gs.length) {
			throw new Exception("Invalid graphics device index.");
		}
		
		return gs[index];
	}
	
	/**
	 * Get number of configurations of a graphics device represented by index.
	 * @param screenIndex index of screen.
	 * @return number of configurations of a graphics device represented by index.
	 * @throws Exception
	 */
	public static int getNumberOfConfigurations(int screenIndex) throws Exception {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] gs = ge.getScreenDevices();
		
		if (gs.length > screenIndex) {
			GraphicsDevice gd = gs[screenIndex];
			GraphicsConfiguration[] gc = gd.getConfigurations();
			return gc.length;
		} else {
			throw new Exception("There are only " + gs.length + "available.");
		}
	}
	
	public static Rectangle getRectangle(GraphicsConfiguration gc) {
		return gc.getBounds();
	}
	
	/**
	 * Take screenshots and save it as an image. The image will contain all screens connected to the computer.
	 * @param filename name of the image file to be saved.
	 * @throws AWTException
	 * @throws IOException
	 */
	public static void takeScreenShotsAll(String filename) throws AWTException, IOException {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] gs = ge.getScreenDevices();
		
		Point maxPoint = new Point(Integer.MIN_VALUE, Integer.MIN_VALUE);
		Point minPoint = new Point(Integer.MAX_VALUE, Integer.MAX_VALUE);
		
		for (int i = 0; i < gs.length; i++) {
			GraphicsDevice gd = gs[i];
			GraphicsConfiguration[] gc = gd.getConfigurations();
			
			for (int j = 0; j < gc.length; j++) {
				Rectangle gcBounds = gc[j].getBounds();
				
				maxPoint.x = Math.max(maxPoint.x, gcBounds.x + gcBounds.width - 1);
				maxPoint.y = Math.max(maxPoint.y, gcBounds.y + gcBounds.height - 1);
				
				minPoint.x = Math.min(minPoint.x, gcBounds.x);
				minPoint.y = Math.min(minPoint.y, gcBounds.y);
			}
		}
		
		takeScreenShot(filename, new Rectangle(minPoint.x, minPoint.y, maxPoint.x - minPoint.x + 1, maxPoint.y - minPoint.y + 1));
	}
	
	/**
	 * Take a screenshot within the specified dimension, save it as an image file with the specified file name.  
	 * @param filename name of the image file.
	 * @param r part of the screen to be captured.
	 * @throws AWTException
	 * @throws IOException
	 */
	public static void takeScreenShot(String filename, Rectangle r) throws AWTException, IOException {
        Robot robot = new Robot();
        BufferedImage image = robot.createScreenCapture(r);
        
        File file = new File(filename);
        ImageIO.write(image, "jpg", file);
	}
}

