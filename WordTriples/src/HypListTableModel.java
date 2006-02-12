import javax.swing.table.AbstractTableModel;

public class HypListTableModel extends AbstractTableModel {
	final String[] columnNames = {"Index",
								 "Hypothesis",
								 "Score"};
	final String[][] allHyps;

	public HypListTableModel(int numRows) {
		allHyps = new String[numRows][columnNames.length];
	}
	
	public int getColumnCount() {
		return columnNames.length;
	}

	public int getRowCount() {
		return allHyps.length;
	}

	public void setValueAt(String value, int row, int col) {
		allHyps[row][col] = value;
	}
	
	public Object getValueAt(int rowIndex, int columnIndex) {
		return allHyps[rowIndex][columnIndex];
	}

}
