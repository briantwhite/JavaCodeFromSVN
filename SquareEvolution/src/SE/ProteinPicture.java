package SE;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


public class ProteinPicture extends JFrame{

	private static final Color BACKGROUND_COLOR = new Color(128, 255, 128);
	private static final Color BACKBONE_COLOR = Color.magenta;
	
	private static final Color BASE_COLOR = new Color(128, 128, 255);
	private static final Color ACID_COLOR = new Color(255, 128, 128);
	
	private static final int IMAGE_SIZE = 600;
	private static final int AA_SIZE = 30;

	private DefaultListModel sequenceListModel;
	HashMap<String, ArrayList<String>> sequencesAndStructures;
	private ImagePanel imagePanel;

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
	}

	class ApplicationCloser extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			System.exit(0);
		}
	}

	private void start() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Select the output.txt file");
		int retVal = fileChooser.showOpenDialog(this);
		if (retVal == JFileChooser.APPROVE_OPTION) {
			sequencesAndStructures = new HashMap<String, ArrayList<String>>();
			File file = fileChooser.getSelectedFile();
			BufferedReader reader = null;
			String text = null;
			try {
				reader = new BufferedReader(new FileReader(file));
				while ((text = reader.readLine()) != null) {
					// start with a DNA sequence line
					if (text.matches("^[AGCT][AGCT]+")) {
						// next line is the protein sequence
						String proteinSeq = reader.readLine().replaceAll("\\W","");
						if (!proteinSeq.equals("")) {
							// next lines are structure
							ArrayList<String> structureLines = new ArrayList<String>();
							text = reader.readLine();
							while (!text.contains("Run")) {
								structureLines.add(text);
								text = reader.readLine();
							}
							sequencesAndStructures.put(proteinSeq, structureLines);
						}
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
			resultsDialog.setLayout(new FlowLayout());

			sequenceListModel = new DefaultListModel();
			JList proteins = new JList(sequenceListModel);
			Iterator<String> it = sequencesAndStructures.keySet().iterator();
			int i = 0;
			while (it.hasNext()) {
				String protSeq = it.next();
				sequenceListModel.add(i, protSeq);
				i++;
			}
			proteins.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			proteins.setLayoutOrientation(JList.VERTICAL);
			proteins.setVisibleRowCount(-1);
			proteins.setFont(new Font("Monospaced", Font.PLAIN, 12));
			JScrollPane listScroller = new JScrollPane(proteins);
			listScroller.setPreferredSize(new Dimension(300,300));
			resultsDialog.add(listScroller);

			proteins.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent evt) {
					JList list = (JList) evt.getSource();
					String workFileName =
						(sequenceListModel.get((list.locationToIndex(evt.getPoint())))).toString();
					showStructure(workFileName);
				}
			});

			imagePanel = new ImagePanel();
			resultsDialog.add(imagePanel);

			resultsDialog.pack();
			resultsDialog.setVisible(true);
		}
	}

	private void showStructure(String sequence) {
		ArrayList<String> struct = sequencesAndStructures.get(sequence);
		imagePanel.updateImage(new ImageIcon(makePicture(struct, IMAGE_SIZE)));
	}

	private BufferedImage makePicture(ArrayList<String> struct, int imageSize) {
		BufferedImage pic = new BufferedImage(
				imageSize,
				imageSize,
				BufferedImage.TYPE_INT_RGB);
		Graphics2D g = pic.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
				RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g.setColor(BACKGROUND_COLOR);
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
						g.setColor(getAAColor(c));
						g.fillOval(x - (AA_SIZE/2), y - (AA_SIZE/2), AA_SIZE, AA_SIZE);
						g.setColor(Color.BLACK);
						if (Character.isLowerCase(c)) {
							g.drawOval(x - (AA_SIZE/2), y - (AA_SIZE/2), AA_SIZE, AA_SIZE);
						}
						g.drawString(Character.toString(c), x - (AA_SIZE/4), y + (AA_SIZE/4));
						x = x + AA_SIZE;
					} else if (c == '-'){
						backboneLines.add(new BackboneLine((x - AA_SIZE), y, x, y));
					}
				}
				x = AA_SIZE;
				y = y + AA_SIZE;
			}
		}
		
		g.setColor(BACKBONE_COLOR);
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
