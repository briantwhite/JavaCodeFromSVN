package edu.umb.jsAipotu.client.molGenExp;

import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SimplePanel;

public class CombinedColorPanel extends HorizontalPanel {

	private SimplePanel colorChip;

	public CombinedColorPanel() {
		this.add(new HTML("Combined Color:  "));
		colorChip = new SimplePanel();
		colorChip.setStyleName("colorChip");
		colorChip.getElement().getStyle().setBackgroundColor("white");
		this.add(colorChip);
	}
	
	public void setCombinedColor(CssColor color) {
		colorChip.getElement().getStyle().setBackgroundColor(color.toString());
	}

}
