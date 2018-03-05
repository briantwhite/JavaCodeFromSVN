package ComplementationTestPanel;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.google.gson.Gson;

import Biochemistry.MutantStrain;
import YeastVGL.State;
import YeastVGL.YeastVGL;

public class ComplementationTestPanel extends JPanel {

	private YeastVGL yeastVGL;
	private ComplementationTable complementationTable;
	private JCheckBox[] workingSetCheckboxes;


	public ComplementationTestPanel(YeastVGL yeastVGL) {
		this.yeastVGL = yeastVGL;

		// set up the UI
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.add(Box.createRigidArea(new Dimension(900,10)));
		
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
		
		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
		leftPanel.add(Box.createRigidArea(new Dimension(150,1)));
		
		JPanel instructionPanel = new CTPInstructionPanel();
		leftPanel.add(instructionPanel);
		mainPanel.add(leftPanel);
		
		JPanel middlePanel = new JPanel();
		middlePanel.setLayout(new BoxLayout(middlePanel, BoxLayout.Y_AXIS));
		middlePanel.add(Box.createRigidArea(new Dimension(600,1)));
		
		JPanel ctp = new JPanel();
		ctp.setBorder(BorderFactory.createTitledBorder("Complementation Table"));
		complementationTable = new ComplementationTable(yeastVGL);
		JScrollPane tablePane = new JScrollPane(complementationTable);
		tablePane.setPreferredSize(new Dimension(complementationTable.getPreferredSize().width + 30, 
				complementationTable.getRowHeight() * (complementationTable.getColumnHeadings().length + 1)));
		ctp.add(tablePane);
		middlePanel.add(ctp);
		mainPanel.add(middlePanel);
		
		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
		rightPanel.add(Box.createRigidArea(new Dimension(200,1)));
		
		JPanel wsp = new JPanel();
		wsp.setLayout(new BoxLayout(wsp, BoxLayout.Y_AXIS));
		wsp.setBorder(BorderFactory.createTitledBorder("Working Set"));
		wsp.add(Box.createRigidArea(new Dimension(200,1)));
		// now the working set of mutants
		workingSetCheckboxes = new JCheckBox[yeastVGL.getNumMutants()];
		for (int i = 0; i < yeastVGL.getNumMutants(); i++) {
			workingSetCheckboxes[i] = new JCheckBox("M" + i);
			workingSetCheckboxes[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					yeastVGL.getGUI().haveSomethingToSave();
				}
			});
		}
		for (int i = 0; i < workingSetCheckboxes.length; i++) {
			wsp.add(workingSetCheckboxes[i]);
		}
		rightPanel.add(wsp);
		mainPanel.add(rightPanel);
		
		this.add(mainPanel);
		this.revalidate();
	}

	public ArrayList<MutantStrain> getWorkingSet() {
		ArrayList<MutantStrain> workingSet = new ArrayList<MutantStrain>();
		for (int i = 0; i < workingSetCheckboxes.length; i++) {
			if (workingSetCheckboxes[i].isSelected()) {
				workingSet.add(yeastVGL.getMutantSet().getMutantStrains()[i]);
			}
		}
		return workingSet;
	}
	
	public String getJsonString() {
		boolean[] workingSet = new boolean[workingSetCheckboxes.length];
		for (int i = 0; i < workingSet.length; i++) {
			workingSet[i] = workingSetCheckboxes[i].isSelected();
		}
		State state = new State(yeastVGL.getMutantSet(), complementationTable.getData(), workingSet);
		Gson gson = new Gson();
		return gson.toJson(state);
	}
	
	public void updateState(State state) {
		yeastVGL.setMutantSet(state.getMutantSet());
		complementationTable.setData(state.getComplementationTableData());
		// need to fix column headings
		for (int i = 0; i < complementationTable.getData().length; i++) {
			complementationTable.getColumnModel().getColumn(i + 1).setHeaderValue(
					complementationTable.getData()[i][0].toString());
		}
		complementationTable.getTableHeader().repaint();
		complementationTable.revalidate();
		complementationTable.repaint();
		
		for (int i = 0; i < state.getWorkingSetChoices().length; i++) {
			workingSetCheckboxes[i].setSelected(state.getWorkingSetChoices()[i]);
		}
	}

	
}
