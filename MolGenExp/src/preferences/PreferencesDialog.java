package preferences;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import molGenExp.MolGenExp;

public class PreferencesDialog extends JDialog {
	
	private MolGenExp mge;
	
	private JTabbedPane tabPane;
	
	private MutationSettingsPane mutationSettingsPane;
	private EvolutionPicturesPane evolutionPicturesPane;
		
	public PreferencesDialog(MolGenExp mge) {
		super();
		this.mge = mge;
		setupUI();
	}
	
	private void setupUI() {
		
		JPanel topPanel = new JPanel();
		tabPane = new JTabbedPane();
		
		mutationSettingsPane = new MutationSettingsPane(this);
		tabPane.add(mutationSettingsPane, "Mutation Rates");
		
		evolutionPicturesPane = new EvolutionPicturesPane(this);
		tabPane.add(evolutionPicturesPane, "Images of each Generation");
		
		topPanel.add(tabPane);
		
		add(topPanel);
				
		pack();
	}
}
