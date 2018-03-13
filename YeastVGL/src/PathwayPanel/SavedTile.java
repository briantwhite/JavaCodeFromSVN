package PathwayPanel;

public class SavedTile {
	
	public String type;
	public int selection;
	
	public SavedTile(DrawingPanelTile tile) {
		if (tile instanceof ArrowTile) {
			type = "A";
		} 
		if (tile instanceof EnzymeTile) {
			type = "E";
		} 
		if (tile instanceof MoleculeTile) {
			type = "M";
		} 
		if (tile instanceof PrecursorTile) {
			type = "P";
		} 
		if (tile instanceof UneditableTile) {
			type = "U";
		} 
		selection = tile.getSelection();
	}
}
