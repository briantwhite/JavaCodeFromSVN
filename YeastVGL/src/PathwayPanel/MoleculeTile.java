package PathwayPanel;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JMenuItem;

import YeastVGL.YeastVGL;

public class MoleculeTile extends DrawingPanelTile {
	
	public int selectedMolecule = -1;
	
	public MoleculeTile(YeastVGL yeastVGL, int row, int col) {
		super(yeastVGL, row, col);
		BLANK_BACKGROUND_COLOR = new Color(240, 240, 255);
		ACTIVE_BACKGROUND_COLOR = new Color(220, 220, 255);
		setBackground(BLANK_BACKGROUND_COLOR);
		setOpaque(true);
		JLabel text = new JLabel("M:r" + row + " c" + col);
		add(text);
		
		popupMenu.add(new JMenuItem("-"));
		for (int i = 0; i < yeastVGL.getPathway().getNumberOfMolecules(); i++) {
			JMenuItem item = new JMenuItem(String.valueOf(i));
			item.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String choice = ((JMenuItem)e.getSource()).getText();
					if (choice.equals("-")) {
						setBackground(BLANK_BACKGROUND_COLOR);
						selectedMolecule = -1;
					} else {
						setBackground(ACTIVE_BACKGROUND_COLOR);
						selectedMolecule = Integer.parseInt(choice);
						text.setText("Mol: " + selectedMolecule);
					}
					
				}				
			});
			popupMenu.add(item);
		}
		setComponentPopupMenu(popupMenu);
		
	}
}
