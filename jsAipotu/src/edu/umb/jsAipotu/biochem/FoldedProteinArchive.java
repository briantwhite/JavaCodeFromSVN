package edu.umb.jsAipotu.biochem;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import edu.umb.jsAipotu.preferences.MGEPreferences;




public class FoldedProteinArchive {

	private static FoldedProteinArchive singleton;
	private HashMap<String, FoldedAndColoredProtein> archive;    
	private static final String archiveFileName = "FoldedProteinArchive";
	private static int totalFoldedSequences;

	private FoldedProteinArchive() {
		archive = new HashMap<String, FoldedAndColoredProtein>();
		loadArchiveFromFile();
		totalFoldedSequences = 0;
	}

	public static FoldedProteinArchive getInstance() {
		if (singleton == null) {
			singleton = new FoldedProteinArchive();
		}
		return singleton;
	}

	public synchronized void add(String aaSeq, String proteinString, Color color) {
		archive.put(aaSeq, 
				new FoldedAndColoredProtein(proteinString, color));
		totalFoldedSequences++;
	}

	public synchronized boolean isInArchive(String aaSeq) {
		return archive.containsKey(aaSeq);
	}

	public synchronized FoldedAndColoredProtein getEntry(String aaSeq) {
		return archive.get(aaSeq);
	}

	public int getNumSequencesInArchive() {
		return archive.size();
	}
	
	public static int getTotalFoldedSequences() {
		return totalFoldedSequences;
	}
	
	public void saveArchiveToZipFile() {
		StringBuffer buf = new StringBuffer();
		Set<String> keySet = archive.keySet();
		Iterator<String> it = keySet.iterator();

		while (it.hasNext()) {
			String seq = (String)it.next();
			FoldedAndColoredProtein entry = archive.get(seq);
			buf.append(seq);
			buf.append(";");
			buf.append(entry.getProteinString());
			buf.append(";");
			String colorString = entry.getColor().toString();
			colorString = colorString.replaceAll("java.awt.Color\\[r=", "");
			colorString = colorString.replaceAll(",g=", "/");
			colorString = colorString.replaceAll(",b=", "/");
			colorString = colorString.replaceAll("]", "");
			buf.append(colorString);
			buf.append("\n");
		}

		String archiveString = buf.toString();
		byte[] archiveBytes = null;
		try {
			archiveBytes = archiveString.getBytes("US-ASCII");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		ZipOutputStream archiveWriter = null;
		try {
			archiveWriter = 
				new ZipOutputStream(new FileOutputStream(
						MGEPreferences.getInstance().getGreenhouseDirectory().getAbsolutePath() + 
						System.getProperty("file.separator") +
						archiveFileName + ".zip"));
			archiveWriter.setLevel(Deflater.DEFAULT_COMPRESSION);
			archiveWriter.putNextEntry(new ZipEntry(archiveFileName + ".txt"));
			archiveWriter.write(archiveBytes, 0, archiveBytes.length);
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			try {
				if (archiveWriter != null) {
					archiveWriter.close();
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private synchronized void loadArchiveFromFile() {
		String fullArchiveFileName = 
				MGEPreferences.getInstance().getGreenhouseDirectory().getAbsolutePath() + 
				System.getProperty("file.separator") + 
			archiveFileName + ".zip";

		File archiveFile = new File(fullArchiveFileName);
		if (archiveFile.exists()) {
			try {
				ZipFile archiveZip = new ZipFile(fullArchiveFileName);
				Enumeration zipFileEntries = archiveZip.entries();
				while (zipFileEntries.hasMoreElements()) {
					ZipEntry zipEntry = (ZipEntry)zipFileEntries.nextElement();
					BufferedReader zipReader = 
						new BufferedReader(
								new InputStreamReader(
										archiveZip.getInputStream(zipEntry)));
					String line = null;
					while ((line = zipReader.readLine()) != null) {
						if (line.indexOf(":") > -1) {
							addEntryToArchive(line);
						}
					}
					zipReader.close();
				}
				archiveZip.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	//take it in format <num;>aaseq;direction string; color (as R/G/B)
	// first num is optional (it comes from the folding server and can be ignored
	public synchronized void addEntryToArchive(String s) {
		String[] parts = s.split(";");
		int startIndex = 0;
		// if the first part is a number; ignore it
		if (parts[0].matches("[0-9]+")) {
			startIndex = 1;
		}

		//in format R/G/B
		String colorString = parts[startIndex + 2];
		String[] colorStringParts = colorString.split("/");
		Color color = new Color(
				Integer.parseInt(colorStringParts[0]),
				Integer.parseInt(colorStringParts[1]),
				Integer.parseInt(colorStringParts[2]));
		add(parts[startIndex], parts[startIndex + 1], color);
//		System.out.println(parts[startIndex] + " " + parts[startIndex + 1] + " " + color.toString());
	}

}
