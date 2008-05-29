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

	public MultiSequenceThreadedFolder(int id,
			Evolver evolver,
			LinkedBlockingQueue<String> sequencesToFold, 
			FoldedProteinArchive archive) {
		this.id = id;
		this.evolver = evolver;
		this.sequencesToFold = sequencesToFold;
		this.archive = archive;
	}


	public void run() {
		while(sequencesToFold.peek() != null) {
			String aaSeq = "";
			try {
				aaSeq = sequencesToFold.take();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			FoldedPolypeptide fp = ProteinUtilities.foldProtein(aaSeq);
System.out.println("thread " + id + "folded:" + aaSeq);
			archive.add(aaSeq, fp.getAaSeq(), fp.getColor());
		}
System.out.println("thread " + id + " done");
		evolver.informThreadDone(id);
	}

}
