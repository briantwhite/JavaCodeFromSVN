// Folder.java
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

package protex;

/**
 * Abstract base class for folding algorithms.
 *  
 */

public abstract class Folder {
	protected Grid grid;

	protected static final double defaultHydroPhobicIndex = 1;

	protected static final double defaultHydrogenIndex = 1;

	protected static final double defaultIonicIndex = 1;

	protected double hpIndex = defaultHydroPhobicIndex;

	protected double hIndex = defaultHydrogenIndex;

	protected double iIndex = defaultIonicIndex;

	protected Polypeptide pp;

	private long time;

	protected java.text.DecimalFormat formatter = new java.text.DecimalFormat(
			"####.####");

	// for efficiency: save function calls by creating an array here
	protected AcidInChain[] acids;

	protected int numAcids;

	public Folder(Polypeptide pp, Grid grid) {
		this.grid = grid;
		this.pp = pp;
		numAcids = pp.getLength();
		acids = pp.getAcidArray();
	}

	public void fold() {
		time = System.currentTimeMillis();
		realFold();
		time = System.currentTimeMillis() - time;
		grid.computeStatistics(hpIndex, hIndex, iIndex);
		pp.setFolded();
	}

	public abstract void realFold();

	public abstract String getStatistics();

	public abstract String getName();

	public void setHydroPhobicIndex(double hpIndex) {
		this.hpIndex = hpIndex;
	}

	public void setHydrogenIndex(double hIndex) {
		this.hIndex = hIndex;
	}

	public void setIonicIndex(double iIndex) {
		this.iIndex = iIndex;
	}

	public String report() {
		StringBuffer buf = new StringBuffer(getName());
		buf.append("\n" + pp.toString());
		buf.append("\nenergy "
				+ formatter.format(grid.getEnergy(hpIndex, hIndex, iIndex)));
		buf.append("\nfolding index "
				+ formatter.format(grid
						.getFoldingIndex(hpIndex, hIndex, iIndex)));
		buf.append("\ntime   " + getTime() + " seconds");
		buf.append("\ntopology " + pp.getTopology());
		return buf.toString();
	}

	public String csvReport() {
		StringBuffer buf = new StringBuffer();
		buf.append(""
				+ formatter.format(grid.getEnergy(hpIndex, hIndex, iIndex)));
		buf.append(", "
				+ formatter.format(grid
						.getFoldingIndex(hpIndex, hIndex, iIndex)));
		buf.append(", " + pp.getDirectionSequence());
		buf.append(", " + pp.toCSV());
		return buf.toString();
	}

	public String getTopology() {
		return pp.getTopology();
	}

	public long getTime() {
		return time / 1000;
	}

	public double getEnergy() {
		return grid.getEnergy(hpIndex, hIndex, hIndex);
	}
}
