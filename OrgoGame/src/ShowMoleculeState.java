import java.io.IOException;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.midlet.MIDletStateChangeException;


public abstract class ShowMoleculeState extends Canvas {

	Controller controller;
	ProblemSet problemSet;

	Molecule molecule;

	protected Command quit;
	protected Command newProblem;

	public ShowMoleculeState(Controller controller, ProblemSet problemSet) {
		this.problemSet = problemSet;
		this.controller = controller;
		quit = new Command(OrgoGame.QUIT, Command.EXIT, 1);
		newProblem = new Command(OrgoGame.NEW_PROBLEM, Command.SCREEN, 2);
		addCommand(quit);
		addCommand(newProblem);
		setCommandListener(controller);
	}

	public void doCommmonPainting(Graphics g) {
		int width = getWidth();
		int height = getHeight();

		g.setColor(0x808080);
		g.fillRect(0, 0, width, height);

		g.setColor(0x000000);
		g.fillRect(0, 0, width, 30);
		g.setColor(0xffffff);
		g.drawString("Completed " + problemSet.getNumSuccessfullyCompletedProblems()
				+ " of " + problemSet.getTotalNumberOfProblems() 
				+ " problems.", 0, 0, Graphics.TOP|Graphics.LEFT);
		g.setColor(0xff0000);
		g.drawString("Time Elapsed = " + controller.getElapsedTimeString(), 
				0, 15, 
				Graphics.TOP|Graphics.LEFT);

		Bond[] bonds = molecule.getBonds();
		g.setColor(0x000000);
		for (int i = 0; i < bonds.length; i++) {
			g.drawLine(bonds[i].getAtom1().getX(), 
					bonds[i].getAtom1().getY(), 
					bonds[i].getAtom2().getX(), 
					bonds[i].getAtom2().getY());
		}



		Atom[] atoms = molecule.getAtoms();
		g.setColor(0xff0000);
		g.setFont(problemSet.getScale().getFont());
		for (int i = 0; i < atoms.length; i++) {
			String atomLabel = atoms[i].getType();
			if (!atomLabel.equals("C")) {

				if (atoms[i].getHatomCount() != 0) {
					atomLabel += "H";
					if (atoms[i].getHatomCount() > 1) {
						atomLabel += atoms[i].getHatomCount();
					}
				}

				g.setColor(0x808080);
				int atomLabelWidth = 
					problemSet.getScale().getFont().stringWidth(
							atomLabel);
				int atomLabelHeight =
					problemSet.getScale().getFont().getHeight();
				g.fillArc(atoms[i].getX() - (3 * atomLabelWidth)/4, 
						atoms[i].getY() - (3 * atomLabelHeight)/4, 
						(atomLabelWidth * 3)/2, 
						(atomLabelHeight * 3)/2, 
						0, 
						360);

				g.setColor(0xff00ff);

				if (atoms[i].getType().equals("O")) {
					g.setColor(0xff0000);
				}
				if (atoms[i].getType().equals("N")) {
					g.setColor(0x0000ff);
				}
				if (atoms[i].getType().equals("S")) {
					g.setColor(0xffff00);
				} 
				g.drawString(atomLabel, 
						atoms[i].getX() 
						+ problemSet.getScale().getFont().stringWidth(
								atoms[i].getType())/2, 
								atoms[i].getY() 
								+ problemSet.getScale().getFont().getHeight()/2, 
								Graphics.BOTTOM|Graphics.RIGHT);
			}
		}
	}


	protected final void keyPressed(int keyCode) {
		controller.respondToKeyPress(getGameAction(keyCode), this);
	}

}


