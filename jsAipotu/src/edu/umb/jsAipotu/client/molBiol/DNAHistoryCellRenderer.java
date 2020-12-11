package edu.umb.jsAipotu.client.molBiol;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class DNAHistoryCellRenderer extends JButton 
	implements ListCellRenderer {
	
	public DNAHistoryCellRenderer() {
		super();
		setOpaque(true);
		setLayout(new BorderLayout());
	}

	public Component getListCellRendererComponent(JList list, 
			Object value, int index, boolean isSelected, boolean cellHasFocus) {
		ExpressedGeneWithImages mbhli = (ExpressedGeneWithImages)value;
		JButton button = 
			new JButton(mbhli.getFoldedProteinWithImages().getThumbnailPic());
		button.setOpaque(true);
		button.setBackground(mbhli.getFoldedProteinWithImages().getColor());
		button.setBorder(BorderFactory.createLineBorder(
				isSelected ? Color.GREEN : Color.BLACK, 2));
		button.setToolTipText(mbhli.getToolTipText());
		return button;
	}

}
