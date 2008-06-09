package VGL;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.net.URL;

import javax.swing.JFrame;

/**
 * Nikunj Koolar cs681-3 Fall 2002 - Spring 2003 Project VGL File: VGLMain.java
 * -this is the main driver of the application. It has been configured an applet
 * as a stand-alone application.
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 * 
 * @author Nikunj Koolar
 * @version 1.0 $Id$
 */
public class VGLMain {
	/**
	 * The default path to be specified for all file dialogs
	 */
	private static File m_DefaultDir;

	/**
	 * A unique program id for the application
	 */
	private static String m_PROGRAM_ID = "Virtual Genetics Lab";

	/**
	 * The logo for the application
	 */
	private static URL imageURL = VGLMain.class
			.getResource("UIimages/logo.gif");

	private static Image image = Toolkit.getDefaultToolkit().getImage(imageURL);

	/**
	 * The Constructor
	 */
	public VGLMain() {
		run();
	}

	/**
	 * The main method, args is not used.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		VGLMain VGLMain1 = new VGLMain();
	}

	/**
	 * Run applet as a stand-alone GUI application.
	 */
	private static void run() {
		if (m_DefaultDir == null)
			m_DefaultDir = new File(System.getProperty("user.dir"));
		JFrame frame = new JFrame(m_PROGRAM_ID);
		VGLMainApp app = new VGLMainApp(m_PROGRAM_ID, m_DefaultDir, image,
				frame);
		frame.getContentPane().add("Center", app);
		frame.pack();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int wdt = screenSize.width;
		int hgt = screenSize.height - 30;
		frame.setSize(wdt, hgt);
		frame.setLocation(0, 0);
		frame.setIconImage(image);
		frame.setDefaultCloseOperation(javax.swing.JFrame.DO_NOTHING_ON_CLOSE);
		app.init();
		app.dialogFrame(frame);
		app.start();
		frame.setVisible(true);
	}
}