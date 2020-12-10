//PolypeptideFactory.java


//Copyright 2004, Ethan Bolker and Bogdan Calota
/* 
 * License Information
 * 
 * This  program  is  free  software;  you can redistribute it and/or modify it 
 * under  the  terms  of  the GNU General Public License  as  published  by the 
 * Free  Software  Foundation; either version 2  of  the  License,  or (at your 
 * option) any later version.
 */
/*
 * The following organization of a public class is recommended by X. Jia [2004:
 * Object Oriented Software Development Using Java(TM). Addison Wesley, Boston,
 * 677 pp.]
 * 
 * public class AClass { (public constants) (public constructors) (public
 * accessors) (public mutators) (nonpublic fields) (nonpublic auxiliary methods
 * or nested classes) }
 * 
 * Jia also recommends the following design guidelines.
 * 
 * 1. Avoid public fields. There should be no nonfinal public fields, except
 * when a class is final and the field is unconstrained. 2. Ensure completeness
 * of the public interface. The set of public methods defined in the class
 * should provide full and convenient access to the functionality of the class.
 * 3. Separate interface from implementation. When the functionality supported
 * by a class can be implemented in different ways, it is advisable to separate
 * the interface from the implementation.
 * 
 * Modified: 09 Mar 2005 (D. A. Portman/MGX Team UMB)
 */

package edu.umb.jsAipotu.client.biochem;

import java.util.ArrayList;
//import java.util.StringTokenizer;
import java.util.Arrays;

import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;

import edu.umb.jsAipotu.client.preferences.GlobalDefaults;

public class PolypeptideFactory {

	// accessor method(s)

	public static PolypeptideFactory getInstance() {
		if (instance == null)
			instance = new PolypeptideFactory();
		return instance;
	}

	// mutator methods

	/**
	 * Creates a polypeptide from an array of strings. NOTE: Overrides 
	 * another method.  Used for comand line where the acids come from 
	 * the args.
	 * 
	 * @param args
	 *            String of args corresponding to amino acids (no directions).
	 * @param isSolution
	 *            input represents a folded polypeptide.
	 * @param isRandom
	 *            generate a random polypeptide.
	 * @param length
	 *            length of random polypeptide.
	 * @param seed
	 *            seed of the random polypeptide.
	 */
	public Polypeptide createPolypeptide(String[] args, boolean isSolution,
			boolean isRandom, String length, String seed,
			int numAALetterCode) throws FoldingException {

		return this.createPolypeptide(arrayToString(args), isSolution,
				isRandom, length, seed);
	}

	/**
	 * Constructs a Polypeptide.
	 * 
	 * @param input
	 *            input containing amino acids, with directions or not.
	 * @param isSolution
	 *            input represents a folded polypeptide.
	 * @param isRandom
	 *            generate a random polypeptide.
	 * @param length
	 *            length of random polypeptide.
	 * @param seed
	 *            seed of the random polypeptide.
	 */
	public Polypeptide createPolypeptide(String input, boolean isFolded,
			boolean isRandom, String length, String seed) throws FoldingException {

		if (isFolded) {
			return createFromProteinString(input);
		} else if (isRandom) {
			return createRandom(length, seed);
		} else {
			return createFromAcids(input);
		}
	}

	/**
	 * Create a polypeptide from a string containing a previously folded
	 * polypeptide.
	 * 
	 * @param tableName
	 * @param input
	 * @return Polypeptide
	 * @throws FoldingException
	 */
	// 
	public Polypeptide createFromProteinString(String input) throws FoldingException {

		// parse input into strings representing an acid or a direction
		ArrayList acidString = getTokens(input);

		// parsing each acid string into AminoAcids using AminoAcidTable.
		//     or each direction string into a Direction using Direction
		// From parsing, acids are on even positions, and directions on odd
		//     positions.
		int numberOfTokens = acidString.size();
		int acidIndex = 0;
		int directionIndex = 0;

		AminoAcid[] acids = new AminoAcid[numberOfTokens / 2];
		Direction[] directions = new Direction[numberOfTokens / 2];

		for (int i = 0; i < numberOfTokens; i = i + 2) {

			// parse acid string( found on even positions)
			acids[acidIndex++] = parseAcid((String) acidString.get(i), 1);

			// parse direction string( found on odd positions)
			directions[directionIndex++] = parseDirection((String) acidString
					.get(i + 1));
		}

		// call constructor in Polypeptide
		return new Polypeptide(acids, directions);
	}

	/**
	 * 
	 * 
	 * @param tableName
	 * @param length
	 * @param seed
	 * @return Polypeptide
	 * @throws FoldingException
	 */
	public Polypeptide createRandom( String length, String seed) throws FoldingException {

		int len = 0;
		int s = -1;
		try {
			len = Integer.parseInt(length);
		} catch (NumberFormatException e) {
			throw new IntegerFormatFoldingException(
					"Length: REQUIRED: integer GIVEN: " + length);
		}

		try {
			s = Integer.parseInt(seed);
		} catch (NumberFormatException e) {
			throw new IntegerFormatFoldingException(
					"Seed:  REQUIRED: integer GIVEN: " + seed);
		}

		if (len < 1)
			throw new IntegerFormatFoldingException(
					"Length: REQUIRED: > 0 GIVEN: " + length);
		if (s < 0)
			throw new IntegerFormatFoldingException(
					"Seed: REQUIRED:  >=0 GIVEN: " + seed);

		return new Polypeptide(len, s);
	}

	/**
	 * 
	 * @param tableName
	 * @param input
	 * @return Polypeptide
	 * @throws FoldingException
	 */
	public Polypeptide createFromAcids(String input) throws FoldingException {
		AminoAcid[] acids = parseInputStringToAmAcArray(input);
		return new Polypeptide(acids);
	}

	public AminoAcid[] parseInputStringToAmAcArray(String input) throws FoldingException {

		AminoAcid[] acids;

		//figure out what kind of protein sequence it is
		int numAALetterCode = 1;

		RegExp re = RegExp.compile("[a-z]{1,}");
		MatchResult m = re.exec(input);
		if (m != null) {
			numAALetterCode = 3;
		}

		// take out any labels and other useless stuff
		input = input.replaceAll(" ", "");
		input = input.replaceAll("N-", "");
		input = input.replaceAll("-C", "");


		switch (numAALetterCode) {
		case 1:
			char[] letters = input.toCharArray();
			acids = new AminoAcid[letters.length];
			for (int i = 0; i < letters.length; i++) {
				acids[i] = parseAcid(String.valueOf(letters[i]), numAALetterCode);
			}
			break;
		case 3:
			//insert spaces between amino acid codes
			StringBuffer psBuffer = new StringBuffer(input);
			for (int i = 3; i < psBuffer.length(); i = i + 4) {
				psBuffer = psBuffer.insert(i, " ");
			}
			input = psBuffer.toString();

			// parse input into strings, each representing an acid
			ArrayList acidString = getTokens(input);

			// parsing each acid string into AminoAcids using the AminoAcidTable.
			acids = new AminoAcid[acidString.size()];
			for (int i = 0; i < acids.length; i++) {
				acids[i] = parseAcid((String) acidString.get(i), numAALetterCode);
			}
			break;
		default:
			acids = null;
		throw new FoldingException("nonexistent number of letters in AA code "
				+ numAALetterCode);
		}
		return acids;
	}



	/**
	 * 
	 * @param input
	 * @return ArrayList
	 */
	public ArrayList<String> getTokens(String input) {
		// setting delimiters
		for (int i = 0; i < DELIMITERS.length; i++)
			input = input.replace(DELIMITERS[i], ' ');

		// parsing
		ArrayList<String> tokens = new ArrayList<String>(Arrays.asList(input.split(" ")));
		return tokens;
	}

	/**
	 * 
	 * @param acidString
	 * @param table
	 * @return AminoAcid
	 * @throws FoldingException
	 */
	public AminoAcid parseAcid(
			String acidString, 
			int numAALetterCode) throws FoldingException {

		AminoAcid acid;
		switch (numAALetterCode) {
		case 1:
			acid = GlobalDefaults.aaTable.getFromAbName(acidString);
			break;
		case 3:
			acid = GlobalDefaults.aaTable.get(acidString);
			break;
		default:
			acid = null;
		throw new FoldingException("nonexistent number of letters in AA code "
				+ numAALetterCode);
		}

		if (acid == null)
			throw new FoldingException("acid not found. ACID: " + acidString
					+ " TABLE: " + GlobalDefaults.aaTable.getName());
		return acid;
	}

	/**
	 * 
	 * 
	 * @param directionString
	 * @return Direction
	 * @throws FoldingException
	 */
	public Direction parseDirection(String directionString)
	throws FoldingException {
		if (directionString.trim().equalsIgnoreCase("none")) {
			//System.out.println("Direction is: none");
			return Direction.none;
		}
		Direction direction = Direction.getDirection(directionString);
		if (direction == Direction.none) // direction not found
			throw new FoldingException("direction not found. DIRECTION:  "
					+ directionString);
		return direction;
	}

	// non-public fields

	private static PolypeptideFactory instance;

	private static final char[] DELIMITERS = { ':', ',' };

	// non-public constructor

	private PolypeptideFactory() {
		// does nothing
	}

	// Helper method to construct a String from a String[]
	private static String arrayToString(String[] listOfIndices) {
		String s = "";
		for (int i = 0; i < listOfIndices.length; i++) {
			s += listOfIndices[i] + " ";
		}
		return s;
	}

	private AminoAcidTable createTable(String tableName)
	throws FoldingException {
		AminoAcidTable table;
		table = AminoAcidTable.makeTable(tableName);
		return table;
	}
}
