package PathwayPanel;

public class SavedTile {
	
	public int type;
	public static final int ARROW = 0;
	public static final int ENZYME = 1;
	public static final int MOLECULE = 2;
	public static final int PRECURSOR = 3;
	public static final int UNEDITABLE = 4;
	public int selection;
	
	public SavedTile(DrawingPanelTile tile) {
		if (tile instanceof ArrowTile) {
			type = ARROW;
		} 
		if (tile instanceof EnzymeTile) {
			type = ENZYME;
		} 
		if (tile instanceof MoleculeTile) {
			type = MOLECULE;
		} 
		if (tile instanceof PrecursorTile) {
			type = PRECURSOR;
		} 
		if (tile instanceof UneditableTile) {
			type = UNEDITABLE;
		} 
		selection = tile.getSelection();
	}
}
