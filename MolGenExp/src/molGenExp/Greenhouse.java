package molGenExp;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
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

	public Greenhouse (ListModel dataModel, final MolGenExp mgeX) {
		super(dataModel);
		this.mge = mgeX;
		this.setCellRenderer(new OrganismCellRenderer());
		greenhouseDataModel = (DefaultListModel)dataModel;
		this.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		this.setSelectionModel(new CustomListSelectionModel(mge, this));
		this.setFixedCellWidth(80);

		this.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {				
				if (e.getClickCount() == 2) {
					mge.loadOrganismIntoActivePanel(
							(Organism)getSelectedValue());
				}
			}
			public void mouseEntered(MouseEvent arg0) {}
			public void mouseExited(MouseEvent arg0) {}
			public void mousePressed(MouseEvent arg0) {}
			public void mouseReleased(MouseEvent arg0) {}
		});
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

	public boolean nameExistsAlready(String newName) {
		Object[] allOrgs = greenhouseDataModel.toArray();
		ArrayList allNames = new ArrayList();
		for (int i = 0; i < allOrgs.length; i++) {
			allNames.add(((Organism)allOrgs[i]).getName());
		}
		return allNames.contains(newName);
	}

	public Organism getSelectedOrganism() {
		return (Organism)greenhouseDataModel.getElementAt(
				getSelectedIndex());
	}
	
	public void setDefaultSelectionSettings() {
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setSelectionModel(new DefaultListSelectionModel());
	}
	
	public void setCustomSelectionSettings() {
		setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		setSelectionModel(new CustomListSelectionModel(mge, this));
	}

}
