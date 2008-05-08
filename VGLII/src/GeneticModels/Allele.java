package GeneticModels;

public class Allele extends GeneticObject{
	
	public static final int NULL = 0;
	public static final int ONE = 1;
	public static final int TWO = 2;
	public static final int THREE = 3;
	
	private int intVal;
	private String name;
	
	public Allele(int intVal, String name) {
		super(intVal, name);
	}
	
	public Allele(int intVal) {
		super(intVal);
	}
}
