package molGenExp;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;


public class MolGenExp extends JFrame {
	
	private final static String version = "1.0";
	
	JTabbedPane explorerPane;
	
	private JPanel biochemPanel;
	private biochem.Protex protex;
	
	private JPanel molBiolPanel;
	private molBiol.Genex genex;
	
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
		this.setSize(new Dimension(screenSize.width, screenSize.height * 9/10));
		
		explorerPane = new JTabbedPane();
		explorerPane.setSize(new Dimension(screenSize.width * 8/10,
				screenSize.height * 8/10));
		
		biochem.Protex protex = new biochem.Protex();
		explorerPane.addTab("Biochemistry", protex);
		
		molBiol.Genex genex = new molBiol.Genex();
		explorerPane.addTab("Molecular Biology", genex);
		
		add(explorerPane);
		
	}
	
}
