package SE;

import java.util.ArrayList;
 /*
  * data holder class for ProteinPicture
  */
public class ProteinData {
	int run;
	int generation;
	String aaSeq;
	double fitness;
	ArrayList<String> structure;
	
	public ProteinData(int run, int generation, String aaSeq, double fitness, ArrayList<String> structure) {
		this.run = run;
		this.generation = generation;
		this.aaSeq = aaSeq;
		this.fitness = fitness;
		this.structure = structure;
	}
	
	public String toString() {
		StringBuffer b = new StringBuffer();
		b.append("*******\n");
		b.append("\tRun=" + run + " Gen=" + generation + " " + aaSeq + " " + fitness);
		for (int i = 0; i < structure.size(); i++) {
			b.append("\t" + structure.get(i) + "\n");
		}
		b.append("\n");
		return b.toString();
	}

}
