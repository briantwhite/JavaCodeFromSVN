
public class Enzyme {
	private int number;
	private Molecule substrate;
	private Molecule product;
	private boolean active;
	
	public Enzyme(int number, Molecule substrate, Molecule product) {
		this.number = number;
		this.substrate = substrate;
		this.product = product;
		this.active = true;
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
	
	public void activate() {
		active = true;
	}
	
	public void inactivate() {
		active = false;
	}
	
	

}
