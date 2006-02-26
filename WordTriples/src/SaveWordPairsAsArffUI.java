import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import cern.colt.matrix.impl.SparseDoubleMatrix3D;

public class SaveWordPairsAsArffUI extends JPanel {
	
	private InfoLabel infoLabel;
	private ScoreShiftTableModel scoreShiftTableModel;
	private JTable scoresTable;
	private TableSorter sorter;
	private JScrollPane scrollPane;
	private JButton saveFileButton;
	private JFileChooser saveFileChooser;


	public SaveWordPairsAsArffUI() {
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
								 HashMap wordCodeMap,
								 TreeMap pairMap){
			final ArrayList hyps = hypotheses;
			final HashMap wcMap = wordCodeMap;
			final TreeMap pMap = pairMap;
			
			
		saveFileButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveArffFile(hyps, wcMap, pMap);
			}
		});
		
	}
	
	public void saveArffFile(ArrayList hypotheses, 
							HashMap wordCodeMap,
							TreeMap pairMap) {
		// make the header
		StringBuffer headerBuffer = new StringBuffer();
		headerBuffer.append("@RELATION score\n\n");
		
		Iterator scoreIt = pairMap.keySet().iterator();
		while (scoreIt.hasNext()){
			headerBuffer.append("@ATTRIBUTE " 
					+ (String)scoreIt.next()
					+ " {0,1} \n");
		}
		
		headerBuffer.append("@ATTRIBUTE class {");
		StringBuffer possibleScores = new StringBuffer();
		for (int row = 0; row < scoreShiftTableModel.getRowCount(); row++) {
			possibleScores.append(scoreShiftTableModel.getValueAt(row,2)
					+ ",");
		}
		headerBuffer.append(
				(possibleScores.deleteCharAt(possibleScores.length() - 1).toString()));
		headerBuffer.append("}\n");
		
		headerBuffer.append("@DATA \n");

		// make score conversion hash table
		TreeMap newScoreMap = new TreeMap();
		for (int row = 0; row < scoreShiftTableModel.getRowCount(); row++) {
			newScoreMap.put((Integer)scoreShiftTableModel.getValueAt(row,0),
					(Integer)scoreShiftTableModel.getValueAt(row,2));
		}

		Iterator hypIterator = hypotheses.iterator();
		TreeMap scoreMap = new TreeMap();
		TreeMap scoreCounts = new TreeMap();
		while (hypIterator.hasNext()){
			Hypothesis hypothesis = (Hypothesis)hypIterator.next();
			Integer score = new Integer(hypothesis.getScore());
			scoreMap.put(score, score);
			if (!scoreCounts.containsKey(score)){
				scoreCounts.put(score, new Integer(1));
			} else {
				int oldCount = ((Integer)scoreCounts.get(score)).intValue();
				scoreCounts.put(score, new Integer(oldCount + 1));
			}
		}
		
		createTable(scoreMap.size());
		Iterator scoreIterator = scoreMap.keySet().iterator();
		int rowNumber = 0;
		while (scoreIterator.hasNext()){
			Integer currentScore = (Integer)scoreIterator.next();
			addScore(rowNumber, 
					currentScore.intValue(),
					((Integer)scoreCounts.get(currentScore)).intValue());
			rowNumber++;
		}

		StringBuffer scoresBuffer = new StringBuffer();
		Iterator hypIt = hypotheses.iterator();
		while (hypIt.hasNext()){
			TreeMap workingMap = new TreeMap(pairMap);
			Hypothesis hyp = (Hypothesis)hypIt.next();
			int[] wordCodeSet = hyp.getCodeList(wordCodeMap);
			for (int i = 0; i < (wordCodeSet.length - 1); i++) {
				String key = wordCodeSet[i] + "-" + wordCodeSet[i+1];
				if (workingMap.containsKey(key)) {
					workingMap.put(key, new Integer(1));
				}
			}
			
			scoreIterator = workingMap.keySet().iterator();
			while (scoreIterator.hasNext()) {
				String key = (String)scoreIterator.next();
				scoresBuffer.append((Integer)workingMap.get(key) + ",");
			}
			Integer originalScore = new Integer(hyp.getScore());
			Integer newScore = (Integer)newScoreMap.get(originalScore);
			scoresBuffer.append(newScore);
			scoresBuffer.append("\n");
		}
		
		FileWriter arffFileWriter = null;
		if (saveFileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
			try {
				arffFileWriter = new FileWriter(saveFileChooser.getSelectedFile());
				arffFileWriter.write(headerBuffer.toString());
				arffFileWriter.write(scoresBuffer.toString());	
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
