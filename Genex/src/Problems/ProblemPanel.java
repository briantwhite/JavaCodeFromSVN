package Problems;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import GX.GenexGUI;

public class ProblemPanel extends JPanel {
	
	private static final String HTML_START = "<html><body>";
	private static final String HTML_END = "</body></html>";
	
	private Problem prob;
	private GenexGUI genexGUI;
	
	public ProblemPanel(Problem p, GenexGUI genexGUI) {
		super();
		prob = p;
		this.genexGUI = genexGUI;
		setupUI();
	}
	
	public void setupUI() {
		add(new JLabel(HTML_START + prob.getDescription() + HTML_END));
		JButton checkAnswerButton = new JButton("Check Answer");
		checkAnswerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				checkAnswer();
			}
		});
		add(checkAnswerButton);
	}
	
	public void checkAnswer() {
		GenexState state = genexGUI.getState();
	}

}
