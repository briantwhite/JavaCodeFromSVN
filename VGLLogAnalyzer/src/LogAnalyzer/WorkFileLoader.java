package LogAnalyzer;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.TreeMap;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import VGLGeneticModels.Genetics;
import VGLGeneticModels.Model;
import VGLGeneticModels.OList;
import VGLGeneticModels.Organism;
import VGLIIGeneticModels.GeneticModelFactory;
import VGLIIGeneticModels.OrganismList;

/*
 * reads and processes one work file
 * 
 */
public class WorkFileLoader implements Runnable {

	private Object[][] data;

	private ArrayList<String> workFileNames;

	private boolean keepGoing;

	private int progress;

	private Model model;

	private RequiredResultSet requiredResults;

	public WorkFileLoader(ArrayList<String> workFileNames) {
		data = new Object[workFileNames.size()][VGLLogAnalyzer.columnHeadings.length];
		this.workFileNames = workFileNames;
		keepGoing = true;
		progress = 0;
	}

	public void run() {
		for (int i = 0; i < workFileNames.size(); i++) {
			if (!keepGoing) return;
			String fileName = (workFileNames.get(i)).toString();
			File workFile = new File(fileName);
			if (fileName.endsWith(".wrk")) {
				data[i] = processWorkFile(workFile);
			}
			if (fileName.endsWith(".wr2")) {
				data[i] = processWorkFile(workFile);
			}
			progress++;
		}
	}

	public void stop() {
		keepGoing = false;
	}

	public int getLengthOfTask() {
		return workFileNames.size();
	}

	public int getProgress() {
		return progress;
	}

	private Object[] processWorkFile(File workFile) {
		if (VGLLogAnalyzer.debug) System.out.println(workFile.getName());
		Object[] result = new Object[VGLLogAnalyzer.columnHeadings.length];

		result[0] = workFile.getName();

		ArrayList<ClassifiedCross> crosses = new ArrayList<ClassifiedCross>();

		result[1] = new String("???");
		if (workFile.getName().endsWith(".wrk")) {
			result[1] = new String("VGL");
			crosses = classifyVGLCrosses(workFile);
		} else if (workFile.getName().endsWith(".wr2")) {
			result[1] = new String("VGLII");
			crosses = classifyVGLIICrosses(workFile);
		} else {
			return result;
		}

		BufferedImage img = null;
		StringBuffer buf = null;
		if (crosses.size() > 0) {

			img = new BufferedImage((12 * crosses.size()), 10, BufferedImage.TYPE_INT_RGB);
			Graphics g = img.getGraphics();
			g.setColor(Color.DARK_GRAY);
			g.fillRect(0, 0, (12 * crosses.size()), 10);

			/*
			 * make a specific character for each type of cross
			 * bits 0,1 = cross type 
			 *     0 = field
			 *     1 = field X other
			 *     2 = sib
			 *     3 = other
			 * bit 2 (4's place) = diffPhenos(1) or samePhenos(0)
			 * bit 3 (8's place) = showsDominance
			 * bit 5 (16's place) = shows sex-linkage
			 * bit 6 (32's place) = have all info
			 * start at 48 ("A") to make the chars more attractive
			 */
			buf = new StringBuffer();

			Iterator<ClassifiedCross> crossIt = crosses.iterator();
			int x = 0;
			while (crossIt.hasNext()) {

				int code = 48; // code described above

				ClassifiedCross cross = crossIt.next();
				int type = cross.getType();
				int mod = cross.getModifier();
				switch (type) {
				case VGLLogAnalyzer.FIELD_POP_CROSS:
					g.setColor(Color.RED);
					code = code + 0;
					break;
				case VGLLogAnalyzer.FIELD_CROSS_OTHER:
					g.setColor(Color.GREEN);
					code = code + 1;
					break;
				case VGLLogAnalyzer.SIBLING_CROSS:
					g.setColor(Color.BLUE);
					code = code + 2;
					break;
				case VGLLogAnalyzer.OTHER_CROSS:
					g.setColor(Color.cyan);
					code = code + 3;
					break;
				}
				g.fillRect(x, 0, 10, 10);

				if (mod == VGLLogAnalyzer.DIFFERENT_PHENOTYPES) {
					g.setColor(Color.BLACK);
					g.fillRect(x, 0, 3, 3);
					code = code + 4;
				}

				if (cross.showsDominance() || cross.showsSexLinkage()) {
					if (cross.showsDominance()) {
						g.setColor(Color.WHITE);
						code = code + 8;
					}
					if (cross.showsSexLinkage()) {
						g.setColor(Color.BLACK);
						code = code + 16;
					}
					if (cross.showsDominance() && cross.showsSexLinkage()) {
						g.setColor(Color.GRAY);
					}
					g.fillRect(x + 7, 7, 3, 3);
				}

				if (cross.isHaveAllInfo()) {
					g.setColor(Color.WHITE);
					g.fillRect(x + 3, 3, 3, 3);
					code = code + 32;
				}

				x = x + 12;

				buf.append((char)code);
			}
			g.dispose();
		} else {
			img = new BufferedImage(50, 10, BufferedImage.TYPE_INT_RGB);
			buf = new StringBuffer();
			Graphics g = img.getGraphics();
			g.setColor(Color.gray);
			g.fillRect(0, 0, 50, 10);
			g.dispose();
			buf.append("-");
		}
		result[4] = new ImageIcon(img);

		//		StringBuffer buf = new StringBuffer();
		//		while (crossIt.hasNext()) {
		//			ClassifiedCross cross = crossIt.next();
		//			buf.append(cross.toString() + ",");
		//		}
		//		if (buf.length() != 0) buf.deleteCharAt(buf.length() - 1);
		//		result[2] = buf.toString();

		CrossTally tally = tallyCrosses(crosses);
		result[2] = new Integer(tally.getModelNumber());
		result[3] = buf.toString();
		result[5] = new Integer(tally.getTotal());
		result[6] = new Integer(tally.getFieldXfield());
		result[7] = new Integer(tally.getFieldXother());
		result[8] = new Integer(tally.getSibling());
		result[9] = new Integer(tally.getOther());
		result[10] = new Integer(tally.getSamePheno());
		result[11] = new Integer(tally.getDifferentPheno());
		result[12] = new Integer(tally.getExtraCrosses());
		result[13] = new Integer(tally.getShowsSexLinkage());
		result[14] = new Integer(tally.getShowsDominance());

		return result;
	}

	private ArrayList<ClassifiedCross> classifyVGLCrosses(File workFile) {
		Genetics genetics = new Genetics();
		ArrayList<VGLGeneticModels.Cage> cages = null;
		try {
			cages = genetics.open(workFile);
			model = genetics.getModel();
		} catch (Exception e) {
			e.printStackTrace();
		}

		if ((cages == null) || (model == null)) {
			JOptionPane.showMessageDialog(null, 
					"Could not process work file: " 
					+ workFile.getName() 
					+ " , perhaps it is in the wrong format.");
			return null;
		}

		//this is the set of all results required to prove the entire model
		requiredResults = model.getRequiredResultSet();

		ArrayList<ClassifiedCross> result = new ArrayList<ClassifiedCross>();
		for (int i = 0; i < cages.size(); i++) {
			VGLGeneticModels.Cage cage = cages.get(i);
			if (cage.getId() != 0) {
				result.add(processCage(cage));
			}
		}
		return result;
	}

	private ArrayList<ClassifiedCross> classifyVGLIICrosses(File workFile) {
		ArrayList<VGLIIGeneticModels.Cage> cages = 
			(GeneticModelFactory.getInstance().readModelFromFile(workFile)).getCages();

		ArrayList<ClassifiedCross> result = new ArrayList<ClassifiedCross>();
		for (int i = 0; i < cages.size(); i++) {
			VGLIIGeneticModels.Cage cage = cages.get(i);
			if (cage.getId() != 0) {
				result.add(processCage(convertVGLIICageToVGLCage(cage)));
			}
		}
		return result;
	}

	private VGLGeneticModels.Cage convertVGLIICageToVGLCage(VGLIIGeneticModels.Cage vglIICage) {
		ArrayList<VGLIIGeneticModels.Organism> parents = vglIICage.getParents();
		VGLGeneticModels.Cage newCage = 
			new VGLGeneticModels.Cage(
					vglIICage.getId(),
					convertVGLIIOrganismToVGLOrganism(parents.get(0)),
					convertVGLIIOrganismToVGLOrganism(parents.get(1)));
		TreeMap<String, OrganismList> children = vglIICage.getChildren();
		Iterator<String> phenoIt = children.keySet().iterator();
		while (phenoIt.hasNext()) {
			String pheno = phenoIt.next();
			OrganismList organisms = children.get(pheno);
			Iterator<VGLIIGeneticModels.Organism> orgIt = organisms.iterator();
			while (orgIt.hasNext()) {
				newCage.addOrganism(convertVGLIIOrganismToVGLOrganism(orgIt.next()), pheno);
			}
		}
		return newCage;
	}

	private VGLGeneticModels.Organism convertVGLIIOrganismToVGLOrganism(
			VGLIIGeneticModels.Organism vglIIOrganism) {

		int sex = 1;
		if (vglIIOrganism.isMale()) sex = 0;

		return new VGLGeneticModels.Organism(
				vglIIOrganism.getCageId(),			//cage number
				sex,								// 0 = male; 1= female
				"",									// should be allele1, not present in VGLII
				"",									// should be allele2, not present in VGLII
				vglIIOrganism.getPhenotypeString(),
				0);
	}

	private ClassifiedCross processCage(VGLGeneticModels.Cage cage) {

		/*
		 * flags for whether this cross is conspicuous or not
		 * it can be conspicuous to show dominance between alleles
		 * or to show sex-linkage (or lack of that)
		 */
		boolean showsSexLinkage = false;
		boolean showsDominance = false;

		if (VGLLogAnalyzer.debug) System.out.println("cage " + cage.getId());
		ArrayList<Organism> parents = cage.getParents();
		int p1CageId = parents.get(0).getCageId();
		int p2CageId = parents.get(1).getCageId();
		String p1Pheno = parents.get(0).getPhenotype();
		String p2Pheno = parents.get(1).getPhenotype();
		HashMap<String, OList> offspring = cage.getChildren();

		// read in the offspring and find males and females
		//   of each pheno
		HashSet<String> sonPhenotypes = new HashSet<String>();
		HashSet<String> daughterPhenotypes = new HashSet<String>();
		HashSet<String>	allPhenotypes = new HashSet<String>();
		Iterator<String> phenoIt = offspring.keySet().iterator();
		while (phenoIt.hasNext()) {
			String pheno = phenoIt.next();
			OList olist = offspring.get(pheno);
			for (int i = 0; i < olist.size(); i++) {
				Organism o = olist.get(i);
				if (o.getSexType() == 0) {
					sonPhenotypes.add(pheno);
				} else {
					daughterPhenotypes.add(pheno);
				}
				allPhenotypes.add(pheno);
			}
		}


		// look for conspicuous crosses
		ArrayList<String> foundNewPhenos = new ArrayList<String>();	// if a new phenotype appears in the offspring
		boolean foundP1Pheno = false; //   see if parent phenos present in offspring
		boolean foundP2Pheno = false; //    other half

		// see if you've found the heterozygote phenotype in either males or females
		//  this is needed to rule out sex-linkage in incomplete dominance
		String hetPheno = null;
		if (requiredResults.getHeterozygotePhenotype() != null) {
			hetPheno = requiredResults.getHeterozygotePhenotype();

			if (p1Pheno.equals(hetPheno) || sonPhenotypes.contains(hetPheno)) {
				requiredResults.processResult(new FoundHeterozygotePhenotypeResult(true));
				showsSexLinkage = true;
			}

			if (p2Pheno.equals(hetPheno) || daughterPhenotypes.contains(hetPheno)) {
				requiredResults.processResult(new FoundHeterozygotePhenotypeResult(false));
				showsSexLinkage = true;
			}
		}

		//process all phenotypes involved
		Iterator<String> allPhenoIt = allPhenotypes.iterator();
		while (allPhenoIt.hasNext()) {
			String pheno = allPhenoIt.next();

			// see if pheno is not present in parents
			if ((!pheno.equals(p1Pheno) && (!pheno.equals(p2Pheno)))) {
				foundNewPhenos.add(pheno);
			}
			if (p1Pheno.equals(pheno)) foundP1Pheno = true;
			if (p2Pheno.equals(pheno)) foundP2Pheno = true;
		}

		// characterize the information gained

		if (!(foundP1Pheno && foundP2Pheno)) {
			// one parent pheno is missing A X B -> A but no B
			//  so, we know the dominance relationship between A and B
			requiredResults.processResult(new DominanceInteractionResult(
					new ArrayList<String>(Arrays.asList(p1Pheno,p2Pheno))));
			showsDominance = true;
			if (VGLLogAnalyzer.debug) System.out.println(
					"missing; "+ p1Pheno + " " + p2Pheno + " " + requiredResults.isHaveAllInfo());
		}

		// a new pheno appears in offspring
		if (foundNewPhenos.size() > 0) {
			Iterator<String> npIt = foundNewPhenos.iterator();
			while (npIt.hasNext()) {
				String foundPheno = npIt.next();
				if (p1Pheno.equals(p2Pheno)) {
					// A X A -> B (maybe with others)
					//  thererfore, get dom relationship between A and B
					requiredResults.processResult(new DominanceInteractionResult(
							new ArrayList<String>(Arrays.asList(p1Pheno,foundPheno))));
					showsDominance = true;
					if (VGLLogAnalyzer.debug) System.out.println(
							"A x A -> B; "
							+ p1Pheno + " " + p2Pheno + " " + foundPheno + " " 
							+ requiredResults.isHaveAllInfo());

				} else {
					// A X B -> C (maybe with others)
					if(foundP1Pheno || foundP2Pheno) {
						// A X B -> C with A or B
						//  this is a classic hierarc/circ dom result
						//   that gives LINKED info:
						//    you know dom relationship between (A and C) OR (B and C)
						if (VGLLogAnalyzer.debug) System.out.println(
								"A x B -> C +; "
								+ p1Pheno + " " + p2Pheno + " " + foundPheno + " " 
								+ requiredResults.isHaveAllInfo());
						requiredResults.processResult(new LinkedGeneticResult(
								new DominanceInteractionResult(
										new ArrayList<String>(Arrays.asList(p1Pheno,foundPheno))),
										new DominanceInteractionResult(
												new ArrayList<String>(Arrays.asList(p2Pheno,foundPheno)))));
						showsDominance = true;
					} else {
						// A X B -> C only 
						//  this is a classic inc dom result
						requiredResults.processResult(new DominanceInteractionResult(
								new ArrayList<String>(Arrays.asList(p1Pheno,foundPheno))));
						requiredResults.processResult(new DominanceInteractionResult(
								new ArrayList<String>(Arrays.asList(p2Pheno,foundPheno))));
						showsDominance = true;
						if (VGLLogAnalyzer.debug) System.out.println(
								"A x B -> C only; "
								+ p1Pheno + " " + p2Pheno + " " + foundPheno + " " 
								+ requiredResults.isHaveAllInfo());
					}
				}
			}
		}		

		/*
		 * look for obvious signs of sex-linkage
		 *    = a phenotype found in one sex but not the other
		 *      this gives you a linked result:
		 *        if you know which is dom, then you know XX/XY or ZZ/ZW
		 *        and vice-versa
		 */
		ArrayList<String> phenosOnlyInSons = new ArrayList<String>();
		ArrayList<String> phenosOnlyInDaughters = new ArrayList<String>();
		Iterator<String> sonPhenoIt = sonPhenotypes.iterator();
		while (sonPhenoIt.hasNext()) {
			String sonPheno = sonPhenoIt.next();
			if (!daughterPhenotypes.contains(sonPheno)) {
				showsSexLinkage = true;
				phenosOnlyInSons.add(sonPheno);
				if (VGLLogAnalyzer.debug) System.out.println(
						"sex linked: " + sonPheno + " not found in daughters" + " " 
						+ requiredResults.isHaveAllInfo());
			}
		}
		Iterator<String> daughterPhenoIt = daughterPhenotypes.iterator();
		while (daughterPhenoIt.hasNext()) {
			String daughterPheno = daughterPhenoIt.next();
			if(!sonPhenotypes.contains(daughterPheno)) {
				showsSexLinkage = true;
				phenosOnlyInDaughters.add(daughterPheno);
				if (VGLLogAnalyzer.debug) System.out.println(
						"sex linked: " + daughterPheno + " not found in sons" + " " 
						+ requiredResults.isHaveAllInfo());				
			}
		}

		if (showsSexLinkage) {
			/*
			 * if there is sex-linkage shown, then there are several possibilities
			 *   {conclusions in braces}
			 *   1) A X A -> no B females {A is dom & XX/XY}
			 *        or  -> no B males {A is dom & ZZ/ZW}
			 *   2) A male X B female -> A females & B males (criss-cross)
			 *      {it is sex-linked, but a linked result between sex-linkage type and dominance
			 *        - if you know one, you know the other}
			 *      {therefore: Linked (sex-linkage, dominance(A,B)}
			 *   3) A male X B female -> A male & female + B females + C males
			 *      {XX/XY and a linked(dom(A,C), dom(B,C))
			 *      if the new pheno (C) only appears in females, it's ZZ/ZW
			 *   4) A male X B female -> B male + C female (where C is the heterozygote)
			 *      {XX/XY, Dom(A,C), Dom(B,C)} 
			 *      ZZ/ZW if C appears in males
			 */
			if (p1Pheno.equals(p2Pheno)) {
				// case (1) - we know the sex-linkage type and dom relationship
				requiredResults.processResult(new SexLinkageResult(true));

				if ((phenosOnlyInSons.size() > 1) || (phenosOnlyInDaughters.size() > 1)) {
					if (VGLLogAnalyzer.debug) System.out.println(
							"Strange error, found more than one son-only ("
							+ phenosOnlyInSons.size() + ") or daughter-only ("
							+ phenosOnlyInDaughters.size() + ") pheno!");					
				} else {
					// one son-only or daughter-only pheno found, so we're ok 
					String newPheno = "";
					if (phenosOnlyInSons.size() == 1) {
						newPheno = phenosOnlyInSons.get(0);
					} else if (phenosOnlyInDaughters.size() == 1) {
						newPheno = phenosOnlyInDaughters.get(0);
					} else {
						if (VGLLogAnalyzer.debug) System.out.println(
						"Strange error, found both a son-only or daughter-only pheno from A x A!");											
					}

					requiredResults.processResult(
							new DominanceInteractionResult(
									new ArrayList<String>(Arrays.asList(p1Pheno, newPheno))));
					showsDominance = true;
				}

			} else {
				// p1Pheno != p2Pheno: must be A X B; therefore cases (2) and (3)
				// (2) see if criss-cross
				if ((sonPhenotypes.size() == 1) && (daughterPhenotypes.size() == 1)) {
					// only one pheno in sons and one in daughters
					String sonPhenotype = sonPhenotypes.iterator().next();
					String daughterPhenotype = daughterPhenotypes.iterator().next();
					if (p1Pheno.equals(daughterPhenotype) && p2Pheno.equals(sonPhenotype)) {
						requiredResults.processResult(
								new LinkedGeneticResult(
										new SexLinkageResult(true),
										new DominanceInteractionResult(
												new ArrayList<String>(Arrays.asList(p1Pheno,p2Pheno)))));
						showsDominance = true;
					} else if (hetPheno != null) {
						/*
						 * if it's inc dom, it could be a case (4)
						 *   first, see if the heterozygote pheno appears in one sex
						 *   of the offspring only
						 *   - if so, you know it all
						 */
						if (sonPhenotype.equals(hetPheno) || daughterPhenotype.equals(hetPheno)){
							requiredResults.processResult(new SexLinkageResult(true));
							requiredResults.processResult(new DominanceInteractionResult(
									new ArrayList<String>(Arrays.asList(p1Pheno,hetPheno))));
							requiredResults.processResult(new DominanceInteractionResult(
									new ArrayList<String>(Arrays.asList(p2Pheno,hetPheno))));
							showsDominance = true;
						}
					}
				} else {
					/*
					 * more than one son or daughter pheno
					 * and since one pheno is missing from sons or daughters
					 * it must be (3)
					 * so know sex-linkage and a linked dominance result with new pheno
					 */
					requiredResults.processResult(new SexLinkageResult(true));

					// find the pheno in offspring that isn't in parents
					HashSet<String> offspringPhenos = new HashSet<String>(sonPhenotypes);
					offspringPhenos.addAll(daughterPhenotypes);
					offspringPhenos.remove(p1Pheno);
					offspringPhenos.remove(p2Pheno);
					// there should be only one left
					if (offspringPhenos.size() == 1) {
						Iterator<String> opIt = offspringPhenos.iterator();
						String newPheno = opIt.next();
						requiredResults.processResult(new LinkedGeneticResult(
								new DominanceInteractionResult(
										new ArrayList<String>(Arrays.asList(p1Pheno,newPheno))),
										new DominanceInteractionResult(
												new ArrayList<String>(Arrays.asList(p2Pheno,newPheno)))));
						showsDominance = true;
					} else {
						if (VGLLogAnalyzer.debug) System.out.println(
								"Strange error, "
								+ offspringPhenos.size()
								+ " non-parental phenos found from A x B -> A, B, C!");																	
					}
				}
			}

		} else {
			/*
			 * look for evidence that there is NO sex-linkage
			 *  you get this in parts
			 *  full evidence = reciprocal crosses
			 *    male A X female B -> no sex-linkage shown (equal males & females of any pheno)
			 *    male B X female A -> ditto
			 *  parent 1 = dad
			 *  parent 2 = mom
			 *
			 * don't bother if you know it shows sex linkage
			 * if A X B shows no sex-linkage, it's a candidate
			 */
			if (!(p1Pheno.equals(p2Pheno))) {
				requiredResults.processResult(new PartialNoSexLinkageResult(p1Pheno, p2Pheno));
			}
		}

		int modifier = VGLLogAnalyzer.DIFFERENT_PHENOTYPES;
		if (parents.get(0).getPhenotype().equals(parents.get(1).getPhenotype())) {
			modifier = VGLLogAnalyzer.SAME_PHENOTYPES;
		}

		if ((p1CageId == 0) && (p2CageId == 0)) {
			return new ClassifiedCross(
					VGLLogAnalyzer.FIELD_POP_CROSS, 
					modifier,
					showsDominance,
					showsSexLinkage,
					requiredResults.isHaveAllInfo());
		}
		if ((p1CageId == 0) || (p2CageId == 0)) {
			return new ClassifiedCross(
					VGLLogAnalyzer.FIELD_CROSS_OTHER, 
					modifier,
					showsDominance,
					showsSexLinkage,
					requiredResults.isHaveAllInfo());
		}
		if (p1CageId == p2CageId) {
			return new ClassifiedCross(
					VGLLogAnalyzer.SIBLING_CROSS, 
					modifier,
					showsDominance,
					showsSexLinkage,
					requiredResults.isHaveAllInfo());
		}
		return new ClassifiedCross(
				VGLLogAnalyzer.OTHER_CROSS, 
				modifier,
				showsDominance,
				showsSexLinkage,
				requiredResults.isHaveAllInfo());
	}

	private CrossTally tallyCrosses(ArrayList<ClassifiedCross> crosses) {
		int fieldXfield = 0;
		int fieldXother = 0;
		int sibling = 0;
		int other = 0;
		int samePheno = 0;
		int differentPheno = 0;
		int showsDominance = 0;
		int showsSexLinkage = 0;
		int extraCrosses = 0;

		Iterator<ClassifiedCross> crossIt = crosses.iterator();
		while (crossIt.hasNext()) {
			ClassifiedCross cross = crossIt.next();

			int type = cross.getType();
			switch (type) {
			case VGLLogAnalyzer.FIELD_POP_CROSS:
				fieldXfield++;
				break;
			case VGLLogAnalyzer.FIELD_CROSS_OTHER:
				fieldXother++;
				break;
			case VGLLogAnalyzer.SIBLING_CROSS:
				sibling++;
				break;
			case VGLLogAnalyzer.OTHER_CROSS:
				other++;
				break;
			}

			int modifier = cross.getModifier();
			switch (modifier) {
			case VGLLogAnalyzer.SAME_PHENOTYPES:
				samePheno++;
				break;
			case VGLLogAnalyzer.DIFFERENT_PHENOTYPES:
				differentPheno++;
				break;
			}

			if (cross.showsDominance()) showsDominance++;
			if (cross.showsSexLinkage()) showsSexLinkage++;

			if (cross.isHaveAllInfo()) extraCrosses++;

		}
		return new CrossTally(
				model.getModelNo(),
				fieldXfield, 
				fieldXother, 
				sibling, 
				other, 
				samePheno, 
				differentPheno,
				showsDominance,
				showsSexLinkage,
				extraCrosses - 1);	// don't count the first as extra
	}

	public Object[][] getResult() {
		return data;
	}

}
