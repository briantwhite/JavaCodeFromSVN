package ModelBuilder;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jdom.Element;

import VGL.Messages;
import VGL.VGLII;

public class LinkagePanel extends JPanel {

	private VGLII vglII;

	private JComboBox g1g2Linked;
	private JComboBox g2g3Linked;
	private JComboBox g3g1Linked;

	private JComboBox g1g2LinkageRelevantCage;
	private JComboBox g2g3LinkageRelevantCage;
	private JComboBox g3g1LinkageRelevantCage;	

	private String[] chars;

	public LinkagePanel(String[] characters, VGLII vglII) {
		this.vglII = vglII;

		g1g2LinkageRelevantCage = new JComboBox(vglII.getCageList());
		g2g3LinkageRelevantCage = new JComboBox(vglII.getCageList());
		g3g1LinkageRelevantCage = new JComboBox(vglII.getCageList());

		String[] choices = new String[51];
		this.chars = characters;
		choices[0] = Messages.getInstance().getString("VGLII.Unknown");
		choices[1] = Messages.getInstance().getString("VGLII.Unlinked");
		for (int i = 2; i < 51; i++) {
			choices[i] = Messages.getInstance().getString("VGLII.Linked") 
			+ " & RF= " + (i - 1) + "%";
		}
		g1g2Linked = new JComboBox(choices);
		g2g3Linked = new JComboBox(choices);
		g3g1Linked = new JComboBox(choices);


		add(new JLabel(
				chars[0] + " "
				+ Messages.getInstance().getString("VGLII.And") + " "
				+ chars[1] + " "
				+ Messages.getInstance().getString("VGLII.Are")));
		add(g1g2Linked);

		add(new JLabel(Messages.getInstance().getString("VGLII.RelevantCages")));
		add(g1g2LinkageRelevantCage);

		if (chars.length == 3) {
			setLayout(new GridLayout(3,2));
			add(new JLabel(
					chars[1] + " "
					+ Messages.getInstance().getString("VGLII.And") + " "
					+ chars[2] + " "
					+ Messages.getInstance().getString("VGLII.Are")));
			add(g2g3Linked);
			add(new JLabel(Messages.getInstance().getString("VGLII.RelevantCages")));
			add(g2g3LinkageRelevantCage);

			add(new JLabel(
					chars[0] + " "
					+ Messages.getInstance().getString("VGLII.And") + " "
					+ chars[2] + " "
					+ Messages.getInstance().getString("VGLII.Are")));
			add(g3g1Linked);
			add(new JLabel(Messages.getInstance().getString("VGLII.RelevantCages")));
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
		String[] parts = ((String)g1g2LinkageRelevantCage.getSelectedItem()).split("Cage");
		if (parts.length != 2) return -1;
		return Integer.parseInt(parts[1].trim());
	}
	public int getG2G3LinkageRelevantCage() {
		if (g2g3LinkageRelevantCage == null) return -1;
		String[] parts = ((String)g2g3LinkageRelevantCage.getSelectedItem()).split("Cage");
		if (parts.length != 2) return -1;
		return Integer.parseInt(parts[1].trim());
	}
	public int getG1G3LinkageRelevantCage() {
		if (g3g1LinkageRelevantCage == null) return -1;
		String[] parts = ((String)g3g1LinkageRelevantCage.getSelectedItem()).split("Cage");
		if (parts.length != 2) return -1;
		return Integer.parseInt(parts[1].trim());
	}
	
	private double getSelectedRf(JComboBox comboBox) {
		if (comboBox.getSelectedItem().equals(Messages.getInstance().getString("VGLII.Unknown"))) return -1.0f;
		if (comboBox.getSelectedItem().equals(Messages.getInstance().getString("VGLII.Unlinked"))) return 0.5f;
		String choice = (String)comboBox.getSelectedItem();
		String[] parts = choice.split("=");
		double percent = Double.parseDouble(parts[1].replaceAll("%", ""));
		return percent/100.0f;
	}


	public void setStateFromFile(Element element) {
		List<Element> elements = element.getChildren();
		Iterator<Element> it = elements.iterator();
		while(it.hasNext()) {
			Element e = it.next();
			if (e.getName().equals("G1G2")) {
				g1g2Linked.setSelectedItem((String)e.getText());
			}
			if (e.getName().equals("G1G2Evidence")) {
				g1g2LinkageRelevantCage.setSelectedItem((String)e.getText());
			}
			
			if (e.getName().equals("G2G3")) {
				g2g3Linked.setSelectedItem((String)e.getText());
			}
			if (e.getName().equals("G2G3Evidence")) {
				g2g3LinkageRelevantCage.setSelectedItem((String)e.getText());
			}

			if (e.getName().equals("G3G1")) {
				g3g1Linked.setSelectedItem((String)e.getText());
			}
			if (e.getName().equals("G3G1Evidence")) {
				g3g1LinkageRelevantCage.setSelectedItem((String)e.getText());
			}

		}
	}

	public Element save() {
		Element lpe = new Element("LinkagePanel");

		Element e = new Element("G1G2");
		e.setText((String)g1g2Linked.getSelectedItem());
		lpe.addContent(e);	
		e = new Element("G1G2Evidence");
		e.setText((String)g1g2LinkageRelevantCage.getSelectedItem());
		lpe.addContent(e);

		e = new Element("G2G3");
		e.setText((String)g2g3Linked.getSelectedItem());
		lpe.addContent(e);
		e = new Element("G2G3Evidence");
		e.setText((String)g2g3LinkageRelevantCage.getSelectedItem());
		lpe.addContent(e);

		e = new Element("G3G1");
		e.setText((String)g3g1Linked.getSelectedItem());
		lpe.addContent(e);
		e = new Element("G3G1Evidence");
		e.setText((String)g3g1LinkageRelevantCage.getSelectedItem());
		lpe.addContent(e);

		return lpe;
	}

	public String getAsHtml(boolean isForGrader) {
		StringBuffer b = new StringBuffer();
		b.append("<b>");
		b.append(Messages.getInstance().getString("VGLII.Linkage"));
		b.append("</b><br><ul>");
		b.append("<li>" + chars[0] + " "
				+ Messages.getInstance().getString("VGLII.And") + " "
				+ chars[1] + " "
				+ Messages.getInstance().getString("VGLII.Are") + " ");
		b.append((String)g1g2Linked.getSelectedItem() + "</li>");
		b.append("<ul><li>" 
				+ Messages.getInstance().getString("VGLII.RelevantCages")
				+ " "
				+ g1g2LinkageRelevantCage.getSelectedItem()
				+ "</li></ul>");
	
		if (chars.length == 3) {
			b.append("<li>" + chars[1] + " "
					+ Messages.getInstance().getString("VGLII.And") + " "
					+ chars[2] + " "
					+ Messages.getInstance().getString("VGLII.Are") + " ");
			b.append((String)g2g3Linked.getSelectedItem() + "</li>");
			b.append("<ul><li>" 
					+ Messages.getInstance().getString("VGLII.RelevantCages")
					+ " "
					+ g2g3LinkageRelevantCage.getSelectedItem()
					+ "</li></ul>");

			b.append("<li>" + chars[0] + " "
					+ Messages.getInstance().getString("VGLII.And") + " "
					+ chars[2] + " "
					+ Messages.getInstance().getString("VGLII.Are") + " ");
			b.append((String)g3g1Linked.getSelectedItem() + "</li>");
			b.append("<ul><li>" 
					+ Messages.getInstance().getString("VGLII.RelevantCages")
					+ " "
					+ g3g1LinkageRelevantCage.getSelectedItem()
					+ "</li></ul>");
		}
		b.append("</ul>");
		return b.toString();
	}
	
	public void updateCageChoices(int nextCageId) {
		g1g2LinkageRelevantCage.addItem(Messages.getInstance().getString("VGLII.Cage") + " " + nextCageId);
		g2g3LinkageRelevantCage.addItem(Messages.getInstance().getString("VGLII.Cage") + " " + nextCageId);
		g3g1LinkageRelevantCage.addItem(Messages.getInstance().getString("VGLII.Cage") + " " + nextCageId);
		revalidate();
	}
	
	public ArrayList<Integer> getRelevantCages() {
		ArrayList<Integer> result = new ArrayList<Integer>();
		result.add(g1g2LinkageRelevantCage.getSelectedIndex());
		result.add(g2g3LinkageRelevantCage.getSelectedIndex());
		result.add(g3g1LinkageRelevantCage.getSelectedIndex());		
		return result;
	}

}
