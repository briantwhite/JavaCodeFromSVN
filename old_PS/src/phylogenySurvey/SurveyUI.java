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
import javax.swing.JButton;
import javax.swing.JPanel;

public class SurveyUI {

	public static final int LABEL_WIDTH = 150;
	public static final int LABEL_HEIGHT = 30;

	private Container masterContainer;
	private JPanel workPanel;

	// the currently selected items
	//  max of 2 at a time
	private int numSelectedItems;
	private SelectableLabel selectionA;
	private SelectableLabel selectionB;

	private ArrayList<OrganismLabel> organisms;

	// location of where clicked in the dragged item
	//  prevents jerky movement
	private int xAdjustment;
	private int yAdjustment;
	private SelectableLabel dragComponent;
	
	private JButton linkButton;
	private JButton unlinkButton;

	public SurveyUI(Container masterContainer) {
		this.masterContainer = masterContainer;
		organisms = new ArrayList<OrganismLabel>();
		numSelectedItems = 0;
		selectionA = null;
		selectionB = null;
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

				ol.setOpaque(true);
				ol.setBackground(Color.WHITE);
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

		public void mouseClicked(MouseEvent e) {
			Component c = workPanel.findComponentAt(e.getX(), e.getY());
			if (c instanceof SelectableLabel) {
				updateSelections((SelectableLabel)c);
			}
		}

		public void mouseEntered(MouseEvent e) {}

		public void mouseExited(MouseEvent e) {}

		public void mousePressed(MouseEvent e) {
			dragComponent = null;

			Component c = workPanel.findComponentAt(e.getX(), e.getY());

			if (c instanceof JPanel) return;

			if (c instanceof SelectableLabel) {
				dragComponent = (SelectableLabel)c;
				xAdjustment = dragComponent.getLocation().x - e.getX();
				yAdjustment = dragComponent.getLocation().y - e.getY();
				dragComponent.setLocation(e.getX() + xAdjustment, e.getY() + yAdjustment);
			}
		}

		public void mouseReleased(MouseEvent e) {
			dragComponent = null;
		}
	}

	private void updateSelections(SelectableLabel sl) {
		if (sl.isSelected()) {
			if (sl == selectionA) {
				selectionA.setSelected(false);
				selectionA.setBorder(BorderFactory.createLineBorder(Color.BLACK));
				selectionA = selectionB;
				selectionB = null;
			}
			if (sl == selectionB) {
				selectionB = null;
			}
			sl.setSelected(false);
			sl.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		} else {
			if (selectionA != null) {
				if (selectionB != null) {
					selectionB.setSelected(false);
					selectionB.setBorder(BorderFactory.createLineBorder(Color.BLACK));
				}
				selectionB = selectionA;
			} 
			selectionA = sl;
			sl.setSelected(true);
			sl.setBorder(BorderFactory.createLineBorder(Color.RED));			
		}
		
		numSelectedItems = 0;
		if (selectionA != null) numSelectedItems++;
		if (selectionB != null) numSelectedItems++;
		
		
	}
	
}
