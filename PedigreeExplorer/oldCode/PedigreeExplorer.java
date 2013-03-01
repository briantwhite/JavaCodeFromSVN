package PE;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;

import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import Pelican.Pelican;
import Pelican.PelicanPerson;

public class PedigreeExplorer extends JFrame {

	private static String VERSION = "0.9";
	private static int RIGHT_PANEL_WIDTH = 200;
	private Pelican pelican;
	private JPanel rightPanel;
	private JLabel modelLabel;

	public PedigreeExplorer() {
		super("Pedigree Explorer " + VERSION);
		addWindowListener(new ApplicationCloser());

		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));
		pelican = new Pelican();
		pelican.setPedEx(this);
		pelican.setPreferredSize(new Dimension(600,400));
		add(pelican);

		rightPanel = new JPanel();
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
		JPanel controlPanel = new JPanel();
		JButton solveButton = new JButton("Solve");
		controlPanel.add(solveButton);
		solveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				solvePedigree();
			}
		});
		rightPanel.add(controlPanel);
		modelLabel = new JLabel();
		modelLabel.setPreferredSize(new Dimension(RIGHT_PANEL_WIDTH, 200));
		modelLabel.setMinimumSize(new Dimension(RIGHT_PANEL_WIDTH, 200));
		modelLabel.setMaximumSize(new Dimension(RIGHT_PANEL_WIDTH, 200));
		rightPanel.add(modelLabel);
		
		rightPanel.add(Box.createRigidArea(new Dimension(RIGHT_PANEL_WIDTH,2)));
		add(rightPanel);

		setPreferredSize(new Dimension(750, 680));

		setLocation(50,50);
		pack();
		JMenuBar menuBar = pelican.createMenuBar();
		setJMenuBar(menuBar);
		setVisible(true);
	}

	class ApplicationCloser extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			System.exit(0);
		}
	}

	public static void main(String[] args) {
		PedigreeExplorer pe = new PedigreeExplorer();
	}

	public Pelican getPelican() {
		return pelican;
	}

	public void modelSelectionChanged(int newChoiceNumber) {
		if (newChoiceNumber == 0) {
			modelLabel.setText("");
		} else {
			modelLabel.setText(ModelDescriptionHTML.HTML[newChoiceNumber - 1]);
		}
	}

	private void solvePedigree() {
		Vector<PelicanPerson> people = pelican.getAllPeople();
		Iterator<PelicanPerson> it = people.iterator();
		while (it.hasNext()) {
			PelicanPerson p = it.next();
			System.out.println(p);
			System.out.println();
		}
		PedigreeSolver ps = new PedigreeSolver(pelican.getAllPeople(), pelican.getMatingList());
		ps.solve();
	}

	public JFrame getMasterFrame() {
		return this;
	}

	public int getPelicanWidth() {
		return this.getContentPane().getWidth() - rightPanel.getWidth();
	}

	public int getPelicanHeight() {
		return this.getContentPane().getHeight();
	}

}
