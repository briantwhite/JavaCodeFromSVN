package preferences;

import java.awt.GridLayout;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class DisplaySettingsPane extends PreferencePane {
	
	MGEPreferences prefs = MGEPreferences.getInstance();
	
	private JCheckBox showColorNamesAsTextCheckbox;

	public DisplaySettingsPane(PreferencesDialog parentDialog) {
		super(parentDialog);
	}
	
	protected void cancel() {
		parentDialog.setVisible(false);
	}

	protected void ok() {
		prefs.setShowColorNameText(
				showColorNamesAsTextCheckbox.isSelected());
		parentDialog.setVisible(false);
	}

	protected void restoreDefaults() {
		prefs.setShowColorNameText(
				MGEPreferences.DEFAULT_SHOW_COLOR_NAME_TEXT);
		parentDialog.setVisible(false);
	}

	protected JPanel setupCustomPanel() {
		JPanel customPanel = new JPanel();
		customPanel.setLayout(new GridLayout(1,2));
		
		customPanel.add(new JLabel("Show names of colors in popup labels"));
		showColorNamesAsTextCheckbox = new JCheckBox();
		showColorNamesAsTextCheckbox.setSelected(
				MGEPreferences.DEFAULT_SHOW_COLOR_NAME_TEXT);
		customPanel.add(showColorNamesAsTextCheckbox);
		return customPanel;
	}

}
