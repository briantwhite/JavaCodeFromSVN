package ComplementationTestPanel;

import javax.swing.table.DefaultTableModel;

public class ComplementationTableModel extends DefaultTableModel {
	
	private Object[][] data;
	private String[] columnHeadings;
	
	public ComplementationTableModel(Object[][] data, String[] columnHeadings) {
		super(data, columnHeadings);
		this.data = data;
		this.columnHeadings = columnHeadings;
	}

	public int getRowCount() { 
		return data.length;
	}
	public int getColumnCount() {
		return columnHeadings.length;
	}
	public String getColumnName(int col) {
		return columnHeadings[col];
	}
	public boolean isCellEditable(int r, int c) {
		return false;
	}
	public Object getValueAt(int rowIndex, int columnIndex) {
		return data[rowIndex][columnIndex];
	}
	public void setValueAt(Object value, int row, int col) {
		data[row][col] = value;
		fireTableCellUpdated(row, col);
	}
}

