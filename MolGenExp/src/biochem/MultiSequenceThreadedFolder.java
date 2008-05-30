package biochem;

import java.util.concurrent.LinkedBlockingQueue;

import evolution.Evolver;

import utilities.ProteinUtilities;

import molGenExp.FoldedProteinArchive;

public class MultiSequenceThreadedFolder implements Runnable {

	private int id;
	private Evolver evolver;

	public MultiSequenceThreadedFolder(int id, Evolver evolver) {
		this.id = id;
		this.evolver = evolver;
	}
	
	public int getId() {
		return id;
	}

	public void run() {
		while(evolver.getSequencesToFold().peek() != null) {
			String aaSeq = "";
			try {
				aaSeq = evolver.getSequencesToFold().take();
			} catch (InterruptedException e) {
				return;
			}
			FoldedPolypeptide fp = ProteinUtilities.foldProtein(aaSeq);
		}
	}

}
