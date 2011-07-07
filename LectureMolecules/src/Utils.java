import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JRadioButton;

import org.jmol.api.JmolViewer;


public class Utils {
	
	public static final String htmlStart = new String("<html><font size=+5><font color=white>");
	public static final String htmlEnd = new String("</font></font></html>");
	public static final String darkGray = new String("color [100,100,100];");
	public static final String lightYellow = new String("color [200,200,0];");
	
	public static JButton makeLoadStructureButton(String buttonLabel, 
			final String pdbFile,
			final String script,
			final String caption,
			final JLabel captionLabel,
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
	
	
	public static JButton makeScriptButton(String buttonLabel, 
			final String script,
			final String caption,
			final JLabel captionLabel,
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
	
	public static JRadioButton[] makeSpinToggleButtons(JmolPanel jmolPanel){
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
	
	public static String getPDBasString(String PDBfileName){
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

}
