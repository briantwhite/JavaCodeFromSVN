package ModelBuilder;

import java.awt.GridLayout;
import java.util.Iterator;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jdom.Element;

import VGL.Messages;

public class LinkagePanel extends JPanel {

	private JComboBox g1g2Linked;
	private JComboBox g2g3Linked;
	private JComboBox g3g1Linked;

	public LinkagePanel(String[] characters) {
		String[] choices = new String[3];
		choices[0] = Messages.getInstance().getString("VGLII.Unknown");
		choices[1] = Messages.getInstance().getString("VGLII.Unlinked");
		choices[2] = Messages.getInstance().getString("VGLII.Linked");
		g1g2Linked = new JComboBox(choices);
		g2g3Linked = new JComboBox(choices);
		g3g1Linked = new JComboBox(choices);


		add(new JLabel(
				characters[0] + " "
				+ Messages.getInstance().getString("VGLII.And") + " "
				+ characters[1] + " "
				+ Messages.getInstance().getString("VGLII.Are")));
		add(g1g2Linked);

		if (characters.length == 3) {
			setLayout(new GridLayout(3,2));
			add(new JLabel(
					characters[1] + " "
					+ Messages.getInstance().getString("VGLII.And") + " "
					+ characters[2] + " "
					+ Messages.getInstance().getString("VGLII.Are")));
			add(g2g3Linked);

			add(new JLabel(
					characters[0] + " "
					+ Messages.getInstance().getString("VGLII.And") + " "
					+ characters[2] + " "
					+ Messages.getInstance().getString("VGLII.Are")));
			add(g3g1Linked);

		}
	}

	public void setStateFromFile(Element element) {
		List<Element> elements = element.getChildren();
		Iterator<Element> it = elements.iterator();
		while(it.hasNext()) {
			Element e = it.next();
			if (e.getName().equals("G1G2")) {
				g1g2Linked.setSelectedItem((String)e.getText());
			}
			if (e.getName().equals("G2G3")) {
				g2g3Linked.setSelectedItem((String)e.getText());
			}
			if (e.getName().equals("G3G1")) {
				g3g1Linked.setSelectedItem((String)e.getText());
			}
		}
	}

	public Element save() {
		Element lpe = new Element("LinkagePanel");

		Element e = new Element("G1G2");
		e.setText((String)g1g2Linked.getSelectedItem());
		lpe.addContent(e);

		e = new Element("G2G3");
		e.setText((String)g2g3Linked.getSelectedItem());
		lpe.addContent(e);

		e = new Element("G3G1");
		e.setText((String)g3g1Linked.getSelectedItem());
		lpe.addContent(e);

		return lpe;
	}

}
