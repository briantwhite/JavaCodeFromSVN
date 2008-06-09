package VGL;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.Border;

/**
 * Nikunj Koolar cs681-3 Fall 2002 - Spring 2003 Project VGL File:
 * ExpertiseLevel.java - this class extends the JDialog class and is
 * instantiated if the practice mode option is true and allows the user to
 * select between the various modes of informative display.
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
 * @version 1.0 $Id$
 */
public class ExpertiseLevel extends JDialog {
	/**
	 * The panel to hold all the panels and other widgets in the class
	 */
	private JPanel m_SuperPanel;

	/**
	 * This variable sets the borderlayout for the superpanel
	 */
	private BorderLayout m_BorderLayout1;

	/**
	 * The array of radiobuttons, that forms the set of selections to choose
	 * from
	 */
	private JRadioButton[] m_Selections;

	/**
	 * The buttongroup instance that maintains mutual exclusion in the above
	 * array of radiobuttons
	 */
	private ButtonGroup m_Bg;

	/**
	 * The constructor
	 * 
	 * @param frame
	 *            the reference frame for the dialog
	 * @param title
	 *            the heading for the dialog
	 * @param modal
	 *            true if the dialog is to modal, false otherwise
	 */
	public ExpertiseLevel(Frame frame, String title, boolean modal) {
		super(frame, title, modal);
		try {
			jbInit();
			setPosition(frame);
			pack();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * No argument constructor
	 */
	public ExpertiseLevel() {
		this(null, "", false);
	}

	/**
	 * Method that sets the position of the dialog
	 * 
	 * @param frm
	 *            the reference frame of the dialog
	 */
	private void setPosition(Frame frm) {
		int width = 285;
		int height = 180;
		int dtHeight = (int) (getGraphicsConfiguration().getDevice()
				.getDefaultConfiguration().getBounds().getHeight());
		int dtWidth = (int) (getGraphicsConfiguration().getDevice()
				.getDefaultConfiguration().getBounds().getWidth());
		int locationX = (int) (dtWidth / 2 - width / 2);
		int locationY = (int) (dtHeight / 2 - height / 2);
		setLocation(new Point(locationX, locationY));
		setResizable(false);
	}

	/**
	 * This method sets up the dialog
	 * 
	 * @throws Exception
	 */
	private void jbInit() throws Exception {
		m_SuperPanel = new JPanel();
		m_BorderLayout1 = new BorderLayout();
		m_Selections = new JRadioButton[2];
		m_Bg = new ButtonGroup();
		m_SuperPanel.setLayout(m_BorderLayout1);
		m_SuperPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		JPanel selectionPanel = new JPanel();
		GridLayout gl = new GridLayout(3, 0);
		selectionPanel.setLayout(gl);
		Border etched = BorderFactory.createEtchedBorder();
		selectionPanel.setBorder(etched);
		m_Selections[0] = new JRadioButton("Don't show model or genotypes",
				true);
		m_Selections[1] = new JRadioButton("Show model and all genotypes",
				false);
		m_Bg.add(m_Selections[0]);
		m_Bg.add(m_Selections[1]);
		JLabel heading = new JLabel("Select mode");
		selectionPanel.add(heading);
		selectionPanel.add(m_Selections[0]);
		selectionPanel.add(m_Selections[1]);
		m_SuperPanel.add(selectionPanel, BorderLayout.NORTH);
		JButton continueButton = new JButton("Continue");
		continueButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				setVisible(false);
			}
		});
		JPanel southButtonPanel = new JPanel();
		southButtonPanel.setLayout(new BorderLayout());
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(0, 3));
		buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		buttonPanel.add(continueButton);
		southButtonPanel.add(buttonPanel, BorderLayout.CENTER);
		m_SuperPanel.add(Box.createRigidArea(new Dimension(0, 10)),
				BorderLayout.CENTER);
		m_SuperPanel.add(southButtonPanel, BorderLayout.SOUTH);
		getContentPane().add(m_SuperPanel);
	}

	/**
	 * This method returns the selection made by the user from amongst the set
	 * of radio button selections
	 * 
	 * @return the selection in terms of an int value
	 */
	public int getSelection() {
		if (m_Selections[0].isSelected())
			return 0;
		else
			return 1;
	}
}