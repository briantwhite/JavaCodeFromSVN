import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import org.jmol.api.JmolViewer;


public class DnaPanel {

	public static JPanel make(final JLabel captionLabel, final JmolPanel jmolPanel, final JmolViewer viewer) {
		JPanel dnaPanel = new JPanel();
		dnaPanel.setLayout(new BoxLayout(dnaPanel, BoxLayout.Y_AXIS));
		
		
		dnaPanel.add(new JLabel("<html><font color=red size=+2>"
				+ "DNA Structure"
				+ "<br></font></html>"));
		
		dnaPanel.add(Utils.makeLoadStructureButton("Load DNA",
				"DNA1.pdb",
				"spacefill on;"
				+ "rotate x 60;"
				+ "rotate z 28;",
				"DNA",
				captionLabel,
				jmolPanel));
		
		dnaPanel.add(Utils.makeScriptButton("Show two strands",
				"reset;"
				+ "select all;"
				+ "spacefill on;"
				+ "rotate x 60;"
				+ "rotate z 28;"
				+ "select :C;"
				+ "color red;"
				+ "select :D;"
				+ "color green;",
				"The two DNA strands.",
				captionLabel,
				jmolPanel));
		
		dnaPanel.add(Utils.makeScriptButton("Show one strand",
				"reset;"
				+ "select all;"
				+ "spacefill on;"
				+ "rotate x 60;"
				+ "rotate z 28;"
				+ "restrict :C;"
				+ "color red;"
				+ "select :C and backbone;"
				+ "color blue;",
				"One Strand:<font color=red> bases </font>and <font color=blue>Backbone</font>.",
				captionLabel,
				jmolPanel));
		
		dnaPanel.add(Utils.makeScriptButton("Show close-up",
				"reset;"
				+ "select all;"
				+"spacefill 0.48;"
				+ "wireframe 0.16;"
				+ "rotate z -108;"
				+ "rotate y -16;"
				+ "rotate x 127;"
				+ "color green;"
				+ "select backbone;"
				+ "color blue;"
				+ "select *.p or *.o1p or *.o2p or *.o3* or *.o5*;"
				+ "color yellow;"
				+ "select *.c5*;"
				+ "color cyan;"
				+ "select *.C3*;"
				+ "color red;"
				+ "restrict 460-463c;"
				+ "center selected;"
				+ "zoom 500;",
				"<font color=blue>Sugar</font>"
				+ " <font color=yellow>Phosphate</font>"
				+ " <font color=green>Base</font>"
				+ " <font color=red>3'C</font>"
				+ " <font color=30FFFF>5'C</font>.",
				captionLabel,
				jmolPanel));
		
		dnaPanel.add(Utils.makeScriptButton("Show one base-pair",
				"reset;"
				+ "select all;"
				+"spacefill 0.48;"
				+"wireframe 0.16;"
				+"set monitors off;"
				+"monitor 238 747;"
				+"monitor 237 749;"
				+ "color monitors yellow;"
				+"rotate z -108;"
				+"rotate y -16;"
				+"rotate x 127;"
				+"color green;"
				+"select backbone;"
				+"color blue;"
				+"select *.p or *.o1p or *.o2p or *.o3* or *.o5*;"
				+"color yellow;"
				+"select *.c5*;"
				+"color cyan;"
				+"select *.C3*;"
				+"color red;"
				+"restrict 460c or 487d;"
				+"center selected;"
				+"zoom 500;",
				"Base-pair: "
				+ "<font color=blue>Sug. </font>" 
				+ "<font color=yellow>Phos. </font>" 
				+ "<font color=green>Base </font>" 
				+ "<font color=red>3'C </font>" 
				+ "<font color=30FFFF>5'C</font>.",
				captionLabel,
				jmolPanel));
		
		dnaPanel.add(Utils.makeScriptButton("Show whole thing",
				"reset;"
				+ "select all;"
				+"spacefill 0.48;"
				+"wireframe 0.16;"
				+"set monitors off;"
				+ "rotate x 60;"
				+ "rotate z 28;"
				+"select a or 490;"
				+"color red;"
				+"select t;"
				+"color purple;"
				+"select g;"
				+"color green;"
				+"select c;"
				+"color blue;"
				+"select backbone and :c;"
				+"color yellow;"
				+"select backbone and :d;"
				+"color white;",
				"<font size=+4 color=white>Back</font>" 
				+ "<font color=yellow>bone </font>" 
				+ "<font color=red>A</font>" 
				+ "<font color=purple>T</font>" 
				+ "<font color=green>G</font>" 
				+ "<font color=blue>C</font>.",
				captionLabel,
				jmolPanel));
		
		JRadioButton[] buttons4 = Utils.makeSpinToggleButtons(jmolPanel);
		dnaPanel.add(buttons4[0]);
		dnaPanel.add(buttons4[1]);
		
		dnaPanel.add(new JLabel("<html><br></html>"));
		
		dnaPanel.add(new JLabel(
				new ImageIcon(MoleculesInLect.class.getResource("cpkColors.gif"))));
		
		return dnaPanel;
	}

}
