package edu.umb.jsAipotu.client.molGenExp;

public abstract class HistListItem {
	
	public String toolTipText;
	
	public HistListItem() {
		
	}

	public abstract String getToolTipText();
	public abstract void setToolTipText(String text);
}
