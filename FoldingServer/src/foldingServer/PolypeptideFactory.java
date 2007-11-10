// PolypeptideFactory.java
//
//
// Copyright 2004, Ethan Bolker and Bogdan Calota
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

package foldingServer;

import java.util.ArrayList;
import java.util.StringTokenizer;

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
	 * @param tableName
	 *            table of amino acids.
	 * @param ppId
	 *            String ID for the folded polypeptide.
	 */
	public Polypeptide createPolypeptide(String[] args, boolean isSolution,
			boolean isRandom, String length, String seed, String tableName,
			int numAALetterCode, String ppId) throws FoldingException {

		return this.createPolypeptide(arrayToString(args), isSolution,
				isRandom, length, seed, tableName, numAALetterCode, ppId);
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
	 * @param tableName
	 *            table of amino acids.
	 * @param ppId
	 *            String ID for the folded polypeptide.
	 */
	public Polypeptide createPolypeptide(String input, boolean isSolution,
			boolean isRandom, String length, String seed, String tableName,
			int numAALetterCode, String ppId) throws FoldingException {

		if (isSolution) {
			return createSolution(tableName, numAALetterCode, input, ppId);
		} else if (isRandom) {
			return createRandom(tableName, length, seed, ppId);
		} else {
			return createFromAcids(tableName, numAALetterCode, input, ppId);
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
	public Polypeptide createSolution(String tableName, int numAALetterCode, String input,
			String ppId) throws FoldingException {
		AminoAcidTable table = createTable(tableName);

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
			acids[acidIndex++] = parseAcid((String) acidString.get(i), table, numAALetterCode);

			// parse direction string( found on odd positions)
			directions[directionIndex++] = parseDirection((String) acidString
					.get(i + 1));
		}

		// call constructor in Polypeptide
		return new Polypeptide(table, acids, directions, ppId);
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
	public Polypeptide createRandom(String tableName, String length,
			String seed, String ppId) throws FoldingException {

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

		return new Polypeptide(createTable(tableName), len, s, ppId);
	}

	/**
	 * 
	 * @param tableName
	 * @param input
	 * @return Polypeptide
	 * @throws FoldingException
	 */
	public Polypeptide createFromAcids(String tableName, int numAALetterCode, String input,
			String ppId) throws FoldingException {
		AminoAcidTable table = createTable(tableName);
		AminoAcid[] acids;
		
		switch (numAALetterCode) {
		case 1:
			char[] letters = input.toCharArray();
			acids = new AminoAcid[letters.length];
			for (int i = 0; i < letters.length; i++) {
				acids[i] = parseAcid(String.valueOf(letters[i]), table, numAALetterCode);
			}
			break;
		case 3:
			// parse input into strings, each representing an acid
			ArrayList acidString = getTokens(input);
			
			// parsing each acid string into AminoAcids using the AminoAcidTable.
			acids = new AminoAcid[acidString.size()];
			for (int i = 0; i < acids.length; i++) {
				acids[i] = parseAcid((String) acidString.get(i), table, numAALetterCode);
			}
			break;
		default:
			acids = null;
			throw new FoldingException("nonexistent number of letters in AA code "
					+ numAALetterCode);
		}
		
		// call constructor in Polypeptide
		return new Polypeptide(table, acids, ppId);
	}

	/**
	 * 
	 * @param input
	 * @return ArrayList
	 */
	public ArrayList getTokens(String input) {
		// setting delimiters
		for (int i = 0; i < DELIMITERS.length; i++)
			input = input.replace(DELIMITERS[i], ' ');

		// parsing
		ArrayList tokens = new ArrayList();
		StringTokenizer st = new StringTokenizer(input);
		while (st.hasMoreTokens())
			tokens.add(st.nextToken());
		return tokens;
	}

	/**
	 * 
	 * @param acidString
	 * @param table
	 * @return AminoAcid
	 * @throws FoldingException
	 */
	public AminoAcid parseAcid(String acidString, AminoAcidTable table, int numAALetterCode)
	throws FoldingException {
		AminoAcid acid;
		switch (numAALetterCode) {
			case 1:
				acid = table.getFromAbName(acidString);
				break;
			case 3:
				acid = table.get(acidString);
				break;
			default:
				acid = null;
				throw new FoldingException("nonexistent number of letters in AA code "
						+ numAALetterCode);
		}
		
		if (acid == null)
			throw new FoldingException("acid not found. ACID: " + acidString
					+ " TABLE: " + table.getName());
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
