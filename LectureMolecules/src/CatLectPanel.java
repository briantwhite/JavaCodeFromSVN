import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import org.jmol.api.JmolViewer;


public class CatLectPanel {

	public static JPanel make(final JLabel captionLabel, final JmolPanel jmolPanel, final JmolViewer viewer){
		JPanel catLectPanel = new JPanel();
		catLectPanel.setLayout(new BoxLayout(catLectPanel, BoxLayout.Y_AXIS));
		
		catLectPanel.add(new JLabel("<html><font color=red size=+2>"
				+ "Catalase"
				+ "<br></font></html>"));
		
		catLectPanel.add(Utils.makeLoadStructureButton("Load Catalase.",
				"7cat-tet.pdb",
				"spacefill on;",
				"Catalase",
				captionLabel,
				jmolPanel));
		
		catLectPanel.add(Utils.makeScriptButton("Show four chains",
				"reset;"
				+ "restrict protein;"
				+ "spacefill on;"
				+ "color chain;",
				"The four Polypeptide chains.",
				captionLabel,
				jmolPanel));
		
		catLectPanel.add(Utils.makeScriptButton("Show four tunnels",
				"reset;"
				+ "select protein;"
				+ "spacefill 1.5;"
				+ "wireframe off;"
				+ "backbone off;"
				+ "color white;"
				+ "select ligand and not ndp;"
				+ "spacefill on;"
				+ "colour red;"
				+ "select 126, 127,  167,  168,  176;"
				+ "colour yellow;"
				+ "select 115, 116, 128, 152, 153, 163, 164, 198;"
				+ "colour green;"
				+ "select iron;"
				+ "color purple;",
				"The four <font color=yellow>Tun</font><font color=green>nels</font> that"
				+ " lead to the <font color=yellow>Active </font><font color=purple>Sites</font>",
				captionLabel,
				jmolPanel));
		
		catLectPanel.add(Utils.makeScriptButton("Show active site close-up",
				"reset;"
				+ "select protein;"
				+ "spacefill off;"
				+ "select ligand;"
				+ "spacefill off;"
				+ "wireframe 0;"
				+ "backbone off;"
				+ "restrict ligand and (not ndp) and :b;"
				+ "wireframe on;"
				+ "color red;"
				+ "select iron and :b;"
				+ "color purple;"
				+ "spacefill 0.6;"
				+ "select (147b or 74b) and not backbone;"
				+ "spacefill 0.4;"
				+ "wireframe 0.10;"
				+ "color cpk;"
				+ "select (126b, 127b,  167b,  168b,  176b) and not backbone;"
				+ "colour yellow;"
				+ "select (115b, 116b, 128b, 152b, 153b, 163b, 164b, 198b) and not backbone;"
				+ "colour green;"
				+ "dots on;"
				+ "select iron and :b;"
				+ "center selected;"
				+ "move 0 0 0  300  0 0 0  0  5 10 15;",
				"The inside of the active site.",
				captionLabel,
				jmolPanel));
		
		catLectPanel.add(new JLabel("<html><br></html>"));
		
		JRadioButton[] buttons0 = Utils.makeSpinToggleButtons(jmolPanel);
		catLectPanel.add(buttons0[0]);
		catLectPanel.add(buttons0[1]);

		catLectPanel.add(new JLabel("<html><br></html>"));
		catLectPanel.add(new JLabel(
				new ImageIcon(MoleculesInLect.class.getResource("cpkColors.gif"))));
		
		return catLectPanel;
	}

}
