
import java.util.ArrayList;

/**
 * Naing Naing Maw cs681-3 Fall 2002 - Spring 2003 Project VGL File:
 * ~nnmaw/VGL/OList.java
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
 * @author Naing Naing Maw
 * @version 1.0 $Id: OList.java,v 1.1 2004-09-24 15:30:16 brian Exp $
 */

public class OList {
	private ArrayList elements;

	private int maleCount;

	private int femaleCount;

	/**
	 * Constructor.
	 */
	public OList() {
		elements = new ArrayList();
		maleCount = 0;
		femaleCount = 0;
	}

	/**
	 * Add the given organism.
	 * 
	 * @param o
	 *            an organism to be added
	 */
	public void add(Organism o) {
		if (o.getSexType() == 0) { // if male, add from front
			elements.add(0, o);
			maleCount++;
		} else if (o.getSexType() == 1) { // if female, add from end
			elements.add(o);
			femaleCount++;
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
		return (Organism) elements.get(index);
	}

	/**
	 * Return the organism which has the given id.
	 * 
	 * @param id
	 *            the organism's id
	 * @return the organism which has the given id
	 */
	public Organism find(int id) throws Exception {
		for (int i = 0; i < elements.size(); i++) {
			Organism o = (Organism) elements.get(i);
			if (o.getId() == id)
				return o;
		}
		throw new GeneticsException("Cannot find Organism");
	}

	/**
	 * Return the number of elements in this list.
	 * 
	 * @return the number of elements
	 */
	public int size() {
		return elements.size();
	}

	/**
	 * Return the number of male.
	 * 
	 * @return the number of male
	 */
	public int getMaleCount() {
		return maleCount;
	}

	/**
	 * Return the number of female.
	 * 
	 * @return the number of female
	 */
	public int getFemaleCount() {
		return femaleCount;
	}

	/**
	 * Return the list of elements.
	 * 
	 * @return the list of elements
	 */
	public ArrayList getArrayList() {
		return elements;
	}
}