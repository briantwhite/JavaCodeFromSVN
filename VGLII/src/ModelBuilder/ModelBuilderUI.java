package ModelBuilder;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.jdom.Element;

import GeneticModels.GeneModel;
import GeneticModels.GeneticModel;
import GeneticModels.PhenotypeProcessor;
import GeneticModels.ProblemTypeSpecification;
import VGL.GeneticModelAndCageSet;
import VGL.Messages;
import VGL.VGLII;


public class ModelBuilderUI extends JPanel {

	private JDialog parentDialog;
	private VGLII vglII;

	private GeneticModelAndCageSet result;
	private GeneticModel geneticModel;
	private LinkagePanel linkagePanel;
	private ModelPane[] modelPanes;

	private boolean desiredVisibility;
	private int desiredXpos;
	private int desiredYpos;


	// from new problem or saved work file
	public ModelBuilderUI (JDialog parentDialog, VGLII vglII, GeneticModel geneticModel) {
		this.parentDialog = parentDialog;
		this.vglII = vglII;
		this.geneticModel = geneticModel;
		setupUI();
	}


	// for problem for grading
	public ModelBuilderUI(VGLII vglII, GeneticModelAndCageSet result) {
		this.parentDialog = null;
		this.vglII = vglII;
		this.geneticModel = result.getGeneticModel();
		this.result = result;
		setupUI();
	}

	private void setupUI() {

		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		add(Box.createRigidArea(new Dimension(1,300)));

		JPanel masterPanel = new JPanel();
		masterPanel.setLayout(new BoxLayout(masterPanel,BoxLayout.Y_AXIS));
		masterPanel.add(Box.createRigidArea(new Dimension(400,1)));
		JTabbedPane tabs = new JTabbedPane();
		GeneModel[] geneModels = new GeneModel[geneticModel.getNumberOfGeneModels()];
		modelPanes = new ModelPane[geneticModel.getNumberOfCharacters()];

		ProblemTypeSpecification specs = geneticModel.getProblemTypeSpecification();
		if (specs.getPhenotypeInteraction() == 0.0) {
			// simple if no complementation or epistasis
			for (int i = 0; i < geneticModel.getNumberOfGeneModels(); i++) {
				geneModels[i] = geneticModel.getGeneModelByIndex(i);
				modelPanes[i] = new ModelPane(i,
						geneModels[i].getCharacter(),
						geneModels[i].getTraitStrings(), 
						specs, 
						this);
				tabs.addTab(Messages.getInstance().getTranslatedCharacterName(
						geneModels[i].getTraits()[0]), 
						modelPanes[i]);
				modelPanes[i].setupActionListeners();
			}
		} else {
			// more of a pain if epistasis or complementation
			//	first pane is the possibly interacting pheno
			geneModels[0] = geneticModel.getGeneModelByIndex(0);
			if (geneticModel.getPhenoTypeProcessor().getInteractionType() == PhenotypeProcessor.NO_INTERACTION) {
				// if no interaction, just get the first gene model
				modelPanes[0] = new ModelPane(0, 
						geneModels[0].getCharacter(),
						geneModels[0].getTraitStrings(), 
						specs, 
						this);
				tabs.addTab(Messages.getInstance().getTranslatedCharacterName(
						geneModels[0].getTraits()[0]), 
						modelPanes[0]);
			} else {
				// if there is an interaction, need to get the phenotypes from the PhenoProcessor
				modelPanes[0] = new ModelPane(0, 
						geneticModel.getPhenoTypeProcessor().getCharacter(),
						geneticModel.getPhenoTypeProcessor().getTraits(), 
						specs, 
						this);
				tabs.addTab(Messages.getInstance().getTranslatedCharacterName(
						geneticModel.getPhenoTypeProcessor().getT1()), 
						modelPanes[0]);
			}
			modelPanes[0].setupActionListeners();

			// if a last character, add it (it's index = 2 since 1st 2 are interacting)
			if (geneticModel.getNumberOfCharacters() == 2) {
				geneModels[1] = geneticModel.getGeneModelByIndex(2);
				modelPanes[1] = new ModelPane(1, 
						geneModels[1].getCharacter(),
						geneModels[1].getTraitStrings(), 
						specs, 
						this);
				tabs.addTab(Messages.getInstance().getTranslatedCharacterName(
						geneModels[1].getTraits()[0]), 
						modelPanes[1]);
			}

		}

		// set up for linkage if needed
		if (geneticModel.getNumberOfGeneModels() > 1) {
			if ((specs.getGene2_chSameChrAsGene1() != 0.0) &&
					(specs.getGene2_minRfToGene1() < 0.5)) {
				setupLinkagePanel(geneModels, tabs);
			}
			if ((geneticModel.getNumberOfGeneModels() > 2) &&
					(specs.getGene3_chSameChrAsGene1() != 0.0) &&
					(specs.getGene3_chSameChrAsGene1() < 0.5)) {
				setupLinkagePanel(geneModels, tabs);
			}
		}

		masterPanel.add(tabs);
		add(masterPanel);
	}

	private void setupLinkagePanel(GeneModel[] geneModels, JTabbedPane tabs) {
		ArrayList<String> characters = new ArrayList<String>();
		characters.add(geneModels[0].getCharacter());
		characters.add(geneModels[1].getCharacter());
		if (geneticModel.getNumberOfGeneModels() == 3) {
			characters.add(geneModels[2].getCharacter());
		}
		String[] chars = new String[characters.size()];
		for (int i = 0; i < characters.size(); i++) {
			chars[i] = characters.get(i);
		}
		linkagePanel = new LinkagePanel(chars);
		tabs.add(Messages.getInstance().getString("VGLII.Linkage"), linkagePanel);
	}

	public void updateUI() {
		repaint();
		revalidate();
		if (parentDialog != null) parentDialog.pack();
	}

	public void configureFromFile(Element root) {

		List<Element> elements = root.getChildren();
		Iterator<Element> it = elements.iterator();
		while (it.hasNext()) {
			Element e = it.next();
			if (e.getName().equals("Character")) {
				int index = Integer.parseInt(e.getAttributeValue("Index"));
				modelPanes[index].setStateFromFile(e);
			}
			if (e.getName().equals("LinkagePanel")) {
				linkagePanel.setStateFromFile(e);
			}
		}

		desiredVisibility = Boolean.parseBoolean(root.getAttributeValue("Visible"));
		desiredXpos = Integer.parseInt(root.getAttributeValue("Xpos"));
		desiredYpos = Integer.parseInt(root.getAttributeValue("Ypos"));

	}

	public VGLII getVGLII() {
		return vglII;
	}

	public GeneticModelAndCageSet getGenticModelAndCageSet() {
		return result;
	}

	public boolean getDesiredVisibility() {
		return desiredVisibility;
	}

	public int getDesiredXpos() {
		return desiredXpos;
	}

	public int getDesiredYpos() {
		return desiredYpos;
	}

	public void updateCageChoices(int nextCageId) {
		for (int i = 0; i < modelPanes.length; i++) {
			modelPanes[i].updateCageChoices(nextCageId);
		}
	}

	/**
	 * get all the selected cage numbers 
	 * 	note - these are the DISPLAY numbers
	 * 		so need to subtract 1 to get internal index number
	 * 	also, don't include 0's - they're "?"
	 * these will be in numerical order
	 */
	public TreeSet<Integer> getChosenRelevantCages() {
		TreeSet<Integer> relevantCages = new TreeSet<Integer>();
		for (int i = 0; i < modelPanes.length; i++) {
			ArrayList<Integer> indices = modelPanes[i].getRelevantCages();
			for (int j = 0; j < indices.size(); j++) {
				int num = indices.get(j);
				if (num != 0) relevantCages.add(num);
			}
		}	
		return relevantCages;
	}

	public Element save() {
		Element mbuie = new Element("ModelBuilderState");
		boolean visible = this.getParent().isShowing();
		mbuie.setAttribute("Visible", String.valueOf(visible));
		if (visible) {
			mbuie.setAttribute("Xpos", String.valueOf(this.getParent().getLocationOnScreen().x));
			mbuie.setAttribute("Ypos", String.valueOf(this.getParent().getLocationOnScreen().y));
		} else {
			mbuie.setAttribute("Xpos", String.valueOf(300));
			mbuie.setAttribute("Ypos", String.valueOf(300));
		}
		for (int i = 0; i < modelPanes.length; i++) {
			mbuie.addContent(modelPanes[i].save());
		}
		if (linkagePanel != null) mbuie.addContent(linkagePanel.save());
		return mbuie;
	}

	public String getAsHtml(boolean isForGrader) {
		StringBuffer b = new StringBuffer();
		for (int i = 0; i < modelPanes.length; i++) {
			b.append(modelPanes[i].getAsHtml(isForGrader));
			b.append("<hr>");
		}
		if (linkagePanel != null) b.append(linkagePanel.getAsHtml(isForGrader));

		return b.toString();
	}

}
