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
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jmol.adapter.smarter.SmarterJmolAdapter;
import org.jmol.api.JmolAdapter;
import org.jmol.api.JmolSimpleViewer;

/*
 * Created on Apr 19, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * @author brian
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Molecules {

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
				viewer.openStringInline("2\n"
						              + "atoms\n"
									  + "H  0.0 0.0 0.0\n"
									  + "H  0.5 0.0 0.0\n");
				viewer.evalString("restrict protein;");
			}
	    	
	    });
	    
	    JPanel problem1Panel = new JPanel();
	    problemPane.addTab("Problem 1.1.2(1)", problem1Panel);
	    problem1Panel.setLayout(new BoxLayout(problem1Panel, BoxLayout.Y_AXIS));
	    
	    problem1Panel.add(makeSimpleButton("The linear form of glucose",
	    		                           "D-glucose.pdb",
										   jmolPanel));
	    problem1Panel.add(makeSimpleButton("The linear form of fructose",
                							"D-fructose.pdb",
											jmolPanel));
	    problem1Panel.add(makeSimpleButton("The circular form of glucose",
											"beta-D-glucopyranose.pdb",
											jmolPanel));
	    problem1Panel.add(makeSimpleButton("The first amino acid",
											"AA1.PDB",
											jmolPanel));
	    problem1Panel.add(makeSimpleButton("The second amino acid",
											"AA2.PDB",
											jmolPanel));
	    problem1Panel.add(makeSimpleButton("The third amino acid",
											"AA3.PDB",
											jmolPanel));
	    problem1Panel.add(new JLabel(
	    		new ImageIcon(Molecules.class.getResource("cpkColors.gif"))));
	    
	    
	    JPanel problem2Panel = new JPanel();
	    problemPane.addTab("Problem 1.1.2(2)", problem2Panel);
	    problem2Panel.setLayout(new BoxLayout(problem2Panel, BoxLayout.Y_AXIS));
	    
	    problem2Panel.add(makeSimpleButton("The first tripeptide",
	    		                           "tripeptide1.pdb",
										   jmolPanel));
	    problem2Panel.add(makeSimpleButton("The second tripeptide",
                							"tripeptide2.pdb",
											jmolPanel));
	    problem2Panel.add(new JLabel(
	    		new ImageIcon(Molecules.class.getResource("cpkColors.gif"))));

	    contentPane.add(problemPane);
	    
	    frame.pack();
	    frame.setVisible(true);

	    //    viewer.openFile("../samples/caffeine.xyz");
	    //    viewer.openFile("http://database.server/models/1pdb.pdb.gz");
	    //    viewer.openStringInline(strXyzHOH);
	    //    viewer.evalString(strScript);
	    String strError = viewer.getOpenFileError();
	    if (strError != null)
	      System.out.println(strError);
	  }

	  public static JButton makeSimpleButton(String buttonLabel, 
	  		                                 String pdbFile,
											 JmolPanel jmolPanel){
	  	final JmolSimpleViewer viewer = jmolPanel.getViewer();
	  	final String pdbFileName = pdbFile;
	  	JButton button = new JButton(buttonLabel);
	    button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			    viewer.openStringInline(getPDBasString(pdbFileName));
			}
	    });
	    return button;
	  }
	  
	  public static String getPDBasString(String PDBfileName){
	    String moleculeString = new String();
	    URL moleculeURL = Molecules.class.getResource(PDBfileName);
	    InputStream moleculeInput = null;
		try {
			moleculeInput = moleculeURL.openStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
		BufferedReader moleculeStream = 
			new BufferedReader(new InputStreamReader(moleculeInput));
	    String inLine = null;
	    try {
			while ((inLine = moleculeStream.readLine())	!= null ){
				moleculeString += (inLine + "\n");
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        return moleculeString;
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
