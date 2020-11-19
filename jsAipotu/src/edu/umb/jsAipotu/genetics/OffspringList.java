package edu.umb.jsAipotu.genetics;

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

import preferences.GlobalDefaults;


import molBiol.ExpressedGene;
import molGenExp.CustomListSelectionModel;
import molGenExp.MolGenExp;
import molGenExp.Organism;
import molGenExp.OrganismCellRenderer;

import biochem.FoldedProteinWithImages;
import biochem.PaintedInACornerFoldingException;

// the panel that shows the reults of a cross or mutation
public class OffspringList extends JList {

	DefaultListModel offspringListDataModel;
	MolGenExp mge;

	public OffspringList (ListModel dataModel, final MolGenExp mgeX) {
		super(dataModel);
		this.mge = mgeX;
		this.setCellRenderer(new OrganismCellRenderer());
		offspringListDataModel = (DefaultListModel)dataModel;
		this.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		this.setSelectionModel(new CustomListSelectionModel(mge, this));
		this.setVisibleRowCount(-1);	//pack in as many as will fit 
		this.setFixedCellWidth(60);
		this.setLayoutOrientation(JList.VERTICAL_WRAP);

		this.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					try {
						mge.loadOrganismIntoActivePanel(
								(Organism)getSelectedValue());
					} catch (PaintedInACornerFoldingException e1) {
						JOptionPane.showMessageDialog(mge, 
								GlobalDefaults.paintedInACornerNotice,
								"Folding Error", JOptionPane.WARNING_MESSAGE);
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
		offspringListDataModel.addElement(o);
	}

	public void deleteSelected() {
		if (getSelectedIndex() != -1 ) {
			offspringListDataModel.removeElementAt(getSelectedIndex());
		} else {
			JOptionPane.showMessageDialog(null, "You have not selected an "
					+ "item to delete.",
					"None Selected", JOptionPane.WARNING_MESSAGE);
		}
	}

	public void clearList() {
		offspringListDataModel.removeAllElements();
	}

	public Object[] getAll() {
		return offspringListDataModel.toArray();
	}

	public boolean nameExistsAlready(String newName) {
		Object[] allOrgs = offspringListDataModel.toArray();
		ArrayList allNames = new ArrayList();
		for (int i = 0; i < allOrgs.length; i++) {
			allNames.add(((Organism)allOrgs[i]).getName());
		}
		return allNames.contains(newName);
	}

	public Organism getSelectedOrganism() {
		return (Organism)offspringListDataModel.getElementAt(
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
