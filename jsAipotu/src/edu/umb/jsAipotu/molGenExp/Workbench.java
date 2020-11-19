package edu.umb.jsAipotu.molGenExp;

import javax.swing.JPanel;

import biochem.PaintedInACornerFoldingException;

public abstract class Workbench extends JPanel {
	
	public MolGenExp mge;
	
	public Workbench(MolGenExp mge) {
		this.mge = mge;
	}
	
	public MolGenExp getMGE() {
		return mge;
	}
	
	public abstract WorkPanel getUpperPanel();
	public abstract WorkPanel getLowerPanel();
	
	public abstract void sendToUpperPanel(Object o) 
	throws PaintedInACornerFoldingException;
	public abstract void sendToLowerPanel(Object o) 
	throws PaintedInACornerFoldingException;
	public abstract void addToHistoryList(Object o) 
	throws PaintedInACornerFoldingException;
	
}
