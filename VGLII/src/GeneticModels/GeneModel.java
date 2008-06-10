package GeneticModels;

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
 * @version 1.0 $Id$
 */

public abstract class GeneModel {
	
	CharacterSpecificationBank charSpecBank;
	TraitSet traitSet;
	
	Random rand;
	
	Phenotype[][] genoPhenoTable;
	
	public GeneModel() {
		rand = new Random();
		charSpecBank = CharacterSpecificationBank.getInstance();
		traitSet = charSpecBank.getRandomTraitSet();
		setupGenoPhenoTable();
	}
	
	public abstract void setupGenoPhenoTable();
	
	public abstract Phenotype getPhenotype(Allele a1, Allele a2);
	
	public abstract Allele[] getRandomAllelePair();
	
	public abstract String toString();
	
}
