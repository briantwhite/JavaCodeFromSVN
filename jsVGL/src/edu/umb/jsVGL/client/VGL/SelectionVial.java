package edu.umb.jsVGL.client.VGL;

import javax.swing.JLabel;

import GeneticModels.Organism;

/**
 * Nikunj Koolar cs681-3 Fall 2002 - Spring 2003 Project VGL File:
 * Brian White 2008
 * SelectionVial.java
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
public class SelectionVial {
	/**
	 * Reference to the male organism parent
	 */
	private OrganismUI m_MaleParent;

	/**
	 * Reference to the female organism parent
	 */
	private OrganismUI m_FemaleParent;

	/**
	 * Reference to the status-panel's display label to display the current
	 * selections
	 */
	private JLabel m_DisplayLabel;

	/**
	 * The constructor
	 */
	public SelectionVial() {
	}

	/**
	 * The constructor
	 * 
	 * @param displayLabel
	 *            the label to which the current selections should printed
	 */
	public SelectionVial(JLabel displayLabel) {
		m_DisplayLabel = displayLabel;
	}

	/**
	 * Sets the male parent
	 * 
	 * @param maleParent -
	 *            the male parent to be set as currently selected
	 */
	public void setMaleParent(OrganismUI maleParent) {
		if (m_MaleParent != null)
			m_MaleParent.setSelected(false);
		m_MaleParent = maleParent;
		updateDisplayLabel();
	}

	/**
	 * Sets female parent
	 * 
	 * @param femaleParent -
	 *            the female parent to be set as currently selected
	 */
	public void setFemaleParent(OrganismUI femaleParent) {
		if (m_FemaleParent != null)
			m_FemaleParent.setSelected(false);
		m_FemaleParent = femaleParent;
		updateDisplayLabel();
	}

	/**
	 * method to get the currently selected male parent
	 * 
	 * @return the male parent if any is selected else null
	 */
	public OrganismUI getMaleParent() {
		if (m_MaleParent != null)
			return m_MaleParent;
		else
			return null;
	}

	/**
	 * method to get the currently selected female parent
	 * 
	 * @return the female parent if any is selected else null
	 */
	public OrganismUI getFemaleParent() {
		if (m_FemaleParent != null)
			return m_FemaleParent;
		else
			return null;
	}

	/**
	 * Refreshes the display label to display the current male-female organism
	 * selections
	 */
	private void updateDisplayLabel() {
		if (m_DisplayLabel != null) {
			String maleInfo = "";
			String femaleInfo = "";
			if (m_MaleParent != null) {
				Organism o1 = m_MaleParent.getOrganism();
				maleInfo = "[ " + Messages.getInstance().getString("VGLII.Male") + " (" 
				+ Messages.getInstance().getString("VGLII.Cage") + "# " + (o1.getCageId() + 1) + ") "
						+ o1.getPhenotypeString() + "]";
			}

			if (m_FemaleParent != null) {
				Organism o2 = m_FemaleParent.getOrganism();
				femaleInfo = "[ " + Messages.getInstance().getString("VGLII.Female") 
				+ " (" + Messages.getInstance().getString("VGLII.Cage") + "# " + (o2.getCageId() + 1) + ") "
						+ o2.getPhenotypeString() + "]";
			}
			m_DisplayLabel.setText(" ");
			m_DisplayLabel.repaint();
			m_DisplayLabel.setText(femaleInfo + "  " + maleInfo);
			m_DisplayLabel.repaint();
		}
	}
}