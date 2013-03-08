package SE;

public class NeighborResult {

	public int sequence;
	public String DNA;
	public String protein;
	public double rawFit;
	public double normFit;
	public String task;
	public String codeName;
	public boolean silent;

	public NeighborResult(int sequence, String DNA) {
		this.sequence = sequence;
		this.DNA = DNA;
		protein = "";
		rawFit = 0.0f;
		normFit = 0.0f;
		task = "";
		codeName = "";
		silent = false;
	}

	public String toString() {
		return String.valueOf(sequence) + ","
				+ DNA + "," 
				+ protein + ","	
				+ String.valueOf(rawFit) + ","
				+ String.valueOf(normFit) + ","	
				+ task + ","	
				+ codeName + ","	
				+ String.valueOf(silent);
	}

}
