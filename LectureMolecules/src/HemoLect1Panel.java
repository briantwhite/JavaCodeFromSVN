import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import org.jmol.api.JmolViewer;


public class HemoLect1Panel {
	
	public static JPanel make(final JLabel captionLabel, final JmolPanel jmolPanel, final JmolViewer viewer) {
		JPanel hemoLect1Panel = new JPanel();
		hemoLect1Panel.setLayout(new BoxLayout(hemoLect1Panel, BoxLayout.Y_AXIS));
		
		hemoLect1Panel.add(new JLabel("<html><font color=red size=+2>"
				+ "Hemoglobin Struture II<br></font</html>"));
		
		hemoLect1Panel.add(Utils.makeLoadStructureButton("Load Hemoglobin",
				"1a3n.pdb",
				"spacefill on;"
				+ "slab off;",
				"deoxy-Hemoglobin",
				captionLabel,
				jmolPanel));
		
		hemoLect1Panel.add(Utils.makeScriptButton("Show 4 subunits and Hemes",
				"select all;"
				+ "center selected;"
				+ "slab off;"
				+ "hbonds off;"
				+ "labels off;"
				+ "spacefill on;"
				+ "select :a;"
				+ "color yellow;"
				+ "select :b;"
				+ "color green;"
				+ "select :c;"
				+ "color blue;"
				+ "select :d;"
				+ "color cyan;"
				+ "select ligand;"
				+ "color red;",
				"Hemoglobin Subunits and "
				+ "<font color=red>Hemes</font>",
				captionLabel,
				jmolPanel));
		
		hemoLect1Panel.add(Utils.makeScriptButton("Show backbone and Hemes",
				"select all;"
				+ "center selected;"
				+ "slab off;"
				+ "hbonds off;"
				+ "labels off;"
				+ "spacefill off;"
				+ "wireframe off;"
				+ "backbone 0.25;"
				+ "select :a;"
				+ "color yellow;"
				+ "select :b;"
				+ "color green;"
				+ "select :c;"
				+ "color blue;"
				+ "select :d;"
				+ "color cyan;"
				+ "select ligand;"
				+ "spacefill on;"
				+ "color red;",
				"Protein Backbone and "
				+ "<font color=red>Hemes</font>",
				captionLabel,
				jmolPanel));
		
		hemoLect1Panel.add(Utils.makeScriptButton("Show one beta-globin subunit",
				"reset;"
				+ "slab off;"
				+ "labels off;"
				+ "hbonds off;"
				+ "select all;"
				+ "center selected;"
				+ "spacefill on;"
				+ "wireframe off;"
				+ "backbone off;"
				+ "select :a;"
				+ "color yellow;"
				+ "select :b;"
				+ "color green;"
				+ "select :c;"
				+ "color blue;"
				+ "select :d;"
				+ "color cyan;"
				+ "select ligand;"
				+ "spacefill on;"
				+ "color red;"
				+ "move 0 45 0  0  0 0 0  0 5 30 15;"
				+ "restrict :b;"
				+ "delay 5;"
				+ "color cpk;",
				"One beta-globin subunit",
				captionLabel,
				jmolPanel));
		
		hemoLect1Panel.add(Utils.makeScriptButton("Show beta's backbone and side-chains",
				"reset;"
				+ "slab off;"
				+ "labels off;"
				+ "spacefill off;"
				+ "restrict :b;"
				+ "select protein and :b;"
				+ "color yellow;"
				+ "center selected;"
				+ "spacefill off;"
				+ "wireframe 0.2;"
				+ "backbone 0.4;"
				+ "select backbone;"
				+ "color blue;"
				+ "select ligand and :b;"
				+ "spacefill 0.4;"
				+ "wireframe 0.16;"
				+ "color red;"
				+ "move 0 0 0  200  0 0 0  0  5 30 15",
				"Protein <font color=blue>Backbone</font>, "
				+ "<font color=yellow>side-chains</font>, and "
				+ "<font color=red>Heme</font>",
				captionLabel,
				jmolPanel));
		
		hemoLect1Panel.add(Utils.makeScriptButton("Show beta's backbone and Heme",
				"reset;"
				+ "labels off;"
				+ "slab off;"
				+ "hbonds off;"
				+ "restrict :b;"
				+ "select protein and :b;"
				+ "center selected;"
				+ "spacefill off;"
				+ "wireframe off;"
				+ "backbone 0.2;"
				+ "color structure;"
				+ "select ligand and :b;"
				+ "spacefill 0.4;"
				+ "wireframe 0.16;"
				+ "color red;"
				+ "move 0 0 0  200  0 0 0  0  1;",
				"Protein Backbone and "
				+ "<font color=red>Heme</font>",
				captionLabel,
				jmolPanel));
		
		hemoLect1Panel.add(Utils.makeScriptButton("Show an alpha helix",
				"reset;"
				+ "slab off;"
				+ "hbonds off;"
				+ "labels off;"
				+ "restrict protein and :b;"
				+ "select protein and :b;"
				+ "center selected;"
				+ "spacefill off;"
				+ "wireframe off;"
				+ "backbone 0.2;"
				+ "color structure;"
				+ "select 124-146b;"
				+ "center selected;"
				+ "move 0 180 0  0  0 0 0  0 1;"
				+ "move 0 0 0  200  0 0 0  0 1;"
				+ "restrict 124-146b;",
				"Alpha helix",
				captionLabel,
				jmolPanel));
		
		hemoLect1Panel.add(Utils.makeScriptButton("add H-bonds",
				"backbone off;"
				+ "colour hbonds yellow;"
				+ "hbonds 16;"
				+ "select 124-146b and backbone;"
				+ "wireframe 0.15;"
				+ "spacefill 0.45;"
				+ "color cpk;"
				+ "move 0 180 0  0  0 0 0  0  1;",
				"Alpha helix with H-bonds",
				captionLabel,
				jmolPanel));
		
		hemoLect1Panel.add(Utils.makeScriptButton("Show an ionic bond",
				"reset;"
				+ "labels off;"
				+ "slab off;"
				+ "hbonds off;"
				+ "monitors off;"
				+ "hbonds off;"
				+ "restrict protein and :b;"
				+ "select protein and :b;"
				+ "spacefill off;"
				+ "wireframe off;"
				+ "backbone 0.25;"
				+ "color white;"
				+ "select 94b or 146b;"
				+ "wireframe 0.16;"
				+ "spacefill 0.42;"
				+ "color cpk;"
				+ "center selected;"
				+ "select 140-146b and backbone;"
				+ "color red;"
				+ "select 90-98b and backbone;"
				+ "color green;"
				+ "move 90 -90 0  0  0 0 0  0 1;"
				+ "move 0 0 0  50  0 0 0  0 1;"
				+ "monitor 1820 2228;"
				+ "set monitor off;"
				+ "set monitor 4;"
				+ "color monitor yellow;"
				+ "select atomno = 1820;"
				+ "label-;"
				+ "font label 44 SansSerif Plain;"
				+ "color label white;"
				+ "select atomno = 2228;"
				+ "label+;"
				+ "font label 34 SansSerif Plain;"
				+ "color label white;"
				+ "move 0 90 0  0  0 0 0  0  1;",
				"A Ionic Bond",
				captionLabel,
				jmolPanel));
		
		hemoLect1Panel.add(Utils.makeScriptButton("Show an H-bond",
				"reset;"
				+ "slab off;"
				+ "labels off;"
				+ "hbonds off;"
				+ "monitors off;"
				+ "hbonds off;"
				+ "restrict protein and :b;"
				+ "select protein and :b;"
				+ "spacefill off;"
				+ "wireframe off;"
				+ "backbone 0.25;"
				+ "color white;"
				+ "select 98b or 145b;"
				+ "wireframe 0.16;"
				+ "spacefill 0.42;"
				+ "color cpk;"
				+ "center selected;"
				+ "select 140-146b and backbone;"
				+ "color red;"
				+ "select 94-102b and backbone;"
				+ "color green;"
				+ "move 90 -90 0  0  0 0 0  0 1;"
				+ "move 0 0 0  50  0 0 0  0 1;"
				+ "monitor 1852 2218;"
				+ "set monitors off;"
				+ "set monitor 4;"
				+ "color monitor yellow;"
				+ "select all;"
				+ "label off;"
				+ "select atomno = 1852;"
				+ "color cpk;"
				+ "label A;"
				+ "font label 44 SansSerif Plain;"
				+ "color label white;"
				+ "select atomno = 2218;"
				+ "color cpk;"
				+ "label D;"
				+ "font label 44 SansSerif Plain;"
				+ "color label white;"
				+ "move 0 -45 0  0  0 0 0  0  1;",
				"An Hydrogen Bond",
				captionLabel,
				jmolPanel));
		
		hemoLect1Panel.add(Utils.makeScriptButton("Show an Hydrophobic Interaction",
				"reset;"
				+ "slab off;"
				+ "labels off;"
				+ "hbonds off;"
				+ "monitors off;"
				+ "hbonds off;"
				+ "restrict protein and :b;"
				+ "select protein and :b;"
				+ "spacefill off;"
				+ "wireframe off;"
				+ "backbone 0.25;"
				+ "color white;"
				+ "select 68b or 71b;"
				+ "wireframe 0.16;"
				+ "spacefill 0.42;"
				+ "color cpk;"
				+ "center selected;"
				+ "select 67-69b and backbone;"
				+ "color red;"
				+ "select 70-72b and backbone;"
				+ "color green;"
				+ "move 90 -90 0  0  0 0 0  0 1;"
				+ "move 0 0 0  50  0 0 0  0 1;"
				+ "select ((atomno >=1628) and (atomno <=1630)) or ((atomno >=1645) and (atomno <=1650));"
				+ "dots on;"
				+ "set monitors off;"
				+ "select all;"
				+ "label off;"
				+ "move 30 120 0  0  0 0 0  0  1;",
				"Hydrophobic interaction",
				captionLabel,
				jmolPanel));
		
		hemoLect1Panel.add(Utils.makeScriptButton("Show hydrophobic core.",
				"reset;"
				+ "labels off;"
				+ "hbonds off;"
				+ "restrict protein and :b;"
				+ "select protein and :b;"
				+ "spacefill on;"
				+ "color white;"
				+ "select hydrophobic;"
				+ "color red;"
				+ "delay 5;"
				+ "slab on;"
				+ "slab 55;",
				"<font color=red>Hydrophobic</font> core.",
				captionLabel,
				jmolPanel));
		
		
		JRadioButton[] buttons1 = Utils.makeSpinToggleButtons(jmolPanel);
		hemoLect1Panel.add(buttons1[0]);
		hemoLect1Panel.add(buttons1[1]);
		
		hemoLect1Panel.add(new JLabel(
				new ImageIcon(MoleculesInLect.class.getResource("cpkColors.gif"))));
		
		return hemoLect1Panel;
	}

}
