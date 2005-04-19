import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

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
	    contentPane.setLayout(new GridLayout(1,2));
	    
	    JmolPanel jmolPanel = new JmolPanel();
	    contentPane.add(jmolPanel);
	    
	    JTabbedPane problemPane = new JTabbedPane();
	    
	    JPanel problem1Panel = new JPanel();
	    problem1Panel.add(new JButton("hi"));
	    problem1Panel.add(new JButton("testing"));
	    problemPane.addTab("Problem 1", problem1Panel);
	    
	    JPanel problem2Panel = new JPanel();
	    problemPane.addTab("Problem 2", problem2Panel);
	    
	    contentPane.add(problemPane);
	    
	    frame.setSize(600, 300);
	    frame.setVisible(true);

	    JmolSimpleViewer viewer = jmolPanel.getViewer();
	    //    viewer.openFile("../samples/caffeine.xyz");
	    //    viewer.openFile("http://database.server/models/1pdb.pdb.gz");
	    //    viewer.openStringInline(strXyzHOH);
	    //    viewer.evalString(strScript);
	    viewer.openStringInline(getPDBasString("D-glucose.pdb"));
	    String strError = viewer.getOpenFileError();
	    if (strError != null)
	      System.out.println(strError);
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
	  final static String strXyzHOH = 
	    "3\n" +
	    "water\n" +
	    "O  0.0 0.0 0.0\n" +
	    "H  0.76923955 -0.59357141 0.0\n" +
	    "H -0.76923955 -0.59357141 0.0\n";

	  final static String strScript = "delay; move 360 0 0 0 0 0 0 0 4;";

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
