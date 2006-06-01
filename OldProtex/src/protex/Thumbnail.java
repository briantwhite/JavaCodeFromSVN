// Thumbnail.java 
// 
// 
// @author Marco Schmidt <marcoschmidt@users.sourceforge.net>
/* 
 * License Information
 * 
 * This  program  is  free  software;  you can redistribute it and/or modify it 
 * under  the  terms  of  the GNU General Public License  as  published  by the 
 * Free  Software  Foundation; either version 2  of  the  License,  or (at your 
 * option) any later version.
 */
/*
 * The following organization of a public class is recommended by X. Jia [2004: 
 * Object Oriented Software Development Using Java(TM). Addison Wesley, Boston, 
 * 677 pp.]
 *
 *     public class AClass {
 *         (public constants)
 *         (public constructors)
 *         (public accessors)
 *         (public mutators)
 *         (nonpublic fields)
 *         (nonpublic auxiliary methods or nested classes)
 *     }
 *
 * Jia also recommends the following design guidelines.
 *
 *     1. Avoid public fields.  There should be no nonfinal public fields, 
 *        except when a class is final and the field is unconstrained.
 *     2. Ensure completeness of the public interface.  The set of public 
 *        methods defined in the class should provide full and convenient 
 *        access to the functionality of the class.
 *     3. Separate interface from implementation.  When the functionality 
 *        supported by a class can be implemented in different ways, it is 
 *        advisable to separate the interface from the implementation.
 * 
 * Modified:  04 Apr 2005 (D. A. Portman/MGX Team UMB)
 */

package protex;

import com.sun.image.codec.jpeg.*;
import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.net.URL;

import javax.swing.ImageIcon;

/**
 * 
 * This class loads an image via java.awt.Toolkit, scales it down to
 * a user-defined resolution and saves it as a JPEG file. It is used to
 * display a Thumbnail of the HTMLPanel to add in the historyPanel
 * 
 * @author Modified on 09 Mar 2005 by Ruchi Dubey/MGX Team UMB
 * @author Modified on 31 Mar 2005 by David Portman/MGX Team UMB
 */

public class Thumbnail {

	/**
	 * Creates a thumbnail-size image by scaling a full-size image.
	 * 
	 * @param image
	 *            BufferedImage
	 * 
	 * @param thumbImgName
	 *            String name of thumbnail image.
	 * 
	 * @return ImageIcon holding thumbnail image.
	 *  
	 */
	public static ImageIcon createThumbnail(BufferedImage image,
	String thumbName) {
		MediaTracker mediaTracker = new MediaTracker(new Container());
		mediaTracker.addImage(image, 0);

		try {
			mediaTracker.waitForID(0);
		}
		catch (InterruptedException e3) {
			e3.printStackTrace();
		}

		// The actual size of the thumbnail will be computed from that maximum
		// size
		//	and the actual size of the image (all sizes are given as pixels).
		imageWidth = image.getWidth(null);
		imageHeight = image.getHeight(null);
		imageRatio = (double) imageWidth / (double) imageHeight;
		thumbRatio = (double) thumbWidth / (double) thumbHeight;

		if (thumbRatio < imageRatio) {
			thumbHeight = (int) (thumbWidth / imageRatio);
		}
		else {
			thumbWidth = (int) (thumbHeight * imageRatio);
		}

		// draw original image to thumbnail image object;
		// 	scale it to the new size on-the-fly

		BufferedImage thumbImage =
		new BufferedImage(
		thumbWidth,
		thumbHeight,
		BufferedImage.TYPE_INT_RGB);
		Graphics2D g = thumbImage.createGraphics();
		g.setRenderingHint(
		RenderingHints.KEY_INTERPOLATION,
		RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.drawImage(image, 0, 0, thumbWidth, thumbHeight, null);

		try {
			out = new BufferedOutputStream(new FileOutputStream(thumbName));
			encoder = JPEGCodec.createJPEGEncoder(out);
			param = encoder.getDefaultJPEGEncodeParam(thumbImage);
			quality = Math.max(0, Math.min(quality, 100));
			param.setQuality((float) quality / 100.0f, false);
			encoder.setJPEGEncodeParam(param);
			encoder.encode(thumbImage);
			out.close();
		}
		catch (FileNotFoundException e2) {
			e2.printStackTrace();
		}
		catch (ImageFormatException e) {
			e.printStackTrace();
		}
		catch (IOException e1) {
			e1.printStackTrace();
		}
		ImageIcon thumbIcon = new ImageIcon(thumbImage);
		return thumbIcon;
	}

	/**
	 * Retrieves a thumbnail-size image from local disk.
	 * 
	 * @param thumbName
	 *            String
	 * 
	 * @return ImageIcon holding thumbnail image.
	 *  
	 */
	public static ImageIcon importThumbnail(String thumbName) {
		URL thumbnailURL = Thumbnail.class.getResource(thumbName);

		if (FoldingManager.getInstance().DEBUG) {
			System.out.println(
			"\nThumbnail.importThumbnail(): " +
			"Retrieving thumbnail image " + thumbName);
			System.out.println(
			"\nThumbnail.importThumbnail(): " +
			"Thumbnail URL is " + thumbnailURL.toString());
		}

		return new ImageIcon(thumbnailURL);
	}

	// fixing quality of the image. the codec expects (I mostly use 0.75f).
	// the higher that quality number is, the better the resulting thumbnail
	// image quality, but also the larger the resulting file.
	private static int quality = 90;

	// fix thumbnail size from WIDTH and HEIGHT of full-size image;
	//	these values (in pixels) are for scaling purposes
	private static int thumbWidth = 130;
	private static int thumbHeight = 70;

	// for dimensions of full-size image
	private static int imageWidth, imageHeight;
	private static double thumbRatio, imageRatio;
	private static Image image;
	private static BufferedOutputStream out;

	// for JPEG images
	private static JPEGImageEncoder encoder;
	private static JPEGEncodeParam param;
}
