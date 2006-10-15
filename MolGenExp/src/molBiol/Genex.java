package molBiol;/* this is the main class - the genex application * written by Brian White 2004 *  brian.white@umb.edu *   This program is free software; you can redistribute it and/or * modify it under the terms of the GNU General Public License * as published by the Free Software Foundation; either version 2 * of the License, or (at your option) any later version. * * This program is distributed in the hope that it will be useful, * but WITHOUT ANY WARRANTY; without even the implied warranty of * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the * GNU General Public License for more details. * */ import java.awt.Color;import java.awt.Dimension;import java.awt.GridLayout;import javax.swing.BorderFactory;import javax.swing.Box;import javax.swing.BoxLayout;import javax.swing.DefaultListModel;import javax.swing.JLabel;import javax.swing.JOptionPane;import javax.swing.JPanel;import javax.swing.JScrollPane;import molGenExp.ColorModel;import molGenExp.MolGenExp;import molGenExp.Organism;import molGenExp.RYBColorModel;import biochem.FoldedPolypeptide;public class Genex extends JPanel {	MolGenExp mge;	GeneExpressionWindow upperGeneExpWin;	GeneExpressionWindow lowerGeneExpWin;	GenexParams params;		ColorModel colorModel;		MiddleButtonPanel middleButtonPanel;		DNAHistoryList dnaHistoryList;	JScrollPane histListScrollPane;	public Genex(MolGenExp mge) {		super();		this.mge = mge;				params = new GenexParams();				colorModel = new RYBColorModel();				JPanel leftPanel = new JPanel();		dnaHistoryList = new DNAHistoryList(new DefaultListModel(), mge);		histListScrollPane = new JScrollPane(dnaHistoryList);		histListScrollPane.setBorder(				BorderFactory.createTitledBorder("History List"));		histListScrollPane.setMaximumSize(new Dimension(250,1000));		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));		leftPanel.add(Box.createRigidArea(new Dimension(250,1)));		leftPanel.add(histListScrollPane);		middleButtonPanel = new MiddleButtonPanel(this);				JPanel genexPanel = new JPanel();		genexPanel.setLayout(new GridLayout(2,1));		upperGeneExpWin = new GeneExpressionWindow(				"Upper Gene Window", 				params, 				colorModel,				this, 				mge);		lowerGeneExpWin = new GeneExpressionWindow(				"Lower Gene Window", 				params, 				colorModel,				this,				mge);		genexPanel.add(upperGeneExpWin);		genexPanel.add(lowerGeneExpWin);		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));		add(leftPanel);		add(middleButtonPanel);		add(genexPanel);	}	public void addExpressedToHistoryList(ExpressedGene gene) {		dnaHistoryList.add(gene);		histListScrollPane.revalidate();		histListScrollPane.repaint();		updateCombinedColor();	}		public void updateCombinedColor() {		if ((upperGeneExpWin == null) || (lowerGeneExpWin == null)) {			return;		}		if ((upperGeneExpWin.getColor() != null) 				&& (lowerGeneExpWin.getColor() != null)) {			Color combined = colorModel.mixTwoColors(					upperGeneExpWin.getColor(), 					lowerGeneExpWin.getColor());			middleButtonPanel.setCombinedColor(combined);		}		else {			middleButtonPanel.setCombinedColor(Color.LIGHT_GRAY);		}	}	public void sendSelectedGenetoUP() {		if (dnaHistoryList.getSelectedValue() != null) {			upperGeneExpWin.setCurrentGene(					(ExpressedGene)dnaHistoryList.getSelectedValue());		}	}		public void sendSelectedGenetoLP() {		if (dnaHistoryList.getSelectedValue() != null) {			lowerGeneExpWin.setCurrentGene(					(ExpressedGene)dnaHistoryList.getSelectedValue());		}	}		public void makeOrganism() {		// fold proteins if not aready done		if (upperGeneExpWin.getColor() == null) {			upperGeneExpWin.foldExpressedProtein();		}				if (lowerGeneExpWin.getColor() == null) {			lowerGeneExpWin.foldExpressedProtein();		}		Organism o = new Organism(				upperGeneExpWin.getCurrentGene(),				lowerGeneExpWin.getCurrentGene(),				colorModel);		JOptionPane.showMessageDialog(				this,				"",				"Organism",				JOptionPane.INFORMATION_MESSAGE,				o.getImage());	}}