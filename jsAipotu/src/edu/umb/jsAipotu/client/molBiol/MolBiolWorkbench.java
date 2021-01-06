package edu.umb.jsAipotu.client.molBiol;/* this is the main class - the genex application * written by Brian White 2004 *  brian.white@umb.edu *   This program is free software; you can redistribute it and/or * modify it under the terms of the GNU General Public License * as published by the Free Software Foundation; either version 2 * of the License, or (at your option) any later version. * * This program is distributed in the hope that it will be useful, * but WITHOUT ANY WARRANTY; without even the implied warranty of * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the * GNU General Public License for more details. * */ import java.awt.Color;import java.util.ArrayList;import javax.swing.JLabel;import javax.swing.JOptionPane;import javax.swing.border.LineBorder;import com.google.gwt.canvas.dom.client.CssColor;import com.google.gwt.user.cellview.client.CellList;import com.google.gwt.user.client.ui.CaptionPanel;import com.google.gwt.user.client.ui.Image;import com.google.gwt.user.client.ui.ScrollPanel;import com.google.gwt.user.client.ui.VerticalPanel;import edu.umb.jsAipotu.client.biochem.FoldedProteinWithImages;import edu.umb.jsAipotu.client.biochem.FoldingException;import edu.umb.jsAipotu.client.molGenExp.CombinedColorPanel;import edu.umb.jsAipotu.client.molGenExp.MolGenExp;import edu.umb.jsAipotu.client.molGenExp.Organism;import edu.umb.jsAipotu.client.molGenExp.OrganismFactory;import edu.umb.jsAipotu.client.molGenExp.WorkPanel;import edu.umb.jsAipotu.client.molGenExp.Workbench;import edu.umb.jsAipotu.client.preferences.GlobalDefaults;import edu.umb.jsAipotu.client.resources.Resources;public class MolBiolWorkbench extends Workbench {		MolGenExp mge;	MolBiolWorkpanel upperMolBiolWorkpanel;	MolBiolWorkpanel lowerMolBiolWorkpanel;	MolBiolParams params;		private OrganismFactory organismFactory;		private CombinedColorPanel combinedColorPanel;		ArrayList<ExpressedGeneWithImages> expressedGenes;	CellList<ExpressedGeneWithImages> molBiolHistList;	ScrollPanel histListScrollPanel;		public MolBiolWorkbench(MolGenExp mge) {		super(mge);		this.mge = mge;				params = new MolBiolParams();				organismFactory = new OrganismFactory();				VerticalPanel leftPanel = new VerticalPanel();		CaptionPanel geneticCodePanel = new CaptionPanel("Genetic Code");		geneticCodePanel.add(new Image(Resources.INSTANCE.geneticCodeImage()));		leftPanel.add(geneticCodePanel);				CaptionPanel histListPanel = new CaptionPanel("History List");		molBiolHistList = new CellList<ExpressedGeneWithImages>(new MolBiolHistListItem());		histListScrollPanel = new ScrollPanel(molBiolHistList);		leftPanel.add(histListPanel);				add(leftPanel);				VerticalPanel centerPanel = new VerticalPanel();		upperMolBiolWorkpanel = new MolBiolWorkpanel(				"Upper Gene Window",				this, 				mge);		lowerMolBiolWorkpanel = new MolBiolWorkpanel(				"Lower Gene Window",				this,				mge);		centerPanel.add(upperMolBiolWorkpanel);		combinedColorPanel = new CombinedColorPanel();		centerPanel.add(combinedColorPanel);		centerPanel.add(lowerMolBiolWorkpanel);				add(centerPanel);	}		public void updateCombinedColor() {		if ((upperMolBiolWorkpanel == null) || (lowerMolBiolWorkpanel == null)) {			return;		}		if ((upperMolBiolWorkpanel.getColor() != null) 				&& (lowerMolBiolWorkpanel.getColor() != null)) {			CssColor combined = GlobalDefaults.colorModel.mixTwoColors(					upperMolBiolWorkpanel.getColor(), 					lowerMolBiolWorkpanel.getColor());			combinedColorPanel.setCombinedColor(combined);		}		else {			combinedColorPanel.setCombinedColor(CssColor.make("lightgray"));		}	}		public void saveOrganismToGreenhouse() throws FoldingException {		// fold proteins if not aready done		if (upperMolBiolWorkpanel.getColor() == null) {			upperMolBiolWorkpanel.foldExpressedProtein();		}				if (lowerMolBiolWorkpanel.getColor() == null) {			lowerMolBiolWorkpanel.foldExpressedProtein();		}				Organism o = organismFactory.createOrganism("",				upperMolBiolWorkpanel.getCurrentGene(),				lowerMolBiolWorkpanel.getCurrentGene());				mge.saveOrganismToGreenhouse(o);			}		public void loadOrganism(Organism o) {		upperMolBiolWorkpanel.setCurrentGene(o.getGene1());		lowerMolBiolWorkpanel.setCurrentGene(o.getGene2());	}			public MolGenExp getMolGenExp() {		return mge;	}		public void addToHistoryList(Object o) {		expressedGenes.add((ExpressedGeneWithImages) o);		molBiolHistList.setRowData(expressedGenes);		updateCombinedColor();	}		public WorkPanel getLowerPanel() {		return lowerMolBiolWorkpanel;	}		public WorkPanel getUpperPanel() {		return upperMolBiolWorkpanel;	}		public void sendToLowerPanel(Object o) {		lowerMolBiolWorkpanel.setCurrentGene(				((ExpressedGeneWithImages)o).getEFG());	}		public void sendToUpperPanel(Object o) {		upperMolBiolWorkpanel.setCurrentGene(				((ExpressedGeneWithImages)o).getEFG());	}			}