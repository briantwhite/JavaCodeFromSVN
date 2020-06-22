package jsVGLReader;


import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class GraderHelp {

	public static void showhelp() {
		JPanel panel = new JPanel();
		JLabel label = new JLabel(
				"<html>" 
				+ "<b>Notes on interpreting the grader display:</b><br>"
				+ "<br>"
				+ "(1) Under &quot;Student Answer&quot;, the text in gray indicates that<br>"
				+ "part of the model was given to the student; they did not need to<br>"
				+ "determine it.<br>"
				+ "<br>(2) Under &quot;Relevant Cages&quot;, &quot;?&quot; means no cage was<br>"
				+ "selected.<br>"
				+ "<br>(3) The information from each cage is scored generously: all crosses that<br>"
				+ "show sex-linkage also dominance even though, strictly speaking,<br>"
				+ "you need to know if it is XX/XY or ZZ/ZW to be sure.<br>"
				+ "<br>(4) It may run more clearly in English than any other language.<br>"
				+ "<br>"
				+ "</html>");
		panel.add(label);
		
		JOptionPane.showMessageDialog(null, 
				panel,
				"Grader Help",
				JOptionPane.PLAIN_MESSAGE);
	}
}
