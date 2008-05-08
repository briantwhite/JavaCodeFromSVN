package GeneticModels;

import java.util.ArrayList;

public class CharacterBank {
	
	private ArrayList<Character> allCharacters;
	private ArrayList<String> allCharacterNames;
	private ArrayList<String> allColorTraitNames;
	private ArrayList<String> allShapeTraitNames;
	private ArrayList<String> allNumberTraitNames;

	private static CharacterBank instance;
	
	private CharacterBank() {
	}
	
	public static CharacterBank getInstance() {
		if (instance == null) {
			instance = new CharacterBank();
		}
		return instance;
	}
	

}
