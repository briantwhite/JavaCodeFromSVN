package Biochemistry;

public class Enzyme {
	private int number;
	private Molecule substrate;
	private Molecule product;
	private boolean active;
	private String name;		// what it's called in the student-drawn pathway
	
	public Enzyme(int number) {
		this.number = number;
		this.active = true;
		this.substrate = null;
		this.product = null;
		this.name = "";
	}
	
	public void setSubstrate(Molecule substrate) {
		this.substrate = substrate;
	}
	
	public void setProduct(Molecule product) {
		this.product = product;
	}
	
	public void activate() {
		active = true;
	}
	
	public void inactivate() {
		active = false;
	}

	public int getNumber() {
		return number;
	}
	
	public Molecule getSubstrate() {
		return substrate;
	}
	
	public Molecule getProduct() {
		return product;
	}
	
	public boolean isActive() {
		return active;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	
	

}
