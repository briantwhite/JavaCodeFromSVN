package edu.umb.jsAipotu.client.biochem;

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
import com.google.gwt.user.client.ui.VerticalPanel;

import edu.umb.jsAipotu.client.JsAipotu;
import edu.umb.jsAipotu.client.molGenExp.WorkPanel;
import edu.umb.jsAipotu.client.preferences.GlobalDefaults;

public class BiochemistryWorkpanel extends WorkPanel {

	final BiochemistryWorkbench bwbench;
	
	CssColor defaultBackgroundColor;
	
	VerticalPanel mainPanel;
	
	CaptionPanel proteinPanelWrapper;
	SimplePanel proteinPanel;  // where the protein gets drawn
	ScrollPanel proteinPanelScroller;
	CaptionPanel proteinSequenceWrapper;
	ProteinSequenceEntryBox proteinSequenceEntryBox;

	HorizontalPanel buttonPanel;
	Button foldButton;
	Button loadSampleButton;
	Button clearAASeqButton;
	HTML colorLabel;
	SimplePanel colorChip;

	Polypeptide polypeptide;
	FoldingManager manager;

	StandardTable table;

	FoldedProteinWithImages foldedProteinWithImages;

	public BiochemistryWorkpanel(String title, final BiochemistryWorkbench bwbench) {
		super(title);
		defaultBackgroundColor = GlobalDefaults.PROTEIN_BACKGROUND_COLOR;
		this.bwbench = bwbench;
		foldedProteinWithImages = null;
		manager = new FoldingManager();
		table = new StandardTable();

		setupUI();
	}
	
	private void setupUI() {
		mainPanel = new VerticalPanel();
		
		proteinSequenceWrapper = new CaptionPanel("Amino Acid Sequence");
		proteinSequenceEntryBox = new ProteinSequenceEntryBox(this);
		proteinSequenceEntryBox.setStyleName("proteinSequenceEntryBox");
		proteinSequenceWrapper.add(proteinSequenceEntryBox);
		mainPanel.add(proteinSequenceWrapper);
		
		proteinPanelWrapper = new CaptionPanel("Folded Protein");
		proteinPanel = new SimplePanel();
		proteinPanel.setStyleName("proteinPanel");
		proteinPanelScroller = new ScrollPanel(proteinPanel);
		proteinPanelWrapper.add(proteinPanelScroller);
		proteinPanelWrapper.setStyleName("proteinPanelFOLDED");
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

		clearAASeqButton = new Button("Clear Amino Acid Sequence");
		clearAASeqButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent arg0) {
				proteinSequenceEntryBox.setAminoAcidSequence("");

				//update the color chip on the folding window
				colorChip.getElement().getStyle().setBackgroundColor("white");

				//update the combined color chip
				bwbench.updateCombinedColor();

				//update the picture as well
				if (proteinPanel.getWidget() != null) {
					proteinPanel.remove(proteinPanel.getWidget());
				}
				foldButton.setEnabled(false);
			}
		});
		buttonPanel.add(clearAASeqButton);

		loadSampleButton = new Button("Load Sample Protein");
		loadSampleButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent arg0) {
				proteinSequenceEntryBox.setAminoAcidSequence(GlobalDefaults.sampleProtein);
				foldProtein();
			}
		});
		buttonPanel.add(loadSampleButton);
		
		mainPanel.add(buttonPanel);	
		
		add(mainPanel);
	}

	private void foldProtein() {
		try {
			foldedProteinWithImages = manager.foldWithPix(proteinSequenceEntryBox.getAminoAcidSequence());
			// if it folded into a corner, it will have a null for a pic
			//  detect this and warn user
			if (foldedProteinWithImages.getFullSizePic() == null) {
				JsAipotu.consoleLog(GlobalDefaults.paintedInACornerNotice);
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
			
			bwbench.addToHistoryList(foldedProteinWithImages);

			foldButton.setEnabled(false);
			proteinPanelWrapper.setStyleName("proteinPanelWrapperFOLDED");
			proteinPanelWrapper.setCaptionText("Folded Protein");

		} catch (FoldingException e) {
			Window.alert(GlobalDefaults.paintedInACornerNotice);
		}	
	}	

	public void foldProteinIfButtonEnabled() {
		if (foldButton.isEnabled()) {
			foldProtein();
		}
	}

	public String getAaSeq() {
		return proteinSequenceEntryBox.getAminoAcidSequence();
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
		proteinPanelWrapper.setStyleName("proteinPanelWrapperNotFOLDED");
		proteinPanelWrapper.setCaptionHTML("<font color=\"red\">------ click FOLD to fold new amino acid sequence ------</font>");
		if (proteinPanel.getWidget() != null) {
			proteinPanel.remove(proteinPanel.getWidget());
		}
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
		StringBuffer abAASeq = new StringBuffer();
		for (int i = 0; i < acids.length; i++) {
			abAASeq.append(acids[i].getAbName());
		}
		proteinSequenceEntryBox.setAminoAcidSequence(abAASeq.toString());

		//update the color chip on the folding window
		colorChip.getElement().getStyle().setBackgroundColor(fp.getColor().toString());

		//update the combined color chip
		bwbench.updateCombinedColor();

		//update the picture as well
		if (proteinPanel.getWidget() != null) {
			proteinPanel.remove(proteinPanel.getWidget());
		}
		proteinPanel.add(foldedProteinWithImages.getFullSizePic());
		foldButton.setEnabled(false);
	}

}
