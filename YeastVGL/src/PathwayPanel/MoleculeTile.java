package PathwayPanel;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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

		JMenuItem blankItem = new JMenuItem("-");
		blankItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setBackground(BLANK_BACKGROUND_COLOR);
				selectedMolecule = -1;
				text.setText("");
			}			
		});
		popupMenu.add(blankItem);
		for (int i = 0; i < yeastVGL.getPathway().getNumberOfMolecules(); i++) {
			JMenuItem item = new JMenuItem(String.valueOf(i));
			// shade the terminal molecules green
			if (yeastVGL.getPathway().getMolecules()[i].isTerminal()) {
				item.setForeground(TERMINAL_MOLECULE_COLOR);
			}
			item.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String choice = ((JMenuItem)e.getSource()).getText();
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
			});
			popupMenu.add(item);
		}
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				showPopup(e);
			}
			public void mouseReleased(MouseEvent e) {
				showPopup(e);
			}
			private void showPopup(MouseEvent e) {
				popupMenu.show(e.getComponent(),
						e.getX(), e.getY());
			}
		});
	}
}
