package evolution;

import molGenExp.MolGenExp;

public class Evolver implements Runnable {
	
	private MolGenExp mge;
	private EvolutionWorkArea evolutionWorkArea;
	private World world;
	
	public Evolver(final MolGenExp mge) {
		this.mge = mge;
		this.evolutionWorkArea = mge.getEvolutionWorkArea();
		this.world = mge.getEvolutionWorkArea().getWorld();
	}

	public void run() {
		// find the gene pool
		
		//make next generation
	}

}
