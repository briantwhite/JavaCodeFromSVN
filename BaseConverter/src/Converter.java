
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigInteger;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Converter {

	static JFrame frame = new JFrame("Base Converter");

	static JPanel topPanel = new JPanel(new GridLayout(2, 2));

	static JPanel middlePanel = new JPanel();

	static JPanel bottomPanel = new JPanel(new GridLayout(2, 2));

	static final JTextField inputNumberText = new JTextField(20);

	static final JTextField outputNumberText = new JTextField(20);

	static final JTextField inputBaseValue = new JTextField(3);

	static final JTextField outputBaseValue = new JTextField(3);

	static final JSlider inputBaseSlider = new JSlider(2, 36);

	static final JSlider outputBaseSlider = new JSlider(2, 36);

	public static void main(String[] args) {

		topPanel.add(inputNumberText);
		topPanel.add(new JLabel("in base:"));

		topPanel.add(inputBaseValue);
		topPanel.add(inputBaseSlider);

		middlePanel.add(new JLabel(
				"<html><hr><font size=+3 color=red>EQUALS</font></html>"));

		bottomPanel.add(outputNumberText);
		bottomPanel.add(new JLabel("in base:"));

		bottomPanel.add(outputBaseValue);
		bottomPanel.add(outputBaseSlider);

		frame.getContentPane().add(topPanel, BorderLayout.NORTH);
		frame.getContentPane().add(middlePanel, BorderLayout.CENTER);
		frame.getContentPane().add(bottomPanel, BorderLayout.SOUTH);

		inputBaseValue.setEditable(false);
		outputNumberText.setEditable(false);
		outputBaseValue.setEditable(false);

		inputNumberText.setText("100");

		inputBaseSlider.setMajorTickSpacing(5);
		inputBaseSlider.setMinorTickSpacing(1);
		inputBaseSlider.setPaintTicks(true);
		inputBaseSlider.setPaintLabels(true);
		inputBaseSlider.setSnapToTicks(true);
		inputBaseSlider.setValue(10);

		outputBaseSlider.setMajorTickSpacing(5);
		outputBaseSlider.setMinorTickSpacing(1);
		outputBaseSlider.setPaintTicks(true);
		outputBaseSlider.setPaintLabels(true);
		outputBaseSlider.setSnapToTicks(true);
		outputBaseSlider.setValue(5);

		inputBaseSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				updateDisplay();
			}
		});

		outputBaseSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				updateDisplay();
			}
		});

		inputNumberText.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateDisplay();
			}
		});

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
		updateDisplay();
	}

	public static void updateDisplay() {
		Integer inputBase = new Integer(inputBaseSlider.getValue());
		inputBaseValue.setText(inputBase.toString());

		Integer outputBase = new Integer(outputBaseSlider.getValue());
		outputBaseValue.setText(outputBase.toString());

		try {
			BigInteger inputNumber = new BigInteger(inputNumberText.getText(),
					inputBase.intValue());

			outputNumberText.setText(inputNumber
					.toString(outputBase.intValue()));
		} catch (NumberFormatException exc) {
			outputNumberText.setText("Impossible!");
		}

	}

}