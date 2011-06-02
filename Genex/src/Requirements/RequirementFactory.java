package Requirements;

import java.util.List;

import org.jdom.Element;

public class RequirementFactory {
	
	public static Requirement build(Element e) {
		Requirement r = null;
		List<Element> parts = e.getChildren();
		if (e.getAttributeValue("type").equals("BaseSelection")) r = new BaseSelectionRequirement(parts);
		if (e.getAttributeValue("type").equals("SingleMutation")) r = new SingleMutationRequirement(parts);
		if (e.getAttributeValue("type").equals("ShorterProtein")) r = new ShorterProteinRequirement(parts);
		return r;
	}

}
