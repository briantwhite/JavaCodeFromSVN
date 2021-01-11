package edu.umb.jsAipotu.client.biochem;

import java.awt.Color;
import java.awt.image.BufferedImage;

import javax.swing.Action;
import javax.swing.JOptionPane;

import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import edu.umb.jsAipotu.client.molGenExp.WorkPanel;
import edu.umb.jsAipotu.client.preferences.GlobalDefaults;
import edu.umb.jsAipotu.client.preferences.MGEPreferences;

public class BiochemistryWorkpanel extends WorkPanel {

	final BiochemistryWorkbench protex;
	
	CssColor defaultBackgroundColor;
	
	VerticalPanel mainPanel;
	
	CaptionPanel proteinPanelWrapper;
	SimplePanel proteinPanel;  // where the protein gets drawn
	ScrollPanel proteinPanelScroller;
	CaptionPanel proteinSequenceWrapper;
	TextBox proteinSequence;
	TripleLetterCodeDocument tlcDoc;

	HorizontalPanel buttonPanel;
	Button foldButton;
	Button loadSampleButton;
	HTML colorLabel;
	SimplePanel colorChip;

	Polypeptide polypeptide;
	FoldingManager manager;

	StandardTable table;

	FoldedProteinWithImages foldedProteinWithImages;

	Action foldProteinAction;

	public BiochemistryWorkpanel(String title, final BiochemistryWorkbench protex) {
		super(title);
		defaultBackgroundColor = GlobalDefaults.PROTEIN_BACKGROUND_COLOR;
		this.protex = protex;
		foldedProteinWithImages = null;
		manager = new FoldingManager();
		table = new StandardTable();

		setupUI();
	}
	
	private void setupUI() {
		mainPanel = new VerticalPanel();
		
		tlcDoc = new TripleLetterCodeDocument();
		tlcDoc.setLinkedFoldingWindow(this);

		proteinSequenceWrapper = new CaptionPanel("Amino Acid Sequence");
		proteinSequence = new TextBox();
		proteinSequenceWrapper.add(proteinSequence);
		proteinSequence.setDocument(tlcDoc);
		mainPanel.add(proteinSequenceWrapper);
		
		proteinPanelWrapper = new CaptionPanel("Folded Protein");
		proteinPanel = new SimplePanel();
		proteinPanelScroller = new ScrollPanel(proteinPanel);
		proteinPanelWrapper.add(proteinPanelScroller);
		mainPanel.add(proteinPanelWrapper);

		buttonPanel = new HorizontalPanel();
		buttonPanel.setStyleName("BiochemButtonPanel");
		
		foldButton = new Button("FOLD");
		foldButton.setEnabled(false);
		foldButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent e) {
				foldProtein();
			}						
		});
		buttonPanel.add(foldButton);

		colorLabel = new HTML("Color:");
		colorChip = new SimplePanel();
		colorChip.setStyleName("colorChip");
		buttonPanel.add(colorChip);

		loadSampleButton = new Button("Load Sample Protein");
		loadSampleButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent arg0) {
				proteinSequence.setText(GlobalDefaults.sampleProtein);
				foldProtein();
			}
		});
		buttonPanel.add(loadSampleButton);
		mainPanel.add(buttonPanel);	
	}

	private void foldProtein() {
		try {
			foldedProteinWithImages = manager.foldWithPix(proteinSequence.getText().trim());

			// if it folded into a corner, it will have a null for a pic
			//  detect this and warn user
			if (foldedProteinWithImages.getFullSizePic() == null) {
				Window.alert(GlobalDefaults.paintedInACornerNotice);	
				return;
			}
			//display it
			CssColor proteinColor = foldedProteinWithImages.getColor();
			colorChip.getElement().getStyle().setBackgroundColor(proteinColor.toString());
			if (proteinPanel.getWidget() != null) {
				proteinPanel.remove(proteinPanel.getWidget());
			}
			proteinPanel.add(foldedProteinWithImages.getFullSizePic());
			
			protex.addToHistoryList(foldedProteinWithImages);

			foldButton.setEnabled(false);

		} catch (FoldingException e) {
			Window.alert(GlobalDefaults.paintedInACornerNotice);
		}	
	}	


	public String getAaSeq() {
		return proteinSequence.getText();
	}

	public CssColor getColor() {
		if (foldedProteinWithImages == null) return null;
		return foldedProteinWithImages.getColor();
	}

	public FoldedProteinWithImages getFoldedProteinWithImages() {
		return foldedProteinWithImages;
	}

	//callback from the Document in the text field when
	// the aa seq is changed
	public void aaSeqChanged() {
		foldButton.setEnabled(true);
//		resultPanel.setBackground(Color.PINK);
	}

	public void setFoldedProteinWithImages(FoldedProteinWithImages fp) {

		foldedProteinWithImages = fp;

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

		//update the color chip on the folding window
		colorChip.getElement().getStyle().setBackgroundColor(fp.getColor().toString());

		//update the combined color chip
		protex.updateCombinedColor();

		//update the picture as well
		if (proteinPanel.getWidget() != null) {
			proteinPanel.remove(proteinPanel.getWidget());
		}
		proteinPanel.add(foldedProteinWithImages.getFullSizePic());
		foldButton.setEnabled(false);
	}

}
