package VGL;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Messages {
	public static final String BUNDLE_NAME = "VGL.messages"; //$NON-NLS-1$

	private static ResourceBundle RESOURCE_BUNDLE = ResourceBundle
			.getBundle(BUNDLE_NAME);

	private Messages() {
	}
	
	public static void updateResourceBundle() {
		RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME, Locale.getDefault());
	}
	
	public static String translatePhenotypeName(String phenoString) {
		//first, parse the two parts on either side of the dash
		// it is initially adjective-noun (eg. "red-eyes")
		String[] parts = phenoString.split("-");
		
		//translate the parts
		String adjective = getString("VGLII." + parts[0]);
		String noun = getString("VGLII." + parts[1]);
		
		// then, put them in the right order
		if ((RESOURCE_BUNDLE.getString("VGLII.NounAdjective")).equals("Y")) {
			// for example, french
			return noun + "-" + adjective;
		} else {
			// for example, english
			return adjective + "-" + noun;
		}
	}
	
	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}

}
