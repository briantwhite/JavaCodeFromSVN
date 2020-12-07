package edu.umb.jsAipotu.client.biochem;

import java.util.HashMap;

import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Window;

public class FoldedProteinArchive {

	private static FoldedProteinArchive singleton = null;
	private HashMap<String, FoldedAndColoredProtein> archive;    
	private static int totalFoldedSequences;

	private FoldedProteinArchive() {
		archive = new HashMap<String, FoldedAndColoredProtein>();
		totalFoldedSequences = 0;
	}

	public static FoldedProteinArchive getInstance() {
		if (singleton == null) {
			singleton = new FoldedProteinArchive();
		}
		return singleton;
	}

	public void add(String aaSeq, String proteinString, CssColor color) {
		archive.put(aaSeq, 
				new FoldedAndColoredProtein(proteinString, color));
		totalFoldedSequences++;
	}

	public boolean isInArchive(String aaSeq) {
		return archive.containsKey(aaSeq);
	}

	public FoldedAndColoredProtein getEntry(String aaSeq) {
		return archive.get(aaSeq);
	}

	public int getNumSequencesInArchive() {
		return archive.size();
	}
	
	public static int getTotalFoldedSequences() {
		return totalFoldedSequences;
	}
	
	public void saveArchiveToZipFile() {

	}

}
