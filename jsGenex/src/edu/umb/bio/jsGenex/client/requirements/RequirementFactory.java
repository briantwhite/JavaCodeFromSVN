package edu.umb.bio.jsGenex.client.requirements;

import java.util.List;

//import org.jdom.Element;

public class RequirementFactory {
	
	public static Requirement build(String req) {
		Requirement r = null;
		if (req.equals("BaseSelection")) r = new BaseSelectionRequirement();
		if (req.equals("SingleMutation")) r = new SingleMutationRequirement();
		if (req.equals("ShortermRNA")) r = new ShortermRNARequirement();
		if (req.equals("LongermRNA")) r = new LongermRNARequirment();
		if (req.equals("ShorterProtein")) r = new ShorterProteinRequirement();
		if (req.equals("LongerProtein")) r = new LongerProteinRequirement();
		if (req.equals("NomRNA")) r = new NomRNARequirement();
		if (req.equals("NoProtein")) r = new NoProteinRequirement();
		if (req.equals("SameProtein")) r = new SameProteinRequirement();
		if (req.equals("ParticularProtein")) r = new ParticularProteinRequirement();
		if (req.equals("ProteinLength")) r = new ProteinLengthRequirement();
		if (req.equals("IntronNumber")) r = new IntronNumberRequirement();
		if (req.equals("MutationLocation")) r = new MutationLocationRequirement();

		return r;
	}

}
