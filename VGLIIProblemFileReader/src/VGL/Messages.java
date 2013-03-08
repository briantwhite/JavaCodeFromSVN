package VGL;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import GeneticModels.Allele;
import GeneticModels.Trait;

public class Messages {
	public static final String BUNDLE_NAME = "VGL.messages"; //$NON-NLS-1$

	private static ResourceBundle RESOURCE_BUNDLE = ResourceBundle
			.getBundle(BUNDLE_NAME);

	private Messages() {
	}
	
	public static void updateResourceBundle() {
		RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME, Locale.getDefault());
	}
	
	//translate red-eyes/six-antennae/bent-body as appropriate
	public static String translateLongPhenotypeName(String phenoName) {
		StringBuffer b = new StringBuffer();
		String[] parts = phenoName.split("/");
		for (int i = 0; i < parts.length; i++) {
			b.append(translatePhenotypeName(parts[i]));
			b.append("/");
		}
		b.deleteCharAt(b.length() - 1);
		return b.toString();
	}
	
	//translate red-eyes as appropriate
	public static String translatePhenotypeName(String phenoString) {
		//first, see if it doesn't have a dash
		//  if so, just translate it as one word
		if (phenoString.indexOf("-") == -1) {
			return getString("VGLII." + phenoString);
		}
		
		//then, parse the two parts on either side of the dash
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
	
	public static String getTranslatedAlleleName(Allele a) {
		StringBuffer b = new StringBuffer();
		if (getString("VGLII.NounAdjective").equals("Y")) {
			b.append(Messages.getString(
					"VGLII." + a.getTrait().getBodyPart().toString())
					+ "-"
					+ Messages.getString(
							"VGLII." +a.getTrait().getTraitName().toString()));
		} else {
			b.append(Messages.getString(
					"VGLII." + a.getTrait().getTraitName().toString())
					+ "-"
					+ Messages.getString(
							"VGLII." +a.getTrait().getBodyPart().toString()));
		}
		return b.toString();
	}
	
	public static String getTranslatedTraitName(Trait t) {
		StringBuffer b = new StringBuffer();
		if (getString("VGLII.NounAdjective").equals("Y")) {
			b.append(Messages.getString(
					"VGLII." + t.getType().toString())
					+ "-"
					+ Messages.getString(
							"VGLII." + t.getBodyPart().toString()));
		} else {
			b.append(Messages.getString(
					"VGLII." + t.getBodyPart().toString())
					+ " "
					+ Messages.getString(
							"VGLII." + t.getType().toString()));
		}
		return b.toString();		
	}
	
	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}

}
