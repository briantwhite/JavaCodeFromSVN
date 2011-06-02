package Problems;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import GX.GenexGUI;

public class ProblemSetFactory {
	
	public static JPanel buildProblemSet(GenexGUI genexGUI, Problem[] problems) {
		JPanel masterPanel = new JPanel();
		ProblemPanel[] problemPanels = new ProblemPanel[problems.length];
		JTabbedPane tabs = new JTabbedPane();
		for (int i = 0; i < problems.length; i++) {
			problemPanels[i] = new ProblemPanel(problems[i]);
			tabs.addTab("(" + (i+1) + ") " + problems[i].getName(), problemPanels[i]);
		}
		masterPanel.add(tabs);
		return masterPanel;
	}

}
