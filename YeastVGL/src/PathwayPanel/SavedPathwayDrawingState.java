package PathwayPanel;

public class SavedPathwayDrawingState {
	
	private int numRows;
	private int numCols;
	private SavedTile[][] tiles;
	
	public SavedPathwayDrawingState(int numRows, int numCols, SavedTile[][] tiles) {
		this.numRows = numRows;
		this.numCols = numCols;
		this.tiles = tiles;
	}
	
	public int getNumRows() {
		return numRows;
	}
	
	public int getNumCols() {
		return numCols;
	}
	
	public SavedTile[][] getTiles() {
		return tiles;
	}

}
