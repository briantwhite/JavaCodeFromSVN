package GeneticModels;

import java.util.ArrayList;
import java.util.Random;

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
 * @version 1.0 $Id: CharacterSpecification.java,v 1.3 2008-06-10 15:34:26 brian Exp $
 */

public class CharacterSpecification {

	ArrayList<TraitSet> possibleTraitSets;
	
	public CharacterSpecification() {
		possibleTraitSets = new ArrayList<TraitSet>();
	}
	
	public void add(TraitSet ts) {
		possibleTraitSets.add(ts);
	}
	
	public int geteNumberOfMembers() {
		return possibleTraitSets.size();
	}
	
	//get TraitSet and remove it from list to prevent re-use
	public TraitSet getRandomTraitSet() {
		if (possibleTraitSets.size() == 0) {
			return null;
		}
		
		Random r = new Random();
		int i = r.nextInt(possibleTraitSets.size());
		TraitSet ts = possibleTraitSets.get(i);
		possibleTraitSets.remove(i);
		return ts;
	}
}
