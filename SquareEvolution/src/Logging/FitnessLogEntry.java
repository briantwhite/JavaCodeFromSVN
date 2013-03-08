package Logging;


public class FitnessLogEntry extends LogEntry {

	//header
	public FitnessLogEntry(boolean nearestNeighborMode) {
		if (nearestNeighborMode) {
			value = "Sequence,DNA,Protein,RawFit,NormFit,Task,CodeName,Silent";
		} else {
			value = "Run,Generation,MaxFit,AvgFit,LenMax,XMax,YMax,RotMax,SeqMax,NumDNASpecies,ShannonDNADiv,"
					+ "NumProtSpecies,ShannonProtDiv";
		}
	}

	public FitnessLogEntry(String value) {
		this.value = value;
	}
}
