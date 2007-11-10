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

public class TargetShapeList extends JList {
	DefaultListModel targetShapeListDataModel;
	
	public TargetShapeList(ListModel dataModel, final Protex protex) {
		super(dataModel);
		this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.setCellRenderer(new TargetShapeCellRenderer());
		targetShapeListDataModel = (DefaultListModel)dataModel;
		this.setFixedCellWidth(20);
		this.addMouseListener(new MouseAdapter() {
			
			public void mouseClicked(MouseEvent evt) {
				if (evt.getClickCount() == 2) {
					protex.setSelectedTargetShape(
							(TargetShape)
							((TargetShapeList)
									evt.getSource()).getSelectedValue());
				}
			}
		});

	}
	
	public void add(TargetShape ts) {
		targetShapeListDataModel.addElement(ts);
	}
	
	public void deleteSelected() {
		if (getSelectedIndex() != -1 ) {
			targetShapeListDataModel.removeElementAt(getSelectedIndex());
		} else {
			JOptionPane.showMessageDialog(null, "You have not selected an "
					+ "item to delete.",
					"None Selected", JOptionPane.WARNING_MESSAGE);
		}
	}
	
	public void clearList() {
		targetShapeListDataModel.removeAllElements();
	}
	
	public Object[] getAll() {
		return targetShapeListDataModel.toArray();
	}

}
