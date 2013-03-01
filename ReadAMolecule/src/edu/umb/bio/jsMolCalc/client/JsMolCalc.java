package edu.umb.bio.jsMolCalc.client;

import java.util.ArrayList;
import java.util.Arrays;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class JsMolCalc implements EntryPoint {

	private TextSubmissionButtonProcessor formulaProcessor;
	private ChoiceSubmissionButtonProcessor hBondProcessor;
	private ChoiceSubmissionButtonProcessor iBondProcessor;
	private ChoiceSubmissionButtonProcessor vdwProcessor;
	private ChoiceSubmissionButtonProcessor phobicProcessor;
	
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {

		final String[] YES_NO_CHOICES = {"?", "No", "Yes"};
		final String[] PHOBIC_CHOICES = {"?", "Hydrophobic", "Intermediate", "Hydrophilic"};

		TextBox formulaBox = new TextBox();
		Button formulaButton = new Button("Submit Answer");
		HTML formulaResult = new HTML();
		formulaProcessor = new TextSubmissionButtonProcessor(formulaBox, formulaButton, formulaResult);
		formulaButton.addClickHandler(formulaProcessor);
		RootPanel.get("formulaField").add(formulaBox);
		RootPanel.get("formulaButton").add(formulaButton);
		RootPanel.get("formulaResult").add(formulaResult);

		ListBox hBondChoices = new ListBox();
		setOptions(hBondChoices, YES_NO_CHOICES);
		Button hBondButton = new Button("Submit Answer");
		HTML hBondResult = new HTML();
		hBondProcessor = new ChoiceSubmissionButtonProcessor(hBondChoices, hBondButton, hBondResult);
		hBondButton.addClickHandler(hBondProcessor);
		RootPanel.get("hBonds").add(hBondChoices);
		RootPanel.get("hBondButton").add(hBondButton);
		RootPanel.get("hBondResult").add(hBondResult);

		ListBox iBondChoices = new ListBox();
		setOptions(iBondChoices, YES_NO_CHOICES);
		Button iBondButton = new Button("Submit Answer");
		HTML iBondResult = new HTML();
		iBondProcessor = new ChoiceSubmissionButtonProcessor(iBondChoices, iBondButton, iBondResult);
		iBondButton.addClickHandler(iBondProcessor);
		RootPanel.get("iBonds").add(iBondChoices);
		RootPanel.get("iBondButton").add(iBondButton);
		RootPanel.get("iBondResult").add(iBondResult);

		ListBox vdwBondChoices = new ListBox();
		setOptions(vdwBondChoices, YES_NO_CHOICES);
		Button vdwBondButton = new Button("Submit Answer");
		HTML vdwBondResult = new HTML();
		vdwProcessor = new ChoiceSubmissionButtonProcessor(vdwBondChoices, vdwBondButton, vdwBondResult);
		vdwBondButton.addClickHandler(vdwProcessor);
		RootPanel.get("vdwBonds").add(vdwBondChoices);
		RootPanel.get("vdwBondButton").add(vdwBondButton);
		RootPanel.get("vdwBondResult").add(vdwBondResult);

		ListBox phobicChoices = new ListBox();
		setOptions(phobicChoices, PHOBIC_CHOICES);
		Button phobicButton = new Button("Submit Answer");
		HTML phobicResult = new HTML();
		phobicProcessor = new ChoiceSubmissionButtonProcessor(phobicChoices, phobicButton, phobicResult);
		phobicButton.addClickHandler(phobicProcessor);
		RootPanel.get("phobic").add(phobicChoices);
		RootPanel.get("phobicButton").add(phobicButton);
		RootPanel.get("phobicResult").add(phobicResult);

		final TextArea molBox = new TextArea();
		molBox.setCharacterWidth(50);
		molBox.setVisibleLines(10);
		RootPanel.get("molInput").add(molBox);

		final HTML phobicityAnswer = new HTML();
		RootPanel.get("phobicityAnswer").add(phobicityAnswer);
		final HTML hBondAnswer = new HTML();
		RootPanel.get("hBondAnswer").add(hBondAnswer);
		final HTML iBondAnswer = new HTML();
		RootPanel.get("iBondAnswer").add(iBondAnswer);

		Button computeButton = new Button("Compute");
		RootPanel.get("computeButton").add(computeButton);
		class ComputeButtonHandler implements ClickHandler {
			public void onClick(ClickEvent event) {
				setJMEmol(molBox.getText());
				MoleculeInfo result = analyzeMolecule(getJMEmol(), getJMESmiles(), getJMEjme());
				if (result.phobicity > 1.0f) {
					phobicityAnswer.setHTML("<html><body>It is hydrophobic</body></html>");
				} else if (result.phobicity < -1.0f) {
					phobicityAnswer.setHTML("<html><body>It is hydrophilic</body></html>");
				} else {
					phobicityAnswer.setHTML("<html><body>It is intermediate</body></html>");
				}

				if (result.canMakeHBonds) {
					hBondAnswer.setHTML("<html><body>Yes</body></html>");
				} else {
					hBondAnswer.setHTML("<html><body>No</body></html>");
				}
				
				if (result.canMakeIonicBonds) {
					iBondAnswer.setHTML("<html><body>Yes</body></html>");
				} else {
					iBondAnswer.setHTML("<html><body>No</body></html>");
				}
				
			}
		}
		ComputeButtonHandler cbHandler = new ComputeButtonHandler();
		computeButton.addClickHandler(cbHandler);

		try {
			new RequestBuilder(RequestBuilder.GET, "dopamine.mol").sendRequest("", new RequestCallback() {
				public void onResponseReceived(Request req, Response resp) {
					String molString = resp.getText();
					setJMEmol(molString);
					MoleculeInfo result = analyzeMolecule(getJMEmol(), getJMESmiles(), getJMEjme());

					// set correct answers
					formulaProcessor.setCorrectAnswer(result.formula);
					
					if (result.canMakeHBonds) {
						hBondProcessor.setCorrectAnswer(2);
					} else {
						hBondProcessor.setCorrectAnswer(1);
					}
					
					if (result.canMakeIonicBonds) {
						iBondProcessor.setCorrectAnswer(2);
					} else {
						iBondProcessor.setCorrectAnswer(1);
					}
					
					// vdw is always possible
					vdwProcessor.setCorrectAnswer(2);
					
					if (result.phobicity > 1.0f) {
						phobicProcessor.setCorrectAnswer(1);
					} else if (result.phobicity < -1.0f) {
						phobicProcessor.setCorrectAnswer(3);
					} else {
						phobicProcessor.setCorrectAnswer(2);
					}
				}
				public void onError(Request res, Throwable throwable) {
					// handle errors
				}
			});
		} catch (RequestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

	private void setOptions(ListBox lb, String[]options) {
		for (int i = 0; i < options.length; i++) {
			lb.addItem(options[i]);
		}
	}

	private native void setJMEmol(String mol) /*-{
		var JME = $doc.getElementById('JME');
		JME.readMolFile(mol);
	}-*/;

	private native String getJMESmiles() /*-{
		var JME = $doc.getElementById('JME');
		return JME.smiles();
	}-*/;

	private native String getJMEjme() /*-{
		var JME = $doc.getElementById('JME');
		return JME.jmeFile();
	}-*/;

	private native String getJMEmol() /*-{
		var JME = $doc.getElementById('JME');
		return JME.molFile();
	}-*/;


	MoleculeInfo analyzeMolecule(String molString, String smileString,
			String jmeString) {

		if (molString.equals("") || smileString.equals("") || jmeString.equals("")) {
			return null;
		}

		StringBuffer atomDataLines = new StringBuffer(); //for output
		String formulaString = new String();
		String errorString = new String();

		ArrayList atomList = new ArrayList();
		atomList.add(new Atom()); // make atom[0] a blank

		// get the molecule data from the molstring
		String[] molStringLines = molString.split("\n");

		//        for (int i = 0; (i < molStringLines.length); i++) {
		//            System.out.println(i + ":" + molStringLines[i]);
		//        }

		//get number of bonds & number of atoms
		String[] line3Parts = molStringLines[3].split("[ ]+");
		int numAtoms = Integer.parseInt(line3Parts[1]);
		int numBonds = Integer.parseInt(line3Parts[2]);

		//read the atom lines & create appropriate atoms
		for (int i = 1; i < (numAtoms + 1); i++) {
			String[] atomLineParts = molStringLines[i + 3].split("[ ]+");
			String element = new String(atomLineParts[4]);
			Atom atom = new Atom();
			atom.setElement(element);
			atomList.add(atom);
		}

		//read the charge lines & set atom charge
		for (int i = 0; i < molStringLines.length; i++) {
			if (molStringLines[i].indexOf("CHG") != -1) {
				String[] chargeLineParts = molStringLines[i].split("[ ]+");
				int atomNumber = Integer.parseInt(chargeLineParts[3]);
				int charge = Integer.parseInt(chargeLineParts[4]);
				Atom atom = (Atom) atomList.get(atomNumber);
				atom.setCharge(charge);
			}
		}

		// fill the bond array with 0's
		int[][] bondArray = new int[numAtoms + 1][numAtoms + 1];
		for (int i = 1; i < (numAtoms + 1); i++) {
			Arrays.fill(bondArray[i], 0);
		}

		//read in the bond lines & fill the bondArray
		for (int i = 1; (i < numBonds + 1); i++) {
			String[] bondLineParts = molStringLines[i + numAtoms + 3]
					.split("[ ]+");
			int firstAtom = Integer.parseInt(bondLineParts[1]);
			int secondAtom = Integer.parseInt(bondLineParts[2]);
			int bondIndex = Integer.parseInt(bondLineParts[3]);
			bondArray[firstAtom][secondAtom] = bondIndex;
			bondArray[secondAtom][firstAtom] = bondIndex;
		}

		// go thru data & process each atom's bonds & neighbors
		// round 1: get hybridization info
		for (int i = 1; i < (numAtoms + 1); i++) {
			Atom currentAtom = (Atom) atomList.get(i); // get the center of this
			// group
			for (int j = 1; j < (numAtoms + 1); j++) {
				if (bondArray[i][j] != 0) {
					currentAtom.updateHybridization(bondArray[i][j]);
				}
			}
		}

		//round 2: find if aromatic
		// to be aromatic, it must be a 6-membered ring of sp2 carbons/nitrogens
		// with alternating
		// single & double bonds

		for (int i = 1; i < (numAtoms + 1); i++) {
			Atom firstAtom = (Atom) atomList.get(i);
			if ((firstAtom.getElement().equals("C") || firstAtom.getElement()
					.equals("N"))
					&& (firstAtom.getHybridization() == 2 || firstAtom
					.getAromatic())) {
				for (int j = 1; j < (numAtoms + 1); j++) {
					Atom secondAtom = (Atom) atomList.get(j);
					if ((bondArray[i][j] == 1
							|| bondArray[i][j] == 2
							|| (secondAtom.getAromatic() && bondArray[i][j] != 0) || (firstAtom
									.getAromatic() && bondArray[i][j] != 0))
									&& (secondAtom.getElement().equals("C") || secondAtom
											.getElement().equals("N"))
											&& (secondAtom.getHybridization() == 2) && (i != j)) {
						for (int k = 1; k < (numAtoms + 1); k++) {
							Atom thirdAtom = (Atom) atomList.get(k);
							if (((bondArray[j][k] == 1 && bondArray[i][j] == 2) //these
									// first
									// 2
									// look
									// for
									// alternating
									|| (bondArray[j][k] == 2 && bondArray[i][j] == 1) //single
									// &
									// double
									// bonds
									|| (thirdAtom.getAromatic() && bondArray[j][k] != 0) // or a
									// bond
									// to
									// an
									// aro
									// atom
									|| (secondAtom.getAromatic() && bondArray[j][k] != 0) // or a
									// bond
									// from
									// an
									// aro
									// atom
									)
									&& (thirdAtom.getElement().equals("C") || thirdAtom
											.getElement().equals("N")) //must
											// be C/N
											&& (thirdAtom.getHybridization() == 2) //must
											// be
											// sp2
											&& ((k != i) && (k != j)) //must not be any
											// other atom so
											// far
									) { //in this chain (no backtracking)
								for (int l = 1; l < (numAtoms + 1); l++) {
									Atom fourthAtom = (Atom) atomList.get(l);
									if (((bondArray[k][l] == 1 && bondArray[j][k] == 2)
											|| (bondArray[k][l] == 2 && bondArray[j][k] == 1)
											|| (fourthAtom.getAromatic() && bondArray[k][l] != 0) || (thirdAtom
													.getAromatic() && bondArray[k][l] != 0))
													&& (fourthAtom.getElement().equals(
															"C") || fourthAtom
															.getElement().equals("N"))
															&& (fourthAtom.getHybridization() == 2)
															&& ((l != i) && (l != j) && (l != k))) {
										for (int m = 1; m < (numAtoms + 1); m++) {
											Atom fifthAtom = (Atom) atomList
													.get(m);
											if (((bondArray[l][m] == 1 && bondArray[k][l] == 2)
													|| (bondArray[l][m] == 2 && bondArray[k][l] == 1)
													|| (fifthAtom.getAromatic() && bondArray[l][m] != 0) || (fourthAtom
															.getAromatic() && bondArray[l][m] != 0))
															&& (fifthAtom.getElement()
																	.equals("C") || fifthAtom
																	.getElement()
																	.equals("N"))
																	&& (fifthAtom
																			.getHybridization() == 2)
																			&& ((m != i) && (m != j)
																					&& (m != k) && (m != l))) {
												for (int n = 1; n < (numAtoms + 1); n++) {
													Atom sixthAtom = (Atom) atomList
															.get(n);
													if (((bondArray[m][n] == 1 && bondArray[l][m] == 2)
															|| (bondArray[m][n] == 2 && bondArray[l][m] == 1)
															|| (sixthAtom
																	.getAromatic() && bondArray[m][n] != 0) || (fifthAtom
																			.getAromatic() && bondArray[m][n] != 0))
																			&& (sixthAtom
																					.getElement()
																					.equals("C") || sixthAtom
																					.getElement()
																					.equals("N"))
																					&& (sixthAtom
																							.getHybridization() == 2)
																							&& ((n != i)
																									&& (n != j)
																									&& (n != k)
																									&& (n != l) && (n != m))) {
														//now we have 6 sp2 N/C
														// connected by
														// alternating
														//single & double bonds
														// if 1 connects to 6
														// properly, we have an
														// aromatic ring
														if ((bondArray[n][i] == 2 && bondArray[m][n] == 1)
																|| (bondArray[n][i] == 1 && bondArray[m][n] == 2)) {
															firstAtom
															.setAromatic();
															secondAtom
															.setAromatic();
															thirdAtom
															.setAromatic();
															fourthAtom
															.setAromatic();
															fifthAtom
															.setAromatic();
															sixthAtom
															.setAromatic();
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}

		//round 3: neighbor counts
		for (int i = 1; i < (numAtoms + 1); i++) {
			Atom currentAtom = (Atom) atomList.get(i); // get the center of this
			// group
			for (int j = 1; j < (numAtoms + 1); j++) {
				if (bondArray[i][j] != 0) {
					Atom currentNeighbor = (Atom) atomList.get(j);
					currentAtom.processNeighbor(currentNeighbor.getElement(),
							bondArray[i][j], currentNeighbor.getCharge(),
							currentNeighbor.getAromatic());
				}
			}
		}

		//round 4: check for pi-bonded neighbors
		//be careful - the carbons in ethylene have no pi neighbors
		//since the pi bond is between them
		//to be a pi neighbor, it must be sp2/sp and double-bonded to
		//a different atom than the center
		//first, go atom-by-atom
		for (int i = 1; i < (numAtoms + 1); i++) {
			Atom currentAtom = (Atom) atomList.get(i);
			//then check all of its neighbors
			for (int j = 1; j < (numAtoms + 1); j++) {
				if (bondArray[i][j] != 0) {
					Atom currentNeighbor = (Atom) atomList.get(j);
					if (currentNeighbor.getHybridization() < 3) { //sp2 or sp
						// neighbor
						//go thru the neighbor's neighbors
						for (int k = 1; k < (numAtoms + 1); k++) {
							if ((bondArray[j][k] > 1) && (k != i)) {
								currentAtom.incNumNeighborPi();
							}
						}
					}
				}
			}

		}

		//round 5: calculate number of H neighbors for each atom and sum up charge
		int h = 0;		
		int charge = 0;

		for (int i = 1; i < (numAtoms + 1); i++) {
			Atom atom = (Atom) atomList.get(i);
			atom.getNumNeighborHs();
			h = h + atom.numNeighborHs;
			charge = charge + atom.charge;
		}

		//round 6: see if an amide (N with neighbor carbonyl)
		for (int i = 1; i < (numAtoms + 1); i++) {
			Atom currentAtom = (Atom) atomList.get(i); // get the center of this
			// group
			for (int j = 1; j < (numAtoms + 1); j++) {
				if (bondArray[i][j] != 0) {
					Atom currentNeighbor = (Atom) atomList.get(j);
					if (currentAtom.getElement().equals("N")
							&& currentNeighbor.isACarbonyl) {
						currentAtom.setAmide();
					}
				}
			}
		}

		//round 7: check for "illegal" atoms
		StringBuffer illegalAtoms = new StringBuffer();
		for (int i = 1; i < (numAtoms + 1); i++) {
			Atom atom = (Atom) atomList.get(i);
			if (atom.getElement().equals("C")) {
				if (atom.getCharge() != 0)
					illegalAtoms.append("A Charged C atom.<br>");
			}
			if (atom.getElement().equals("N")) {
				if (atom.getCharge() > 1)
					illegalAtoms
					.append("An N atom with too high + charge.<br>");
				if (atom.getCharge() < 0)
					illegalAtoms.append("An N atom with - charge.<br>");
				if ((atom.getNumNeighborHs() < 0)
						&& !(atom.doubleBondedNeighbor.equals("O2")
								&& (atom.getNumNeighborHs() == -2)
								&& (atom.getNumNeighborCs() == 1)
								&& (atom.getNumNeighborXs() == 2) && (atom
										.getCharge() == 0))) { //don't worry if nitro
					illegalAtoms.append("An N atom making too many bonds.<br>");
				}
			}
			if (atom.getElement().equals("O")) {
				if (atom.getCharge() < -1)
					illegalAtoms
					.append("An O atom with too high - charge.<br>");
				if (atom.getCharge() > 0)
					illegalAtoms.append("An O atom with + charge.<br>");
			}
			if (atom.getElement().equals("S")) {
				if (atom.getCharge() > 0)
					illegalAtoms.append("An S atom with a + charge.<br>");
				if ((atom.getNumNeighborHs() < 0)
						&& (atom.getNumNeighborHs() != -4)
						&& (atom.getNumNeighborHs() != -2)) {
					illegalAtoms
					.append("An S atom not making 2, 4, or 6 bonds.<br>");
				}
			}
			if (atom.getElement().equals("P")) {
				if (atom.getCharge() != 0)
					illegalAtoms.append("A Charged P atom.<br>");
				if (atom.numNeighborHs != -3) {
					illegalAtoms
					.append("A P atom not making 5 bonds.<br>");
				}
			}
			if (atom.getElement().equals("F") || atom.getElement().equals("Cl")
					|| atom.getElement().equals("Br")
					|| atom.getElement().equals("I")) {
				if (atom.getCharge() != 0)
					illegalAtoms.append("A Charged " + atom.getElement()
							+ " atom.<br>");
			}
			if (atom.getElement().equals("X")) {
				illegalAtoms.append("An X atom.<br>");
			}
		}
		errorString = "";
		if (illegalAtoms.length() != 0) {
			errorString = "<html><body>It is not possible to calculate logp<br>"
					+ "for your molecule because it contains:<br>"
					+ illegalAtoms.toString() + "</body></html>";
		}

		//compute total logp = sum of individual atom contributions.
		double logp = 0.000;
		boolean canMakeHbonds = false;
		boolean canMakeIonicBonds = false;

		if (illegalAtoms.length() == 0) {
			NumberFormat nf = NumberFormat.getFormat("0.000");

			for (int i = 1; i < (numAtoms + 1); i++) {
				Atom atom = (Atom) atomList.get(i);
				AtomSpec currentAtomSpec = atom.getAtomSpec();
				logp = logp + currentAtomSpec.getLogp();
				canMakeHbonds = canMakeHbonds || currentAtomSpec.canMakeHbonds();
				canMakeIonicBonds = canMakeIonicBonds || currentAtomSpec.canMakeIonicBonds();

				StringBuffer neighbors = new StringBuffer();
				for (int j = 1; j < (numAtoms + 1); j++) {
					if (bondArray[i][j] != 0) {
						neighbors.append(j + " ");
					}
				}

				atomDataLines.append(i + " " + currentAtomSpec.getType()
						+ "; bonded to: " + neighbors.toString() 
						+ "; logp= " + nf.format(currentAtomSpec.getLogp()) 
						+ "; H-bonds: " + String.valueOf(currentAtomSpec.canMakeHbonds())
						+ "; ionic bonds: " + String.valueOf(currentAtomSpec.canMakeIonicBonds())
						+ "\n");
			}
		}

		//now compute the formula
		// set the counters to zero
		numBonds = 0;
		int numAromaticAtoms = 0;
		int c = 0;
		int n = 0;
		int o = 0;
		int s = 0;
		int p = 0;
		int cl = 0;
		int br = 0;
		int f = 0;
		int iodine = 0;

		StringBuffer formula = new StringBuffer();

		for (int i = 0; i < smileString.length(); i++) {
			switch (smileString.charAt(i)) {
			case 'C':
				c++;
				break;
				// remove H counting since using numNeighborH's works better
				//			case 'H':  
				//				h++;
				//				break;
			case 'N':
				n++;
				break;
			case 'O':
				o++;
				break;
			case 'S':
				s++;
				break;
			case 'P':
				p++;
				break;
			case 'l': // the second letter of 'Cl' so the last C was really 'Cl'
				c--;
				cl++;
				break;
			case 'B':
				br++;
				break;
			case 'F':
				f++;
				break;
			case 'I':
				iodine++;
				break;
			case 'c': //aromatic C
				c++;
				numAromaticAtoms++;
				break;
			case 'n': //aromatic N
				n++;
				numAromaticAtoms++;
				break;
			case 's': //aromatic S
				s++;
				numAromaticAtoms++;
				break;
			case 'o': // aromatic o
				o++;
				numAromaticAtoms++;
				break;
			case '#': //triple bond
				numBonds++;
			case '=': //double bond
				numBonds++;
				break;
			}
		}

		prettyPrint("C", c, formula);
		prettyPrint("H", h, formula);
		prettyPrint("N", n, formula);
		prettyPrint("O", o, formula);
		prettyPrint("P", p, formula);
		prettyPrint("S", s, formula);
		prettyPrint("Cl", cl, formula);
		prettyPrint("Br", br, formula);
		prettyPrint("F", f, formula);
		prettyPrint("I", iodine, formula);

		if (charge != 0) {
			formula.append("(");
			if (charge == -1) {
				formula.append("-");
			} else if (charge > 0) {
				formula.append("+");
			}

			if (Math.abs(charge) != 1) {
				formula.append(String.valueOf(charge));
			}
			formula.append(")");
		}

		formulaString = formula.toString().replaceAll("\\s","");
		
		//now report it all
		MoleculeInfo info = null;
		if (!errorString.equals("")) {
			info = new MoleculeInfo(null, false, false, 0.0f, errorString);
		} else {
			info = new MoleculeInfo(formulaString, canMakeHbonds, canMakeIonicBonds, logp, null);
		}

		return info;
	}

	private void prettyPrint(String atomLabel, int number,
			StringBuffer outString) {
		if (number == 0) {
			return;
		}
		outString.append(atomLabel);

		if (number == 1) {
			outString.append(" ");
			return;
		}

		outString.append(String.valueOf(number));

	}


}
