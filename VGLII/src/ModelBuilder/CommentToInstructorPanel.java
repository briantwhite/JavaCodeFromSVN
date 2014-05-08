package ModelBuilder;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Iterator;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.StyledDocument;

import org.jdom.Element;

public class CommentToInstructorPanel extends JPanel implements DocumentListener {
	
	private static final int MAX_CHARS = 200;
	
	private JLabel topLabel;
	private JLabel characterCountLabel;
	private JTextPane commentPane;
	private AbstractDocument comments;
	
	public CommentToInstructorPanel() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		topLabel = new JLabel("Comments to instructor:");
		add(topLabel);
		
		JPanel commentPanel = new JPanel();
		commentPanel.setLayout(new BoxLayout(commentPanel, BoxLayout.X_AXIS));
		commentPane = new JTextPane();
		commentPane.setMaximumSize(new Dimension(800,100));
		StyledDocument styledComments = commentPane.getStyledDocument();
		if (styledComments instanceof AbstractDocument) {
		    comments = (AbstractDocument) styledComments;
		    comments.setDocumentFilter(new DocumentSizeFilter(MAX_CHARS));
		}
		comments.addDocumentListener(this);
		commentPanel.add(commentPane);
		commentPanel.add(Box.createRigidArea(new Dimension(1,100)));
		add(commentPanel);
		
		characterCountLabel = new JLabel(MAX_CHARS + " characters remaining");
		add(characterCountLabel);
		
		
	}

	public void changedUpdate(DocumentEvent arg0) {
		updateCharCountLabel();
	}

	public void insertUpdate(DocumentEvent arg0) {
		updateCharCountLabel();
	}

	public void removeUpdate(DocumentEvent arg0) {
		updateCharCountLabel();
	}
	
	private void updateCharCountLabel() {
		int charsLeft = 200 - comments.getLength();
		characterCountLabel.setText(charsLeft + " characters remaining");
	}

	public String getAsHtml() {
		return commentPane.getText();
	}
	
	public void setStateFromFile(Element element) {
		List<Element> elements = element.getChildren();
		Iterator<Element> it = elements.iterator();
		while(it.hasNext()) {
			Element e = it.next();
			if (e.getName().equals("Comment")) {
				commentPane.setText((String)e.getText());
			}
		}
	}
	
	public Element save() {
		Element ce = new Element("CommentToInstructor");
		Element e = new Element("Comment");
		e.setText(commentPane.getText());
		ce.addContent(e);	
		return ce;
	}

}
