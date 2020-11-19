package edu.umb.jsAipotu.preferences;

import java.io.File;
import java.io.FilePermission;
import java.security.AccessControlException;
import java.security.AccessController;
import java.util.prefs.Preferences;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/*
 * this singleton keeps track of where the Greenhouse is
 * it's needed to deal with Apple's App Translocation security
 * want to make single unit - just the app - version for mac
 * but the problem is that, if you put Greenhouse/ in the .app, you
 * can't write to it.
 * 
 * So, this is to load from the GH in the .app at first and then needs to
 * find another location when saving
 */
public class GreenhouseDirectoryManager {
	
	private static GreenhouseDirectoryManager instance;
	
	// note that this does not include the final "Greenhouse" 
	private static File greenhouseDirectory;
	
	private GreenhouseDirectoryManager() {
		greenhouseDirectory = null;
	}
	
	public static GreenhouseDirectoryManager getInstance() {
		if (instance == null) {
			instance = new GreenhouseDirectoryManager();
		}
		return instance;
	}
	
	public void setGreenhouseDirectory(File ghd) {
		greenhouseDirectory = ghd;
	}
	
	public String[] listGreenhouseFiles() {
		if (testGreenhouseDirectory(false)) {
			return greenhouseDirectory.list();
		} else {
			return null;
		}
	}

	public File getReadableFileFromGreenhouse(String filename) {
		if (testGreenhouseDirectory(false)) {
			return new File(greenhouseDirectory.getAbsolutePath() 
					+ System.getProperty("file.separator") 
					+ GlobalDefaults.greenhouseDirName
					+ System.getProperty("file.separator") + filename);
		} else {
			return null;
		}
	}
	
	public File getWritableFileFromGreenhouse(String filename) {
		if (testGreenhouseDirectory(true)) {
			return new File(greenhouseDirectory.getAbsolutePath() + System.getProperty("file.separator") + filename);
		} else {
			return null;
		}
	}
	
	// tests if you can read the folder by default; also tests write if param set
	// if you don't have permission, it offers option to cancel or to find a place
	//  for the GH
	//  returns true if it succeeded and changes the greenhouseDirectory
	private boolean testGreenhouseDirectory(boolean testWriteToo) {
		Object[] options = {"Cancel", "Select where to put the Greenhouse"};
		if (testWriteToo) {
			try {
				AccessController.checkPermission(
						new FilePermission(
								greenhouseDirectory.getAbsolutePath() 
								+ System.getProperty("file.separator") 
								+ GlobalDefaults.greenhouseDirName,"read,write"));
			} catch (AccessControlException e1) {
				int n = JOptionPane.showOptionDialog(null, "Aipotu can't save to the Greenhouse in its "
						+ "current location.\n"
						+ "You can show Aipotu where to save the Greenhouse and \n"
						+ "Aipotu will save your preference.\n"
						+ "Or, you can cancel the save.", 
						"Can't save to Greenhouse",
						JOptionPane.YES_NO_OPTION,
						JOptionPane.WARNING_MESSAGE,
						null, options, options[0]);
				if (n == 0) {
					return false;
				}
				final JFileChooser fc = new JFileChooser(
						System.getProperty("user.home") + System.getProperty("file.separator") + "Desktop");
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				fc.setDialogTitle("Choose the DIRECTORY where Aipotu will save the Greenhouse directory");
				int returnVal = fc.showSaveDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					greenhouseDirectory = 
							new File(fc.getSelectedFile().getAbsolutePath() 
									+ System.getProperty("file.separator"));
					if (testGreenhouseDirectory(true)) {
//						Preferences prefs = Preferences.userRoot().node(this.getClass().getName());
//						prefs.put(GlobalDefaults.GREENHOUSE_DIR_PREF_NAME, greenhouseDirectory.getAbsolutePath());
						return true;
					} else {
						return false;
					}
				} else {
					return false;
				}
			}		
		} else {
			try {
				AccessController.checkPermission(
						new FilePermission(
								greenhouseDirectory.getAbsolutePath() 
								+ System.getProperty("file.separator") 
								+ GlobalDefaults.greenhouseDirName,"read"));
			} catch (AccessControlException e1) {
				int n = JOptionPane.showOptionDialog(null, "Aipotu can't open or find the Greenhouse in its "
						+ "current location.\n"
						+ "You can show Aipotu where the Greenhouse is and \n"
						+ "Aipotu will save your preference.\n"
						+ "Or, you can cancel the open.", 
						"Can't open/find the Greenhouse",
						JOptionPane.YES_NO_OPTION,
						JOptionPane.WARNING_MESSAGE,
						null, options, options[0]);
				if (n == 0) {
					return false;
				}
				final JFileChooser fc = new JFileChooser(
						System.getProperty("user.home") + System.getProperty("file.separator") + "Desktop");
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				fc.setDialogTitle("Choose the Greenhouse directory");
				int returnVal = fc.showSaveDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					greenhouseDirectory = 
							new File(fc.getSelectedFile().getAbsolutePath() 
									+ System.getProperty("file.separator"));
					if (testGreenhouseDirectory(true)) {
						return true;
					} else {
						return false;
					}
				} else {
					return false;
				}
			}		
		}
		return false;
	}
}
