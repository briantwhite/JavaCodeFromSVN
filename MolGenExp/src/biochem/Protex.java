package biochem;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.StringTokenizer;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import match.Blosum50;
import match.NWSmart;
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

	//compute differences between aa sequences of upper and lower proteins
	public void computeDifference() {
		if (upperFoldingWindow.getFoldedPolypeptide() == null) {
			JOptionPane.showMessageDialog(this, 
					"No protein in Upper Folding Window.",
					"Can't compare sequences.",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		if (lowerFoldingWindow.getFoldedPolypeptide() == null) {
			JOptionPane.showMessageDialog(this, 
					"No protein in Lower Folding Window.",
					"Can't compare sequences.",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		String[] alignment =
			(new NWSmart(new Blosum50(),
					8,
					convert3LetterTo1Letter(
							upperFoldingWindow.getAaSeq()),
							convert3LetterTo1Letter(
									lowerFoldingWindow.getAaSeq()))).getMatch();

		String upperAlignedSequence = convert1LetterTo3Letter(alignment[0]);
		String lowerAlignedSequence = convert1LetterTo3Letter(alignment[1]);
		
		//mark the differences
		StringBuffer differenceBuffer = new StringBuffer();
		for (int i = 0; i < alignment[0].length(); i++){
			if (alignment[0].charAt(i) != alignment[1].charAt(i)) {
				differenceBuffer.append("*** ");
			} else {
				differenceBuffer.append("    ");
			}
		}
		String differenceString = differenceBuffer.toString();
		
		final JEditorPane differencePane = new JEditorPane();
		differencePane.setEditable(false);
		differencePane.setContentType("text/html");
		JDialog differenceDialog = new JDialog(mge, 
				"Differences between Upper and Lower Amino Acid Sequences.");
		differenceDialog.getContentPane().add(differencePane);
		differencePane.setText("<html><pre>\n"
				+ "Upper Sequence: "
				+ upperAlignedSequence
				+ "\n"
				+ "<font color=red>Differences:    "
				+ differenceString 
				+ "</font>\n"
				+ "Lower Sequence: "
				+ lowerAlignedSequence
				+ "\n"
				+ "</pre></html>");
		differenceDialog.pack();
		differenceDialog.setVisible(true);
	}

	public String convert3LetterTo1Letter(String aaSeq) {
		StandardTable table = new StandardTable();
		StringBuffer abAASeq = new StringBuffer();
		StringTokenizer st = new StringTokenizer(aaSeq);
		while (st.hasMoreTokens()){
			AminoAcid aa = table.get(st.nextToken());
			abAASeq.append(aa.getAbName());
		}
		return abAASeq.toString();
	}

	public String convert1LetterTo3Letter(String abAASeq) {
		StandardTable table = new StandardTable();
		StringBuffer aaSeq = new StringBuffer();
		for (int i = 0; i < abAASeq.length(); i++) {
			String aa = String.valueOf(abAASeq.charAt(i));
			if (aa.equals("-")) {
				aaSeq.append("--- ");
			} else {
				aaSeq.append(table.getFromAbName(aa));
				aaSeq.append(" ");
			}
		}
		return aaSeq.toString();
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
