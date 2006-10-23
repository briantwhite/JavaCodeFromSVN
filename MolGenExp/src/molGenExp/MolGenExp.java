package molGenExp;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import molBiol.Genex;

import biochem.Protex;


public class MolGenExp extends JFrame {
	
	private final static String version = "1.0";
	
	private JPanel mainPanel;
	
	private Greenhouse greenhouse;
	
	JTabbedPane explorerPane;
	
	private JPanel biochemPanel;
	private Protex protex;
	
	private JPanel molBiolPanel;
	private Genex genex;
	
	public MolGenExp() {
		super("Molecular Genetics Explorer " + version);
		addWindowListener(new ApplicationCloser());
		setupUI();
	}

	public static void main(String[] args) {
		MolGenExp mge = new MolGenExp();
		mge.pack();
		mge.setVisible(true);
	}

	class ApplicationCloser extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			System.exit(0);
		}
	}

	private void setupUI() {
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//		this.setSize(new Dimension(screenSize.width, screenSize.height * 9/10));
		
		mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
				
		explorerPane = new JTabbedPane();
//		explorerPane.setSize(new Dimension(screenSize.width * 8/10,
//				screenSize.height * 8/10));
		
		protex = new Protex();
		explorerPane.addTab("Biochemistry", protex);
		
		genex = new Genex(this);
		explorerPane.addTab("Molecular Biology", genex);
		
		mainPanel.add(explorerPane);
		
		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
		rightPanel.add(Box.createRigidArea(new Dimension(150,1)));
		greenhouse = new Greenhouse(new DefaultListModel(), this);
		rightPanel.setMaximumSize(new Dimension(150,1000));
		rightPanel.setBorder(BorderFactory.createTitledBorder("Greenhouse"));
		rightPanel.add(greenhouse);
		
		mainPanel.add(rightPanel);

		getContentPane().add(mainPanel);
		
	}
	
	public Greenhouse getGreenhouse() {
		return greenhouse;
	}
	
	public void saveToGreenhouse(Organism o) {
		greenhouse.add(o);
	}
	
	public void loadOrganismIntoActivePanel(Organism o) {
		String selectedPane = 
			explorerPane.getSelectedComponent().getClass().toString();

		if (selectedPane.equals("class molBiol.Genex")) {
			genex.loadOrganism(o);
		}
		
		if (selectedPane.equals("class biochem.Protex")) {
			protex.loadOrganism(o);
		}
	}
	
}
