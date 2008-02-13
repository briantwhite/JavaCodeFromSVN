package molGenExp;

import java.awt.Color;
import java.util.HashMap;

public class FoldedProteinArchive {
	
	private static FoldedProteinArchive singleton;
	private HashMap archive;
	
	private FoldedProteinArchive() {
		archive = new HashMap();
		//in the future, have it look for archive file and load if present
	}
	
	public static FoldedProteinArchive getInstance() {
		if (singleton == null) {
			singleton = new FoldedProteinArchive();
		}
		return singleton;
	}
	
	public void add(String aaSeq, String proteinString, Color color) {
		archive.put(aaSeq, 
				new FoldedProteinArchiveEntry(proteinString, color));
	}
	
	public boolean isInArchive(String aaSeq) {
		return archive.containsKey(aaSeq);
	}
	
	public FoldedProteinArchiveEntry getArchiveEntry(String aaSeq) {
		return (FoldedProteinArchiveEntry) archive.get(aaSeq);
	}
	
	public int getNumSequencesInArchive() {
		return archive.size();
	}
	
	public void saveArchiveToZipFile(String fileName) {
		//need to do this
	}

}
