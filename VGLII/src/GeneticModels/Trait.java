package GeneticModels;

public abstract class Trait {
	
	//eg. yellow
	private String traitName;
	//eg. color
	private String type;
	// eg. eye
	private String bodyPart;
	
	public Trait(String traitName, String type, String bodyPart) {
		this.traitName = traitName;
		this.type = type;
		this.bodyPart = bodyPart;
	}
	
	public String getTraitName() {
		return traitName;
	}

	public String getType() {
		return type;
	}

	public String getBodyPart() {
		return bodyPart;
	}
	
	public static Trait getNullVersion(Trait t) {
		if (t instanceof ColorTrait) {
			return new ColorTrait("-", t.getBodyPart());
		}
		if (t instanceof NumberTrait) {
			return new NumberTrait("-", t.getBodyPart());
		}
		if (t instanceof ShapeTrait) {
			return new ShapeTrait("-", t.getBodyPart());
		}
		return null;
	}
		
	public String toString() {
		return bodyPart + ":" + type + ":" + traitName;
	}

}
