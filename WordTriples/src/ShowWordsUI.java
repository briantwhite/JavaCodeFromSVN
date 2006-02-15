import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

public class ShowWordsUI extends JPanel {
	
	private InfoLabel infoLabel;
	private WordListTableModel wordListTableModel;
	private JTable wordsTable;
	private TableSorter sorter;
	private JScrollPane wordsScrollPane;
	private JPanel buttonPanel;
	private JButton resetGroupButton;
	private JButton makeAllGroupZbutton;
	private JButton assignCodesButton;
	private JButton saveToFileButton;

	public ShowWordsUI() {
		super();
		infoLabel = new InfoLabel("Hi there");
		buttonPanel = new JPanel();
		resetGroupButton = new JButton("Reset Group Assignments");
		makeAllGroupZbutton = new JButton("Set Selected to Group Z");
		assignCodesButton = new JButton("Assign Codes");
		saveToFileButton = new JButton("Save");
		this.setLayout(new BorderLayout());
		this.add(infoLabel, BorderLayout.NORTH);
		
		buttonPanel.setLayout(new GridLayout(1,4));
		buttonPanel.add(resetGroupButton);
		buttonPanel.add(makeAllGroupZbutton);
		buttonPanel.add(assignCodesButton);
		buttonPanel.add(saveToFileButton);
		this.add(buttonPanel, BorderLayout.SOUTH);
	}
	
	public void createTable(int numRows){
		wordListTableModel = new WordListTableModel(numRows);
		sorter = new TableSorter(wordListTableModel);
		wordsTable = new JTable(sorter);
		sorter.setTableHeader(wordsTable.getTableHeader());
		wordsScrollPane = new JScrollPane(wordsTable);
		this.add(wordsScrollPane, BorderLayout.CENTER);	
		
		wordListTableModel.addTableModelListener(new TableModelListener() {

			public void tableChanged(TableModelEvent e) {
				int row = e.getFirstRow();
				int col = e.getColumn();
					System.out.println("row:" + row + " col:" + col);
			}
			
		});
	}
	
	public void addWord(int rowNumber, 
			String wordText, 
			int count,
			String group,
			int code){
		
		wordListTableModel.setValueAt(wordText,rowNumber,0);
		wordListTableModel.setValueAt(new Integer(count),rowNumber,1);
		wordListTableModel.setValueAt(group,rowNumber,2);
		wordListTableModel.setValueAt(new Integer(code),rowNumber,3);
		wordListTableModel.fireTableRowsInserted(rowNumber,rowNumber);		
	}
	
	public void setInfoLabelText(String text){
		infoLabel.setText(text);
	}
}
