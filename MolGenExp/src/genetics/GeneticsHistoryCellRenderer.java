package genetics;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import biochem.FoldedPolypeptide;

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
			new JButton(tray.getThumbImage());
		button.setBorder(BorderFactory.createLineBorder(
				isSelected ? Color.GREEN : Color.BLACK, 2));
//		button.setToolTipText(tray.getFoldedPolypeptide().getAaSeq());
		return button;
	}

}
