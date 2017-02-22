package VGL;

import java.io.File;

import javax.swing.JFileChooser;

public class ProblemFolderFileChooser implements Runnable {
	private final JFileChooser fc = new JFileChooser();
	private File problemFolderDirectory = null;
	private int retVal = 0;
	public void run() {
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fc.setDialogTitle("Choose the DIRECTORY where the Problems folder can be found");
		retVal = fc.showOpenDialog(null);
		if (retVal == JFileChooser.APPROVE_OPTION) {
			problemFolderDirectory = fc.getSelectedFile();
		} 
	}

	public File getProblemFolderDirectory() {
		return problemFolderDirectory;
	}

}
