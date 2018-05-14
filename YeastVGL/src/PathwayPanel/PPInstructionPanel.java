package PathwayPanel;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JEditorPane;
import javax.swing.JPanel;

public class PPInstructionPanel extends JPanel {

	public PPInstructionPanel() {
		super();
		setBorder(BorderFactory.createTitledBorder("Instructions"));
		setSize(new Dimension(400,200));
		String text = "<html><div WIDTH="
				+ getWidth() + ">"
				+ "<ol>"
				+ "<li>Use the tools at the left to work out the pathway.</li>"
				+ "<ul>"
				+ "<li>Select a genotype using one or more of the mutations in your working group.</li>"
				+ "<li>Select one or more of the molecules in the pathway to put in the medium.</li>"
				+ "<ul>"
				+ "<li>Note that the end product(s) of the pathway are shown in <font color='red'>red</font>.</li>"
				+ "</ul>"
				+ "</ul>"
				+ "<li>Once you have determined the pathway, you can enter it in the chart below.</li>"
				+ "<li>You can the check to see if you have it correct.</li>"
				+ "<li>Be sure to save your work as you go.</li>"
				+ "</ol>"
				+ "</div></html>";
		JEditorPane label = new JEditorPane();
		label.setContentType("text/html");
		label.setText(text);
		add(label);
	}
}
