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
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jmol.adapter.smarter.SmarterJmolAdapter;
import org.jmol.api.JmolAdapter;
import org.jmol.api.JmolSimpleViewer;

/*
 * Created on Apr 19, 2005
 *
 */

/**
 * @author brian
 *
 */
public class Molecules {
	
    static String drugDotsScript = "select S58; spacefill off; dots on;";
    static String drugSfScript = "select S58; spacefill on; dots off;";
    static String coxDotsScript = "select 120 or 90 or 355 or 530 or 385"
    	                       + "or 531 or 381; spacefill off; dots on;";
    static String coxSfScript = "select 120 or 90 or 355 or 530 or 385"
        					   + "or 531 or 381; spacefill on; dots off;";
    static String AcpkScript = "select atomno=4561; color cpk;";
    static String ApurpScript = "select atomno=4561; color purple;";
    static String BcpkScript = "select atomno=4578; color cpk;";
    static String BgreenScript = "select atomno=4578; color green;";
    static String CcpkScript = "select atomno=4579; color cpk;";
    static String CpinkScript = "select atomno=4579; color pink;";
    static String DcpkScript = "select atomno=4582; color cpk;";
    static String DwhiteScript = "select atomno=4582; color white;";

	public static void main(String[] args) {
	    JFrame frame = new JFrame("Molecules in 3-dimensions");
	    frame.addWindowListener(new ApplicationCloser());
	    Container contentPane = frame.getContentPane();
	    contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.X_AXIS));
	    
	    final JmolPanel jmolPanel = new JmolPanel();
	    jmolPanel.setPreferredSize(new Dimension(600,600));
	    contentPane.add(jmolPanel);
	    
	    final JmolSimpleViewer viewer = jmolPanel.getViewer();
	    
	    JTabbedPane problemPane = new JTabbedPane();
	    problemPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				viewer.evalString("zap;");
			}
	    	
	    });
	    
	    JPanel problem1Panel = new JPanel();
	    problemPane.addTab("Problem 1.1.2(1)", problem1Panel);
	    problem1Panel.setLayout(new BoxLayout(problem1Panel, BoxLayout.Y_AXIS));
	    
	    problem1Panel.add(new JLabel("<html><font color=red size=+2>"
	    		                   + "Small Molecules<br></font</html>"));
	    problem1Panel.add(makeLoadStructureButton("The linear form of glucose",
	    		                           "D-glucose.pdb",
										   null,
										   jmolPanel));
	    problem1Panel.add(makeLoadStructureButton("The linear form of fructose",
                							"D-fructose.pdb",
											null,
											jmolPanel));
	    problem1Panel.add(makeLoadStructureButton("The circular form of glucose",
											"beta-D-glucopyranose.pdb",
											null,
											jmolPanel));
	    problem1Panel.add(makeLoadStructureButton("The first amino acid",
											"AA1.PDB",
											null,
											jmolPanel));
	    problem1Panel.add(makeLoadStructureButton("The second amino acid",
											"AA2.PDB",
											null,
											jmolPanel));
	    problem1Panel.add(makeLoadStructureButton("The third amino acid",
											"AA3.PDB",
											null,
											jmolPanel));
	    problem1Panel.add(new JLabel("<html><br></html>"));
	    problem1Panel.add(new JLabel(
	    		new ImageIcon(Molecules.class.getResource("cpkColors.gif"))));
	    
	    
	    JPanel problem2Panel = new JPanel();
	    problemPane.addTab("Problem 1.1.2(2)", problem2Panel);
	    problem2Panel.setLayout(new BoxLayout(problem2Panel, BoxLayout.Y_AXIS));
	    
	    problem2Panel.add(new JLabel("<html><font color=red size=+2>"
	    		                   + "Small Polypeptides<br></font></html>"));
	    problem2Panel.add(makeLoadStructureButton("The first tripeptide",
	    		                           "tripeptide1.pdb",
										   null,
										   jmolPanel));
	    problem2Panel.add(makeLoadStructureButton("The second tripeptide",
                							"tripeptide2.pdb",
											null,
											jmolPanel));
	    problem2Panel.add(new JLabel("<html><br></html>"));
	    problem2Panel.add(new JLabel(
	    		new ImageIcon(Molecules.class.getResource("cpkColors.gif"))));

	    
	    JPanel problem3Panel = new JPanel();
	    problemPane.addTab("Problem 2.1", problem3Panel);
	    problem3Panel.setLayout(new BoxLayout(problem3Panel, BoxLayout.Y_AXIS));
	    
	    problem3Panel.add(new JLabel("<html><font color=red size=+2>"
	    		                   + "Lysozyme 2<sup>o</sup> Structure"
								   + "<br></font></html>"));
	    problem3Panel.add(makeLoadStructureButton("Show backbone.",
	    		                           "1LYD.PDB",
										   "spacefill off; wireframe off;"
										 + "backbone 0.3; color structure;",
										   jmolPanel));
	    problem3Panel.add(makeScriptButton("Show alpha helix.",
											"restrict protein; wireframe off;" 
	    		                          + "spacefill 0; backbone 0.3;"
                                          + "color structure; center selected; "
										  + "reset; restrict 60-80; center selected; "
										  + "zoom 200",
											jmolPanel));
	    problem3Panel.add(makeScriptButton("Show beta sheets.",
	    									"restrict protein; wireframe off; "
	    								  + "spacefill 0; backbone 0.3; "
	                                      + "color structure; center selected; "
										  + "reset; restrict 12-59; center selected; "
										  + "zoom 300",
										     jmolPanel));
	    problem3Panel.add(new JLabel("<html><br></html>"));
	    problem3Panel.add(new JLabel(
	    		new ImageIcon(Molecules.class.getResource("structureColors.gif"))));

	    JPanel problem4Panel = new JPanel();
	    problemPane.addTab("Problem 2.2.2(1)", problem4Panel);
	    problem4Panel.setLayout(new BoxLayout(problem4Panel, BoxLayout.Y_AXIS));
	    
	    problem4Panel.add(new JLabel("<html><font color=red size=+2>"
	    		                   + "Lysozyme 3<sup>o</sup> Structure I"
								   + "<br></font></html>"));
	    problem4Panel.add(makeLoadStructureButton("Show exterior; red = phobic.",
	    		                           "1LYD.PDB",
	    		                           "slab off; restrict protein; "
	    		                           + "spacefill on; "
	    		                           + "select all; color white; "
	    		                           + "select hydrophobic; color red",
										   jmolPanel));
	    problem4Panel.add(makeScriptButton("Show interior; red = phobic.",
	    									"restrict protein; spacefill on; "
	    							      + "select all; color white; "
										  + "dots off; wireframe off;"
										  + "select hydrophobic; color red; "
										  + "reset; move 90 -90 0  0  0 0 0  0  5;"
										  + "delay 5;"
										  + "slab on;"
										  + "slab 50",
											jmolPanel));
	    problem4Panel.add(makeScriptButton("Show Valines.",
	    									"slab off; restrict protein; "
	    		                          + "select protein; color yellow; "
										  + "spacefill off; wireframe off; dots on; "
										  + "backbone off; select val; dots off; "
										  + "color cpk; spacefill on",
										     jmolPanel));
	    problem4Panel.add(makeScriptButton("Show Lysines.",
	    									"slab off; restrict protein; "
	    								  + "select protein; color yellow; "
										  + "spacefill off; wireframe off; dots on; "
										  + "backbone off; select lys; dots off; "
										  + "color cpk; spacefill on",
										  	jmolPanel));
	    problem4Panel.add(new JLabel("<html><br></html>"));
	    problem4Panel.add(new JLabel(
	    		new ImageIcon(Molecules.class.getResource("cpkColors.gif"))));

	    JPanel problem5Panel = new JPanel();
	    problemPane.addTab("Problem 2.2.2(2)", problem5Panel);
	    problem5Panel.setLayout(new BoxLayout(problem5Panel, BoxLayout.Y_AXIS));
	    
	    JRadioButton bsButton = new JRadioButton("Show atoms as Ball & Stick");
	    bsButton.setSelected(true);
	    JRadioButton sfButton = new JRadioButton("Show atoms as Spacefill");
	    ButtonGroup group = new ButtonGroup();
	    group.add(bsButton);
	    group.add(sfButton);
	    
	    bsButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				viewer.evalString("spacefill 0.5; wireframe 0.2; ");
			}
	    	
	    });
	    sfButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				viewer.evalString("spacefill on; ");
			}
	    	
	    });

	    problem5Panel.add(new JLabel("<html><font color=red size=+2>"
	    		                   + "Lysozyme 3<sup>o</sup> Structure I"
								   + "<br></font></html>"));
	    problem5Panel.add(makeLoadStructureButton("Show Lysozyme.",
	    		                           "1LYD.PDB",
	    		                           "restrict protein; "
	    		                           + "spacefill 0.5; wireframe 0.2; "
	    		                           + "select all; color cpk",
										   jmolPanel));
	    problem5Panel.add(makeSizeSensitiveScriptButton("Show Glu 11 and Arg 145.",
											"restrict protein; spacefill off; "
	    							  	  + "backbone off; "
										  + "reset; select all; wireframe on; color [60,60,60]; "
										  + "select 11 or 145; color cpk; ",
										  bsButton,
										  jmolPanel));
	    problem5Panel.add(makeSizeSensitiveScriptButton("Show Asp 10 and Tyr 161.",
	    									"restrict protein; spacefill off; "
	    								   + "backbone off; "
										   + "reset; select all; wireframe on; color [60,60,60]; "
										   + "select 10 or 161; color cpk; ",
											bsButton,
										   jmolPanel));
	    problem5Panel.add(makeSizeSensitiveScriptButton("Show Gln 105 and Trp 138.",
	    									"restrict protein; spacefill off; "
	    									+ "backbone off; "
											+ "reset; select all; wireframe on; color [60,60,60]; "
											+ "select 105 or 138; color cpk; ",
											bsButton,
											jmolPanel));
	    problem5Panel.add(makeSizeSensitiveScriptButton("Show Met 102 and Phe 114.",
											"restrict protein; spacefill off; "
	    								  + "backbone off; "
										  + "reset; select all; wireframe on; color [60,60,60]; "
										  + "select 102 or 114; color cpk; ",
										  bsButton,
										  jmolPanel));
	    problem5Panel.add(makeSizeSensitiveScriptButton("Show Tyr 24 and Lys 35.",
											"restrict protein; spacefill off; "
	    								  + "backbone off; "
										  + "reset; select all; wireframe on; color [60,60,60]; "
										  + "select 24 or 35; color cpk; ",
										  bsButton,
										  jmolPanel));
	    problem5Panel.add(makeScriptButton("Show Phe 67 and Secondary Struct.",
	    									"restrict protein; spacefill off; "
	            						  + "reset; select all; wireframe off; "
										  + "backbone 0.2; color structure; "
										  + "select 67; color red; spacefill on;"
										  + "center selected; translate center center;",
										  jmolPanel));
	    problem5Panel.add(new JLabel("<html><br></html>"));
	    
	    problem5Panel.add(bsButton);
	    problem5Panel.add(sfButton);
	    problem5Panel.add(new JLabel("<html><br></html>"));
	    
	    problem5Panel.add(new JLabel(
	    		new ImageIcon(Molecules.class.getResource("cpkColors.gif"))));

	    JPanel problem6Panel = new JPanel();
	    problemPane.addTab("Problem 2.3.2(1)", problem6Panel);
	    problem6Panel.setLayout(new BoxLayout(problem6Panel, BoxLayout.Y_AXIS));
	    
	    JRadioButton bsButton1 = new JRadioButton("Show atoms as Ball & Stick");
	    bsButton1.setSelected(true);
	    JRadioButton sfButton1 = new JRadioButton("Show atoms as Spacefill");
	    ButtonGroup group1 = new ButtonGroup();
	    group1.add(bsButton1);
	    group1.add(sfButton1);
	    
	    bsButton1.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				viewer.evalString("spacefill 0.5; wireframe 0.2; ");
			}
	    	
	    });
	    sfButton1.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				viewer.evalString("spacefill on; ");
			}
	    	
	    });

	    problem6Panel.add(new JLabel("<html><font color=red size=+2>"
	    		                   + "The DNA-Repair Enzyme AAG"
								   + "<br></font></html>"));
	    problem6Panel.add(makeLoadStructureButton("Show AAG and DNA.",
	    		                           "1BNK.PDB",
	    		                           "restrict not water; spacefill on;"
	    		                           + "select not water; color chain; "
	    		                           + "select ligand; color red",
										   jmolPanel));
	    problem6Panel.add(makeSizeSensitiveScriptButton("Show Arg 182 in the protein and T8 in the DNA.",
	    									"restrict not water; spacefill off; "
	            							+ "reset; wireframe on; color [60,60,60];"
											+ "select T8 or 182 or atomno=1679; color cpk; ",
										  bsButton1,
										  jmolPanel));
	    problem6Panel.add(makeSizeSensitiveScriptButton("Show Thr 143 in the protein and G23 in the DNA.",
	    									"restrict not water; spacefill off; "
	            							+ "reset; wireframe on; color [60,60,60];"
											+ "select G23 or 143 or atomno=1973; color cpk; ",
											bsButton1,
										   jmolPanel));
	    problem6Panel.add(makeSizeSensitiveScriptButton("Show Met 164 in the protein and T19 in the DNA.",
	    									"restrict not water; spacefill off; "
	            							+ "reset; wireframe on; color [60,60,60];"
											+ "select T19 or 164; color cpk; ",
											bsButton1,
											jmolPanel));
	    problem6Panel.add(makeSizeSensitiveScriptButton("Show Tyr 162 in the protein and T8 in the DNA.",
	    									"restrict not water; spacefill off; "
	            							+ "reset; wireframe on; color [60,60,60];"
											+ "select T8 or 162; color cpk; ",
										  bsButton1,
										  jmolPanel));

	    problem6Panel.add(new JLabel("<html><br></html>"));
	    
	    problem6Panel.add(bsButton1);
	    problem6Panel.add(sfButton1);
	    problem6Panel.add(new JLabel("<html><br></html>"));
	    
	    problem6Panel.add(new JLabel(
	    		new ImageIcon(Molecules.class.getResource("cpkColors.gif"))));

	    JPanel problem7Panel = new JPanel();
	    problemPane.addTab("Problem 2.3.2(2a)", problem7Panel);
	    problem7Panel.setLayout(new BoxLayout(problem7Panel, BoxLayout.Y_AXIS));
	    
	    problem7Panel.add(new JLabel("<html><font color=red size=+2>"
                + "The Enzyme COX-2"
				   + "<br></font></html>"));
	    problem7Panel.add(makeLoadStructureButton("Show COX-2 with drug bound.",
                        "6COX.PDB",
                        "restrict protein or S58; spacefill on;"
                        + "select protein or S58; color chain; "
                        + "select S58; color red;"
                        + "move -45 0 0  0  0 0 0  0  2",
						   jmolPanel));
	    JRadioButton sphereButton = new JRadioButton("Show atoms as Spheres");
	    sphereButton.setSelected(true);
	    JRadioButton dotButton = new JRadioButton("Show atoms as Dots");
	    ButtonGroup group2 = new ButtonGroup();
	    group2.add(sphereButton);
	    group2.add(dotButton);
	    sphereButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				viewer.evalString("select protein; wireframe off; spacefill on; dots off; ");
			}
	    });
	    dotButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				viewer.evalString("select protein; wireframe off; spacefill off; dots on; ");
			}
	    });
	    problem7Panel.add(sphereButton);
	    problem7Panel.add(dotButton);
	    
	    problem7Panel.add(new JLabel("<html><br></html>"));
	    	    
	    problem7Panel.add(new JLabel(
	    		new ImageIcon(Molecules.class.getResource("cpkColors.gif"))));

	    JPanel problem8Panel = new JPanel();	    	                       		
	    problemPane.addTab("Problem 2.3.2(2bc)", problem8Panel);
	    problem8Panel.setLayout(new BoxLayout(problem8Panel, BoxLayout.Y_AXIS));
	    final JCheckBox drugDotsCheckbox = new JCheckBox("Show Drug as Dots");
	    final JCheckBox coxDotsCheckbox = new JCheckBox("Show COX-2 as Dots");
	    final JCheckBox aColorCheckbox = new JCheckBox("Color Position A Purple");
	    final JCheckBox bColorCheckbox = new JCheckBox("Color Position B Green");
	    final JCheckBox cColorCheckbox = new JCheckBox("Color Postion C Pink");
	    final JCheckBox dColorCheckbox = new JCheckBox("Color Postion D White");

	    
	    problem8Panel.add(new JLabel("<html><font color=red size=+2>"
                + "The Enzyme COX-2"
				   + "<br></font></html>"));
	    problem8Panel.add(makeSensitiveLoadStructureButton("Show COX-2 with model drug bound.",
				   "edited_cox2.pdb",
                "reset; select S58; spacefill on; center selected;"
				 + "select protein or hem; wireframe off; spacefill off;"
				 + "select 120 or 90 or 355 or 530 or 385 or 531 or 381;"
				 + "wireframe 0.2; spacefill on;"
				 + "zoom 300;",
				 drugDotsCheckbox,
				 coxDotsCheckbox,
				 aColorCheckbox,
				 bColorCheckbox,
				 cColorCheckbox,
				 dColorCheckbox,
				 jmolPanel));
	    problem8Panel.add(makeSensitiveLoadStructureButton("Show COX-2 with celebrex bound.",
				   "celebrex_cox.pdb",
             "reset; select S58; spacefill on; center selected;"
				 + "select protein or hem; wireframe off; spacefill off;"
				 + "select 120 or 90 or 355 or 530 or 385 or 531 or 381;"
				 + "wireframe 0.2; spacefill on;"
				 + "zoom 300;",
				 drugDotsCheckbox,
				 coxDotsCheckbox,
				 aColorCheckbox,
				 bColorCheckbox,
				 cColorCheckbox,
				 dColorCheckbox,
				 jmolPanel));
	    
	    drugDotsCheckbox.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				if (drugDotsCheckbox.isSelected()){
					viewer.evalString(drugDotsScript);
				} else {
					viewer.evalString(drugSfScript);
				}
			}
	    });
	    coxDotsCheckbox.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				if (coxDotsCheckbox.isSelected()){
					viewer.evalString(coxDotsScript);
				} else {
					viewer.evalString(coxSfScript);
				}
			}
	    });
	    aColorCheckbox.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				if (aColorCheckbox.isSelected()){
					viewer.evalString(ApurpScript);
				} else {
					viewer.evalString(AcpkScript);
				}
			}
	    });
	    bColorCheckbox.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				if (bColorCheckbox.isSelected()){
					viewer.evalString(BgreenScript);
				} else {
					viewer.evalString(BcpkScript);
				}
			}
	    });
	    cColorCheckbox.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				if (cColorCheckbox.isSelected()){
					viewer.evalString(CpinkScript);
				} else {
					viewer.evalString(CcpkScript);
				}
			}
	    });
	    dColorCheckbox.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				if (dColorCheckbox.isSelected()){
					viewer.evalString(DwhiteScript);
				} else {
					viewer.evalString(DcpkScript);
				}
			}
	    });

	    problem8Panel.add(drugDotsCheckbox);
	    problem8Panel.add(coxDotsCheckbox);
	    problem8Panel.add(aColorCheckbox);
	    problem8Panel.add(bColorCheckbox);
	    problem8Panel.add(cColorCheckbox);
	    problem8Panel.add(dColorCheckbox);
	    
	    
	    problem8Panel.add(new JLabel("<html><br></html>"));	    
	    problem8Panel.add(new JLabel(
	    		new ImageIcon(Molecules.class.getResource("cpkColors.gif"))));

	    JPanel problem9Panel = new JPanel();	    	                       		
	    problemPane.addTab("Problem 4.2", problem9Panel);
	    problem9Panel.setLayout(new BoxLayout(problem9Panel, BoxLayout.Y_AXIS));
	    problem9Panel.add(new JLabel("<html><font color=red size=+2>"
                + "The Protein Hemoglobin."
				   + "<br></font></html>"));

	    problem9Panel.add(new JLabel("<html><br></html>"));	    
	    problem9Panel.add(new JLabel(
	    		new ImageIcon(Molecules.class.getResource("cpkColors.gif"))));
	    
	    contentPane.add(problemPane);
	    
	    frame.pack();
	    frame.setVisible(true);

	    String strError = viewer.getOpenFileError();
	    if (strError != null)
	      System.out.println(strError);
	  }

	  public static JButton makeLoadStructureButton(String buttonLabel, 
	  		                                 String pdbFile,
											 String script,
											 JmolPanel jmolPanel){
	  	final JmolSimpleViewer viewer = jmolPanel.getViewer();
	  	final String pdbFileName = pdbFile;
	  	final String scriptString = script;
	  	JButton button = new JButton(buttonLabel);
	    button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			    viewer.openStringInline(getPDBasString(pdbFileName));
			    if (scriptString != null){
			    	viewer.evalString(scriptString);
			    }
			}
	    });
	    return button;
	  }

	  public static JButton makeSensitiveLoadStructureButton(String buttonLabel, 
	  														String pdbFile,
															String baseScript,
															JCheckBox drugCheckBox,
															JCheckBox coxCheckBox,
															JCheckBox AcheckBox,
															JCheckBox BcheckBox,
															JCheckBox CcheckBox,
															JCheckBox DcheckBox,
															JmolPanel jmolPanel){
	  	final JmolSimpleViewer viewer = jmolPanel.getViewer();
	  	final String pdbFileName = pdbFile;
	  	final String scriptString = baseScript;
	  	final JCheckBox DrugCheckBox = drugCheckBox;
	  	final JCheckBox CoxCheckBox = coxCheckBox;
	  	final JCheckBox aCheckBox = AcheckBox;
	  	final JCheckBox bCheckBox = BcheckBox;
	  	final JCheckBox cCheckBox = CcheckBox;
	  	final JCheckBox dCheckBox = DcheckBox;
	  	
	  	JButton button = new JButton(buttonLabel);
	  	button.addActionListener(new ActionListener() {
	  		public void actionPerformed(ActionEvent e) {
	  			viewer.openStringInline(getPDBasString(pdbFileName));
	  			StringBuffer addOnScript = new StringBuffer();
	  			if (scriptString != null){
	  				if (DrugCheckBox.isSelected()){
	  					addOnScript.append(drugDotsScript);
	  				} else {
	  					addOnScript.append(drugSfScript);
	  				}
	  				
	  				if (CoxCheckBox.isSelected()){
	  					addOnScript.append(coxDotsScript);
	  				} else {
	  					addOnScript.append(coxSfScript);
	  				}
	  				
	  				if (aCheckBox.isSelected()){
	  					addOnScript.append(ApurpScript);
	  				} else {
	  					addOnScript.append(AcpkScript);
	  				}
	  				
	  				if (bCheckBox.isSelected()){
	  					addOnScript.append(BgreenScript);
	  				} else {
	  					addOnScript.append(BcpkScript);
	  				}

	  				if (cCheckBox.isSelected()){
	  					addOnScript.append(CpinkScript);
	  				} else {
	  					addOnScript.append(CcpkScript);
	  				}

	  				if (dCheckBox.isSelected()){
	  					addOnScript.append(DwhiteScript);
	  				} else {
	  					addOnScript.append(DcpkScript);
	  				}

	  				viewer.evalString(scriptString + addOnScript.toString());
	  			}
	  		}
	  	});
	  	return button;
	  }

	  public static JButton makeScriptButton(String buttonLabel, 
	  											String script,
												JmolPanel jmolPanel){
	  	final JmolSimpleViewer viewer = jmolPanel.getViewer();
	  	final String scriptString = script;
	  	JButton button = new JButton(buttonLabel);
	  	button.addActionListener(new ActionListener() {
	  		public void actionPerformed(ActionEvent e) {
	  			viewer.evalString(scriptString);
	  		}
	  	});
	  	return button;
	  }

	  public static JButton makeSizeSensitiveScriptButton(String buttonLabel, 
	  														String script,
															JRadioButton controlButton,
															JmolPanel jmolPanel){
	  	final JmolSimpleViewer viewer = jmolPanel.getViewer();
	  	final String scriptString = script;
	  	JButton button = new JButton(buttonLabel);
	  	final JRadioButton bsButton = controlButton;
	  	button.addActionListener(new ActionListener() {
	  		public void actionPerformed(ActionEvent e) {
	  		  	String sizeString = "spacefill on;";
	  		  	if (bsButton.isSelected()){
	  		  		sizeString = "spacefill 0.5; wireframe 0.2; ";
	  		  	} 
	  			viewer.evalString(scriptString + sizeString
	  					+ "center selected; zoom 400; translate center center;");
	  		}
	  	});
	  	return button;
	  }

	  public static String getPDBasString(String PDBfileName){
	    StringBuffer moleculeString = new StringBuffer();
	    URL moleculeURL = Molecules.class.getResource(PDBfileName);
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

	  static class JmolPanel extends JPanel {
	    JmolSimpleViewer viewer;
	    JmolAdapter adapter;
	    JmolPanel() {
	      adapter = new SmarterJmolAdapter(null);
	      viewer = JmolSimpleViewer.allocateSimpleViewer(this, adapter);
	    }

	    public JmolSimpleViewer getViewer() {
	      return viewer;
	    }

	    final Dimension currentSize = new Dimension();
	    final Rectangle rectClip = new Rectangle();

	    public void paint(Graphics g) {
	      getSize(currentSize);
	      g.getClipBounds(rectClip);
	      viewer.renderScreenImage(g, currentSize, rectClip);
	    }
	  }
	}
