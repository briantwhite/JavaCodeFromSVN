package edu.umb.jsVGL.client.ModelBuilder;

import javax.swing.JComboBox;
import javax.swing.JPanel;

import VGL.Messages;

public class ThreeAllelePanel extends JPanel {
	
	private JComboBox interactionTypeChoices;

	public ThreeAllelePanel(boolean circularPossible) {
		String[] choices = null;
		if (circularPossible) {
			choices = new String[4];
			choices[0] = Messages.getInstance().getString("VGLII.Unknown");
			choices[1] = Messages.getInstance().getString("VGLII.HierarchicalDominance");
			choices[2] = Messages.getInstance().getString("VGLII.CircularDominance");
			choices[3] = Messages.getInstance().getString("VGLII.IncompleteDominance");
		} else {
			choices = new String[3];
			choices[0] = Messages.getInstance().getString("VGLII.Unknown");
			choices[1] = Messages.getInstance().getString("VGLII.HierarchicalDominance");
			choices[2] = Messages.getInstance().getString("VGLII.IncompleteDominance");
		}
		interactionTypeChoices = new JComboBox(choices);
		this.add(interactionTypeChoices);
	}
	
	public JComboBox getInteractionTypeChoices() {
		return interactionTypeChoices;
	}


}
