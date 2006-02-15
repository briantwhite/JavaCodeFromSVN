import javax.swing.table.AbstractTableModel;

public class WordListTableModel extends AbstractTableModel {
	final String[] columnNames = {"Word",
								 "Count",
								 "Group",
								 "Code"};
	final Object[][] allWords;

	public WordListTableModel(int numRows) {
		allWords = new Object[numRows][columnNames.length];
	}
	
	public int getColumnCount() {
		return columnNames.length;
	}

	public int getRowCount() {
		return allWords.length;
	}
	
	public String getColumnName(int c) {
		return columnNames[c];
	}

	public Class getColumnClass(int c) {
		return getValueAt(0, c).getClass();
	}
	
	public boolean isCellEditable(int row, int col) {
		if (col == 2) {
			return true;
		} else {
			return false;
		}
	}
	
	public void setValueAt(Object value, int row, int col) {
		allWords[row][col] = value;
	}
	
	public Object getValueAt(int rowIndex, int columnIndex) {
		return allWords[rowIndex][columnIndex];
	}
	
	public Object[][] getAllWords() {
		return allWords;
	}

}
