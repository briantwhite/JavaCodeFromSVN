package ModelBuilder;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import VGL.Messages;

public class TwoAllelePanel extends JPanel {
	
	public TwoAllelePanel(ModelPane parent,
			JComboBox interactionTypeChoices) {
		String[] choices = new String[3];
		choices[0] = Messages.getInstance().getString("VGLII.Unknown");
		choices[1] = Messages.getInstance().getString("VGLII.SimpleDominance");
		choices[2] = Messages.getInstance().getString("VGLII.IncompleteDominance");
		interactionTypeChoices = new JComboBox(choices);
		interactionTypeChoices.addItemListener(parent);
		this.add(interactionTypeChoices);
	}

}
