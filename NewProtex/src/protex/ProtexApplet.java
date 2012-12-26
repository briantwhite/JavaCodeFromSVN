package protex;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JApplet;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ProtexApplet extends JApplet {

	private FoldingWindow foldingWindow;
	private Protex protex;

	public void init() {
		
		protex = new Protex(true);
		foldingWindow = new FoldingWindow("Folding Window", protex);
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));

		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
		
		JPanel aapPanel = new JPanel();
		aapPanel.setBorder(BorderFactory.createTitledBorder("Amino acids"));
		aapPanel.setLayout(new BoxLayout(aapPanel, BoxLayout.X_AXIS));
		AminoAcidPalette aaPalette = new AminoAcidPalette(225, 180, 4, 5, false);
		aapPanel.setMaximumSize(new Dimension(250, 200));
		aapPanel.add(aaPalette);
		aapPanel.add(Box.createRigidArea(new Dimension(1,180)));
		leftPanel.add(aapPanel);
		
	    String targetShapeString = getParameter("TARGET_SHAPE");
	    if (targetShapeString != null) {
	    	ProteinImageSet pis = ProteinImageFactory.buildProtein(targetShapeString, false);
	    	JPanel targetPanel = new JPanel();
	    	targetPanel.setBorder(BorderFactory.createTitledBorder("Target Shape"));
	    	targetPanel.add(new JLabel(new ImageIcon(pis.getFullScaleImage())));
	    	leftPanel.add(targetPanel);
	    }

		mainPanel.add(leftPanel);
		mainPanel.add(foldingWindow);

		getContentPane().add(mainPanel);
	}

}
