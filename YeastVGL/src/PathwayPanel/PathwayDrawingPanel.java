package PathwayPanel;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import Biochemistry.Enzyme;
import Biochemistry.Molecule;
import Biochemistry.Pathway;
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

	public Pathway convertToPathway() {
		ArrayList<Enzyme> enzymeList = new ArrayList<Enzyme>();
		ArrayList<Molecule> moleculeList = new ArrayList<Molecule>();
		int enzymeIndex = 0;
		int moleculeIndex = 0;
		// follow the pathway starting at the precursor
		//  first, find the precursor; then do a recursive exploration
		int col = 0;
		int row = 0;
		boolean foundPrecursor = false;
		for (col = 0; col < NUM_COLS; col++) {
			for (row = 0; row < NUM_ROWS; row++) {
				if (tileArray[row][col] instanceof PrecursorTile) {
					foundPrecursor = true;
					break;
				}
			}
			if (foundPrecursor) {
				break;
			}
		}
		if (!foundPrecursor) {
			System.out.println("No precursor in pathway; aborting!");
			return null;
		}
		System.out.println("found P at r:" + row + " col:" + col);
		try {
			explorePathwayStartingAt(row, col, -1, 0);
		} catch (PathwayDrawingException e) {
			System.out.println(e.getMessage());;
		}
		return null;
	}

	// recursive function to walk the drawn pathway
	//  and process along the way
	private void explorePathwayStartingAt(int row, int col, int lastEnzymeIndex, int lastMoleculeIndex) throws PathwayDrawingException {
		int enzymeIndex = lastEnzymeIndex;
		int moleculeIndex = lastMoleculeIndex;
		
		StringBuffer b = new StringBuffer();
		b.append("Looking at r" + row + " c" + col);
		DrawingPanelTile tile = tileArray[row][col];
		if (tile instanceof MoleculeTile) {
			b.append(" molecule #" + tile.getSelection());
			moleculeIndex = tile.getSelection();
			b.append("\n\tsetting the product of enzyme " + enzymeIndex + " to molecule " + moleculeIndex);
		}
		if (tile instanceof EnzymeTile) {
			b.append(" enzyme #" + tile.getSelection());
			enzymeIndex = tile.getSelection();
			b.append("\n\tcreating enzyme number " + tile.getSelection() + "\n");
			b.append("\tsetting it's substrate to " + moleculeIndex);
		}
		System.out.println(b.toString());
		// look for arrow to right
		if (col == NUM_COLS) {
			throw new PathwayDrawingException("You have an arrow leading off the page; you should shorten your pathway.");
		}
		if (tileArray[row][col + 1] instanceof ArrowTile) {
			if (tileArray[row][col + 1].getSelection() != ArrowTile.BLANK_ARROW) {
				if (tileArray[row][col + 1].getSelection() == ArrowTile.STRAIGHT_ARROW) {
					// keep going straight on
					explorePathwayStartingAt(row, col + 2, enzymeIndex, moleculeIndex);
				} 
				if (tileArray[row][col + 1].getSelection() == ArrowTile.FORKED_ARROW) {
					// hit a branch
					//  first, be sure they did it right
					if (row == 0) {
						// you can't branch up in the top row
						throw new PathwayDrawingException("You have an arrow branching off the page; you should move your pathway down.");
					}
					if (tileArray[row - 1][col + 1].getSelection() == ArrowTile.BENT_ARROW) {
						// keep going straight on
						explorePathwayStartingAt(row, col + 2, enzymeIndex, moleculeIndex);
						// and take the branch
						explorePathwayStartingAt(row - 1, col + 2, enzymeIndex, moleculeIndex);
					} else {
						throw new PathwayDrawingException("You have a forked arrow leading nowhere; you should connect it to a bent arrow.");
					}
				}
			} else {
				return;
			}
		} else {
			return;
		}

	}

}
