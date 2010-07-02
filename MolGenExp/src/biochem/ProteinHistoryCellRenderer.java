package biochem;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class ProteinHistoryCellRenderer extends JButton 
	implements ListCellRenderer {
	
	public ProteinHistoryCellRenderer() {
		super();
		setOpaque(true);
		setLayout(new BorderLayout());
	}

	public Component getListCellRendererComponent(JList list, 
			Object value, int index, boolean isSelected, boolean cellHasFocus) {
		FoldedProteinWithImages fp = (FoldedProteinWithImages)value;
		JButton button = new JButton(fp.getThumbnailPic());
		button.setOpaque(true);
		button.setBackground(fp.getColor());
		button.setBorder(BorderFactory.createLineBorder(
				isSelected ? Color.GREEN : Color.BLACK, 2));
		button.setToolTipText(fp.getToolTipText());
		return button;
	}

}
