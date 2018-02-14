package YeastVGL;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class YeastVGL_GUI extends JFrame {

	YeastVGL yeastVGL;
	Pathway pathway;
	MutantSet mutantSet;
	int numEnzymes;
	int numMolecules;
		

	public YeastVGL_GUI(YeastVGL yeastVGL) {
		super("Yeast VGL 0.1");
		addWindowListener(new ApplicationCloser());
		this.yeastVGL = yeastVGL;
		this.pathway = yeastVGL.getPathway();
		this.numEnzymes = pathway.getNumberOfEnzymes();
		this.numMolecules = pathway.getNumberOfMolecules();
		this.mutantSet = yeastVGL.getMutantSet();
		setupUI();
	}
	
	class ApplicationCloser extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			System.exit(0);
		}
	}
	
	public void setupUI() {
		
		JTabbedPane mainPane = new JTabbedPane();

		ComplementationTestPanel ctp = new ComplementationTestPanel(yeastVGL);
		mainPane.addTab("Complementation Test", ctp);
		
		PathwayPanel pp = new PathwayPanel(pathway);
		mainPane.addTab("Pathway Analysis", pp);
		pp.updateDisplay();
		
		this.add(mainPane);
		this.pack();
		
	}
	

}
