package ModelBuilder;

import javax.swing.JLabel;
import javax.swing.JPanel;

import VGL.Messages;

public class UnknownSpecificsPanel extends JPanel {
	
	public UnknownSpecificsPanel() {
		this.add(new JLabel(Messages.getInstance().getString("VGLII.MustSelectType")));
	}


}
