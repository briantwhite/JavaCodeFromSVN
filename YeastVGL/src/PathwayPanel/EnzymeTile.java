package PathwayPanel;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.SwingConstants;

import YeastVGL.YeastVGL;

public class EnzymeTile extends DrawingPanelTile {

	private int selectedEnzyme = -1;
	private JLabel text;
	private YeastVGL yeastVGL;

	public EnzymeTile(YeastVGL yeastVGL, int row, int col) {
		super(yeastVGL, row, col);
		this.yeastVGL = yeastVGL;
		BLANK_BACKGROUND_COLOR = new Color(240, 255, 240);
		ACTIVE_BACKGROUND_COLOR = new Color(200, 255, 200);
		setBackground(BLANK_BACKGROUND_COLOR);
		setOpaque(true);
		text = new JLabel();
		add(text);

		JMenuItem blankItem = new JMenuItem("-");
		blankItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectedEnzyme = -1;
				updateSelectedTile(selectedEnzyme);
			}			
		});
		popupMenu.add(blankItem); 
		// use the String complementation group name for display only
		//  within the application, use the proper index
		if ((yeastVGL.getPathwayPanel() != null) && (yeastVGL.getPathwayPanel().getCGNames() != null)) {
			String[] cgChoices = yeastVGL.getPathwayPanel().getCGNames();
			TreeMap<String, Integer> cgNames = yeastVGL.getPathwayPanel().getCGNumbers();
			for (int i = 0; i < cgChoices.length; i++) {
				JMenuItem item = new JMenuItem(cgChoices[i]);
				item.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						String choice = ((JMenuItem)e.getSource()).getText();
						selectedEnzyme = cgNames.get(choice);
						updateSelectedTile(selectedEnzyme);
					}						
				});
				popupMenu.add(item);
			}
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

	public void updateSelectedTile(int selectedEnzyme) {
		this.selectedEnzyme = selectedEnzyme;
		if (selectedEnzyme == -1) {
			setBackground(BLANK_BACKGROUND_COLOR);
			setBorder(BorderFactory.createEmptyBorder());
			text.setText("");
		} else {
			setBackground(ACTIVE_BACKGROUND_COLOR);
			text.setText("<html><align='center'>Enz<br>CG:<br><b>" + yeastVGL.getPathwayPanel().getCGNames()[selectedEnzyme] + "</b></align></html>");
			text.setHorizontalAlignment(SwingConstants.CENTER);
			text.setVerticalAlignment(SwingConstants.CENTER);
			setBorder(
					BorderFactory.createCompoundBorder(
							BorderFactory.createRaisedBevelBorder(), 
							BorderFactory.createLoweredBevelBorder()));
		}
	}

	public int getSelection() {
		return selectedEnzyme;
	}

	public void setSelection(int s) {
		selectedEnzyme = s;
		updateSelectedTile(selectedEnzyme);
	}
}
