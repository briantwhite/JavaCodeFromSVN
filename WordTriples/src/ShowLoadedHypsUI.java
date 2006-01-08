import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

public class ShowLoadedHypsUI extends JPanel {
	
	private InfoLabel infoLabel;
	private JTextPane hypsPane;
	private JScrollPane hypsScrollPane;


	public ShowLoadedHypsUI() {
		super();
		infoLabel = new InfoLabel("hi there");
		hypsPane = new JTextPane();
		hypsScrollPane = new JScrollPane(hypsPane);
		this.setLayout(new BorderLayout());
		this.add(hypsScrollPane, BorderLayout.CENTER);
		this.add(infoLabel, BorderLayout.NORTH);
	}
	
	public void setInfoLabel(String newLabel){
		infoLabel.setText(newLabel);
	}
	
	public void setHypsPane(String hyps){
		hypsPane.setText(hyps);
	}
}
