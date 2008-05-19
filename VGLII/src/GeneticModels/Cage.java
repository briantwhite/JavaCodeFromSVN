package GeneticModels;

import java.util.HashMap;

public class Cage {
	
	private HashMap<String, OrganismList> cageContents;
	
	public Cage() {
		cageContents = new HashMap<String, OrganismList>();
	}
	
	public void add(Organism o) {
		//if there isn't a list of organisms with this pheno
		//  make one
		if (!cageContents.containsKey(o.getPhenotypeString())) {
			OrganismList oList = new OrganismList();
			oList.add(o);
			cageContents.put(o.getPhenotypeString(), oList);
		} 
		
		// add the orf to the list and add to the hash map
		OrganismList oList = cageContents.get(o.getPhenotypeString());
		oList.add(o);
	}
	
	public String toString() {
		StringBuffer b = new StringBuffer();
		b.append("results:\n");
		for (String phenoString: cageContents.keySet()) {
			OrganismList oList = cageContents.get(phenoString);
			b.append(phenoString + ": " 
					+ oList.getTotalNumber() + ", "
					+ oList.getNumberOfMales() + " males, " 
					+ oList.getNumberOfFemales() + " females\n");
		}
		return b.toString();
	}

}
