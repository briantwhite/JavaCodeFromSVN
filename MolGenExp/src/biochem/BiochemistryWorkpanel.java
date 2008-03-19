package biochem;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.StringTokenizer;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import utilities.ColorModel;
import utilities.GlobalDefaults;

import molGenExp.MolGenExp;
import molGenExp.ProteinImageFactory;
import molGenExp.ProteinImageSet;
import molGenExp.WorkPanel;

public class BiochemistryWorkpanel extends WorkPanel {

	final BiochemistryWorkbench protex;

	JPanel proteinPanel;
	JTextField proteinSequence;
	TripleLetterCodeDocument tlcDoc;
	OutputPalette foldedProtein;

	JPanel buttonPanel;
	JButton foldButton;
	JButton loadSampleButton;
	JLabel colorLabel;
	JLabel colorChip;

	Polypeptide polypeptide;
	FoldingManager manager;

	StandardTable table;

	FoldedPolypeptide foldedPolypeptide;
	BufferedImage fullSizePic;

	public BiochemistryWorkpanel(String title, 
			final BiochemistryWorkbench protex) {
		super();
		this.setLayout(new BorderLayout());
		this.setBorder(BorderFactory.createTitledBorder(title));
		this.setBackground(Color.lightGray);

		this.protex = protex;

		foldedPolypeptide = null;

		fullSizePic = null;

		proteinPanel = new JPanel();
		proteinPanel.setLayout(new BorderLayout());
		tlcDoc = new TripleLetterCodeDocument();
		proteinSequence = new JTextField(50);
		proteinSequence.setBorder(BorderFactory.createTitledBorder("Amino Acid Sequence"));
		proteinSequence.setDocument(tlcDoc);
		foldedProtein = new OutputPalette();
		proteinPanel.add(proteinSequence, BorderLayout.NORTH);
		proteinPanel.add(foldedProtein, BorderLayout.CENTER);

		loadSampleButton = new JButton("Load Sample Protein");

		foldButton = new JButton("FOLD");
		foldButton.setEnabled(false);
		tlcDoc.setLinkedFoldingWindow(this);

		colorLabel = new JLabel("Color:");
		colorChip = new JLabel("     ");
		colorChip.setOpaque(true);
		colorChip.setBackground(Color.WHITE);
		colorChip.setBorder(new LineBorder(Color.BLACK));

		buttonPanel = new JPanel();
		buttonPanel.add(foldButton);
		buttonPanel.add(colorLabel);
		buttonPanel.add(colorChip);
		buttonPanel.add(loadSampleButton);

		this.add(proteinPanel, BorderLayout.CENTER);
		this.add(buttonPanel, BorderLayout.SOUTH);

		manager = FoldingManager.getInstance();

		table = new StandardTable();

		foldButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				//fold the polypeptide
				try {
					manager.fold(proteinSequence.getText().trim());
				} catch (FoldingException e) {
					e.printStackTrace();
				}	

				//display it
				manager.createCanvas(foldedProtein);
				Dimension requiredCanvasSize = 
					foldedProtein.getDrawingPane().getRequiredCanvasSize();
				foldedProtein.repaint();
				Color proteinColor = foldedProtein.getProteinColor();
				colorChip.setBackground(proteinColor);

				//make full size and thumbnail images
				ProteinImageSet images = 
					ProteinImageFactory.generateImages(
							foldedProtein,
							requiredCanvasSize);

				foldedPolypeptide = new FoldedPolypeptide(
						proteinSequence.getText().trim(),
						foldedProtein.getDrawingPane().getGrid(), 
						new ImageIcon(images.getThumbnailImage()), 
						proteinColor);
				protex.addToHistoryList(foldedPolypeptide);
				images = null; 

				foldButton.setEnabled(false);
				foldedProtein.setBackground(Color.lightGray);

			}	
		});

		loadSampleButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				proteinSequence.setText(GlobalDefaults.sampleProtein);
				aaSeqChanged();
			}
		});

	}

	public String getAaSeq() {
		return proteinSequence.getText();
	}

	public Color getColor() {
		if (foldedProtein.getProteinColor() == null) {
			return Color.WHITE;
		}
		return foldedProtein.getProteinColor();
	}

	public FoldedPolypeptide getFoldedPolypeptide() {
		return foldedPolypeptide;
	}

	public BufferedImage getFullSizePic() {
		return fullSizePic;
	}

	//callback from the Document in the text field when
	// the aa seq is changed
	public void aaSeqChanged() {
		foldButton.setEnabled(true);
		foldedProtein.setBackground(Color.PINK);		
	}

	public void setFoldedPolypeptide(FoldedPolypeptide fp){

		foldedPolypeptide = fp;
		
		//process amino acid sequence
		PolypeptideFactory factory = PolypeptideFactory.getInstance();
		AminoAcid[] acids = null;
		try {
			acids = factory.parseInputStringToAmAcArray(fp.getAaSeq());
		} catch (FoldingException e) {
			e.printStackTrace();
		}
		((TripleLetterCodeDocument)
				proteinSequence.getDocument()).removeAll();
		StringBuffer abAASeq = new StringBuffer();
		for (int i = 0; i < acids.length; i++) {
			abAASeq.append(acids[i].getAbName());
		}
		proteinSequence.setText(abAASeq.toString());


		//send the picture of the folded protein
		foldedProtein.getDrawingPane().setGrid(fp.getFullSizeGrid());
		foldedProtein.getDrawingPane().repaint();
		foldedProtein.getDrawingPane().revalidate();

		//update the color chip on the folding window
		colorChip.setBackground(fp.getColor());

		//update the combined cholor chip
		protex.updateCombinedColor();


		//update the picture as well
		Dimension requiredCanvasSize = 
			foldedProtein.getDrawingPane().getRequiredCanvasSize();

		fullSizePic = new BufferedImage(requiredCanvasSize.width, 
				requiredCanvasSize.height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = fullSizePic.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
				RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		foldedProtein.getDrawingPane().paint(g);
		g.dispose();

		foldedProtein.setBackground(Color.LIGHT_GRAY);
		foldButton.setEnabled(false);

	}
}
