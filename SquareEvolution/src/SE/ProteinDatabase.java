package SE;
import java.util.HashMap;
import java.util.Iterator;

/*
 * database of folded protein sequences
 *   saved by String key =
 *      <Protein Sequence>;<Ligand Sequence>;<Ligand Structure>
 *   that way, can fold and bind to multiple ligands in same expt.
 */

public class ProteinDatabase {

	private HashMap<String, ProteinDatabaseEntry> database;
	private static ProteinDatabase instance;
	private double minimumFitness;

	private ProteinDatabase() {
		database = new HashMap<String, ProteinDatabaseEntry>();
		minimumFitness = 1.0f;
	}

	public static ProteinDatabase getInstance() {
		if (instance == null) {
			instance = new ProteinDatabase();
		}
		return instance;
	}
	
	public void clearAndAddBlankEntry(Configuration config) {
		database = new HashMap<String, ProteinDatabaseEntry>(); 
		/*
		 * note that fitness is adjusted in constructor to account for neutrality
		 * see Square Evolution Log 06 page 25
		 * 
		 */
		addEntry(new ProteinDatabaseEntry(
				config,
				"", 
				"None", 
				config.getLigandSequence(),
				config.getLigandStructure(),
				config.getLigandRotamer(),
				0,					// best lig x
				0,					// best lig y
				Double.MIN_VALUE,	// dGf
				Double.MIN_VALUE,	// dGb
				SquareEvolution.NO_PROTEIN_FITNESS));	// fitness 

	}

	public void addEntry(ProteinDatabaseEntry e) {
		String keyString = e.proteinSequence 
		+ ";" + e.ligandSequence 
		+ ";" + e.ligandStructure;
		database.put(keyString, e);
		
		double fit = e.fitness;
		if ((fit != 0.0f) && (fit < minimumFitness)) minimumFitness = fit;
	}

	public boolean containsEntry(
			String proteinSequence,
			String ligandSequence,
			String ligandStructure) {
		String keyString = proteinSequence 
		+ ";" + ligandSequence
		+ ";" + ligandStructure;
		return database.containsKey(keyString);
	}
	
	public ProteinDatabaseEntry getEntry(
			String proteinSequence,
			String ligandSequence,
			String ligandStructure) {
		String keyString = proteinSequence 
		+ ";" + ligandSequence
		+ ";" + ligandStructure;
		return database.get(keyString);
	}
	
	public int getSize() {
		return database.size();
	}
	
	public double getMinimumFitness() {
		return minimumFitness;
	}
	
	public String toString() {
		StringBuffer b = new StringBuffer();
		Iterator<String> it = database.keySet().iterator();
		b.append("Protein database\n");
		while (it.hasNext()) {
			String s = it.next();
			b.append("protein *" + s + "!" + database.get(s).fitness + "\n");
		}
		b.append("*****\n");
		return b.toString();
	}
}
