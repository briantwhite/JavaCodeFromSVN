package edu.umb.jsVGL.client.ModelBuilder;

import java.util.ArrayList;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.XMLParser;

import edu.umb.jsVGL.client.VGL.VGLII;

public class LinkagePanel extends AbsolutePanel {

	private VGLII vglII;

	private ListBox g1g2Linked;
	private ListBox g2g3Linked;
	private ListBox g3g1Linked;

	private ListBox g1g2LinkageRelevantCage;
	private ListBox g2g3LinkageRelevantCage;
	private ListBox g3g1LinkageRelevantCage;	

	private String[] chars;

	public LinkagePanel(String[] characters, VGLII vglII) {
		this.vglII = vglII;

		g1g2LinkageRelevantCage = new ListBox();
		g2g3LinkageRelevantCage = new ListBox();
		g3g1LinkageRelevantCage = new ListBox();
		String[] cageList = vglII.getCageList();
		for (int i = 0; i < cageList.length; i++) {
			g1g2LinkageRelevantCage.addItem(cageList[i]);
			g2g3LinkageRelevantCage.addItem(cageList[i]);
			g3g1LinkageRelevantCage.addItem(cageList[i]);
		}

		this.chars = characters;

	
		g1g2Linked = new ListBox();
		g2g3Linked = new ListBox();
		g3g1Linked = new ListBox();
		g1g2Linked.addItem("Unknown");
		g2g3Linked.addItem("Unknown");
		g3g1Linked.addItem("Unknown");
		g1g2Linked.addItem("Unlinked");
		g2g3Linked.addItem("Unlinked");
		g3g1Linked.addItem("Unlinked");

		for (int i = 2; i < 51; i++) {
			String s = "Linked & RF= " + (i - 1) + "%";
			g1g2Linked.addItem(s);
			g2g3Linked.addItem(s);
			g3g1Linked.addItem(s);
		}


		add(new Label(chars[0] + " and " + chars[1] + " are "));
		add(g1g2Linked);

		add(new Label("Relevant Cages:"));
		add(g1g2LinkageRelevantCage);

		if (chars.length == 3) {
			add(new Label(chars[1] + " and " + chars[2] + " are "));
			add(g2g3Linked);
			
			add(new Label("Relevant Cages:"));
			add(g2g3LinkageRelevantCage);

			add(new Label(chars[0] + " and " + chars[2] + " are "));
			add(g3g1Linked);
			
			add(new Label("Relevant Cages:"));
			add(g3g1LinkageRelevantCage);

		}
	}
	
	/*
	 * for these, the encoding is:
	 * 	unknown = -1.0f
	 *  unlinked = 0.5f
	 *  linkage is between 0 and 0.5
	 */
	public double getG1G2LinkageChoice() {
		return getSelectedRf(g1g2Linked);
	}
	public double getG2G3LinkageChoice() {
		return getSelectedRf(g2g3Linked);
	}
	public double getG1G3LinkageChoice() {
		return getSelectedRf(g3g1Linked);
	}
	
	/*
	 * Note for cage numbers vs IDs
	 * 	cages have Ids that start with 0 (field pop)
	 *     but these are hidden from the user
	 *  cages have NUMBERS that the users see that start with 1
	 *  
	 *  so, to get the index, you have to subtract 1 from the number
	 */
	public int getG1G2LinkageRelevantCage() {
		if (g1g2LinkageRelevantCage == null) return -1;
		String[] parts = ((String)g1g2LinkageRelevantCage.getItemText(g1g2LinkageRelevantCage.getSelectedIndex())).split("Cage");
		if (parts.length != 2) return -1;
		return Integer.parseInt(parts[1].trim());
	}
	public int getG2G3LinkageRelevantCage() {
		if (g2g3LinkageRelevantCage == null) return -1;
		String[] parts = ((String)g2g3LinkageRelevantCage.getItemText(g2g3LinkageRelevantCage.getSelectedIndex())).split("Cage");
		if (parts.length != 2) return -1;
		return Integer.parseInt(parts[1].trim());
	}
	public int getG1G3LinkageRelevantCage() {
		if (g3g1LinkageRelevantCage == null) return -1;
		String[] parts = ((String)g3g1LinkageRelevantCage.getItemText(g3g1LinkageRelevantCage.getSelectedIndex())).split("Cage");
		if (parts.length != 2) return -1;
		return Integer.parseInt(parts[1].trim());
	}
	
	private double getSelectedRf(ListBox listBox) {
		String choice = (String)listBox.getItemText(listBox.getSelectedIndex());
		if (choice.equals("Unknown")) return -1.0f;
		if (choice.equals("Unlinked")) return 0.5f;
		String[] parts = choice.split("=");
		double percent = Double.parseDouble(parts[1].replaceAll("%", ""));
		return percent/100.0f;
	}


	public void setStateFromFile(Element element) {
//		List<Element> elements = element.getChildren();
//		Iterator<Element> it = elements.iterator();
//		while(it.hasNext()) {
//			Element e = it.next();
//			if (e.getName().equals("G1G2")) {
//				g1g2Linked.setSelectedItem((String)e.getText());
//			}
//			if (e.getName().equals("G1G2Evidence")) {
//				g1g2LinkageRelevantCage.setSelectedItem((String)e.getText());
//			}
//			
//			if (e.getName().equals("G2G3")) {
//				g2g3Linked.setSelectedItem((String)e.getText());
//			}
//			if (e.getName().equals("G2G3Evidence")) {
//				g2g3LinkageRelevantCage.setSelectedItem((String)e.getText());
//			}
//
//			if (e.getName().equals("G3G1")) {
//				g3g1Linked.setSelectedItem((String)e.getText());
//			}
//			if (e.getName().equals("G3G1Evidence")) {
//				g3g1LinkageRelevantCage.setSelectedItem((String)e.getText());
//			}
//
//		}
	}

	public Element save() {
		Document d = XMLParser.createDocument();

		Element lpe = d.createElement("LinkagePanel");

		Element e = d.createElement("G1G2");
		e.appendChild(d.createTextNode(((String)g1g2Linked.getItemText(g1g2Linked.getSelectedIndex()))));
		lpe.appendChild(e);	
		e = d.createElement("G1G2Evidence");
		e.appendChild(d.createTextNode(((String)g1g2LinkageRelevantCage.getItemText(g1g2LinkageRelevantCage.getSelectedIndex()))));
		lpe.appendChild(e);

		e = d.createElement("G2G3");
		e.appendChild(d.createTextNode(((String)g2g3Linked.getItemText(g2g3Linked.getSelectedIndex()))));
		lpe.appendChild(e);
		e = d.createElement("G2G3Evidence");
		e.appendChild(d.createTextNode(((String)g2g3Linked.getItemText(g2g3LinkageRelevantCage.getSelectedIndex()))));
		lpe.appendChild(e);

		e = d.createElement("G3G1");
		e.appendChild(d.createTextNode(((String)g3g1Linked.getItemText(g3g1Linked.getSelectedIndex()))));
		lpe.appendChild(e);
		e = d.createElement("G3G1Evidence");
		e.appendChild(d.createTextNode(((String)g3g1LinkageRelevantCage.getItemText(g3g1LinkageRelevantCage.getSelectedIndex()))));
		lpe.appendChild(e);

		return lpe;
	}

	
	public void updateCageChoices(int nextCageId) {
		g1g2LinkageRelevantCage.addItem("Cage " + nextCageId);
		g2g3LinkageRelevantCage.addItem("Cage " + nextCageId);
		g3g1LinkageRelevantCage.addItem("Cage " + nextCageId);
	}
	
	public ArrayList<Integer> getRelevantCages() {
		ArrayList<Integer> result = new ArrayList<Integer>();
		result.add(g1g2LinkageRelevantCage.getSelectedIndex());
		result.add(g2g3LinkageRelevantCage.getSelectedIndex());
		result.add(g3g1LinkageRelevantCage.getSelectedIndex());		
		return result;
	}

}
