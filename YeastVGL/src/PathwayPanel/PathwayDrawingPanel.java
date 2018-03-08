package PathwayPanel;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import YeastVGL.YeastVGL;

public class PathwayDrawingPanel extends JPanel {

	private YeastVGL yeastVGL;

	public static final int NUM_ROWS = 3;
	public static final int NUM_COLS = 30;
	public static final int CELL_SPACING = 1;

	public PathwayDrawingPanel(YeastVGL yeastVGL) {
		this.yeastVGL = yeastVGL;

		JPanel innerPanel = new JPanel();
		innerPanel.setLayout(new GridLayout(NUM_ROWS, NUM_COLS, CELL_SPACING, CELL_SPACING));
		for (int i = 0; i < (NUM_ROWS * NUM_COLS); i++) {
			// first column
			if ((i % NUM_COLS) == 0) {
				if (i == (NUM_COLS)) {
					innerPanel.add(new PrecursorTile(yeastVGL));
				} else {
					innerPanel.add(new UneditableTile(yeastVGL));
				}
			} else {
				innerPanel.add(new EnzymeTile(yeastVGL));
			}
		}
		innerPanel.setPreferredSize(new Dimension(
				(NUM_COLS * (DrawingPanelTile.TILE_WIDTH + CELL_SPACING)) + 50,
				(NUM_ROWS * (DrawingPanelTile.TILE_HEIGHT + CELL_SPACING)) + 50));
		JScrollPane scroller = new JScrollPane(
				innerPanel, 
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, 
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scroller.setPreferredSize(new Dimension(
				800, (NUM_ROWS * (DrawingPanelTile.TILE_HEIGHT + CELL_SPACING) + 50)));
		add(scroller);
	}

}
