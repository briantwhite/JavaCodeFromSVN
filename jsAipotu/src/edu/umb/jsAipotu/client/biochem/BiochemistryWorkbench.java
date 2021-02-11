package edu.umb.jsAipotu.client.biochem;

import java.util.ArrayList;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
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
	BiochemHistoryList biochemHistoryList;
	ScrollPanel histListScrollPanel;

	private Button sendToUpperButton;
	private Button sendToLowerButton;

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
		HorizontalPanel buttonPanel = new HorizontalPanel();
		sendToUpperButton = new Button("\u21E8Upper Window");
		sendToUpperButton.setStyleName("histListButton");
		sendToUpperButton.setEnabled(false);
		sendToUpperButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (biochemHistoryList.getSelectedHistListItem() != null) {
					sendToUpperPanel(biochemHistoryList.getSelectedHistListItem());
				}
			}			
		});
		buttonPanel.add(sendToUpperButton);
		sendToLowerButton = new Button("\u21E8Lower Window");
		sendToLowerButton.setStyleName("histListButton");
		sendToLowerButton.setEnabled(false);
		sendToLowerButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (biochemHistoryList.getSelectedHistListItem() != null) {
					sendToLowerPanel(biochemHistoryList.getSelectedHistListItem());
				}
			}			
		});
		buttonPanel.add(sendToLowerButton);	
		VerticalPanel innerPanel = new VerticalPanel();
		innerPanel.add(buttonPanel);
		biochemHistoryList = new BiochemHistoryList(this, new BiochemHistListItem());
		biochemHistoryList.setStyleName("biochemHistList");
		histListScrollPanel = new ScrollPanel(biochemHistoryList);
		innerPanel.add(histListScrollPanel);
		histListPanel.add(innerPanel);
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
		biochemHistoryList.add((FoldedProteinWithImages)o);
		updateCombinedColor();
	}

	public WorkPanel getLowerPanel() {
		return lowerWorkPanel;
	}

	public WorkPanel getUpperPanel() {
		return upperWorkPanel;
	}

	public void sendToLowerPanel(Object o) {
		lowerWorkPanel.setFoldedProteinWithImages((FoldedProteinWithImages)o);
	}

	public void sendToUpperPanel(Object o) {
		upperWorkPanel.setFoldedProteinWithImages((FoldedProteinWithImages)o);
	}

	public void enableMoveToHistListButtons() {
		sendToUpperButton.setEnabled(true);
		sendToLowerButton.setEnabled(true);
	}

	public void disableMoveToHistListButtons() {
		sendToUpperButton.setEnabled(false);
		sendToLowerButton.setEnabled(false);
	}
}

