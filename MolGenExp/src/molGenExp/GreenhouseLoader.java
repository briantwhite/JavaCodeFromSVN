package molGenExp;

import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;

import javax.swing.ImageIcon;

import biochem.Attributes;
import biochem.FoldedPolypeptide;
import biochem.FoldingException;
import biochem.FoldingManager;
import biochem.OutputPalette;

import molBiol.ExpressedGene;
import molBiol.MolBiolWorkpanel;

public class GreenhouseLoader implements Runnable {
	
	File greenhouseDir;
	ArrayList organismFiles;
	MolBiolWorkpanel mbwp;
	Greenhouse greenhouse;
	int i;
	
	public GreenhouseLoader(File greenhouseDir,
			MolBiolWorkpanel mbwp,
			Greenhouse greenhouse) {
		this.greenhouseDir = greenhouseDir;
		this.mbwp = mbwp;
		this.greenhouse = greenhouse;
		String[] files = greenhouseDir.list();
		organismFiles = new ArrayList();
		for (int counter = 0; counter < files.length; counter++) {
			String fileName = files[counter];
			if (fileName.endsWith(".organism")) {
				organismFiles.add(fileName);
			}
		}
	}
	
	public int getLengthOfTask() {
		return organismFiles.size();
	}
	
	public int getCurrent() {
		return i;
	}
	
	public void stop() {
		i = organismFiles.size();
	}
	
	boolean done() {
		if (i >= organismFiles.size()) {
			return true;
		} else {
			return false;
		}
	}
	
	
	public void run() {
		for (i = 0; i < organismFiles.size(); i++){
			String fileString = (String)organismFiles.get(i);
			
			ArrayList geneSequences = new ArrayList();
			
			String organismName = 
				fileString.replaceAll(".organism", "");
			String orgFileName = greenhouseDir.toString() 
			+ System.getProperty("file.separator") 
			+ fileString;
			
			BufferedReader input = null;
			try {
				input = new BufferedReader(new FileReader(orgFileName));
				String line = null;
				while ((line = input.readLine()) != null) {
					Pattern p = Pattern.compile("[^AGCT]+");
					if (!p.matcher(line).find()) {
						geneSequences.add(line);
					} 
				}
				
				// be sure there are only 2 DNA sequences in the organism
				if (geneSequences.size() == 2) {
					ExpressedGene eg1 = 
						mbwp.expressGene((String)geneSequences.get(0), -1);
					ExpressedGene eg2 = 
						mbwp.expressGene((String)geneSequences.get(1), -1);
					eg1.setFoldedPolypeptide(foldProtein(eg1.getGene().getProteinString()));
					eg2.setFoldedPolypeptide(foldProtein(eg2.getGene().getProteinString()));
					Organism o = new Organism(organismName, eg1, eg2);
					greenhouse.add(o);
				}
				input.close();
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
			finally {
				try {
					if (input!= null) {
						input.close();
					}
				}
				catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}
		
		
	}
	
	public FoldedPolypeptide foldProtein(String aaSeq) {
		if (aaSeq.indexOf("none") != -1) {
			aaSeq = "";
		} else {
			//remove leading/trailing spaces and the N- and C-
			aaSeq = aaSeq.replaceAll(" ", "");
			aaSeq = aaSeq.replaceAll("N-", "");
			aaSeq = aaSeq.replaceAll("-C", "");
			
			//insert spaces between amino acid codes
			StringBuffer psBuffer = new StringBuffer(aaSeq);
			for (int i = 3; i < psBuffer.length(); i = i + 4) {
				psBuffer = psBuffer.insert(i, " ");
			}
			aaSeq = psBuffer.toString();
		}
		
		//fold it
		Attributes attributes = new Attributes(aaSeq, 3, "straight");
		FoldingManager manager = FoldingManager.getInstance();
		try {
			manager.fold(attributes);
		} catch (FoldingException e) {
			e.printStackTrace();
		}
		
		//make an icon and display it in a dialog
		OutputPalette op = new OutputPalette();
		manager.createCanvas(op);
		Dimension requiredCanvasSize = 
			op.getDrawingPane().getRequiredCanvasSize();
		
		ProteinImageSet images = 
			ProteinImageFactory.generateImages(op, requiredCanvasSize);
		
		return new FoldedPolypeptide(
				aaSeq,
				op.getDrawingPane().getGrid(), 
				new ImageIcon(images.getThumbnailImage()), 
				op.getProteinColor());
		
	}
	
}
