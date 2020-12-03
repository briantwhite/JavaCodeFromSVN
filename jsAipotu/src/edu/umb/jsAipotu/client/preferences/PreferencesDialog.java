package edu.umb.jsAipotu.client.preferences;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import edu.umb.jsAipotu.client.molGenExp.MolGenExp;

public class PreferencesDialog extends JDialog {

	private MolGenExp mge;

	private JTabbedPane tabPane;

	private DisplaySettingsPane displaySettingsPane;
	private WorldSettingsPane worldSettingsPane;
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
		
		displaySettingsPane = new DisplaySettingsPane(this);
		tabPane.add(displaySettingsPane, "Display Settings");

		worldSettingsPane = new WorldSettingsPane(this);
		tabPane.add(worldSettingsPane, "World Settings");

		mutationSettingsPane = new MutationSettingsPane(this);
		tabPane.add(mutationSettingsPane, "Mutation Rates");

		evolutionPicturesPane = new EvolutionPicturesPane(this);
		tabPane.add(evolutionPicturesPane, "Images of each Generation");

		topPanel.add(tabPane);

		getContentPane().add(topPanel);

		pack();
	}

	public MolGenExp getMGE() {
		return mge;
	}
}
