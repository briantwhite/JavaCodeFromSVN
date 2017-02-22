package VGL;

import java.awt.EventQueue;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.prefs.Preferences;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class ProblemsFolderFinder {

	static File findProblemsFolder(VGLII vglII, String[] args) {
		File vglFolderPath = null;
		/*
		 *  need to look for relevant files (Problems/ and .key files)
		 *   in two places
		 *   - the directory where the program is running "user.dir"
		 *   - the directory where the .jar/.exe is 
		 *   this is to deal with possible app translocation by OS X
		 *    (see 2/2017 entries in log)
		 */
		// create a list of directories to search for the Problems/ folder
		// all end with the trailing /
		ArrayList<String> dirsToTry = new ArrayList<String>();
		// first, see if we saved a dir in the preferences
		Preferences prefs = Preferences.userRoot().node(vglII.getClass().getName());
		if (!prefs.get(VGLII.VGL_DIR_PREF_NAME, "").equals("")) {
			dirsToTry.add(prefs.get(VGLII.VGL_DIR_PREF_NAME, ""));
		}
		// first, check the args to see if we passed in a useful directory
		//  this checks args and looks if we passed in -D with a directory
		//  this is for mac only
		if ((args.length == 1) && args[0].startsWith("-D")) {
			String appRootDir = args[0].replace("-D", "");		// mode 1a (mac only)
			// need to chop off last directory /VGL-3.2.2.app to get to enclosing folder
			StringBuffer appDirBuffer = new StringBuffer();
			String[] parts = appRootDir.split("/");
			for (int i = 0; i < (parts.length - 1); i++) {
				appDirBuffer.append(parts[i]);
				appDirBuffer.append("/");
			}
			dirsToTry.add(appDirBuffer.toString());
		}
		// then, try where the .jar/.exe file is
		StringBuffer jarPathBuffer = null;
		try {
			jarPathBuffer = new StringBuffer(URLDecoder.decode(
					vglII.getClass().getProtectionDomain().getCodeSource().getLocation().toString(), "UTF-8"));

			// strip off the leading "file:"
			jarPathBuffer.delete(0, jarPathBuffer.indexOf(":") + 1);
			// strip off the trailing "VGLII.jar" - everything after the last file.separator
			jarPathBuffer.delete(jarPathBuffer.lastIndexOf(System.getProperty("file.separator")) + 1, jarPathBuffer.length());
		} catch (UnsupportedEncodingException e) {
			jarPathBuffer = null;
			e.printStackTrace();
		}
		if (jarPathBuffer != null) {
			dirsToTry.add(jarPathBuffer.toString());
		}
		// add a set of canonical directories & folders
		String homeDirHeader = System.getProperty("user.home") + System.getProperty("file.separator");
		String[] typicalPaths = 
			{homeDirHeader + "Desktop" + System.getProperty("file.separator"),
					homeDirHeader + "Documents" + System.getProperty("file.separator"),
					homeDirHeader + "Downloads" + System.getProperty("file.separator"),
					homeDirHeader + "Applications" + System.getProperty("file.separator"),
					System.getProperty("file.separator") + "Applications" + System.getProperty("file.separator")};
		String[] typicalFolders = {	
				"",												// if not in folder
				"VGL" + System.getProperty("file.separator"),
				"VGLII" + System.getProperty("file.separator"),
				"VGLII-" + VGLII.version + System.getProperty("file.separator")};
		for (int p = 0; p < typicalPaths.length; p++) {
			for (int f = 0; f < typicalFolders.length; f++) {
				dirsToTry.add(typicalPaths[p] + typicalFolders[f]);
			}
		}
		// loop over all possibilities until you find Problems/VGL/Level01.pr2
		//  look for a particular file just in case they have a random
		//  non-VGL-related Problems/ folder
		Iterator<String> locationIt = dirsToTry.iterator();
		while (locationIt.hasNext()) {
			String dirString = locationIt.next();
			File testFile = new File(dirString 
					+ "Problems" + System.getProperty("file.separator") 
					+ "VGL" + System.getProperty("file.separator")
					+ "Level01.pr2");
			System.out.println("trying " + testFile.getAbsolutePath());
			if (testFile.exists()) {
				vglFolderPath = new File(dirString);
				break;
			}
		}
		// if still null, pop up dialog
		if (vglFolderPath == null) {
			Object[] options = {"Show VGL where the folder is",
			"Quit VGL"};
			int n = JOptionPane.showOptionDialog(vglII,
					"VGLII cannot find the Problems folder.\n"
							+ "You can tell VGLII where to find this folder\n"
							+ "and VGLII will remember this location.\n"
							+ "(see the README.txt file for details)",
							"Problems Folder not Found",
							JOptionPane.YES_NO_OPTION,
							JOptionPane.WARNING_MESSAGE,
							null,
							options,
							options[0]);
			if (n == JOptionPane.NO_OPTION) {
				System.exit(0);
			}
			ProblemFolderFileChooser pffc = new ProblemFolderFileChooser();
			try {
				EventQueue.invokeAndWait(pffc);
			} catch (InvocationTargetException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			vglFolderPath = pffc.getProblemFolderDirectory();
			if (vglFolderPath == null) {
				System.exit(0);
			}
			// is this a correct Problems folder?
			File testFile = new File(vglFolderPath.getAbsolutePath()
					+ System.getProperty("file.separator") 
					+ "Problems" + System.getProperty("file.separator") 
					+ "VGL" + System.getProperty("file.separator") 
					+ "Level01.pr2");
			if (!testFile.exists()) {
				JOptionPane.showMessageDialog(vglII, "That folder does not contain VGLII problems; exiting.");
				System.exit(0);
			}
			//  if OK, set dir and save to prefs
			prefs.put(VGLII.VGL_DIR_PREF_NAME, vglFolderPath.getAbsolutePath() + System.getProperty("file.separator"));
		}
		System.out.println(vglFolderPath.getAbsolutePath());
		return vglFolderPath;
	}
}
