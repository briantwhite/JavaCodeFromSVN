package ModelBuilder;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jdom.Element;

import GeneticModels.ProblemTypeSpecification;
import VGL.Messages;

public class ModelPane extends JPanel implements ItemListener {

	/*
	 *  color for list items where the student didn't have 
	 *    a choice - so don't grade
	 */
	private static final String NOT_A_CHOICE_COLOR = "gray";

	private int index;
	private String character;
	private String[] traits;
	private ProblemTypeSpecification specs;
	private ModelBuilderUI modelBuilderUI;

	private JComboBox sexLinkageChoices;
	private JComboBox sexLinkageCageChoices;
	private JComboBox alleleNumberChoices;
	private JComboBox interactionTypeChoices;
	private JComboBox interactionCageChoices;
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

	// values updated by the model details panels
	private int t1Value;
	private int t2Value;
	private int t3Value;
	private int t4Value;
	private int t5Value;
	private int t6Value;


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

		/*
		 * sex linkage info
		 * 	must always be in this order for AutoGrader to work
		 * 	0 = unknown
		 * 	1 = not sex-linked
		 *  2 = xx/xy
		 *  3 = zz/zw (if present)
		 */
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
		sexLinkageChoices.addItemListener(this);
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
		circularPossible = false;
		if ((specs.getGene1_chCircDom() != 0.0)
				|| (specs.getGene2_chCircDom() != 0.0)
				|| (specs.getGene3_chCircDom() != 0.0)) circularPossible = true;

		incDomPossible = false;
		if ((specs.getGene1_chIncDom() != 0.0)
				|| (specs.getGene2_chIncDom() != 0.0)
				|| (specs.getGene3_chIncDom() != 0.0)) incDomPossible = true;

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

		// relevant crosses
		JPanel relevantCrossPanel = new JPanel();
		relevantCrossPanel.setLayout(new GridLayout(2,0));
		relevantCrossPanel.setBorder(
				BorderFactory.createTitledBorder(
						Messages.getInstance().getString("VGLII.RelevantCages")));
		// if sex-linkage, need a relevant cage selector
		if (specs.getGene1_chSexLinked() > 0.0) {
			JPanel upperPanel = new JPanel();
			upperPanel.setLayout(new BoxLayout(upperPanel, BoxLayout.X_AXIS));
			sexLinkageCageChoices = new JComboBox(modelBuilderUI.getVGLII().getCageList());
			sexLinkageCageChoices.addItemListener(this);
			upperPanel.add(
					new JLabel(Messages.getInstance().getString("VGLII.ForSexLinkage")));
			upperPanel.add(sexLinkageCageChoices);
			relevantCrossPanel.add(upperPanel);
		}
		JPanel lowerPanel = new JPanel();
		lowerPanel.setLayout(new BoxLayout(lowerPanel, BoxLayout.X_AXIS));
		interactionCageChoices = new JComboBox(modelBuilderUI.getVGLII().getCageList());
		interactionCageChoices.addItemListener(this);
		lowerPanel.add(
				new JLabel(Messages.getInstance().getString("VGLII.ForDetails")));
		lowerPanel.add(interactionCageChoices);
		relevantCrossPanel.add(lowerPanel);
		masterPanel.add(relevantCrossPanel);

		this.add(masterPanel);
	}

	public void setT1Value(int t1Value) {
		this.t1Value = t1Value;
		modelBuilderUI.getVGLII().setChangeSinceLastSave();
	}

	public void setT2Value(int t2Value) {
		this.t2Value = t2Value;
		modelBuilderUI.getVGLII().setChangeSinceLastSave();
	}

	public void setT3Value(int t3Value) {
		this.t3Value = t3Value;
		modelBuilderUI.getVGLII().setChangeSinceLastSave();
	}

	public void setT4Value(int t4Value) {
		this.t4Value = t4Value;
		modelBuilderUI.getVGLII().setChangeSinceLastSave();
	}

	public void setT5Value(int t5Value) {
		this.t5Value = t5Value;
		modelBuilderUI.getVGLII().setChangeSinceLastSave();
	}

	public void setT6Value(int t6Value) {
		this.t6Value = t6Value;
		modelBuilderUI.getVGLII().setChangeSinceLastSave();
	}

	public void clearValues() {
		modelBuilderUI.getVGLII().setChangeSinceLastSave();
		t1Value = 0;
		t2Value = 0;
		t3Value = 0;
		t4Value = 0;
		t5Value = 0;
		t6Value = 0;
	}

	public void updateCageChoices(int nextCageId) {
		if (sexLinkageCageChoices != null) {
			sexLinkageCageChoices.addItem(
					Messages.getInstance().getString("VGLII.Cage") + " " + nextCageId);
		}
		interactionCageChoices.addItem(
				Messages.getInstance().getString("VGLII.Cage") + " " + nextCageId);
		revalidate();
	}

	public void itemStateChanged(ItemEvent e) {

		modelBuilderUI.getVGLII().setChangeSinceLastSave();

		if (e.getSource().equals(alleleNumberChoices)) {
			if (e.getItem().toString().equals(
					Messages.getInstance().getString("VGLII.Unknown"))) {
				interactionTypePanel.removeAll();
				interactionTypePanel.add(new UnknownInteractionPanel());
				interactionTypePanel.revalidate();

				interactionDetailsPanel.removeAll();
				interactionDetailsPanel.add(new UnknownSpecificsPanel());
				interactionDetailsPanel.revalidate();
				modelBuilderUI.updateUI();
				clearValues();
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
				modelBuilderUI.updateUI();
				clearValues();
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
				modelBuilderUI.updateUI();
				clearValues();
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
					modelBuilderUI.updateUI();
					clearValues();
				}
				if (e.getItem().toString().equals(
						Messages.getInstance().getString("VGLII.SimpleDominance"))) {
					interactionDetailsPanel.setBorder(
							BorderFactory.createTitledBorder(
									Messages.getInstance().getString("VGLII.SpecificAllelicInteractions")));
					interactionDetailsPanel.removeAll();
					interactionDetailsPanel.add(
							new TwoSimplePanel(traits, t1Choices, t2Choices, this));
					interactionTypePanel.revalidate();
					modelBuilderUI.updateUI();
					clearValues();
				}
				if (e.getItem().toString().equals(
						Messages.getInstance().getString("VGLII.IncompleteDominance"))) {
					interactionDetailsPanel.setBorder(
							BorderFactory.createTitledBorder(
									Messages.getInstance().getString("VGLII.SpecificAllelicInteractions")));
					interactionDetailsPanel.removeAll();
					interactionDetailsPanel.add(
							new TwoIncPanel(traits, t1Choices, t2Choices, t3Choices, this));
					interactionTypePanel.revalidate();
					modelBuilderUI.updateUI();
					clearValues();
				}
				if (e.getItem().toString().equals(
						Messages.getInstance().getString("VGLII.Complementation"))) {
					interactionDetailsPanel.setBorder(
							BorderFactory.createTitledBorder(
									Messages.getInstance().getString("VGLII.Pathway")));
					interactionDetailsPanel.removeAll();
					interactionDetailsPanel.add(
							new ComplementationPanel(traits, t1Choices, t2Choices, this));
					interactionTypePanel.revalidate();
					modelBuilderUI.updateUI();
					clearValues();
				}
				if (e.getItem().toString().equals(
						Messages.getInstance().getString("VGLII.Epistasis"))) {
					interactionDetailsPanel.setBorder(
							BorderFactory.createTitledBorder(
									Messages.getInstance().getString("VGLII.Pathway")));
					interactionDetailsPanel.removeAll();
					interactionDetailsPanel.add(
							new EpistasisPanel(traits, t1Choices, t2Choices, t3Choices, this));
					interactionTypePanel.revalidate();
					modelBuilderUI.updateUI();
					clearValues();
				}
			}
			if (alleleNumberChoices.getSelectedItem().toString().equals(
					"3-" + Messages.getInstance().getString("VGLII.Allele"))) {
				if (e.getItem().toString().equals(
						Messages.getInstance().getString("VGLII.Unknown"))) {
					interactionDetailsPanel.removeAll();
					interactionDetailsPanel.add(new UnknownSpecificsPanel());
					interactionDetailsPanel.revalidate();
					modelBuilderUI.updateUI();
					clearValues();
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
									t6Choices, 
									this));
					interactionTypePanel.revalidate();		
					modelBuilderUI.updateUI();
					clearValues();
				}
				if (e.getItem().toString().equals(
						Messages.getInstance().getString("VGLII.HierarchicalDominance"))) {
					interactionDetailsPanel.removeAll();
					interactionDetailsPanel.add(
							new ThreeHierPanel(traits, 
									t1Choices, t2Choices, t3Choices, this));
					interactionTypePanel.revalidate();	
					modelBuilderUI.updateUI();
					clearValues();
				}
				if (e.getItem().toString().equals(
						Messages.getInstance().getString("VGLII.CircularDominance"))) {
					interactionDetailsPanel.removeAll();
					interactionDetailsPanel.add(
							new ThreeCircPanel(traits, 
									t1Choices, t2Choices, t3Choices, this));
					interactionTypePanel.revalidate();	
					modelBuilderUI.updateUI();
					clearValues();
				}
			}
		}
	}

	/**
	 * get the cages selected as relevant
	 * since the 0th element in the choice list is "?"
	 * the index = the displayed cage number
	 * so need to subtract 1 to get internal cage index
	 * @return
	 */
	public ArrayList<Integer> getRelevantCages() {
		ArrayList<Integer> result = new ArrayList<Integer>();
		if (sexLinkageCageChoices != null) 
			result.add(sexLinkageCageChoices.getSelectedIndex());
		if (interactionCageChoices != null)
			result.add(interactionCageChoices.getSelectedIndex());
		return result;
	}

	public void setupActionListeners() {
		if (alleleNumberChoices != null) {
			alleleNumberChoices.addItemListener(this);	
		}
	}
	
	/*
	 * 	0 = unknown
	 * 	1 = not sex-linked
	 *  2 = xx/xy
     *  3 = zz/zw (if present)
	 */
	public int getSexLinkageChoice() {
		return sexLinkageChoices.getSelectedIndex();
	}
	
	/*
	 *  0 = unknown
	 *  1 = 2-allele
	 *  2 = 3-allele
	 */
	public int getAlleleNumberChoice() {
		return alleleNumberChoices.getSelectedIndex();
	}
	
	/*
	 * this returns the string that was selected
	 * in the local language 
	 */
	public String getInteractionTypeChoice() {
		return interactionTypeChoices.getSelectedItem().toString();
	}

	public void setStateFromFile(Element element) {

		List<Element> elements = element.getChildren();
		Iterator<Element> it = elements.iterator();
		while(it.hasNext()) {
			Element e = it.next();
			if (e.getName().equals("SexLinkage")) {
				sexLinkageChoices.setSelectedIndex(Integer.parseInt(e.getText()));
			}

			if (e.getName().equals("AlleleNumber")) {
				alleleNumberChoices.setSelectedIndex(Integer.parseInt(e.getText()));
			}

			if (e.getName().equals("InteractionType")) {
				interactionTypeChoices.setSelectedIndex(Integer.parseInt(e.getText()));
			}

			// get a reference to the details panel
			ModelDetailsPanel mdp = (ModelDetailsPanel)interactionDetailsPanel.getComponents()[0];

			if (e.getName().equals("T1")) {
				setT1Value(Integer.parseInt(e.getText()));
				mdp.updateT1Choices(Integer.parseInt(e.getText()));
			}

			if (e.getName().equals("T2")) {
				setT2Value(Integer.parseInt(e.getText()));
				mdp.updateT2Choices(Integer.parseInt(e.getText()));
			}

			if (e.getName().equals("T3")) {
				setT3Value(Integer.parseInt(e.getText()));
				mdp.updateT3Choices(Integer.parseInt(e.getText()));
			}

			if (e.getName().equals("T4")) {
				setT4Value(Integer.parseInt(e.getText()));
				mdp.updateT4Choices(Integer.parseInt(e.getText()));
			}

			if (e.getName().equals("T5")) {
				setT5Value(Integer.parseInt(e.getText()));
				mdp.updateT5Choices(Integer.parseInt(e.getText()));
			}

			if (e.getName().equals("T6")) {
				setT6Value(Integer.parseInt(e.getText()));
				mdp.updateT6Choices(Integer.parseInt(e.getText()));
			}

			if (e.getName().equals("SexLinkageCage")) {
				sexLinkageCageChoices.setSelectedIndex(Integer.parseInt(e.getText()));
			}

			if (e.getName().equals("DetailsCage")) {
				interactionCageChoices.setSelectedIndex(Integer.parseInt(e.getText()));
			}

		}
	}

	public Element save() {
		Element mpe = new Element("Character");
		mpe.setAttribute("Name", character);
		mpe.setAttribute("Index", String.valueOf(index));

		Element e = null;
		e = new Element("SexLinkage");
		e.setText(String.valueOf(sexLinkageChoices.getSelectedIndex()));
		mpe.addContent(e);

		e = new Element("AlleleNumber");
		e.setText(String.valueOf(alleleNumberChoices.getSelectedIndex()));
		mpe.addContent(e);

		if (interactionTypeChoices != null) {
			e = new Element("InteractionType");
			e.setText(String.valueOf(interactionTypeChoices.getSelectedIndex()));
			mpe.addContent(e);
		}

		e = new Element("T1");
		e.addContent(String.valueOf(t1Value));
		mpe.addContent(e);

		e = new Element("T2");
		e.addContent(String.valueOf(t2Value));
		mpe.addContent(e);

		e = new Element("T3");
		e.addContent(String.valueOf(t3Value));
		mpe.addContent(e);

		e = new Element("T4");
		e.addContent(String.valueOf(t4Value));
		mpe.addContent(e);

		e = new Element("T5");
		e.addContent(String.valueOf(t5Value));
		mpe.addContent(e);

		e = new Element("T6");
		e.addContent(String.valueOf(t6Value));
		mpe.addContent(e);

		if (sexLinkageCageChoices != null) {
			e = new Element("SexLinkageCage");
			e.addContent(String.valueOf(sexLinkageCageChoices.getSelectedIndex()));
			mpe.addContent(e);
		}

		e = new Element("DetailsCage");
		e.addContent(String.valueOf(interactionCageChoices.getSelectedIndex()));
		mpe.addContent(e);

		return mpe;
	}

	/*
	 * if for grader, the items that are not choices
	 * 		(that is, their choice lists have zero length)
	 *   	should be shown grayed out
	 */
	public String getAsHtml(boolean isForGrader) {
		StringBuffer b = new StringBuffer();
		b.append("<b>" + character + "</b><br>");
		b.append("<ul>");

		b.append("<li>");
		if (sexLinkageChoices.getItemCount() == 1) 
			b.append("<font color=" + NOT_A_CHOICE_COLOR + ">");
		b.append(sexLinkageChoices.getSelectedItem());
		if (sexLinkageChoices.getItemCount() == 1) b.append("</font>");
		b.append("</li>");

		b.append("<li>");
		if (alleleNumberChoices.getItemCount() == 1) 
			b.append("<font color=" + NOT_A_CHOICE_COLOR + ">");
		b.append(alleleNumberChoices.getSelectedItem());
		if (alleleNumberChoices.getItemCount() == 1) b.append("</font>");
		b.append("</li>");

		if (interactionTypeChoices != null) {

			b.append("<li>");
			if (interactionTypeChoices.getItemCount() == 1)
				b.append("<font color=" + NOT_A_CHOICE_COLOR + ">");
			b.append(interactionTypeChoices.getSelectedItem());
			if (interactionTypeChoices.getItemCount() == 1) b.append("</font>");
			b.append("</li>");

			ModelDetailsPanel mdp = (ModelDetailsPanel)interactionDetailsPanel.getComponents()[0];
			b.append(mdp.getAsHtml(isForGrader));
		}

		b.append("<li>");
		if (isForGrader) {
			b.append("Relevant Cages:");
		} else {
			b.append(Messages.getInstance().getString("VGLII.RelevantCages"));
		}
		b.append("</li><ul>");

		if (sexLinkageCageChoices != null) {
			b.append("<li>");
			if (isForGrader) {
				b.append("For/against Sex-linkage:");
			} else {
				b.append(Messages.getInstance().getString("VGLII.ForSexLinkage") + " ");				
			}
			b.append("<b>");
			b.append(sexLinkageCageChoices.getSelectedItem() + "</b></li>");
		}

		b.append("<li>");
		if (isForGrader) {
			b.append("For allele interaction type:");
		} else {
			b.append(Messages.getInstance().getString("VGLII.ForDetails") + " ");
		}
		b.append("<b>");

		b.append(interactionCageChoices.getSelectedItem() + "</b></li>");
		b.append("</ul>");
		b.append("</ul><br>");
		return b.toString();
	}
}
