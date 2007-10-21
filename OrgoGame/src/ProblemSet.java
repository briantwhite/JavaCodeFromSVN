import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Random;
import java.util.Timer;
import java.util.Vector;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Image;
import javax.microedition.midlet.MIDletStateChangeException;


public class ProblemSet {

	Scale scale;

	Random randomizer;

	private OrgoGame orgoGame;

	private boolean screenTooSmall;

	private int numMolecules;
	private String[] reactions;
	private int numReactions;

	private Molecule[] molecules;
	private int startingMaterial;
	private int product;

	private ReactionList[][] correctAnswerArray;

	private Vector studentsAnswer;

	private boolean[][] successfullyCompletedProblemArray;
	private int numSuccessfullyCompletedProblems = 0;
	private int totalNumberOfProblems;


	public ProblemSet(OrgoGame orgoGame) {
		this.orgoGame = orgoGame;

		//read in the problem file
		String problemFileString = "";
		//determine the size of the file
		int size = 0;
		InputStream problemFileStream = 
			getClass().getResourceAsStream("ProblemFiles/Problem.pml");
		byte b;
		try {
			b = (byte)problemFileStream.read();

			while (b != -1) {
				size ++;
				b = (byte)problemFileStream.read();
			}
			problemFileStream.close();
			//create byte array to hold the file contents
			byte buf[] = new byte[size];

			//read the contents into the byte array
			problemFileStream = 
				getClass().getResourceAsStream("ProblemFiles/Problem.pml");
			problemFileStream.read(buf);
			problemFileStream.close();

			// convert the array into string
			problemFileString = new String(buf);

		} catch (IOException e) {
			e.printStackTrace();
		}

		// convert to String[] with each line is an entry
		String[] problemFileLines = 
			Utilities.parseToStringArray(problemFileString, "\r");

		// count the relevant parameters
		numMolecules = 0;
		numReactions = 0;
		for (int i = 0; i < problemFileLines.length; i++) {
			String line = problemFileLines[i];
			if (line.startsWith("<molecule ")) {
				numMolecules++;
			}
			if (line.startsWith("<reaction ")) {
				numReactions++;
			}
		}

		//read in the reactions and correct answers
		reactions = new String[numReactions];
		correctAnswerArray = new ReactionList[numMolecules][numMolecules];
		for (int i = 0; i < problemFileLines.length; i++) {
			String line = problemFileLines[i];
			if (line.startsWith("<reaction ")) {
				String[] parts = 
					Utilities.parseToStringArray(line, " ");
				reactions[
				          Integer.parseInt(
				        		  Utilities.extractFromWithinQuotes(parts[1]))] =
				        			  Utilities.extractFromWithinQuotes(parts[2]);
			}
			if (line.startsWith("<answer ")) {
				String[] parts = 
					Utilities.parseToStringArray(line, " ");
				correctAnswerArray[
				                   Integer.parseInt(
				                		   Utilities.extractFromWithinQuotes(parts[1]))]
				                		   [
				                		    Integer.parseInt(
				                		    		Utilities.extractFromWithinQuotes(
				                		    				parts[2]))]
				                		    				= new ReactionList(
				                		    						Utilities.extractFromWithinQuotes(
				                		    								parts[3]));
			}
		}
		//can't convert mol0 to mol0
		for (int i = 0; i < numMolecules; i++) {
			correctAnswerArray[i][i] = null;
		}

		// read in the molecules; one-by-one
		molecules = new Molecule[numMolecules];
		int moleculeCounter = 0;

		int numAtoms = 0;
		int numBonds = 0;
		Vector atomVector = null;
		Vector bondVector = null;
		for (int i = 0; i < problemFileLines.length; i++) {
			String line = problemFileLines[i];

			// see if starting a new molecule
			//  if so, clear everything out
			if (line.startsWith("<molecule ")) {
				numAtoms = 0;
				numBonds = 0;
				atomVector = new Vector();
				bondVector = new Vector();
			}

			// if it's an atom, make one and add to vector
			if (line.startsWith("<atom ")) {
				String type = "";
				int x = 0;
				int y = 0;
				int hydrogenCount = 0;
				String id = "";
				String[] parts = Utilities.parseToStringArray(line, " ");
				for (int j = 0; j < parts.length; j++) {
					String part = parts[j];
					if (part.startsWith("elementType")) {
						type = Utilities.extractFromWithinQuotes(part);
					}
					if (part.startsWith("id")) {
						id = Utilities.extractFromWithinQuotes(part);
					}
					if (part.startsWith("hydrogenCount")) {
						hydrogenCount = 
							Integer.parseInt(Utilities.extractFromWithinQuotes(
									part));
					}
					if (part.startsWith("x2")) {
						x = 
							Utilities.parseStringToBigInt(
									Utilities.extractFromWithinQuotes(
											part), 3);
					}
					if (part.startsWith("y2")) {
						y = 
							Utilities.parseStringToBigInt(
									Utilities.extractFromWithinQuotes(
											part), 3);
					}
				}
				atomVector.addElement(new Atom(type, x, y, hydrogenCount, id));
			}

			// if it's a bond, add it to the vector
			if (line.startsWith("<bond ")) {
				Atom a1 = null;
				Atom a2 = null;
				int bondOrder = 0;
				String[] parts = Utilities.parseToStringArray(line, " ");
				for (int j = 0; j < parts.length; j++) {
					String part = parts[j];
					if (part.startsWith("atomRefs")) {
						String[] atomIdsInBond =
							Utilities.parseToStringArray(
									Utilities.extractFromWithinQuotes(part), " ");
						a1 = findAtomWithThisId(atomVector, atomIdsInBond[0]);
						a2 = findAtomWithThisId(atomVector, atomIdsInBond[1]);
					}
					if (part.startsWith("order")) {
						bondOrder =
							Integer.parseInt(Utilities.extractFromWithinQuotes(
									part));
					}
				}
				bondVector.addElement(new Bond(a1, a2, bondOrder));
			}

			// if it's the end of a molecule, save the molecule
			if (line.startsWith("</molecule")) {

				Atom[] atoms = new Atom[atomVector.size()];
				for (int j = 0; j < atoms.length; j++) {
					atoms[j] = (Atom)atomVector.elementAt(j);
				}

				Bond[] bonds = new Bond[bondVector.size()];
				for (int j = 0; j < bonds.length; j++) {
					bonds[j] = (Bond)bondVector.elementAt(j);
				}

				molecules[moleculeCounter] =
					new Molecule(atoms, bonds);
				moleculeCounter++;
			}
		}


		//start with largest scale
		// if that fails, the try smaller ones
		// if none fit, quit
		Molecule[] scaledMolecules = new Molecule[numMolecules];
		screenTooSmall = false;
		scale = new LargeScale();
		scaledMolecules = scaleMolecules(molecules);
		if (scaledMolecules == null) {
			scale = new MediumScale();
			scaledMolecules = scaleMolecules(molecules);
			if (scaledMolecules == null) {
				scale = new SmallScale();
				scaledMolecules = scaleMolecules(molecules);
				if (scaledMolecules == null) {
					screenTooSmall = true;
				}
			}
		}
		molecules = scaledMolecules;
		scaledMolecules = null;

		totalNumberOfProblems = (numMolecules * numMolecules) - numMolecules;

		//list of reactions in current solution attempt
		studentsAnswer = null;

		randomizer = new Random();

		//set up array of successfully-completed problems
		successfullyCompletedProblemArray = new boolean[numMolecules][numMolecules];
		for (int i = 0; i < numMolecules; i++) {
			for (int j = 0; j < numMolecules; j++) {
				if (i == j) {
					successfullyCompletedProblemArray[i][j] = true;
				} else {
					successfullyCompletedProblemArray[i][j] = false;
				}
			}
		}

		newProblem();
	}

	// returns null if molecules will not fit in useable screen area
	private Molecule[] scaleMolecules(Molecule[] unscaledMolecules) {
		// figure useable screen coordinates
		int minX = 0;
		int minY = 0;
		int maxX = orgoGame.screenSizeMeasurer.getWidth();
		int maxY = orgoGame.screenSizeMeasurer.getHeight();
		if (scale instanceof SmallScale) {
			minY = 25;
		} else {
			minY = 36;
		}
		maxY = maxY - 12;
		scale.setXOffset((minX + maxX)/2);
		scale.setYOffset((minY + maxY)/2);

		MinMaxTallier mmt = MinMaxTallier.getInstance();
		mmt.reset();

		//scale the measurements
		// use a standard bond length to scale each molecule
		// so all bonds the same length
		Molecule[] scaledMolecules = new Molecule[numMolecules];
		for (int i = 0; i < numMolecules; i++) {
			Molecule molec = unscaledMolecules[i];

			molec.normalizeXandY();
			int scaleFactor = molec.getOneBondLength()/scale.getBondLength();

			Atom[] unscaledAtoms = molec.getAtoms();
			Atom[] scaledAtoms = new Atom[unscaledAtoms.length];
			for (int j = 0; j < unscaledAtoms.length; j++) {
				scaledAtoms[j] = unscaledAtoms[j].scale(scaleFactor, 
						scale.getXOffset(), scale.getYOffset(),
						mmt);
			}

			Bond[] unscaledBonds = molec.getBonds();
			Bond[] scaledBonds = new Bond[unscaledBonds.length];
			for (int j = 0; j < unscaledBonds.length; j++) {
				scaledBonds[j] = new Bond(
						findAtomWithThisId(scaledAtoms, 
								unscaledBonds[j].getAtom1().getId()),
								findAtomWithThisId(scaledAtoms, 
										unscaledBonds[j].getAtom2().getId()),
										unscaledBonds[j].getBondOrder());
			}
			scaledMolecules[i] = new Molecule(scaledAtoms, scaledBonds);
		}
		if (mmt.insideUseableScreenArea(minX, minY, maxX, maxY)) {
			return scaledMolecules;
		} else {
			return null;
		}
	}

	private Atom findAtomWithThisId(Vector v, String id) {
		for (int i = 0; i < v.size(); i++) {
			Atom a = (Atom)v.elementAt(i);
			if (a.getId().equals(id)) {
				return a;
			}
		}
		return null;
	}

	private Atom findAtomWithThisId(Atom[] atoms, String id) {
		for (int i = 0; i < atoms.length; i++) {
			Atom a = atoms[i];
			if (a.getId().equals(id)) {
				return a;
			}
		}
		return null;
	}

	public void newProblem() {

		studentsAnswer = new Vector();
		startingMaterial = 0;
		product = 0;
		while (isSuccessfullyCompleted(startingMaterial, product)) {
			startingMaterial = getRandomInt(0,numMolecules);
			product = getRandomInt(0,numMolecules);
		}
	}

	public boolean screenTooSmall() {
		return screenTooSmall;
	}

	public int getNumMolecules() {
		return numMolecules;
	}

	public String getReactionDescription(int i) {
		if (i < reactions.length) {
			return reactions[i];
		} else {
			return "";
		}
	}

	public String[] getAllReactions() {
		return reactions;
	}

	public int getNumReactions() {
		return numReactions;
	}

	public Molecule getMolecule(int i) {
		return molecules[i];
	}

	public int getStartingMaterial() {
		return startingMaterial;
	}

	public int getProduct() {
		return product;
	}

	public Scale getScale() {
		return scale;
	}

	public ReactionList getCorrectAnswer(int startingMaterial, int product) {
		if ((startingMaterial < numMolecules) &&
				(product < numMolecules)) {
			return correctAnswerArray[startingMaterial][product];
		} else {
			return null;
		}
	}

	public int getSizeOfStudentsAnswer() {
		return studentsAnswer.size();
	}

	public int[] getStudentsAnswer() {
		if (studentsAnswer == null){
			return null;
		}

		int[] array = new int[studentsAnswer.size()];
		for (int i = 0; i < studentsAnswer.size(); i++){
			array[i] = ((Integer)studentsAnswer.elementAt(i)).intValue();
		}
		return array;
	}

	//adds to end
	private void addReactionToStudentsAnswer(int reaction) {
		studentsAnswer.addElement(new Integer(reaction));
	}

	//adds before element at location
	public void addReactionToStudentsAnswer(int reaction, int location){
		if (location == -1) {
			addReactionToStudentsAnswer(reaction);
		} else {
			studentsAnswer.insertElementAt(new Integer(reaction), location);
		}
	}

	public void deleteReactionFromStudentsAnswer(int location) {
		studentsAnswer.removeElementAt(location);
	}

	public boolean isCurrentListCorrect() {
		int[] correctList = getCorrectAnswer(startingMaterial, product).getList();
		int[] trialAnswer = getStudentsAnswer();
		if (correctList.length != trialAnswer.length) {
			return false;
		}

		boolean isCorrect = true;
		for (int i = 0; i < correctList.length; i++) {
			if (correctList[i] != trialAnswer[i]){
				isCorrect = false;
			}
		}

		return isCorrect;
	}

	public void setSuccessfullyCompleted(int startingMaterial, int product) {
		if ((startingMaterial < numMolecules) 
				&& (product < numMolecules)) {
			successfullyCompletedProblemArray[startingMaterial][product] = true;
			numSuccessfullyCompletedProblems++;
		}
	}

	public boolean isSuccessfullyCompleted(int startingMaterial, int product) {
		return successfullyCompletedProblemArray[startingMaterial][product];
	}

	public int getNumSuccessfullyCompletedProblems() {
		return numSuccessfullyCompletedProblems;
	}

	public int getTotalNumberOfProblems() {
		return totalNumberOfProblems;
	}

	public int getRandomInt(int min, int max){
		int r = Math.abs(randomizer.nextInt());
		return (r % (max - min)) + min;
	}
}
