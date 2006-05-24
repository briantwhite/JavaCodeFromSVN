import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jmol.adapter.smarter.SmarterJmolAdapter;
import org.jmol.api.JmolAdapter;
import org.jmol.api.JmolStatusListener;
import org.jmol.api.JmolViewer;

/*
 * Created on Apr 19, 2005
 *
 */

/**
 * @author brian
 *
 */
public class MoleculesInLect extends JFrame {
	
	final String htmlStart = new String("<html><font size=+5><font color=white>");
	final String htmlEnd = new String("</font></font></html>");
	final String darkGray = new String("color [100,100,100];");
	final String lightYellow = new String("color [200,200,0];");
	
	private JLabel captionLabel;
	JTabbedPane lecturePane;
	JPanel moleculePanel;
	JmolPanel jmolPanel;
	JmolViewer viewer;
	
	public MoleculesInLect() {
		super("Molecules in Lecture 1.2");
		moleculePanel = new JPanel();
		lecturePane = new JTabbedPane();
		jmolPanel = new JmolPanel();
	    viewer = jmolPanel.getViewer();
	    captionLabel = new JLabel("<html><font size=+4 color=white>"
				+ "Welcome!"
				+ "</font></html>");
	    
		addWindowListener(new ApplicationCloser());
		Container contentPane = getContentPane();
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.X_AXIS));
		
		moleculePanel.add(jmolPanel);
		jmolPanel.setPreferredSize(new Dimension(600,600));
		moleculePanel.add(captionLabel);
		captionLabel.setOpaque(true);
		captionLabel.setBackground(Color.BLACK);
		captionLabel.setPreferredSize(new Dimension(600,100));
		moleculePanel.setLayout(new BoxLayout(moleculePanel, BoxLayout.Y_AXIS));
		moleculePanel.setOpaque(true);
		moleculePanel.setBackground(Color.BLACK);
		contentPane.add(moleculePanel);

		lecturePane.addTab("Hemoglobin I", makeOxyDeoxyPanel(jmolPanel, viewer));
		lecturePane.addTab("Hemoglobin II", makeHemoLect1Panel(jmolPanel, viewer));
		lecturePane.addTab("Hemoglobin III", makeHemoLect2Panel(jmolPanel, viewer));
		lecturePane.addTab("Alkaline Phosphatase", makeapLectPanel(jmolPanel, viewer));
		lecturePane.addTab("Catalase", makeCatLectPanel(jmolPanel, viewer));
		lecturePane.addTab("Membranes", makeMembranePanel(jmolPanel, viewer));
		lecturePane.addTab("DNA", makeDnaPanel(jmolPanel, viewer));

		lecturePane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				viewer.evalString("zap;"
						+ "spacefill off;"
						+ "wireframe off;"
						+ "backbone off;"
						+ "slab off;"
						+ "spin off;"
						+ "cartoon off;"
						+ "hbonds off;"
						+ "dots off;");
				captionLabel.setText("");
			}
			
		});
		contentPane.add(lecturePane);
	}
	

	public static void main(String[] args) {
		JFrame myMolecs = new MoleculesInLect();
		myMolecs.pack();
		myMolecs.setVisible(true);
	}
	
	public JPanel makeOxyDeoxyPanel(final JmolPanel jmolPanel, final JmolViewer viewer) {
		JPanel oxyDeoxyPanel = new JPanel();
		oxyDeoxyPanel.setLayout(new BoxLayout(oxyDeoxyPanel, BoxLayout.Y_AXIS));
		
		oxyDeoxyPanel.add(new JLabel("<html><font color=red size=+2>"
				+ "Hemoglobin Struture I<br></font</html>"));
		
		oxyDeoxyPanel.add(makeLoadStructureButton("Load Both forms of Hemoglobin",
				"oxy-deoxy.pdb",
				"spacefill on;"
				+ "dots off;"
				+ "color cpk;",
				"Hemoglobin",
				jmolPanel));
		
		oxyDeoxyPanel.add(makeScriptButton("Show interior",
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
				jmolPanel));
		
		oxyDeoxyPanel.add(makeScriptButton("Show backbone, Hemes, and Oxygens",
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
				captionLabel.setText(htmlStart 
						+ "Hemoglobin without O<sub>2</sub>"
						+ htmlEnd);
			}
		});
		oxyButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				viewer.evalString("animation frame 2;");
				captionLabel.setText(htmlStart
						+ "Hemoglobin with 4 "
						+ "<font color=#FF8E8E>O<sub>2</sub></font>"
						+ " bound"
						+ htmlEnd);
			}
		});
		
		oxyDeoxyPanel.add(deoxyButton);
		oxyDeoxyPanel.add(oxyButton);
		oxyDeoxyPanel.add(new JLabel("<html><br></html>"));
		
		JRadioButton[] buttons0 = makeSpinToggleButtons(jmolPanel);
		oxyDeoxyPanel.add(buttons0[0]);
		oxyDeoxyPanel.add(buttons0[1]);
		
		oxyDeoxyPanel.add(new JLabel("<html><br></html>"));
		oxyDeoxyPanel.add(new JLabel(
				new ImageIcon(MoleculesInLect.class.getResource("cpkColors.gif"))));
		
		return oxyDeoxyPanel;
	}
	
	public JPanel makeHemoLect1Panel(final JmolPanel jmolPanel, final JmolViewer viewer) {
		JPanel hemoLect1Panel = new JPanel();
		hemoLect1Panel.setLayout(new BoxLayout(hemoLect1Panel, BoxLayout.Y_AXIS));
		
		hemoLect1Panel.add(new JLabel("<html><font color=red size=+2>"
				+ "Hemoglobin Struture II<br></font</html>"));
		
		hemoLect1Panel.add(makeLoadStructureButton("Load Hemoglobin",
				"1a3n.pdb",
				"spacefill on;"
				+ "slab off;",
				"deoxy-Hemoglobin",
				jmolPanel));
		
		hemoLect1Panel.add(makeScriptButton("Show 4 subunits and Hemes",
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
				jmolPanel));
		
		hemoLect1Panel.add(makeScriptButton("Show backbone and Hemes",
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
				jmolPanel));
		
		hemoLect1Panel.add(makeScriptButton("Show one beta-globin subunit",
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
				jmolPanel));
		
		hemoLect1Panel.add(makeScriptButton("Show beta's backbone and side-chains",
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
				jmolPanel));
		
		hemoLect1Panel.add(makeScriptButton("Show beta's backbone and Heme",
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
				jmolPanel));
		
		hemoLect1Panel.add(makeScriptButton("Show an alpha helix",
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
				jmolPanel));
		
		hemoLect1Panel.add(makeScriptButton("add H-bonds",
				"backbone off;"
				+ "colour hbonds yellow;"
				+ "hbonds 16;"
				+ "select 124-146b and backbone;"
				+ "wireframe 0.15;"
				+ "spacefill 0.45;"
				+ "color cpk;"
				+ "move 0 180 0  0  0 0 0  0  1;",
				"Alpha helix with H-bonds",
				jmolPanel));
		
		hemoLect1Panel.add(makeScriptButton("Show an ionic bond",
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
				jmolPanel));
		
		hemoLect1Panel.add(makeScriptButton("Show an H-bond",
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
				jmolPanel));
		
		hemoLect1Panel.add(makeScriptButton("Show an Hydrophobic Interaction",
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
				jmolPanel));
		
		hemoLect1Panel.add(makeScriptButton("Show hydrophobic core.",
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
				jmolPanel));
		
		
		JRadioButton[] buttons1 = makeSpinToggleButtons(jmolPanel);
		hemoLect1Panel.add(buttons1[0]);
		hemoLect1Panel.add(buttons1[1]);
		
		hemoLect1Panel.add(new JLabel(
				new ImageIcon(MoleculesInLect.class.getResource("cpkColors.gif"))));
		
		return hemoLect1Panel;
	}

	public JPanel makeHemoLect2Panel(final JmolPanel jmolPanel, final JmolViewer viewer) {
		JPanel hemoLect2Panel = new JPanel();
		hemoLect2Panel.setLayout(new BoxLayout(hemoLect2Panel, BoxLayout.Y_AXIS));
		
		hemoLect2Panel.add(new JLabel("<html><font color=red size=+2>"
				+ "Hemoglobin Mutants"));
		
		hemoLect2Panel.add(makeLoadStructureButton("Load hemoglobin",
				"1a3n.pdb",
				"spacefill on;",
				"Hemoglobin",
				jmolPanel));
		
		hemoLect2Panel.add(makeScriptButton("Show Hb Christchurch",
				"reset;"
				+ "select protein;"
				+ "spacefill off;"
				+ "wireframe off;"
				+ "backbone off;"
				+ "dots on;"
				+ lightYellow
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
				jmolPanel));
		
		hemoLect2Panel.add(makeScriptButton("Show Hb Woolwich",
				"reset;"
				+ "select protein;"
				+ "spacefill off;"
				+ "wireframe off;"
				+ "backbone off;"
				+ "dots on;"
				+ lightYellow
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
				jmolPanel));
		
		hemoLect2Panel.add(makeLoadStructureButton("Load sickle-cell hemoglobin",
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
				jmolPanel));
		
		hemoLect2Panel.add(makeScriptButton("Show Valine 6",
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
				jmolPanel));
		
		hemoLect2Panel.add(makeScriptButton("Show Valine and partners",
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
				jmolPanel));
		
		JRadioButton[] buttons2 = makeSpinToggleButtons(jmolPanel);
		hemoLect2Panel.add(buttons2[0]);
		hemoLect2Panel.add(buttons2[1]);
		
		hemoLect2Panel.add(new JLabel("<html><br></html>"));
		hemoLect2Panel.add(new JLabel(
				new ImageIcon(MoleculesInLect.class.getResource("cpkColors.gif"))));
		
		return hemoLect2Panel;
	}
	 
	public JPanel makeapLectPanel(final JmolPanel jmolPanel, final JmolViewer viewer){
		JPanel apLectPanel = new JPanel();
		apLectPanel.setLayout(new BoxLayout(apLectPanel, BoxLayout.Y_AXIS));
		
		apLectPanel.add(new JLabel("<html><font color=red size=+2>"
				+ "Alkaline Phosphatase"
				+ "<br></font></html>"));
		
		apLectPanel.add(makeLoadStructureButton("Load Alkaline Phospahatase.",
				"ap.pdb",
				"spacefill on;",
				"Alkaline Phosphatase",
				jmolPanel));
		
		apLectPanel.add(makeScriptButton("Show two chains",
				"reset;"
				+ "select all;"
				+ "spacefill on;"
				+ "color chain;",
				"The two Polypeptide chains.",
				jmolPanel));
		
		apLectPanel.add(makeScriptButton("Show two active sites",
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
				jmolPanel));
		
		apLectPanel.add(makeScriptButton("Show active site close-up",
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
				jmolPanel));
		
		apLectPanel.add(new JLabel("<html><br></html>"));
		apLectPanel.add(new JLabel(
				new ImageIcon(MoleculesInLect.class.getResource("cpkColors.gif"))));
		
		return apLectPanel;
	}

	public JPanel makeCatLectPanel(final JmolPanel jmolPanel, final JmolViewer viewer){
		JPanel catLectPanel = new JPanel();
		catLectPanel.setLayout(new BoxLayout(catLectPanel, BoxLayout.Y_AXIS));
		
		catLectPanel.add(new JLabel("<html><font color=red size=+2>"
				+ "Catalase"
				+ "<br></font></html>"));
		
		catLectPanel.add(makeLoadStructureButton("Load Catalase.",
				"7cat-tet.pdb",
				"spacefill on;",
				"Catalase",
				jmolPanel));
		
		catLectPanel.add(makeScriptButton("Show four chains",
				"reset;"
				+ "restrict protein;"
				+ "spacefill on;"
				+ "color chain;",
				"The four Polypeptide chains.",
				jmolPanel));
		
		catLectPanel.add(makeScriptButton("Show four tunnels",
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
				jmolPanel));
		
		catLectPanel.add(makeScriptButton("Show active site close-up",
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
				jmolPanel));
		
		catLectPanel.add(new JLabel("<html><br></html>"));
		
		JRadioButton[] buttons0 = makeSpinToggleButtons(jmolPanel);
		catLectPanel.add(buttons0[0]);
		catLectPanel.add(buttons0[1]);

		catLectPanel.add(new JLabel("<html><br></html>"));
		catLectPanel.add(new JLabel(
				new ImageIcon(MoleculesInLect.class.getResource("cpkColors.gif"))));
		
		return catLectPanel;
	}

	public JPanel makeMembranePanel(final JmolPanel jmolPanel, final JmolViewer viewer) {
		JPanel membranePanel = new JPanel();
		membranePanel.setLayout(new BoxLayout(membranePanel, BoxLayout.Y_AXIS));
		
		membranePanel.add(new JLabel("<html><font color=red size=+2>"
				+ "Membranes"
				+ "<br></font></html>"));
		
		membranePanel.add(makeLoadStructureButton("Load one phospholipid.",
				"1lip.pdb",
				"spacefill 0.48;"
				+ "wireframe 0.16;",
				"One Phospholipid.",
				jmolPanel));
		
		membranePanel.add(makeLoadStructureButton("Load one layer.",
				"1layer.pdb",
				"spacefill 0.48;"
				+ "wireframe 0.16;",
				"One Layer of a Membrane.",
				jmolPanel));
		
		membranePanel.add(makeLoadStructureButton("Load membrane.",
				"crystall.pdb",
				"spacefill 0.48;"
				+ "wireframe 0.16;",
				"A Piece of Membrane.",
				jmolPanel));
		
		membranePanel.add(makeScriptButton("Hide Water",
				"reset;"
				+ "reset;"
				+ "restrict lip;"
				+ "spacefill 0.48;"
				+ "wireframe 0.16;"
				+ "color cpk;",
				"Membrane without water.",
				jmolPanel));
		
		membranePanel.add(makeScriptButton("Show Water",
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
				jmolPanel));
		
		membranePanel.add(makeLoadStructureButton("Load a transmembrane pore.",
				"1pho.pdb",
				"restrict not water;"
				+ "spacefill on;"
				+ "color cpk;"
				+ "select hydrophobic;"
				+ "color purple;"
				+ "rotate x -90;",
				"A transmembrane pore. <font color=purple>\'phobic</font>",
				jmolPanel));
		
		JRadioButton[] buttons3 = makeSpinToggleButtons(jmolPanel);
		membranePanel.add(buttons3[0]);
		membranePanel.add(buttons3[1]);
		
		membranePanel.add(new JLabel("<html><br></html>"));
		membranePanel.add(new JLabel(
				new ImageIcon(MoleculesInLect.class.getResource("cpkColors.gif"))));
		
		return membranePanel;
	}
	
	public JPanel makeDnaPanel (final JmolPanel jmolPanel, final JmolViewer viewer) {
		JPanel dnaPanel = new JPanel();
		dnaPanel.setLayout(new BoxLayout(dnaPanel, BoxLayout.Y_AXIS));
		
		
		dnaPanel.add(new JLabel("<html><font color=red size=+2>"
				+ "DNA Structure"
				+ "<br></font></html>"));
		
		dnaPanel.add(makeLoadStructureButton("Load DNA",
				"DNA1.pdb",
				"spacefill on;"
				+ "rotate x 60;"
				+ "rotate z 28;",
				"DNA",
				jmolPanel));
		
		dnaPanel.add(makeScriptButton("Show two strands",
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
				jmolPanel));
		
		dnaPanel.add(makeScriptButton("Show one strand",
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
				jmolPanel));
		
		dnaPanel.add(makeScriptButton("Show close-up",
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
				jmolPanel));
		
		dnaPanel.add(makeScriptButton("Show one base-pair",
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
				jmolPanel));
		
		dnaPanel.add(makeScriptButton("Show whole thing",
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
				jmolPanel));
		
		JRadioButton[] buttons4 = makeSpinToggleButtons(jmolPanel);
		dnaPanel.add(buttons4[0]);
		dnaPanel.add(buttons4[1]);
		
		dnaPanel.add(new JLabel("<html><br></html>"));
		
		dnaPanel.add(new JLabel(
				new ImageIcon(MoleculesInLect.class.getResource("cpkColors.gif"))));
		
		return dnaPanel;
	}
	
	public  JButton makeLoadStructureButton(String buttonLabel, 
			final String pdbFile,
			final String script,
			final String caption,
			JmolPanel jmolPanel){
		
		final JmolViewer viewer = jmolPanel.getViewer();

		JButton button = new JButton("<html><font color=green>"
				+ buttonLabel
				+ "</font></html>");

		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				viewer.openStringInline(getPDBasString(pdbFile));
				captionLabel.setText(htmlStart + caption + htmlEnd);
				if (script != null){
					viewer.evalString(script);
				}
			}
		});
		return button;
	}
	
	
	public JButton makeScriptButton(String buttonLabel, 
			final String script,
			final String caption,
			JmolPanel jmolPanel){
		
		final JmolViewer viewer = jmolPanel.getViewer();

		JButton button = new JButton(buttonLabel);
		
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				viewer.evalString(script);
				captionLabel.setText(htmlStart + caption + htmlEnd);
			}
		});
		return button;
	}
	
	public JRadioButton[] makeSpinToggleButtons(JmolPanel jmolPanel){
		final JmolViewer viewer = jmolPanel.getViewer();
		JRadioButton[] buttons = new JRadioButton[2];
		buttons[0] = new JRadioButton("Spin on");
		buttons[1] = new JRadioButton("Spin off");
		buttons[1].setSelected(true);
		ButtonGroup group = new ButtonGroup();
		group.add(buttons[0]);
		group.add(buttons[1]);
		buttons[0].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				viewer.evalString("set spin Y 5; spin on;");
			}	
		});
		buttons[1].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				viewer.evalString("spin off;");
			}	
		});
		return buttons;
	}
	
	public String getPDBasString(String PDBfileName){
		StringBuffer moleculeString = new StringBuffer();
		URL moleculeURL = MoleculesInLect.class.getResource(PDBfileName);
		InputStream moleculeInput = null;
		try {
			moleculeInput = moleculeURL.openStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
		BufferedReader moleculeStream = 
			new BufferedReader(new InputStreamReader(moleculeInput));
		String line = null;
		try {
			while ((line = moleculeStream.readLine())	!= null ){
				moleculeString.append(line);
				moleculeString.append("\n");
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return moleculeString.toString();
	}
	
	static class ApplicationCloser extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			System.exit(0);
		}
	}
	
	class JmolPanel extends JPanel 
	implements JmolStatusListener {
		JmolViewer viewer;
		JmolAdapter adapter;
		JmolPanel() {
			adapter = new SmarterJmolAdapter(null);
			viewer = org.jmol.viewer.Viewer.allocateViewer(this, adapter);
			viewer.setJmolStatusListener(this);
		}
		
		public JmolViewer getViewer() {
			return viewer;
		}
		
		final Dimension currentSize = new Dimension();
		final Rectangle rectClip = new Rectangle();
		
		public void paint(Graphics g) {
			getSize(currentSize);
			g.getClipBounds(rectClip);
			viewer.renderScreenImage(g, currentSize, rectClip);
		}
		
		//jmol status listener methods
		
		public void scriptStatus(String statusString) {
			
		}
		
		public void notifyAtomPicked(int arg0, String atomInfo) {
			
		}
		
		public void notifyFileLoaded(String arg0, String arg1, String arg2, Object arg3, String arg4) {}
		public void scriptEcho(String arg0) {}
		public void setStatusMessage(String arg0) {}
		public void notifyScriptTermination(String arg0, int arg1) {}
		public void handlePopupMenu(int arg0, int arg1) {}
		public void notifyMeasurementsChanged() {}
		public void notifyFrameChanged(int arg0) {}
		public void showUrl(String arg0) {}
		public void showConsole(boolean arg0) {}
	}
	
	
}
