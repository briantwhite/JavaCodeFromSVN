package edu.umb.jsVGL.client.VGL;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;

import edu.umb.jsVGL.client.GeneticModels.Cage;
import edu.umb.jsVGL.client.GeneticModels.Organism;
import edu.umb.jsVGL.client.GeneticModels.OrganismList;
import edu.umb.jsVGL.client.ModelBuilder.ModelBuilderUI;

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
		htmlString.append("<html><body>"); 
		htmlString.append("<h3>A summary of your data:</h3>");
		for (int i = 0; i < cageCollection.size(); i++) {
			Cage c = (cageCollection.get(i)).getCage();
			ArrayList<Organism> parents = c.getParents();
			Organism parent1 = (Organism) parents.get(0);
			Organism parent2 = (Organism) parents.get(1);
			TreeMap<String, OrganismList> children = c.getChildren();
			int id = c.getId();
			htmlString.append("<table border=1><tr><td align=center colspan=3" 
					+ " bgcolor=#C0C0C0>Cage " + (id + 1) + "</td></tr>"); 
			htmlString.append("<tr><td nowrap colspan=3>");
			htmlString.append("Parents"); 
			if (parent1 != null && parent2 != null) {
				htmlString.append("<ul><li>" + parent1.getSexString() + " " 
						+ parent1.getPhenotypeString() + " " 
						+ "From Cage"
						+ (parent1.getCageId() + 1));
				htmlString.append("<li>" + parent2.getSexString() + " " 
						+ parent2.getPhenotypeString() + " " 
						+ "From Cage"
						+ (parent2.getCageId() + 1) + "</ul>"); 
			}
			htmlString.append("</td></tr>"); 
			htmlString.append("<tr><td nowrap align=center colspan=3>");
			htmlString.append("Offspring");
			htmlString.append("</td></tr>");
			htmlString.append("<tr><td nowrap align=center>");
			htmlString.append("Phenotype"
					+ "</td>"
					+ "<td nowrap align=center>"
					+ "Sex"
					+ "</td>"
					+ "<td nowrap align=center>"
					+ "Count"
					+ "</td></tr>"); 
			Iterator<String> it = children.keySet().iterator();
			while (it.hasNext()) {
				String phenotype = it.next();
				OrganismList l = children.get(phenotype);

				htmlString.append("<tr><td nowrap align=center>" 
						+ phenotype
						+ "</td>" + "<td nowrap align=center>" + "Male"
						+ "</td><td nowrap align=center>" + l.getNumberOfMales() 
						+ "</td></tr>"); 
				htmlString.append("<tr><td nowrap align=center>" 
						+ phenotype
						+ "</td>" 
						+"<td nowrap align=center>"
						+ "Female"
						+ "</td><td nowrap align=center>" + l.getNumberOfFemales() 
						+ "</td></tr>"); 
			}
			htmlString.append("</table><p></p>"); 
		}

		htmlString.append("<br><br><hr>");
		htmlString.append("<h3>");
		htmlString.append("The model you entered:");
		htmlString.append("</h3>");
		htmlString.append(modelBuilder.getAsHtml());
				

		htmlString.append("</body></html>"); 
		return htmlString.toString();
	}

}