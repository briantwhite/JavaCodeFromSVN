package Requirements;

import java.util.List;

import org.jdom.Element;

public class RequirementFactory {
	
	public static Requirement build(Element e) {
		Requirement r = null;
		List<Element> parts = e.getChildren();
		if (e.getAttributeValue("type").equals("BaseSelection")) r = new BaseSelectionRequirement(parts);
		if (e.getAttributeValue("type").equals("SingleMutation")) r = new SingleMutationRequirement(parts);
		if (e.getAttributeValue("type").equals("ShortermRNA")) r = new ShortermRNARequirement(parts);
		if (e.getAttributeValue("type").equals("LongermRNA")) r = new LongermRNARequirment(parts);
		if (e.getAttributeValue("type").equals("ShorterProtein")) r = new ShorterProteinRequirement(parts);
		if (e.getAttributeValue("type").equals("LongerProtein")) r = new LongerProteinRequirement(parts);
		if (e.getAttributeValue("type").equals("NomRNA")) r = new NomRNARequirement(parts);
		if (e.getAttributeValue("type").equals("NoProtein")) r = new NoProteinRequirement(parts);
		if (e.getAttributeValue("type").equals("SameProtein")) r = new SameProteinRequirement(parts);
		if (e.getAttributeValue("type").equals("ParticularProtein")) r = new ParticularProteinRequirement(parts);
		if (e.getAttributeValue("type").equals("ProteinLength")) r = new ProteinLengthRequirement(parts);
		if (e.getAttributeValue("type").equals("IntronNumber")) r = new IntronNumberRequirement(parts);
		if (e.getAttributeValue("type").equals("MutationLocation")) r = new MutationLocationRequirement(parts);

		return r;
	}

}
