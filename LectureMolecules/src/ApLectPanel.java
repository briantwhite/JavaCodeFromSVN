import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jmol.api.JmolViewer;


public class ApLectPanel {

	public static JPanel make(final JLabel captionLabel, final JmolPanel jmolPanel, final JmolViewer viewer){
		JPanel apLectPanel = new JPanel();
		apLectPanel.setLayout(new BoxLayout(apLectPanel, BoxLayout.Y_AXIS));
		
		apLectPanel.add(new JLabel("<html><font color=red size=+2>"
				+ "Alkaline Phosphatase"
				+ "<br></font></html>"));
		
		apLectPanel.add(Utils.makeLoadStructureButton("Load Alkaline Phospahatase.",
				"ap.pdb",
				"spacefill on;",
				"Alkaline Phosphatase",
				captionLabel,
				jmolPanel));
		
		apLectPanel.add(Utils.makeScriptButton("Show two chains",
				"reset;"
				+ "select all;"
				+ "spacefill on;"
				+ "color chain;",
				"The two Polypeptide chains.",
				captionLabel,
				jmolPanel));
		
		apLectPanel.add(Utils.makeScriptButton("Show two active sites",
				"reset;"
				+ "spacefill on;"
				+ "select protein;"
				+ "color white;"
				+ "select ligand;"
				+ "color purple;"
				+ "select 49 or 51 or 100 or 101 or 102 or 153 or 154 or 166 or 167;"
				+ "color yellow;"
				+ "select 327 or 328 or 331 or 369 or 412;"
				+ "color yellow;"
				+ "select 549 or 551 or 600 or 601 or 602 or 653 or 654 or 666 or 667;"
				+ "color yellow;"
				+ "select 827 or 828 or 831 or 869 or 912;"
				+ "color yellow;",
				"The two <font color=yellow>Active Sites</font>"
				+ " and <font color=purple>substrates</font>.",
				captionLabel,
				jmolPanel));
		
		apLectPanel.add(Utils.makeScriptButton("Show active site close-up",
				"reset;"
				+ "select protein;"
				+ "backbone 0.25;"
				+ "wireframe off;"
				+ "spacefill off;"
				+ "color white;"
				+ "restrict (:a and ligand) or 50-52 or 154-156 or 165-167 or 321-323;"
				+ "select ligand and :a;"
				+ "center selected;"
				+ "spacefill 0.42;"
				+ "wireframe 0.16;"
				+ "select 51 or 155 or 166 or 322;"
				+ "spacefill 0.42;"
				+ "wireframe 0.16;"
				+ "monitor 31 1241;"
				+ "monitor 30 1242;"
				+ "monitor 27 6648;"
				+ "monitor 29 6649;"
				+ "monitor 6649 408;"
				+ "monitor 6651 409;"
				+ "monitor 6651 2406;"
				+ "monitor 6651 1163;"
				+ "set monitors off;"
				+ "set monitor 4;"
				+ "color monitor yellow;"
				+ "set fontsize 24;"
				+ "color label white;"
				+ "select atomno = 6648;"
				+ "label +;"
				+ "select atomno = 6649;"
				+ "label +;"
				+ "select atomno = 6651;"
				+ "label +;"
				+ "select atomno = 29;"
				+ "label -;"
				+ "select atomno = 30;"
				+ "label -;"
				+ "select atomno = 31;"
				+ "label -;"
				+ "select atomno=1239;"
				+ "color cpk;"
				+ "select atomno=1240;"
				+ "color cpk;"
				+ "select atomno=1241;"
				+ "color cpk;"
				+ "label +;"
				+ "select atomno=1242;"
				+ "color cpk;"
				+ "label +;"
				+ "select atomno=2403;"
				+ "color cpk;"
				+ "select atomno=2404;"
				+ "color cpk;"
				+ "select atomno=2405;"
				+ "color cpk;"
				+ "select atomno=2406;"
				+ "color cpk;"
				+ "label -;"
				+ "select atomno=1162;"
				+ "color cpk;"
				+ "select atomno=1163;"
				+ "color cpk;"
				+ "select atomno=1164;"
				+ "color cpk;"
				+ "select atomno=406;"
				+ "color cpk;"
				+ "select atomno=407;"
				+ "color cpk;"
				+ "select atomno=408;"
				+ "color cpk;"
				+ "label -;"
				+ "select atomno=409;"
				+ "color cpk;"
				+ "label -;"
				+ "select atomno=19;"
				+ "color magenta;"
				+ "select atomno=20;"
				+ "color magenta;"
				+ "select atomno=24;"
				+ "color magenta;"
				+ "select atomno=25;"
				+ "color magenta;"
				+ "select atomno=26;"
				+ "color magenta;"
				+ "select atomno=32;"
				+ "color magenta;"
				+ "select atomno=33;"
				+ "color magenta;"
				+ "select atomno=34;"
				+ "color magenta;"
				+ "select atomno=35;"
				+ "color magenta;"
				+ "select atomno=36;"
				+ "color magenta;"
				+ "set monitors off;"
				+ "zoom 500;",
				"The inside of the active site.",
				captionLabel,
				jmolPanel));
		
		apLectPanel.add(new JLabel("<html><br></html>"));
		apLectPanel.add(new JLabel(
				new ImageIcon(MoleculesInLect.class.getResource("cpkColors.gif"))));
		
		return apLectPanel;
	}

}
