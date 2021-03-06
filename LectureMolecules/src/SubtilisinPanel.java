import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import org.jmol.api.JmolViewer;


public class SubtilisinPanel {

	public static JPanel make(final JLabel captionLabel, final JmolPanel jmolPanel, final JmolViewer viewer) {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		
		panel.add(new JLabel("<html><font color=red size=+2>"
				+ "Subtilisin"
				+ "<br></font></html>"));
		
		panel.add(Utils.makeLoadStructureButton("Load Subtilisin",
				"1S01.pdb",
				"restrict protein;"
				+ "spacefill on;"
				+ "color cpk;",
				"Subtilisin",
				captionLabel,
				jmolPanel));
		
		panel.add(Utils.makeScriptButton("Show active site",
				"reset;"
				+ "restrict protein;"
				+ "spacefill on;"
				+ "color white;"
				+ "select 32 or 64 or 221;"
				+ "color green;",
				"<font color=green>Active Site</font>",
				captionLabel,
				jmolPanel));
		
		panel.add(Utils.makeScriptButton("Show active site and met 222",
				"reset;"
				+ "restrict protein;"
				+ "spacefill on;"
				+ "color white;"
				+ "select 32 or 64 or 221;"
				+ "color green;"
				+ "select 222;"
				+ "color red;",
				"<font color=green>Active Site</font> <font color=red>Met 222</font>",
				captionLabel,
				jmolPanel));
		
		
		JRadioButton[] buttons4 = Utils.makeSpinToggleButtons(jmolPanel);
		panel.add(buttons4[0]);
		panel.add(buttons4[1]);
		
		panel.add(new JLabel("<html><br></html>"));
		
		panel.add(new JLabel(
				new ImageIcon(MoleculesInLect.class.getResource("cpkColors.gif"))));
		
		return panel;
	}

}
