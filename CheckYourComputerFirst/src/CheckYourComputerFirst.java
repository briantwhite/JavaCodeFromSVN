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
		
		String pwdCommand = "pwd";
		if (System.getProperty("os.name").toLowerCase().indexOf("win") != -1) {
			pwdCommand = "echo %cd%";
		}

		Runtime rt = Runtime.getRuntime();
		Process p;
		StringBuffer result = new StringBuffer();
		try {
			p = rt.exec(pwdCommand);
			p.waitFor();
			BufferedReader reader=new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line = reader.readLine();
			while(line != null) {
				result.append(line);
				line = reader.readLine();
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		if ((firstDigit >= 1) && (secondDigit >= 6)) {
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
				+ "<br>"
				+ result.toString()
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
