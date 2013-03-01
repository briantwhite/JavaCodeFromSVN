package LogAnalyzer;

import java.util.ArrayList;
import java.util.Iterator;

/*
 * a genetic result that can be concluded from a conspicuous cross
 */
public class DominanceInteractionResult extends GeneticResult {
	private ArrayList<String> phenos;

	public DominanceInteractionResult(ArrayList<String> phenos) {
		this.phenos = phenos;
	}

	public boolean equals(Object o) {
		if (o instanceof DominanceInteractionResult) {
			DominanceInteractionResult d = (DominanceInteractionResult)o;
			if (d.phenos.containsAll(phenos)) return true;
		}
		return false;
	}
	
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("Result: ");
		Iterator<String> sIt = phenos.iterator();
		while(sIt.hasNext()) {
			buf.append(sIt.next() + ";");
		}
		buf.deleteCharAt(buf.length() - 1);
		return buf.toString();
	}
	
	public int hashCode() {
		int result = 0;
		Iterator<String> phIt = phenos.iterator();
		while (phIt.hasNext()) {
			result = result ^ phIt.next().hashCode();
		}
		return result;
	}

}
