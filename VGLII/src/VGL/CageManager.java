package VGL;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Nikunj Koolar cs681-3 Fall 2002 - Spring 2003 Project VGL File:
 * Brian White 2008
 * CageManager.java - This class provides the dialog for the user to set the
 * visibility of the cages. He can make the cages visible/invisible by
 * selecting/deselecting them from this dialog.
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 * 
 * @author Nikunj Koolar & Brian White
 * @version 1.0 $Id$
 */
public class CageManager extends JDialog {
	/**
	 * Parameter to the set the width of the dialog
	 */
	private int dialogWidth;

	/**
	 * Parameter to the set the height of the dialog
	 */
	private int dialogHeight;

	/**
	 * Parameter to the set the X-coordinate of the dialog
	 */
	private int dialogLocationX;

	/**
	 * Parameter to the set the Y-coordinate of the dialog
	 */
	private int dialogLocationY;

	/**
	 * The panel that contains all the subpanels.
	 */
	private JPanel optionPanel;

	/**
	 * This panel contains the Dialog Label and the Table widget
	 */
	private JPanel selectionPanel;

	/**
	 * This panel contains the ok button
	 */
	private JPanel southButtonPanel;

	/**
	 * The ok button for the dialog
	 */
	private JButton okButton;

	/**
	 * The table widget that contains the listing of all the currently existing
	 * Cage Objects
	 */
	private JTable selectionTable;

	/**
	 * The scrollPane for the table (to allow for vertical scrolling when number
	 * of rows exceeds the displayable range)
	 */
	private JScrollPane scrollPane;

	/**
	 * The model that stores the actual data for the table and from which the
	 * table reads and displays data
	 */
	private SelectionTableModel selectionTableModel;

	/**
	 * Reference to the collection of CageUIs
	 */
	private ArrayList<CageUI> cageCollection = null;

	/**
	 * The constructor
	 * 
	 * @param importFrame
	 *            the reference frame for the dialog
	 * @param title
	 *            the title for the dialog
	 * @param cageCollection
	 *            the list of Cages
	 */
	public CageManager(Frame importFrame, String title, ArrayList<CageUI> cageCollection) {
		//initialize parent
		super(importFrame, true);
		setTitle(title);
		this.cageCollection = cageCollection;

		//setup the GUI of its internal components
		components();

		//setup the GUI of the dialog Box
		setupDialogBox(importFrame);
	}

	/**
	 * This method sets up the GUI and other characteristics of the internals of
	 * the Dialog Box
	 */
	private void components() {
		setupSelectionTable();
		setupPanels();
		setContentPane(optionPanel);
	}

	/**
	 * This method sets up the GUI for the Dialog Box
	 */
	private void setupDialogBox(Frame importFrame) {
		int width = 350;
		int height = 250;
		int dtHeight = (int) (getGraphicsConfiguration().getDevice()
				.getDefaultConfiguration().getBounds().getHeight());
		int dtWidth = (int) (getGraphicsConfiguration().getDevice()
				.getDefaultConfiguration().getBounds().getWidth());
		int locationX = (int) (dtWidth / 2 - width / 2);
		int locationY = (int) (dtHeight / 2 - height / 2);
		setSize(width, height);
		setLocation(new Point(locationX, locationY));
		setResizable(false);
	}

	/**
	 * This method sets up the table widget to be displayed in the dialog, and
	 * adds it to a panel
	 */
	private void setupSelectionTable() {
		selectionTableModel = new SelectionTableModel(cageCollection);
		selectionTable = new JTable(selectionTableModel);
		selectionTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		selectionTable.setRowSelectionInterval(0, 0);
		selectionTable.getTableHeader().setReorderingAllowed(false);
		selectionTable.getTableHeader().getColumnModel()
				.setColumnSelectionAllowed(false);
		selectionTable.setPreferredScrollableViewportSize(new Dimension(
				dialogWidth - 50, 65));
		selectionTable.getColumnModel().getColumn(0).setMinWidth(0);
		selectionTable.getColumnModel().getColumn(0).setMaxWidth(85);
		selectionTable.getColumnModel().getColumn(0).setPreferredWidth(75);
		JLabel dialogLabel = new JLabel(Messages.getString("VGLII.SetVisibility"));
		DefaultTableCellRenderer tRenderer = new DefaultTableCellRenderer();
		tRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		Font fn = new Font(dialogLabel.getFont().getName(), Font.BOLD,
				dialogLabel.getFont().getSize());
		tRenderer.setFont(fn);
		selectionTable.getColumnModel().getColumn(1).setCellRenderer(
				tRenderer);
		scrollPane = new JScrollPane(selectionTable);

		BorderLayout bSelectionLayout = new BorderLayout();
		bSelectionLayout.setVgap(10);
		selectionPanel = new JPanel();
		selectionPanel.setLayout(bSelectionLayout);
		selectionPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0,
				10));

		selectionPanel.add(dialogLabel, BorderLayout.NORTH);
		selectionPanel.add(scrollPane, BorderLayout.CENTER);
	}

	/**
	 * This method sets up the various panels within the Dialog Box.
	 */
	private void setupPanels() {
		setupButtons();
		optionPanel = new JPanel();
		BorderLayout bLayout = new BorderLayout();
		bLayout.setVgap(10);
		optionPanel.setLayout(bLayout);
		optionPanel.add(new JPanel(), BorderLayout.WEST);
		optionPanel.add(selectionPanel, BorderLayout.CENTER);
		optionPanel.add(new JPanel(), BorderLayout.EAST);
		southButtonPanel = new JPanel();
		JPanel okButtonPanel = new JPanel();
		JPanel cancelButtonPanel = new JPanel();
		okButtonPanel.setLayout(new BorderLayout());
		cancelButtonPanel.setLayout(new BorderLayout());
		okButtonPanel.add(okButton, BorderLayout.CENTER);
		southButtonPanel.add(okButtonPanel);
		southButtonPanel.add(cancelButtonPanel);
		optionPanel.add(southButtonPanel, BorderLayout.SOUTH);
	}

	/**
	 * This method simply creates the two buttons to accept and close or cancel
	 * and close the dialog.
	 */
	private void setupButtons() {
		okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ok();
			}
		});
	}

	/**
	 * The stuff to be done if user clicks on the OK button of the dialog
	 */
	private void ok() {
		setVisible(false);
	}

	/**
	 * The stuff to be done if user clicks on the CANCEL button of the dialog
	 */
	private void cancel() {
		setVisible(false);
	}

	/**
	 * This inner class is the TableModel used by the table widget to read in
	 * data from.
	 */
	class SelectionTableModel extends AbstractTableModel {
		/**
		 * The array of strings that holds the headers for the columns
		 */
		final String[] m_ColumnNames = { Messages.getString("VGLII.Visible"), 
				Messages.getString("VGLII.Cage") + "#" };

		/**
		 * The two dimensional array to store the table information
		 */
		final Object[][] m_RowData;

		/**
		 * A reference to the list of cages.
		 */
		private ArrayList<CageUI> cages;

		/**
		 * The constructor
		 * 
		 * @param cageCollection
		 *            the list of cages
		 */
		public SelectionTableModel(ArrayList<CageUI> cageCollection) {
			cages = cageCollection;
			int i = 0;
			m_RowData = new Object[cageCollection.size()][2];
			for (Iterator<CageUI> it = cageCollection.iterator(); it.hasNext();) {
				CageUI cageUI = it.next();
				String objName = Messages.getString("VGLII.Cage") + "# " + (cageUI.getCage().getId() + 1);
				m_RowData[i][0] = new Boolean(cageUI.isVisible());
				m_RowData[i][1] = objName;
				i++;
			}
		}

		/**
		 * Returns the total number of Columns in the table model
		 */
		public int getColumnCount() {
			return m_ColumnNames.length;
		}

		/**
		 * Returns the total number of Rows in the table model
		 */
		public int getRowCount() {
			return m_RowData.length;
		}

		/**
		 * Returns the column name of the column at index 'col'
		 */
		public String getColumnName(int col) {
			return m_ColumnNames[col];
		}

		/**
		 * Returns the value stored in the cell at Row 'row' and Column 'col'
		 */
		public Object getValueAt(int row, int col) {
			return m_RowData[row][col];
		}

		/**
		 * JTable uses this method to determine the default renderer/ editor for
		 * each cell. If we didn't implement this method, then the first column
		 * would contain text ("true"/"false"), rather than a check box.
		 */
		public Class getColumnClass(int c) {
			return getValueAt(0, c).getClass();
		}

		/**
		 * Don't need to implement this method unless your table's editable.
		 */
		public boolean isCellEditable(int row, int col) {
			//Note that the data/cell address is constant,
			//no matter where the cell appears onscreen.
			if (col == 0) {
				return true;
			} else {
				return false;
			}
		}

		/**
		 * Don't need to implement this method unless your table's data can
		 * change.
		 */
		public void setValueAt(Object value, int row, int col) {
			m_RowData[row][col] = value;
			fireTableCellUpdated(row, col);
			((CageUI) cages.get(row)).setVisible(((Boolean) value)
					.booleanValue());
		}
	}
}

