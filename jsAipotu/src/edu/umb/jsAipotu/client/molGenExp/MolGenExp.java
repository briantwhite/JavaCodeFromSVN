package edu.umb.jsAipotu.client.molGenExp;

import java.util.HashSet;
import java.util.Iterator;

import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.storage.client.Storage;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import edu.umb.jsAipotu.client.JsAipotu;
import edu.umb.jsAipotu.client.biochem.FoldedProteinArchive;
import edu.umb.jsAipotu.client.biochem.FoldingException;

public class MolGenExp {

	private JsAipotu jsA;

	//indices for tabbed panes
	public final static int GENETICS = 0;
	public final static int BIOCHEMISTRY = 1;
	public final static int MOLECULAR_BIOLOGY = 2;
	public final static int EVOLUTION = 3;

	private Greenhouse greenhouse;
	private GreenhouseLoader greenhouseLoader;

	//for genetics only; the two selected organisms
	private OrganismUI oui1;
	private OrganismUI oui2;

	public MolGenExp(JsAipotu jsA) {
		this.jsA = jsA;
		greenhouse = new Greenhouse(this);
		greenhouseLoader = new GreenhouseLoader(greenhouse);
		greenhouseLoader.load();

		// the two selected organisms in genetics
		oui1 = null;
		oui2 = null;
	}

	public Greenhouse getGreenhouse() {
		return greenhouse;
	}

	public GreenhouseLoader getGreenhouseLoader() {
		return greenhouseLoader;
	}

	// deal with organism selections
	public void organismWasClicked(OrganismUI oui) {

		switch (jsA.getSelectedTabIndex()) {

		case GENETICS:
			processSelectionInGenetics(oui);
			break;

		case BIOCHEMISTRY:
			processSelectionInMoboOrBiochem(oui);
			break;

		case MOLECULAR_BIOLOGY:
			processSelectionInMoboOrBiochem(oui);
			break;

		case EVOLUTION:
			// just return since evolution uses ThinOrganismUIs
			break;
		} 
	}

	private void processSelectionInGenetics(OrganismUI oui) {
		//  oui2 = most recently selected organism
		//  oui1 = least recently selected organism
		//  new orgs added to oui2 and push down to oui1 and then dropped

		// first, see if this was a selection or a de-selection event
		if (oui.isSelected()) {
			if ((oui1 == null) & (oui2 == null)) {
				// none selected yet, so put in oui2
				oui2 = oui;

			} else if ((oui1 == null) & (oui2 != null)) {
				// only one previously selected, move old to 1 and put new in 2
				oui1 = oui2;
				oui2 = oui;
			} else if ((oui1 != null) & (oui2 != null)) {
				// two were previously selected, un-select old 1, move 2 to 1 and put new in 2
				oui1.setSelected(false);
				oui1 = oui2;
				oui2 = oui;
			} 
		} else {
			// a de-selection event
			if (oui.equals(oui1)) {
				// it's ok to have oui1 empty
				oui1 = null;
			} else if (oui.equals(oui2)) {
				// if you un-select oui2, need to move oui1 up
				oui2 = oui1;
				oui1 = null;
			}
		}
		updateGeneticsButtonStatus();
		return;
	}

	private void processSelectionInMoboOrBiochem(OrganismUI oui) {
		greenhouse.selectOnlyThisOrganism(oui);
		if (jsA.getSelectedTabIndex() == MOLECULAR_BIOLOGY) {
			jsA.getMolBiolWorkbench().loadOrganism(oui.getOrganism());
		}
		if (jsA.getSelectedTabIndex() == BIOCHEMISTRY) {
			jsA.getBiochemWorkbench().loadOrganism(oui.getOrganism());
		}
	}

	public void clearSelectedOrganismsEverywhere() {
		greenhouse.clearAllSelections();
		clearSelectedOrganismsInGeneticsWorkbench();
		jsA.getEvolutionWorkArea().getWorld().deselectAllOrganismUIs();
	}

	public void clearSelectedOrganismsInGeneticsWorkbench() {
		if (oui1 != null) {
			oui1.setSelected(false);
		}

		if (oui2 != null) {
			oui2.setSelected(false);
		}

		oui1 = null;
		oui2 = null;
		updateGeneticsButtonStatus();
	}

	public OrganismUI getOUI1() {
		return oui1;
	}

	public OrganismUI getOUI2() {
		return oui2;
	}
	
	public JsAipotu getjsAipotu() {
		return jsA;
	}

	public void saveSelectedOrganismToGreenhouse() {

		switch (jsA.getSelectedTabIndex()) {

		case GENETICS:
			if ((oui1 == null) & (oui2 != null)) {
				saveOrganismToGreenhouse(oui2.getOrganism());
			}
			break;

		case BIOCHEMISTRY:
			// not possible - you can't save proteins, only organisms or DNA
			break;

		case MOLECULAR_BIOLOGY:
			try {
				jsA.getMolBiolWorkbench().saveOrganismToGreenhouse();
			} catch (FoldingException e) {
				return;
			}
			break;

		case EVOLUTION:
			jsA.getEvolutionWorkArea().saveOrganismToGreenhouse();
			break;
		}

	}

	public void saveOrganismToGreenhouse(final Organism o) {
		final DialogBox getNameDialog = new DialogBox(false);
		Button okButton = new Button("OK");
		Button cancelButton = new Button("Cancel");
		Label textLabel = new Label("Enter a name for your organism");
		final TextBox nameBox = new TextBox();
		nameBox.setMaxLength(50);

		VerticalPanel mainPanel = new VerticalPanel();
		mainPanel.setStyleName("enterNameDialog");
		mainPanel.add(textLabel);
		mainPanel.add(nameBox);
		HorizontalPanel buttonPanel = new HorizontalPanel();
		buttonPanel.add(cancelButton);
		buttonPanel.add(okButton);
		mainPanel.add(buttonPanel);
		getNameDialog.add(mainPanel);

		cancelButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				getNameDialog.hide();
			}
		});

		okButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				String name = nameBox.getText();
				RegExp p = RegExp.compile("[^ A-Za-z0-9\\_\\-]+");
				if (name.equals("")) {
					Window.alert("The name you entered was blank.\nPlease try again.");
					return;
				}
				if (p.test(name)) {
					Window.alert("Names may only contain letters, numbers, dashes, and underscores.\nPlease try a different name.");
					return;
				}
				if (greenhouse.nameExistsAlready(name)) {
					Window.alert("There is already an organism in the Greenhouse with that name.\nPlease try another.");
					return;
				}

				greenhouse.add(new OrganismFactory().createOrganism(name,o));
				clearSelectedOrganismsEverywhere();
				saveGreenhouseToHTML5storage();
				getNameDialog.hide();
			}
		});

		getNameDialog.setPopupPosition(greenhouse.getAbsoluteLeft() - 250, greenhouse.getAbsoluteTop());
		getNameDialog.show();
	}

	public void saveGreenhouseToHTML5storage() {
		// save to HTML5 storage http://www.gwtproject.org/doc/latest/DevGuideHtml5Storage.html 
		Storage greenhouseStore = null;
		greenhouseStore = Storage.getLocalStorageIfSupported();
		if (greenhouseStore != null) {
			greenhouseStore.setItem("greenhouse", getGreenhouseJSONstring());
		}
	}

	private String getGreenhouseJSONstring() {
		JSONObject greenhouseJSON = new JSONObject();

		// only archive the proteins needed for the greenhouse organisms
		// as you do this, create the JSON for the organisms
		JSONArray organisms = new JSONArray();
		int i = 0;
		HashSet<String> aaSeqsNeeded = new HashSet<String>();
		Iterator<Organism> orgIt = greenhouse.getAllOrganisms().iterator();
		while (orgIt.hasNext()) {
			Organism o = orgIt.next();
			// DNA sequences for the organism
			JSONObject organismJSON = new JSONObject();
			organismJSON.put("name", new JSONString(o.getName()));
			organismJSON.put("upperDNA", new JSONString(o.getGene1().getExpressedGene().getDNA()));
			organismJSON.put("lowerDNA", new JSONString(o.getGene2().getExpressedGene().getDNA()));
			organisms.set(i, organismJSON);
			i++;
			// proteins for the archive
			aaSeqsNeeded.add(o.getGene1().getFoldedProteinWithImages().getAaSeq());
			aaSeqsNeeded.add(o.getGene2().getFoldedProteinWithImages().getAaSeq());
		}
		greenhouseJSON.put("organisms", organisms);
		// assemble the folded protein archive
		JSONArray entries = new JSONArray();
		i = 0;
		Iterator<String> aaSeqIt = aaSeqsNeeded.iterator();
		while (aaSeqIt.hasNext()) {
			String aaSeq = aaSeqIt.next();
			JSONObject entryJSON = new JSONObject();
			entryJSON.put("aaSeq", new JSONString(aaSeq));
			entryJSON.put("topology", new JSONString(FoldedProteinArchive.getInstance().getEntry(aaSeq).getProteinString()));
			entryJSON.put("color", generateColorJSONString(FoldedProteinArchive.getInstance().getEntry(aaSeq).getColor()));
			entries.set(i, entryJSON);
			i++;
		}
		greenhouseJSON.put("foldedProteinArchive", entries);
		return greenhouseJSON.toString();
	}

	// convert rgb(255,255,0) to 255/255/0
	private JSONString generateColorJSONString(CssColor c) {
		String s = c.toString();
		s = s.substring(4); // trim rgb(
		s = s.replace(")", ""); // trailing )
		s = s.replace(",", "/");
		return new JSONString(s);
	}

	public void saveGreenhouseToFile() {
		String GHfileName = Window.prompt("Please enter a name for the saved Greenhouse file:", "saved.jsgh");
		if (GHfileName == null) {
			return;
		}
		if (GHfileName == "") {
			Window.alert("You entered a blank filename. Please try again.");
		}
		if (!GHfileName.endsWith(".jsgh")) {
			GHfileName = GHfileName + ".jsgh";
		}
		Window.alert("A file named " + GHfileName + " will be saved to your Desktop.\n Your browser may warn you about the file; it is safe.");
		saveFile(GHfileName, getGreenhouseJSONstring());		
	}

	public void loadGreenhouseFromFile(String greenhouseJSONstring) {
		greenhouse.clearAllOrganisms();
		greenhouseLoader.processJSONString(greenhouseJSONstring);
		greenhouse.updateDisplay();
	}

	public void setAddToGreenhouseButtonEnabled(boolean b) {
		jsA.enableAddToGreenhouseButton(b);
	}

	public void updateGeneticsButtonStatus() {
		if ((oui1 == null) & (oui2 == null)) {
			jsA.getGeneticsWorkbench().setCrossTwoButtonsEnabled(false);
			jsA.getGeneticsWorkbench().setSelfCrossButtonsEnabled(false);
			jsA.getGeneticsWorkbench().setMutateButtonsEnabled(false);
			jsA.enableAddToGreenhouseButton(false);
		} else if ((oui1 == null) & (oui2 != null)) {
			jsA.getGeneticsWorkbench().setCrossTwoButtonsEnabled(false);
			jsA.getGeneticsWorkbench().setSelfCrossButtonsEnabled(true);
			jsA.getGeneticsWorkbench().setMutateButtonsEnabled(true);	
			if (!greenhouse.isInGreenhouse(oui2.getOrganism())) {
				jsA.enableAddToGreenhouseButton(true);
			} else {
				jsA.enableAddToGreenhouseButton(false);
			}	
		} else if ((oui1 != null) & (oui2 != null)) {
			jsA.getGeneticsWorkbench().setCrossTwoButtonsEnabled(true);
			jsA.getGeneticsWorkbench().setSelfCrossButtonsEnabled(false);
			jsA.getGeneticsWorkbench().setMutateButtonsEnabled(false);
			jsA.enableAddToGreenhouseButton(false);
		}
	}
	
	public static native void saveFile(String fileName, String text) /*-{
	var blob = new Blob([text], {type: "text/plain;charset=utf-8"});
		$wnd.saveAs(blob, fileName);
	}-*/;

	public void showLoadGreenhouseFileDialog() {
		showLoadFileDialog();
	}

	public static native void showLoadFileDialog() /*-{
		$wnd.loadWorkDialog();
	}-*/;

}
