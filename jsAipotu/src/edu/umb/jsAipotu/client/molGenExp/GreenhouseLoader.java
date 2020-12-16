/* loads the greenhouse file
 *   first, filling the FoldedProteinArchive with entries relevant to the greenhouse members
 *   then, reading in the organisms in the greenhouse
 */
package edu.umb.jsAipotu.client.molGenExp;

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

import edu.umb.jsAipotu.client.JsAipotu;
import edu.umb.jsAipotu.client.biochem.FoldedProteinArchive;
import edu.umb.jsAipotu.client.biochem.FoldingException;
import edu.umb.jsAipotu.client.resources.Resources;

public class GreenhouseLoader {
	
	private OrganismFactory organismFactory;
	private Greenhouse greenhouse;

	public GreenhouseLoader(Greenhouse greenhouse) {
		organismFactory = new OrganismFactory();
		this.greenhouse = greenhouse;
		
	}
	
	public void load() {
		processJSONString(Resources.INSTANCE.defaultGreenhouse().getText());
	}

	public void loadFromFile(String fileName) {
		RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, fileName);
		try {
			requestBuilder.sendRequest(null, new RequestCallback() {

				public void onResponseReceived(Request request, Response response) {
					processJSONString(response.getText());
				}
				public void onError(Request request, Throwable exception) {
					Window.alert("An error occurred while trying to load the greenhouse: " + exception.getMessage());
				}
			});
		} catch (RequestException e) {
			Window.alert("An error occurred while trying to load the greenhouse: " + e.toString());
		}
	}

	private void processJSONString(String jsonString) {
		JSONValue jsonValue = JSONParser.parseStrict(jsonString);
		JSONObject jsonObject = jsonValue.isObject();
		loadFoldedProteinArchive(jsonObject.get("foldedProteinArchive").isArray());
		loadGreenhouse(jsonObject.get("organisms").isArray());
	}

	private void loadFoldedProteinArchive(JSONArray fpaEntryArray) {
		for (int i = 0; i < fpaEntryArray.size(); i++) {
			JSONObject entryObj = fpaEntryArray.get(i).isObject();
			String aaSeq = entryObj.get("aaSeq").toString().replace("\"", "");
			String topology = entryObj.get("topology").toString().replace("\"", "");
			CssColor color = parseColorString(entryObj.get("color").toString().replace("\"", ""));
			FoldedProteinArchive.getInstance().add(aaSeq, topology, color);
		}
	}
	
	private CssColor parseColorString(String colorString) {
		String[] colorStringParts = colorString.split("/");
		return CssColor.make(Integer.parseInt(colorStringParts[0]), Integer.parseInt(colorStringParts[1]), Integer.parseInt(colorStringParts[2]));
	}
	
	private void loadGreenhouse(JSONArray organismArray) {
		for (int i = 0; i < organismArray.size(); i++) {
			JSONObject org = organismArray.get(i).isObject();
			String name = org.get("name").toString().replace("\"", "");
			String gene1 = org.get("upperDNA").toString().replace("\"", "");
			String gene2 = org.get("lowerDNA").toString().replace("\"", "");	
			try {
				Organism o = organismFactory.createOrganism(name, gene1, gene2);
				greenhouse.add(o);
			} catch (FoldingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}
