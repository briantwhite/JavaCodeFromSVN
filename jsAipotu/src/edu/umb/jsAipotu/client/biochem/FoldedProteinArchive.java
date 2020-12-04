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
	private static final String greenhouseFileName = "default.greenhouse";
	private static int totalFoldedSequences;

	private FoldedProteinArchive() {
		archive = new HashMap<String, FoldedAndColoredProtein>();
		totalFoldedSequences = 0;
		loadArchiveFromGreenhouseFile(greenhouseFileName);
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

	private void loadArchiveFromGreenhouseFile(String fileName) {
		RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, fileName);
		try {
			requestBuilder.sendRequest(null, new RequestCallback() {
				public void onResponseReceived(Request request, Response response) {
					JSONValue jsonValue = JSONParser.parseStrict(response.getText());
					JSONObject jsonObject = jsonValue.isObject();
					JSONArray fpaEntryArray = jsonObject.get("foldedProteinArchive").isArray();
					for (int i = 0; i < fpaEntryArray.size(); i++) {
						JSONObject entry = fpaEntryArray.get(i).isObject();
						String aaSeq = entry.get("aaSeq").toString().replace("\"", "");
						String topology = entry.get("topology").toString().replace("\"", "");
						String colorString = entry.get("color").toString().replace("\"", "");
						String[] colorStringParts = colorString.split("/");
						CssColor color = CssColor.make(
								Integer.parseInt(colorStringParts[0]),
								Integer.parseInt(colorStringParts[1]),
								Integer.parseInt(colorStringParts[2]));
						add(aaSeq, topology, color);
					}
				}

				public void onError(Request request, Throwable exception) {
					Window.alert("An error occurred while trying to load the FPA in the Greenhouse: " + exception.getMessage());
				}
			});
		} catch (RequestException e) {
			Window.alert("An error occurred while trying to load the FPA in the Greenhouse: " + e.toString());
		}
	}
}
