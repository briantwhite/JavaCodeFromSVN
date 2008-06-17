package VGL;

public class MFTotCounts {
	private int males;
	private int females;
	private int total;
	
	public MFTotCounts(int males, int females) {
		this.males = males;
		this.females = females;
		total = males + females;
	}

	public int getMales() {
		return males;
	}

	public int getFemales() {
		return females;
	}

	public int getTotal() {
		return total;
	}
	
	public MFTotCounts add(MFTotCounts x) {
		return new MFTotCounts(
				males + x.getMales(), 
				females + x.getFemales());
	}
	
	public String toString() {
		return "M: " + males + " F: " + females + " T: " + total;
	}
	
}
