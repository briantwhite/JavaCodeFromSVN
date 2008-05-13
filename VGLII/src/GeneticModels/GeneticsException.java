package GeneticModels;

public class GeneticsException extends Exception {
	
	String message;
	
	public GeneticsException(String message) {
		this.message = message;
	}
	
	public String toString() {
		return message;
	}

}
