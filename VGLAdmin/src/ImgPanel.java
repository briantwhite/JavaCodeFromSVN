/*
 * Chung Ying Yu cs681~3 Fall 2002- Spring 2003 Project VGL File:
 * ~cyyu/src/ImgPanel.java
 * 
 * @author Chung Ying Yu $Id: ImgPanel.java,v 1.1 2004-09-24 15:46:39 brian Exp $
 */

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

public class ImgPanel extends JPanel {
	Image image;

	/** ImgPanel constructor */
	public ImgPanel() {
	}

	public ImgPanel(Image image) {
		this.image = image;
	}

	/** paint the background */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(image, 0, 0, 316, 400, this);
	}
}