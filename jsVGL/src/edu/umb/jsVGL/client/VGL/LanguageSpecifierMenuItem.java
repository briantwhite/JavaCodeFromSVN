package edu.umb.jsVGL.client.VGL;

import javax.swing.JMenuItem;

public class LanguageSpecifierMenuItem extends JMenuItem {
	
	private String name;
	private String language;
	private String country;
	
	public LanguageSpecifierMenuItem(String name, String language, String country) {
		super(name);
		this.name = name;
		this.language = language;
		this.country = country;
	}

	public String getName() {
		return name;
	}

	public String getLanguage() {
		return language;
	}

	public String getCountry() {
		return country;
	}

}
