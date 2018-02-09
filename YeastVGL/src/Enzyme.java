
public class Enzyme {
	private int number;
	private Molecule substrate;
	private Molecule product;
	private boolean active;
	
	public Enzyme(int number) {
		this.number = number;
		this.active = true;
		this.substrate = null;
		this.product = null;
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
	
	
	

}
