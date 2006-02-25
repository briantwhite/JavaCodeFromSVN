import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class ShowWordDoublesUI extends JPanel {
	
	private InfoLabel infoLabel;
	private JTable dataTable;
	private WordPairHistoTableModel wordPairHistoModel;
	private JScrollPane scrollPane;
	private TableSorter sorter;
	private JButton cutoffButton;
	private JButton saveListToFileButton;
	private JComboBox cutoffChoice;
	private String[] cutoffNumbers = {"0", "1", "2", "3", "4", "5", 
            "6", "7", "8", "9", "10"};
	private int cutoff;
	private JFileChooser saveFileChooser;
	private int codes;
	private int[][] pairTallyTable;

	
	public ShowWordDoublesUI() {
		super();
		infoLabel = new InfoLabel("Hi there");
		this.setLayout(new BorderLayout());
		this.add(infoLabel, BorderLayout.NORTH);
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		cutoffButton = new JButton("Exclude at or below cutoff");
		buttonPanel.add(cutoffButton);
		cutoffChoice = new JComboBox(cutoffNumbers);
		buttonPanel.add(cutoffChoice);
		saveListToFileButton = new JButton("Save List to File");
		buttonPanel.add(saveListToFileButton);
		this.add(buttonPanel, BorderLayout.SOUTH);
		cutoff = 0;
		saveFileChooser = new JFileChooser();
	}
		
	public void createTable(int numRows, int numCodes, int[][] pairs){
		final int[][] pairTallyTable = pairs;
		this.pairTallyTable = pairs;
		final int codes = numCodes;
		this.codes = codes;
		wordPairHistoModel = new WordPairHistoTableModel(numRows);
		sorter = new TableSorter(wordPairHistoModel);
		dataTable = new JTable(sorter);
		sorter.setTableHeader(dataTable.getTableHeader());
		scrollPane = new JScrollPane(dataTable);
		this.add(scrollPane, BorderLayout.CENTER);			
		
		cutoffButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cutoff = cutoffChoice.getSelectedIndex();
				int tallyTotal = 0;
				for (int row = 0; row < wordPairHistoModel.getRowCount(); row++){
					int count = 
						((Integer)wordPairHistoModel.getValueAt(row,0)).intValue();
					int tally = 
						((Integer)wordPairHistoModel.getValueAt(row,1)).intValue();
					if (count > cutoff) {
						tallyTotal += tally;
					}
				}
				setInfoLabelText("There are " + tallyTotal + " pairs with" 
						+ " counts above " + cutoff);
			}
		});
		
		saveListToFileButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				StringBuffer listBuffer = new StringBuffer();
				for (int x = 0; x < codes; x++) {
					for (int y = 0; y < codes; y++){
						int count = pairTallyTable[x][y];
						if (count > cutoff) {
							listBuffer.append((x + "-" + y) + ","
									+ x + "," + y + "," + count + "\n");
						}
					}
				}
				String listString = listBuffer.toString();
				FileWriter listFileWriter = null;
				saveFileChooser.setDialogTitle("Save as a pair list file");
				if (saveFileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
					try {
						listFileWriter = new FileWriter(saveFileChooser.getSelectedFile());
						listFileWriter.write(listString);
						listFileWriter.write("\n");
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					finally {
						try {
							listFileWriter.close();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				}
			}
		});
	}
	
	public void addData(int rowNumber, int pairCount, int count){
		wordPairHistoModel.setValueAt(new Integer(pairCount),rowNumber,0);
		wordPairHistoModel.setValueAt(new Integer(count),rowNumber,1);
		wordPairHistoModel.fireTableRowsInserted(rowNumber,rowNumber);		
	}
	
	public void setInfoLabelText(String text){
		infoLabel.setText(text);
	}
	
	public TreeMap getPairMap() {
		TreeMap pairMap = new TreeMap();
		for (int x = 0; x < codes; x++) {
			for (int y = 0; y < codes; y++){
				if (pairTallyTable[x][y] > cutoff){
					String key = String.valueOf(x) + "-" + String.valueOf(y);
					pairMap.put(key, new Integer(0));
				}
			}
		}
		return pairMap;
	}
}
