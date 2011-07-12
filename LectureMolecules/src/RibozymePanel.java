import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import org.jmol.api.JmolViewer;


public class RibozymePanel {
	
	public static JPanel make(final JLabel captionLabel, final JmolPanel jmolPanel, final JmolViewer viewer) {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		
		panel.add(new JLabel("<html><font color=red size=+2>"
				+ "Ribozyme"
				+ "<br></font></html>"));
		
		panel.add(Utils.makeLoadStructureButton("Load Ribozyme",
				"1U6B.pdb",
				"restrict RNA;"
				+ "spacefill on;"
				+ "color cpk;",
				"Ribozyme",
				captionLabel,
				jmolPanel));
		
		panel.add(Utils.makeScriptButton("Show backbone and bases",
				"reset;"
				+ "select all;"
				+ "spacefill off;"
				+ "color cpk;"
				+ "restrict RNA;"
				+ "select backbone and RNA;"
				+ "color white;"
				+ "backbone 0.5;"
				+ "wireframe on;",
				"<font color=white>Backbone</font>",
				captionLabel,
				jmolPanel));
		
		panel.add(Utils.makeScriptButton("Show substrate",
				"reset;"
				+ "select all;"
				+ "spacefill off;"
				+ "restrict RNA;"
				+ "select RNA;"
				+ "color white;"
				+ "spacefill on;"
				+ "select :C;"
				+ "color red;"
				+ "select :D;"
				+ "color purple;",
				"<font color=white>Ribozyme</font> <font color=red>Sub</font><font color=purple>strate</font>",
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
