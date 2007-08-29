import java.io.IOException;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;


public class StartingMaterialState extends ShowMoleculeState {

	public StartingMaterialState(OrgoGame orgoGame, 
			ProblemSet problemSet, 
			Controller controller) {
		super(orgoGame, problemSet, controller);
	}
	
	public void paint(Graphics g) {
		doCommmonPainting(g);
		
		int width = getWidth();
		int height = getHeight();
				
		g.setColor(0x000000);
		
		g.drawString("Starting Material", 0, 20, Graphics.TOP|Graphics.LEFT);
		
		g.drawImage(problemSet.getMoleculeImage(problemSet.getStartingMaterial()), 20, 40, 
					Graphics.TOP|Graphics.LEFT);
	}

	public void commandAction(Command c, Displayable arg1) {
		doCommonCommandActions(c);
		
	}
	
	protected void keyPressed(int keyCode) {
		if(getGameAction(keyCode) == RIGHT){
			controller.switchToProductState();
			return;
		}
		if (getGameAction(keyCode) == FIRE){
			controller.switchToEditAnswerState();
		}
	}

}
