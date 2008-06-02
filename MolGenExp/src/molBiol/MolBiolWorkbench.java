package molBiol;/* this is the main class - the genex application * written by Brian White 2004 *  brian.white@umb.edu *   This program is free software; you can redistribute it and/or * modify it under the terms of the GNU General Public License * as published by the Free Software Foundation; either version 2 * of the License, or (at your option) any later version. * * This program is distributed in the hope that it will be useful, * but WITHOUT ANY WARRANTY; without even the implied warranty of * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the * GNU General Public License for more details. * */ import java.awt.Color;import java.awt.Dimension;import java.net.URL;import javax.swing.BorderFactory;import javax.swing.Box;import javax.swing.BoxLayout;import javax.swing.DefaultListModel;import javax.swing.ImageIcon;import javax.swing.JLabel;import javax.swing.JOptionPane;import javax.swing.JPanel;import javax.swing.JScrollPane;import javax.swing.SwingConstants;import javax.swing.border.LineBorder;import molGenExp.CombinedColorPanel;import molGenExp.HistoryList;import molGenExp.MolGenExp;import molGenExp.Organism;import molGenExp.OrganismFactory;import molGenExp.ProteinImageFactory;import molGenExp.ProteinImageSet;import molGenExp.WorkPanel;import molGenExp.Workbench;import utilities.GlobalDefaults;import utilities.MolBiolParams;import biochem.FoldedPolypeptide;import biochem.OutputPalette;public class MolBiolWorkbench extends Workbench {		MolGenExp mge;	MolBiolWorkpanel upperMolBiolWorkpanel;	MolBiolWorkpanel lowerMolBiolWorkpanel;	MolBiolParams params;		private OrganismFactory organismFactory;		CombinedColorPanel combinedColorPanel;		HistoryList dnaHistoryList;	JScrollPane histListScrollPane;		public MolBiolWorkbench(MolGenExp mge) {		super(mge);		this.mge = mge;				params = new MolBiolParams();				organismFactory = new OrganismFactory();				JPanel leftPanel = new JPanel();		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));				JPanel geneticCodePanel = new JPanel();		URL geneticCodePicURL = molGenExp.MolGenExp.class.getResource("/images/GeneticCode.gif"	);						JLabel geneticCodeLabel = 			new JLabel(new ImageIcon(geneticCodePicURL), SwingConstants.LEFT);		geneticCodePanel.add(geneticCodeLabel);				leftPanel.add(geneticCodePanel);				JPanel histListPanel = new JPanel();		histListPanel.setLayout(new BoxLayout(histListPanel, BoxLayout.Y_AXIS));		histListPanel.setBorder(				BorderFactory.createTitledBorder("History List"));		dnaHistoryList = new HistoryList(new DefaultListModel(), 				this, 				new DNAHistoryCellRenderer(),				true);		histListScrollPane = new JScrollPane(dnaHistoryList);		histListScrollPane.setPreferredSize(new Dimension(150,1000));		histListPanel.add(Box.createRigidArea(new Dimension(150,1)));		histListPanel.add(histListScrollPane);				leftPanel.add(histListPanel);				JPanel genexPanel = new JPanel();		genexPanel.setLayout(new BoxLayout(genexPanel, BoxLayout.Y_AXIS));		upperMolBiolWorkpanel = new MolBiolWorkpanel(				"Upper Gene Window",				this, 				mge);		lowerMolBiolWorkpanel = new MolBiolWorkpanel(				"Lower Gene Window",				this,				mge);		genexPanel.add(upperMolBiolWorkpanel);		combinedColorPanel = new CombinedColorPanel();		genexPanel.add(combinedColorPanel);		genexPanel.add(lowerMolBiolWorkpanel);				setLayout(new BoxLayout(this, BoxLayout.X_AXIS));		add(leftPanel);		add(genexPanel);	}		public void updateCombinedColor() {		if ((upperMolBiolWorkpanel == null) || (lowerMolBiolWorkpanel == null)) {			return;		}		if ((upperMolBiolWorkpanel.getColor() != null) 				&& (lowerMolBiolWorkpanel.getColor() != null)) {			Color combined = GlobalDefaults.colorModel.mixTwoColors(					upperMolBiolWorkpanel.getColor(), 					lowerMolBiolWorkpanel.getColor());			combinedColorPanel.setCombinedColor(combined);		}		else {			combinedColorPanel.setCombinedColor(Color.LIGHT_GRAY);		}	}		public void saveOrganismToGreenhouse() {		// fold proteins if not aready done		if (upperMolBiolWorkpanel.getColor() == null) {			upperMolBiolWorkpanel.foldExpressedProtein();		}				if (lowerMolBiolWorkpanel.getColor() == null) {			lowerMolBiolWorkpanel.foldExpressedProtein();		}				Organism o = organismFactory.createOrganism("",				upperMolBiolWorkpanel.getCurrentGene(),				lowerMolBiolWorkpanel.getCurrentGene());				mge.saveOrganismToGreenhouse(o);			}		public void loadOrganism(Organism o) {		upperMolBiolWorkpanel.setCurrentGene(o.getGene1());		lowerMolBiolWorkpanel.setCurrentGene(o.getGene2());	}			public MolGenExp getMolGenExp() {		return mge;	}		public void addToHistoryList(Object o) {		dnaHistoryList.add(o);		histListScrollPane.revalidate();		histListScrollPane.repaint();		updateCombinedColor();	}		public WorkPanel getLowerPanel() {		return lowerMolBiolWorkpanel;	}		public WorkPanel getUpperPanel() {		return upperMolBiolWorkpanel;	}		public void sendToLowerPanel(Object o) {		lowerMolBiolWorkpanel.setCurrentGene(				((MolBiolHistListItem)o).getEFG());	}		public void sendToUpperPanel(Object o) {		upperMolBiolWorkpanel.setCurrentGene(				((MolBiolHistListItem)o).getEFG());	}		public void showProteinCloseUp(Object o) {		FoldedPolypeptide fp = ((MolBiolHistListItem)o).getFoldedPolypeptide();		JLabel colorChipForDialog = new JLabel("     ");		colorChipForDialog.setOpaque(true);		colorChipForDialog.setBackground(fp.getColor());		colorChipForDialog.setBorder(new LineBorder(Color.BLACK));				Object[] options = {"OK",				new JLabel("Color:"),				colorChipForDialog		};				OutputPalette op = new OutputPalette();		op.getDrawingPane().setGrid(fp.getFullSizeGrid());		op.getDrawingPane().calculateRequiredCanvasSize();		ProteinImageSet images = 			ProteinImageFactory.generateImages(op, op.getDrawingPane().getRequiredCanvasSize());				JOptionPane.showOptionDialog(				mge,				"",				"Protein Structure",				JOptionPane.YES_OPTION,				JOptionPane.INFORMATION_MESSAGE,				new ImageIcon(images.getFullScaleImage()),				options,				options[0]);		images = null;	}		}