import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

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
		frame = new JFrame("Check Your Computer First.");
		frame.getContentPane().add(label);
		
		javaVersion = System.getProperty("java.version");
		versionPieces = javaVersion.split("[^0-9]");
		firstDigit = Integer.parseInt(versionPieces[0]);
		secondDigit = Integer.parseInt(versionPieces[1]);
		
		label.setOpaque(true);
		
		if ((firstDigit >= 1) && (secondDigit >= 4)) {
			label.setBackground(Color.GREEN);
			infoString = "You have a correct version of Java "
				+ "installed on your computer.<br>"
				+ "The software on this CD should run correctly.<br>";
		} else {
			label.setBackground(Color.RED);
			infoString = "It appears that you do not have a correct "
				+ "version of Java installed on your computer.<br>"
				+ "The software on this CD will not run correctly.<br>"
				+ "You should install the version of Java found on this CD<br>"
				+ "or go to http://java.sun.com to download the"
				+ " latest version of Java for free.<br>";
		}
		
		label.setText("<html><body>"
				+ infoString + "<br>"
				+ "Java version " + javaVersion
				+ "</font></body></html>");
		
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		frame.pack();
		frame.setLocation(200,200);
		frame.show();

	}

}
