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
				infoString += "However, it appears that you have not <br>"
					+ "un-zipped the folder containing this software.<br>"
					+ "The software will not run properly unless you <br>" 
					+ "un-zip it. See the instructions where you downloaded<br>"
					+ "these files for how to un-zip properly.";
			}
		} else {
			label.setBackground(Color.RED);
			infoString = "It appears that you do not have a correct "
				+ "version of Java installed on your computer.<br>"
				+ "The software in this folder will not run correctly.<br>"
				+ "You should install by going to http://www.java.com to download the"
				+ " latest version of Java for free.<br>";
			if (isInZip) {
				infoString += "Also, it appears that you have not <br>"
					+ "un-zipped the folder containing this software.<br>"
					+ "The software will not run properly unless you <br>" 
					+ "un-zip it. See the instructions where you downloaded<br>"
					+ "these files for how to un-zip properly.";
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
