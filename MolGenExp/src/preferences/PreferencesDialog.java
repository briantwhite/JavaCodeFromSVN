package preferences;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import molGenExp.MolGenExp;

public class PreferencesDialog extends JDialog {
	
	private MolGenExp mge;
	
	private JTabbedPane tabPane;
	
	private WorldSizeSettingsPane worldSizeSettingsPane;
	private MutationSettingsPane mutationSettingsPane;
	private EvolutionPicturesPane evolutionPicturesPane;
	private ServerPreferencesPane serverPreferencesPane;
		
	public PreferencesDialog(MolGenExp mge) {
		super();
		this.mge = mge;
		setupUI();
	}
	
	private void setupUI() {
		
		JPanel topPanel = new JPanel();
		tabPane = new JTabbedPane();
		
		worldSizeSettingsPane = new WorldSizeSettingsPane(this);
		tabPane.add(worldSizeSettingsPane, "World Size");
		
		mutationSettingsPane = new MutationSettingsPane(this);
		tabPane.add(mutationSettingsPane, "Mutation Rates");
		
		evolutionPicturesPane = new EvolutionPicturesPane(this);
		tabPane.add(evolutionPicturesPane, "Images of each Generation");
		
		serverPreferencesPane = new ServerPreferencesPane(this);
		tabPane.add(serverPreferencesPane, "Folding Server");
		
		topPanel.add(tabPane);
		
		add(topPanel);
				
		pack();
	}
	
	public MolGenExp getMGE() {
		return mge;
	}
}
