import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import org.jmol.api.JmolViewer;


public class MembranePanel {

	public static JPanel make(final JLabel captionLabel, final JmolPanel jmolPanel, final JmolViewer viewer) {
		JPanel membranePanel = new JPanel();
		membranePanel.setLayout(new BoxLayout(membranePanel, BoxLayout.Y_AXIS));
		
		membranePanel.add(new JLabel("<html><font color=red size=+2>"
				+ "Membranes"
				+ "<br></font></html>"));
		
		membranePanel.add(Utils.makeLoadStructureButton("Load one phospholipid.",
				"1lip.pdb",
				"spacefill 0.48;"
				+ "wireframe 0.16;",
				"One Phospholipid.",
				captionLabel,
				jmolPanel));
		
		membranePanel.add(Utils.makeLoadStructureButton("Load one layer.",
				"1layer.pdb",
				"spacefill 0.48;"
				+ "wireframe 0.16;",
				"One Layer of a Membrane.",
				captionLabel,
				jmolPanel));
		
		membranePanel.add(Utils.makeLoadStructureButton("Load membrane.",
				"crystall.pdb",
				"spacefill 0.48;"
				+ "wireframe 0.16;",
				"A Piece of Membrane.",
				captionLabel,
				jmolPanel));
		
		membranePanel.add(Utils.makeScriptButton("Hide Water",
				"reset;"
				+ "reset;"
				+ "restrict lip;"
				+ "spacefill 0.48;"
				+ "wireframe 0.16;"
				+ "color cpk;",
				"Membrane without water.",
				captionLabel,
				jmolPanel));
		
		membranePanel.add(Utils.makeScriptButton("Show Water",
				"reset;"
				+ "reset;"
				+ "restrict lip;"
				+ "spacefill 0.48;"
				+ "wireframe 0.16;"
				+ "color cpk;"
				+ "select water;"
				+ "spacefill on;"
				+ "color blue;",
				"Membrane with <font color=blue>water</font>.",
				captionLabel,
				jmolPanel));
		
		membranePanel.add(Utils.makeLoadStructureButton("Load a transmembrane pore.",
				"1pho.pdb",
				"restrict not water;"
				+ "spacefill on;"
				+ "color cpk;"
				+ "select hydrophobic;"
				+ "color purple;"
				+ "rotate x -90;",
				"A transmembrane pore. <font color=purple>\'phobic</font>",
				captionLabel,
				jmolPanel));
		
		JRadioButton[] buttons3 = Utils.makeSpinToggleButtons(jmolPanel);
		membranePanel.add(buttons3[0]);
		membranePanel.add(buttons3[1]);
		
		membranePanel.add(new JLabel("<html><br></html>"));
		membranePanel.add(new JLabel(
				new ImageIcon(MoleculesInLect.class.getResource("cpkColors.gif"))));
		
		return membranePanel;
	}

}
