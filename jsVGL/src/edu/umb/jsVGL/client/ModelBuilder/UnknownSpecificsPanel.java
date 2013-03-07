package edu.umb.jsVGL.client.ModelBuilder;

import java.awt.event.ItemEvent;

import javax.swing.JLabel;

public class UnknownSpecificsPanel extends ModelDetailsPanel {

	public UnknownSpecificsPanel() {
		this.add(new JLabel(Messages.getInstance().getString("VGLII.MustSelectType")));
	}

	public void itemStateChanged(ItemEvent e) {}

	public String getAsHtml(boolean isForGrader) {
		StringBuffer b = new StringBuffer();
		b.append("<ul>");
		if (isForGrader) {
			b.append("<li>Unknown</li> ");
		} else {
			b.append("<li>" + Messages.getInstance().getString("VGLII.Unknown") + "</li> ");
		}
		b.append("</ul>");
		return b.toString();
	}

}
