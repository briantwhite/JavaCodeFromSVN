package GeneticModels;

import java.util.HashMap;
import java.util.Iterator;

public class AllelePool extends GeneticObjectPool {
	
	private static AllelePool instance;
	private HashMap<String, Allele> allAllelesByName;
	private Allele[] allAllelesByNumber;
	
	private AllelePool() {
		super();
		allObjectsByName.put("NULL", new Allele(Allele.NULL, "NULL"));
		allObjectsByName.put("ONE", new Allele(Allele.ONE, "ONE"));
		allObjectsByName.put("TWO", new Allele(Allele.TWO, "TWO"));
		allObjectsByName.put("THREE", new Allele(Allele.THREE, "THREE"));
		setupTables();
	}
	
	public static AllelePool getInstance() {
		if (instance == null) {
			instance = new AllelePool();
		}
		return instance;
	}
	
}
