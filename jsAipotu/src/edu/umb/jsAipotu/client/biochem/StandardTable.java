// StandardTable.java
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

package edu.umb.jsAipotu.client.biochem;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;



/**
 * Model the standard table of amino acids.
 */
public class StandardTable extends AminoAcidTable  {
	private Map table;
	private Map abNameTable; //added by TJ -- to save search time

	private double maxEnergy;

	public StandardTable() {
		table = new TreeMap();
		abNameTable = new TreeMap();
		try {
			add(new AminoAcid("R", "Arg", -15.86, 1, 1), 0.057);
			add(new AminoAcid("D", "Asp", -9.66, 1, -1), 0.053);
			add(new AminoAcid("E", "Glu", -7.75, 1, -1), 0.062);
			add(new AminoAcid("N", "Asn", -7.58, 1, 0), 0.044);
			add(new AminoAcid("K", "Lys", -6.49, 1, 1), 0.057);
			add(new AminoAcid("Q", "Gln", -6.48, 1, 0), 0.040);
			add(new AminoAcid("H", "His", -5.60, 1, 1), 0.022);
			add(new AminoAcid("S", "Ser", -4.34, 1, 0), 0.069);
			add(new AminoAcid("T", "Thr", -3.51, 1, 0), 0.058);
			add(new AminoAcid("Y", "Tyr", -1.08, 1, 0), 0.032);
			add(new AminoAcid("G", "Gly", 0.00, 0, 0), 0.072);
			add(new AminoAcid("P", "Pro", 0.01, 0, 0), 0.051); // check value
			add(new AminoAcid("C", "Cys", 0.34, 0, 0), 0.017);
			add(new AminoAcid("A", "Ala", 0.87, 0, 0), 0.083);
			add(new AminoAcid("W", "Trp", 1.39, 0, 0), 0.013);
			add(new AminoAcid("M", "Met", 1.41, 0, 0), 0.024);
			add(new AminoAcid("F", "Phe", 2.04, 0, 0), 0.039);
			add(new AminoAcid("V", "Val", 3.10, 0, 0), 0.066);
			add(new AminoAcid("I", "Ile", 3.98, 0, 0), 0.052);
			add(new AminoAcid("L", "Leu", 3.98, 0, 0), 0.090);
		} catch (FoldingException e) {
			System.err.println("shouldn't get here");
			e.printStackTrace();
		}
		normalize();
	}

	public Iterator getIterator() throws FoldingException {
		return table.keySet().iterator();
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
		String aName = a.getName().trim().toUpperCase();
		table.put(aName, new AcidInTable(a, probability));
		abNameTable.put(a.getAbName(), aName); // added by TJ
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
		throw new FoldingException(
				"can't add to standard table without probability");
	}

	/**
	 * Retrieve an acid from the table.
	 * 
	 * @param name
	 *            the name of the acid.
	 * 
	 * @return the acid, null if none.
	 */
	public AminoAcid get(String name) {
		AcidInTable a = (AcidInTable) table.get(name.trim().toUpperCase());
		if (a == null) {
			return null;
		}
		return a.a;
	}
	
	/**
	 * added by TJ
	 * Retrieve an acid from the table with abName
	 */
	public AminoAcid getFromAbName(String abName){
		String aName = (String) abNameTable.get(abName.trim().toUpperCase());
		if(aName == null){  // no amino acid with abName existed, return
			return null;
		}
		AcidInTable a = (AcidInTable) table.get(aName);
		return a.a;
	}

	/**
	 * A constant between 0 and 1 that leads to good contrast when normalized
	 * hydrophobic index determines color in a folding on a grid.
	 * 
	 * Default is 0.5;
	 * 
	 * @return the constant.
	 */
	public float getContrastScaler() {
		return (float) 1.0;
	}

	/**
	 * Returns the name of this table
	 */
	public String getName() {
		return AminoAcidTable.STANDARD;
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
		Random r = new Random(seed);
		for (int i = 0; i < sequence.length; i++) {
			double d = r.nextDouble();
			double ptotal = 0;
			Iterator iter = table.values().iterator();
			while (iter.hasNext()) {
				AcidInTable a = (AcidInTable) iter.next();
				ptotal += a.probability;
				if (d < ptotal) {
					sequence[i] = a.a;
					break; // back to for loop
				}
			}
		}
		return sequence;
	}

	public AminoAcid[] getAllAcids() {
		AminoAcid[] sequence = new AminoAcid[table.size()];
		int i = 0;
		Iterator iter = table.values().iterator();
		while (iter.hasNext()) {
			AcidInTable a = (AcidInTable) iter.next();
			sequence[i] = a.a;
			i++;
		}
		return sequence;
	}

	private void normalize() {
		double maxHI = Double.MIN_VALUE;
		double minHI = Double.MAX_VALUE;
		double ptotal = 0.0;
		Iterator i = table.values().iterator();
		while (i.hasNext()) {
			AcidInTable a = (AcidInTable) i.next();
			ptotal += a.probability;
			if (a.a.getHydrophobicIndex() > maxHI) {
				maxHI = a.a.getHydrophobicIndex();
			}
			if (a.a.getHydrophobicIndex() < minHI) {
				minHI = a.a.getHydrophobicIndex();
			}
		}

		// assume maxHI > 0 and minHI < 0
		maxEnergy = Math.max(maxHI, -minHI);

		i = table.values().iterator();
		while (i.hasNext()) {
			AcidInTable a = (AcidInTable) i.next();
			a.probability /= ptotal;
			a.a.setNormalizedHydrophobicIndex((a.a.getHydrophobicIndex() - minHI)
					/ (maxHI - minHI));
		}
	}

	private class AcidInTable implements Serializable {
		protected AminoAcid a;

		protected double probability;

		public AcidInTable(AminoAcid a, double probability) {
			this.a = a;
			this.probability = probability;
		}

		public String toString() {
			return a.toString() + '\t' + a.getHydrophobicIndex() + '\t'
					+ a.getNormalizedHydrophobicIndex() + "\t\t" + probability
					+ '\n';
		}
	}

	public String toString() {
		return "name\t\thi\tnormal\tprob\n" + table.toString();
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
		AminoAcidTable t = new StandardTable();
		System.out.println(t);
		AminoAcid[] list = t.getRandom(15, 999);
		for (int i = 0; i < list.length; i++) {
			System.out.println(list[i]);
		}
	}
}
