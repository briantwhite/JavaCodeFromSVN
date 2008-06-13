package GeneticModels;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

public class GeneticModelFactory {

	private Random r;

	private static GeneticModelFactory instance;

	private GeneticModelFactory() {
		r = new Random();
	}

	public static GeneticModelFactory getInstance() {
		if (instance == null) {
			instance = new GeneticModelFactory();
		}
		return instance;
	}

	public GeneticModel createRandomModel(File modelSpecFile) {
		GeneticModel model = null;
		try {
			FileInputStream input = new FileInputStream(modelSpecFile);
			SAXBuilder builder = new SAXBuilder();
			Document doc = builder.build(input);
			ProblemTypeSpecification specs = 
				processElements(doc.getRootElement().getChildren());
			model = createRandomModel(specs);
		} catch (IOException e1) {
			System.err.println(e1.getMessage());
		} catch (JDOMException e2) {
			System.err.println(e2.getMessage());
		}
//		System.out.println(model);
		return model;
	}

	public GeneticModel readModelFromFile(File workFile) {
		return null;
	}

	public GeneticModel createTestModel() {
		GeneticModel model = new GeneticModel(true);
		try {
			model.addFirstAutosomalGeneModel(new TwoAlleleSimpleDominanceGeneModel());
			model.addNextAutosomalGeneModel(0.2f, new TwoAlleleSimpleDominanceGeneModel());
		} catch (GeneticsException e) {
			e.printStackTrace();
		}
		return model;
	}

	private ProblemTypeSpecification processElements(List<Element> elements) {
		ProblemTypeSpecification problemSpec = new ProblemTypeSpecification();

		Iterator<Element> it = elements.iterator();
		while(it.hasNext()) {
			Element current = it.next();
			String name = current.getName();
			if (name.equals("ZZ_ZW")) 
				problemSpec.setChZZ_ZW(
						Float.parseFloat(current.getTextTrim()));

			if (name.equals("Gene1_SexLinked"))
				problemSpec.setGene1_chSexLinked(
						Float.parseFloat(current.getTextTrim()));
			if (name.equals("Gene1_3Alleles"))
				problemSpec.setGene1_ch3Alleles(
						Float.parseFloat(current.getTextTrim()));
			if (name.equals("Gene1_IncDom"))
				problemSpec.setGene1_chIncDom(
						Float.parseFloat(current.getTextTrim()));

			if (name.equals("Gene2_Present"))
				problemSpec.setGene2_chPresent(
						Float.parseFloat(current.getTextTrim()));
			if (name.equals("Gene2_SameChrAsGene1"))
				problemSpec.setGene2_chSameChrAsGene1(
						Float.parseFloat(current.getTextTrim()));
			if (name.equals("Gene2_MinRfToGene1"))
				problemSpec.setGene2_minRfToGene1(
						Float.parseFloat(current.getTextTrim()));
			if (name.equals("Gene2_MaxRfToGene1"))
				problemSpec.setGene2_maxRfToGene1(
						Float.parseFloat(current.getTextTrim()));
			if (name.equals("Gene2_3Alleles"))
				problemSpec.setGene2_ch3Alleles(
						Float.parseFloat(current.getTextTrim()));
			if (name.equals("Gene2_IncDom"))
				problemSpec.setGene2_chIncDom(
						Float.parseFloat(current.getTextTrim()));

			if (name.equals("Gene3_Present"))
				problemSpec.setGene3_chPresent(
						Float.parseFloat(current.getTextTrim()));
			if (name.equals("Gene3_SameChrAsGene1"))
				problemSpec.setGene3_chSameChrAsGene1(
						Float.parseFloat(current.getTextTrim()));
			if (name.equals("Gene3_MinRfToGene1"))
				problemSpec.setGene3_minRfToGene1(
						Float.parseFloat(current.getTextTrim()));
			if (name.equals("Gene3_MaxRfToGene1"))
				problemSpec.setGene3_maxRfToGene1(
						Float.parseFloat(current.getTextTrim()));
			if (name.equals("Gene3_3Alleles"))
				problemSpec.setGene3_ch3Alleles(
						Float.parseFloat(current.getTextTrim()));
			if (name.equals("Gene3_IncDom"))
				problemSpec.setGene3_chIncDom(
						Float.parseFloat(current.getTextTrim()));
		}
		return problemSpec;
	}

	private GeneticModel createRandomModel(ProblemTypeSpecification specs) {
		GeneticModel model = null;

		boolean gene1SexLinked = false;

		// first, the global sex-determination mechanism
		//  even if no sex-linked genes
		if (r.nextFloat() < specs.getChZZ_ZW()) {
			model = new GeneticModel(false);
		} else {
			model = new GeneticModel(true);
		}

		try {
			//first gene (always must be one)
			if (r.nextFloat() < specs.getGene1_chSexLinked()) {
				model.addFirstSexLinkedGeneModel(getRandomGeneModel(
						specs.getGene2_ch3Alleles(),
						specs.getGene1_chIncDom()));
				gene1SexLinked = true;
			} else {
				model.addFirstAutosomalGeneModel(getRandomGeneModel(
						specs.getGene1_ch3Alleles(),
						specs.getGene1_chIncDom()));
			}

			// second gene (may be one)
			if (r.nextFloat() < specs.getGene2_chPresent()) {
				GeneModel gene2Model = getRandomGeneModel(
						specs.getGene2_ch3Alleles(), 
						specs.getGene2_chIncDom());
				addGeneModelRandomly(
						model, 
						gene1SexLinked, 
						specs.getGene2_chSameChrAsGene1(), 
						specs.getGene2_minRfToGene1(), 
						specs.getGene2_maxRfToGene1(), 
						gene2Model);
			} else {
				// no second gene (therefore no third)
				return model;
			}
			
			//third gene (may be one)
			if (r.nextFloat() < specs.getGene3_chPresent()) {
				GeneModel gene3Model = getRandomGeneModel(
						specs.getGene3_ch3Alleles(), 
						specs.getGene3_chIncDom());
				addGeneModelRandomly(
						model, 
						gene1SexLinked, 
						specs.getGene3_chSameChrAsGene1(), 
						specs.getGene3_minRfToGene1(), 
						specs.getGene3_maxRfToGene1(), 
						gene3Model);
			} else {
				// no third gene (therefore no third)
				return model;
			}
			

		} catch (GeneticsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return model;
	}

	private GeneModel getRandomGeneModel(float ch3Alleles, float chIncDom) {

		GeneModel geneModel = null;

		boolean threeAlleles = false;
		if (r.nextFloat() < ch3Alleles) threeAlleles = true;

		if (r.nextFloat() < chIncDom) {
			// inc dom
			if (threeAlleles) {
				geneModel = new ThreeAlleleIncompleteDominanceGeneModel();
			} else {
				geneModel = new TwoAlleleIncompleteDominanceGeneModel();
			}

		} else {
			// simple dom
			if (threeAlleles) {
				// random choice 50/50 for circ or hierarch dom
				if (r.nextBoolean()) {
					geneModel = new ThreeAlleleHierarchicalDominanceGeneModel();
				} else {
					geneModel = new ThreeAlleleCircularDominanceGeneModel();
				}				
			} else {
				geneModel = new TwoAlleleSimpleDominanceGeneModel();
			}
		}
		return geneModel;
	}

	// adds to GeneticModel - modifies it
	private void addGeneModelRandomly(GeneticModel model,
			boolean prevGeneSexLinked,
			float chSameChromo, 
			float minRf, 
			float maxRf,
			GeneModel geneModel) throws GeneticsException{

		boolean sameChr = false;
		float rf = (r.nextFloat() * (maxRf - minRf)) + minRf;

		if (r.nextFloat() < chSameChromo) sameChr = true;

		if (prevGeneSexLinked) {
			if (sameChr) {
				// add to sex-chromosome
				// we know that there's already at least one
				//  gene on the sex-chromo so addNext
				model.addNextSexLinkedGeneModel(rf, geneModel);
			} else {
				// add to autosome
				//there may be an autosomal model or not
				// so check
				if (model.anyTraitsOnAutosome()) {
					model.addNextAutosomalGeneModel(rf, geneModel);
				} else {
					model.addFirstAutosomalGeneModel(geneModel);
				}
			}
		} else {
			//prev was autosomal
			if (sameChr) {
				//add to autosome
				// since sameChr, we know there's already
				//  at least one gene there already
				model.addNextAutosomalGeneModel(rf, geneModel);
			} else {
				// add to sex-chromosome
				//  there may or may not be genes there already
				//  so check
				if (model.anyTraitsOnSexChromosome()) {
					model.addNextSexLinkedGeneModel(rf, geneModel);
				} else {
					model.addFirstSexLinkedGeneModel(geneModel);
				}
			}
		}
	}
}
