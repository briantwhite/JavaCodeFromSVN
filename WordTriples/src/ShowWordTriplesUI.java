import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import cern.colt.matrix.impl.SparseDoubleMatrix3D;

public class ShowWordTriplesUI extends JPanel {
	
	private InfoLabel infoLabel;
	private JTable dataTable;
	private WordTripleHistoTableModel wordTripleHistoModel;
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
	private SparseDoubleMatrix3D tripleTallyTable;
	private boolean cutoffSet;

	
	public ShowWordTriplesUI() {
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
		
	public void createTable(int numRows, int numCodes, 
                            SparseDoubleMatrix3D triples){
		final SparseDoubleMatrix3D tripleTallyTable = triples;
		this.tripleTallyTable = triples;
		final int codes = numCodes;
		this.codes = codes;
		wordTripleHistoModel = new WordTripleHistoTableModel(numRows);
		sorter = new TableSorter(wordTripleHistoModel);
		dataTable = new JTable(sorter);
		sorter.setTableHeader(dataTable.getTableHeader());
		scrollPane = new JScrollPane(dataTable);
		this.add(scrollPane, BorderLayout.CENTER);			
		
		dataTable.setShowGrid(true);
		dataTable.setGridColor(Color.BLACK);
		
		cutoffButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cutoff = cutoffChoice.getSelectedIndex();
				int tallyTotal = 0;
				for (int row = 0; row < wordTripleHistoModel.getRowCount(); row++){
					int count = 
						((Integer)wordTripleHistoModel.getValueAt(row,0)).intValue();
					int tally = 
						((Integer)wordTripleHistoModel.getValueAt(row,1)).intValue();
					if (count > cutoff) {
						tallyTotal += tally;
					}
				}
				setInfoLabelText("There are " + tallyTotal + " pairs with" 
						+ " counts above " + cutoff);
				cutoffSet = true;
			}
		});
		
		saveListToFileButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				StringBuffer listBuffer = new StringBuffer();
				for (int x = 0; x < codes; x++) {
					for (int y = 0; y < codes; y++){
						for (int z = 0; z < codes; z++){
							double count = tripleTallyTable.getQuick(x,y,z);
							if (count > cutoff) {
								listBuffer.append((x + "-" + y + "-" + z) + ","
										+ x + "," + y + "," + z + "," + count + "\n");
							}
						}
					}
				}
				String listString = listBuffer.toString();
				FileWriter listFileWriter = null;
				saveFileChooser.setDialogTitle("Save as a triple list file");
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
	
	public void addData(int rowNumber, int tripleCount, int count){
		wordTripleHistoModel.setValueAt(new Integer(tripleCount),rowNumber,0);
		wordTripleHistoModel.setValueAt(new Integer(count),rowNumber,1);
		wordTripleHistoModel.fireTableRowsInserted(rowNumber,rowNumber);		
	}
	
	public void setInfoLabelText(String text){
		infoLabel.setText(text);
	}
	
	public TreeMap getTripleMap() {
		TreeMap tripleMap = new TreeMap();
		for (int x = 0; x < codes; x++) {
			for (int y = 0; y < codes; y++){
				for (int z = 0; z < codes; z++){
					if (tripleTallyTable.getQuick(x,y,z) > cutoff){
						String key = String.valueOf(x) 
						             + "-" + String.valueOf(y)
						             + "-" + String.valueOf(z);
						tripleMap.put(key, new Integer(0));
					}
				}
			}
		}
		return tripleMap;
	}

	public boolean isCutoffSet() {
		return cutoffSet;
	}

	public void setCutoffSet(boolean cutoffSet) {
		this.cutoffSet = cutoffSet;
	}
}
