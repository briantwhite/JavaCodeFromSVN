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
 * @version 1.0 $Id$
 */

public class ColorTraitSet extends TraitSet {
	
	public ColorTraitSet(String bodyPart) {
		traits = new ArrayList<Trait>();
		traits.add(new ColorTrait(Messages.getString("VGLII.Red"), bodyPart));
		traits.add(new ColorTrait(Messages.getString("VGLII.Green"), bodyPart));
		traits.add(new ColorTrait(Messages.getString("VGLII.Blue"), bodyPart));
		traits.add(new ColorTrait(Messages.getString("VGLII.Yellow"), bodyPart));
		traits.add(new ColorTrait(Messages.getString("VGLII.Purple"), bodyPart));
		traits.add(new ColorTrait(Messages.getString("VGLII.Black"), bodyPart));
		traits.add(new ColorTrait(Messages.getString("VGLII.Brown"), bodyPart));
		traits.add(new ColorTrait(Messages.getString("VGLII.Gray"), bodyPart));
	}
	
}
