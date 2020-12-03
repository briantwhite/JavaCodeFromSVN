package edu.umb.jsAipotu.client.molGenExp;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;

public class GreenhouseCell extends AbstractCell<Organism> {

	interface Templates extends SafeHtmlTemplates {
		@SafeHtmlTemplates.Template("<table style=\"border:1px solid black\"><tr><td style=\"width:100px\"><img src=\"images/red.gif\"/></td></tr><tr><td>{0}</td></tr></table>")
		SafeHtml cell(SafeHtml value);
	}
	private static Templates templates = GWT.create(Templates.class);

	public void render(Context context, Organism value, SafeHtmlBuilder sb) {
		if (value == null) {
			return;
		}
		SafeHtml safeName = SafeHtmlUtils.fromString(value.getName());
		sb.append(templates.cell(safeName));
	}

}
