package Logging;


public class BadProteinLogEntry extends LogEntry {
	
	// header
	public BadProteinLogEntry() {
		value = "Sequence,dGf,bestBindingNrg,bindingPartSum";
	}
	
	public BadProteinLogEntry(String value) {
		this.value = value;
	}
}
