package preferences;

import java.awt.GridLayout;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerListModel;

public class WorldSettingsPane extends PreferencePane {
	
	MGEPreferences prefs = MGEPreferences.getInstance();

	private JSpinner sizeSpinner;
	private JCheckBox showBothAllelesCheckbox;

	public WorldSettingsPane(PreferencesDialog parentDialog) {
		super(parentDialog);
	}

	protected void cancel() {
		parentDialog.setVisible(false);
	}

	protected void ok() {
		prefs.setWorldSize(Integer.parseInt((String)sizeSpinner.getValue()));
		prefs.setShowBothAllelesInWorld(showBothAllelesCheckbox.isSelected());
		parentDialog.setVisible(false);
	}

	protected void restoreDefaults() {
		prefs.setWorldSize(MGEPreferences.DEFAULT_WORLD_SIZE);
		prefs.setShowBothAllelesInWorld(MGEPreferences.DEFAULT_SHOW_BOTH_ALLELES);
		parentDialog.setVisible(false);
	}

	protected JPanel setupCustomPanel() {
		JPanel customPanel = new JPanel();
		customPanel.setLayout(new GridLayout(2,2));
		
		JLabel sizeLabel = new JLabel("World size (x by x organisms)");
		customPanel.add(sizeLabel);
		//must divide 500 evenly
		String[] allowableSizes = {"10", "20", "25", "50"};
		SpinnerListModel model = new SpinnerListModel(allowableSizes);
		sizeSpinner = new JSpinner(model);
		customPanel.add(sizeSpinner);
		
		JLabel bothAllelesLabel = new JLabel("Show Colors of Both Alleles");
		customPanel.add(bothAllelesLabel);
		showBothAllelesCheckbox = new JCheckBox();
		showBothAllelesCheckbox.setSelected(MGEPreferences.DEFAULT_SHOW_BOTH_ALLELES);
		customPanel.add(showBothAllelesCheckbox);
		
		return customPanel;
	}

}
