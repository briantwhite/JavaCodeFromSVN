package Requirements;

import Problems.GenexState;

public abstract class Requirement {
	
	public abstract String getFailureString();
	
	public abstract boolean isSatisfied(GenexState state);

}
