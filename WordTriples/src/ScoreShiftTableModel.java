import javax.swing.table.AbstractTableModel;

public class ScoreShiftTableModel extends AbstractTableModel {
	final String[] columnHeadings = {"Original Score", 
							   "Number of hyps with original score",
			                   "Modified Score"};
	final Object[][] allScores;

	public ScoreShiftTableModel(int numRows) {
		allScores = new Object[numRows][columnHeadings.length];
	}
	
	public int getColumnCount() {
		return columnHeadings.length;
	}

	public int getRowCount() {
		return allScores.length;
	}
	
	public String getColumnName(int c) {
		return columnHeadings[c];
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
		allScores[row][col] = value;
	}
	
	public Object getValueAt(int rowIndex, int columnIndex) {
		return allScores[rowIndex][columnIndex];
	}

}
