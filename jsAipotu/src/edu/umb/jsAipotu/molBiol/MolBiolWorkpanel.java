package edu.umb.jsAipotu.molBiol;/* this is the class that sets up the common GUI for the application * and the applet *  * written by Brian White 2005 *  brian.white@umb.edu *   This program is free software; you can redistribute it and/or * modify it under the terms of the GNU General Public License * as published by the Free Software Foundation; either version 2 * of the License, or (at your option) any later version. * * This program is distributed in the hope that it will be useful, * but WITHOUT ANY WARRANTY; without even the implied warranty of * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the * GNU General Public License for more details. * */ import java.awt.BorderLayout;import java.awt.Color;import java.awt.Graphics;import java.awt.Point;import java.awt.event.ActionEvent;import java.awt.event.ActionListener;import java.awt.event.KeyEvent;import java.awt.event.KeyListener;import java.awt.event.MouseEvent;import java.awt.event.MouseListener;import java.awt.image.BufferedImage;import javax.swing.BorderFactory;import javax.swing.JButton;import javax.swing.JLabel;import javax.swing.JOptionPane;import javax.swing.JPanel;import javax.swing.JScrollPane;import javax.swing.JTextPane;import javax.swing.border.LineBorder;import edu.umb.jsAipotu.biochem.FoldedProteinWithImages;import edu.umb.jsAipotu.biochem.FoldingManager;import edu.umb.jsAipotu.molGenExp.ExpressedAndFoldedGene;import edu.umb.jsAipotu.molGenExp.MolGenExp;import edu.umb.jsAipotu.molGenExp.WorkPanel;import edu.umb.jsAipotu.preferences.GlobalDefaults;import edu.umb.jsAipotu.preferences.MGEPreferences;public class MolBiolWorkpanel extends WorkPanel {	MolBiolWorkbench molBiolWorkbench;		//the parent MolBiolWorkbench that holds this window	MolGenExp mge;		//the parent application	String title;	JTextPane textPane;	JScrollPane scrollPane;	JLabel infoLabel;	JPanel buttonPanel;	JButton loadSampleButton;	JButton newSequenceButton;	JButton foldProteinButton;	JLabel colorLabel;	JLabel colorChip;	String defaultDNA;	String DNA;	String promoterSequence;	String terminatorSequence;	String intronStartSequence;	String intronEndSequence;	String polyATail;	ExpressedGene currentGene;	FoldedProteinWithImages foldedProteinWithImages;	ExpressedAndFoldedGene efg;	int headerLength;        //length (as actually displayed) of the text 	// & labels before the start of the top DNA strand	int DNASequenceLength;	String caption;         //the caption at the bottom of the frame	String previousProteinString;  //the last protein sequence displayed	String currentProteinString;	int caretPosition;  //the number of the selected DNA base	GeneExpresser geneExpresser;	public MolBiolWorkpanel(String title, 			final MolBiolWorkbench genex,			final MolGenExp mge) {		super();		this.molBiolWorkbench = genex;		this.title = title;		this.mge = mge;		MolBiolParams params = GlobalDefaults.molBiolParams;		geneExpresser = new GeneExpresser();		caretPosition = 0;		defaultDNA = params.getDefaultDNA();		DNA = params.getDefaultDNA();		DNASequenceLength = DNA.length();		promoterSequence = params.getPromoterSequence();		terminatorSequence = params.getTerminatorSequence();		intronStartSequence = params.getIntronStartSequence();		intronEndSequence = params.getIntronEndSequence();		polyATail = params.getPolyATail();		//if it's a prokaryote, no poly A tail		if (intronStartSequence.equals("none") || intronEndSequence.equals("none")) {			polyATail = "";		}		setupUI();	}	private void setupUI() {		textPane = new JTextPane();		infoLabel = new JLabel("Selected Base = ");		loadSampleButton = new JButton("Load Sample DNA");		newSequenceButton = new JButton("Enter New DNA");		foldProteinButton = new JButton("Fold Protein");		colorLabel = new JLabel("Color:");		colorChip = new JLabel("     ");		colorChip.setOpaque(true);		colorChip.setBackground(Color.WHITE);		colorChip.setBorder(new LineBorder(Color.BLACK));		scrollPane = new JScrollPane(textPane);		buttonPanel = new JPanel();		buttonPanel.add(colorLabel);		buttonPanel.add(colorChip);		buttonPanel.add(foldProteinButton);		buttonPanel.add(loadSampleButton);		buttonPanel.add(newSequenceButton);		buttonPanel.add(infoLabel);		setLayout(new BorderLayout());		add(buttonPanel, BorderLayout.SOUTH);		add(scrollPane, BorderLayout.CENTER);	 		setBorder(BorderFactory.createTitledBorder(title));		textPane.setContentType("text/html");		textPane.setDragEnabled(false);		textPane.setEditable(false);		loadSampleButton.setToolTipText("Restore the original DNA Sequence");		newSequenceButton.setToolTipText("Enter an entirely new DNA sequence");		caption = new String("");		previousProteinString = new String("");		currentProteinString = new String("");		//display a blank starting gene		DNA = "";		currentGene = geneExpresser.expressGene(DNA, -1);		textPane.setText(currentGene.getHtmlString() + caption + "</pre></body></html>");		headerLength = currentGene.getHeaderLength();		DNASequenceLength = currentGene.getDNA().length();		currentProteinString = currentGene.getProteinForDisplay();		showProteinChangedButNotFolded();		//display the gene with a selected base		textPane.addMouseListener(new MouseListener() {   			public void mouseClicked(MouseEvent e) {				Point pt = new Point(e.getX(), e.getY());				int pos = textPane.viewToModel(pt);				int clickSite = pos - headerLength;				if ((clickSite >= 0) && (clickSite <= DNASequenceLength)) {					currentGene = geneExpresser.expressGene(DNA, clickSite);					refreshDisplay(currentGene, clickSite);					DNASequenceLength = currentGene.getDNA().length();					caretPosition = clickSite;					headerLength = currentGene.getHeaderLength();				}  else {					if (textPane.getCaretPosition() !=0) {						textPane.setCaretPosition(0);					}				}			}			public void mouseEntered(MouseEvent arg0) {}			public void mouseExited(MouseEvent arg0) {}			public void mousePressed(MouseEvent arg0) {}			public void mouseReleased(MouseEvent arg0) {}		});		//allow editing of the gene		//  AGCT inserts DNA		//  agct replaces DNA		//  DELETE deletes DNA		textPane.addKeyListener(new KeyListener() {			public void keyTyped(KeyEvent e) {				String keyTyped = String.valueOf(e.getKeyChar());				int keyNum = Character.getNumericValue(e.getKeyChar());				if (keyTyped.equals("A")						|| keyTyped.equals("G")						|| keyTyped.equals("C")						|| keyTyped.equals("T") ) {					previousProteinString = currentProteinString;					StringBuffer workingDNAbuffer = new StringBuffer(DNA);					workingDNAbuffer.insert(caretPosition, keyTyped);					DNA = workingDNAbuffer.toString();					caretPosition++;					currentGene = geneExpresser.expressGene(DNA, caretPosition);					refreshDisplay(currentGene, caretPosition);					currentProteinString = currentGene.getProteinForDisplay();					DNASequenceLength = currentGene.getDNA().length();					headerLength = currentGene.getHeaderLength() + 1; 					//need the +1 otherwise, when you click on a base after moving + or -					// the selected base is n+1 - why??            					showProteinChangedButNotFolded();				}				if (keyTyped.equals("a")						|| keyTyped.equals("g")						|| keyTyped.equals("c")						|| keyTyped.equals("t") ) {					previousProteinString = currentProteinString;					StringBuffer workingDNAbuffer = new StringBuffer(DNA);					workingDNAbuffer.replace(caretPosition, 							caretPosition + 1,							keyTyped.toUpperCase());					DNA = workingDNAbuffer.toString();					currentGene = geneExpresser.expressGene(DNA, caretPosition);					refreshDisplay(currentGene, caretPosition);					currentProteinString = currentGene.getProteinForDisplay();					DNASequenceLength = currentGene.getDNA().length();					headerLength = currentGene.getHeaderLength() + 1; 					//need the +1 otherwise, when you click on a base after moving + or -					// the selected base is n+1 - why??  					showProteinChangedButNotFolded();				}				if (keyTyped.equals("+") 						|| keyTyped.equals("-")						|| keyTyped.equals("=")) {					if (keyTyped.equals("+") || keyTyped.equals("=")) {						caretPosition++;						if (caretPosition > (DNA.length() - 1)) {							caretPosition = DNA.length() - 1;						}					} else {						caretPosition--;						if (caretPosition < 0) {							caretPosition = 0;						}					}					currentGene = geneExpresser.expressGene(DNA, caretPosition);					refreshDisplay(currentGene, caretPosition);					DNASequenceLength = currentGene.getDNA().length();					headerLength = currentGene.getHeaderLength() + 1;       					//need the +1 otherwise, when you click on a base after moving + or -					// the selected base is n+1 - why??            				}			}			public void keyPressed(KeyEvent e) {				//delete				if (e.getKeyCode() == 8) {					previousProteinString = currentProteinString;					StringBuffer workingDNAbuffer = new StringBuffer(DNA);					workingDNAbuffer.deleteCharAt(caretPosition);					DNA = workingDNAbuffer.toString();					if (caretPosition >= 0) {						caretPosition--;					}					currentGene = geneExpresser.expressGene(DNA, caretPosition);					refreshDisplay(currentGene, caretPosition);					currentProteinString = currentGene.getProteinForDisplay();					DNASequenceLength = currentGene.getDNA().length();					headerLength = currentGene.getHeaderLength();					showProteinChangedButNotFolded();								}				// right arrow				if (e.getKeyCode() == 39) {					caretPosition++;					if (caretPosition > (DNA.length() - 1)) {						caretPosition = DNA.length() - 1;					}					currentGene = geneExpresser.expressGene(DNA, caretPosition);					refreshDisplay(currentGene, caretPosition);					DNASequenceLength = currentGene.getDNA().length();					headerLength = currentGene.getHeaderLength() + 1;       					//need the +1 otherwise, when you click on a base after moving + or -					// the selected base is n+1 - why??            				}				//left arrow				if (e.getKeyCode() == 37){					caretPosition--;					if (caretPosition < 0) {						caretPosition = 0;					}      						currentGene = geneExpresser.expressGene(DNA, caretPosition);					refreshDisplay(currentGene, caretPosition);					DNASequenceLength = currentGene.getDNA().length();					headerLength = currentGene.getHeaderLength() + 1;       					//need the +1 otherwise, when you click on a base after moving + or -					// the selected base is n+1 - why??            				}				//return				if ((e.getKeyCode() == 10) && foldProteinButton.isEnabled()) {					foldExpressedProtein();				}			}			public void keyReleased(KeyEvent e) {			}		});		//display the sample DNA		loadSampleButton.addActionListener(new ActionListener() {			public void actionPerformed(ActionEvent e) {				DNA = defaultDNA;				currentGene = geneExpresser.expressGene(DNA, -1);				refreshDisplay(currentGene, -1);				currentProteinString = currentGene.getProteinForDisplay();				headerLength = currentGene.getHeaderLength();				DNASequenceLength = currentGene.getDNA().length(); 				headerLength = currentGene.getHeaderLength(); 				showProteinChangedButNotFolded();			}		});		//allow the user to enter a sequence manually		newSequenceButton.addActionListener(new ActionListener() {			public void actionPerformed(ActionEvent e) {				String newDNA = (String)JOptionPane.showInputDialog(null,						"Enter new DNA Sequence",						"New DNA Sequence",						JOptionPane.PLAIN_MESSAGE,						null, null, DNA);				if (newDNA == null) return;				previousProteinString = currentProteinString;				newDNA = newDNA.toUpperCase();				newDNA = newDNA.replaceAll("[^AGCT]","");				DNA = newDNA;				caretPosition = -1;				currentGene = geneExpresser.expressGene(DNA, -1);				refreshDisplay(currentGene, -1);				currentProteinString = currentGene.getProteinForDisplay();				headerLength = currentGene.getHeaderLength();				DNASequenceLength = currentGene.getDNA().length();  				showProteinChangedButNotFolded();			}		});		//fold the protein, calculate the color, and add to history list		foldProteinButton.addActionListener(new ActionListener() {			public void actionPerformed(ActionEvent arg0) {				foldExpressedProtein();			}		});	}	public String getDNA() {		return DNA;	}	public void foldExpressedProtein() {		//process the protein sequence into a form that		// the folding routine can understand		String proteinSequence = currentGene.getProtein();		//fold it		FoldingManager foldingManager = new FoldingManager();		foldedProteinWithImages = foldingManager.foldWithPix(proteinSequence);		// see if it was folded in a corner		if (foldedProteinWithImages.getFullSizePic() == null) {			JOptionPane.showMessageDialog(this, 					GlobalDefaults.paintedInACornerNotice,					"Folding Error", JOptionPane.WARNING_MESSAGE);							return;		}		colorChip.setBackground(foldedProteinWithImages.getColor());		if (MGEPreferences.getInstance().isShowColorNameText()) {			colorChip.setToolTipText(					GlobalDefaults.colorModel.getColorName(foldedProteinWithImages.getColor()));		} else {			colorChip.setToolTipText(null);		}		showProteinFolded();		efg = new ExpressedAndFoldedGene(currentGene, foldedProteinWithImages);		//add to history list		molBiolWorkbench.addToHistoryList(				new MolBiolHistListItem(efg));	}	public void refreshDisplay(ExpressedGene eg, int selectedBase) {		if (selectedBase != -1) {			infoLabel.setText("Selected Base = " + selectedBase);		}  else {			infoLabel.setText("Selected Base = ");		}		textPane.setText(eg.getHtmlString() 				+ "<font color=blue>" + previousProteinString 				+ "</font></pre><br><br><br><font size=+1>" + caption 				+ "</font></body></html>");		textPane.setCaretPosition(0);	}	public void showProteinChangedButNotFolded() {		foldedProteinWithImages = null;		foldProteinButton.setEnabled(true);		this.setBackground(Color.pink);		buttonPanel.setBackground(Color.pink);		colorChip.setBackground(Color.LIGHT_GRAY);		colorChip.setToolTipText(null);		molBiolWorkbench.updateCombinedColor();		molBiolWorkbench.getMolGenExp().setAddToGreenhouseButtonEnabled(true);	}	public void showProteinFolded() {		foldProteinButton.setEnabled(false);		this.setBackground(Color.LIGHT_GRAY);		buttonPanel.setBackground(Color.LIGHT_GRAY);		molBiolWorkbench.getMolGenExp().setAddToGreenhouseButtonEnabled(true);	}	public Color getColor() {		if (foldedProteinWithImages != null) {			return foldedProteinWithImages.getColor();		} else {			return null;		}	}	public ExpressedAndFoldedGene getCurrentGene() {		return new ExpressedAndFoldedGene(currentGene, foldedProteinWithImages);	}	public void setCurrentGene(ExpressedAndFoldedGene efg) {		this.efg = efg;		currentGene = efg.getExpressedGene();		textPane.setText(currentGene.getHtmlString() + caption + "</pre></body></html>");		DNA = currentGene.getDNA();		headerLength = currentGene.getHeaderLength();		DNASequenceLength = DNA.length();		currentProteinString = currentGene.getProteinForDisplay();		showProteinFolded();		foldedProteinWithImages = efg.getFoldedProteinWithImages();		colorChip.setBackground(foldedProteinWithImages.getColor());		if (MGEPreferences.getInstance().isShowColorNameText() 				&& (foldedProteinWithImages != null)) {			colorChip.setToolTipText(					GlobalDefaults.colorModel.getColorName(foldedProteinWithImages.getColor()));		} else {			colorChip.setToolTipText(null);		}		molBiolWorkbench.updateCombinedColor();	}	public BufferedImage takeSnapshot() {		int width = textPane.getWidth();		int height = textPane.getHeight();		BufferedImage imageBuffer = new BufferedImage(				width,				height + 40,				BufferedImage.TYPE_INT_RGB);		Graphics g = imageBuffer.getGraphics();		//the gene		textPane.paint(g);		//fill in extra space for color chip		g.setColor(Color.WHITE);		g.fillRect(0, height, width, 40);		// the color chip		g.setColor(Color.BLACK);		g.drawString("Color:", 5, height + 15);		g.drawString(				GlobalDefaults.colorModel.getColorName(foldedProteinWithImages.getColor()),				5, height + 30);		g.setColor(foldedProteinWithImages.getColor());		g.fillRect(60, height + 5, 30, 30);		g.setColor(Color.BLACK);		g.drawRect(59, height + 4, 31, 31);		return imageBuffer;	}}