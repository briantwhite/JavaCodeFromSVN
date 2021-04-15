package edu.umb.jsAipotu.client.molGenExp;

import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.SimplePanel;

import edu.umb.jsAipotu.client.preferences.GlobalDefaults;

public abstract class WorkPanel extends CaptionPanel {
	
	protected SimplePanel colorChip;
	protected HTML colorName;

	
	public WorkPanel(String title) {
		super(title);
		colorChip = new SimplePanel();
		colorName = new HTML();
		colorName.setStyleName("colorChipLabelText");
		colorChip.setStyleName("colorChip");
		colorChip.add(colorName);
	}
	
	protected void updateColorChip(CssColor proteinColor) {
		colorChip.getElement().getStyle().setBackgroundColor(proteinColor.toString());
		if (proteinColor.toString().equals("rgb(0,0,0)") || proteinColor.toString().equals("rgb(0,0,255)") || proteinColor.toString().equals("rgb(138,43,226)")) {
			// dark colors need white letters
			colorName.setHTML(SafeHtmlUtils.fromTrustedString("<font color=\"white\">" + GlobalDefaults.colorModel.getColorName(proteinColor) + "</font>"));
		} else {
			colorName.setHTML(SafeHtmlUtils.fromTrustedString("<font color=\"black\">" + GlobalDefaults.colorModel.getColorName(proteinColor) + "</font>"));
		}
	}

}
