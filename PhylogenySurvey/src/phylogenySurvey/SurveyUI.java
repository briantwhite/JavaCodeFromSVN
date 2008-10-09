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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import scoring.Scorer;

public class SurveyUI {

	public static final int LABEL_WIDTH = 120;
	public static final int LABEL_HEIGHT = 30;

	private static final String PASSWORD = "treedata";

	private boolean scoringOn;

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
	private JButton undoButton;
	private JButton loadButton;
	private JButton saveButton;
	private JButton scoreButton;


	public SurveyUI(Container masterContainer) {
		this.masterContainer = masterContainer;
		numSelectedItems = 0;
		selectionA = null;
		selectionB = null;
		selectionOnly = null;
		scoringOn = true;
	}

	public void setupUI(boolean scoringEnabled, String password) {

		if (scoringEnabled && password.equals(PASSWORD)) {
			scoringOn = true;
		}

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

		undoButton = new JButton("Undo");
		buttonPanel.add(undoButton);

		if (scoringOn) {

			loadButton = new JButton("Load");
			buttonPanel.add(loadButton);

			saveButton = new JButton("Save");
			buttonPanel.add(saveButton);

			scoreButton = new JButton("Score");
			buttonPanel.add(scoreButton);
		}

		masterContainer.add(buttonPanel, BorderLayout.NORTH);

		workPanel = new DrawingPanel(this);
		workPanel.setLayout(null);
		workPanel.addMouseListener(new MoveLabelHandler());
		workPanel.addMouseMotionListener(new MoveLabelHandler());

		loadOrganisms();

		masterContainer.add(workPanel, BorderLayout.CENTER);

		linkButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if ((selectionA instanceof SelectableLinkableObject) && (selectionB instanceof SelectableLinkableObject)) {
					SurveyData.getInstance().add(new Link(selectionA, selectionB));
					workPanel.repaint();
					SurveyData.getInstance().saveStateToHistoryList();
				}
			}
		});

		unlinkButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SurveyData.getInstance().deleteLink(selectionA, selectionB);
				workPanel.repaint();
				SurveyData.getInstance().saveStateToHistoryList();
			}
		});

		labelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String s = (String)JOptionPane.showInputDialog(
						masterContainer,
						"Enter Label Text:",
						"Create a Label",
						JOptionPane.PLAIN_MESSAGE,
						null,
						null,
				"");
				if (s != null) {
					TextLabel tl = new TextLabel(s);
					tl.setBorder(BorderFactory.createLineBorder(Color.BLACK));
					SurveyData.getInstance().add(tl, workPanel);
				}
				workPanel.repaint();
				SurveyData.getInstance().saveStateToHistoryList();
			}
		});

		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// only can delete single Nodes or TextLabels
				if (selectionA instanceof Node) {
					SurveyData.getInstance().delete((Node)selectionA, workPanel);
					selectionA = null;
					workPanel.repaint();
					SurveyData.getInstance().saveStateToHistoryList();
				}

				if (selectionOnly != null) {
					SurveyData.getInstance().delete((TextLabel)selectionOnly, workPanel);
					selectionOnly = null;
					workPanel.repaint();
					SurveyData.getInstance().saveStateToHistoryList();
				}
			}
		});

		splitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SurveyData.getInstance().split(selectionA, selectionB, workPanel);
				workPanel.repaint();
				SurveyData.getInstance().saveStateToHistoryList();
			}
		});

		printButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PrinterJob printJob = PrinterJob.getPrinterJob();
				printJob.setPrintable(workPanel);
				if (printJob.printDialog())
					try { 
						printJob.print();
					} catch(PrinterException pe) {
						System.out.println("Error printing: " + pe);
					}
			}
		});

		undoButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				undo();
			}
		});


		if (scoringOn) {

			loadButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JFileChooser fc = new JFileChooser();
					fc.setCurrentDirectory(new File("../"));
					int returnVal = fc.showOpenDialog(null);
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						File inFile = fc.getSelectedFile();
						StringBuffer contents = new StringBuffer();
						try {
							BufferedReader input =  new BufferedReader(new FileReader(inFile));
							String line = null; //not declared within while loop
							while (( line = input.readLine()) != null){
								contents.append(line);
								contents.append(System.getProperty("line.separator"));
							}
						} catch (Exception e1) {
							e1.printStackTrace();
						}
						SurveyData.getInstance().setState(contents.toString(), workPanel);
						workPanel.repaint();
						selectionA = null;
						selectionB = null;
						selectionOnly = null;
						linkButton.setEnabled(false);
						unlinkButton.setEnabled(false);	
						deleteButton.setEnabled(false);
						splitButton.setEnabled(false);		
					}
				}
			});

			saveButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JFileChooser fc = new JFileChooser();
					fc.setCurrentDirectory(new File("../"));
					int returnVal = fc.showSaveDialog(null);
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						File outFile = fc.getSelectedFile();
						Writer output = null;
						try {
							output = new BufferedWriter(new FileWriter(outFile));
							output.write(SurveyData.getInstance().getState());
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						finally {
							try {
								output.close();
							} catch (IOException e1) {
								e1.printStackTrace();
							}
						}
					}
				}
			});

			scoreButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JLabel message = new JLabel(
							Scorer.getInstance().score(SurveyData.getInstance()));
					JOptionPane.showMessageDialog(null, message);
				}
			});
		}
	}

	private void loadOrganisms() {
		URL listFileURL = this.getClass().getResource("/images/list.txt");
		SurveyData.getInstance().loadOrganisms(listFileURL, workPanel);
		workPanel.repaint();
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
				Node node = new Node(new ImageIcon(this.getClass().getResource("/images/node.gif" )));
				SurveyData.getInstance().add(node, workPanel);
				node.setBounds(e.getX(), e.getY(), 12, 12);
				SurveyData.getInstance().saveStateToHistoryList();
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
			if (dragComponent != null) {
				SurveyData.getInstance().saveStateToHistoryList();
				dragComponent = null;
			}
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

	private void undo() {
		String newState = SurveyData.getInstance().undo();
		if (newState == null) {
			return;
		}
		SurveyData.getInstance().setState(newState, workPanel);
		workPanel.repaint();
		selectionA = null;
		selectionB = null;
		selectionOnly = null;
		linkButton.setEnabled(false);
		unlinkButton.setEnabled(false);	
		deleteButton.setEnabled(false);
		splitButton.setEnabled(false);		
	}

	/*
	 * public methods for setting & getting the state of the drawing
	 */
	public String getState() {
		return SurveyData.getInstance().getState();
	}

	public void setState(String state) {
		SurveyData.getInstance().setState(state, workPanel);
	}

}