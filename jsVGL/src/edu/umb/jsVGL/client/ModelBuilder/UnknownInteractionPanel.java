package edu.umb.jsVGL.client.ModelBuilder;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class UnknownInteractionPanel extends JPanel{
	
	public UnknownInteractionPanel() {
		this.add(new JLabel(Messages.getInstance().getString("VGLII.MustSelectNumAlleles")));
	}

}
