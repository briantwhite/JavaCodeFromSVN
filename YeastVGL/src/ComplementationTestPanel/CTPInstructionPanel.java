package ComplementationTestPanel;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JEditorPane;
import javax.swing.JPanel;

public class CTPInstructionPanel extends JPanel {
	
	public CTPInstructionPanel() {
		super();
		setBorder(BorderFactory.createTitledBorder("Instructions"));
		setSize(new Dimension(400,800));
		String text = "<html><div WIDTH="
				+ getWidth() + ">"
				+ "<ol>"
				+ "<li>Determine your complementation groups using the data at the left</li>"
				+ "<ul>"
				+ "<li>You can drag the data columns to the left or right to make it easier to see the groups.</li>"
				+ "<li>You can save your work along the way.</li>"
				+ "</ul>"
				+ "<li>You should then assign a complementation group letter to each group by clicking in the corresponding cell"
				+ " and choosing from the list that pops up.</li>"
				+ "<li>Choose one member of each group for your working set by checking the box at the right.</li>"
				+ "<li>Save your work before continuing to the Pathway pane.</li>"
				+ "</ol>"
				+ "</div></html>";
		JEditorPane label = new JEditorPane();
		label.setContentType("text/html");
		label.setText(text);
		add(label);
	}
}
