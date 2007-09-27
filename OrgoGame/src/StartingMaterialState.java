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
		doCommmonPainting(g);
		
		int width = getWidth();
		int height = getHeight();
				
		g.setColor(0x000000);
		
		g.drawString("Starting Material", 0, 40, Graphics.TOP|Graphics.LEFT);
		
		g.drawImage(problemSet.getMoleculeImage(problemSet.getStartingMaterial()), 20, 60, 
					Graphics.TOP|Graphics.LEFT);
	}
	
}
