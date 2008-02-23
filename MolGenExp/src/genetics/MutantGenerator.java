package genetics;


import java.awt.Dimension;
import java.util.Random;

import javax.swing.ImageIcon;

import preferences.MGEPreferences;
import utilities.MGEUtilities;

import molBiol.ExpressedGene;
import molBiol.Gene;
import molGenExp.MolGenExp;
import molGenExp.Organism;
import molGenExp.ProteinImageFactory;
import molGenExp.ProteinImageSet;
import biochem.Attributes;
import biochem.FoldedPolypeptide;
import biochem.FoldingException;
import biochem.FoldingManager;
import biochem.OutputPalette;

public class MutantGenerator implements Runnable {

	private int mutantCount;	//number of mutants to make
	private int current;
	private Organism o;
	private int trayNum;
	private GeneticsWorkbench gw;
	private OffspringList offspringList;
	
	private MGEPreferences preferences;
	private MGEUtilities utilities;

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
		preferences = MGEPreferences.getInstance();
		utilities = new MGEUtilities();
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
			ExpressedGene eg1 = null;
			ExpressedGene eg2 = null;
			eg1 = utilities.mutateGene(o.getGene1());
			eg2 = utilities.mutateGene(o.getGene2());

			if (current < mutantCount) {
				offspringList.add(new Organism(trayNum + "-" + (current + 1),
						eg1,
						eg2));
			}
		}
	}
}
