// IOPanel.java
//
//
// Copyright 2004-2005 MGX Team UMB. All rights reserved.
/* 
 * License Information
 * 
 * This  program  is  free  software;  you can redistribute it and/or modify it 
 * under  the  terms  of  the GNU General Public License  as  published  by the 
 * Free  Software  Foundation; either version 2  of  the  License,  or (at your 
 * option) any later version.
 */
/*
 * The following organization of a public class is recommended by X. Jia [2004: 
 * Object Oriented Software Development Using Java(TM). Addison Wesley, Boston, 
 * 677 pp.]
 *
 *     public class AClass {
 *         (public constants)
 *         (public constructors)
 *         (public accessors)
 *         (public mutators)
 *         (nonpublic fields)
 *         (nonpublic auxiliary methods or nested classes)
 *     }
 *
 * Jia also recommends the following design guidelines.
 *
 *     1. Avoid public fields.  There should be no nonfinal public fields, 
 *        except when a class is final and the field is unconstrained.
 *     2. Ensure completeness of the public interface.  The set of public 
 *        methods defined in the class should provide full and convenient 
 *        access to the functionality of the class.
 *     3. Separate interface from implementation.  When the functionality 
 *        supported by a class can be implemented in different ways, it is 
 *        advisable to separate the interface from the implementation.
 * 
 * Created:  03 Mar 2005 
 * Modified: 10 May 2005 (David Portman/MGX Team UMB)
 */

package protex;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.Border;

/**
 * ProtexMainGUI contains two IOPanel objects. Each IOPanel holds an inputPanel,
 * and outputCanvasPanel and a buttonPanel. These panels are treated as fields
 * here; but they could also be objects.
 * 
 * @author David Portman/MGX Team UMB
 */
public class IOPanel extends JPanel implements MouseListener {

	/**
	 * Special class created to control changes of JButton text field in method
	 * setText().
	 * 
	 * @author David Portman/MGX Team UMB
	 */
	public class PpPIdButton extends JButton {

		public PpPIdButton(String s) {
			super.setText(s);
		}

		/**
		 * Set the String text value in the IOPanel JButton bar.
		 * 
		 * @param s
		 *            String Id
		 */
		public void setText() {
			if (pp == null)
				super.setText(defaultText);
			else if (pp.isInHistory())
				super.setText(pp.getId());
		}
	}

	/**
	 * Constructor.
	 * 
	 * @param name String name of this IOPanel.
	 */
	public IOPanel(String name) {
		super(new BorderLayout());

		lineBorder = null;
		this.setBorder(lineBorder);

		// create the inputPanel
		inputPanel = new InputPalette(false);
		inputPanel.setBackground(offColor);
		inputPanel.setOpaque(true);
		inputPanel.addMouseListener(this);
		((InputPalette) inputPanel).setIOPanel(this);

		// create the outputCanvasPanel
		outputCanvasPanel = new OutputPalette();
		outputCanvasPanel.setBackground(offColor);
		outputCanvasPanel.setOpaque(true);
		outputCanvasPanel.addMouseListener(this);
		((OutputPalette) outputCanvasPanel).setIOPanel(this);

		// create the buttonPanel
		buttonPanel = new JPanel();
		buttonPanel.setBackground(offColor);
		buttonPanel.addMouseListener(this);

		// push buttons EDIT, FOLD and CLEAR
		//  these buttons don't need MouseListeners
		editButton = new JButton("EDIT");
		editButton.addActionListener(new EditButtonListener(this));
		editButton.setEnabled(true);

		foldButton = new JButton("FOLD");
		foldButton.addActionListener(new FoldButtonListener(this));
		foldButton.setEnabled(true);

		clearButton = new JButton("CLEAR");
		clearButton.addActionListener(new ClearButtonListener(this));
		clearButton.setEnabled(true);

		// display buttons
		JLabel idLabel = new JLabel(" ID: ");
		pppIdButton = new PpPIdButton("     ");
		pppIdButton.setForeground(Color.BLACK);
		pppIdButton.addMouseListener(this);
		pppIdButton.setEnabled(false);

		JLabel colorLabel = new JLabel(" Color: ");
		colorButton = new JButton("     ");
		// added by TJ -- fix the color of the colorLabel
		colorButton.setContentAreaFilled(false);
		colorButton.setOpaque(true);
		
		colorButton.setBackground(offColor);
		colorButton.addMouseListener(this);
		colorButton.setEnabled(false);

		// pack in the various components
		buttonPanel.add(editButton);
		buttonPanel.add(foldButton);
		buttonPanel.add(clearButton);

		buttonPanel.add(idLabel);
		buttonPanel.add(pppIdButton);

		buttonPanel.add(colorLabel);
		buttonPanel.add(colorButton);

		this.add(inputPanel, BorderLayout.NORTH);
		this.add(outputCanvasPanel, BorderLayout.CENTER);
		this.add(buttonPanel, BorderLayout.SOUTH);

		// assign the name of this IOPanel
		this.name = name;
	}

	// accessors

	/**
	 * 
	 * @return JPanel
	 */
	public JPanel getInputPanel() {
		return inputPanel;
	}

	/**
	 * 
	 * @return JPanel
	 */
	public JPanel getOutputCanvasPanel() {
		return outputCanvasPanel;
	}

	/**
	 * 
	 * @return Returns the pppIdButton from buttonPanel.
	 */
	public PpPIdButton getPPPIdButton() {
		return pppIdButton;
	}

	/**
	 * 
	 * @return Returns the colorButton from buttonPanel.
	 */
	public JButton getColorButton() {
		return colorButton;
	}

	/**
	 * @return String name of this IOPanel.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return Return the Polypeptide currently associated with this IOPanel.
	 */
	public Polypeptide getPolypeptide() {
		return pp;
	}

	// mutator methods

	/**
	 * Set this IOPanel to active state.
	 *  
	 */
	public void setToActive() {
		setActiveColor(true);
		setBorder(BorderFactory.createRaisedBevelBorder());
	}

	/**
	 * Set this IOPanel to inactive state.
	 *  
	 */
	public void setToInActive() {
		setActiveColor(false);
		setBorder(BorderFactory.createLoweredBevelBorder());
	}

	/**
	 * Associate a Polypeptide with this IOPanel; adjust the value of isChanged
	 * (boolean), and write the ppId in the pppIdButton of the JButton panel.
	 * 
	 * @param pp Polypeptide.
	 */
	public void setPolypeptide(Polypeptide pp) {
		this.pp = pp;
		((InputPalette) getInputPanel()).setIsChangedTo(false);
		((OutputPalette) getOutputCanvasPanel()).setTitle();
		getPPPIdButton().setText();
	}

	/**
	 * Clear this IOPanel.
	 * 
	 */
	public void clearAll() {
		clearInputPalette();
		clearOutputPalette();
	}

	// implementation of inherited methods

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

	/**
	 * If mouse is clicked, make current IOPanel the active panel.
	 * 
	 * @param event MouseEvent.
	 */
	public void mouseClicked(MouseEvent event) {
//		ProtexMainApp.activeIOPanel = this;
//		ProtexMainApp.setActiveIOPanelBorder();
//		System.out.println("Shifting keyboard focus");
//		this.getKeyboardFocus(); // added by TJ -- shift focus to active inputPanel
		ProtexMainApp.resetIOPanel(this);
	}
	public void getKeyboardFocus(){
		((InputPalette)inputPanel).getKeyboardFocus();
	}

	// non-public classes

	/*
	 * EditButtonListener class implementing ActionListener and referencing this
	 * IOPanel.
	 * 
	 * @author David Portman/MGX Team UMB
	 */
	private class EditButtonListener implements ActionListener {

		/**
		 * Constructor.
		 * 
		 * @param i IOPanel.
		 */
		public EditButtonListener(IOPanel i) {
			super();
			this.iOP = i;
		}

		public void actionPerformed(ActionEvent evt) {

			// ensure that this IOPanel is active
			ProtexMainApp.activeIOPanel = iOP;
			ProtexMainApp.setActiveIOPanelBorder();

			// enable folding of AminoAcid chain in InputPalette
			((InputPalette) iOP.getInputPanel()).setIsChangedTo(true);

			// clear the OutputPalette
			iOP.clearOutputPalette();
		}

		private IOPanel iOP;
	}

	/*
	 * FoldButtonListener class implementing ActionListener and referencing this
	 * IOPanel.
	 * 
	 * @author David Portman/MGX Team UMB
	 */
	private class FoldButtonListener implements ActionListener {

		/**
		 * Constructor.
		 * 
		 * @param i IOPanel.
		 */
		public FoldButtonListener(IOPanel i) {
			super();
			this.iOP = i;
		}

		public void actionPerformed(ActionEvent evt) {

			// ensure that this IOPanel is active
			ProtexMainApp.activeIOPanel = iOP;
			ProtexMainApp.setActiveIOPanelBorder();

			// access existing Polypeptide, if any (pp can be null)
			InputPalette iP = (InputPalette) iOP.getInputPanel();
			Polypeptide pp = iOP.getPolypeptide();

			// fold a Polypeptide; do not attempt unless there are 
			//	AminoAcids in the InputPalette
			if (iP.getAminoAcids().size() > 0) {
				if (pp == null 
						|| !pp.isInHistory() || iP.getIsChanged()) {
					iOP.nullifyReferences(null);
					((InputPalette) iP).setIsChangedTo(true);
					ProtexMainApp.foldChain(null);
				}
			}
		}

		private IOPanel iOP;
	}

	/*
	 * ClearButtonListener class implementing ActionListener and referencing
	 * this IOPanel.
	 * 
	 * @author David Portman/MGX Team UMB
	 */
	private class ClearButtonListener implements ActionListener {

		/**
		 * Constructor.
		 * 
		 * @param i IOPanel.
		 */
		public ClearButtonListener(IOPanel i) {
			super();
			this.iOP = i;
		}

		public void actionPerformed(ActionEvent evt) {

			// ensure that this IOPanel is active
			ProtexMainApp.activeIOPanel = iOP;
			ProtexMainApp.setActiveIOPanelBorder();

			if (pp == null) {
				iOP.clearAll();
				return;
			}
			
			// prompt for confirmation
			if (!pp.isInHistory() && !pp.isInOtherIOPanel(iOP)) {
				int result = 0;

				if (iOP.equals(ProtexMainApp.upperIOPanel))
					result = JOptionPane.showConfirmDialog(null, pp.getId()
							+ " is no longer in History."
							+ "\nClear Upper Work Panel anyway?",
							"Clear Work Panel", JOptionPane.YES_NO_OPTION);
				else if (iOP.equals(ProtexMainApp.lowerIOPanel))
					result = JOptionPane.showConfirmDialog(null, pp.getId()
							+ " is no longer in History."
							+ "\nClear Lower Work Panel anyway?",
							"Clear Work Panel", JOptionPane.YES_NO_OPTION);

				if (result == JOptionPane.NO_OPTION)
					return;
			}

			iOP.clearAll();
		}

		private IOPanel iOP;
	}

	private Color onColor = 
		new Color((float) 0.6, (float) 0.6, (float) 0.9);

	protected static Color offColor = Color.LIGHT_GRAY;

	private String name, defaultText = "     ";

	private Border lineBorder;

	private JPanel inputPanel, outputCanvasPanel, buttonPanel;

	private JButton colorButton;
	private JButton editButton, foldButton, clearButton;

	private PpPIdButton pppIdButton;

	private Polypeptide pp;

	/*
	 * Set the correct background color reflecting the state of this IOPanel.
	 * 
	 * @param b boolean
	 */
	private void setActiveColor(boolean b) {
		inputPanel.setBackground(b ? onColor : offColor);
		outputCanvasPanel.setBackground(b ? onColor : offColor);
		buttonPanel.setBackground(b ? onColor : offColor);
	}

	/*
	 * 
	 */
	private void clearInputPalette() {
		// remove contents of InputPalette
		InputPalette inputPanel = (InputPalette) getInputPanel();
		inputPanel.removeAll();
		inputPanel.repaint();
	}

	/*
	 * 
	 */
	protected void clearOutputPalette() {
		
		// nullify references to a Polypeptide
		Polypeptide ppFromHistory = nullifyReferences(null);
	
		// refresh buttonPanel (pppIdButton and colorButton)
		pppIdButton.setText(defaultText);
		colorButton.setBackground(offColor);
		
		// remove graphic in OutputCanvasPalette
		OutputPalette outputPanel = 
			(OutputPalette) getOutputCanvasPanel();
		outputPanel.removeAll();
		outputPanel.repaint();
		
		// added by TJ -- reset mixedColorLabel in ProtexMainApp
		ProtexMainApp.resetMixedColorLabel();
	}

	/**
	 * Set to null various references to the objects from this IOPanel.
	 * 
	 * @param ppFromHistory Polypeptide.
	 * @return Polypeptide with references set to null.
	 */
	protected Polypeptide nullifyReferences(Polypeptide ppFromHistory) {

		// unassign Polypeptide from this IOPanel
		if (pp != null) {
			Polypeptide ppInOtherIOPanel = null;

			// if this Polypeptide is referenced in History,
			//	remove its reference from History
			if (pp.isInHistory()) {
				ppFromHistory = pp.getUHL()[1];

				if (this.equals(ProtexMainApp.upperIOPanel)) {
					ppFromHistory.setInUpperIOPanelTo(false);
					ppInOtherIOPanel = ppFromHistory.getUHL()[2];
					ppFromHistory.setUHL(null, ppFromHistory, ppInOtherIOPanel);

					// if this Polypeptide is referenced by the Polypeptide
					//	in the Lower work panel, remove its reference there
					if (ppInOtherIOPanel != null) {
						ppInOtherIOPanel.setInUpperIOPanelTo(false);
						ppInOtherIOPanel.setUHL(null, ppFromHistory,
								ppInOtherIOPanel);
					}
				} else if (this.equals(ProtexMainApp.lowerIOPanel)) {
					ppFromHistory.setInLowerIOPanelTo(false);
					ppInOtherIOPanel = ppFromHistory.getUHL()[0];
					ppFromHistory.setUHL(ppInOtherIOPanel, ppFromHistory, null);

					// if this Polypeptide is referenced by the Polypeptide
					//	in the Upper work panel, remove its reference there
					if (ppInOtherIOPanel != null) {
						ppInOtherIOPanel.setInLowerIOPanelTo(false);
						ppInOtherIOPanel.setUHL(ppInOtherIOPanel,
								ppFromHistory, null);
					}
				} else {
					System.out.println("\nIOPanel.nullifyReferences(): "
							+ "ERROR - You should never get here.");
				}
			}
			this.setPolypeptide(null);
		}

		return ppFromHistory;
	}
}
