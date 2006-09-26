// VirtualTable.java
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
//Modified by Namita, Ruchi (NR) on 10/17/2004

package biochem;

/**
 * A virtual table of acids contains one for each possible hydrophobic index.
 */
public class VirtualTable extends AminoAcidTable {
	double maxEnergy;

	public VirtualTable() {
		maxEnergy = 1.0;
	}

	/**
	 * Add an AminoAcid to the table, with its frequency.
	 * 
	 * @param a
	 *            the AminoAcid to add.
	 * @param probability
	 *            how likely is that AminoAcid?
	 */
	public void add(AminoAcid a, double probability) throws FoldingException {
		throw new FoldingException("can't add to virtual table");
	}

	/**
	 * Add an AminoAcid to the table.
	 * 
	 * @param a
	 *            the AminoAcid to add.
	 * 
	 * @throws FoldingException
	 *             if operation not allowed.
	 */
	public void add(AminoAcid a) throws FoldingException {
		throw new FoldingException("can't add to virtual table");
	}

	/**
	 * Returns the name of this table
	 */
	public String getName() {
		return AminoAcidTable.VIRTUAL;
	}

	/**
	 * Retrieve an acid from the table.
	 * 
	 * @param name
	 *            the name of the acid.
	 * 
	 * @return the acid, null if none.
	 */
	public AminoAcid get(String name) throws FoldingException {
		try {
			double hydrophobicIndex = (new Double(name)).doubleValue();

			//Added on 10/17/2004 by Namita and Ruchi
			double hydrogenbondIndex = (new Double(name)).doubleValue();
			double ionicIndex = (new Double(name)).doubleValue();

			updateMax(hydrophobicIndex);
			return new AminoAcid(hydrophobicIndex, hydrogenbondIndex,
					ionicIndex);
		} catch (NumberFormatException e) {
			throw new FoldingException("hydrophobic index " + name
					+ " not a number");
		}
	}

	/**
	 * Choose a random sequence of AminoAcids from this table.
	 * 
	 * @param length
	 *            the length of the desired sequence.
	 * @param seed
	 *            a seed for the random number generator.
	 */
	public AminoAcid[] getRandom(int length, int seed) {
		AminoAcid[] sequence = new AminoAcid[length];
		java.util.Random random = new java.util.Random(seed);
		for (int i = 0; i < length; i++) {
			double d = ((double) random.nextInt(1000)) / 1000;
			if (random.nextBoolean()) {
				d = -d;
			}
			updateMax(d);
		}
		return sequence;
	}

	private void updateMax(double d) {
		maxEnergy = Math.max(Math.abs(d), maxEnergy);
	}

	/**
	 * A bound on the absolute value of the energy of AminoAcids in the table.
	 * 
	 * @return the bound.
	 */
	public double getMaxEnergy() {
		return maxEnergy;
	}

	/**
	 * Method main for unit testing.
	 */
	public static void main(String[] args) {
		AminoAcidTable t = new VirtualTable();
		AminoAcid[] list = t.getRandom(15, 999);
		for (int i = 0; i < list.length; i++) {
			System.out.println(list[i]);
		}
	}

	public AminoAcid getFromAbName(String name) throws FoldingException {
		// TODO Auto-generated method stub
		return null;
	}
}
