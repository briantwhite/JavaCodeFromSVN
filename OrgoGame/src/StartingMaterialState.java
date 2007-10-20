import java.io.IOException;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;


public class StartingMaterialState extends ShowMoleculeState {

	public StartingMaterialState(Controller controller, ProblemSet problemSet) {
		super(controller, problemSet);
	}
	
	public void paint(Graphics g) {
		molecule = problemSet.getMolecule(problemSet.getStartingMaterial());
		backgroundColor = DisplayColors.SM_BACKGROUND;
		doCommmonPainting(g);
		g.setColor(0x000000);
		if (!(problemSet.getScale() instanceof SmallScale)) {
			g.setColor(0x000000);
			g.drawString("Starting Material", 0, 25, Graphics.TOP|Graphics.LEFT);		
		}
	}
	
}
