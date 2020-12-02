package edu.umb.jsAipotu.molGenExp;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

public class OrganismCell extends AbstractCell<Organism> {
	
	private Organism org;
	
	public OrganismCell(Organism o) {
		this.org = o;
	}

	@Override
	public void render(Context context, Organism org, SafeHtmlBuilder sb) {
		sb.appendEscaped(org.getName());
	}

}
