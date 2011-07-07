import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import org.jmol.api.JmolViewer;


public class OxyDeoxyPanel {


	public static JPanel make(final JLabel captionLabel, final JmolPanel jmolPanel, final JmolViewer viewer) {
		JPanel oxyDeoxyPanel = new JPanel();
		oxyDeoxyPanel.setLayout(new BoxLayout(oxyDeoxyPanel, BoxLayout.Y_AXIS));
		
		oxyDeoxyPanel.add(new JLabel("<html><font color=red size=+2>"
				+ "Hemoglobin Struture I<br></font</html>"));
		
		oxyDeoxyPanel.add(Utils.makeLoadStructureButton("Load Both forms of Hemoglobin",
				"oxy-deoxy.pdb",
				"spacefill on;"
				+ "dots off;"
				+ "color cpk;",
				"Hemoglobin",
				captionLabel,
				jmolPanel));
		
		oxyDeoxyPanel.add(Utils.makeScriptButton("Show interior",
				"select all;"
				+ "spacefill off;"
				+ "wireframe off;"
				+ "backbone off;"
				+ "select protein;"
				+ "dots on;"
				+ "color yellow;"
				+ "select ligand;"
				+ "spacefill 0.4;"
				+ "wireframe 0.16;"
				+ "color red;"
				+ "select HEM.o1 or HEM.o2;"
				+ "spacefill on;"
				+ "color pink;",
				"Hemoglobin and <font color=red>heme</font>"
				+ " and <font color=#FF8E8E>O<sub>2</sub></font>",
				captionLabel,
				jmolPanel));
		
		oxyDeoxyPanel.add(Utils.makeScriptButton("Show backbone, Hemes, and Oxygens",
				"select all;"
				+ "spacefill off;"
				+ "dots off;"
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
				+ "spacefill 0.4;"
				+ "wireframe 0.16;"
				+ "color red;"
				+ "select HEM.o1 or HEM.o2;"
				+ "spacefill on;"
				+ "color pink;",
				"Hemoglobin and <font color=red>heme</font>"
				+ " and <font color=#FF8E8E>O<sub>2</sub></font>",
				captionLabel,
				jmolPanel));
		
		JRadioButton deoxyButton = new JRadioButton("DE-oxy hemoglobin");
		JRadioButton oxyButton = new JRadioButton("OXY hemoglobin");
		deoxyButton.setSelected(true);
		ButtonGroup oxyDeoxyGroup = new ButtonGroup();
		oxyDeoxyGroup.add(deoxyButton);
		oxyDeoxyGroup.add(oxyButton);
		deoxyButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				viewer.evalString("animation frame 1;");
				captionLabel.setText(Utils.htmlStart 
						+ "Hemoglobin without O<sub>2</sub>"
						+ Utils.htmlEnd);
			}
		});
		oxyButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				viewer.evalString("animation frame 2;");
				captionLabel.setText(Utils.htmlStart
						+ "Hemoglobin with 4 "
						+ "<font color=#FF8E8E>O<sub>2</sub></font>"
						+ " bound"
						+ Utils.htmlEnd);
			}
		});
		
		oxyDeoxyPanel.add(deoxyButton);
		oxyDeoxyPanel.add(oxyButton);
		oxyDeoxyPanel.add(new JLabel("<html><br></html>"));
		
		JRadioButton[] buttons0 = Utils.makeSpinToggleButtons(jmolPanel);
		oxyDeoxyPanel.add(buttons0[0]);
		oxyDeoxyPanel.add(buttons0[1]);
		
		oxyDeoxyPanel.add(new JLabel("<html><br></html>"));
		oxyDeoxyPanel.add(new JLabel(
				new ImageIcon(MoleculesInLect.class.getResource("cpkColors.gif"))));
		
		return oxyDeoxyPanel;
	}

}
