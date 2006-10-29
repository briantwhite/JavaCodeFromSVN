package molGenExp;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.Serializable;
import java.util.ArrayList;

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

	boolean inGeneticsMode;
	DefaultListModel greenhouseDataModel;
	MolGenExp mge;

	public Greenhouse (ListModel dataModel, final MolGenExp mgeX) {
		super(dataModel);
		inGeneticsMode = true;
		this.setCellRenderer(new GreenhouseCellRenderer());
		greenhouseDataModel = (DefaultListModel)dataModel;
		this.setFixedCellWidth(145);
		this.mge = mgeX;

		this.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {
				if (inGeneticsMode) {
					if (e.getClickCount() == 1) {
						mge.thisOrganismWasClicked(
								(Organism)getSelectedValue());
					}
				} else {
					if (e.getClickCount() == 2) {
						mge.loadOrganismIntoActivePanel(
								(Organism)getSelectedValue());
					}
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

	public void setInGeneticsMode(boolean mode) {
		inGeneticsMode = mode;
	}
}
