package edu.umb.jsAipotu.client.molGenExp;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

import edu.umb.jsAipotu.client.preferences.GlobalDefaults;

public class GreenhouseCell extends AbstractCell<Organism> {

	interface Templates extends SafeHtmlTemplates {
		@SafeHtmlTemplates.Template("<table style=\"border:1px solid black\"><tr><td style=\"width:100px\">{0}</td></tr><tr><td>{1}</td></tr></table>")
		SafeHtml cell(SafeHtml imageHTML, SafeHtml labelHTML);
	}
	private static Templates templates = GWT.create(Templates.class);

	public void render(Context context, Organism value, SafeHtmlBuilder sb) {
		if (value == null) {
			return;
		}
		ImageResource flowerImageResource = GlobalDefaults.colorModel.getImageResourceFromColor(value.getColor());
		SafeHtml safeImage = AbstractImagePrototype.create(flowerImageResource).getSafeHtml();
		SafeHtml safeName = SafeHtmlUtils.fromString(value.getName());
		sb.append(templates.cell(safeImage, safeName));
	}

}
