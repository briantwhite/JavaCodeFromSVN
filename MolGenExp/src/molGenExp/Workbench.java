package molGenExp;

import javax.swing.JPanel;

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
	
	public abstract void sendToUpperPanel(Object o);
	public abstract void sendToLowerPanel(Object o);
	public abstract void addToHistoryList(Object o);
	
}
