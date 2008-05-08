package GeneticModels;

public abstract class GeneticObject {
	
	private int intVal;
	private String name;
	
	public GeneticObject(int intVal, String name) {
		this.intVal = intVal;
		this.name = name;
	}
	
	public GeneticObject(int intVal) {
		this.intVal = intVal;
		name = "";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getIntVal() {
		return intVal;
	}


}
