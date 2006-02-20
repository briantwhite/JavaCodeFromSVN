import javax.swing.table.AbstractTableModel;

public class ScoredHypTableModel extends AbstractTableModel {
	String[] columnHeadings;
	Object[][] allHyps;

	public ScoredHypTableModel(int numRows, String[] words) {
		allHyps = new Object[numRows][words.length + 1];
		columnHeadings = new String[words.length + 1];
		columnHeadings[0] = "#";
		columnHeadings[1] = "Hypothesis";
		for (int i = 1; i < (words.length); i++){
			columnHeadings[i + 1] = words[i];
		}
	}
	
	public int getColumnCount() {
		return columnHeadings.length;
	}

	public int getRowCount() {
		return allHyps.length;
	}
	
	public String getColumnName(int c) {
		return columnHeadings[c];
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
