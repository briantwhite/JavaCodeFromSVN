import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class SaveSingleWordAsArffUI extends JPanel {
	
	private InfoLabel infoLabel;
	private ScoreShiftTableModel scoreShiftTableModel;
	private JTable scoresTable;
	private TableSorter sorter;
	private JScrollPane scrollPane;
	private JButton saveFileButton;
	private JFileChooser saveFileChooser;


	public SaveSingleWordAsArffUI() {
		super();
		infoLabel = new InfoLabel("Hi there");
		this.setLayout(new BorderLayout());
		this.add(infoLabel, BorderLayout.NORTH);
		JPanel buttonPanel = new JPanel();
		saveFileButton = new JButton("Save scores to ARFF file");
		buttonPanel.setLayout(new FlowLayout());
		buttonPanel.add(saveFileButton);
		this.add(buttonPanel, BorderLayout.SOUTH);
		saveFileChooser = new JFileChooser();
		saveFileChooser.setDialogTitle("Save output to ARFF file");
	}
	
	public void createTable(int numRows){
		scoreShiftTableModel = new ScoreShiftTableModel(numRows);
		sorter = new TableSorter(scoreShiftTableModel);
		scoresTable = new JTable(sorter);
		scoresTable.setShowGrid(true);
		scoresTable.setGridColor(Color.BLACK);

		sorter.setTableHeader(scoresTable.getTableHeader());
		scrollPane = new JScrollPane(scoresTable);
		this.add(scrollPane, BorderLayout.CENTER);	
	}
	
	public void addScore(int rowNumber,
			int originalScore, 
			int count){
		scoreShiftTableModel.setValueAt(new Integer(originalScore),rowNumber,0);
		scoreShiftTableModel.setValueAt(new Integer(count),rowNumber,1);
		scoreShiftTableModel.setValueAt(new Integer(originalScore),rowNumber,2);
		scoreShiftTableModel.fireTableRowsInserted(rowNumber,rowNumber);
	}
	
	public void setInfoLabelText(String text){
		infoLabel.setText(text);
	}
	
	public ScoreShiftTableModel getScoreShiftTableModel() {
		return scoreShiftTableModel;
	}
	
	public void setHypsAndScores(ArrayList hypotheses,
			ScoredHypTableModel hypScoresModel) {
			final ArrayList hyps = hypotheses;
			final ScoredHypTableModel hypScoModel = hypScoresModel;
			
		saveFileButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveArffFile(hyps, hypScoModel);
			}
		});
	}
	
	public void saveArffFile(ArrayList hypotheses,
			ScoredHypTableModel hypScoresModel) {
		FileWriter arffFileWriter = null;
		if (saveFileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
			try {
				arffFileWriter = new FileWriter(saveFileChooser.getSelectedFile());
				
				//first, the header
				arffFileWriter.write("@RELATION score\n\n");
				
				for (int col = 2; col < hypScoresModel.getColumnCount(); col++){
					arffFileWriter.write("@ATTRIBUTE " + hypScoresModel.getColumnName(col)
							+ " {0,1}\n");
					arffFileWriter.write("%    attribute number: " + (col - 2) + "\n");
				}
				
				arffFileWriter.write("@ATTRIBUTE class {");
				StringBuffer possibleScores = new StringBuffer();
				for (int row = 0; row < scoreShiftTableModel.getRowCount(); row++) {
					possibleScores.append(scoreShiftTableModel.getValueAt(row,2)
							+ ",");
				}
				arffFileWriter.write(
						(possibleScores.deleteCharAt(possibleScores.length() - 1).toString()));
				arffFileWriter.write("}\n");
				
				arffFileWriter.write("@DATA \n");
				
				// make score conversion hash table
				TreeMap newScoreMap = new TreeMap();
				for (int row = 0; row < scoreShiftTableModel.getRowCount(); row++) {
					newScoreMap.put((Integer)scoreShiftTableModel.getValueAt(row,0),
							(Integer)scoreShiftTableModel.getValueAt(row,2));
				}
				
				//now write out the data
				for (int row = 0; row < hypScoresModel.getRowCount(); row++){
					arffFileWriter.write("{");
					for (int col = 2; col < hypScoresModel.getColumnCount(); col++) {
//						arffFileWriter.write(hypScoresModel.getValueAt(row,col)
//								+ ",");
						if (((Integer)hypScoresModel.getValueAt(row,col)).intValue() == 1) {
							arffFileWriter.write((col - 2) + " 1, ");
						}
					}
					//then the modified score
					Hypothesis hyp = (Hypothesis)hypotheses.get(row);
					Integer originalScore = new Integer(hyp.getScore());
					Integer newScore = (Integer)newScoreMap.get(originalScore);
					arffFileWriter.write((hypScoresModel.getColumnCount() - 2 ) 
							+ " " + newScore.toString() + "}\n");
				}
				
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			finally {
				try {
					arffFileWriter.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			
		}
	}
	
	
}
