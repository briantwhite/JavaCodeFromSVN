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
	
	int backgroundColor;

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

		g.setColor(backgroundColor);
		g.fillRect(0, 0, width, height);

		g.setColor(0x000000);
		g.fillRect(0, 0, width, 30);
		g.setColor(0xffffff);
		g.drawString("Completed " + problemSet.getNumSuccessfullyCompletedProblems()
				+ " of " + problemSet.getTotalNumberOfProblems(), 
				0, 0, Graphics.TOP|Graphics.LEFT);
		g.setColor(0xff0000);
		g.drawString("Time = " + controller.getElapsedTimeString(), 
				0, 15, 
				Graphics.TOP|Graphics.LEFT);

		Bond[] bonds = molecule.getBonds();
		g.setColor(DisplayColors.BOND);
		for (int i = 0; i < bonds.length; i++) {

			int deltaX = bonds[i].getAtom2().getX() - bonds[i].getAtom1().getX();
			int deltaY = bonds[i].getAtom2().getY() - bonds[i].getAtom1().getY();
			int shiftX = 0;
			int shiftY = 0;
			if ((deltaX > 0) && (deltaY > 0)) {
				shiftX = (problemSet.getScale().getMultiBondOffset() * 3)/4;
				shiftY = (problemSet.getScale().getMultiBondOffset() * 3)/4;
			} else if (deltaX > 0) {
				shiftY = problemSet.getScale().getMultiBondOffset();
			} else if (deltaY > 0) {
				shiftX = problemSet.getScale().getMultiBondOffset();
			}
			
			if (bonds[i].getBondOrder() == 2) {
								                                                                        
				g.drawLine(bonds[i].getAtom1().getX() + shiftX, 
						bonds[i].getAtom1().getY() + shiftY, 
						bonds[i].getAtom2().getX() + shiftX, 
						bonds[i].getAtom2().getY() + shiftY);
				
				g.drawLine(bonds[i].getAtom1().getX() - shiftX, 
						bonds[i].getAtom1().getY() - shiftY, 
						bonds[i].getAtom2().getX() - shiftX, 
						bonds[i].getAtom2().getY() - shiftY);
			} else {
				g.drawLine(bonds[i].getAtom1().getX(), 
						bonds[i].getAtom1().getY(), 
						bonds[i].getAtom2().getX(), 
						bonds[i].getAtom2().getY());
			}
			if (bonds[i].getBondOrder() == 3) {
				g.drawLine(bonds[i].getAtom1().getX() + (shiftX * 3)/2, 
						bonds[i].getAtom1().getY() + (shiftY * 3)/2, 
						bonds[i].getAtom2().getX() + (shiftX * 3)/2, 
						bonds[i].getAtom2().getY() + (shiftY * 3)/2);
				
				g.drawLine(bonds[i].getAtom1().getX() - (shiftX * 3)/2, 
						bonds[i].getAtom1().getY() - (shiftY * 3)/2, 
						bonds[i].getAtom2().getX() - (shiftX * 3)/2, 
						bonds[i].getAtom2().getY() - (shiftY * 3)/2);				
			}
		}



		Atom[] atoms = molecule.getAtoms();
		g.setFont(problemSet.getScale().getFont());
		for (int i = 0; i < atoms.length; i++) {
			String atomLabel = atoms[i].getType();
			if (!atomLabel.equals("C")) {

				if (atoms[i].getHatomCount() != 0) {
					atomLabel += "H";
					if (atoms[i].getHatomCount() > 1) {
						atomLabel += atoms[i].getHatomCount();
					} else {

					}
				}

				g.setColor(backgroundColor);
				int atomLabelWidth = 
					problemSet.getScale().getFont().stringWidth(
							atomLabel);
				int atomLabelHeight =
					problemSet.getScale().getFont().getHeight();
				g.fillArc(atoms[i].getX() - (3 * atomLabelWidth)/4, 
						atoms[i].getY() - (3 * atomLabelHeight)/4, 
						(atomLabelWidth * 4)/3, 
						(atomLabelHeight * 4)/3, 
						0, 
						360);

				g.setColor(DisplayColors.OTHER_ATOM);

				if (atoms[i].getType().equals("O")) {
					g.setColor(DisplayColors.OXYGEN);
				}
				if (atoms[i].getType().equals("N")) {
					g.setColor(DisplayColors.NITROGEN);
				}
				if (atoms[i].getType().equals("S")) {
					g.setColor(DisplayColors.SULFUR);
				} 
				g.drawString(atomLabel, 
						atoms[i].getX() 
						+ problemSet.getScale().getFont().stringWidth(
								atomLabel)/2, 
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


