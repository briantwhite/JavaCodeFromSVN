package biochem;

import java.util.concurrent.LinkedBlockingQueue;

import utilities.ProteinUtilities;

import molGenExp.FoldedProteinArchive;

public class MultiSequenceThreadedFolder implements Runnable {
	
	private LinkedBlockingQueue<String> sequencesToFold;
	private FoldedProteinArchive archive;
	
	public MultiSequenceThreadedFolder(
			LinkedBlockingQueue<String> sequencesToFold, 
			FoldedProteinArchive archive) {
		this.sequencesToFold = sequencesToFold;
		this.archive = archive;
	}
	

	public void run() {
		String aaSeq = "";
		try {
			aaSeq = sequencesToFold.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Folding:" + aaSeq);
		FoldedPolypeptide fp = ProteinUtilities.foldProtein(aaSeq);
		archive.add(aaSeq, fp.getAaSeq(), fp.getColor());
	}

}
