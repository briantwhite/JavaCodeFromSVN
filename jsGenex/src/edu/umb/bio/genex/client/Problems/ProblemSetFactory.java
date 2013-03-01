package edu.umb.bio.genex.client.Problems;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import edu.umb.bio.genex.client.GX.GenexGUI;

public class ProblemSetFactory {
	
	public static JPanel buildProblemSet(JDialog parentDialog, GenexGUI genexGUI, Problem[] problems) {
		JPanel masterPanel = new JPanel();
		ProblemPanel[] problemPanels = new ProblemPanel[problems.length];
		JTabbedPane tabs = new JTabbedPane();
		for (int i = 0; i < problems.length; i++) {
			problemPanels[i] = new ProblemPanel(parentDialog, problems[i], genexGUI);
			tabs.addTab("(" + (i+1) + ") " + problems[i].getName(), problemPanels[i]);
		}
		masterPanel.add(tabs);
		return masterPanel;
	}

}
