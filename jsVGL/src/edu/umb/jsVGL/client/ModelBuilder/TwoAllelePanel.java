package edu.umb.jsVGL.client.ModelBuilder;

import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JPanel;

import VGL.Messages;

public class TwoAllelePanel extends JPanel {

	private JComboBox interactionTypeChoices;

	public TwoAllelePanel(boolean incDomPossible, 
			boolean complementationPossible, 
			boolean epistasisPossible) {
		ArrayList<String> choiceStrings = new ArrayList<String>();
		choiceStrings.add(Messages.getInstance().getString("VGLII.Unknown"));
		choiceStrings.add(Messages.getInstance().getString("VGLII.SimpleDominance"));
		if (incDomPossible || epistasisPossible) {
			choiceStrings.add(Messages.getInstance().getString("VGLII.IncompleteDominance"));
		}
		if (complementationPossible) {
			choiceStrings.add(Messages.getInstance().getString("VGLII.Complementation"));
		}
		if (epistasisPossible) {
			choiceStrings.add(Messages.getInstance().getString("VGLII.Epistasis"));
		}
		String[] choices = new String[choiceStrings.size()];
		for (int i = 0; i < choices.length; i++) {
			choices[i] = choiceStrings.get(i);
		}
		interactionTypeChoices = new JComboBox(choices);
		this.add(interactionTypeChoices);
	}

	public JComboBox getInteractionTypeChoices() {
		return interactionTypeChoices;
	}

}
