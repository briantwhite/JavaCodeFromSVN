package GeneticModels;

import java.util.HashMap;

/**
 * Brian White Summer 2008
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 * 
 * @author Brian White
 * @version 1.0 $Id$
 */

public class Cage {

	private HashMap<String, OrganismList> cageContents;

	public Cage() {
		cageContents = new HashMap<String, OrganismList>();
	}

	public void add(Organism o) {
		//if there isn't a list of organisms with this pheno
		//  make one
		if (!cageContents.containsKey(o.getPhenotypeString())) {
			OrganismList oList = new OrganismList();
			oList.add(o);
			cageContents.put(o.getPhenotypeString(), oList);
		} else {
			// add the org to the list and add to the hash map
			OrganismList oList = cageContents.get(o.getPhenotypeString());
			oList.add(o);
		}
	}

	public String toString() {
		StringBuffer b = new StringBuffer();
		b.append("results:\n");
		for (String phenoString: cageContents.keySet()) {
			OrganismList oList = cageContents.get(phenoString);
			b.append(phenoString + ": " 
					+ oList.getTotalNumber() + ", "
					+ oList.getNumberOfMales() + " males, " 
					+ oList.getNumberOfFemales() + " females\n");
		}
		return b.toString();
	}

}
