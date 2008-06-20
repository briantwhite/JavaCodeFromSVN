package GeneticModels;

import org.jdom.Element;

public class TraitFactory {
	
	private static TraitFactory instance;
	
	private TraitFactory() {
		
	}
	
	public static TraitFactory getInstance() {
		if (instance == null) {
			instance = new TraitFactory();
		}
		return instance;
	}
	
	public Trait buildTrait(Element e) {
		String traitName = e.getAttributeValue("TraitName");
		String type = e.getAttributeValue("Type");
		String bodyPart = e.getAttributeValue("BodyPart");
		if (type.equals("Color")) {
			return new ColorTrait(traitName, bodyPart);
		} else if (type.equals("Number")) {
			return new NumberTrait(traitName, bodyPart);
		} else if (type.equals("Shape")) {
			return new ShapeTrait(traitName, bodyPart);
		} else {
			return null;
		}
	}

}
