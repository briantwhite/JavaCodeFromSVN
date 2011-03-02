package VGL;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;

import GeneticModels.Cage;
import GeneticModels.Organism;
import GeneticModels.OrganismList;
import ModelBuilder.ModelBuilderUI;

public class GetWorkAsHTML {
	
	/**
	 * generate an html representation of the current work
	 * used by print() 
	 *
	 */
	public static String getWorkAsHTML(
			ArrayList<CageUI> cageCollection, 
			ModelBuilderUI modelBuilder) {
		
		StringBuffer htmlString = new StringBuffer();
		htmlString.append("<html><body>"); //$NON-NLS-1$
		for (int i = 0; i < cageCollection.size(); i++) {
			Cage c = (cageCollection.get(i)).getCage();
			ArrayList<Organism> parents = c.getParents();
			Organism parent1 = (Organism) parents.get(0);
			Organism parent2 = (Organism) parents.get(1);
			TreeMap<String, OrganismList> children = c.getChildren();
			int id = c.getId();
			htmlString.append("<table border=1><tr><td align=center colspan=3" //$NON-NLS-1$
					+ " bgcolor=#C0C0C0>Cage " + (id + 1) + "</td></tr>"); //$NON-NLS-1$ //$NON-NLS-2$
			htmlString.append("<tr><td nowrap colspan=3>");
			htmlString.append(Messages.getInstance().getInstance().getString("VGLII.Parents")); //$NON-NLS-1$
			if (parent1 != null && parent2 != null) {
				htmlString.append("<ul><li>" + parent1.getSexString() + " " //$NON-NLS-1$ //$NON-NLS-2$
						+ Messages.getInstance().getInstance().translateLongPhenotypeName(parent1.getPhenotypeString()) + " " 
						+ Messages.getInstance().getInstance().getString("VGLII.FromCage") //$NON-NLS-1$
						+ (parent1.getCageId() + 1));
				htmlString.append("<li>" + parent2.getSexString() + " " //$NON-NLS-1$ //$NON-NLS-2$
						+ Messages.getInstance().getInstance().translateLongPhenotypeName(parent2.getPhenotypeString()) + " " 
						+ Messages.getInstance().getInstance().getString("VGLII.FromCage") //$NON-NLS-1$
						+ (parent2.getCageId() + 1) + "</ul>"); //$NON-NLS-1$
			}
			htmlString.append("</td></tr>"); //$NON-NLS-1$
			htmlString.append("<tr><td nowrap align=center colspan=3>");
			htmlString
			.append(Messages.getInstance().getInstance().getString("VGLII.Offspring")); //$NON-NLS-1$
			htmlString.append("</td></tr>");
			htmlString.append("<tr><td nowrap align=center>");
			htmlString.append(Messages.getInstance().getInstance().getString("VGLII.Phenotype") //$NON-NLS-1$
					+ "</td>"
					+ "<td nowrap align=center>"
					+ Messages.getInstance().getInstance().getString("VGLII.Sex") //$NON-NLS-1$
					+ "</td>"
					+ "<td nowrap align=center>"
					+ Messages.getInstance().getInstance().getString("VGLII.Count")
					+ "</td></tr>"); //$NON-NLS-1$
			Iterator<String> it = children.keySet().iterator();
			while (it.hasNext()) {
				String phenotype = it.next();
				OrganismList l = children.get(phenotype);

				htmlString.append("<tr><td nowrap align=center>" 
						+ Messages.getInstance().getInstance().translateLongPhenotypeName(phenotype) //$NON-NLS-1$
						+ "</td>" + "<td nowrap align=center>" + Messages.getInstance().getInstance().getString("VGLII.Male") //$NON-NLS-1$ //$NON-NLS-2$
						+ "</td><td nowrap align=center>" + l.getNumberOfMales() //$NON-NLS-1$
						+ "</td></tr>"); //$NON-NLS-1$
				htmlString.append("<tr><td nowrap align=center>" 
						+ Messages.getInstance().getInstance().translateLongPhenotypeName(phenotype) //$NON-NLS-1$
						+ "</td>" 
						+"<td nowrap align=center>"
						+ Messages.getInstance().getInstance().getString("VGLII.Female") //$NON-NLS-1$ //$NON-NLS-2$
						+ "</td><td nowrap align=center>" + l.getNumberOfFemales() //$NON-NLS-1$
						+ "</td></tr>"); //$NON-NLS-1$
			}
			htmlString.append("</table><p></p>"); //$NON-NLS-1$
		}

		htmlString.append("<br><br><hr>");
		htmlString.append("<h3>");
		htmlString.append(Messages.getInstance().getString("VGLII.ModelBuilder"));
		htmlString.append("</h3>");
		htmlString.append(modelBuilder.getAsHtml(false));

		htmlString.append("</body></html>"); //$NON-NLS-1$
		return htmlString.toString();
	}

}
