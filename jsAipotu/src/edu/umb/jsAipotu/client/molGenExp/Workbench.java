package edu.umb.jsAipotu.client.molGenExp;

import com.google.gwt.user.client.ui.HorizontalPanel;

import edu.umb.jsAipotu.client.biochem.PaintedInACornerFoldingException;

public abstract class Workbench extends HorizontalPanel {
	
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
