package PathwayPanel;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.SwingConstants;

import YeastVGL.YeastVGL;

public class EnzymeTile extends DrawingPanelTile {

	public int selectedEnzyme = -1;

	public EnzymeTile(YeastVGL yeastVGL, int row, int col) {
		super(yeastVGL, row, col);
		BLANK_BACKGROUND_COLOR = new Color(240, 255, 240);
		ACTIVE_BACKGROUND_COLOR = new Color(200, 255, 200);
		setBackground(BLANK_BACKGROUND_COLOR);
		setOpaque(true);
		JLabel text = new JLabel();
		add(text);

		JMenuItem blankItem = new JMenuItem("-");
		blankItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setBackground(BLANK_BACKGROUND_COLOR);
				selectedEnzyme = -1;
				setBorder(BorderFactory.createEmptyBorder());
				text.setText("");
			}			
		});
		popupMenu.add(blankItem);
		for (int i = 0; i < (yeastVGL.getPathway().getNumberOfEnzymes() + 2); i++) {
			JMenuItem item = new JMenuItem(String.valueOf(i));
			item.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String choice = ((JMenuItem)e.getSource()).getText();
					setBackground(ACTIVE_BACKGROUND_COLOR);
					selectedEnzyme = Integer.parseInt(choice);
					text.setText("<html><align='center'>Enz<br>CG:<br><b>" + selectedEnzyme + "</b></align></html>");
					text.setHorizontalAlignment(SwingConstants.CENTER);
					text.setVerticalAlignment(SwingConstants.CENTER);
					setBorder(
							BorderFactory.createCompoundBorder(
									BorderFactory.createRaisedBevelBorder(), 
									BorderFactory.createLoweredBevelBorder()));
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
