package biochem;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class FoldedProteinPanel extends JPanel {
	
	private ImageIcon image;
	
	public FoldedProteinPanel() {
		super(new BorderLayout());
		
		this.setPreferredSize(new Dimension(175,175));
		
		// make blank starting image
		BufferedImage bi = new BufferedImage(600, 300, BufferedImage.TYPE_INT_RGB);
		Graphics g = bi.getGraphics();
		g.setColor(BiochemistryWorkbench.BACKGROUND_COLOR);
		g.fillRect(0, 0, 600, 300);
		image = new ImageIcon(bi);
		g.dispose();
	}
	
	public void updateImage(ImageIcon newImage) {
		image = newImage;
		this.setPreferredSize(new Dimension(image.getIconWidth(), image.getIconHeight()));
		revalidate();
		repaint();
	}
	
	public void paintComponent(Graphics g) {
		g.drawImage(image.getImage(), 0, 0, null);
	}

}
