package Problems;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class ProblemPanel extends JPanel {
	
	private static final String HTML_START = "<html><body>";
	private static final String HTML_END = "</body></html>";
	
	public ProblemPanel(Problem p) {
		super();
		add(new JLabel(HTML_START + p.getDescription() + HTML_END));
	}

}
