import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import org.jmol.api.JmolViewer;


public class HemoLect2Panel {
	
	public static JPanel make(final JLabel captionLabel, final JmolPanel jmolPanel, final JmolViewer viewer) {
		JPanel hemoLect2Panel = new JPanel();
		hemoLect2Panel.setLayout(new BoxLayout(hemoLect2Panel, BoxLayout.Y_AXIS));
		
		hemoLect2Panel.add(new JLabel("<html><font color=red size=+2>"
				+ "Hemoglobin Mutants"));
		
		hemoLect2Panel.add(Utils.makeLoadStructureButton("Load hemoglobin",
				"1a3n.pdb",
				"spacefill on;",
				"Hemoglobin",
				captionLabel,
				jmolPanel));
		
		hemoLect2Panel.add(Utils.makeScriptButton("Show Hb Christchurch",
				"reset;"
				+ "select protein;"
				+ "spacefill off;"
				+ "wireframe off;"
				+ "backbone off;"
				+ "dots on;"
				+ Utils.lightYellow
				+ "select ligand;"
				+ "spacefill 0.42;"
				+ "wireframe 0.16;"
				+ "color red;"
				+ "restrict :b;"
				+ "select 71b;"
				+ "spacefill on;"
				+ "dots off;"
				+ "color cpk;"
				+ "center selected;"
				+ "select 71b and backbone;"
				+ "spacefill 0.42;"
				+ "wireframe 0.16;"
				+ "dots off;"
				+ "color white;"
				+ "move 0 0 0  100  0 0 0  0  1;",
				"Amino acid altered in Hb Christchurch",
				captionLabel,
				jmolPanel));
		
		hemoLect2Panel.add(Utils.makeScriptButton("Show Hb Woolwich",
				"reset;"
				+ "select protein;"
				+ "spacefill off;"
				+ "wireframe off;"
				+ "backbone off;"
				+ "dots on;"
				+ Utils.lightYellow
				+ "select ligand;"
				+ "spacefill 0.42;"
				+ "wireframe 0.16;"
				+ "color red;"
				+ "restrict :b;"
				+ "select 132b;"
				+ "spacefill on;"
				+ "dots off;"
				+ "color cpk;"
				+ "center selected;"
				+ "select 132b and backbone;"
				+ "spacefill 0.42;"
				+ "wireframe 0.16;"
				+ "dots off;"
				+ "color white;"
				+ "move 0 0 0  100  0 0 0  0  1;",
				"Amino acid altered in Hb Woolwich",
				captionLabel,
				jmolPanel));
		
		hemoLect2Panel.add(Utils.makeLoadStructureButton("Load sickle-cell hemoglobin",
				"2hbs.pdb",
				"spacefill on;"
				+ "select :a or: e;"
				+ "color blue;"
				+ "select :b or :f;"
				+ "color cyan;"
				+ "select :c or :g;"
				+ "color green;"
				+ "select :d or :h;"
				+ "color yellow;",
				"Sickle-cell Hemoglobin",
				captionLabel,
				jmolPanel));
		
		hemoLect2Panel.add(Utils.makeScriptButton("Show Valine 6",
				"reset;"
				+ "select all;"
				+ "wireframe off;"
				+ "spacefill off;"
				+ "dots off;"
				+ "select protein;"
				+ "wireframe on;"
				+ "spacefill off;"
				+ "select :h;"
				+ "dots on;"
				+ "wireframe off;"
				+ "color yellow;"
				+ "select 6h;"
				+ "spacefill on;"
				+ "dots off;"
				+ "color purple;",
				"<font color=purple>Valine 6</font>",
				captionLabel,
				jmolPanel));
		
		hemoLect2Panel.add(Utils.makeScriptButton("Show Valine and partners",
				"reset;"
				+ "select all;"
				+ "wireframe off;"
				+ "spacefill off;"
				+ "dots off;"
				+ "select protein;"
				+ "wireframe on;"
				+ "spacefill off;"
				+ "select :h;"
				+ "dots on;"
				+ "wireframe off;"
				+ "color yellow;"
				+ "select 6h;"
				+ "spacefill on;"
				+ "dots off;"
				+ "color purple;"
				+ "select 88b or 85b;"
				+ "spacefill on;"
				+ "dots off;"
				+ "color green;",
				"<font color=purple>Valine 6</font>"
				+ " and <font color=green>partners</font>",
				captionLabel,
				jmolPanel));
		
		JRadioButton[] buttons2 = Utils.makeSpinToggleButtons(jmolPanel);
		hemoLect2Panel.add(buttons2[0]);
		hemoLect2Panel.add(buttons2[1]);
		
		hemoLect2Panel.add(new JLabel("<html><br></html>"));
		hemoLect2Panel.add(new JLabel(
				new ImageIcon(MoleculesInLect.class.getResource("cpkColors.gif"))));
		
		return hemoLect2Panel;
	}

}
