import javax.swing.table.AbstractTableModel;

public class WordPairHistoTableModel extends AbstractTableModel {
	final String[] columnNames = {"Pair Count",
								 "Number of pairs with that count"};
	final Object[][] allData;

	public WordPairHistoTableModel(int numRows) {
		allData = new Object[numRows][columnNames.length];
	}
	
	public int getColumnCount() {
		return columnNames.length;
	}

	public int getRowCount() {
		return allData.length;
	}
	
	public String getColumnName(int c) {
		return columnNames[c];
	}

	public Class getColumnClass(int c) {
		return getValueAt(0, c).getClass();
	}
	
	
	public void setValueAt(Object value, int row, int col) {
		allData[row][col] = value;
	}
	
	public Object getValueAt(int rowIndex, int columnIndex) {
		return allData[rowIndex][columnIndex];
	}

}
