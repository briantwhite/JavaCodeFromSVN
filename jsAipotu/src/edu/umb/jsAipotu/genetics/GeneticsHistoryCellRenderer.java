package edu.umb.jsAipotu.genetics;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;

import preferences.MGEPreferences;

public class GeneticsHistoryCellRenderer extends JButton 
implements ListCellRenderer {

	public GeneticsHistoryCellRenderer() {
		super();
		setOpaque(true);
		setLayout(new BorderLayout());
	}

	public Component getListCellRendererComponent(JList list, 
			Object value, int index, boolean isSelected, boolean cellHasFocus) {
		Tray tray = (Tray)value;
		JButton button = 
			new JButton("Tray " + tray.getNumber(), tray.getThumbImage());
		button.setHorizontalTextPosition(SwingConstants.CENTER);
		button.setVerticalTextPosition(SwingConstants.BOTTOM);
		button.setBorder(BorderFactory.createLineBorder(
				isSelected ? Color.GREEN : Color.BLACK, 2));

		if (MGEPreferences.getInstance().isShowColorNameText()) {
			button.setToolTipText("<html>" + tray.getParentInfo() + "<br>"
					+ tray.getColorCountInfo() + "</html>");
		} else {
			button.setToolTipText(tray.getParentInfo());
		}
		return button;
	}

}
