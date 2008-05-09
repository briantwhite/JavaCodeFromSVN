package GeneticModels;

import java.util.ArrayList;
import java.util.Random;

public class CharacterSpecificationBank {

	private ArrayList<CharacterSpecification> allCharSpecs;


	private static CharacterSpecificationBank instance;

	private CharacterSpecificationBank() {	
		refreshAll();		
	}

	public static CharacterSpecificationBank getInstance() {
		if (instance == null) {
			instance = new CharacterSpecificationBank();
		}
		return instance;
	}

	public TraitSet getRandomTraitSet() {
		TraitSet ts = null;
		// keep looking until you get a real one
		while(ts == null) {
			Random r = new Random();
			CharacterSpecification cs = 
				allCharSpecs.get(r.nextInt(allCharSpecs.size()));
			ts = cs.getRandomTraitSet();
		}
		return ts;
	}

	public void refreshAll() {
		//build the bank of possible characters
		allCharSpecs = new ArrayList();

		//add in the body-related characters
		// only color and shape allowed (number doesn't make sense)
		CharacterSpecification bodyCharSpecs = new CharacterSpecification();
		bodyCharSpecs.add(new ColorTraitSet("Body"));
		bodyCharSpecs.add(new ShapeTraitSet("Body"));
		allCharSpecs.add(bodyCharSpecs);

		//add in the eye-related characters
		//  only color allowed
		CharacterSpecification eyeCharSpecs = new CharacterSpecification();
		eyeCharSpecs.add(new ColorTraitSet("Eye"));
		allCharSpecs.add(eyeCharSpecs);

		//add in the antenna-related characters
		// all are possible
		CharacterSpecification antennaCharSpecs = new CharacterSpecification();
		antennaCharSpecs.add(new ColorTraitSet("Antenna"));
		antennaCharSpecs.add(new ShapeTraitSet("Antenna"));
		antennaCharSpecs.add(new NumberTraitSet("Antenna"));
		allCharSpecs.add(antennaCharSpecs);

		//add in the leg-related characters
		// all are possible
		CharacterSpecification legCharSpecs = new CharacterSpecification();
		legCharSpecs.add(new ColorTraitSet("Leg"));
		legCharSpecs.add(new ShapeTraitSet("Leg"));
		legCharSpecs.add(new NumberTraitSet("Leg"));
		allCharSpecs.add(legCharSpecs);
	}

}
