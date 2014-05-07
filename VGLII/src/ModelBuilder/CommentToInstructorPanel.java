package ModelBuilder;

import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class CommentToInstructorPanel extends JPanel {
	private JLabel topLabel;
	private JLabel bottomLabel;
	private JTextArea comments;
	
	public CommentToInstructorPanel() {
		setLayout(new GridLayout(3,1));
		topLabel = new JLabel("Comments to instructor:");
		add(topLabel);
		comments = new JTextArea(10,20);
		add(comments);
		bottomLabel = new JLabel("200 character maximum");
		add(bottomLabel);
	}

}
