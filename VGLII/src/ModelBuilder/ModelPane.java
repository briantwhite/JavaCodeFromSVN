package ModelBuilder;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
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
	
	private boolean circularPossible;


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
		circularPossible = true;
		if ((specs.getGene1_chCircDom() == 0.0)
				|| (specs.getGene2_chCircDom() == 0.0)
				|| (specs.getGene3_chCircDom() == 0.0)) circularPossible = false;

		interactionTypePanel = new JPanel();
		interactionTypePanel.setBorder(
				BorderFactory.createTitledBorder(
						Messages.getInstance().getString("VGLII.GeneralAllelicInteractions")));
		interactionTypePanel.add(new UnknownInteractionPanel());

		masterPanel.add(interactionTypePanel);

		this.add(masterPanel);
	}


	public void itemStateChanged(ItemEvent e) {

		if (e.getSource().equals(alleleNumberChoices)) {
			if (e.getItem().toString().equals(
					Messages.getInstance().getString("VGLII.Unknown"))) {
				interactionTypePanel.removeAll();
				interactionTypePanel.add(new UnknownInteractionPanel());
				interactionTypePanel.revalidate();
			}
			if (e.getItem().toString().equals(
					"2-" + Messages.getInstance().getString("VGLII.Allele"))) {
				interactionTypePanel.removeAll();
				interactionTypePanel.add(new TwoAllelePanel(interactionTypeChoices));
				interactionTypePanel.revalidate();				
			}
			if (e.getItem().toString().equals(
					"3-" + Messages.getInstance().getString("VGLII.Allele"))) {
				interactionTypePanel.removeAll();
				interactionTypePanel.add(
						new ThreeAllelePanel(interactionTypeChoices, circularPossible));
				interactionTypePanel.revalidate();				
			}

		}
		if (e.getSource().equals(interactionTypeChoices)) {

		}
	}

	public void setupActionListeners() {
		if (alleleNumberChoices != null) {
			alleleNumberChoices.addItemListener(this);	
		}
		if (interactionTypeChoices != null) {
			interactionTypeChoices.addItemListener(this);
		}
	}
}
