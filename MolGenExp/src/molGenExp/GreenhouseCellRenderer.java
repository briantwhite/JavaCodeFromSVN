package molGenExp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import molBiol.ExpressedGene;

import biochem.FoldedPolypeptide;

public class GreenhouseCellRenderer extends JButton 
	implements ListCellRenderer {
	
	public GreenhouseCellRenderer() {
		super();
		setOpaque(true);
		setLayout(new BorderLayout());
	}

	public Component getListCellRendererComponent(JList list, 
			Object value, int index, boolean isSelected, boolean cellHasFocus) {
		Organism o = (Organism)value;
		JButton button = 
			new JButton(o.getImage());
		button.setBackground(Color.LIGHT_GRAY);
		button.setBorder(BorderFactory.createLineBorder(
				isSelected ? Color.GREEN : Color.BLACK, 2));
		return button;
	}

}
