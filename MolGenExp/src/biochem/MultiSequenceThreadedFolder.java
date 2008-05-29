package biochem;

import java.util.concurrent.LinkedBlockingQueue;

import evolution.Evolver;

import utilities.ProteinUtilities;

import molGenExp.FoldedProteinArchive;

public class MultiSequenceThreadedFolder implements Runnable {

	private int id;
	private Evolver evolver;
	private LinkedBlockingQueue<String> sequencesToFold;
	private FoldedProteinArchive archive;

	public MultiSequenceThreadedFolder(int id, Evolver evolver) {
		this.id = id;
		this.evolver = evolver;
		this.sequencesToFold = evolver.getSequencesToFold();
		this.archive = evolver.getArchive();
	}


	public void run() {
		while(sequencesToFold.peek() != null) {
			String aaSeq = "";
			try {
				aaSeq = sequencesToFold.take();
			} catch (InterruptedException e) {
				return;
			}
			FoldedPolypeptide fp = ProteinUtilities.foldProtein(aaSeq);
			archive.add(aaSeq, fp.getAaSeq(), fp.getColor());
		}
		evolver.informThreadDone(id);
	}

}
