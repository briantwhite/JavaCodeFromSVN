package edu.umb.jsAipotu.client.molGenExp;

import java.awt.Color;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import com.google.gwt.canvas.dom.client.CssColor;

import edu.umb.jsAipotu.client.preferences.GlobalDefaults;
import edu.umb.jsAipotu.client.preferences.MGEPreferences;

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
	
	public void setCombinedColor(CssColor color) {
		colorChip.setBackground(color);
		if (MGEPreferences.getInstance().isShowColorNameText()) {
			colorChip.setToolTipText(
					GlobalDefaults.colorModel.getColorName(color));
		} else {
			colorChip.setToolTipText(null);
		}
	}

}
