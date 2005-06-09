
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 * Nikunj Koolar cs681-3 Fall 2002 - Spring 2003 Project VGL File:
 * OrganismUI.java - this class handles the UI associated with the Organism
 * object
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
 * @author Nikunj Koolar
 * @version 1.0 $Id: OrganismUI.java,v 1.2 2005-06-09 17:16:48 brian Exp $
 */
public class OrganismUI extends JLabel implements MouseListener {
	/**
	 * The reference to the organism object
	 */
	private Organism m_Organism;

	/**
	 * This field is used to decide the selectd/deselected state of the
	 * organismUI
	 */
	private boolean m_IsOrganismSelected = false;

	/**
	 * The image to be shown when the organism is selected
	 */
	private ImageIcon m_ImageSelected;

	/**
	 * The image to be shown when the organism is deselected
	 */
	private ImageIcon m_ImageDeselected;

	/**
	 * A reference to the selection vial object. This reference is used by the
	 * organism to add itself to the selection vial's currently selected male-
	 * female set.
	 */
	private SelectionVial m_Vial;

	/**
	 * A list of references to all the OrganismUI objects that are existing as
	 * parents in other windows. This helps in maintaining a single organism,
	 * with its reflections existing in other cages where its a parent. Only the
	 * organismUI object that is not a parent object can create and maintain
	 * this list
	 */
	private ArrayList m_SameOrganismReferences = null;

	/**
	 * True is this organismUI is existing as a parent in a cage, false
	 * otherwise. default - false
	 */
	private boolean m_IsParent = false;

	/**
	 * The reference to the orginial organism object. This reference is used by
	 * organismUI objects that are existing as parents in other cages. This
	 * references helps the parent objects to propagate selection changes to the
	 * main organism object.
	 */
	private OrganismUI m_CentralOrganismUI = null;

	/**
	 * True if problem is currently being worked upon in beginner's mode, false
	 * otherwise
	 */
	private boolean m_IsBeginnersMode;

	/**
	 * The constructor
	 * 
	 * @param organism
	 *            the organism object that this organismUI represents
	 * @param isParent
	 *            true if this organismUI exists as a parent in a cage, false
	 *            otherwise
	 * @param isbeginnersmode
	 *            true if beginner's mode is enabled false otherwise
	 * @param sv
	 *            a reference to the selection vial
	 */
	public OrganismUI(Object organism, boolean isParent,
			boolean isbeginnersmode, SelectionVial sv) {
		super();
		m_Organism = (Organism) organism;
		m_IsParent = isParent;
		m_IsBeginnersMode = isbeginnersmode;
		m_Vial = sv;
		if (m_Organism.getSexType() == 0) {
			if (m_IsParent) {
				URL m_ImageSelectedURL = OrganismUI.class
						.getResource("UIimages/malegreen.gif");
				m_ImageSelected = new ImageIcon(m_ImageSelectedURL);
			} else {
				URL m_ImageSelectedURL = OrganismUI.class
						.getResource("UIimages/male.gif");
				m_ImageSelected = new ImageIcon(m_ImageSelectedURL);
			}
			URL m_ImageDeselectedURL = OrganismUI.class
					.getResource("UIimages/maleblack.gif");
			m_ImageDeselected = new ImageIcon(m_ImageDeselectedURL);
			setIcon(m_ImageDeselected);
		} else {
			if (m_IsParent) {
				URL m_ImageSelectedURL = OrganismUI.class
						.getResource("UIimages/femalegreen.gif");
				m_ImageSelected = new ImageIcon(m_ImageSelectedURL);
			} else {
				URL m_ImageSelectedURL = OrganismUI.class
						.getResource("UIimages/female.gif");
				m_ImageSelected = new ImageIcon(m_ImageSelectedURL);
			}
			URL m_ImageDeselectedURL = OrganismUI.class
					.getResource("UIimages/femaleblack.gif");
			m_ImageDeselected = new ImageIcon(m_ImageDeselectedURL);
			setIcon(m_ImageDeselected);
		}

		if (!m_IsParent) {
			m_SameOrganismReferences = new ArrayList();
			m_CentralOrganismUI = this;
		}
		addMouseListener(this);
	}

	/**
	 * Sets the selected state of the OrganismUI
	 * 
	 * @param selected
	 *            true if the organismUI is to be set as selected, false if the
	 *            organismUI is to be set as deselected
	 */
	public void setSelected(boolean selected) {
		if (selected) {
			setIcon(m_ImageSelected);
			m_IsOrganismSelected = true;
		} else {
			setIcon(m_ImageDeselected);
			m_IsOrganismSelected = false;
		}
		if (!m_IsParent)
			refreshParents(selected, m_SameOrganismReferences);
	}

	/**
	 * Returns the organism object that this UI represents
	 * 
	 * @return the organism object
	 */
	public Organism getOrganism() {
		return m_Organism;
	}

	/**
	 * Implementation for mouse listener, sets the organismUI as
	 * selected/deselected depending on the previous state of the organismUI
	 * 
	 * @param e
	 *            the mouse event
	 */
	public void mousePressed(MouseEvent e) {
		if (m_IsOrganismSelected)
			refreshOrganism(false, null);
		else
			refreshOrganism(true, this);
	}

	/**
	 * Implementation for mouse listener, does nothing
	 * 
	 * @param e
	 */
	public void mouseReleased(MouseEvent e) {
	}

	/**
	 * Implementation for mouse listener, does nothing
	 * 
	 * @param e
	 */
	public void mouseClicked(MouseEvent e) {
	}

	/**
	 * Implementation for mouse listener, does nothing
	 * 
	 * @param e
	 */
	public void mouseEntered(MouseEvent e) {
	}

	/**
	 * Implementation for mouse listener, does nothing
	 * 
	 * @param e
	 */
	public void mouseExited(MouseEvent e) {
	}

	/**
	 * If this object is a parent refresh the selected/deselected state of the
	 * original organism (which then refreshes the selected/deselected state of
	 * each of the parents iteratively including this parent itself) else if
	 * this object is not a parent then simply refresh selected/deselected state
	 * of itself which will result in the refreshing of the selected/deselected
	 * state of each its existing parents iteratively
	 * 
	 * @param selected
	 *            true if the organismUI is selected , false otherwise
	 * @param organismUI
	 *            reference to the original organismUI (used to set the
	 *            currently selected organism in the selection vial by the
	 *            parent organismUI object)
	 */
	private void refreshOrganism(boolean selected, OrganismUI organismUI) {
		if (!m_IsParent) {
			setSelected(selected);
			if (m_Organism.getSexType() == 0)
				m_Vial.setMaleParent(organismUI);
			else
				m_Vial.setFemaleParent(organismUI);
		} else {
			OrganismUI setterUI;
			if (selected)
				setterUI = m_CentralOrganismUI;
			else
				setterUI = null;
			if (m_Organism.getSexType() == 0)
				m_Vial.setMaleParent(setterUI);
			else
				m_Vial.setFemaleParent(setterUI);
			m_CentralOrganismUI.setSelected(selected);
		}
	}

	/**
	 * Refreshes the selected/deselected state of all existing parents of the
	 * current organismUI object. This method can only be invoked if the
	 * organism is not a parent
	 * 
	 * @param isselected
	 *            true if the organism is selected, false otherwise
	 * @param references
	 *            the list of parents to be refreshed
	 */
	private void refreshParents(boolean isselected, ArrayList references) {
		Iterator it = references.iterator();
		while (it.hasNext()) {
			OrganismUI o = (OrganismUI) it.next();
			o.setSelected(isselected);
		}
	}

	/**
	 * Returns the list of parent references of the organism
	 * 
	 * @return the list of parents
	 */
	public ArrayList getReferencesList() {
		if (m_SameOrganismReferences != null)
			return m_SameOrganismReferences;
		else
			return null;
	}

	/**
	 * Sets the original organismUI for this parent organismUI.
	 * 
	 * @param organismUI
	 *            the original organismUI
	 */
	public void setCentralOrganismUI(OrganismUI organismUI) {
		m_CentralOrganismUI = organismUI;
	}

	/**
	 * Set the tooltip text to be displayed depending whether the balloon help
	 * is enabled or not.
	 * 
	 * @param selected
	 *            true if balloon help is on, false if balloon help is off
	 */
	public void setBalloonHelp(boolean selected) {
		String info = "";
		if (selected) {
			if (m_IsBeginnersMode)
				info = "Genotype: " + m_Organism.getGenotype1()
						+ " ; " + m_Organism.getGenotype2();
			else
				info = " Phenotype: " + m_Organism.getPhenotype();
		}
		setToolTipText(info);
	}
}