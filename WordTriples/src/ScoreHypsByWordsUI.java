import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;

public class ScoreHypsByWordsUI extends JPanel {
	private JLabel infoLabel;
	private JFileChooser outputFileChooser;
	private JTable hypsTable;
	private ScoredHypTableModel scoredHypTableModel;
	private JScrollPane hypScrollPane;
	
	public ScoreHypsByWordsUI() {
		super();
		infoLabel = new InfoLabel("Hi there");
		this.setLayout(new BorderLayout());
		this.add(infoLabel, BorderLayout.NORTH);
	}
	
	public void createTable(int numRows, String[] words){
		scoredHypTableModel = new ScoredHypTableModel(numRows, words);
		hypsTable = new JTable(scoredHypTableModel);
		hypsTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		hypsTable.setShowGrid(true);
		hypsTable.setGridColor(Color.BLACK);

		hypScrollPane = new JScrollPane(hypsTable);
		hypScrollPane.setHorizontalScrollBarPolicy(
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		this.add(hypScrollPane, BorderLayout.CENTER);	
	}

	public void addHyp(int rowNumber,  
			String hypothesisText, 
			int[] scores){
		
		scoredHypTableModel.setValueAt(new Integer(rowNumber),rowNumber,0);
		scoredHypTableModel.setValueAt(hypothesisText,rowNumber,1);
		for (int i = 1; i < (scores.length); i++) {
			scoredHypTableModel.setValueAt(new Integer(scores[i]),rowNumber, (i + 1));
		}
		
		scoredHypTableModel.fireTableRowsInserted(rowNumber,rowNumber);		
	}
	
	public void setInfoLabelText(String text){
		infoLabel.setText(text);
	}
	
	public ScoredHypTableModel getScoresTableModel() {
		return scoredHypTableModel;
	}

}
