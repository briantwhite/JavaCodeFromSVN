package ModelBuilder;

import java.awt.GridLayout;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import VGL.Messages;

public class LinkagePanel extends JPanel {
	
	private JComboBox g1g2Linked;
	private JComboBox g2g3Linked;
	private JComboBox g3g1Linked;
	
	public LinkagePanel(String[] characters) {
		String[] choices = new String[3];
		choices[0] = Messages.getInstance().getString("VGLII.Unknown");
		choices[1] = Messages.getInstance().getString("VGLII.Unlinked");
		choices[2] = Messages.getInstance().getString("VGLII.Linked");
		g1g2Linked = new JComboBox(choices);
		g2g3Linked = new JComboBox(choices);
		g3g1Linked = new JComboBox(choices);
		
		
		add(new JLabel(
				characters[0] + " "
				           + Messages.getInstance().getString("VGLII.And") + " "
				           + characters[1] + " "
				                        + Messages.getInstance().getString("VGLII.Are")));
		add(g1g2Linked);
		
		if (characters.length == 3) {
			setLayout(new GridLayout(3,2));
			add(new JLabel(
					characters[1] + " "
					           + Messages.getInstance().getString("VGLII.And") + " "
					           + characters[2] + " "
					                        + Messages.getInstance().getString("VGLII.Are")));
			add(g2g3Linked);
			
			add(new JLabel(
					characters[0] + " "
					           + Messages.getInstance().getString("VGLII.And") + " "
					           + characters[2] + " "
					                        + Messages.getInstance().getString("VGLII.Are")));
			add(g3g1Linked);
			
		}
		
		
	}

}
