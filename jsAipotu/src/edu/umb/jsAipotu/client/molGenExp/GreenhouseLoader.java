/* loads the greenhouse file
 *   first, filling the FoldedProteinArchive with entries relevant to the greenhouse members
 *   then, reading in the organisms in the greenhouse
 */
package edu.umb.jsAipotu.client.molGenExp;

import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;

import edu.umb.jsAipotu.client.JsAipotu;
import edu.umb.jsAipotu.client.molBiol.GeneExpresser;
import edu.umb.jsAipotu.client.resources.Resources;

public class GreenhouseLoader {
	
//	private OrganismFactory organismFactory;
	private Greenhouse greenhouse;
	private GeneExpresser geneExpresser;

	public GreenhouseLoader(Greenhouse greenhouse) {
//		organismFactory = new OrganismFactory();
		geneExpresser = new GeneExpresser();
		this.greenhouse = greenhouse;
		
	}
	
	public void load() {
		JSONValue jsonValue = JSONParser.parseStrict(Resources.INSTANCE.defaultGreenhouse().getText());
		JsAipotu.consoleLog(jsonValue.toString());
	}

//	public void loadFromFile(String fileName) {
//		RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, fileName);
//		try {
//			requestBuilder.sendRequest(null, new RequestCallback() {
//
//				public void onResponseReceived(Request request, Response response) {
//					JSONValue jsonValue = JSONParser.parseStrict(response.getText());
//					JSONObject jsonObject = jsonValue.isObject();
//					loadFoldedProteinArchive(jsonObject.get("foldedProteinArchive").isArray());
//					loadGreenhouse(jsonObject.get("organisms").isArray());
//				}
//				public void onError(Request request, Throwable exception) {
//					Window.alert("An error occurred while trying to load the greenhouse: " + exception.getMessage());
//				}
//			});
//		} catch (RequestException e) {
//			Window.alert("An error occurred while trying to load the greenhouse: " + e.toString());
//		}
//	}

//	private void loadFoldedProteinArchive(JSONArray fpaEntryArray) {
//		for (int i = 0; i < fpaEntryArray.size(); i++) {
//			JSONObject entryObj = fpaEntryArray.get(i).isObject();
//			String aaSeq = entryObj.get("aaSeq").toString().replace("\"", "");
//			String topology = entryObj.get("topology").toString().replace("\"", "");
//			CssColor color = parseColorString(entryObj.get("color").toString().replace("\"", ""));
//			FoldedProteinArchive.getInstance().add(aaSeq, topology, color);
//		}
//	}
	
	private CssColor parseColorString(String colorString) {
		String[] colorStringParts = colorString.split("/");
		return CssColor.make(Integer.parseInt(colorStringParts[0]), Integer.parseInt(colorStringParts[1]), Integer.parseInt(colorStringParts[2]));
	}
	
//	private void loadGreenhouse(JSONArray organismArray) {
//		for (int i = 0; i < organismArray.size(); i++) {
//			JSONObject org = organismArray.get(i).isObject();
//			String name = org.get("name").toString().replace("\"", "");
//			String gene1 = org.get("upperDNA").toString().replace("\"", "");
//			String gene2 = org.get("lowerDNA").toString().replace("\"", "");						
//		}
//
//	}
}
