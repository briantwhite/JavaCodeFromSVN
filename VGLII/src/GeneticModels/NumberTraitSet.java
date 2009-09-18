package GeneticModels;

import java.util.ArrayList;

import VGL.Messages;

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
 * @version 1.0 $Id: NumberTraitSet.java,v 1.7 2009-09-18 19:55:12 brian Exp $
 */

public class NumberTraitSet extends TraitSet {
	
	public NumberTraitSet(String bodyPart) {
		traits = new ArrayList<Trait>();
		traits.add(new NumberTrait(Messages.getString("VGLII.One"), bodyPart));
		traits.add(new NumberTrait(Messages.getString("VGLII.Two"), bodyPart));
		traits.add(new NumberTrait(Messages.getString("VGLII.Three"), bodyPart));
		traits.add(new NumberTrait(Messages.getString("VGLII.Four"), bodyPart));
		traits.add(new NumberTrait(Messages.getString("VGLII.Five"), bodyPart));
		traits.add(new NumberTrait(Messages.getString("VGLII.Six"), bodyPart));
	}

}
