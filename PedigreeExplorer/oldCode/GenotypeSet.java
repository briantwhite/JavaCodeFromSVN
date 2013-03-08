package PE;

import java.util.ArrayList;

public class GenotypeSet {
	
	private ArrayList<String[]> sets;
	
	public GenotypeSet() {
		sets = new ArrayList<String[]>();
	}
	
	public void add(String[] set) {
		sets.add(set);
	}
	
	public String[] get(int i) {
		return sets.get(i);
	}
	
	public ArrayList<String[]> getAll() {
		return sets;
	}
	
}
