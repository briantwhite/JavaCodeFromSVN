package biochem;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.border.LineBorder;

import molGenExp.ProteinImageFactory;
import molGenExp.ProteinImageSet;
import molGenExp.WorkPanel;
import preferences.MGEPreferences;
import utilities.GlobalDefaults;

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

	Action foldProteinAction;

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
		proteinSequence.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(foldButton.isEnabled()) {
					foldProtein();
				}
			}			
		});

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

		manager = new FoldingManager();

		table = new StandardTable();

		foldButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				foldProtein();
			}						
		});

		loadSampleButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				proteinSequence.setText(GlobalDefaults.sampleProtein);
				aaSeqChanged();
			}
		});

	}

	private void foldProtein() {
		try {
			manager.fold(proteinSequence.getText().trim());

			//display it
			manager.createCanvas(foldedProtein);
			Dimension requiredCanvasSize = 
				foldedProtein.getDrawingPane().getRequiredCanvasSize();
			foldedProtein.repaint();
			Color proteinColor = foldedProtein.getProteinColor();
			colorChip.setBackground(proteinColor);
			if (MGEPreferences.getInstance().isShowColorNameText()) {
				colorChip.setToolTipText(
						GlobalDefaults.colorModel.getColorName(proteinColor));
			} else {
				colorChip.setToolTipText(null);
			}

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

		} catch (FoldingException e) {
			JOptionPane.showMessageDialog(protex, 
					GlobalDefaults.paintedInACornerNotice,
					"Folding Error", JOptionPane.WARNING_MESSAGE);
		}	

	}	




	public String getAaSeq() {
		return proteinSequence.getText();
	}

	public Color getColor() throws PaintedInACornerFoldingException {
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

	public void setFoldedPolypeptide(FoldedPolypeptide fp) 
	throws PaintedInACornerFoldingException{

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
		if (MGEPreferences.getInstance().isShowColorNameText()) {
			colorChip.setToolTipText(
					GlobalDefaults.colorModel.getColorName(fp.getColor()));
		} else {
			colorChip.setToolTipText(null);
		}

		//update the combined color chip
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

	public BufferedImage takeSnapshot() {
		Dimension requiredCanvasSize = 
			foldedProtein.getDrawingPane().getRequiredCanvasSize();
		int width = requiredCanvasSize.width;
		int height = requiredCanvasSize.height;

		BufferedImage imageBuffer = new BufferedImage(
				width,
				height + 60,
				BufferedImage.TYPE_INT_RGB);
		Graphics g = imageBuffer.getGraphics();

		//the protein
		foldedProtein.getDrawingPane().paint(g);

		//fill in extra space for aa seq and color
		g.setColor(Color.WHITE);
		g.fillRect(0, height, width, 60);

		//the amino acid sequence
		// be sure it'll fit
		String aaSeq = foldedPolypeptide.getAaSeq();
		Font defaultFont = g.getFont();
		FontMetrics defaultFm = g.getFontMetrics(defaultFont);
		int defaultWidth = defaultFm.stringWidth(aaSeq);
		if (defaultWidth > width) {
			Font smallFont = defaultFont.deriveFont(defaultFont.getSize() * 0.75f);
			g.setFont(smallFont);
			FontMetrics smallFm = g.getFontMetrics(smallFont);
			int smallWidth = smallFm.stringWidth(aaSeq);
			if (smallWidth > width) {
				Font tinyFont = defaultFont.deriveFont(defaultFont.getSize() * 0.5f);
				g.setFont(tinyFont);
			}	
		}
		g.setColor(Color.BLACK);
		g.drawString(aaSeq, 0, height + 15);

		//the color chip
		g.setFont(defaultFont);
		g.setColor(Color.BLACK);
		g.drawString("Color:", 5, height + 30);
		g.drawString(
				GlobalDefaults.colorModel.getColorName(foldedPolypeptide.getColor()),
				5, height + 45);
		g.setColor(foldedPolypeptide.getColor());
		g.fillRect(60, height + 20, 30, 30);
		g.setColor(Color.BLACK);
		g.drawRect(59, height + 19, 31, 31);

		return imageBuffer;
	}
}
