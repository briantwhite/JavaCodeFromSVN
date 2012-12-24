package protex;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.border.BevelBorder;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;


public class Protex extends JFrame {

	public final static String targetShapeFileName = "target_shapes.txt";
	public final static String contactEnergyListFileName = "ContactEnergies.txt";

	private final static String version = "3.0.2";

	public static final Color SS_BONDS_OFF_BACKGROUND = new Color((float) 0.7,
			(float) 0.7, (float) 1.0);

	public static final Color SS_BONDS_ON_BACKGROUND = new Color((float) 0.7,
			(float) 1.0, (float) 1.0);
	
	public static boolean isApplet;

	JPanel topMenuPanel;
	JMenuBar menuBar;
	JMenu fileMenu;
	JMenu energies;
	JRadioButtonMenuItem standard;
	JRadioButtonMenuItem custom;

	JMenuItem print;
	JMenuItem quit;

	JMenu histListMenu;
	JMenuItem save;
	JMenuItem saveAs;
	JMenuItem load;
	JMenuItem clearAll;
	JMenuItem saveToHTML;

	JMenu gameMenu;
	JCheckBoxMenuItem strictModeItem;
	JMenuItem chooseATargetShape;
	JMenuItem saveUpperAsTarget;
	JMenuItem saveLowerAsTarget;

	JMenu infoMenu;
	JMenuItem help;
	JMenuItem about;

	FoldingWindow upperFoldingWindow;
	FoldingWindow lowerFoldingWindow;
	HistoryList historyList;
	JScrollPane histListScrollPane;

	private boolean strictMatchingMode;

	ProteinPrinter printer;

	File outFile;

	private TargetShapeSelectionDialog targetShapeSelectionDialog;
	private TargetShapeDisplayDialog targetShapeDisplayDialog;
	private TargetShape currentTargetShape;

	public Protex(boolean isApplet) {
		super("Protein Investigator " + version);
		Protex.isApplet = isApplet;
		addWindowListener(new ApplicationCloser());
		if (!isApplet) {
			printer = new ProteinPrinter();
		}
		outFile = null;
		currentTargetShape = null;
		strictMatchingMode = false;
		setupUI();
	}

	public static void main(String[] args) {
		Protex protex = new Protex(false);
		protex.pack();
		protex.setVisible(true);
	}

	class ApplicationCloser extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			System.exit(0);
		}
	}

	private void setupUI() {

		StandardTable table = StandardTable.getInstance();

		topMenuPanel = new JPanel();
		topMenuPanel.setLayout(new BoxLayout(topMenuPanel, BoxLayout.X_AXIS));

		menuBar = new JMenuBar();
		menuBar.setBorder(new BevelBorder(BevelBorder.RAISED));

		fileMenu = new JMenu("File");
		print = new JMenuItem("Print");
		fileMenu.add(print);
		if (!table.getContactEnergyListName().equals("")) {
			ButtonGroup group = new ButtonGroup();
			JMenu energies = new JMenu("Select Interaction Energies");
			standard = new JRadioButtonMenuItem("Standard Energies");
			custom = new JRadioButtonMenuItem(table.getContactEnergyListName());
			group.add(standard);
			group.add(custom);
			standard.setSelected(true);
			energies.add(standard);
			energies.add(custom);
			fileMenu.add(energies);
		}
		quit = new JMenuItem("Quit");
		fileMenu.add(quit);
		menuBar.add(fileMenu);

		histListMenu = new JMenu("History List");
		save = new JMenuItem("Save");
		histListMenu.add(save);
		saveAs = new JMenuItem("Save As...");
		histListMenu.add(saveAs);
		load = new JMenuItem("Load...");
		histListMenu.add(load);
		histListMenu.addSeparator();
		clearAll = new JMenuItem("Clear");
		histListMenu.add(clearAll);
		histListMenu.addSeparator();
		saveToHTML = new JMenuItem("Save as Web Page...");
		histListMenu.add(saveToHTML);
		menuBar.add(histListMenu);

		gameMenu = new JMenu("Game");
		strictModeItem = new JCheckBoxMenuItem("Strict Matching Mode");
		gameMenu.add(strictModeItem);
		chooseATargetShape = new JMenuItem("Choose a Target Shape...");
		gameMenu.add(chooseATargetShape);
		gameMenu.addSeparator();
		saveUpperAsTarget = new JMenuItem("Save Upper Protein as Target...");
		gameMenu.add(saveUpperAsTarget);
		saveLowerAsTarget = new JMenuItem("Save Lower Protein as Target...");
		gameMenu.add(saveLowerAsTarget);
		menuBar.add(gameMenu);

		menuBar.add(Box.createHorizontalGlue());

		infoMenu = new JMenu("Information");
		help = new JMenuItem("Help");
		infoMenu.add(help);
		about = new JMenuItem("About");
		infoMenu.add(about);
		menuBar.add(infoMenu);

		topMenuPanel.add(menuBar);

		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
		leftPanel.add(Box.createRigidArea(new Dimension(225,1)));
		JPanel aapPanel = new JPanel();
		aapPanel.setBorder(BorderFactory.createTitledBorder("Amino acids"));
		aapPanel.setLayout(new BoxLayout(aapPanel, BoxLayout.X_AXIS));
		AminoAcidPalette aaPalette 
		= new AminoAcidPalette(225, 180, 4, 5, false);
		aapPanel.setMaximumSize(new Dimension(250, 200));
		aapPanel.add(aaPalette);
		aapPanel.add(Box.createRigidArea(new Dimension(1,180)));

		historyList = new HistoryList(new DefaultListModel(), this);
		histListScrollPane = new JScrollPane(historyList);
		histListScrollPane.setBorder(
				BorderFactory.createTitledBorder("History List"));
		histListScrollPane.setMaximumSize(new Dimension(250,1000));

		leftPanel.add(aapPanel);
		leftPanel.add(histListScrollPane);

		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new GridLayout(2,1));
		upperFoldingWindow = new FoldingWindow("Upper Folding Window", this);
		lowerFoldingWindow = new FoldingWindow("Lower Folding Window", this);
		rightPanel.add(upperFoldingWindow);
		rightPanel.add(lowerFoldingWindow);

		JPanel mainPanel = new JPanel();

		mainPanel.setLayout(
				new BoxLayout(mainPanel, BoxLayout.X_AXIS));
		mainPanel.add(leftPanel);
		mainPanel.add(rightPanel);

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(mainPanel, BorderLayout.CENTER);
		getContentPane().add(topMenuPanel, BorderLayout.NORTH);	

		targetShapeSelectionDialog = new TargetShapeSelectionDialog(this);
		targetShapeDisplayDialog = new TargetShapeDisplayDialog(this);

		about.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog(null, 
						"<html><body><center>Protein Investigator<br>"
								+ "Version " + version 
								+ "<br>Copyright 2007<br>"
								+ "Brian White and MGX Team<br>"
								+ "brian.white@umb.edu"+ "</center</body></html>",
								"About ProtInv",
								JOptionPane.INFORMATION_MESSAGE);
			}
		});

		load.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Object[] all = null;
				File inFile = null;
				JFileChooser infileChooser = new JFileChooser();
				infileChooser.setFileFilter(new HistListFileFilter());
				int returnVal = infileChooser.showOpenDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					inFile = infileChooser.getSelectedFile();

					try {
						FileInputStream in = new FileInputStream(inFile);
						ObjectInputStream s = new ObjectInputStream(in);
						all = (Object[]) s.readObject();
						s.close();
						in.close();
					} 
					catch (Exception e) {
						e.printStackTrace();
					}
					if (all != null) {
						historyList.clearList();
						for (int i = 0; i < all.length; i++) {
							historyList.add((FoldedPolypeptide)all[i]);
						}
						histListScrollPane.revalidate();
						histListScrollPane.repaint();
					}
				}
			}

		});

		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//save hist list to avoid serialization bug that wipes it clean 
				Object[] all = historyList.getAll();
				if (all.length == 0) {
					JOptionPane.showMessageDialog(null, "No History List to Save",
							"Blank History List", JOptionPane.WARNING_MESSAGE);
					return;
				}

				//save it
				if (outFile != null) {
					saveToFile(outFile, all);
				} else {
					saveToChosenFile(all);
				}
			}
		});

		saveAs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Object[] all = historyList.getAll();
				if (all.length == 0) {
					JOptionPane.showMessageDialog(null, "No History List to Save",
							"Blank History List", JOptionPane.WARNING_MESSAGE);
					return;
				}
				saveToChosenFile(all)	;
			}
		});

		clearAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (JOptionPane.showConfirmDialog(null, "<html>Are you sure you "
						+ "want to clear the history list? <br>"
						+ "This cannot be undone!",
						"Confirm Clear", 
						JOptionPane.OK_CANCEL_OPTION) == JOptionPane.YES_OPTION) {
					historyList.clearList();
				}
			}	
		});

		saveToHTML.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				File htmlDirectory = null;
				Object[] all = historyList.getAll();
				if (all.length == 0) {
					JOptionPane.showMessageDialog(null, "No History List to Save",
							"Blank History List", JOptionPane.WARNING_MESSAGE);
					return;
				}
				JFileChooser htmlFileChooser = new JFileChooser();
				htmlFileChooser.setDialogTitle("Choose a Folder to save the Web Page in");
				htmlFileChooser.setDialogType(JFileChooser.DIRECTORIES_ONLY);
				int returnVal = htmlFileChooser.showSaveDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					htmlDirectory = htmlFileChooser.getSelectedFile();
					BufferedWriter htmlOutputStream = null;
					try {		
						if (htmlDirectory.exists()) {
							JOptionPane.showMessageDialog(null, "The Folder you selected already exists.",
									"Folder Already Exists", JOptionPane.WARNING_MESSAGE);
							return;
						}
						htmlDirectory.mkdirs();
						htmlOutputStream = new BufferedWriter(
								new FileWriter(htmlDirectory.toString() 
										+ System.getProperty("file.separator")
										+ "index.html"));
						htmlOutputStream.write("<html>\n<head>\n");
						htmlOutputStream.write("<title>History List</title>\n</head>\n");
						htmlOutputStream.write("<body>\n");
						htmlOutputStream.write("<h3>History List</h3>\n");
						htmlOutputStream.write("<table border=1>\n");

						for (int i = 0; i < all.length; i++) {
							FoldedPolypeptide fp = (FoldedPolypeptide)all[i];
							htmlOutputStream.write("<tr><td>");
							htmlOutputStream.write("<b>Protein Sequence:</b>\n");
							htmlOutputStream.write("<pre>" + fp.getAaSeq() + "</pre>\n");
							htmlOutputStream.write("<b>Protein Structure:</b><br>\n");
							htmlOutputStream.write("<img src=" + i + ".jpg>\n");
							htmlOutputStream.write("</td></tr>\n");

							ImageIcon pic = fp.getFullSizePic();
							Image picImage = pic.getImage();
							BufferedImage image = new BufferedImage(pic.getIconWidth(),
									pic.getIconHeight(), BufferedImage.TYPE_INT_RGB);
							Graphics g = image.getGraphics();
							g.drawImage(picImage, 0, 0, null);
							g.dispose();

							String jpegFileName = htmlDirectory.toString() 
									+ System.getProperty("file.separator")
									+ i + ".jpg";
							BufferedOutputStream out = 
									new BufferedOutputStream(new FileOutputStream(jpegFileName));
							JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
							JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(image);
							param.setQuality((float) 1, false);
							encoder.setJPEGEncodeParam(param);
							encoder.encode(image);
							out.close();

						}

						htmlOutputStream.write("</table></body>\n</html>\n");
					} catch (IOException e) {
						JOptionPane.showMessageDialog(null, "<html><body>Cannot Create Directory "
								+ "for Web Page Files<br>" 
								+ e.toString()
								+ "</body></html>",
								"Problem Saving Files", JOptionPane.WARNING_MESSAGE);
					}

					finally {
						try {
							if (htmlOutputStream != null) {
								htmlOutputStream.close();
							}
						}
						catch (Exception e) {
							e.printStackTrace();
						}
					}

				}
			}
		});

		strictModeItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				strictMatchingMode = strictModeItem.getState();
				targetShapeDisplayDialog.setVisible(false);
			}
		});

		chooseATargetShape.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				targetShapeSelectionDialog.showTargetShapeSelectionDialog();
			}
		});

		saveUpperAsTarget.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				saveAsTargetShape(upperFoldingWindow);
			}
		});

		saveLowerAsTarget.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				saveAsTargetShape(lowerFoldingWindow);
			}
		});

		help.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				onlineHelp();
			}
		});

		print.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				printer.printProteins(upperFoldingWindow, lowerFoldingWindow);
			}
		});

		quit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (JOptionPane.showConfirmDialog(null, "Are you sure you want to quit?",
						"Confirm Quit", 
						JOptionPane.OK_CANCEL_OPTION) == JOptionPane.YES_OPTION) {
					System.exit(0);
				}
			}	
		});

	}

	public void saveAsTargetShape(FoldingWindow fw) {

		if (fw.getOutputPalette().getDrawingPane().getGrid() == null) {
			return;
		}

		String targetName = (String)JOptionPane.showInputDialog(
				this,
				"Enter a name for this Target Shape",
				"Name the Target Shape",
				JOptionPane.PLAIN_MESSAGE,
				null,
				null,
				null);

		if (targetName.equals("")) {
			return;
		}

		String foldingString = 
				fw.getOutputPalette().getDrawingPane().getGrid().getPP().getDirectionSequence();

		File targetShapeFile = new File(targetShapeFileName);
		BufferedWriter targetShapeFileStream = null;
		try {
			targetShapeFileStream = new BufferedWriter(
					new FileWriter(targetShapeFileName, targetShapeFile.exists()));
			targetShapeFileStream.write(targetName + "," + foldingString + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			try {
				if (targetShapeFileStream != null) {
					targetShapeFileStream.close();
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public void setSelectedTargetShape(TargetShape ts) {
		currentTargetShape = ts;
		targetShapeSelectionDialog.goAway();
		targetShapeDisplayDialog.showTargetShapeDisplayDialog();
	}

	public TargetShape getCurrentTargetShape() {
		return currentTargetShape;
	}

	public void checkUpperAgainstTarget() {
		checkAgainstTarget(upperFoldingWindow);
	}

	public void checkLowerAgainstTarget() {
		checkAgainstTarget(lowerFoldingWindow);
	}

	private void checkAgainstTarget(FoldingWindow fw) {

		ShapeMatcher shapeMatcher = 
				new ShapeMatcher(currentTargetShape.getShapeString(), strictMatchingMode);

		if (fw.getOutputPalette().getBackground().equals(Color.PINK)) {
			JOptionPane.showMessageDialog(this,
					"<html><body>"
							+ "Sorry, the protein sequence you entered<br>"
							+ "has not been folded yet.<br>"
							+ "Please click"
							+ " the FOLD button.</body></html>", 
							"New Protein Not Folded Yet", 
							JOptionPane.WARNING_MESSAGE);
			return;			
		}

		if (fw.getOutputPalette().getDrawingPane().getGrid() == null) {
			JOptionPane.showMessageDialog(this,
					"<html><body>"
							+ "Sorry, there is no folded protein to match.<br>"
							+ "Please enter an amino acid sequence and click"
							+ " the FOLD button.</body></html>", 
							"No Folded Protein", 
							JOptionPane.WARNING_MESSAGE);
			return;
		}


		if (shapeMatcher.matchesTarget(
				fw.getOutputPalette().getDrawingPane().getGrid().getPP().getDirectionSequence())) {
			JOptionPane.showMessageDialog(this, 
					"Congratulations, you got it right!", 
					"Your Shape Matches the Target!", 
					JOptionPane.INFORMATION_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(this, 
					"Sorry, the shapes do not match.", 
					"Shape Mismatch", 
					JOptionPane.WARNING_MESSAGE);
		}	

	}

	public boolean getStrictMatchingMode() {
		return strictMatchingMode;
	}

	private void onlineHelp() {
		final JEditorPane helpPane = new JEditorPane();
		helpPane.setEditable(false);
		helpPane.setContentType("text/html");

		try {
			helpPane.setPage(Protex.class.getResource("help/index1.html"));
		} catch (Exception e) {
		}

		JScrollPane helpScrollPane = new JScrollPane(helpPane);
		JDialog helpDialog = new JDialog(this, "Protein Investigator Help");
		helpDialog.getContentPane().setLayout(new BorderLayout());
		helpDialog.getContentPane().add(helpScrollPane, BorderLayout.CENTER);
		Dimension screenSize = getToolkit().getScreenSize();
		helpDialog.setBounds((screenSize.width / 8), (screenSize.height / 8),
				(screenSize.width * 6 / 10), (screenSize.height * 6 / 10));
		helpDialog.setVisible(true);

		helpPane.addHyperlinkListener(new HyperlinkListener() {
			public void hyperlinkUpdate(HyperlinkEvent e) {
				if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
					try {
						helpPane.setPage(e.getURL());
					} catch (IOException ioe) {
						System.err.println(ioe.toString());
					}
				}
			}

		});
	}


	public void addFoldedToHistList(FoldedPolypeptide fp) {
		historyList.add(fp);
		histListScrollPane.revalidate();
		histListScrollPane.repaint();
	}

	public void sendToUpperPanel(FoldedPolypeptide fp) {
		upperFoldingWindow.setFoldedPolypeptide(fp);
	}

	public void sendToLowerPanel(FoldedPolypeptide fp) {
		lowerFoldingWindow.setFoldedPolypeptide(fp);
	}

	public void saveToChosenFile(Object[] all) {
		JFileChooser outfileChooser = new JFileChooser();
		outfileChooser.setFileFilter(new HistListFileFilter());
		int returnVal = outfileChooser.showSaveDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			outFile = outfileChooser.getSelectedFile();
			if (! outFile.getName().endsWith(".histlist")) {
				outFile = new File(outFile.getParent() 
						+ System.getProperty("file.separator")
						+ outFile.getName() + ".histlist");
			}
			saveToFile(outFile, all);
		}
	}

	public void saveToFile(File outFile, Object[] all) {
		try {
			FileOutputStream f = new FileOutputStream(outFile);
			ObjectOutput s = new ObjectOutputStream(f);
			s.writeObject(all);
			s.flush();
			s.close();
			f.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		//restore history list
		historyList.clearList();
		for (int i = 0; i < all.length; i++) {
			historyList.add((FoldedPolypeptide)all[i]);
		}
		histListScrollPane.revalidate();
		histListScrollPane.repaint();

	}

	public FoldingWindow getUpperFoldingWindow() {
		return upperFoldingWindow;
	}

}
