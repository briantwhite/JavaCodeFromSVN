package ModelBuilder;

import java.awt.event.ItemEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;

import VGL.Messages;

public class UnknownSpecificsPanel extends ModelDetailsPanel {
	
	public UnknownSpecificsPanel() {
		this.add(new JLabel(Messages.getInstance().getString("VGLII.MustSelectType")));
	}

	public void itemStateChanged(ItemEvent e) {}

	public String getAsHtml() {
		StringBuffer b = new StringBuffer();
		b.append("<ul>");
		
		b.append("<li>" + Messages.getInstance().getString("VGLII.Unknown") + "</li> ");
		
		b.append("</ul>");
		return b.toString();
	}

}
