package Requirements;

import Problems.GenexState;

public abstract class Requirement {
	
	protected String failureString = "unassigned";
	
	public String getFailureString() {
		return failureString;
	}
	
	public abstract boolean isSatisfied(GenexState state);

}
