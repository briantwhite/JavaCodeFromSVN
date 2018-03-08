package PathwayPanel;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.SwingConstants;

import YeastVGL.YeastVGL;

public class MoleculeTile extends DrawingPanelTile {

	public int selectedMolecule = -1;
	public static final Color TERMINAL_MOLECULE_COLOR = new Color(200, 0, 0);

	public MoleculeTile(YeastVGL yeastVGL, int row, int col) {
		super(yeastVGL, row, col);
		BLANK_BACKGROUND_COLOR = new Color(240, 240, 255);
		ACTIVE_BACKGROUND_COLOR = new Color(220, 220, 255);
		setBackground(BLANK_BACKGROUND_COLOR);
		setOpaque(true);
		JLabel text = new JLabel();
		add(text);

		popupMenu.add(new JMenuItem("-"));
		for (int i = 0; i < yeastVGL.getPathway().getNumberOfMolecules(); i++) {
			JMenuItem item = new JMenuItem(String.valueOf(i));
			// shade the terminal molecules green
			if (yeastVGL.getPathway().getMolecules()[i].isTerminal()) {
				item.setForeground(TERMINAL_MOLECULE_COLOR);
			}
			item.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String choice = ((JMenuItem)e.getSource()).getText();
					if (choice.equals("-")) {
						setBackground(BLANK_BACKGROUND_COLOR);
						selectedMolecule = -1;
					} else {
						setBackground(ACTIVE_BACKGROUND_COLOR);
						selectedMolecule = Integer.parseInt(choice);
						text.setText("<html><align='center'>Mol<br><b>" + selectedMolecule + "</b></align></html>");
						if (yeastVGL.getPathway().getMolecules()[selectedMolecule].isTerminal()) {
							text.setForeground(TERMINAL_MOLECULE_COLOR);
						} else {
							text.setForeground(Color.BLACK);
						}
						text.setHorizontalAlignment(SwingConstants.CENTER);
						text.setVerticalAlignment(SwingConstants.CENTER);
					}		
				}				
			});
			popupMenu.add(item);
		}
		setComponentPopupMenu(popupMenu);
	}
}
