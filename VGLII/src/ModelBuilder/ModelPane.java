package ModelBuilder;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import GeneticModels.GeneModel;
import GeneticModels.ProblemTypeSpecification;
import VGL.Messages;

public class ModelPane extends JPanel implements ItemListener {

	private int index;
	private GeneModel geneModel;
	private ProblemTypeSpecification specs;

	private JComboBox sexLinkageChoices;
	private JComboBox alleleNumberChoices;
	private JComboBox interactionTypeChoices;
	private JComboBox t1Choices;
	private JComboBox t2Choices;
	private JComboBox t3Choices;
	private JComboBox t4Choices;
	private JComboBox t5Choices;
	private JComboBox t6Choices;
	

	private JPanel interactionTypePanel;
	private JPanel interactionDetailsPanel;

	public ModelPane(int index, GeneModel geneModel, ProblemTypeSpecification specs) {
		this.index = index;
		this.geneModel = geneModel;
		this.specs = specs;
		setupUI();
	}

	private void setupUI() {
		JPanel masterPanel = new JPanel();
		masterPanel.setLayout(new BoxLayout(masterPanel, BoxLayout.Y_AXIS));
		masterPanel.add(Box.createRigidArea(new Dimension(300,1)));

		// sex linkage info
		JPanel sexLinkagePanel = new JPanel();
		sexLinkagePanel.setBorder(
				BorderFactory.createTitledBorder(
						Messages.getInstance().getString("VGLII.SexLinkage")));
		if (specs.getGene1_chSexLinked() > 0.0) {
			ArrayList<String>sexLinkageChoiceStrings = new ArrayList<String>();
			sexLinkageChoiceStrings.add(
					Messages.getInstance().getString(
					"VGLII.Unknown"));
			sexLinkageChoiceStrings.add("XX " 
					+ Messages.getInstance().getString("VGLII.Female")
					+ "/XY "
					+ Messages.getInstance().getString("VGLII.Male"));
			if (specs.getChZZ_ZW() > 0.0) {
				sexLinkageChoiceStrings.add("ZZ " 
						+ Messages.getInstance().getString("VGLII.Male")
						+ "/ZW "
						+ Messages.getInstance().getString("VGLII.Female"));
			}
			String[] slcs = new String[sexLinkageChoiceStrings.size()];
			for (int i = 0; i < slcs.length; i++) {
				slcs[i] = sexLinkageChoiceStrings.get(i);
			}
			sexLinkageChoices = new JComboBox(slcs);
			sexLinkagePanel.add(sexLinkageChoices);
		} else {
			sexLinkagePanel.add(
					new JLabel(
							Messages.getInstance().getString("VGLII.NotSexLinked")));
		}
		masterPanel.add(sexLinkagePanel);


		// number of alleles
		JPanel alleleNumberChoicePanel = new JPanel();
		alleleNumberChoicePanel.setBorder(
				BorderFactory.createTitledBorder(
						Messages.getInstance().getString("VGLII.NumberOfAlleles")));
		if ((specs.getGene1_ch3Alleles() > 0.0) ||
				(specs.getGene2_ch3Alleles() > 0.0) ||
				(specs.getGene3_ch3Alleles() > 0.0)) {
			String[] alleleNumberStrings = new String[3];
			alleleNumberStrings[0] = Messages.getInstance().getString("VGLII.Unknown");
			alleleNumberStrings[1] = "2-" + Messages.getInstance().getString("VGLII.Allele");
			alleleNumberStrings[2] = "3-" + Messages.getInstance().getString("VGLII.Allele");
			alleleNumberChoices = new JComboBox(alleleNumberStrings);
			alleleNumberChoicePanel.add(alleleNumberChoices);
		} else {
			alleleNumberChoicePanel.add(
					new JLabel("2-" +
							Messages.getInstance().getString("VGLII.Allele")));
		}
		masterPanel.add(alleleNumberChoicePanel);

		// allele interaction type
		interactionTypePanel = new JPanel(new CardLayout());
		interactionTypePanel.setBorder(
				BorderFactory.createTitledBorder(
						Messages.getInstance().getString("VGLII.GeneralAllelicInteractions")));

		JPanel unknownPanel = new JPanel();	// panel for 'unknown' # of alleles
		unknownPanel.add(
				new JLabel(Messages.getInstance().getString("VGLII.MustSelectNumAlleles")));
		interactionTypePanel.add(unknownPanel, Messages.getInstance().getString("VGLII.Unknown"));

		JPanel twoAllelePanel = new JPanel();	// panel for 2 allele models
		ArrayList<String> twoAlleleInteractionChoices = new ArrayList<String>();
		twoAlleleInteractionChoices.add(Messages.getInstance().getString("VGLII.Unknown"));
		twoAlleleInteractionChoices.add(Messages.getInstance().getString("VGLII.SimpleDominance"));
		if ((specs.getGene1_chIncDom() > 0.0)
				|| (specs.getGene2_chIncDom() > 0.0)
				|| (specs.getGene3_chIncDom() > 0.0)) {
			twoAlleleInteractionChoices.add(
					Messages.getInstance().getString("VGLII.IncompleteDominance"));
		}
		String[] twoAIC = new String[twoAlleleInteractionChoices.size()];
		for (int i = 0; i < twoAIC.length; i++) {
			twoAIC[i] = twoAlleleInteractionChoices.get(i);
		}
		interactionTypeChoices = new JComboBox(twoAIC);
		twoAllelePanel.add(interactionTypeChoices);
		interactionTypePanel.add(twoAllelePanel, "2-" +
				Messages.getInstance().getString("VGLII.Allele"));

		JPanel threeAllelePanel = new JPanel();		// panel for 3 allele models
		ArrayList<String> threeAlleleInteractionChoices = new ArrayList<String>();
		threeAlleleInteractionChoices.add(Messages.getInstance().getString("VGLII.Unknown"));
		threeAlleleInteractionChoices.add(
				Messages.getInstance().getString("VGLII.HierarchicalDominance"));
		if ((specs.getGene1_chCircDom() > 0.0)
				|| (specs.getGene2_chCircDom() > 0.0)
				|| (specs.getGene3_chCircDom() > 0.0)) {
			threeAlleleInteractionChoices.add(
					Messages.getInstance().getString("VGLII.CircularDominance"));			
		}
		threeAlleleInteractionChoices.add(
				Messages.getInstance().getString("VGLII.IncompleteDominance"));
		String[] threeAIC = new String[threeAlleleInteractionChoices.size()];
		for (int i = 0; i < threeAIC.length; i++) {
			threeAIC[i] = threeAlleleInteractionChoices.get(i);
		}
		interactionTypeChoices = new JComboBox(threeAIC);
		threeAllelePanel.add(interactionTypeChoices);
		interactionTypePanel.add(threeAllelePanel, "3-" +
				Messages.getInstance().getString("VGLII.Allele"));

		masterPanel.add(interactionTypePanel);


		// specific allele interactions
		String[] traits = geneModel.getTraits();
		interactionDetailsPanel = new JPanel(new CardLayout());
		interactionDetailsPanel.setBorder(
				BorderFactory.createTitledBorder(
						Messages.getInstance().getString("VGLII.SpecificAllelicInteractions")));
		
		JPanel unknownInteractionPanel = new JPanel();  // panel for 'unknown'
		unknownInteractionPanel.add(
				new JLabel(Messages.getInstance().getString("VGLII.MustSelectType")));
		interactionDetailsPanel.add(unknownInteractionPanel, 
				Messages.getInstance().getString("VGLII.Unknown"));
		
		JPanel twoSimplePanel = new JPanel();	//panel for 2-allele simple dom
		t1Choices = new JComboBox(traits);
		twoSimplePanel.add(t1Choices);
		t2Choices = new JComboBox(traits);
		twoSimplePanel.add(t2Choices);
		interactionDetailsPanel.add(twoSimplePanel, 
				Messages.getInstance().getString("VGLII.SimpleDominance"));
		
		masterPanel.add(interactionDetailsPanel);


		this.add(masterPanel);
	}


	public void itemStateChanged(ItemEvent e) {
		if (e.getSource().equals(alleleNumberChoices)) {
			CardLayout cl = (CardLayout)(interactionTypePanel.getLayout());
			cl.show(interactionTypePanel, (String)e.getItem());
		}
	}

	public void setupActionListeners() {
		if (alleleNumberChoices != null) {
			alleleNumberChoices.addItemListener(this);	
		}
	}
}
