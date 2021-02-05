package edu.umb.jsAipotu.client.biochem;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.safecss.shared.SafeStyles;
import com.google.gwt.safecss.shared.SafeStylesBuilder;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;

import edu.umb.jsAipotu.client.JsAipotu;

public class BiochemHistListItem extends AbstractCell<FoldedProteinWithImages>{
	
	interface Templates extends SafeHtmlTemplates {
		@SafeHtmlTemplates.Template("<table style=\"{0}\"><tr><td>{1}</td></tr><tr><td>{2}</td></tr></table>")
		SafeHtml cell(SafeStyles colorStyle, SafeHtml imageHTML, SafeHtml labelHTML);
	}
	private static Templates templates = GWT.create(Templates.class);

	public void render(Context context, FoldedProteinWithImages fp, SafeHtmlBuilder sb) {
		if (fp == null) {
			return;
		}
		JsAipotu.consoleLog("Biochem hist list item 27: thumb width=" + fp.getThumbnailPic().getCoordinateSpaceWidth() + " height=" + fp.getThumbnailPic().getCoordinateSpaceHeight());
		SafeStylesBuilder b = new SafeStylesBuilder();
		b.appendTrustedString("border:1px solid black;");
		b.trustedBackgroundColor(fp.getColor().toString());
		SafeStyles colorStyle = b.toSafeStyles();
		SafeHtml safeImage = SafeHtmlUtils.fromTrustedString("<img src=\"" + fp.getThumbnailPic().toDataUrl() + "\" />");
		SafeHtml safeLabel;
		if (fp.getColor().value().equals("rgb(0,0,0)")) {
			// black color needs white letters
			safeLabel = SafeHtmlUtils.fromTrustedString("<font color=\"white\">" + fp.getAaSeq() + "</font>");
		} else {
			safeLabel = SafeHtmlUtils.fromTrustedString("<font color=\"black\">" + fp.getAaSeq() + "</font>");
		}
		sb.append(templates.cell(colorStyle, safeImage, safeLabel));
	}

}
