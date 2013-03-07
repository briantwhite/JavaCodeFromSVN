package edu.umb.jsVGL.client.VGL;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import edu.umb.jsVGL.client.GeneticModels.Allele;
import edu.umb.jsVGL.client.GeneticModels.Trait;

public class Messages {
	public final String BUNDLE_NAME = "VGL.messages"; //$NON-NLS-1$

	private ResourceBundle RESOURCE_BUNDLE;

	private static Messages instance;

	private Messages() {
		try {
			RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);
		} catch (MissingResourceException e) {
			Locale.setDefault(Locale.US);
			RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);
		}
	}

	public static Messages getInstance() {
		if (instance == null) {
			instance = new Messages();
		}
		return instance;
	}

	public void updateResourceBundle() {
		RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME, Locale.getDefault());
	}

	//translate Male/red-eyes/six-antennae/bent-body as appropriate
	public String translateLongPhenotypeName(String phenoName) {
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
	public String translatePhenotypeName(String phenoString) {
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

	public String getTranslatedAlleleName(Allele a) {
		StringBuffer b = new StringBuffer();
		if (getString("VGLII.NounAdjective").equals("Y")) {
			b.append(Messages.getInstance().getString(
					"VGLII." + a.getTrait().getBodyPart().toString())
					+ "-"
					+ Messages.getInstance().getString(
							"VGLII." +a.getTrait().getTraitName().toString()));
		} else {
			b.append(Messages.getInstance().getString(
					"VGLII." + a.getTrait().getTraitName().toString())
					+ "-"
					+ Messages.getInstance().getString(
							"VGLII." +a.getTrait().getBodyPart().toString()));
		}
		return b.toString();
	}

	public String getTranslatedCharacterName(Trait t) {
		StringBuffer b = new StringBuffer();
		if (getString("VGLII.NounAdjective").equals("Y")) {
			b.append(Messages.getInstance().getString(
					"VGLII." + t.getType().toString())
					+ "-"
					+ Messages.getInstance().getString(
							"VGLII." + t.getBodyPart().toString()));
		} else {
			b.append(Messages.getInstance().getString(
					"VGLII." + t.getBodyPart().toString())
					+ " "
					+ Messages.getInstance().getString(
							"VGLII." + t.getType().toString()));
		}
		return b.toString();		
	}

	// change "Red" to "Rouge"
	public String getTranslatedShortTraitName(String s) {
		return Messages.getInstance().getString("VGLII." + s);
	}

	public String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}

}
