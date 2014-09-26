package SE;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;

/**
 * Opens dialog to select output.txt file
 * shows all proteins there and their sequences and fitnesses for viewing and copying to clipboard
 * 
 * if -bw arg image is in B/W
 * @author brian
 *
 */
public class ProteinPicture extends JFrame{

	private static final Color BACKGROUND_COLOR = new Color(128, 255, 128);
	private static final Color BACKBONE_COLOR = Color.magenta;

	private static final Color BASE_COLOR = new Color(128, 128, 255);
	private static final Color ACID_COLOR = new Color(255, 128, 128);

	private static final int IMAGE_SIZE = 1000;
	private static final int AA_SIZE = 60;

	private ArrayList<ProteinData>proteins;

	private JTable table;

	private JLabel structure;
	private ArrayList<ArrayList<String>> structures;

	private static boolean isColor; // color or b/w image

	public ProteinPicture() {
		super("Protein Picture Maker");
		addWindowListener(new ApplicationCloser());
		add(new JLabel("Protein Picture Maker"));
		pack();
		setVisible(true);
		start();
	}

	public static void main(String[] args) {
		ProteinPicture proteinPicture = new ProteinPicture();
		if ((args.length > 0) && (args[0].equals("-bw"))) {
			isColor = false;
		} else {
			isColor = true;
		}
	}

	class ApplicationCloser extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			System.exit(0);
		}
	}

	// need to do this to get the dialog to show up
	// http://stackoverflow.com/questions/14640103/jfilechooser-not-showing-up
	Runnable r = new Runnable() {
		public void run() {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setDialogTitle("Select the output.txt file");
			int retVal = fileChooser.showOpenDialog(null);
			if (retVal == JFileChooser.APPROVE_OPTION) {
				showTable(fileChooser.getSelectedFile());
			}
		}
	};
	
	private void start() {
		SwingUtilities.invokeLater(r);
	}

	private void showTable(File file) {
		proteins = new ArrayList<ProteinData>();
		BufferedReader reader = null;
		String text = null;
		int run = -1;
		int generation = -1;
		double fitness = 0.0f;
		boolean inStructure = false;
		String proteinSequence = "";
		ArrayList<String>structureLines = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			while ((text = reader.readLine()) != null) {
				/*
				 * look for start of a structure record
				 */
				if (text.contains("StartStructure")) {
					inStructure = true;
					String[] pieces = text.split(" ");
					run = Integer.parseInt(pieces[1].split(":")[1]);
					generation = Integer.parseInt(pieces[2].split(":")[1]);
					fitness = Double.parseDouble(pieces[3].split(":")[1]);
					if (pieces[4].split(":").length != 2) {
						proteinSequence = "";
					} else {
						proteinSequence = pieces[4].split(":")[1];
					}
					structureLines = new ArrayList<String>();
				} else if (text.contains("EndStructure")) {
					inStructure = false;
					// log it
					if (!proteinSequence.equals("")) {
						ProteinData pd = new ProteinData(run, generation, proteinSequence, fitness, structureLines);
						proteins.add(pd);
					}
				} else if (inStructure) {
					structureLines.add(text);
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// set up to show them
		JDialog resultsDialog = new JDialog();
		resultsDialog.setTitle("Results");
		resultsDialog.setLayout(new BorderLayout());

		String[] columnNames = {"Run", "Generation", "Sequence", "Fitness"};
		Object[][] data = new Object[proteins.size()][columnNames.length];
		structures = new ArrayList<ArrayList<String>>();
		for (int i = 0; i < proteins.size(); i++) {
			ProteinData pd = proteins.get(i);
			data[i][0] = pd.run;
			data[i][1] = pd.generation;
			data[i][2] = pd.aaSeq;
			data[i][3] = pd.fitness;
			structures.add(pd.structure);
		}

		table = new JTable(new MyTableModel(columnNames, data));
		table.setAutoCreateRowSorter(true);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setFont(new Font("Courier", Font.PLAIN, 12));
		table.getColumnModel().getColumn(2).setPreferredWidth(100);
		JScrollPane scroller = new JScrollPane(table);
		table.setFillsViewportHeight(true);

		ListSelectionModel lsm = table.getSelectionModel();
		lsm.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				int i = table.getSelectedRow();
				structure.setIcon(new ImageIcon(makePicture(structures.get(table.convertRowIndexToModel(i)), IMAGE_SIZE, isColor)));
			}				
		});
		table.setSelectionModel(lsm);

		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
		leftPanel.add(Box.createRigidArea(new Dimension(400,1)));
		leftPanel.add(scroller);

		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
		rightPanel.add(Box.createRigidArea(new Dimension(1000,1)));
		JButton clipboardButton = new JButton("Copy Image to Clipboard");
		clipboardButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				int i = table.getSelectedRow();
				if (i >= 0) {
					Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
					ImageForClipboard ifc = new ImageForClipboard(makePicture(structures.get(i),IMAGE_SIZE, isColor));
					c.setContents(ifc, null);
				}
			}				
		});
		rightPanel.add(clipboardButton);
		structure = new JLabel();
		structure.setHorizontalAlignment(SwingConstants.LEFT);
		rightPanel.add(structure);

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
		mainPanel.add(leftPanel);
		mainPanel.add(rightPanel);


		resultsDialog.add(mainPanel);
		resultsDialog.pack();
		resultsDialog.setVisible(true);

	}

	class MyTableModel extends AbstractTableModel {

		String[] columnNames;
		Object[][] data;

		public MyTableModel(String[] columnNames, Object[][] data) {
			this.columnNames = columnNames;
			this.data = data;
		}

		public int getColumnCount() {
			return columnNames.length;
		}

		public int getRowCount() {
			return data.length;
		}

		public String getColumnName(int col) {
			return columnNames[col];
		}

		public Object getValueAt(int row, int col) {
			return data[row][col];
		}

		public Class getColumnClass(int c) {
			return getValueAt(0, c).getClass();
		}

	}

	private BufferedImage makePicture(ArrayList<String> struct, int imageSize, boolean isColor) {
		BufferedImage pic = new BufferedImage(
				imageSize,
				imageSize,
				BufferedImage.TYPE_INT_RGB);

		Graphics2D g = pic.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
				RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g.setFont(new Font("Courier", Font.PLAIN, 24));
		if (isColor) {
			g.setColor(BACKGROUND_COLOR);
		} else {
			g.setColor(Color.WHITE);
		}
		g.fillRect(0, 0, imageSize, imageSize);

		int x = AA_SIZE;
		int y = AA_SIZE;
		ArrayList<BackboneLine> backboneLines = new ArrayList<BackboneLine>();
		for(int i = 0; i < struct.size(); i++) {
			String line = struct.get(i);
			String noWS = line.replaceAll("\\W","");
			if (line.contains("|")) {
				int spaceCount = 0;
				for (int j = 0; j < line.length(); j++) {
					if (line.charAt(j) == '|') {
						if (spaceCount > 1) {
							x = x + Math.round(spaceCount/2) * (AA_SIZE);
							spaceCount = 0;
						} else {
							spaceCount = 0;
						}
						backboneLines.add(new BackboneLine(x, y, x, y - AA_SIZE));
						x = x + AA_SIZE;
					} else {
						spaceCount++;
					}
				}
				x = AA_SIZE;
			} else if (noWS.equals("")){

			} else {
				int spaceCount = 0;
				for (int j = 0; j < line.length(); j++) {
					char c = line.charAt(j);
					if (Character.isWhitespace(c)) {
						spaceCount++;
					} else if (Character.isLetter(c)) {
						if (spaceCount > 1) {
							x = x + Math.round(spaceCount/2) * (AA_SIZE);
							spaceCount = 0;
						} else {
							spaceCount = 0;
						}
						// draw amino acid
						if (isColor) {
							g.setColor(getAAColor(c));
							g.fillOval(x - (AA_SIZE/2), y - (AA_SIZE/2), AA_SIZE, AA_SIZE);
							g.setColor(Color.BLACK);
							if (Character.isLowerCase(c)) {
								g.drawOval(x - (AA_SIZE/2), y - (AA_SIZE/2), AA_SIZE, AA_SIZE);
							}
							g.drawString(Character.toString(c), x - (AA_SIZE/4) + 10, y + (AA_SIZE/4) - 10);
						} else {
							// lower case  = ligand ; upper case = protein
							if (Character.isLowerCase(c)) {
								g.setColor(Color.LIGHT_GRAY);
							} else {
								g.setColor(Color.GRAY);
							}
							g.fillOval(x - (AA_SIZE/2), y - (AA_SIZE/2), AA_SIZE, AA_SIZE);
							g.setColor(Color.BLACK);
							g.drawString(Character.toString(Character.toUpperCase(c)), x - (AA_SIZE/4) + 10, y + (AA_SIZE/4) - 10);
						}
						x = x + AA_SIZE;
					} else if (c == '-'){
						backboneLines.add(new BackboneLine((x - AA_SIZE), y, x, y));
					}
				}
				x = AA_SIZE;
				y = y + AA_SIZE;
			}
		}

		if (isColor) {
			g.setColor(BACKBONE_COLOR);
		} else {
			g.setColor(Color.BLACK);
		}
		for (int b = 0; b < backboneLines.size(); b++) {
			BackboneLine l = backboneLines.get(b);
			g.drawLine(l.x1, l.y1, l.x2, l.y2);
		}

		g.dispose();
		pic.flush();
		return pic;
	}

	class BackboneLine {
		public int x1;
		public int y1;
		public int x2;
		public int y2;

		public BackboneLine(int x1, int y1, int x2, int y2) {
			this.x1 = x1;
			this.y1 = y1;
			this.x2 = x2;
			this.y2 = y2;
		}
	}

	class ImagePanel extends JPanel {
		private ImageIcon image;

		public ImagePanel() {
			super(new BorderLayout());

			// make blank starting image
			BufferedImage bi = 
					new BufferedImage(IMAGE_SIZE, IMAGE_SIZE, BufferedImage.TYPE_INT_RGB);
			Graphics g = bi.getGraphics();
			g.setColor(BACKGROUND_COLOR);
			g.fillRect(0, 0, IMAGE_SIZE, IMAGE_SIZE);
			image = new ImageIcon(bi);
			g.dispose();
			this.setPreferredSize(new Dimension(IMAGE_SIZE, IMAGE_SIZE));
		}

		public void updateImage(ImageIcon newImage) {
			image = newImage;
			revalidate();
			repaint();
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(image.getImage(), 0, 0, null);
		}

	}

	private Color getAAColor(char a) {
		switch(Character.toUpperCase(a)) {
		case 'A':
			return Color.LIGHT_GRAY;
		case 'C':
			return Color.LIGHT_GRAY;
		case 'D':
			return ACID_COLOR;
		case 'E':
			return ACID_COLOR;
		case 'F':
			return Color.DARK_GRAY;
		case 'G':
			return Color.LIGHT_GRAY;
		case 'H':
			return BASE_COLOR;
		case 'I':
			return Color.DARK_GRAY;
		case 'K':
			return BASE_COLOR;
		case 'L':
			return Color.DARK_GRAY;
		case 'M':
			return Color.DARK_GRAY;
		case 'P':
			return Color.DARK_GRAY;
		case 'Q':
			return Color.WHITE;
		case 'R':
			return BASE_COLOR;
		case 'S':
			return Color.white;
		case 'T':
			return Color.white;
		case 'V':
			return Color.DARK_GRAY;
		case 'W':
			return Color.DARK_GRAY;
		case 'Y':
			return Color.WHITE;
		default:
			return Color.LIGHT_GRAY;
		}
	}

}
