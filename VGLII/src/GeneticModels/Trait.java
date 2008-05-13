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
	
	// make null trait for Y or W chromo
	//  keep the character but make the trait "-"
	public void nullify() {
		traitName = "-";
	}
		
	public String toString() {
		return bodyPart + ":" + type + ":" + traitName;
	}

}
