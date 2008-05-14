package GeneticModels;

public abstract class Trait implements Cloneable {
	
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
	
	public Object clone() {
		Object o = null;
		try {
			o = super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return o;
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
	
	public void setTraitName(String s) {
		traitName = s;
	}
	
	public String toString() {
		return bodyPart + ":" + type + ":" + traitName;
	}

}
