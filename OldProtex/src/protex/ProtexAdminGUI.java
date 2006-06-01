// ProtexAdminGUI.java
//
//
// Copyright 2004-2005 MGX Team UMB.  All rights reserved.
/* 
 * License Information
 * 
 * This  program  is  free  software;  you can redistribute it and/or modify it 
 * under  the  terms  of  the GNU General Public License  as  published  by the 
 * Free  Software  Foundation; either version 2  of  the  License,  or (at your 
 * option) any later version.
 */

package protex;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;
import java.util.Hashtable;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.NumberFormatter;

/**
 *
 * @author Namita Singla 
 * @version v0.1 
 */
public class ProtexAdminGUI extends JFrame implements FoldingObserver {

	public int SLIDER_INIT = 0;

	private static boolean blackColoring = false;

	private Attributes attrib;

	private JButton foldButton, exitButton;

	private JRadioButton bent, straight;

	private JLabel breakTiesLabel;

	protected static StringBuffer buffer = new StringBuffer();

	private JFormattedTextField hydroPhobicTextField, hydrogenWeightField,
			ionicWeightField;

	private NumberFormat numberFormat = java.text.NumberFormat
			.getNumberInstance();

	private NumberFormatter formatter = new NumberFormatter(numberFormat);

	private JPanel inputOutputPanel;

	protected static JPanel inputPanel;

	private JPanel breakTiesPanel, outputPanel;

	private FoldingManager manager;

	private ButtonGroup breakTiesGroup = new ButtonGroup();

	private Hashtable labelTable;

	/**
	 * Constructor
	 */
	public ProtexAdminGUI() {
		setTitle("Protex Beta: Administrator Mode");
		setBackground(Color.white);
		init();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();

		//Center the frame
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = this.getSize();
		if (frameSize.height > screenSize.height) {
			frameSize.height = screenSize.height;
		}
		if (frameSize.width > screenSize.width) {
			frameSize.width = screenSize.width;
		}
		this.setLocation((screenSize.width - frameSize.width) / 2,
				(screenSize.height - frameSize.height) / 2);
		setVisible(true);

		attrib = null;
	}

	/**
	 * Initialize components
	 *  
	 */
	public void init() {

		getContentPane().setLayout(new BorderLayout());

		manager = FoldingManager.getInstance();
		manager.setBlackColoring(blackColoring);
		manager.attach(this);

		formatter.setMinimum(new Double(0));
		formatter.setMaximum(new Double(1));
		hydroPhobicTextField = new JFormattedTextField(formatter);
		hydroPhobicTextField.setColumns(5);
		hydrogenWeightField = new JFormattedTextField(formatter);
		hydrogenWeightField.setColumns(5);
		ionicWeightField = new JFormattedTextField(formatter);
		ionicWeightField.setColumns(5);

		//Label table for sliders
		labelTable = new Hashtable();
		labelTable.put(new Integer(0), new JLabel("0"));
		labelTable.put(new Integer(1000), new JLabel("1"));

		// Add components to main frame
		getContentPane().add(createButtonPanel(), BorderLayout.SOUTH);
		getContentPane().add(createWeightAndPalettePanel(), BorderLayout.NORTH);
		getContentPane().add(createInputOutputPanel(), BorderLayout.CENTER);
	}

	/**
	 * Report statistics, create and add canvas showing folded protein
	 */
	public void doneFolding(Attributes a) {
		//report statistics
		System.out.println(manager.report());
		manager.createCanvas((OutputPalette) outputPanel);
		outputPanel.repaint();
	}

	/**
	 * Initialize different atrributes Most of them are fixed as required by
	 * client
	 * 
	 * @return Attributes
	 */
	private Attributes createAttributes() {
		attrib = new Attributes(buffer.toString(), "standard", (straight
				.isSelected() ? "straight" : "bent"), "hexagonal",
				"incremental", "8", "4", hydroPhobicTextField.getText(),
				hydrogenWeightField.getText(), ionicWeightField.getText(), null, 
				null);
		return attrib;
	}

	/**
	 * Create button panel with "Fold" and "Exit" buttons
	 * 
	 * @return JPanel
	 */
	protected JPanel createButtonPanel() {
		JPanel buttonPanel = new JPanel();
		foldButton = new JButton("FOLD");
		exitButton = new JButton("EXIT");
		foldButton.setEnabled(true);
		exitButton.setEnabled(true);
		foldButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				try {
					manager.fold(createAttributes());
				} catch (FoldingException ex) {
					ex.printStackTrace();
				}
			}
		});

		exitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				showExitMessage();
			}

		});

		buttonPanel.add(foldButton);
		buttonPanel.add(exitButton);
		return buttonPanel;
	}

	/**
	 * Create weight and palette panel
	 * 
	 * @return JPanel
	 */
	private JPanel createWeightAndPalettePanel() {

		// Create vertical box to add all panels for
		// Hydrophobic weight, hydrogen weight and ionic weight
		Box weightPanelsBox = Box.createVerticalBox();

		// Hydrophobic weight panel
		JPanel hydroPhobicWeightPanel = createWeightPanel(
				"HydroPhobic Weight:", hydroPhobicTextField);

		// Hydrogen weight panel
		JPanel hydrogenBondWeightPanel = createWeightPanel(
				"Hydrogen Weight:      ", hydrogenWeightField);

		// Ionic weight panel
		JPanel ionicBondWeightPanel = createWeightPanel(
				"Ionic Weight:              ", ionicWeightField);

		// Add panels to box
		weightPanelsBox.add(hydroPhobicWeightPanel);
		weightPanelsBox.add(hydrogenBondWeightPanel);
		weightPanelsBox.add(ionicBondWeightPanel);

		//Main panel
		JPanel panel = new JPanel();

		//Amino acid palette panel
		JPanel palette = new JPanel();
		palette
				.setBorder(BorderFactory
						.createTitledBorder("AminoAcid Palette"));
		JPanel palettePanel = new AminoAcidPalette(225, 180, 4, 5, true);
		palette.add(palettePanel);

		// Add palette and weight panel to main panel
		panel.add(weightPanelsBox);
		panel.add(palette);

		return panel;
	}

	/**
	 * Create Weight panel
	 * 
	 * @return JPanel
	 */
	private JPanel createWeightPanel(String label,
			final JFormattedTextField textField) {
		JPanel weightPanel = new JPanel();
		weightPanel.add(new JLabel(label));
		weightPanel.add(textField);

		textField.setValue(new Double(0));
		checkForValidInput(textField);

		final JSlider weightSlider = new JSlider(JSlider.HORIZONTAL, 0, 1000,
				SLIDER_INIT);

		//Listeners to synchronize values of slider and textfield
		textField.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent e) {
				if ("value".equals(e.getPropertyName())) {
					Number value = (Number) e.getNewValue();
					double newVal = value.doubleValue() * 1000;
					if (weightSlider != null && value != null) {
						weightSlider.setValue((int) newVal);
					}
				}
			}
		});

		weightSlider.setBorder(BorderFactory.createTitledBorder(label));
		weightSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider) e.getSource();
				int fps = (int) source.getValue();
				if (!source.getValueIsAdjusting()) { //done adjusting
					textField.setValue(new Double((double) fps / 1000));
				} else { //value is adjusting; just set the text
					textField.setText(String.valueOf((double) fps / 1000));
				}
			}
		});

		weightSlider.setLabelTable(labelTable);
		weightSlider.setPaintTicks(true);
		weightSlider.setPaintLabels(true);
		weightPanel.add(weightSlider);
		return weightPanel;
	}

	/**
	 * Check for valid input in TextField
	 * 
	 * @param textField
	 */
	private void checkForValidInput(final JFormattedTextField textField) {
		textField.getInputMap().put(
				KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "check");
		textField.getActionMap().put("check", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				if (!textField.isEditValid()) { //The text is invalid.
					Toolkit.getDefaultToolkit().beep();
					textField.selectAll();
				} else
					try { //The text is valid,
						textField.commitEdit(); //so use it.
					} catch (java.text.ParseException exc) {
					}
			}
		});
	}

	/**
	 * Create Input and Output panel
	 * 
	 * @return
	 */
	protected JPanel createInputOutputPanel() {
		inputOutputPanel = new JPanel(new BorderLayout());
		inputOutputPanel.add(createInputPanel(), BorderLayout.NORTH);
		inputOutputPanel.add(createBreakTiesPanel(), BorderLayout.SOUTH);
		inputOutputPanel.add(createOutputCanvasPanel(), BorderLayout.CENTER);
		return inputOutputPanel;
	}

	/**
	 * Break ties panel
	 * 
	 * @return JPanel
	 */
	protected JPanel createBreakTiesPanel() {
		breakTiesPanel = new JPanel();
		breakTiesLabel = new JLabel(" Break Ties: ");
		straight = new JRadioButton("straight", true);
		bent = new JRadioButton("bent", false);
		breakTiesGroup.add(straight);
		breakTiesGroup.add(bent);
		breakTiesPanel.add(breakTiesLabel);
		breakTiesPanel.add(straight);
		breakTiesPanel.add(bent);
		return breakTiesPanel;
	}

	/**
	 * Create output canvas panel
	 * 
	 * @return JPanel
	 */
	protected JPanel createOutputCanvasPanel() {
		outputPanel = new OutputPalette();
		outputPanel.setOpaque(true); //content panes must be opaque
		return outputPanel;
	}

	/**
	 * Create Input panel
	 * 
	 * @return
	 */
	protected JPanel createInputPanel() {
		inputPanel = new InputPalette(true);
		inputPanel.setOpaque(true); //content panes must be opaque
		return inputPanel;
	}

	/**
	 * Display exit message with yes/no option
	 *  
	 */
	private void showExitMessage() {

		int result = JOptionPane.showConfirmDialog(null,
				"Do you want to exit Folding Window?", "Exit Folding Window?",
				JOptionPane.YES_NO_OPTION);
		if (result == JOptionPane.YES_OPTION)
			System.exit(1);
	}

	/**
	 * Main entry point
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			String nativeLF = UIManager.getSystemLookAndFeelClassName();
			UIManager.setLookAndFeel(nativeLF);
		} catch (Exception e) {
			e.printStackTrace();
		}
		JFrame frame = new ProtexAdminGUI();
		JFrame.setDefaultLookAndFeelDecorated(true);
		SwingUtilities.updateComponentTreeUI(frame);
		frame.setVisible(true);
	}
}

