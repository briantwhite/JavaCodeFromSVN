package GeneticModels;

import java.util.Iterator;

import org.jdom.Element;
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
 * @version 1.0 $Id: OrganismFactory.java,v 1.3 2008-06-24 14:13:47 brian Exp $
 */

public class OrganismFactory {

	public static Organism buildOrganism(Element e, int cageId, GeneticModel geneticModel) {
		Chromosome maternalAutosome = null;
		Chromosome paternalAutosome = null;
		Chromosome maternalSexChromosome = null;
		Chromosome paternalSexChromosome = null;

		boolean male = Boolean.parseBoolean(e.getAttributeValue("Male"));
		int id = Integer.parseInt(e.getAttributeValue("Id"));
		Iterator<Element> chromoIt = e.getChildren().iterator();
		while (chromoIt.hasNext()) {
			Element chromoE = chromoIt.next();
			String type = chromoE.getAttributeValue("Id");

			boolean isNullSexChromosome = false;
			if (Integer.parseInt(chromoE.getAttributeValue("Size")) == -1) {
				isNullSexChromosome = true;
			}

			if (type.equals("MaternalAutosome")) {
				maternalAutosome = new Chromosome(chromoE, 
						Chromosome.AUTOSOME);
			} else if (type.equals("PaternalAutosome")) {
				paternalAutosome = new Chromosome(chromoE, 
						Chromosome.AUTOSOME);
			} else if (type.equals("MaternalSexChromosome")) {
				if (isNullSexChromosome) {
					maternalSexChromosome = NullSexChromosome.getInstance();
				} else {
					maternalSexChromosome = new Chromosome(chromoE, 
							Chromosome.SEX_CHROMOSOME);
				}
			} else if (type.equals("PaternalSexChromosome")) {
				if (isNullSexChromosome) {
					paternalSexChromosome = NullSexChromosome.getInstance();
				} else {
					paternalSexChromosome = new Chromosome(chromoE, 
							Chromosome.SEX_CHROMOSOME);
				}
			}
		}

		Organism o = new Organism(cageId,
				maternalAutosome,
				paternalAutosome,
				maternalSexChromosome,
				paternalSexChromosome,
				geneticModel.getPhenotypes(maternalAutosome,
						paternalAutosome,
						maternalSexChromosome,
						paternalSexChromosome),
						male,
						geneticModel);

		o.setId(id);
		return o;
	}

}
