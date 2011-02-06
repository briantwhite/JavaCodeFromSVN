package ModelBuilder;

import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import org.jdom.Element;

import GeneticModels.ProblemTypeSpecification;
import VGL.Messages;

public class ModelPane extends JPanel implements ItemListener {

	private int index;
	private String character;
	private String[] traits;
	private ProblemTypeSpecification specs;
	private ModelBuilderUI modelBuilderUI;

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
	private boolean incDomPossible;
	private boolean complementationPossible;
	private boolean epistasisPossible;

	private JPanel interactionTypePanel;
	private JPanel interactionDetailsPanel;

	public ModelPane(int index, 
			String character,
			String[] traits, 
			ProblemTypeSpecification specs,
			ModelBuilderUI modelBuilderUI) {
		this.index = index;
		this.character = character;
		this.traits = traits;
		this.specs = specs;
		this.modelBuilderUI = modelBuilderUI;
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
		ArrayList<String>sexLinkageChoiceStrings = new ArrayList<String>();
		if (specs.getGene1_chSexLinked() > 0.0) {
			sexLinkageChoiceStrings.add(
					Messages.getInstance().getString(
					"VGLII.Unknown"));
			sexLinkageChoiceStrings.add(Messages.getInstance().getString("VGLII.NotSexLinked"));
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
		} else {
			sexLinkageChoiceStrings.add(Messages.getInstance().getString("VGLII.NotSexLinked"));
		}
		String[] slcs = new String[sexLinkageChoiceStrings.size()];
		for (int i = 0; i < slcs.length; i++) {
			slcs[i] = sexLinkageChoiceStrings.get(i);
		}
		sexLinkageChoices = new JComboBox(slcs);
		sexLinkagePanel.add(sexLinkageChoices);
		masterPanel.add(sexLinkagePanel);


		// number of alleles
		JPanel alleleNumberChoicePanel = new JPanel();
		alleleNumberChoicePanel.setBorder(
				BorderFactory.createTitledBorder(
						Messages.getInstance().getString("VGLII.NumberOfAlleles")));
		String[] alleleNumberStrings;
		boolean isOnly2Alleles;
		if ((specs.getGene1_ch3Alleles() > 0.0) ||
				(specs.getGene2_ch3Alleles() > 0.0) ||
				(specs.getGene3_ch3Alleles() > 0.0)) {
			isOnly2Alleles = false;
			alleleNumberStrings = new String[3];
			alleleNumberStrings[0] = Messages.getInstance().getString("VGLII.Unknown");
			alleleNumberStrings[1] = "2-" + Messages.getInstance().getString("VGLII.Allele");
			alleleNumberStrings[2] = "3-" + Messages.getInstance().getString("VGLII.Allele");
		} else {
			isOnly2Alleles = true;
			alleleNumberStrings = new String[1];
			alleleNumberStrings[0] = "2-" + Messages.getInstance().getString("VGLII.Allele");
		}
		alleleNumberChoices = new JComboBox(alleleNumberStrings);
		alleleNumberChoicePanel.add(alleleNumberChoices);
		masterPanel.add(alleleNumberChoicePanel);

		// allele interaction type
		circularPossible = true;
		if ((specs.getGene1_chCircDom() == 0.0)
				|| (specs.getGene2_chCircDom() == 0.0)
				|| (specs.getGene3_chCircDom() == 0.0)) circularPossible = false;
		
		incDomPossible = true;
		if ((specs.getGene1_chIncDom() == 0.0)
				|| (specs.getGene2_chIncDom() == 0.0)
				|| (specs.getGene3_chIncDom() == 0.0)) incDomPossible = false;
		
		complementationPossible = false;
		if ((specs.getPhenotypeInteraction() > 0.0)
				&& (specs.getEpistasis() != 1.0)) complementationPossible = true;
		
		epistasisPossible = false;
		if ((specs.getPhenotypeInteraction() > 0.0)
				&& (specs.getEpistasis() != 0.0)) epistasisPossible = true;

		interactionTypePanel = new JPanel();
		interactionTypePanel.setBorder(
				BorderFactory.createTitledBorder(
						Messages.getInstance().getString("VGLII.GeneralAllelicInteractions")));
		if (isOnly2Alleles) {
			interactionTypePanel.removeAll();
			TwoAllelePanel twap = new TwoAllelePanel(
					incDomPossible, complementationPossible, epistasisPossible);
			interactionTypeChoices = twap.getInteractionTypeChoices();
			interactionTypeChoices.addItemListener(this);
			interactionTypePanel.add(twap);
			interactionTypePanel.revalidate();				
		} else {
			interactionTypePanel.add(new UnknownInteractionPanel());
		}

		masterPanel.add(interactionTypePanel);

		// allele interaction details
		interactionDetailsPanel = new JPanel();
		interactionDetailsPanel.setBorder(
				BorderFactory.createTitledBorder(
						Messages.getInstance().getString("VGLII.SpecificAllelicInteractions")));
		interactionDetailsPanel.add(new UnknownSpecificsPanel());
		masterPanel.add(interactionDetailsPanel);

		this.add(masterPanel);
	}


	public void itemStateChanged(ItemEvent e) {

		if (e.getSource().equals(alleleNumberChoices)) {
			if (e.getItem().toString().equals(
					Messages.getInstance().getString("VGLII.Unknown"))) {
				interactionTypePanel.removeAll();
				interactionTypePanel.add(new UnknownInteractionPanel());
				interactionTypePanel.revalidate();

				interactionDetailsPanel.removeAll();
				interactionDetailsPanel.add(new UnknownSpecificsPanel());
				interactionDetailsPanel.revalidate();
				modelBuilderUI.pack();
				modelBuilderUI.repaint();
			}
			if (e.getItem().toString().equals(
					"2-" + Messages.getInstance().getString("VGLII.Allele"))) {
				interactionTypePanel.removeAll();
				TwoAllelePanel twap = new TwoAllelePanel(incDomPossible,
						complementationPossible, epistasisPossible);
				interactionTypeChoices = twap.getInteractionTypeChoices();
				interactionTypeChoices.addItemListener(this);
				interactionTypePanel.add(twap);
				interactionTypePanel.revalidate();				

				interactionDetailsPanel.removeAll();
				interactionDetailsPanel.add(new UnknownSpecificsPanel());
				interactionDetailsPanel.revalidate();
				modelBuilderUI.pack();
				modelBuilderUI.repaint();
			}
			if (e.getItem().toString().equals(
					"3-" + Messages.getInstance().getString("VGLII.Allele"))) {
				interactionTypePanel.removeAll();
				ThreeAllelePanel thap = new ThreeAllelePanel(circularPossible);
				interactionTypeChoices = thap.getInteractionTypeChoices();
				interactionTypeChoices.addItemListener(this);
				interactionTypePanel.add(thap);
				interactionTypePanel.revalidate();				

				interactionDetailsPanel.removeAll();
				interactionDetailsPanel.add(new UnknownSpecificsPanel());
				interactionDetailsPanel.revalidate();
				modelBuilderUI.pack();
				modelBuilderUI.repaint();
			}

		}

		if (e.getSource().equals(interactionTypeChoices)) {

			if (alleleNumberChoices.getSelectedItem().toString().equals(
					"2-" + Messages.getInstance().getString("VGLII.Allele"))) {
				if (e.getItem().toString().equals(
						Messages.getInstance().getString("VGLII.Unknown"))) {
					interactionDetailsPanel.setBorder(
							BorderFactory.createTitledBorder(
									Messages.getInstance().getString("VGLII.SpecificAllelicInteractions")));
					interactionDetailsPanel.removeAll();
					interactionDetailsPanel.add(new UnknownSpecificsPanel());
					interactionDetailsPanel.revalidate();
					modelBuilderUI.pack();
					modelBuilderUI.repaint();
				}
				if (e.getItem().toString().equals(
						Messages.getInstance().getString("VGLII.SimpleDominance"))) {
					interactionDetailsPanel.setBorder(
							BorderFactory.createTitledBorder(
									Messages.getInstance().getString("VGLII.SpecificAllelicInteractions")));
					interactionDetailsPanel.removeAll();
					interactionDetailsPanel.add(
							new TwoSimplePanel(traits, t1Choices, t2Choices));
					interactionTypePanel.revalidate();
					modelBuilderUI.pack();
					modelBuilderUI.repaint();
				}
				if (e.getItem().toString().equals(
						Messages.getInstance().getString("VGLII.IncompleteDominance"))) {
					interactionDetailsPanel.setBorder(
							BorderFactory.createTitledBorder(
									Messages.getInstance().getString("VGLII.SpecificAllelicInteractions")));
					interactionDetailsPanel.removeAll();
					interactionDetailsPanel.add(
							new TwoIncPanel(traits, t1Choices, t2Choices, t3Choices));
					interactionTypePanel.revalidate();
					modelBuilderUI.pack();
					modelBuilderUI.repaint();
				}
				if (e.getItem().toString().equals(
						Messages.getInstance().getString("VGLII.Complementation"))) {
					interactionDetailsPanel.setBorder(
							BorderFactory.createTitledBorder(
									Messages.getInstance().getString("VGLII.Pathway")));
					interactionDetailsPanel.removeAll();
					interactionDetailsPanel.add(
							new ComplementationPanel(traits, t1Choices, t2Choices));
					interactionTypePanel.revalidate();
					modelBuilderUI.pack();
					modelBuilderUI.repaint();
				}
				if (e.getItem().toString().equals(
						Messages.getInstance().getString("VGLII.Epistasis"))) {
					interactionDetailsPanel.setBorder(
							BorderFactory.createTitledBorder(
									Messages.getInstance().getString("VGLII.Pathway")));
					interactionDetailsPanel.removeAll();
					interactionDetailsPanel.add(
							new EpistasisPanel(traits, t1Choices, t2Choices, t3Choices));
					interactionTypePanel.revalidate();
					modelBuilderUI.pack();
					modelBuilderUI.repaint();
				}
			}
			if (alleleNumberChoices.getSelectedItem().toString().equals(
					"3-" + Messages.getInstance().getString("VGLII.Allele"))) {
				if (e.getItem().toString().equals(
						Messages.getInstance().getString("VGLII.Unknown"))) {
					interactionDetailsPanel.removeAll();
					interactionDetailsPanel.add(new UnknownSpecificsPanel());
					interactionDetailsPanel.revalidate();
					modelBuilderUI.pack();
					modelBuilderUI.repaint();
				}
				if (e.getItem().toString().equals(
						Messages.getInstance().getString("VGLII.IncompleteDominance"))) {
					interactionDetailsPanel.removeAll();
					interactionDetailsPanel.add(
							new ThreeIncPanel(
									traits, 
									t1Choices, 
									t2Choices, 
									t3Choices,
									t4Choices,
									t5Choices,
									t6Choices));
					interactionTypePanel.revalidate();		
					modelBuilderUI.pack();
					modelBuilderUI.repaint();
				}
				if (e.getItem().toString().equals(
						Messages.getInstance().getString("VGLII.HierarchicalDominance"))) {
					interactionDetailsPanel.removeAll();
					interactionDetailsPanel.add(
							new ThreeHierPanel(traits, 
									t1Choices, t2Choices, t3Choices));
					interactionTypePanel.revalidate();	
					modelBuilderUI.pack();
					modelBuilderUI.repaint();
				}
				if (e.getItem().toString().equals(
						Messages.getInstance().getString("VGLII.CircularDominance"))) {
					interactionDetailsPanel.removeAll();
					interactionDetailsPanel.add(
							new ThreeCircPanel(traits, 
									t1Choices, t2Choices, t3Choices));
					interactionTypePanel.revalidate();	
					modelBuilderUI.pack();
					modelBuilderUI.repaint();
				}
			}
		}
	}

	public void setupActionListeners() {
		if (alleleNumberChoices != null) {
			alleleNumberChoices.addItemListener(this);	
		}
	}
	
	public void setStateFromFile(Element element) {
		List<Element> elements = element.getChildren();
		Iterator<Element> it = elements.iterator();
		while(it.hasNext()) {
			Element e = it.next();
		}
	}
		
	public Element save() {
		Element mpe = new Element("Character");
		mpe.setAttribute("Name", character);
		mpe.setAttribute("Index", String.valueOf(index));
		
		Element e = null;
		e = new Element("SexLinkage");
		e.setText(String.valueOf(sexLinkageChoices.getSelectedItem()));
		mpe.addContent(e);
		
		e = new Element("AlleleNumber");
		e.setText(String.valueOf(alleleNumberChoices.getSelectedItem()));
		mpe.addContent(e);
		
		e = new Element("InteractionType");
		e.setText(String.valueOf(interactionTypeChoices.getSelectedItem()));
		mpe.addContent(e);

		// can't get selections this way - need to get to panels for info
		System.out.println(interactionDetailsPanel.getComponentCount());
//		e = new Element("T1");
//		e.setText(String.valueOf(t1Choices.getSelectedItem()));
//		mpe.addContent(e);
//		
//		e = new Element("T2");
//		e.setText(String.valueOf(t2Choices.getSelectedItem()));
//		mpe.addContent(e);
//		
//		if (t3Choices != null) {
//			e = new Element("T3");
//			e.setText(String.valueOf(t3Choices.getSelectedItem()));
//			mpe.addContent(e);
//		}
//
//		if (t4Choices != null) {
//			e = new Element("T4");
//			e.setText(String.valueOf(t4Choices.getSelectedItem()));
//			mpe.addContent(e);
//		}
//
//		if (t5Choices != null) {
//			e = new Element("T5");
//			e.setText(String.valueOf(t5Choices.getSelectedItem()));
//			mpe.addContent(e);
//		}
//
//		if (t6Choices != null) {
//			e = new Element("T6");
//			e.setText(String.valueOf(t6Choices.getSelectedItem()));
//			mpe.addContent(e);
//		}

		return mpe;
	}
}
