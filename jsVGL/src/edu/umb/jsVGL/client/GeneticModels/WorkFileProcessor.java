package edu.umb.jsVGL.client.GeneticModels;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.NodeList;

import edu.umb.jsVGL.client.VGL.EdXServerStrings;
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
 * @version 1.0 $Id: WorkFileProcessor.java,v 1.9 2008-10-25 18:55:20 brian Exp $
 */

/**
 * class to read in work files and build a GeneticModel
 * and a set of populated cages
 * @author brian
 *
 */
public class WorkFileProcessor {

	private String problemFileName;
	private GeneticModel geneticModel;
	private ArrayList<Cage> cages;
	private Element modelBuilderState;
	private EdXServerStrings edXServerStrings;

	public WorkFileProcessor(NodeList elements) {
		problemFileName = "";
		edXServerStrings = null; // not always present
		for (int i = 0; i < elements.getLength(); i++) {
			Element current = (Element)elements.item(i);
			String name = current.getTagName();
			try {
				if (name.equals("ProbFileName")) problemFileName = current.getFirstChild().getNodeValue();
				if (name.equals("GeneticModel")) geneticModel = processSavedModelInfo(current);
				if (name.equals("Organisms")) cages = processSavedCages(current);
				if (name.equals("ModelBuilderState")) modelBuilderState = current;
				if (name.equals("edXServerStrings")) edXServerStrings = processEdXServerStrings(current);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public Element getModelBuilderState() {
		return modelBuilderState;
	}

	public GeneticModel getGeneticModel() {
		return geneticModel;
	}

	public ArrayList<Cage> getCages() {
		return cages;
	}
	
	public EdXServerStrings getEdXServerStrings() {
		return edXServerStrings;
	}

	private GeneticModel processSavedModelInfo(Element e) throws Exception {

		GeneticModel model = 
			new GeneticModel(Boolean.parseBoolean(e.getAttribute("XX_XYSexDetermination")));
		
		model.setProblemFileName(problemFileName);

		model.setMinOffspring(Integer.parseInt(e.getAttribute("MinOffspring")));
		model.setMaxOffspring(Integer.parseInt(e.getAttribute("MaxOffspring")));

		//get the tags inside the "Model" tag
		if (e.getAttribute("BeginnerMode") != null) {
			model.setBeginnerMode(Boolean.parseBoolean(e.getAttribute("BeginnerMode")));
		} 
		int numberOfCharacters = Integer.parseInt(e.getAttribute("NumberOfCharacters"));

		int numberOfGeneModels = Integer.parseInt(e.getAttribute("NumberOfGeneModels"));
		

		TraitFactory.getInstance().initializeTraitBank(2, numberOfGeneModels, 6);
		CharacterSpecificationBank.getInstance().refreshAll();

		// now the rest
		NodeList parts = e.getChildNodes();
		for (int i = 0; i < parts.getLength(); i++) {
			Element current = (Element)parts.item(i);
			String name = current.getTagName();

			if (name.equals("ProblemTypeSpecification")) {
				model.setProblemTypeSPecification(
						GeneticModelFactory.getInstance().processModelSpecElements(
								current.getChildNodes()));
			}
			
			if (name.equals("CharacterOrderScrambler")) {
				int[] scrambler = new int[numberOfCharacters];
				NodeList csItems = current.getChildNodes();
				for (int j = 0; j < csItems.getLength(); j++) {
					Element te = (Element)csItems.item(j);
					scrambler[Integer.parseInt(te.getAttribute("Index"))] = 
						Integer.parseInt(te.getFirstChild().getNodeValue());
				}
				model.setScrambledCharacterOrder(scrambler);

			} else if (name.equals("ChromosomeModel")) {
				boolean sexChromosome = Boolean.parseBoolean(current.getAttribute("SexChromosome"));
				processChromosomeModelInfo(model, sexChromosome, current);
			} else if (name.equals("PhenotypeProcessor")) 
				model.getPhenoTypeProcessor().load(current);
		}
		return model;
	}

	private void processChromosomeModelInfo(
			GeneticModel model, 
			Boolean sexChromosome, 
			Element e) throws Exception {

		int chromoNumber = 1;
		if (sexChromosome) chromoNumber = 0;

		// set up the geneModels first
		Iterator<Element> gmIt = e.getChildren().iterator();
		int i = 0;
		float rf = -1.0f;
		while (gmIt.hasNext()) {
			Element gmEl = gmIt.next();
			rf = gmEl.getAttribute("RfToPrevious").getFloatValue();
			GeneModel geneModel = buildGeneModel(gmEl, chromoNumber, i);

			if (sexChromosome) {
				if (i == 0) {
					model.addFirstSexLinkedGeneModel(geneModel);
				} else {
					model.addNextSexLinkedGeneModel(rf, geneModel);
				}
			} else {
				if (i == 0) {
					model.addFirstAutosomalGeneModel(geneModel);
				} else {
					model.addNextAutosomalGeneModel(rf, geneModel);
				}
			}

			i++;
		}
	}

	private GeneModel buildGeneModel(Element e, int chromo, int gene) {
		String type = e.getAttributeValue("Type");

		List<Element> traitList = e.getChildren();
		if (type.equals("TwoAlleleSimpleDominance")) {
			return new TwoAlleleSimpleDominanceGeneModel(
					traitList, chromo, gene);
		} else if(type.equals("TwoAlleleIncompleteDominance")) {
			return new TwoAlleleIncompleteDominanceGeneModel(
					traitList, chromo, gene);
		} else if(type.equals("ThreeAlleleHierarchicalDominance")) {
			return new ThreeAlleleHierarchicalDominanceGeneModel(
					traitList, chromo, gene);
		} else if(type.equals("ThreeAlleleCircularDominance")) {
			return new ThreeAlleleCircularDominanceGeneModel(
					traitList, chromo, gene);			
		} else if(type.equals("ThreeAlleleIncompleteDominance")) {
			return new ThreeAlleleIncompleteDominanceGeneModel(
					traitList, chromo, gene);						
		} else if (type.equals("Interacting")){
			return new InteractingGeneModel(traitList, chromo, gene);
		} else {
			return null;
		}
	}

	private ArrayList<Cage> processSavedCages(Element e) {
		ArrayList<Cage> cages = new ArrayList<Cage>();
		Iterator<Element> cageIt = e.getChildren().iterator();
		while (cageIt.hasNext()) {
			Element cageE = cageIt.next();
			if(cageE.getName().equals("Cage")) {
				Cage c = buildCage(cageE);
				cages.add(c);
			}
		}
		return cages;
	}

	private Cage buildCage(Element e) {
		int cageId;
		try {
			cageId = e.getAttribute("Id").getIntValue();
			int numChildren = e.getAttribute("NumChildren").getIntValue();

			Cage cage = new Cage(cageId);

			/*
			 * see if it's an old style cage (version 2.1.1 & earlier)
			 * in that case, we didn't save the position, superness, and visibility
			 * so need to fill them in
			 */
			if (e.getAttribute("Xpos") != null) {
				cage.setXpos(e.getAttribute("Xpos").getIntValue());
				cage.setYpos(e.getAttribute("Ypos").getIntValue());
				cage.setVisible(e.getAttribute("Visible").getBooleanValue());
				cage.setSuper(e.getAttribute("SuperCross").getBooleanValue());
			} else {
				cage.setXpos(-1);
				cage.setYpos(-1);
				cage.setVisible(true);
				cage.setSuper(false);
			}
			Iterator<Element> contentsIt = e.getChildren().iterator();
			while (contentsIt.hasNext()) {
				Element item = contentsIt.next();
				if (item.getName().equals("Parents")) {
					Iterator<Element> parentIt = item.getChildren().iterator();
					Element p1E = parentIt.next();
					Organism p1 = 
						OrganismFactory.buildOrganism(
								p1E, 
								p1E.getAttribute("CageId").getIntValue(),
								geneticModel);
					Element p2E = parentIt.next();
					Organism p2 = 
						OrganismFactory.buildOrganism(
								p2E,
								p2E.getAttribute("CageId").getIntValue(),
								geneticModel);
					cage.setParents(p1, p2);
				} else if(item.getName().equals("Children")) {
					Organism[] childrenInOrder = new Organism[numChildren];
					Iterator<Element> childIt = item.getChildren().iterator();
					while (childIt.hasNext()) {
						Element childE = childIt.next();
						if (childE.getName().equals("Organism")) {
							int index = childE.getAttribute("Id").getIntValue();
							childrenInOrder[index] = 
								OrganismFactory.buildOrganism(
										childE, cageId, geneticModel);
						}
					}

					for (int i = 0; i < numChildren; i++) {
						cage.addSaved(childrenInOrder[i]);
					}
				}
			}
			return cage;
		} catch (DataConversionException e1) {
			e1.printStackTrace();
			return null;
		}
	}
	
	private EdXServerStrings processEdXServerStrings(Element e) {
		EdXServerStrings result = new EdXServerStrings();
		result.setEdXCookieURL(e.getAttributeValue("edXCookieURL"));
		result.setEdXLoginURL(e.getAttributeValue("edXLoginURL"));
		result.setEdXSubmissionURL(e.getAttributeValue("edXSubmissionURL"));
		result.setEdXLocation(e.getAttributeValue("edXLocation"));
		return result;
	}

}
