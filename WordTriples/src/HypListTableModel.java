import javax.swing.table.AbstractTableModel;

public class HypListTableModel extends AbstractTableModel {
	final String[] columnNames = {"Index",
								 "Hypothesis",
								 "Score"};
	final Object[][] allHyps;

	public HypListTableModel(int numRows) {
		allHyps = new Object[numRows][columnNames.length];
	}
	
	public int getColumnCount() {
		return columnNames.length;
	}

	public int getRowCount() {
		return allHyps.length;
	}
	
	public String getColumnName(int c) {
		return columnNames[c];
	}

	public Class getColumnClass(int c) {
		return getValueAt(0, c).getClass();
	}
	
	
	public void setValueAt(Object value, int row, int col) {
		allHyps[row][col] = value;
	}
	
	public Object getValueAt(int rowIndex, int columnIndex) {
		return allHyps[rowIndex][columnIndex];
	}

}
