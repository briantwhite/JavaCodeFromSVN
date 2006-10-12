package biochem;

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
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.BevelBorder;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import molGenExp.ColorModel;
import molGenExp.RYBColorModel;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;


public class Protex extends JPanel {
	
	JPanel topMenuPanel;
	JMenuBar menuBar;
	JMenu fileMenu;
	JMenuItem print;
	JMenuItem quit;
	JMenu histListMenu;
	JMenuItem save;
	JMenuItem saveAs;
	JMenuItem load;
	JMenuItem deleteSelected;
	JMenuItem clearAll;
	JMenuItem saveToHTML;
	JMenu infoMenu;
	JMenuItem help;
	JMenuItem about;
	
	FoldingWindow upperFoldingWindow;
	FoldingWindow lowerFoldingWindow;
	ProteinHistoryList proteinHistoryList;
	JScrollPane histListScrollPane;
	MiddleButtonPanel middleButtonPanel;
	
	ColorModel colorModel;
	
	ProteinPrinter printer;
	
	File outFile;
	

	public Protex() {
		super();
		colorModel = new RYBColorModel();
		printer = new ProteinPrinter();
		outFile = null;
		setupUI();
	}
	

	class ApplicationCloser extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			System.exit(0);
		}
	}

	private void setupUI() {
		
		topMenuPanel = new JPanel();
		topMenuPanel.setLayout(new BoxLayout(topMenuPanel, BoxLayout.X_AXIS));
		
		menuBar = new JMenuBar();
		menuBar.setBorder(new BevelBorder(BevelBorder.RAISED));
		
		fileMenu = new JMenu("File");
		print = new JMenuItem("Print");
		fileMenu.add(print);
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
		deleteSelected = new JMenuItem("Delete Selected Item");
		histListMenu.add(deleteSelected);
		clearAll = new JMenuItem("Clear");
		histListMenu.add(clearAll);
		histListMenu.addSeparator();
		saveToHTML = new JMenuItem("Save as Web Page...");
		histListMenu.add(saveToHTML);
		menuBar.add(histListMenu);
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
		JPanel aapPanel = new JPanel();
		aapPanel.setBorder(BorderFactory.createTitledBorder("Amino acids"));
		AminoAcidPalette aaPalette 
			= new AminoAcidPalette(225, 180, 4, 5, false);
		aapPanel.setMaximumSize(new Dimension(250, 200));
		aapPanel.add(aaPalette);
		
		proteinHistoryList = new ProteinHistoryList(new DefaultListModel());
		histListScrollPane = new JScrollPane(proteinHistoryList);
		histListScrollPane.setBorder(
				BorderFactory.createTitledBorder("History List"));
		histListScrollPane.setMaximumSize(new Dimension(250,1000));
		
		leftPanel.add(aapPanel);
		leftPanel.add(histListScrollPane);
		
		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new GridLayout(2,1));
		upperFoldingWindow = new FoldingWindow("Upper Folding Window", this, colorModel);
		lowerFoldingWindow = new FoldingWindow("Lower Folding Window", this, colorModel);
		rightPanel.add(upperFoldingWindow);
		rightPanel.add(lowerFoldingWindow);
		
		middleButtonPanel = new MiddleButtonPanel(this);
		middleButtonPanel.setMaximumSize(middleButtonPanel.getPreferredSize());
		
		JPanel mainPanel = new JPanel();
		
		mainPanel.setLayout(
				new BoxLayout(mainPanel, BoxLayout.X_AXIS));
		mainPanel.add(leftPanel);
		mainPanel.add(middleButtonPanel);
		mainPanel.add(rightPanel);
		
		setLayout(new BorderLayout());
		add(mainPanel, BorderLayout.CENTER);
		add(topMenuPanel, BorderLayout.NORTH);	

		
		
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
						proteinHistoryList.clearList();
						for (int i = 0; i < all.length; i++) {
							proteinHistoryList.add((FoldedPolypeptide)all[i]);
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
				Object[] all = proteinHistoryList.getAll();
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
				Object[] all = proteinHistoryList.getAll();
				if (all.length == 0) {
					JOptionPane.showMessageDialog(null, "No History List to Save",
							"Blank History List", JOptionPane.WARNING_MESSAGE);
					return;
				}
				saveToChosenFile(all)	;
			}
		});
		
		deleteSelected.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				proteinHistoryList.deleteSelected();
			}
		});
		
		clearAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (JOptionPane.showConfirmDialog(null, "<html>Are you sure you "
						+ "want to clear the history list? <br>"
						+ "This cannot be undone!",
						"Confirm Clear", 
						JOptionPane.OK_CANCEL_OPTION) == JOptionPane.YES_OPTION) {
					proteinHistoryList.clearList();
				}
			}	
		});
		
		saveToHTML.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				File htmlDirectory = null;
				Object[] all = proteinHistoryList.getAll();
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
							htmlOutputStream.write("<b>Color:</b>\n");
							htmlOutputStream.write("<img src=" + i + "color.jpg>\n");
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
							
							BufferedImage colorChip = new BufferedImage(20, 20, BufferedImage.TYPE_INT_RGB);
							Graphics colorG = colorChip.getGraphics();
							colorG.setColor(fp.getColor());
							colorG.fillRect(0, 0, 20, 20);
							colorG.dispose();
							
							String colorChipFileName = htmlDirectory.toString() 
								+ System.getProperty("file.separator")
								+ i + "color.jpg";
							BufferedOutputStream colorChipOut = 
								new BufferedOutputStream(new FileOutputStream(colorChipFileName));
							JPEGImageEncoder ccEncoder = JPEGCodec.createJPEGEncoder(colorChipOut);
							JPEGEncodeParam ccParam = encoder.getDefaultJPEGEncodeParam(colorChip);
							ccParam.setQuality((float) 1, false);
							ccEncoder.setJPEGEncodeParam(ccParam);
							ccEncoder.encode(colorChip);
							colorChipOut.close();
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
	
	

	public void addFoldedToHistList(FoldedPolypeptide fp) {
		proteinHistoryList.add(fp);
		histListScrollPane.revalidate();
		histListScrollPane.repaint();
		updateCombinedColor();
	}
	
	public void updateCombinedColor() {
		Color u = upperFoldingWindow.getColor();
		Color l = lowerFoldingWindow.getColor();
		Color combined = colorModel.mixTwoColors(u, l);
		middleButtonPanel.setCombinedColor(combined);
	}
	
	public void sendSelectedFPtoUP() {
		if (proteinHistoryList.getSelectedValue() != null) {
			FoldedPolypeptide fp =
				(FoldedPolypeptide) proteinHistoryList.getSelectedValue();
			upperFoldingWindow.setFoldedPolypeptide(fp);
		}
	}
	
	public void sendSelectedFPtoLP() {
		if (proteinHistoryList.getSelectedValue() != null){
			FoldedPolypeptide fp =
				(FoldedPolypeptide) proteinHistoryList.getSelectedValue();
			lowerFoldingWindow.setFoldedPolypeptide(fp);
		}
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
		proteinHistoryList.clearList();
		for (int i = 0; i < all.length; i++) {
			proteinHistoryList.add((FoldedPolypeptide)all[i]);
		}
		histListScrollPane.revalidate();
		histListScrollPane.repaint();

	}
	
}
