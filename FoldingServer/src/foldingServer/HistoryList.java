package protex;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Serializable;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;

public class HistoryList extends JList implements Serializable {
	DefaultListModel histListDataModel;
	HistListPopupMenu histListPopupMenu;
	
	public HistoryList(ListModel dataModel, Protex protex) {
		super(dataModel);
		this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.setCellRenderer(new HistoryCellRenderer());
		histListDataModel = (DefaultListModel)dataModel;
		this.setFixedCellWidth(20);
		histListPopupMenu = new HistListPopupMenu(this, protex);
		this.addMouseListener(new MouseAdapter() {
			
			public void mouseClicked(MouseEvent evt) {
				if (evt.getClickCount() == 2) {
					int x = evt.getX(); 
					int y = evt.getY();
					histListPopupMenu.show((Component)evt.getSource(), x-10, y-2 );
				}
			}
		});

	}
	
	public void add(FoldedPolypeptide fp) {
		histListDataModel.addElement(fp);
	}
	
	public void deleteSelected() {
		if (getSelectedIndex() != -1 ) {
			histListDataModel.removeElementAt(getSelectedIndex());
		} else {
			JOptionPane.showMessageDialog(null, "You have not selected an "
					+ "item to delete.",
					"None Selected", JOptionPane.WARNING_MESSAGE);
		}
	}
	
	public void clearList() {
		histListDataModel.removeAllElements();
	}
	
	public Object[] getAll() {
		return histListDataModel.toArray();
	}

}
