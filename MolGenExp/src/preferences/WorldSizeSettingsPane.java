package preferences;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerListModel;

public class WorldSizeSettingsPane extends PreferencePane {
	
	MGEPreferences prefs = MGEPreferences.getInstance();

	private JSpinner sizeSpinner;

	public WorldSizeSettingsPane(PreferencesDialog parentDialog) {
		super(parentDialog);
	}

	protected void cancel() {
		parentDialog.setVisible(false);
	}

	protected void ok() {
		prefs.setWorldSize(Integer.parseInt((String)sizeSpinner.getValue()));
		parentDialog.setVisible(false);
	}

	protected void restoreDefaults() {
		prefs.setWorldSize(prefs.DEFAULT_WORLD_SIZE);
		parentDialog.setVisible(false);
	}

	protected JPanel setupCustomPanel() {
		JPanel customPanel = new JPanel();
		JLabel sizeLabel = new JLabel("World size (x by x organisms)");
		customPanel.add(sizeLabel);
		
		//must divide 500 evenly
		String[] allowableSizes = {"10", "20", "25", "50"};
		SpinnerListModel model = new SpinnerListModel(allowableSizes);
		sizeSpinner = new JSpinner(model);
		customPanel.add(sizeSpinner);
		
		return customPanel;
	}

}
