package GeneticModels;

public abstract class Trait {
	
	//eg. yellow
	private String name;
	//eg. color
	private String type;
	// eg. eye
	private String bodyPart;
	
	public Trait(String name, String type, String bodyPart) {
		this.name = name;
		this.type = type;
		this.bodyPart = bodyPart;
	}
	
	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public String getBodyPart() {
		return bodyPart;
	}
	
	public String toString() {
		return bodyPart + ":" + type + ":" + name;
	}

}
