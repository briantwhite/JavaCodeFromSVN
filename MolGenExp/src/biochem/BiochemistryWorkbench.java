package biochem;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import utilities.ColorModel;
import utilities.GlobalDefaults;

import molGenExp.CombinedColorPanel;
import molGenExp.HistoryList;
import molGenExp.MolGenExp;
import molGenExp.Organism;
import molGenExp.WorkPanel;
import molGenExp.Workbench;


public class BiochemistryWorkbench extends Workbench {

	public static final Color BACKGROUND_COLOR = new Color((float) 0.7,
			(float) 0.7, (float) 1.0);

	private BiochemistryWorkpanel upperWorkPanel;
	private BiochemistryWorkpanel lowerWorkPanel;
	private HistoryList proteinHistoryList;
	private JScrollPane histListScrollPane;
	private CombinedColorPanel combinedColorPanel;

	ProteinPrinter printer;

	File outFile;

	private MolGenExp mge;

	public BiochemistryWorkbench(MolGenExp mge) {
		super(mge);
		this.mge = mge;
		printer = new ProteinPrinter();
		outFile = null;
		setupUI();
	}


	class ApplicationCloser extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			System.exit(0);
		}
	}

	private void setupUI() {

		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
		leftPanel.add(Box.createRigidArea(new Dimension(200,1)));
		JPanel aapPanel = new JPanel();
		aapPanel.setLayout(new BoxLayout(aapPanel, BoxLayout.X_AXIS));
		aapPanel.setBorder(BorderFactory.createTitledBorder("Amino acids"));
		AminoAcidPalette aaPalette = new AminoAcidPalette(180, 225, 5, 4);
		aapPanel.setMaximumSize(new Dimension(200, 250));
		aapPanel.add(Box.createRigidArea(new Dimension(1,225)));
		aapPanel.add(aaPalette);

		JPanel histListPanel = new JPanel();
		histListPanel.setBorder(
				BorderFactory.createTitledBorder("History List"));
		histListPanel.setLayout(new BoxLayout(histListPanel, BoxLayout.Y_AXIS));
		proteinHistoryList = new HistoryList(
				new DefaultListModel(), 
				this, 
				new ProteinHistoryCellRenderer(),
				false);
		histListScrollPane = new JScrollPane(proteinHistoryList);
		histListPanel.add(histListScrollPane);

		leftPanel.add(aapPanel);
		leftPanel.add(histListPanel);

		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
		upperWorkPanel = new BiochemistryWorkpanel("Upper Folding Window", this);
		lowerWorkPanel = new BiochemistryWorkpanel("Lower Folding Window", this);
		rightPanel.add(upperWorkPanel);
		combinedColorPanel = new CombinedColorPanel();
		rightPanel.add(combinedColorPanel);
		rightPanel.add(lowerWorkPanel);

		JPanel mainPanel = new JPanel();

		mainPanel.setLayout(
				new BoxLayout(mainPanel, BoxLayout.X_AXIS));
		mainPanel.add(leftPanel);
		mainPanel.add(rightPanel);

		setLayout(new BorderLayout());
		add(mainPanel, BorderLayout.CENTER);
	}

	public void updateCombinedColor() throws PaintedInACornerFoldingException {
		Color u = upperWorkPanel.getColor();
		Color l = lowerWorkPanel.getColor();
		Color combined = GlobalDefaults.colorModel.mixTwoColors(u, l);
		combinedColorPanel.setCombinedColor(combined);
	}

	public void loadOrganism(Organism o) throws PaintedInACornerFoldingException {
		upperWorkPanel.setFoldedPolypeptide(
				o.getGene1().getFoldedPolypeptide());
		lowerWorkPanel.setFoldedPolypeptide(
				o.getGene2().getFoldedPolypeptide());
	}

	public void addToHistoryList(Object o) 
	throws PaintedInACornerFoldingException {
		proteinHistoryList.add(o);
		histListScrollPane.revalidate();
		histListScrollPane.repaint();
		updateCombinedColor();
	}

	public WorkPanel getLowerPanel() {
		return lowerWorkPanel;
	}

	public WorkPanel getUpperPanel() {
		return upperWorkPanel;
	}

	public void sendToLowerPanel(Object o) throws PaintedInACornerFoldingException {
		lowerWorkPanel.setFoldedPolypeptide((FoldedPolypeptide)o);
	}

	public void sendToUpperPanel(Object o) throws PaintedInACornerFoldingException {
		upperWorkPanel.setFoldedPolypeptide((FoldedPolypeptide)o);
	}
}

