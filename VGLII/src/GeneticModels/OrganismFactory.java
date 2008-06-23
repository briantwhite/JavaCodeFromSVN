package GeneticModels;

import java.util.Iterator;

import org.jdom.Element;

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

		return new Organism(cageId,
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

	}

}
