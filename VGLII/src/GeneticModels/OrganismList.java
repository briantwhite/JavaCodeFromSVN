package GeneticModels;

import java.util.ArrayList;
import java.util.Iterator;

import VGL.GeneticsException;

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

public class OrganismList {
	
	private ArrayList<Organism> organisms;
	private int numberOfMales;
	private int numberOfFemales;
	
	public OrganismList() {
		organisms = new ArrayList<Organism>();
		numberOfFemales = 0;
		numberOfMales = 0;
	}
	
	public void add(Organism o) {
		organisms.add(o);
		if (o.isMale()) {
			numberOfMales++;
		} else {
			numberOfFemales++;
		}
	}
	
	/**
	 * Return the organism which is at the given index.
	 * 
	 * @param index
	 *            the index
	 * @return the organism at the given index
	 */
	public Organism get(int index) {
		return organisms.get(index);
	}
	
	/**
	 * Return the organism which has the given id.
	 * 
	 * @param id
	 *            the organism's id
	 * @return the organism which has the given id
	 */
	public Organism find(int id) throws Exception {
		for (int i = 0; i < organisms.size(); i++) {
			Organism o = organisms.get(i);
			if (o.getId() == id)
				return o;
		}
		throw new GeneticsException("Cannot find Organism");
	}

	
	public int getNumberOfMales() {
		return numberOfMales;
	}
	
	public int getNumberOfFemales() {
		return numberOfFemales;
	}
	
	public int getTotalNumber() {
		return numberOfMales + numberOfFemales;
	}
	
	public ArrayList<Organism> getAllOrganisms() {
		return organisms;
	}
	
	public Iterator<Organism> iterator() {
		return organisms.iterator();
	}

}
