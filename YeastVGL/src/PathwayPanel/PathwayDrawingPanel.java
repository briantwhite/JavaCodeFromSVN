package PathwayPanel;

import java.awt.GridLayout;

import javax.swing.JPanel;

import YeastVGL.YeastVGL;

public class PathwayDrawingPanel extends JPanel {
	
	private YeastVGL yeastVGL;
	
	public PathwayDrawingPanel(YeastVGL yeastVGL) {
		this.yeastVGL = yeastVGL;
		
		setLayout(new GridLayout(5,5));
	}

}
