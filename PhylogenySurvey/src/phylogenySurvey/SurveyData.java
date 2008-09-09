package phylogenySurvey;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class SurveyData {
	
	private ArrayList<SelectableObject> items;
	private ArrayList<Link> links;
	
	private static SurveyData instance;
	
	private SurveyData() {
		items = new ArrayList<SelectableObject>();
		links = new ArrayList<Link>();
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
	
	public ArrayList<Link> getLinks() {
		return links;
	}

}
