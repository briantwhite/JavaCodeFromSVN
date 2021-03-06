package edu.umb.jsAipotu.molGenExp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import edu.umb.jsAipotu.preferences.GlobalDefaults;
import edu.umb.jsAipotu.preferences.MGEPreferences;

public class OrganismCellRenderer extends JButton 
implements ListCellRenderer {

	public OrganismCellRenderer() {
		super();
		setOpaque(true);
		setLayout(new BorderLayout());
	}

	public Component getListCellRendererComponent(JList list, 
			Object value, int index, boolean isSelected, boolean cellHasFocus) {
		Organism o = (Organism)value;
		JButton button = 
			new JButton(o.getName(), 
					o.getImage());
		button.setBackground(Color.LIGHT_GRAY);
		button.setBorder(BorderFactory.createLineBorder(
				isSelected ? Color.GREEN : Color.BLACK, 2));
		button.setVerticalTextPosition(AbstractButton.BOTTOM);
		button.setHorizontalTextPosition(AbstractButton.CENTER);

		if (MGEPreferences.getInstance().isShowColorNameText()) {
			button.setToolTipText(GlobalDefaults.colorModel.getColorName(o.getColor()));
		} else {
			button.setToolTipText(null);
		}

		return button;
	}

}
