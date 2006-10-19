package molGenExp;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.Serializable;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.border.LineBorder;

import molBiol.ExpressedGene;

import biochem.FoldedPolypeptide;

public class Greenhouse extends JList implements Serializable {
	
	DefaultListModel greenhouseDataModel;
	MolGenExp mge;
	
	public Greenhouse (ListModel dataModel, MolGenExp mge) {
		super(dataModel);
		this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.setCellRenderer(new GreenhouseCellRenderer());
		greenhouseDataModel = (DefaultListModel)dataModel;
		this.setFixedCellWidth(20);
		this.mge = mge;
	}
	
	public void add(Organism o) {
		greenhouseDataModel.addElement(o);
	}
	
	public void deleteSelected() {
		if (getSelectedIndex() != -1 ) {
			greenhouseDataModel.removeElementAt(getSelectedIndex());
		} else {
			JOptionPane.showMessageDialog(null, "You have not selected an "
					+ "item to delete.",
					"None Selected", JOptionPane.WARNING_MESSAGE);
		}
	}
	
	public void clearList() {
		greenhouseDataModel.removeAllElements();
	}
	
	public Object[] getAll() {
		return greenhouseDataModel.toArray();
	}

}
