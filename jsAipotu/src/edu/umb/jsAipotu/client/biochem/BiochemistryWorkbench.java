package edu.umb.jsAipotu.client.biochem;

import java.util.ArrayList;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import edu.umb.jsAipotu.client.molGenExp.CombinedColorPanel;
import edu.umb.jsAipotu.client.molGenExp.MolGenExp;
import edu.umb.jsAipotu.client.molGenExp.Organism;
import edu.umb.jsAipotu.client.molGenExp.WorkPanel;
import edu.umb.jsAipotu.client.molGenExp.Workbench;


public class BiochemistryWorkbench extends Workbench {

	private BiochemistryWorkpanel upperWorkPanel;
	private BiochemistryWorkpanel lowerWorkPanel;

	ArrayList<FoldedProteinWithImages> foldedProteins;
	BiochemHistoryList biochemHistoryList;
	ScrollPanel histListScrollPanel;
	
	private CombinedColorPanel combinedColorPanel;

	private MolGenExp mge;

	public BiochemistryWorkbench(MolGenExp mge) {
		super(mge);
		this.mge = mge;
		setupUI();
	}


	private void setupUI() {

		VerticalPanel leftPanel = new VerticalPanel();
		
		CaptionPanel aapPanel = new CaptionPanel("Amino Acids");
		aapPanel.setStyleName("biochemAATable");
		Canvas aaPalette = AminoAcidPaletteBuilder.build(180, 225, 5, 4);
		aapPanel.add(aaPalette);
		leftPanel.add(aapPanel);

		CaptionPanel histListPanel = new CaptionPanel("History List");
		biochemHistoryList = new BiochemHistoryList(new BiochemHistListItem());
		//biochemHistoryList.setStyleName("biochemHistList");
		histListScrollPanel = new ScrollPanel(biochemHistoryList);
		histListPanel.add(histListScrollPanel);
		leftPanel.add(histListPanel);
		
		add(leftPanel);

		VerticalPanel centerPanel = new VerticalPanel();
		upperWorkPanel = new BiochemistryWorkpanel("Upper Folding Window", this);
		lowerWorkPanel = new BiochemistryWorkpanel("Lower Folding Window", this);
		centerPanel.add(upperWorkPanel);
		combinedColorPanel = new CombinedColorPanel();
		centerPanel.add(combinedColorPanel);
		centerPanel.add(lowerWorkPanel);
		
		add(centerPanel);
	}

	public void updateCombinedColor() {
//		CssColor u = upperWorkPanel.getColor();
//		CssColor l = lowerWorkPanel.getColor();
//		
//		if ((u == null) || (l == null)) return;
//		
//		CssColor combined = GlobalDefaults.colorModel.mixTwoColors(u, l);
//		combinedColorPanel.setCombinedColor(combined);
	}

	public void loadOrganism(Organism o) {
//		upperWorkPanel.setFoldedProteinWithImages(
//				o.getGene1().getFoldedProteinWithImages());
//		lowerWorkPanel.setFoldedProteinWithImages(
//				o.getGene2().getFoldedProteinWithImages());
	}

	public void addToHistoryList(Object o) 
	throws PaintedInACornerFoldingException {
		biochemHistoryList.add((FoldedProteinWithImages)o);
//		updateCombinedColor();
	}

	public WorkPanel getLowerPanel() {
//		return lowerWorkPanel;
		return null;
	}

	public WorkPanel getUpperPanel() {
//		return upperWorkPanel;
		return null;
	}

	public void sendToLowerPanel(Object o) throws PaintedInACornerFoldingException {
//		lowerWorkPanel.setFoldedProteinWithImages((FoldedProteinWithImages)o);
	}

	public void sendToUpperPanel(Object o) throws PaintedInACornerFoldingException {
//		upperWorkPanel.setFoldedProteinWithImages((FoldedProteinWithImages)o);
	}
}

