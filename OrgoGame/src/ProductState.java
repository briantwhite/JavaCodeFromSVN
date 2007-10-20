import java.io.IOException;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;


public class ProductState extends ShowMoleculeState {

	public ProductState(Controller controller, ProblemSet problemSet) {
		super(controller, problemSet);
	}
	
	public void paint(Graphics g) {
		molecule = problemSet.getMolecule(problemSet.getProduct());
		backgroundColor = DisplayColors.PR_BACKGROUND;
		doCommmonPainting(g);
		if (!(problemSet.getScale() instanceof SmallScale)) {
			g.setColor(0x000000);
			g.drawString("Product", 0, 30, Graphics.TOP|Graphics.LEFT);		
		}
	}
}
