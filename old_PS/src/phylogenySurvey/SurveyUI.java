package phylogenySurvey;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class SurveyUI {

	public static final int LABEL_WIDTH = 150;
	public static final int LABEL_HEIGHT = 30;
	
	private Container masterContainer;
	private JPanel workPanel;
	
	private ArrayList<OrganismLabel> organisms;

	// location of where clicked in the dragged item
	//  prevents jerky movement
	private int xAdjustment;
	private int yAdjustment;
	private Component dragComponent;

	private JLabel testLabel;

	public SurveyUI(Container masterContainer) {
		this.masterContainer = masterContainer;
		organisms = new ArrayList<OrganismLabel>();
	}

	public void setupUI() {
		workPanel = new JPanel();
		workPanel.setLayout(null);
		workPanel.addMouseListener(new MoveLabelHandler());
		workPanel.addMouseMotionListener(new MoveLabelHandler());

		loadOrganisms();

		masterContainer.add(workPanel, BorderLayout.CENTER);
	}

	private void loadOrganisms() {
		URL listFileURL = this.getClass().getResource("/images/list.txt");
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
						parts[2]);
				organisms.add(ol);
				
				workPanel.add(ol);
				ol.setBounds(0, (LABEL_HEIGHT * row), LABEL_WIDTH, LABEL_HEIGHT);
				row++;
			}
			in.close ();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	private class MoveLabelHandler implements MouseMotionListener, MouseListener {

		public void mouseDragged(MouseEvent e) {
			if (dragComponent == null) return;
			dragComponent.setLocation(e.getX() + xAdjustment, e.getY() + yAdjustment);
		}

		public void mouseMoved(MouseEvent e) {}

		public void mouseClicked(MouseEvent e) {}

		public void mouseEntered(MouseEvent e) {}

		public void mouseExited(MouseEvent e) {}

		public void mousePressed(MouseEvent e) {
			dragComponent = null;
			Component c = workPanel.findComponentAt(e.getX(), e.getY());

			if (c instanceof JPanel) return;

			dragComponent = c;
			xAdjustment = dragComponent.getLocation().x - e.getX();
			yAdjustment = dragComponent.getLocation().y - e.getY();
			dragComponent.setLocation(e.getX() + xAdjustment, e.getY() + yAdjustment);
		}

		public void mouseReleased(MouseEvent e) {
			dragComponent = null;
		}
	}
}
