package foldingServer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.Date;
import java.util.StringTokenizer;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

public class FoldingWindow extends JPanel {
	
	final Protex protex;
	
	JPanel proteinPanel;
	JTextField proteinSequence;
	TripleLetterCodeDocument tlcDoc;
	OutputPalette outputPalette;
	
	JPanel buttonPanel;
	JButton foldButton;
//	JLabel timeToFoldLabel;
	JComboBox ssBondChoice;
	boolean ssBondsOn;
	
	Polypeptide polypeptide;
	Attributes attributes;
	FoldingManager manager;
	JScrollPane proteinScrollPane;

	StandardTable table;
	
	FoldedPolypeptide foldedPolypeptide;
	BufferedImage fullSizePic;
	
	// fix thumbnail size from WIDTH and HEIGHT of full-size image;
	//	these values (in pixels) are for scaling purposes
	private static final int thumbWidth = 130;
	private static final int thumbHeight = 70;

	
	public FoldingWindow(String title, final Protex protex) {
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
		outputPalette = new OutputPalette();
		proteinScrollPane = new JScrollPane(outputPalette);
		proteinPanel.add(proteinSequence, BorderLayout.NORTH);
		proteinPanel.add(proteinScrollPane, BorderLayout.CENTER);
				
//		timeToFoldLabel = new JLabel("");
//		timeToFoldLabel.setFont((timeToFoldLabel.getFont()).deriveFont(9.0f));
		tlcDoc.setLinkedFoldingWindow(this);
		
		
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
		buttonPanel.add(Box.createHorizontalGlue());
		foldButton = new JButton("FOLD");
		foldButton.setEnabled(false);
		buttonPanel.add(foldButton);
		buttonPanel.add(Box.createHorizontalGlue());
		String[] ssChoices = {"Disulfide Bonds OFF", "Disulfide Bonds ON"};
		ssBondChoice = new JComboBox(ssChoices);
		buttonPanel.add(ssBondChoice);
		ssBondsOn = false;
//		buttonPanel.add(timeToFoldLabel); 
		
		this.add(proteinPanel, BorderLayout.CENTER);
		this.add(buttonPanel, BorderLayout.SOUTH);
		
		manager = FoldingManager.getInstance();
		
		table = new StandardTable();
		
		foldButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				String ssBondIndex = "";
				if (ssBondChoice.getSelectedIndex() == 0) {
					ssBondsOn = false;
					ssBondIndex = "0.0";
				} else {
					ssBondsOn = true;
					ssBondIndex = "1.5";
				}

				attributes = new Attributes(proteinSequence.getText().trim(), 
						3, ssBondIndex, "straight", "test");

//				Date start = new Date();
				//fold the polypeptide
				try {
					manager.fold(attributes);
				} catch (FoldingException e) {
					e.printStackTrace();
				}	
//				long timeToFold = (new Date().getTime()) - start.getTime();
//				timeToFoldLabel.setText(timeToFold + " ms");
				
				//display it
				manager.createCanvas(outputPalette);
				Dimension requiredCanvasSize = 
					outputPalette.getDrawingPane().getRequiredCanvasSize();
				outputPalette.setssBondsOn(ssBondsOn);
				outputPalette.repaint();
				
				ProteinImageSet images = 
					ProteinImageFactory.generateImages(outputPalette);
				
				foldedPolypeptide = new FoldedPolypeptide(ssBondsOn,
						proteinSequence.getText().trim(),
						outputPalette.getDrawingPane().getGrid(), 
						new ImageIcon(images.getFullScaleImage()),
						new ImageIcon(images.getThumbnailImage()));
				protex.addFoldedToHistList(foldedPolypeptide);
				
				foldButton.setEnabled(false);
				outputPalette.setBackground(Color.lightGray);
				
			}	
		});
		
		ssBondChoice.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				aaSeqChanged();
			}
		});
		
	}
	
	public String getAaSeq() {
		return proteinSequence.getText();
	}
	
	public FoldedPolypeptide getFoldedPolypeptide() {
		return foldedPolypeptide;
	}
	
	public OutputPalette getOutputPalette() {
		return outputPalette;
	}
	
	public BufferedImage getFullSizePic() {
		return fullSizePic;
	}
	
	//callback from the Document in the text field when
	// the aa seq is changed
	public void aaSeqChanged() {
		foldButton.setEnabled(true);
		outputPalette.setBackground(Color.PINK);	
//		timeToFoldLabel.setText("");
	}
	
	public void setFoldedPolypeptide(FoldedPolypeptide fp){
		
		foldedPolypeptide = fp;

		//replace amino acid sequence
		String aaSeq = fp.getAaSeq();
		StringBuffer abAASeq = new StringBuffer();
		StringTokenizer st = new StringTokenizer(aaSeq);
		while (st.hasMoreTokens()){
			AminoAcid aa = table.get(st.nextToken());
			abAASeq.append(aa.getAbName());
		}
		((TripleLetterCodeDocument)
				proteinSequence.getDocument()).removeAll();
		proteinSequence.setText(abAASeq.toString());
		
		//send the picture of the folded protein
		outputPalette.getDrawingPane().setGrid(fp.getFullSizeGrid());
		outputPalette.getDrawingPane().repaint();
		outputPalette.getDrawingPane().revalidate();		
		
		//update the picture as well
		Dimension requiredCanvasSize = 
			outputPalette.getDrawingPane().getRequiredCanvasSize();
		
		fullSizePic = new BufferedImage(requiredCanvasSize.width, 
				requiredCanvasSize.height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = fullSizePic.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
				RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		outputPalette.getDrawingPane().paint(g);
		g.dispose();
				
		ssBondsOn = fp.getssBondsOn();
		outputPalette.setssBondsOn(fp.getssBondsOn());
		if (ssBondsOn) {
			ssBondChoice.setSelectedIndex(1);
		} else {
			ssBondChoice.setSelectedIndex(0);
		}
		
		outputPalette.setBackground(Color.LIGHT_GRAY);
		foldButton.setEnabled(false);

	}
}
