package edu.umb.jsVGL.client.VGL;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class AboutVGLLabel {

	static void showAboutVGLLabel(VGLII vglII) {
		JLabel aboutLabel = new JLabel("<html><body><font size=+2>Virtual Genetics Lab II</font><br>" //$NON-NLS-1$
				+ "Release Version " + VGLII.version + "<br>" + "Copyright 2011<br>" + "VGL Team:<br>" //$NON-NLS-4$
				+ "<ul>" //$NON-NLS-1$
				+ "<li><b>Lead Programmer and Developer:</b></li>"
				+ "<ul>"
				+ "<li>Brian White (University of Massachusetts, Boston) <a href=\"mailto:brian.white@umb.edu\">"
				+ "<i>brian.white@umb.edu</i></a></li>"
				+ "</ul>"
				+ "<li><b>Original VGL UI Code:</b></li>"
				+ "<ul>"
				+ "<li><b>Academic Supervisor:</b> Ethan Bolker (UMB)</li>"
				+ "<li>Nikunj Koolar (UMB)</li>"
				+ "<li>Wei Ma (UMB)</li>"
				+ "<li>Naing Naing Maw (UMB)</li>"
				+ "<li>Chung Ying Yu (UMB)</li>"
				+ "</ul>"
				+ "<li><b>Phenotype Images:</b></li>"
				+ "<ul>"
				+ "<li>Antoine Aidamouni (UMB)</li>"
				+ "<li>Sara M. Burke (UMB)</li>"
				+ "<li>Sara Hachemi (UMB)</li>"
				+ "<li>Amit Kumar (UMB)</li>"
				+ "</ul>"
				+ "<li><b>Translations:</b></li>"
				+ "<ul>"
				+ "<li>Traduction Française: Sophie Javerzat (Université Bordeaux 1, FRANCE)</li>"
				+ "<li>Traducción de Español: Juan-Carlos Hernández-Vega</li>"
				+ "<li>Korean Translation: Se-Jin Youn</li>"
				+ "</ul>"
				+ "<li><b>Beta-testers:</b></li>"
				+ "<ul>"
				+ "<li><i>Multiple genes:</i></li>"
				+ "<ul>"
				+ "<li>Sophie Javerzat and her students at Université Bordeaux</li>"
				+ "<li>Harry Roy and his students at RPI</li>"
				+ "</ul>"
				+ "<li><i>Prototype of the Grader:</i> Liz Ryder and her students at WPI</li>"
				+ "<li><i>Model Builder and Grader:</i> Patty Szczys and her students at"
				+ "Eastern Connecticut State University</li>"
				+ "</ul>"
				+ "<li>EdX logo image, by Eric Lander, was created by Runaway Technology Inc. using<br>" 
				+ " PhotoMosaic by Robert Silvers from original artwork by Darryl Leja<br>"
				+ "(courtesy of the Whitehead Institute for Biomedical Research)</li>"
				+"</ul>" 
				+ "All Rights Reserved\n" + "GNU General Public License\n" 
				+ "http://www.gnu.org/copyleft/gpl.html</body></html>"); 
		JOptionPane.showMessageDialog(vglII, aboutLabel,
				"About Virtual Genetics Lab II...", 
				JOptionPane.INFORMATION_MESSAGE);
	}
}
