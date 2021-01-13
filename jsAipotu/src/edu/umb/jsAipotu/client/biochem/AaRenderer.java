package edu.umb.jsAipotu.client.biochem;

import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.text.shared.Renderer;

public class AaRenderer extends AbstractRenderer<String> {
	
	private static AaRenderer instance;
	
	public static Renderer<String> getInstance() {
		if (instance == null) {
			instance = new AaRenderer();
		}
		return instance;
	}

	protected AaRenderer() {
		
	}

	public String render(String object) {
		return "hello";
	}

}
