package Problems;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import Requirements.RequirementFactory;


public class ProblemFileReader {
	
	public static HashSet<Problem> readProblemFile(File problemFile) {
		
		HashSet<Problem> result = new HashSet<Problem>();
		
		if (problemFile == null) return null;
		if (!problemFile.exists()) return null;
		
		Document doc = null;
		SAXBuilder builder = new SAXBuilder();
		try {
			doc = builder.build(problemFile);
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		if (doc != null) {
			Iterator<Element> elIt = doc.getRootElement().getChildren().iterator();
			while (elIt.hasNext()) {
				Element e = elIt.next();
				if (e.getName().equals("Problem")) result.add(processProblem(e)); 
			}
		}
		return result;
	}
	
	private static Problem processProblem(Element problemElement) {
	
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
}
