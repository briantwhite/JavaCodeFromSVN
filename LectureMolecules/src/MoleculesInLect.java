import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jmol.adapter.smarter.SmarterJmolAdapter;
import org.jmol.api.JmolAdapter;
import org.jmol.api.JmolStatusListener;
import org.jmol.api.JmolViewer;

/*
 * Created on Apr 19, 2005
 *
 */

/**
 * @author brian
 *
 */
public class MoleculesInLect extends JFrame {
	
	
	private JLabel captionLabel;
	JTabbedPane lecturePane;
	JPanel moleculePanel;
	JmolPanel jmolPanel;
	JmolViewer viewer;
	
	public MoleculesInLect() {
		super("Molecules in Lecture 1.2");
		moleculePanel = new JPanel();
		lecturePane = new JTabbedPane();
		jmolPanel = new JmolPanel();
	    viewer = jmolPanel.getViewer();
	    captionLabel = new JLabel("<html><font size=+4 color=white>"
				+ "Welcome!"
				+ "</font></html>");
	    
		addWindowListener(new ApplicationCloser());
		Container contentPane = getContentPane();
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.X_AXIS));
		
		moleculePanel.add(jmolPanel);
		jmolPanel.setPreferredSize(new Dimension(600,600));
		moleculePanel.add(captionLabel);
		captionLabel.setOpaque(true);
		captionLabel.setBackground(Color.BLACK);
		captionLabel.setPreferredSize(new Dimension(600,100));
		moleculePanel.setLayout(new BoxLayout(moleculePanel, BoxLayout.Y_AXIS));
		moleculePanel.setOpaque(true);
		moleculePanel.setBackground(Color.BLACK);
		contentPane.add(moleculePanel);

		lecturePane.addTab("Hemoglobin I", OxyDeoxyPanel.make(captionLabel, jmolPanel, viewer));
		lecturePane.addTab("Hemoglobin II", HemoLect1Panel.make(captionLabel, jmolPanel, viewer));
		lecturePane.addTab("Hemoglobin III", HemoLect2Panel.make(captionLabel, jmolPanel, viewer));
		lecturePane.addTab("Alkaline Phosphatase", ApLectPanel.make(captionLabel, jmolPanel, viewer));
		lecturePane.addTab("Catalase", CatLectPanel.make(captionLabel, jmolPanel, viewer));
		lecturePane.addTab("Membranes", MembranePanel.make(captionLabel, jmolPanel, viewer));
		lecturePane.addTab("DNA", DnaPanel.make(captionLabel, jmolPanel, viewer));

		lecturePane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				viewer.evalString("zap;"
						+ "spacefill off;"
						+ "wireframe off;"
						+ "backbone off;"
						+ "slab off;"
						+ "spin off;"
						+ "cartoon off;"
						+ "hbonds off;"
						+ "dots off;");
				captionLabel.setText("");
			}
			
		});
		contentPane.add(lecturePane);
	}
	

	public static void main(String[] args) {
		JFrame myMolecs = new MoleculesInLect();
		myMolecs.pack();
		myMolecs.setVisible(true);
	}
	
	static class ApplicationCloser extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			System.exit(0);
		}
	}
	
	
	
}
