package ModelBuilder;

import javax.swing.JLabel;
import javax.swing.JPanel;

import VGL.Messages;

public class UnknownInteractionPanel extends JPanel{
	
	public UnknownInteractionPanel() {
		this.add(new JLabel(Messages.getInstance().getString("VGLII.MustSelectNumAlleles")));
	}

}
