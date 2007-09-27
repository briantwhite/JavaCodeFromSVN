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
		doCommmonPainting(g);
						
		g.setColor(0x000000);
		
		g.drawString("Product", 0, 40, Graphics.TOP|Graphics.LEFT);
		
		g.drawImage(problemSet.getMoleculeImage(problemSet.getProduct()), 20, 60, 
					Graphics.TOP|Graphics.LEFT);


	}
}
