package VGL;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class IndividualPanelSet {
	
	private JPanel organismPanel;
	private JPanel countsPanel;
	private JPanel[] phenotypePanels;
	private JPanel picturePanel;
	
	public IndividualPanelSet(JPanel organismPanel,
			JPanel countsPanel,
			JPanel[] phenotypePanels,
			JPanel picturePanel) {
		this.organismPanel = organismPanel;
		this.countsPanel = countsPanel;
		this.phenotypePanels = phenotypePanels;
		this.picturePanel = picturePanel;
	}

	public JPanel getOrganismPanel() {
		return organismPanel;
	}

	public JPanel getCountsPanel() {
		return countsPanel;
	}

	public JPanel[] getPhenotypePanels() {
		return phenotypePanels;
	}

	public JPanel getPicturePanel() {
		return picturePanel;
	}

}
