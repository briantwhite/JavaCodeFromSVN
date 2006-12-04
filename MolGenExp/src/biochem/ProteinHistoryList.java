package biochem;

import java.io.Serializable;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import molGenExp.MolGenExp;

public class ProteinHistoryList extends JList {
	DefaultListModel histListDataModel;
	MolGenExp mge;
	
	public ProteinHistoryList(ListModel dataModel, final MolGenExp mge) {
		super(dataModel);
		this.mge = mge;
		this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.setCellRenderer(new ProteinHistoryCellRenderer());
		histListDataModel = (DefaultListModel)dataModel;
		this.setFixedCellWidth(20);
		this.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				if (getValueIsAdjusting()) {
					return;
				}
				if (isSelectionEmpty()) {
					mge.getProtex().setButtonsEnabled(false);
				} else {
					mge.getProtex().setButtonsEnabled(true);
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
			JOptionPane.showMessageDialog(this, "You have not selected an "
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
