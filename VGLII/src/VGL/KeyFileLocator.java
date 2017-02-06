package VGL;

import java.io.File;

/*
 * Developed to deal with Apple's AppTranslocation security process
 * 	OS X 10.12 moves the App to an obfuscated directory 
 * 	away from Problems/ and .key files
 *  two part fix:
 *  - put Problems/ in the .app and sign it
 *  - have this class to try to find the key files
 *    - first check if location set in preferences
 *    - then canonical directories
 *    - finally ask if they're serious and if yes, 
 *      have the user select the directory where they are
 *        (this is then set in preferences)
 * 
 * see development_log 2/2017 for details
 */
public class KeyFileLocator {
	private static File studentKeyFilePath = null;
	private static File graderKeyFilePath = null;
	private static File instructorKeyFilePath = null;
	
	private static String homeDirHeader = System.getProperty("user.home") + System.getProperty("file.separator");
	private static String[] typicalLocations = {homeDirHeader + "Desktop" + System.getProperty("file.separator"),
												homeDirHeader + "Documents" + System.getProperty("file.separator"),
												homeDirHeader + "Downloads" + System.getProperty("file.separator"),
												homeDirHeader + "Applications" + System.getProperty("file.separator"),
												System.getProperty("file.separator") + "Applications" + System.getProperty("file.separator"),
												"C:" + System.getProperty("file.separator") + "Program Files" + System.getProperty("file.separator"),
												"C:" + System.getProperty("file.separator") + "Program Files (x86)" + System.getProperty("file.separator")};
	
	public static File getStudentKeyFilePath() {
		if (studentKeyFilePath != null) {
			return studentKeyFilePath;
		} else {
			
		}
	}
	
	public static File getGraderKeyFilePath() {
		
	}
	
	public static File getInstructorKeyFilePath() {
		
	}
	
	private static boolean studentKeyPresent(File testPath) {
		
	}
	
	private static boolean graderKeyPresent(File testPath) {
		
	}
	
	private static boolean instructorKeyPresent(File testPath) {
		
	}

}
