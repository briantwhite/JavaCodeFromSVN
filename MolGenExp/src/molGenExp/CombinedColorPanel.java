package molGenExp;

import java.awt.Color;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import preferences.MGEPreferences;
import utilities.GlobalDefaults;

public class CombinedColorPanel extends JPanel {

	private JLabel colorChipLabel;
	private JLabel colorChip;

	public CombinedColorPanel() {
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		colorChipLabel = new JLabel("Combined Color:  ");
		this.add(colorChipLabel);
		colorChip = new JLabel("     ");
		colorChip.setOpaque(true);
		colorChip.setBackground(Color.WHITE);
		colorChip.setBorder(new LineBorder(Color.BLACK));
		this.add(colorChip);
	}
	
	public void setCombinedColor(Color color) {
		colorChip.setBackground(color);
		if (MGEPreferences.getInstance().isShowColorNameText()) {
			colorChip.setToolTipText(
					GlobalDefaults.colorModel.getColorName(color));
		} else {
			colorChip.setToolTipText(null);
		}
	}

}
