package VGL;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class AboutVGLLabel {

	static void showAboutVGLLabel(VGLII vglII) {
		JLabel aboutLabel = new JLabel("<html><body><font size=+2>Virtual Genetics Lab II</font><br>" //$NON-NLS-1$
				+ "Release Version " + VGLII.version + "<br>" + "Copyright 2011<br>" + "VGL Team:<br>" //$NON-NLS-4$
				+ "<ul>" //$NON-NLS-1$
				+ "<li><b>Lead Programmer:</b></li>" //$NON-NLS-1$
				+ "<ul><li>Brian White (University of Massachusetts, Boston)</li>"
				+ "<li><i>brian.white@umb.edu</i></li></ul>" //$NON-NLS-1$
				+ "<li><b>Original VGL UI Code:</b></li>" //$NON-NLS-1$
				+ "<ul><li>Nikunj Koolar (UMB)</li><li>Wei Ma (UMB)</li><li>Naing Naing Maw (UMB)</li>" //$NON-NLS-1$
				+ "<li>Chung Ying Yu (UMB)</li></ul>" //$NON-NLS-1$
				+ "<li><b>Phenotype Images:</b></li>" //$NON-NLS-1$
				+ "<ul><li>Antoine Aidamouni (UMB)</li><li>Sara M. Burke (UMB)</li><li>Sara Hachemi (UMB)</li>" //$NON-NLS-1$
				+ "<li>Amit Kumar (UMB)</li></ul>" //$NON-NLS-1$
				+ "<li><b>Translations:</b></li>"
				+ "<ul><li>Traduction française: Sophie Javerzat (Université Bordeaux 1, FRANCE)</li>"
				+ "<li>Traduccion al espanol: Juan Carlos Hernández-Vega</li>"
				+ "</ul>"
				+ "<li><b>Academic Supervisor:</b></li>" //$NON-NLS-1$
				+ "<ul><li>Ethan Bolker (UMB)</li></ul>" //$NON-NLS-1$
				+"</ul>" //$NON-NLS-1$
				+ "All Rights Reserved\n" + "GNU General Public License\n" //$NON-NLS-1$ //$NON-NLS-2$
				+ "http://www.gnu.org/copyleft/gpl.html</body></html>"); //$NON-NLS-1$
		JOptionPane.showMessageDialog(vglII, aboutLabel,
				"About Virtual Genetics Lab II...", //$NON-NLS-1$
				JOptionPane.INFORMATION_MESSAGE);
	}
}
