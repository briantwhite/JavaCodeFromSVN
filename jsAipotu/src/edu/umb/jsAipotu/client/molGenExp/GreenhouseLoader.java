package edu.umb.jsAipotu.client.molGenExp;

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

import edu.umb.jsAipotu.client.molBiol.ExpressedGene;
import edu.umb.jsAipotu.client.molBiol.GeneExpresser;

public class GreenhouseLoader {

//	private OrganismFactory organismFactory;
	private Greenhouse greenhouse;
	private GeneExpresser geneExpresser;

	public GreenhouseLoader(Greenhouse greenhouse) {
//		organismFactory = new OrganismFactory();
		geneExpresser = new GeneExpresser();
		this.greenhouse = greenhouse;
		
	}

	public void load(String fileName) {
		RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, fileName);
		try {
			requestBuilder.sendRequest(null, new RequestCallback() {

				public void onResponseReceived(Request request, Response response) {
					JSONValue jsonValue = JSONParser.parseStrict(response.getText());
					JSONObject jsonObject = jsonValue.isObject();
					JSONArray organismArray = jsonObject.get("organisms").isArray();
					for (int i = 0; i < organismArray.size(); i++) {
						JSONObject org = organismArray.get(i).isObject();
						String name = org.get("name").toString().replace("\"", "");
						String gene1 = org.get("upperDNA").toString().replace("\"", "");
						String gene2 = org.get("lowerDNA").toString().replace("\"", "");
						String protein1 = org.get("upperProtein1").toString().replace("\"", "");
						String protein2 = org.get("lowerProtein1").toString().replace("\"", "");
						
						// sanity checks - figure out proteins from DNA and be sure they match
						ExpressedGene eg1 = geneExpresser.expressGene(gene1, -1);
						convert3letterTo1Letter(eg1.getProtein());
						greenhouse.add(new Organism(name));
					}
				}

				public void onError(Request request, Throwable exception) {
					Window.alert("An error occurred while trying to load the greenhouse: " + exception.getMessage());
				}
			});
		} catch (RequestException e) {
			Window.alert("An error occurred while trying to load the greenhouse: " + e.toString());
		}
	}

	private void convert3letterTo1Letter(String p3) {
		StringBuffer b = new StringBuffer();
		for (int i = 0; i < p3.length(); i = i + 3) {
			String aa3 = p3.substring(i, i + 2);
			b.append(aa3 + "\n");
		}
		Window.alert(b.toString());
	}
}
