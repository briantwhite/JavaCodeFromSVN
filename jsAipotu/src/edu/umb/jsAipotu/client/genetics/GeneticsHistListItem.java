package edu.umb.jsAipotu.client.genetics;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.safecss.shared.SafeStyles;
import com.google.gwt.safecss.shared.SafeStylesBuilder;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;

public class GeneticsHistListItem extends AbstractCell<Tray>{

	public GeneticsHistListItem() {
	}

	interface Templates extends SafeHtmlTemplates {
		@SafeHtmlTemplates.Template("<table style=\"{0}\"><tr><td>{1}</td></tr><tr><td>{2}</td></tr></table>")
		SafeHtml cell(SafeStyles colorStyle, SafeHtml imageHTML, SafeHtml safeLabel);
	}
	private static Templates templates = GWT.create(Templates.class);

	@Override
	public void render(Context context, Tray tray, SafeHtmlBuilder sb) {
		if (tray == null) {
			return;
		}

		SafeStylesBuilder b = new SafeStylesBuilder();
		b.tableLayout(Style.TableLayout.FIXED);
		b.width(180, Unit.PX);
		if (tray.isSelected()) {
			b.appendTrustedString("border:2px solid red;");
		} else {
			b.appendTrustedString("border:1px solid black;");
		}
		b.trustedBackgroundColor("LightGray");
		SafeStyles colorStyle = b.toSafeStyles();
		SafeHtml safeImage = SafeHtmlUtils.fromTrustedString("<img src=\"" + tray.getThumbCanvas().toDataUrl() + "\" />");
		SafeHtml safeLabel = SafeHtmlUtils.fromTrustedString("<font color=\"black\">Tray " + String.valueOf(tray.getNumber()) + "</font>");
		sb.append(templates.cell(colorStyle, safeImage, safeLabel));

	}

}
