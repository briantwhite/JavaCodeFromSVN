
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigInteger;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;

public class Encoder {
	public static void main(String[] args) {

		JFrame frame = new JFrame("Encode or Decode");

		final JTextField number = new JTextField(50);
		final JButton encodeButton = new JButton(
				"Encode into base 10 from letters");
		final JButton decodeButton = new JButton(
				"Decode into letters from base 10");
		final JButton clearButton = new JButton("Clear");

		frame.getContentPane().add(number, BorderLayout.CENTER);
		frame.getContentPane().add(encodeButton, BorderLayout.SOUTH);
		frame.getContentPane().add(decodeButton, BorderLayout.NORTH);
		frame.getContentPane().add(clearButton, BorderLayout.EAST);

		encodeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					BigInteger bigNumber = new BigInteger(number.getText()
							.toUpperCase(), 36);
					number.setText(bigNumber.toString());
				} catch (NumberFormatException exc) {
					number.setText("");
					return;
				}

			}
		});

		decodeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				BigInteger bigNumber;

				try {
					bigNumber = new BigInteger(number.getText());
				}

				catch (NumberFormatException exc) {
					number.setText("");
					return;
				}
				number.setText(bigNumber.toString(36));

			}
		});

		clearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				number.setText("");

			}
		});

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}

}