
public class DataPoint {
	
	public double totalValid;	// used to calculate average
	public int numBad;			// count of -1 "bad data" and blank responses
	public int numValid;
	
	public DataPoint() {
		totalValid = 0.0f;
		numBad = 0;
		numValid = 0;
	}

	public double getAverageValid() {
		return totalValid/numValid;
	}
}
