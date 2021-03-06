package PathwayPanel;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
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
			} else if ((col % 4) == 1) {
				tileArray[row][col] = new LineTile(yeastVGL, row, col);
				innerPanel.add(tileArray[row][col]);
			} else if ((col % 4) == 3) {
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
//		innerPanel.setMaximumSize(new Dimension(
//				(NUM_COLS * (DrawingPanelTile.TILE_WIDTH + CELL_SPACING)) + 100,
//				(NUM_ROWS * (DrawingPanelTile.TILE_HEIGHT + CELL_SPACING)) + 100));
		innerPanel.setBorder(BorderFactory.createTitledBorder("Enter Pathway Here"));
//		JScrollPane scroller = new JScrollPane(
//				innerPanel, 
//				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, 
//				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
//		scroller.setPreferredSize(new Dimension(
//				1000, (NUM_ROWS * (DrawingPanelTile.TILE_HEIGHT + CELL_SPACING) + 50)));
//		add(scroller);
		add(innerPanel);
	}

	/*
	 * "drawing assistance" - when you select certain tiles, this is the callback to 
	 * 	fix the neighbors in a hopefully helpful way
	 * 
	 * 1) when you select an enzyme, you get a straight arrow to the right
	 * 	this goes back to blank if you undo the enzyme
	 * 
	 * 2) if you choose a forked line, it puts a bent line above it
	 * 	this goes back to blank if you undo the bottom line
	 *  (can't do the reverse or you get an infinite loop)
	 *  
	 */
	public void updateNeighboringTiles(int row, int col) {
		DrawingPanelTile tile = tileArray[row][col];
		// (1)
		if ( (tile instanceof EnzymeTile) && (tileArray[row][col + 1] != null) ) {
			if (tile.getSelection() != -1) {
				((ConnectorTile)tileArray[row][col + 1]).setSelection(ConnectorTile.STRAIGHT);
			} else {
				((ConnectorTile)tileArray[row][col + 1]).setSelection(ConnectorTile.BLANK);
			}
		}

		// (2) 
		if ( (tile instanceof LineTile) && (row > 0) && (tileArray[row - 1][col] != null) ) {
			if (((LineTile)tile).getSelection() == ConnectorTile.FORKED) {
				((ConnectorTile)tileArray[row - 1][col]).setSelection(ConnectorTile.BENT);
			} 
		}
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
				if (type == SavedTile.LINE) {
					tileArray[row][col] = new LineTile(yeastVGL, row, col);
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

	// do this once you've selected or changed the CGs
	public void updateCGChoices() {
		for (int row = 0; row < tileArray.length; row++) {
			for (int col = 0; col < tileArray[0].length; col++) {
				DrawingPanelTile tile = tileArray[row][col];
				if (tile instanceof EnzymeTile) {
					((EnzymeTile)tile).updatePopupMenu();
				}
			}
		}
	}

	public Pathway convertToPathway() throws PathwayDrawingException {
		Enzyme[] enzymes = new Enzyme[yeastVGL.getPathway().getNumberOfEnzymes()];
		Molecule[] molecules = new Molecule[yeastVGL.getPathway().getNumberOfMolecules()];
		molecules[0] = new Molecule(0);	// precursor always 0
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
		//		System.out.println("found P at r:" + row + " col:" + col);
		explorePathwayStartingAt(enzymes, molecules, row, col, -1, 0);
		Pathway p = new Pathway(yeastVGL, enzymes, molecules);
		p.checkPathwayIntegrity();
		return p;
	}

	// recursive function to walk the drawn pathway
	//  and process along the way
	private void explorePathwayStartingAt(
			Enzyme[] enzymes,
			Molecule[] molecules,
			int row, 
			int col, 
			int lastEnzymeIndex, 
			int lastMoleculeIndex) throws PathwayDrawingException {
		int enzymeIndex = lastEnzymeIndex;
		int moleculeIndex = lastMoleculeIndex;

		StringBuffer b = new StringBuffer();
		b.append("Looking at r" + row + " c" + col);
		DrawingPanelTile tile = tileArray[row][col];
		if (tile instanceof MoleculeTile) {
			b.append(" molecule #" + tile.getSelection() + "\n");
			moleculeIndex = tile.getSelection();
			if (tile.getSelection() == -1) {
				throw new PathwayDrawingException("You have an arrow that doesn't connect to any molecule; "
						+ "you should delete it or connect it to a molecule");
			} else {
				if (molecules[moleculeIndex] == null) {
					b.append("\tcreating molecule " + moleculeIndex + "\n");
					b.append("\tsetting the product of enzyme " + enzymeIndex + " to molecule " + moleculeIndex);
					molecules[moleculeIndex] = new Molecule(moleculeIndex);
					enzymes[enzymeIndex].setProduct(molecules[moleculeIndex]);
				} else {
					throw new PathwayDrawingException("You used molecule " + moleculeIndex + " more than once; "
							+ "you should check your pathway carefully.");
				}
			}
		}
		if (tile instanceof EnzymeTile) {
			b.append(" enzyme #" + tile.getSelection());
			enzymeIndex = tile.getSelection();
			if (tile.getSelection() == -1) {
				throw new PathwayDrawingException("You have an arrow that doesn't connect to any enzyme; "
						+ "you should delete it or connect it to an enzyme");
			} else {
				if (enzymes[tile.getSelection()] == null) {
					b.append("\n\tcreating enzyme number " + tile.getSelection() + "\n");
					b.append("\tsetting it's substrate to " + moleculeIndex + "\n");
					b.append("\ttelling molecule " + moleculeIndex + " that one of it's next enzymes is " + tile.getSelection());
					Enzyme e = new Enzyme(tile.getSelection());
					e.setSubstrate(molecules[moleculeIndex]);
					enzymes[tile.getSelection()] = e;
					molecules[moleculeIndex].addNextEnzyme(e);
				} else {
					throw new PathwayDrawingException("You used enzyme " + 
							yeastVGL.getPathwayPanel().getCGNames()[tile.getSelection()] + " more than once; "
							+ "you should check your pathway carefully.");
				}
			}
		}
		//		System.out.println(b.toString());

		// look for arrow to right
		if (col == NUM_COLS) {
			throw new PathwayDrawingException("You have an arrow leading off the page; "
					+ "you should shorten your pathway.");
		}
		if (tileArray[row][col + 1] instanceof ConnectorTile) {
			if (tileArray[row][col + 1].getSelection() != ConnectorTile.BLANK) {
				// you can't have a bent arrow after a molecule or enzyme
				if (tileArray[row][col + 1].getSelection() == ConnectorTile.BENT) {
					throw new PathwayDrawingException("Enzyme " + 
							yeastVGL.getPathwayPanel().getCGNames()[enzymeIndex] + " is followed by a bent arrow; "
							+ "you should replace it with a straight or forked arrow.");
				}
				if (tileArray[row][col + 1].getSelection() == ConnectorTile.STRAIGHT) {
					// keep going straight on
					explorePathwayStartingAt(enzymes, molecules, row, col + 2, enzymeIndex, moleculeIndex);
				} 
				if (tileArray[row][col + 1].getSelection() == ConnectorTile.FORKED) {
					// hit a branch
					//  first, be sure they did it right
					if (row == 0) {
						// you can't branch up in the top row
						throw new PathwayDrawingException("You have an arrow branching off the page; "
								+ "you should move your pathway down.");
					}
					if (tileArray[row - 1][col + 1].getSelection() == ConnectorTile.BENT) {
						// keep going straight on
						explorePathwayStartingAt(enzymes, molecules, row, col + 2, enzymeIndex, moleculeIndex);
						// and take the branch
						explorePathwayStartingAt(enzymes, molecules, row - 1, col + 2, enzymeIndex, moleculeIndex);
					} else {
						throw new PathwayDrawingException("You have a forked arrow leading nowhere; "
								+ "you should connect it to a bent arrow.");
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
