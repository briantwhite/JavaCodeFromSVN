import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextPane;

public class ShowWordsUI extends JPanel {
	
	private InfoLabel infoLabel;
	private WordListTableModel wordListTableModel;
	private JTable wordsTable;
	private TableSorter sorter;
	private JScrollPane wordsScrollPane;


	public ShowWordsUI() {
		super();
		infoLabel = new InfoLabel("Hi there");
		this.setLayout(new BorderLayout());
		this.add(infoLabel, BorderLayout.NORTH);
	}
	
	public void createTable(int numRows){
		wordListTableModel = new WordListTableModel(numRows);
		sorter = new TableSorter(wordListTableModel);
		wordsTable = new JTable(sorter);
		sorter.setTableHeader(wordsTable.getTableHeader());
		wordsScrollPane = new JScrollPane(wordsTable);
		this.add(wordsScrollPane, BorderLayout.CENTER);	
	}
	
	public void addWord(int rowNumber, 
			String wordText, 
			int count,
			int group){
		
		wordListTableModel.setValueAt(new Integer(rowNumber),rowNumber,0);
		wordListTableModel.setValueAt(wordText,rowNumber,1);
		wordListTableModel.setValueAt(new Integer(count),rowNumber,2);
		wordListTableModel.setValueAt(new Integer(group),rowNumber,3);
		wordListTableModel.fireTableRowsInserted(rowNumber,rowNumber);		
	}
	
	public void setInfoLabelText(String text){
		infoLabel.setText(text);
	}
}
