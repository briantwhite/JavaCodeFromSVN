package PathwayPanel;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import YeastVGL.State;
import YeastVGL.YeastVGL;

public class PathwayDrawingPanel extends JPanel {

	private YeastVGL yeastVGL;

	public static final int NUM_ROWS = 3;
	public static final int NUM_COLS = 25;
	public static final int CELL_SPACING = 1;

	private DrawingPanelTile[][] tileArray;
	private JPanel innerPanel;

	public PathwayDrawingPanel(YeastVGL yeastVGL) {
		this.yeastVGL = yeastVGL;

		tileArray = new DrawingPanelTile[NUM_ROWS][NUM_COLS];
		innerPanel = new JPanel();
		innerPanel.setLayout(new GridLayout(NUM_ROWS, NUM_COLS, CELL_SPACING, CELL_SPACING));
		for (int i = 0; i < (NUM_ROWS * NUM_COLS); i++) {
			int row = i/NUM_COLS;
			int col = i % NUM_COLS;
			// first column
			if (col == 0) {
				if (row == 1) {
					tileArray[row][col] = new PrecursorTile(yeastVGL, row, col);
					innerPanel.add(tileArray[row][col]);
				} else {
					tileArray[row][col] = new UneditableTile(yeastVGL, row, col);
					innerPanel.add(tileArray[row][col]);
				}
			} else if (((col % 4) == 1) || ((col % 4) == 3)) {
				tileArray[row][col] = new ArrowTile(yeastVGL, row, col);
				innerPanel.add(tileArray[row][col]);
			} else if ((col % 4) == 2) {
				tileArray[row][col] = new EnzymeTile(yeastVGL, row, col);
				innerPanel.add(tileArray[row][col]);
			} else if ((col % 4) == 0) {
				tileArray[row][col] = new MoleculeTile(yeastVGL, row, col);
				innerPanel.add(tileArray[row][col]);
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

	public SavedPathwayDrawingState getState() {
		SavedTile[][] tiles = new SavedTile[NUM_ROWS][NUM_COLS];
		for (int row = 0; row < NUM_ROWS; row++) {
			for (int col = 0; col < NUM_COLS; col++) {
				tiles[row][col] = new SavedTile(tileArray[row][col]);
			}
		}
		return new SavedPathwayDrawingState(NUM_ROWS, NUM_COLS, tiles);
	}
	
	public void restoreSavedState(State state) {
		innerPanel.removeAll();
		SavedPathwayDrawingState spds = state.getSavedPathwayDrawingState();
		int numRows = spds.getNumRows();
		int numCols = spds.getNumCols();
		SavedTile[][] savedTiles = spds.getTiles();
		tileArray = new DrawingPanelTile[numRows][numCols];
		for (int row = 0; row < numRows; row++) {
			for (int col = 0; col < numCols; col++) {
				int type = savedTiles[row][col].type;
				int selection = savedTiles[row][col].selection;
				if (type == SavedTile.ARROW) {
					tileArray[row][col] = new ArrowTile(yeastVGL, row, col);
					tileArray[row][col].updateSelectedTile(selection);
					innerPanel.add(tileArray[row][col]);
				}
				if (type == SavedTile.ENZYME) {
					tileArray[row][col] = new EnzymeTile(yeastVGL, row, col);
					tileArray[row][col].updateSelectedTile(selection);
					innerPanel.add(tileArray[row][col]);
				}
				if (type == SavedTile.MOLECULE) {
					tileArray[row][col] = new MoleculeTile(yeastVGL, row, col);
					tileArray[row][col].updateSelectedTile(selection);
					innerPanel.add(tileArray[row][col]);
				}
				if (type == SavedTile.PRECURSOR) {
					tileArray[row][col] = new PrecursorTile(yeastVGL, row, col);
					innerPanel.add(tileArray[row][col]);
				}
				if (type == SavedTile.UNEDITABLE) {
					tileArray[row][col] = new UneditableTile(yeastVGL, row, col);
					innerPanel.add(tileArray[row][col]);
				}
			}
		}
		innerPanel.revalidate();
		innerPanel.repaint();
	}

}
