import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class ShowWordsUI extends JPanel {
	
	private final int TEXT_COL = 0;
	private final int COUNT_COL = 1;
	private final int GROUP_COL = 2;
	private final int CODE_COL = 3;
	
	private InfoLabel infoLabel;
	private WordListTableModel wordListTableModel;
	private JTable wordsTable;
	private TableSorter sorter;
	private JScrollPane wordsScrollPane;
	private JPanel buttonPanel;
	private JButton resetGroupButton;
	private JButton makeAllGroupZButton;
	private String[] cutoffNumbers = {"0", "1", "2", "3", "4", "5", 
			            "6", "7", "8", "9", "10"};
	private JComboBox cutoffCount;
	private JButton assignCodesButton;
	private JButton saveToFileButton;
	private ListSelectionModel selectedRowsModel;
	private JFileChooser saveFileChooser;

	public ShowWordsUI() {
		super();
		infoLabel = new InfoLabel("Hi there");
		buttonPanel = new JPanel();
		resetGroupButton = new JButton("Reset Group Assignments");
		makeAllGroupZButton = new JButton("Set at or below cutoff to Group Z");
		assignCodesButton = new JButton("Assign Codes");
		saveToFileButton = new JButton("Save");
		this.setLayout(new BorderLayout());
		this.add(infoLabel, BorderLayout.NORTH);
		
		buttonPanel.setLayout(new FlowLayout());
		buttonPanel.add(resetGroupButton);
		buttonPanel.add(makeAllGroupZButton);
		cutoffCount = new JComboBox(cutoffNumbers);
		buttonPanel.add(cutoffCount);
		buttonPanel.add(assignCodesButton);
		buttonPanel.add(saveToFileButton);
		this.add(buttonPanel, BorderLayout.SOUTH);
		
		selectedRowsModel = null;
		saveFileChooser = new JFileChooser();
	}
	
	public void createTable(int numRows){
		wordListTableModel = new WordListTableModel(numRows);
		sorter = new TableSorter(wordListTableModel);
		wordsTable = new JTable(sorter);
		sorter.setTableHeader(wordsTable.getTableHeader());
		wordsScrollPane = new JScrollPane(wordsTable);
		this.add(wordsScrollPane, BorderLayout.CENTER);	
		
		wordsTable.setShowGrid(true);
		wordsTable.setGridColor(Color.BLACK);
		wordsTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		ListSelectionModel wordSelectionModel = wordsTable.getSelectionModel();
		wordSelectionModel.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				selectedRowsModel = (ListSelectionModel)e.getSource();
			}
		});
				
		resetGroupButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for (int i = 0; i < wordListTableModel.getRowCount(); i++ ){
					wordListTableModel.setValueAt("",i,GROUP_COL);
				}				
				wordListTableModel.fireTableDataChanged();
			}
		});

		makeAllGroupZButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for (int i = 0; i < wordListTableModel.getRowCount(); i++ ){
					if (((Integer)wordListTableModel.getValueAt(i,COUNT_COL)).intValue() 
							<= cutoffCount.getSelectedIndex()) {
						wordListTableModel.setValueAt("Z",i,GROUP_COL);
					}
				}				
				wordListTableModel.fireTableDataChanged();
			}
		});

		assignCodesButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// first, find out how many groups there are
				// and set up codes for them
				HashMap groupMap = new HashMap();
				int groupCounter = 1;
				for (int i = 0; i < wordListTableModel.getRowCount(); i++ ){
					String currentGroup = (String)wordListTableModel.getValueAt(i,GROUP_COL);
					if ((!currentGroup.equals("")) && (!currentGroup.equals("Z"))
							&& !groupMap.containsKey(currentGroup)) {
						groupMap.put(currentGroup, new Integer(groupCounter));	
						groupCounter++;
					}
				}				
								
				//now, go through and assign codes
				// if group = Z - give it group 0 "don't care"
				// if group is neither Z nor "", give it the group's #
				// if group = "" - give it a serial # starting after last lettered group
				for (int i = 0; i < wordListTableModel.getRowCount(); i++ ) {
					String currentGroup = (String)wordListTableModel.getValueAt(i,GROUP_COL);
					if (currentGroup.equals("Z")) {
						wordListTableModel.setValueAt(new Integer(0),i,CODE_COL);
					} 
					if (currentGroup.equals("")){
						wordListTableModel.setValueAt(new Integer(groupCounter),i,CODE_COL);
						groupCounter++;
					}
					if (groupMap.containsKey(currentGroup)){
						Integer code = (Integer)groupMap.get(currentGroup);
						wordListTableModel.setValueAt(code,i,CODE_COL);
					}
				}
				wordListTableModel.fireTableDataChanged();
				wordListTableModel.setCodesAssigned(true);
				infoLabel.setText("I assigned " + (groupCounter - 1) + " different codes.");
			}
		});

		saveToFileButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FileWriter wordFileWriter = null;
				saveFileChooser.setDialogTitle("Save as a word list file");
				if (saveFileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
					try {
						wordFileWriter = new FileWriter(saveFileChooser.getSelectedFile());
						for (int i = 0; i < wordListTableModel.getRowCount(); i++ ){
							wordFileWriter.write(wordListTableModel.getValueAt(i,TEXT_COL)
									+ "\t" + wordListTableModel.getValueAt(i,COUNT_COL)
									+ "\t" + wordListTableModel.getValueAt(i,GROUP_COL)
									+ "\t" + wordListTableModel.getValueAt(i,CODE_COL)
									+ "\n");
						}
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					finally {
						try {
							wordFileWriter.close();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				}
			}
		});
		
	}
	
	public void addWord(int rowNumber, 
			String wordText, 
			int count,
			String group,
			int code){
		
		wordListTableModel.setValueAt(wordText,rowNumber,TEXT_COL);
		wordListTableModel.setValueAt(new Integer(count),rowNumber,COUNT_COL);
		wordListTableModel.setValueAt(group,rowNumber,GROUP_COL);
		wordListTableModel.setValueAt(new Integer(code),rowNumber,CODE_COL);
		wordListTableModel.fireTableRowsInserted(rowNumber,rowNumber);		
	}
	
	public void setInfoLabelText(String text){
		infoLabel.setText(text);
	}
	
	public WordListTableModel getWordListTableModel() {
		return wordListTableModel;
	}
	
}
