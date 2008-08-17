package phylogenySurvey;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class SurveyUI {

	public static final int LABEL_WIDTH = 120;
	public static final int LABEL_HEIGHT = 30;

	private Container masterContainer;
	private DrawingPanel workPanel;

	// the currently selected items
	//  max of 2 at a time
	private int numSelectedItems;
	private SelectableLinkableObject selectionA;
	private SelectableLinkableObject selectionB;
	private SelectableObject selectionOnly;    // can only have one of these selected
	// it's a plain text label
	// this is to prevent linking to a text label

	private ArrayList<SelectableObject> items;
	private ArrayList<Link> links;

	// location of where clicked in the dragged item
	//  prevents jerky movement
	private int xAdjustment;
	private int yAdjustment;
	private SelectableObject dragComponent;

	private JButton linkButton;
	private JButton unlinkButton;
	private JButton labelButton;
	private JButton deleteButton;
	private JButton splitButton;
	private JButton printButton;
	private JButton outputButton;

	public SurveyUI(Container masterContainer) {
		this.masterContainer = masterContainer;
		items = new ArrayList<SelectableObject>();
		links = new ArrayList<Link>();
		numSelectedItems = 0;
		selectionA = null;
		selectionB = null;
		selectionOnly = null;
	}

	public void setupUI() {
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));

		linkButton = new JButton("Link");
		linkButton.setToolTipText("Link two selected items");
		linkButton.setEnabled(false);
		buttonPanel.add(linkButton);

		unlinkButton = new JButton("Unlink");
		unlinkButton.setToolTipText("Un-Link two selected items");
		unlinkButton.setEnabled(false);
		buttonPanel.add(unlinkButton);

		labelButton = new JButton("Label");
		buttonPanel.add(labelButton);
		labelButton.setToolTipText("Add a label");

		deleteButton = new JButton("Delete");
		deleteButton.setEnabled(false);
		buttonPanel.add(deleteButton);
		deleteButton.setToolTipText("Delete selected Node or Label");

		splitButton = new JButton("Split");
		splitButton.setToolTipText("Insert a Node between two connected items");
		splitButton.setEnabled(false);
		buttonPanel.add(splitButton);

		printButton = new JButton("Print");
		buttonPanel.add(printButton);
		
		outputButton = new JButton("output");
		buttonPanel.add(outputButton);

		masterContainer.add(buttonPanel, BorderLayout.NORTH);

		workPanel = new DrawingPanel(this);
		workPanel.setLayout(null);
		workPanel.addMouseListener(new MoveLabelHandler());
		workPanel.addMouseMotionListener(new MoveLabelHandler());

		loadOrganisms();

		masterContainer.add(workPanel, BorderLayout.CENTER);

		linkButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				link();
			}
		});

		unlinkButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				unlink();
			}
		});

		labelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addLabel();
			}
		});

		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deleteSelected();
			}
		});

		splitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				split();
			}
		});

		printButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				printSurvey();
			}
		});
		
		outputButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Document d = getState();
				XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());
				System.out.println(out.outputString(d));
			}
		});

	}

	public ArrayList<Link> getLinks() {
		return links;
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
						parts[1],
						parts[2]);
				items.add(ol);

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
			workPanel.repaint();
		}

		public void mouseMoved(MouseEvent e) {}

		public void mouseClicked(MouseEvent e) {
			Component c = workPanel.findComponentAt(e.getX(), e.getY());
			if (c instanceof SelectableObject) {
				updateSelections((SelectableObject)c);
			} else if ((c instanceof DrawingPanel) && e.isShiftDown()) {
				addNode(e.getX(), e.getY());
			}
		}

		public void mouseEntered(MouseEvent e) {}

		public void mouseExited(MouseEvent e) {}

		public void mousePressed(MouseEvent e) {
			dragComponent = null;

			Component c = workPanel.findComponentAt(e.getX(), e.getY());

			if (c instanceof JPanel) return;

			if (c instanceof SelectableObject) {
				dragComponent = (SelectableObject)c;
				xAdjustment = dragComponent.getLocation().x - e.getX();
				yAdjustment = dragComponent.getLocation().y - e.getY();
				dragComponent.setLocation(e.getX() + xAdjustment, e.getY() + yAdjustment);
				workPanel.repaint();
			}
		}

		public void mouseReleased(MouseEvent e) {
			dragComponent = null;
			workPanel.repaint();
		}
	}

	private void updateSelections(SelectableObject so) {
		// see if it can be linked
		if (so instanceof SelectableLinkableObject) {
			if (so.isSelected()) {
				if (so == selectionA) {
					selectionA.setSelected(false);
					selectionA.setBorder(BorderFactory.createLineBorder(Color.BLACK));
					selectionA = selectionB;
					selectionB = null;
				}
				if (so == selectionB) {
					selectionB = null;
				}
				so.setSelected(false);
				so.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			} else {
				if (selectionA != null) {
					if (selectionB != null) {
						selectionB.setSelected(false);
						selectionB.setBorder(BorderFactory.createLineBorder(Color.BLACK));
					}
					selectionB = selectionA;
				} 
				selectionA = (SelectableLinkableObject)so;
				so.setSelected(true);
				so.setBorder(BorderFactory.createLineBorder(Color.RED));	

				//clear any selected text labels
				if (selectionOnly != null) {
					selectionOnly.setSelected(false);
					selectionOnly.setBorder(BorderFactory.createLineBorder(Color.BLACK));
					selectionOnly = null;
				}
			}

			numSelectedItems = 0;
			if (selectionA != null) numSelectedItems++;
			if (selectionB != null) numSelectedItems++;

			switch (numSelectedItems) {
			case 2:
				linkButton.setEnabled(true);
				unlinkButton.setEnabled(true);
				splitButton.setEnabled(true);
				break;

			case 1:
				if (selectionA instanceof Node) {
					deleteButton.setEnabled(true);
				}
				linkButton.setEnabled(false);
				unlinkButton.setEnabled(false);	
				splitButton.setEnabled(false);
				break;

			case 0:
				linkButton.setEnabled(false);
				unlinkButton.setEnabled(false);	
				deleteButton.setEnabled(false);
				splitButton.setEnabled(false);
			}

			//if not, deal with it specially - it is a TextLabel
			//  need to clear other selections
			//  don't keep track of it 
		} else {
			if (so.isSelected()) {
				so.setSelected(false);
				so.setBorder(BorderFactory.createLineBorder(Color.BLACK));
				selectionOnly = null;
				deleteButton.setEnabled(false);
			} else {
				//clear any other selections
				if (selectionA != null) {
					selectionA.setSelected(false);
					selectionA.setBorder(BorderFactory.createLineBorder(Color.BLACK));
					selectionA = null;
				}
				if (selectionB != null) {
					selectionB.setSelected(false);
					selectionB.setBorder(BorderFactory.createLineBorder(Color.BLACK));
					selectionB = null;
				}
				so.setSelected(true);
				so.setBorder(BorderFactory.createLineBorder(Color.RED));
				selectionOnly = so;
				deleteButton.setEnabled(true);
			}
		}
	}

	private void addNode(int x, int y) {
		Node node = new Node(new ImageIcon(this.getClass().getResource("/images/node.gif" )));
		items.add(node);
		workPanel.add(node);
		node.setBounds(x, y, 12, 12);
	}

	private void link() {
		if ((selectionA instanceof SelectableLinkableObject) && (selectionB instanceof SelectableLinkableObject)) {
			links.add(new Link(selectionA, selectionB));
			workPanel.repaint();
		}
	}

	private void unlink() {
		Iterator<Link> it = links.iterator();
		while (it.hasNext()) {
			Link l = it.next();
			if ( ( (l.getOneLabel() == selectionA) && (l.getOtherLabel() == selectionB) )  ||
					( (l.getOneLabel() == selectionB) && (l.getOtherLabel() == selectionA))	
			) {
				links.remove(l);
				workPanel.repaint();
				return;
			}
		}
		JOptionPane.showMessageDialog(masterContainer, 
				"Please select two linked objects to un-link", 
				"Nothing to un-link", 
				JOptionPane.WARNING_MESSAGE);
	}

	private void addLabel() {
		String s = (String)JOptionPane.showInputDialog(
				masterContainer,
				"Enter Label Text:",
				"Create a Label",
				JOptionPane.PLAIN_MESSAGE,
				null,
				null,
		"");
		TextLabel tl = new TextLabel(s);
		tl.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		items.add(tl);
		workPanel.add(tl);
		tl.setBounds(500, 
				500, 
				(workPanel.getFontMetrics(workPanel.getFont())).stringWidth(s) + 5, 
				LABEL_HEIGHT);
	}

	private void deleteSelected() {
		// only can delete single Nodes or TextLabels
		if (selectionA instanceof Node) {
			//remake node list without any links
			//  that include this node
			ArrayList<Link> newLinks = new ArrayList<Link>();
			Iterator<Link> it = links.iterator();
			while (it.hasNext()) {
				Link l = it.next();
				if ((l.getOneLabel() != selectionA) && (l.getOtherLabel() != selectionA)) {
					newLinks.add(l);
				}
			}
			links = newLinks;
			workPanel.remove(selectionA);
			items.remove(selectionA);
			selectionA = null;
			workPanel.repaint();
		}

		if (selectionOnly != null) {
			workPanel.remove(selectionOnly);
			items.remove(selectionOnly);
			selectionOnly = null;
			workPanel.repaint();
		}
	}

	private void split() {
		Iterator<Link> it = links.iterator();
		while(it.hasNext()) {
			Link l = it.next();
			if (
					((l.getOneLabel() == selectionA) && (l.getOtherLabel() == selectionB)) ||
					((l.getOneLabel() == selectionB) && (l.getOtherLabel() == selectionA))
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
				links.add(new Link(selectionA, node));
				links.add(new Link(selectionB, node));
				selectionA.setSelected(false);
				selectionA.setBorder(BorderFactory.createLineBorder(Color.BLACK));
				selectionB.setSelected(false);
				selectionB.setBorder(BorderFactory.createLineBorder(Color.BLACK));				
				workPanel.repaint();
				return;
			}
		}
	}

	private void printSurvey() {
		PrinterJob printJob = PrinterJob.getPrinterJob();
		printJob.setPrintable(workPanel);
		if (printJob.printDialog())
			try { 
				printJob.print();
			} catch(PrinterException pe) {
				System.out.println("Error printing: " + pe);
			}
	}
	
	private Document getState() {
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
		
		return new Document(root);
	}

}
