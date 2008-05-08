package GeneticModels;

public class Allele {
	
	public static final int NULL = 0;
	public static final int ONE = 1;
	public static final int TWO = 2;
	public static final int THREE = 3;
	
	private int intVal;
	private String name;
	
	public Allele(int intVal, String name) {
		this.intVal = intVal;
		this.name = name;
	}
	
	public Allele(int intVal) {
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
