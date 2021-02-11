package edu.umb.jsAipotu.client.molBiol;

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

public class MolBiolHistListItem extends AbstractCell<ExpressedGeneWithImages>{
	
	public MolBiolHistListItem() {
	}

	interface Templates extends SafeHtmlTemplates {
		@SafeHtmlTemplates.Template("<table style=\"{0}\"><tr><td>{1}</td></tr></table>")
		SafeHtml cell(SafeStyles colorStyle, SafeHtml imageHTML);
	}
	private static Templates templates = GWT.create(Templates.class);

	public void render(Context context, ExpressedGeneWithImages eg, SafeHtmlBuilder sb) {
		if (eg == null) {
			return;
		}

		SafeStylesBuilder b = new SafeStylesBuilder();
		b.tableLayout(Style.TableLayout.FIXED);
		b.width(180, Unit.PX);
		if (eg.isSelected()) {
			b.appendTrustedString("border:2px solid red;");
		} else {
			b.appendTrustedString("border:1px solid black;");
		}
		b.trustedBackgroundColor(eg.getFoldedProteinWithImages().getColor().toString());
		SafeStyles colorStyle = b.toSafeStyles();
		SafeHtml safeImage = SafeHtmlUtils.fromTrustedString("<img src=\"" + eg.getFoldedProteinWithImages().getThumbnailPic().toDataUrl() + "\" />");
		sb.append(templates.cell(colorStyle, safeImage));
	}

}
