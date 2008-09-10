package phylogenySurvey;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class SurveyData {
	
	private ArrayList<SelectableObject> items;
	private ArrayList<Link> links;
	
	private ArrayList<String> historyList;
	
	private static SurveyData instance;
	
	private SurveyData() {
		items = new ArrayList<SelectableObject>();
		links = new ArrayList<Link>();
		historyList = new ArrayList<String>();
	}
	
	public static SurveyData getInstance() {
		if (instance == null) {
			instance = new SurveyData();
		}
		return instance;
	}
	
	public void loadOrganisms(URL listFileURL, DrawingPanel workPanel) {
		String line = "";
		int row = 0;
		try {
			InputStream in = listFileURL.openStream();
			BufferedReader dis =  new BufferedReader (new InputStreamReader (in));
			while ((line = dis.readLine ()) != null) {
				String[] parts = line.split(",");
				OrganismLabel ol = new OrganismLabel(
						parts[0],
						new ImageIcon(this.getClass().getResource("/images/" + parts[1])),
						parts[1],
						parts[2]);
				items.add(ol);
				workPanel.add(ol);
				ol.setOpaque(true);
				ol.setBackground(Color.WHITE);
				ol.setBounds(0, (SurveyUI.LABEL_HEIGHT * row), 
						SurveyUI.LABEL_WIDTH, SurveyUI.LABEL_HEIGHT);
				row++;
			}
			in.close ();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void add(Node n, DrawingPanel workPanel) {
		items.add(n);
		workPanel.add(n);
	}
	
	public void delete(Node n, DrawingPanel workPanel) {
		//remake node list without any links
		//  that include this node
		ArrayList<Link> newLinks = new ArrayList<Link>();
		Iterator<Link> it = links.iterator();
		while (it.hasNext()) {
			Link l = it.next();
			if ((l.getOneLabel() != n) && (l.getOtherLabel() != n)) {
				newLinks.add(l);
			}
		}
		links = newLinks;
		workPanel.remove(n);
		items.remove(n);
	}
	
	public void add(TextLabel tl, DrawingPanel workPanel) {
		items.add(tl);
		workPanel.add(tl);
		tl.setBounds(500, 
				500, 
				(workPanel.getFontMetrics(workPanel.getFont())).stringWidth(tl.getText()) + 5, 
				SurveyUI.LABEL_HEIGHT);
	}
	
	public void delete(TextLabel tl, DrawingPanel workPanel) {
		items.remove(tl);
		workPanel.remove(tl);
	}
	
	public void add(Link l) {
		links.add(l);
	}
	
	public void deleteLink(SelectableLinkableObject a, SelectableLinkableObject b) {
		Iterator<Link> it = links.iterator();
		while (it.hasNext()) {
			Link l = it.next();
			if ( ( (l.getOneLabel() == a) && (l.getOtherLabel() == b) )  ||
					( (l.getOneLabel() == b) && (l.getOtherLabel() == a))	
			) {
				links.remove(l);
				return;
			}
		}
		JOptionPane.showMessageDialog(null, 
				"Please select two linked objects to un-link", 
				"Nothing to un-link", 
				JOptionPane.WARNING_MESSAGE);

	}
	
	public void split(SelectableLinkableObject a, 
			SelectableLinkableObject b, 
			DrawingPanel workPanel) {
		Iterator<Link> it = links.iterator();
		while(it.hasNext()) {
			Link l = it.next();
			if (
					((l.getOneLabel() == a) && (l.getOtherLabel() == b)) ||
					((l.getOneLabel() == a) && (l.getOtherLabel() == b))
			) {
				links.remove(l);
				SelectableLinkableObject slo1 = l.getOneLabel();
				SelectableLinkableObject slo2 = l.getOtherLabel();
				int x = (slo1.getCenter().x + slo2.getCenter().x)/2;
				int y = (slo1.getCenter().y + slo2.getCenter().y)/2;
				Node node = new Node(new ImageIcon(this.getClass().getResource("/images/node.gif" )));
				items.add(node);
				workPanel.add(node);
				node.setBounds(x, y, 12, 12);
				links.add(new Link(a, node));
				links.add(new Link(b, node));
				a.setSelected(false);
				a.setBorder(BorderFactory.createLineBorder(Color.BLACK));
				b.setSelected(false);
				b.setBorder(BorderFactory.createLineBorder(Color.BLACK));				
				return;
			}
		}
	}
	
	public ArrayList<Link> getLinks() {
		return links;
	}
	
	public String getState() {
		Element root = new Element("State");

		Element itemEl = new Element("Items");
		Iterator<SelectableObject> itemIt = items.iterator();
		while (itemIt.hasNext()) {
			SelectableObject item = itemIt.next();
			itemEl.addContent(item.save());
		}
		root.addContent(itemEl);

		Element linkEl = new Element("Links");
		Iterator<Link> linkIt = links.iterator();
		while (linkIt.hasNext()) {
			Link link = linkIt.next();
			linkEl.addContent(link.save());
		}
		root.addContent(linkEl);
		XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());
		return out.outputString(new Document(root));
	}
	
	public void saveStateToHistoryList() {
		historyList.add(getState());
	}
	
	public String undo() {
		String state = historyList.get(historyList.size() - 1);
		historyList.remove(historyList.size() - 1);
		return state;
	}

}
