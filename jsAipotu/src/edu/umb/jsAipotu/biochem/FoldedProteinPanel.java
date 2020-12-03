package edu.umb.jsAipotu.biochem;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class FoldedProteinPanel extends JPanel {
	
	private ImageIcon image;
	
	public FoldedProteinPanel() {
		super(new BorderLayout());
				
		// make blank starting image
		BufferedImage bi = 
			new BufferedImage(800, 600, BufferedImage.TYPE_INT_RGB);
		Graphics g = bi.getGraphics();
		g.setColor(BiochemistryWorkbench.BACKGROUND_COLOR);
		g.fillRect(0, 0, 800, 600);
		image = new ImageIcon(bi);
		g.dispose();
		setPreferredSize(new Dimension(image.getIconWidth(), image.getIconHeight()));
		revalidate();
		repaint();
	}
	
	// fill out image to window size
	public void updateImage(ImageIcon newImage, Dimension actualSize) {
		int workingWidth = Math.max(newImage.getIconWidth(), actualSize.width);
		int workingHeight = Math.max(newImage.getIconHeight(), actualSize.height);
		BufferedImage img = 
			new BufferedImage(workingWidth, workingHeight, BufferedImage.TYPE_INT_RGB);
		Graphics g = img.getGraphics();
		g.setColor(BiochemistryWorkbench.BACKGROUND_COLOR);
		g.fillRect(0, 0, workingWidth, workingHeight);
		
		g.drawImage(newImage.getImage(), 0, 0, null);

		image = new ImageIcon(img);
		
		g.dispose();
		
		this.setPreferredSize(new Dimension(workingWidth, workingHeight));
		revalidate();
		repaint();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		setPreferredSize(new Dimension(image.getIconWidth(), image.getIconHeight()));
		g.drawImage(image.getImage(), 0, 0, null);
	}

}
