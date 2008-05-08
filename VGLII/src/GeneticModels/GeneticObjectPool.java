package GeneticModels;

import java.util.HashMap;
import java.util.Iterator;

public class GeneticObjectPool {

	private static GeneticObjectPool instance;
	protected HashMap<String, GeneticObject> allObjectsByName;
	private GeneticObject[] allObjectsByNumber;

	public GeneticObjectPool() {
		allObjectsByName = new HashMap();
	}

	public void setupTables() {
		allObjectsByNumber = new Allele[allObjectsByName.size()];
		Iterator it = allObjectsByName.keySet().iterator();
		while (it.hasNext()) {
			GeneticObject a = allObjectsByName.get((String)it.next());
			allObjectsByNumber[a.getIntVal()] = a;
		}	
	}
	
	public GeneticObject getByName(String name) {
		return allObjectsByName.get(name);
	}

	public GeneticObject getByNumber(int n) {
		return allObjectsByNumber[n];
	}

	protected void buildObjectByNameTable() {
	}

}
