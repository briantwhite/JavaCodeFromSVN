package edu.umb.bio.genex.client.GX;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;

import com.google.gwt.user.client.Element;
import com.google.gwt.xml.client.Document;

import edu.umb.bio.genex.Requirements.RequirementFactory;
import edu.umb.bio.genex.client.Problems.Problem;

public class ConfigurationFileReader {
	
	private HashSet<Problem> problems;
	private GenexParams params;
	
	public ConfigurationFileReader(File configFile) {
		
		problems = new HashSet<Problem>();
		params = new GenexParams();
		
		if (configFile == null) return;
		if (!configFile.exists()) return;
		
		Document doc = null;
		SAXBuilder builder = new SAXBuilder();
		try {
			doc = builder.build(configFile);
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		if (doc != null) {
			Iterator<Element> elIt = doc.getRootElement().getChildren().iterator();
			while (elIt.hasNext()) {
				Element e = elIt.next();
				if (e.getName().equals("Problem")) problems.add(processProblem(e)); 
				if (e.getName().equals("Parameters")) processParams(params, e);
			}
		}
	}
	
	private Problem processProblem(Element problemElement) {
	
		Problem p = new Problem();
		
		Iterator<Element> elIt = problemElement.getChildren().iterator();
		while (elIt.hasNext()) {
			Element e = elIt.next();
			if (e.getName().equals("Name")) p.setName(e.getTextTrim());
			if (e.getName().equals("Number")) p.setNumber(Integer.parseInt(e.getTextTrim()));
			if (e.getName().equals("Description")) p.setDescription(e.getTextTrim());
			if (e.getName().equals("Requirement")) p.addRequirement(RequirementFactory.build(e));
		}
		
		return p;
	}
	
	private void processParams(GenexParams defaultParams, Element newParams) {
		Iterator<Element> elIt = newParams.getChildren().iterator();
		while (elIt.hasNext()) {
			Element e = elIt.next();
			if (e.getName().equals("DNA_SEQUENCE")) defaultParams.setDefaultDNA(onlyACGT(e.getTextTrim()));
			if (e.getName().equals("PROMOTER")) defaultParams.setPromoterSequence(onlyACGT(e.getTextTrim()));
			if (e.getName().equals("PROMOTER_SPACING")) defaultParams.setPromoterSpacing(Integer.parseInt(e.getTextTrim()));
			if (e.getName().equals("TERMINATOR")) defaultParams.setTerminatorSequence(onlyACGT(e.getTextTrim()));
			if (e.getName().equals("INTRON_START")) defaultParams.setIntronStartSequence(onlyAGCU(e.getTextTrim()));
			if (e.getName().equals("INTRON_END")) defaultParams.setIntronEndSequence(onlyAGCU(e.getTextTrim()));
			if (e.getName().equals("POLY_A_TAIL")) defaultParams.setPolyATail(e.getTextTrim());
			if (e.getName().equals("PRINTING")) defaultParams.setAllowPrinting(Boolean.parseBoolean(e.getTextTrim()));
		}
		
	}
	
	private String onlyACGT(String s) {
		return s.replaceAll("[^AGCT]","");
	}
	
	private String onlyAGCU(String s) {
		return s.replaceAll("[^AGCU]","");
	}
	
	public HashSet<Problem> getProblems() {
		return problems;
	}
	
	public GenexParams getParams() {
		return params;
	}

}
