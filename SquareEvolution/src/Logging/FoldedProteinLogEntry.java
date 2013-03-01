package Logging;


public class FoldedProteinLogEntry extends LogEntry {
	
	// header
	public FoldedProteinLogEntry() {
		value = "ProtSeq,ProtConf,LigSeq,LigConf,dGf,Ebest,Bpart,R,X,Y";
	}
	
	public FoldedProteinLogEntry(String value) {
		this.value = value;
	}
}
