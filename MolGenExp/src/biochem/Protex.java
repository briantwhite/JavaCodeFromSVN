package biochem;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import molGenExp.ColorModel;
import molGenExp.MolGenExp;
import molGenExp.Organism;


public class Protex extends JPanel {
		
	FoldingWindow upperFoldingWindow;
	FoldingWindow lowerFoldingWindow;
	ProteinHistoryList proteinHistoryList;
	JScrollPane histListScrollPane;
	ProteinMiddleButtonPanel proteinMiddleButtonPanel;
	
	ColorModel colorModel;
	
	ProteinPrinter printer;
	
	File outFile;
	
	private MolGenExp mge;

	public Protex(MolGenExp mge) {
		super();
		this.mge = mge;
		colorModel = mge.getOverallColorModel();
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
		JPanel aapPanel = new JPanel();
		aapPanel.setBorder(BorderFactory.createTitledBorder("Amino acids"));
		AminoAcidPalette aaPalette 
			= new AminoAcidPalette(180, 225, 5, 4, colorModel);
		aapPanel.setMaximumSize(new Dimension(200, 250));
		aapPanel.add(aaPalette);
		
		proteinHistoryList = new ProteinHistoryList(
				new DefaultListModel(), mge);
		histListScrollPane = new JScrollPane(proteinHistoryList);
		histListScrollPane.setBorder(
				BorderFactory.createTitledBorder("History List"));
		histListScrollPane.setMaximumSize(new Dimension(250,1000));
		
		leftPanel.add(aapPanel);
		leftPanel.add(histListScrollPane);
		
		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new GridLayout(2,1));
		upperFoldingWindow = new FoldingWindow("Upper Folding Window", this, colorModel);
		lowerFoldingWindow = new FoldingWindow("Lower Folding Window", this, colorModel);
		rightPanel.add(upperFoldingWindow);
		rightPanel.add(lowerFoldingWindow);
		
		proteinMiddleButtonPanel = new ProteinMiddleButtonPanel(this);
		proteinMiddleButtonPanel.setMaximumSize(proteinMiddleButtonPanel.getPreferredSize());
		setButtonsEnabled(false);
		
		JPanel mainPanel = new JPanel();
		
		mainPanel.setLayout(
				new BoxLayout(mainPanel, BoxLayout.X_AXIS));
		mainPanel.add(leftPanel);
		mainPanel.add(proteinMiddleButtonPanel);
		mainPanel.add(rightPanel);
		
		setLayout(new BorderLayout());
		add(mainPanel, BorderLayout.CENTER);
	}
	
	public void addFoldedToHistList(FoldedPolypeptide fp) {
		proteinHistoryList.add(fp);
		histListScrollPane.revalidate();
		histListScrollPane.repaint();
		updateCombinedColor();
	}
	
	public void updateCombinedColor() {
		Color u = upperFoldingWindow.getColor();
		Color l = lowerFoldingWindow.getColor();
		Color combined = colorModel.mixTwoColors(u, l);
		proteinMiddleButtonPanel.setCombinedColor(combined);
	}
	
	public void sendSelectedFPtoUP() {
		if (proteinHistoryList.getSelectedValue() != null) {
			FoldedPolypeptide fp =
				(FoldedPolypeptide) proteinHistoryList.getSelectedValue();
			upperFoldingWindow.setFoldedPolypeptide(fp);
		}
	}
	
	public void sendSelectedFPtoLP() {
		if (proteinHistoryList.getSelectedValue() != null){
			FoldedPolypeptide fp =
				(FoldedPolypeptide) proteinHistoryList.getSelectedValue();
			lowerFoldingWindow.setFoldedPolypeptide(fp);
		}
	}
		
	public void loadOrganism(Organism o) {
		upperFoldingWindow.setFoldedPolypeptide(
				o.getGene1().getFoldedPolypeptide());
		lowerFoldingWindow.setFoldedPolypeptide(
				o.getGene2().getFoldedPolypeptide());
	}
	
	public void setButtonsEnabled(boolean b) {
		proteinMiddleButtonPanel.setButtonsEnabled(b);
	}
}
