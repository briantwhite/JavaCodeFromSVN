package edu.umb.jsAipotu.client.genetics;


import edu.umb.jsAipotu.client.biochem.FoldingException;
import edu.umb.jsAipotu.client.molGenExp.ExpressedAndFoldedGene;
import edu.umb.jsAipotu.client.molGenExp.Organism;
import edu.umb.jsAipotu.client.molGenExp.OrganismFactory;

public class MutantGenerator  {

	private OrganismFactory organismFactory;
	private Mutator mutator;

	private static MutantGenerator instance;

	public static MutantGenerator getInstance() {
		if (instance == null) {
			instance = new MutantGenerator();
		}
		return instance;
	}

	private MutantGenerator () {
		mutator = Mutator.getInstance();
		organismFactory = new OrganismFactory();
	}

	public Organism getMutantOf(Organism o, String name) {
		ExpressedAndFoldedGene efg1 = null;
		ExpressedAndFoldedGene efg2 = null;
		efg1 = makeAGoodMutant(o.getGene1());
		efg2 = makeAGoodMutant(o.getGene2());
		return organismFactory.createOrganism(name, efg1, efg2);
	}

	private ExpressedAndFoldedGene makeAGoodMutant(ExpressedAndFoldedGene efg) {
		//loop over making new organisms
		//  if one has an un-foldable protein
		//  (one that paints itself into a corner)
		//  need to go back and try other sequences
		//  until you get one that works
		ExpressedAndFoldedGene result = null;

		boolean gotAGoodOne = false;
		while(!gotAGoodOne) {
			try {
				result = mutator.mutateGene(efg);
				gotAGoodOne = true;
			} catch (FoldingException e) {
				gotAGoodOne = false;
			}
			// see if it was folded in a corner
			if (result.getFoldedProteinWithImages().getFullSizePic() == null) gotAGoodOne = false;
		}
		return result;
	}
}
