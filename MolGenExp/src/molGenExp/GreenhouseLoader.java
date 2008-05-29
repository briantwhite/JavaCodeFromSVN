package molGenExp;

import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;

import javax.swing.ImageIcon;

import utilities.ExpressedGene;
import utilities.GeneExpresser;
import utilities.ProteinUtilities;
import biochem.BiochemAttributes;
import biochem.FoldedPolypeptide;
import biochem.FoldingException;
import biochem.FoldingManager;
import biochem.OutputPalette;

public class GreenhouseLoader implements Runnable {
	
	private File greenhouseDir;
	private ArrayList organismFiles;
	private Greenhouse greenhouse;
	private int i;
	private GeneExpresser geneExpresser;
	
	
	public GreenhouseLoader(File greenhouseDir, Greenhouse greenhouse) {
		this.greenhouseDir = greenhouseDir;
		this.greenhouse = greenhouse;
		String[] files = greenhouseDir.list();
		organismFiles = new ArrayList();
		for (int counter = 0; counter < files.length; counter++) {
			String fileName = files[counter];
			if (fileName.endsWith(".organism")) {
				organismFiles.add(fileName);
			}
		}
		geneExpresser = GeneExpresser.getInstance();
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
						geneExpresser.expressGene((String)geneSequences.get(0), -1);
					ExpressedGene eg2 = 
						geneExpresser.expressGene((String)geneSequences.get(1), -1);
					Organism o = new Organism(organismName, 
							new ExpressedAndFoldedGene(
									eg1,
									ProteinUtilities.foldProtein(eg1.getProtein())), 
							new ExpressedAndFoldedGene(
									eg2,
									ProteinUtilities.foldProtein(eg2.getProtein())));
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
		
}
