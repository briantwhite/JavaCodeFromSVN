package GeneticModels;

import java.util.ArrayList;
import java.util.Random;

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
 * @version 1.0 $Id: GeneticModel.java,v 1.19 2008-06-19 17:49:57 brian Exp $
 */

//This is the wrapper class for the entire genetic model
//- it chooses from the characters and traits randomly to set up 
//each gene model.
//- it generates random organisms to populate the field cage
//- it generates offspring from selected parents

//It works like this:
//- organisms contain 4 chromosomes
//- 2 copies of the autosome (maternal and paternal)
//- 2 copies of the sex chromosome (maternal and paternal)
//- each chromosome is an ArrayList of Alleles

//- there is a ChromosomeModel for each type of chromosome (auto & sex)
//- each ChromosomeModel consists of 0 or more GeneModels
//- the GeneModels handle the genotype-phenotype conversion for one gene each

//- there is a NullSexChromosome with no alleles (actually all Null alleles)
//this is the Y or W chromosome

//- to make a random organism
//1) the GeneticModel asks each GeneModel for 2 random alleles
//chosen to give roughly equal #s of each phenotype
//2) the GeneticModel builds these into a chromosome pair
//and turns this into an Organism

//- cross 2 organisms
//1) the genetic model takes care of recombination in each parent
//using the CHromosomeModel to make 2 gametes
//2) the gametes are combined in to an Organism and its phenotype
//is determined

//- phenotypes are determined :
//1) the GeneticModel has each ChromosomeModel distribute its Alleles
//to the GeneModels to get a Phenotype from each
//2) these are pooled into a set of phenotypes for display, sorting, etc.

//- An Allele consists of:
//- an intVal = used as an integer index to the genotype-phenotype table
//(0 = the null allele - from the Y or W chromosome)
//- a Trait which contains:
//bodyPart (eye, antenna, etc)
//type (shape, colo, etc)
//traitName (green, long, etc)

//to build a GeneticModel: 
//1) Choose XX/XY or ZZ/ZW sex-linkage when you build one
//2) add the gene models to the autosome or sex chromosome as needed
//- the first one is added just as a model
//- any more on the same chromo must be added with a recombination freq
//unlinked genes have a 50% rf.
//(therefore, autosomes are modeled as one big autosome where the
//total rf can be bigger than 100%)

public class GeneticModel {

	public static final boolean XX_XY = true;
	public static final boolean ZZ_ZW = false;
	
	private int minOffspring;
	private int maxOffspring;

	private ChromosomeModel autosomeModel;
	private ChromosomeModel sexChromosomeModel;

	private boolean XX_XYsexLinkage; 

	private Random random;

	private boolean beginnerMode;   //allows viewing of model and genotypes
	
	/**
	 * because we don't want to display the traits in the CageUI in the
	 * order they appear on the chromosome, need a mapping
	 * between the trait number and the displayed trait number
	 * in this array,when i = trait number; sto[i] gives its display order
	 */
	private int[] scrambledTraitOrder;


	protected GeneticModel(boolean XX_XYsexLinkage) {
		beginnerMode = false;
		this.XX_XYsexLinkage = XX_XYsexLinkage;
		autosomeModel = new AutosomeModel();
		sexChromosomeModel = new SexChromosomeModel();
		random = new Random();
	}

	protected void addFirstAutosomalGeneModel(GeneModel gm) throws GeneticsException {
		if (autosomeModel.getNumberOfGeneModels() != 0) {
			throw new GeneticsException("Can't add first autosomal model when" 
					+ " one is already present");
		}
		autosomeModel.addGeneModel(gm);
	}

	protected void addFirstSexLinkedGeneModel(GeneModel gm) throws GeneticsException {
		if (sexChromosomeModel.getNumberOfGeneModels() != 0) {
			throw new GeneticsException("Can't add first sex-linked model when" 
					+ " one is already present");
		}
		sexChromosomeModel.addGeneModel(gm);
	}

	protected void addNextAutosomalGeneModel(float rf, GeneModel gm) throws GeneticsException {
		if (autosomeModel.getNumberOfGeneModels() == 0) {
			throw new GeneticsException("Can't add next autosomal model to" 
					+ " empty list");
		}
		autosomeModel.addGeneModel(gm);
		autosomeModel.addRecombinationFrequency(rf);
	}

	protected void addNextSexLinkedGeneModel(float rf, GeneModel gm) throws GeneticsException {
		if (sexChromosomeModel.getNumberOfGeneModels() == 0) {
			throw new GeneticsException("Can't add next sex-linked model to" 
					+ " empty list");
		}
		sexChromosomeModel.addGeneModel(gm);
		sexChromosomeModel.addRecombinationFrequency(rf);
	}
	
	public boolean isBeginnerMode() {
		return beginnerMode;
	}
	
	protected void setBeginnerMode(boolean beginnerMode) {
		this.beginnerMode = beginnerMode;
	}
	
	protected void setMinOffspring(int min) {
		minOffspring = min;
	}
	
	protected void setMaxOffspring(int max) {
		maxOffspring = max;
	}

	/**
	 * randomize the order in which traits are displayed in CageUI
	 * DO NOT RUN THIS UNTIL MODEL IS COMPLETE
	 */
	protected void scrambleTraitOrder() {
		scrambledTraitOrder = new int[getNumberOfTraits()];
		
		// fill array with blanks
		for (int i = 0; i < scrambledTraitOrder.length; i++) {
			scrambledTraitOrder[i] = -1;
		}
		
		// fill array with possible values to draw from
		int[] source = new int[getNumberOfTraits()];
		for (int i = 0; i < source.length; i++) {
			source[i] = i;
		}
		
		//draw them randomly
		for (int i = 0; i < scrambledTraitOrder.length; i++) {
			scrambledTraitOrder[i] = pickRandomUnusedInt(source);
		}
		
	}
	
	private int pickRandomUnusedInt(int[] source) {
		int i = random.nextInt(source.length);
		int val = source[i];
		while (val == -1) {
			i = random.nextInt(source.length);
			val = source[i];
		}
		source[i] = -1;
		return val;
	}
	
	public Cage generateFieldPopulation() {
		Cage cage = new Cage(0);
		int numOffspring = 
			random.nextInt(maxOffspring - minOffspring) + minOffspring;
		for (int i = 0; i < numOffspring; i++) {
			cage.add(getRandomOrganism());
		}
		return cage;
	}

	private Organism getRandomOrganism() {
		Chromosome[] autosomes = 
			autosomeModel.getChromosomePairWithRandomAlleles();
		Chromosome[] sexChromosomes = 
			sexChromosomeModel.getChromosomePairWithRandomAlleles();
		ArrayList<Phenotype> phenotypes = new ArrayList<Phenotype>();
		phenotypes.addAll(
				autosomeModel.getPhenotypes(autosomes[0], autosomes[1]));
		phenotypes.addAll(  
				sexChromosomeModel.getPhenotypes(sexChromosomes[0], sexChromosomes[1]));
		return new Organism(
				autosomes[0],
				autosomes[1],
				sexChromosomes[0],
				sexChromosomes[1],
				phenotypes,
				isMale(sexChromosomes[0], sexChromosomes[1]),
				this);
	}

	public Cage crossTwo(int newCageID, Organism mom, Organism dad) {
		Cage cage = new Cage(newCageID, mom, dad);
		int numOffspring = 
			random.nextInt(maxOffspring - minOffspring) + minOffspring;
		for (int i = 0; i < numOffspring; i++) {
			cage.add(getOffspringOrganism(newCageID, mom, dad));
		}
		return cage;
	}

	private Organism getOffspringOrganism(int cageID, Organism mom, Organism dad) {
		Chromosome maternalAutosomeContribution = null;
		Chromosome maternalSexChromosomeContribution = null;
		Chromosome paternalAutosomeContribution = null;
		Chromosome paternalSexChromosomeContribution = null;
		maternalAutosomeContribution = 
			autosomeModel.getGamete(
					mom.getMaternalAutosome(), mom.getPaternalAutosome());
		maternalSexChromosomeContribution = 
			sexChromosomeModel.getGamete(
					mom.getMaternalSexChromosome(), mom.getPaternalSexChromosome());
		paternalAutosomeContribution = 
			autosomeModel.getGamete(
					dad.getMaternalAutosome(), dad.getPaternalAutosome());
		paternalSexChromosomeContribution = 
			sexChromosomeModel.getGamete(
					dad.getMaternalSexChromosome(), dad.getPaternalSexChromosome());

		ArrayList<Phenotype> phenotypes = new ArrayList<Phenotype>();
		phenotypes.addAll(
				autosomeModel.getPhenotypes(
						maternalAutosomeContribution, 
						paternalAutosomeContribution));
		phenotypes.addAll(  
				sexChromosomeModel.getPhenotypes(
						maternalSexChromosomeContribution, 
						paternalSexChromosomeContribution));
		return new Organism(cageID,
				maternalAutosomeContribution,
				paternalAutosomeContribution,
				maternalSexChromosomeContribution,
				paternalSexChromosomeContribution,
				phenotypes,
				isMale(maternalSexChromosomeContribution, 
						paternalSexChromosomeContribution),
						this);
	}

	public boolean isMale(Chromosome sexChr1, Chromosome sexChr2) {		
		//see if one of the chromos is a NullSexChromosome (Y or W)
		boolean heterogametic = false;
		if((sexChr1 == NullSexChromosome.getInstance()) || 
				(sexChr2 == NullSexChromosome.getInstance())) {
			heterogametic = true;
		}	

		if (XX_XYsexLinkage) {
			if (heterogametic) {
				return true;
			} else {
				return false;
			}
		} else {
			if (heterogametic) {
				return false;
			} else {
				return true;
			}
		}
	}

	public boolean getSexLinkageType() {
		return XX_XYsexLinkage;
	}

	public int getNumberOfTraits() {
		return autosomeModel.getNumberOfGeneModels() 
		+ sexChromosomeModel.getNumberOfGeneModels();

	}
	
	public int[] getScrambledTraitOrder() {
		return scrambledTraitOrder;
	}
	
	public boolean anyTraitsOnSexChromosome() {
		if (sexChromosomeModel.getNumberOfGeneModels() > 0) {
			return true;
		} else {
			return false;
		}
	}

	public boolean anyTraitsOnAutosome() {
		if (autosomeModel.getNumberOfGeneModels() > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	public Element save() throws Exception {
		Element e = new Element("GeneticModel");
		e.setAttribute("BeginnerMode", String.valueOf(beginnerMode));
		e.setAttribute("NumberOfTraits", String.valueOf(getNumberOfTraits()));
		Element scrambler = new Element("TraitOrderScrambler");
		for (int i = 0; i < getNumberOfTraits(); i++) {
			scrambler.addContent(
					new Element("Trait_" + i).addContent(
							String.valueOf(scrambledTraitOrder[i])));
		}
		e.addContent(scrambler);
		e.addContent(autosomeModel.save());
		e.addContent(sexChromosomeModel.save());
		return e;
	}

	public String toString() {
		StringBuffer b = new StringBuffer();
		b.append("<html><body>");
		if (XX_XYsexLinkage) {
			b.append("XX/XY sex determination");
		} else {
			b.append("ZZ/ZW sex determination");
		}
		b.append("<br>");
		b.append(autosomeModel.toString());
		b.append(sexChromosomeModel.toString());
		b.append("</body></html>");
		return b.toString();
	}

}
