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
	private static String[] typicalPaths = {homeDirHeader + "Desktop" + System.getProperty("file.separator"),
												homeDirHeader + "Documents" + System.getProperty("file.separator"),
												homeDirHeader + "Downloads" + System.getProperty("file.separator"),
												homeDirHeader + "Applications" + System.getProperty("file.separator"),
												System.getProperty("file.separator") + "Applications" + System.getProperty("file.separator"),
												"C:" + System.getProperty("file.separator") + "Program Files" + System.getProperty("file.separator"),
												"C:" + System.getProperty("file.separator") + "Program Files (x86)" + System.getProperty("file.separator")};
	
	private static String[] typicalFolders = {	"VGL" + System.getProperty("file.separator"),
												"VGLII" + System.getProperty("file.separator"),
												"VGLII-" + VGLII.version + System.getProperty("file.separator")};
	/*
	 * these methods return the complete path to the corresponding .key file
	 * or null if the file can't be found and the user cancels the operation
	 */
	public static File getStudentKeyFilePath() {
		if (studentKeyFilePath != null) {
			return studentKeyFilePath;
		} else {
			return getKeyFilePath("student.key");
		}
	}
	
	public static File getGraderKeyFilePath() {
		if (graderKeyFilePath != null) {
			return graderKeyFilePath;
		} else {
			return getKeyFilePath("grader.key");
		}
		
	}
	
	public static File getInstructorKeyFilePath() {
		if (instructorKeyFilePath != null) {
			return instructorKeyFilePath;
		} else {
			return getKeyFilePath("instructor.key");
		}
		
	}
	
	// tries all the possible places - dialog if not found
	//  returns null if they cancel dialog
	private static File getKeyFilePath(String keyFileName) {
		File result = null;
		for (int p = 0; p < typicalPaths.length; p++) {
			for (int f = 0; f < typicalFolders.length; f++) {
				result = new File(typicalPaths[p] + typicalFolders[f] + keyFileName);
				if (functionalKeyFilePresent(result)) {
					return result;
				}
			}
		}
		// choice dialog here- locate files or punt
		return null;
	}
	
	
	/*
	 * this looks in the test path for the appropriate key file
	 *   and tests to be sure it's functional
	 */
	private static boolean functionalKeyFilePresent(File testPath) {
		
	}
	

}
