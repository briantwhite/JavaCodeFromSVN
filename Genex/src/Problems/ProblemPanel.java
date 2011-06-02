package Problems;

import java.awt.BorderLayout;
import java.awt.Color;
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
	
	private JLabel resultLabel;
	
	public ProblemPanel(Problem p, GenexGUI genexGUI) {
		super();
		prob = p;
		this.genexGUI = genexGUI;
		setupUI();
	}
	
	public void setupUI() {
		setLayout(new BorderLayout());
		add(new JLabel(HTML_START + prob.getDescription() + HTML_END), BorderLayout.PAGE_START);
		JButton checkAnswerButton = new JButton("Check Answer");
		checkAnswerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				checkAnswer();
			}
		});
		add(checkAnswerButton, BorderLayout.PAGE_END);
		resultLabel = new JLabel();
		add(resultLabel, BorderLayout.CENTER);
	}
	
	public void checkAnswer() {
		GenexState state = genexGUI.getState();
		if (state == null) return;
		String result = prob.evaluate(state);
		if (result.equals("OK")) {
			remove(resultLabel);
			resultLabel = new JLabel("Correct!");
			resultLabel.setOpaque(true);
			resultLabel.setBackground(Color.GREEN);
			add(resultLabel);
			revalidate();
			repaint();
		} else {
			remove(resultLabel);
			resultLabel = new JLabel(result);
			resultLabel.setOpaque(true);
			resultLabel.setBackground(Color.RED);
			add(resultLabel);
			revalidate();
			repaint();
		}
	}

}
