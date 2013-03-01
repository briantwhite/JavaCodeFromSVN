package edu.umb.bio.jsMolCalc.client;

public class AtomSpec {
	private String type;
	private double logp;
	private boolean canMakeHbonds;
	private boolean canMakeIonicBonds;
	

	public AtomSpec(double logp, String type, boolean canMakeHbonds, boolean canMakeIonicBonds) {
		this.type = type;
		this.logp = logp;
		this.canMakeHbonds = canMakeHbonds;
		this.canMakeIonicBonds = canMakeIonicBonds; 
	}

	public String getType() {
		return type;
	}

	public double getLogp() {
		return logp;
	}
	
	public boolean canMakeHbonds() {
		return canMakeHbonds;
	}
	
	public boolean canMakeIonicBonds() {
		return canMakeIonicBonds;
	}

}
