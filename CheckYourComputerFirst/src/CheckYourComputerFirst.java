import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class CheckYourComputerFirst {

	static JFrame frame;
	static JLabel label;
	static String javaVersion;
	static String[] versionPieces;
	static int firstDigit;
	static int secondDigit;
	static String infoString;

	public static void main(String[] args) {
		label = new JLabel();
		frame = new JFrame("Check Your Computer First 2.0");
		frame.getContentPane().add(label);

		javaVersion = System.getProperty("java.version");
		versionPieces = javaVersion.split("[^0-9]");
		firstDigit = Integer.parseInt(versionPieces[0]);
		secondDigit = Integer.parseInt(versionPieces[1]);

		label.setOpaque(true);
		
		boolean isInZip = false;
		if (System.getProperty("user.dir").toLowerCase().contains("\\windows\\system")) isInZip = true;
		
		if ((firstDigit >= 1) && (secondDigit >= 6)) {
			label.setBackground(Color.GREEN);
			infoString = "You have a correct version of Java "
				+ "installed on your computer.<br>"
				+ "The software in this folder should run correctly.<br>";
			if (isInZip) {
				label.setBackground(Color.YELLOW);
				infoString = "You have a correct version of Java installed on your computer.<br><br>"
					+ "<b><u>However</b></u>, it appears that you have not un-zipped the folder<br>"
					+ "containing this software. The software will not run properly<br>"
					+ "unless you un-zip it. See the instructions where you <br>"
					+ "downloaded these files for how to un-zip properly.<br>";
			}
		} else {
			label.setBackground(Color.RED);
			infoString = "It appears that you do not have a correct "
				+ "version of Java installed on your computer.<br>"
				+ "The software in this folder will not run correctly.<br>"
				+ "You should go to http://www.java.com to download the latest version of Java for free.<br><br>";
			if (isInZip) {
				infoString += "<b><u>Also</b></u>, it appears that you have not un-zipped the folder<br>"
					+ "containing this software. The software will not run properly<br>"
					+ "unless you un-zip it. See the instructions where you <br>"
					+ "downloaded these files for how to un-zip properly.<br>";
			}			
		}

		label.setText("<html><body>"
				+ infoString + "<br>"
				+ "Java version " + javaVersion
				+ "<br>"
				+ System.getProperty("user.dir")
				+ "</font></body></html>");

		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		frame.pack();
		frame.setLocation(200,200);
		frame.setVisible(true);

	}

}
