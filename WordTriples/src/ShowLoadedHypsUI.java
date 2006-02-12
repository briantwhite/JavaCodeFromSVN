import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextPane;

public class ShowLoadedHypsUI extends JPanel {
	
	private InfoLabel infoLabel;
	private HypListTableModel hypListTableModel;
	private JTable hypsTable;
	private TableSorter sorter;
	private JScrollPane hypsScrollPane;


	public ShowLoadedHypsUI() {
		super();
		infoLabel = new InfoLabel("Hi there");
		this.setLayout(new BorderLayout());
		this.add(infoLabel, BorderLayout.NORTH);
	}
	
	public void createTable(int numRows){
		hypListTableModel = new HypListTableModel(numRows);
		sorter = new TableSorter(hypListTableModel);
		hypsTable = new JTable(sorter);
		sorter.setTableHeader(hypsTable.getTableHeader());
		hypsScrollPane = new JScrollPane(hypsTable);
		this.add(hypsScrollPane, BorderLayout.CENTER);	
	}
	
	public void addHyp(int rowNumber, 
			String hypNumber, 
			String hypothesisText, 
			String score){
		
		hypListTableModel.setValueAt(hypNumber,rowNumber,0);
		hypListTableModel.setValueAt(hypothesisText,rowNumber,1);
		hypListTableModel.setValueAt(score,rowNumber,2);
		hypListTableModel.fireTableRowsInserted(rowNumber,rowNumber);		
	}
	
	public void setInfoLabelText(String text){
		infoLabel.setText(text);
	}
}
