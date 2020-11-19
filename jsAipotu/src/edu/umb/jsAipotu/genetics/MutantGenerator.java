package edu.umb.jsAipotu.genetics;


import biochem.FoldedProteinArchive;
import biochem.FoldingException;
import molGenExp.ExpressedAndFoldedGene;
import molGenExp.Organism;
import molGenExp.OrganismFactory;

public class MutantGenerator implements Runnable {

	private int mutantCount;	//number of mutants to make
	private int current;
	private Organism o;
	private int trayNum;
	private GeneticsWorkbench gw;
	private OffspringList offspringList;

	private OrganismFactory organismFactory;
	private Mutator mutator;

	MutantGenerator (Organism o,
			int mutantCount,
			int trayNum,
			OffspringList offspringList, 
			GeneticsWorkbench gw) {
		this.o = o;
		this.trayNum = trayNum;
		this.gw = gw;
		this.offspringList = offspringList;
		this.mutantCount = mutantCount;
		mutator = Mutator.getInstance();
		organismFactory = new OrganismFactory();
	}

	public int getLengthOfTask() {
		return mutantCount;
	}

	public int getCurrent() {
		return current;
	}

	public Organism getOrganism() {
		return o;
	}

	public void stop() {
		current = mutantCount;
	}

	boolean done() {
		if (current >= mutantCount) {
			return true;
		} else {
			return false;
		}
	}

	public void run() {
		for (current = 0; current < mutantCount; current++) {
			ExpressedAndFoldedGene efg1 = null;
			ExpressedAndFoldedGene efg2 = null;
			efg1 = makeAGoodMutant(o.getGene1());
			efg2 = makeAGoodMutant(o.getGene2());

			if (current < mutantCount) {
				offspringList.add(
						organismFactory.createOrganism(
								trayNum + "-" + (current + 1),
								efg1,
								efg2));
			}
		}
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
