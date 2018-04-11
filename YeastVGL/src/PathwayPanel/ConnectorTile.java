package PathwayPanel;

import YeastVGL.YeastVGL;

public abstract class ConnectorTile extends DrawingPanelTile {
	
	public static final int BLANK = 0;
	public static final int STRAIGHT = 1;
	public static final int FORKED = 2;
	public static final int BENT = 3;

	public ConnectorTile(YeastVGL yeastVGL, int row, int col) {
		super(yeastVGL, row, col);
	}

}
