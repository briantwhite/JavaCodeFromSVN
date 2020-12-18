package edu.umb.jsAipotu.client.biochem;

import java.util.ArrayList;

import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import edu.umb.jsAipotu.client.molGenExp.CombinedColorPanel;
import edu.umb.jsAipotu.client.molGenExp.MolGenExp;
import edu.umb.jsAipotu.client.molGenExp.Organism;
import edu.umb.jsAipotu.client.molGenExp.WorkPanel;
import edu.umb.jsAipotu.client.molGenExp.Workbench;
import edu.umb.jsAipotu.client.preferences.GlobalDefaults;


public class BiochemistryWorkbench extends Workbench {

	private BiochemistryWorkpanel upperWorkPanel;
	private BiochemistryWorkpanel lowerWorkPanel;

	ArrayList<FoldedProteinWithImages> foldedProteins;
	CellList<FoldedProteinWithImages> proteinHistoryList;
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
		AminoAcidPalette aaPalette = new AminoAcidPalette(180, 225, 5, 4);
		aapPanel.add(aaPalette);
		leftPanel.add(aapPanel);

		CaptionPanel histListPanel = new CaptionPanel("History List");
		proteinHistoryList = new CellList<FoldedProteinWithImages>(new BiochemHistListItem());
		histListScrollPanel = new ScrollPanel(proteinHistoryList);
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
		CssColor u = upperWorkPanel.getColor();
		CssColor l = lowerWorkPanel.getColor();
		
		if ((u == null) || (l == null)) return;
		
		CssColor combined = GlobalDefaults.colorModel.mixTwoColors(u, l);
		combinedColorPanel.setCombinedColor(combined);
	}

	public void loadOrganism(Organism o) {
		upperWorkPanel.setFoldedProteinWithImages(
				o.getGene1().getFoldedProteinWithImages());
		lowerWorkPanel.setFoldedProteinWithImages(
				o.getGene2().getFoldedProteinWithImages());
	}

	public void addToHistoryList(Object o) 
	throws PaintedInACornerFoldingException {
		foldedProteins.add((FoldedProteinWithImages) o);
		proteinHistoryList.setRowData(foldedProteins);
		updateCombinedColor();
	}

	public WorkPanel getLowerPanel() {
		return lowerWorkPanel;
	}

	public WorkPanel getUpperPanel() {
		return upperWorkPanel;
	}

	public void sendToLowerPanel(Object o) throws PaintedInACornerFoldingException {
		lowerWorkPanel.setFoldedProteinWithImages((FoldedProteinWithImages)o);
	}

	public void sendToUpperPanel(Object o) throws PaintedInACornerFoldingException {
		upperWorkPanel.setFoldedProteinWithImages((FoldedProteinWithImages)o);
	}
}

