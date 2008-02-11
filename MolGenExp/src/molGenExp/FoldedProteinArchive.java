package molGenExp;

import java.util.HashMap;

public class FoldedProteinArchive {
	
	private HashMap archive;
	
	public FoldedProteinArchive() {
		archive = new HashMap();
		//in the future, have it look for archive file and load if present
	}
	
	public void add(String aaSeq, String proteinString) {
		archive.put(aaSeq, proteinString);
	}
	
	public boolean isInArchive(String aaSeq) {
		return archive.containsKey(aaSeq);
	}
	
	public String getProteinString(String aaSeq) {
		return (String) archive.get(aaSeq);
	}
	
	public int getNumSequencesInArchive() {
		return archive.size();
	}
	
	public void saveArchiveToZipFile(String fileName) {
		//need to do this
	}

}
