package edu.umb.bio.jsMolCalc.client;

import java.util.ArrayList;
import java.util.Arrays;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestBuilder.Method;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class JsMolCalc implements EntryPoint {

	private ArrayList<String> moleculeSet = new ArrayList<String>();

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {

		final Button calculateButton = new Button("Calculate");
		final Button newMoleculeButton = new Button("New Molecule");
		final Button loadMolFileButton = new Button("Load MolFile");
		final Button exportMolFileButton = new Button("Export MolFile");
		final TextArea molFileTextArea = new TextArea();
		molFileTextArea.setCharacterWidth(50);
		molFileTextArea.setVisibleLines(10);
		final HTML resultField = new HTML();
		resultField.setHTML("Ready");
		final HTML targetsField = new HTML();

		// We can add style names to widgets
		calculateButton.addStyleName("calculateButton");

		// Add the buttons, etc. to the RootPanel
		// Use RootPanel.get() to get the entire body element
		RootPanel.get("resultFieldContainer").add(resultField);
		RootPanel.get("calculateButtonContainer").add(calculateButton);
		RootPanel.get("newMoleculeButtonContainer").add(newMoleculeButton);
		RootPanel.get("loadMolFileButtonContainer").add(loadMolFileButton);
		RootPanel.get("exportMolFileButtonContainer").add(exportMolFileButton);
		RootPanel.get("molfileInput").add(molFileTextArea);
		RootPanel.get("targetsField").add(targetsField);


		// Create a handler for the calculateButton
		class CalculateButtonHandler implements ClickHandler {
			public void onClick(ClickEvent event) {
				InfoAndTargets result = computeAndDisplay(getJMEmol(), getJMESmiles(), getJMEjme());
				resultField.setHTML(result.info);
				targetsField.setHTML(result.targets);
			}
		}
		CalculateButtonHandler cbHandler = new CalculateButtonHandler();
		calculateButton.addClickHandler(cbHandler);

		// Create a handler for the newMoleculeButton
		class NewMoleculeButtonHandler implements ClickHandler {
			public void onClick(ClickEvent event) {
				String molecule = getNewMolecule();
				setJMEmol(molecule);
				resultField.setHTML("");
			}
		}
		NewMoleculeButtonHandler nmbHandler = new NewMoleculeButtonHandler();
		newMoleculeButton.addClickHandler(nmbHandler);

		// Create a handler for the loadMolFileButton
		class LoadMolFileButtonHandler implements ClickHandler {
			public void onClick(ClickEvent event) {
				String molFile = molFileTextArea.getText();
				setJMEmol(molFile);
				InfoAndTargets result = computeAndDisplay(getJMEmol(), getJMESmiles(), getJMEjme());
				resultField.setHTML(result.info);
				targetsField.setHTML(result.targets);
			}
		}
		LoadMolFileButtonHandler lmbHandler = new LoadMolFileButtonHandler();
		loadMolFileButton.addClickHandler(lmbHandler);

		// Create a handler for the exportMolFileButton
		class ExportMolFileButtonHandler implements ClickHandler {
			public void onClick(ClickEvent event) {
				molFileTextArea.setText(getJMEmol());;
			}
		}
		ExportMolFileButtonHandler embHandler = new ExportMolFileButtonHandler();
		exportMolFileButton.addClickHandler(embHandler);

		// load the set of molecules, if present
		try {
			new RequestBuilder(RequestBuilder.GET, "molecules.txt").sendRequest("", new RequestCallback() {
				public void onResponseReceived(Request req, Response resp) {
					String text = resp.getText();
					String[] lines = text.split("\n");
					for (int i = 0; i < lines.length; i++) {
						moleculeSet.add(lines[i]);
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


	String getNewMolecule() {
		if (moleculeSet.size() == 0) return null;
		return moleculeSet.get(Random.nextInt(moleculeSet.size()));
	}
	
	public String getInfo(String molString, String smileString,
			String jmeString) {
		InfoAndTargets iat = computeAndDisplay(molString, smileString, jmeString);
		String info = iat.info;
		if (info.startsWith("It is not possible")) {
			return "";
		} else {
			return info;
		}
	}
	
	public String getErrors(String molString, String smileString,
			String jmeString) {
		InfoAndTargets iat = computeAndDisplay(molString, smileString, jmeString);
		String info = iat.info;
		if (info.startsWith("It is not possible")) {
			return info;
		} else {
			return "";
		}
	}

	InfoAndTargets computeAndDisplay(String molString, String smileString,
			String jmeString) {

		if (molString.equals("") || smileString.equals("") || jmeString.equals("")) {
			return new InfoAndTargets("","");
		}

		StringBuffer atomDataLines = new StringBuffer(); //for output
		String logpString = new String();
		String bondString = new String();
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
			((Atom)atomList.get(firstAtom)).addBondToNonHAtom(bondIndex);
			((Atom)atomList.get(secondAtom)).addBondToNonHAtom(bondIndex);
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
				if (atom.getCharge() != 0) {
					illegalAtoms.append("A Charged " + atom.getElement()
							+ " atom.<br>");
				}
				if (atom.getNumBondsToNonHAtoms() > 1) {
					illegalAtoms.append("A " + atom.getElement() +  " atom making more than one bond.<br>");
				}
			}
			if (atom.getElement().equals("X")) {
				illegalAtoms.append("An X atom.<br>");
			}
		}
		errorString = "";
		if (illegalAtoms.length() != 0) {
			errorString = "<html><body>It is not possible to calculate the properties<br>"
					+ "of your molecule because it contains:<br>"
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
			if (logp < 0) {
				logpString = "<font color=green>Hydrophobicity index = " + nf.format(logp)
						+ "</font>";
			} else {
				logpString = "<font color=red>Hydrophobicity index = " + nf.format(logp)
						+ "</font>";
			}

			if (canMakeHbonds) {
				bondString = "<font color=green>Can Make Strong Hydrogen Bonds</font><br>";
			} else {
				bondString = "<font color=red>Can not Make Strong Hydrogen Bonds</font><br>";
			}

			if (canMakeIonicBonds) {
				bondString += "<font color=green>Can Make Ionic Bonds</font>";
			} else {
				bondString += "<font color=red>Can not Make Ionic Bonds</font>";
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

		formulaString = "Formula: " + formula.toString();

		//now show it all
		String moleculeInfo = "";
		if (!errorString.equals("")) {
			moleculeInfo = errorString;
		} else {
			moleculeInfo = "<html><body>" 
					+ formulaString + "<br>"
					+ logpString + "<br>"
					+ bondString
					+ "</body></html>";
		}

		/*
		 * use the current specifications to generate modification targets
		 *   eg, if it can make ionic bonds, a target is no ionic bonds, etc. 
		 */
		String targets = "";
		if (errorString.equals("")) {
			NumberFormat nf = NumberFormat.getFormat("0.000");

			targets = "<html><body>Edit the molecule so that it:<br><ul>";

			targets += "<li>can ";
			if (canMakeHbonds) {
				targets += " not ";
			} 
			targets += "make Hydrogen bonds</li>";

			targets += "<li>can ";
			if (canMakeIonicBonds) {
				targets += " not ";
			} 
			targets += "make Ionic bonds</li>";

			targets += "<li>has a relative hydrophobicity between ";
			targets += nf.format(logp - 1.5f);
			targets += " and ";
			targets += nf.format(logp - 0.5f);
			targets += "</li>";

			targets += "<li>has a relative hydrophobicity between ";
			targets += nf.format(logp + 0.5f);
			targets += " and ";
			targets += nf.format(logp + 1.5f);
			targets += "</li>";

			targets += "</ul></body></html>";
		} 



		return new InfoAndTargets(moleculeInfo, targets);
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

		outString.append("<sub>" + String.valueOf(number) + "</sub> ");

	}

	private class Atom {

		String element;

		int charge;

		boolean aromatic;

		boolean amide;

		boolean isACarbonyl;

		String doubleBondedNeighbor;

		int numNeighborHs;

		int numNeighborCs;

		int numNeighborXs;

		int hybridization;

		int numNeighborPi;

		int numNeighborNAro;

		int numNeighborAro;

		double chargeOffset;

		String chargeString;

		boolean alreadyHasOneDoubleBond;

		boolean isAnAllene;

		int currentMaxBondIndex;
		
		int numBondsToNonHAtoms;

		boolean phobic;

		double extra;

		public Atom() {
			element = new String("");
			charge = 0;
			aromatic = false;
			amide = false;
			isACarbonyl = false;
			doubleBondedNeighbor = new String("");
			numNeighborHs = 0;
			numNeighborCs = 0;
			numNeighborXs = 0;
			hybridization = 3;
			numNeighborPi = 0;
			numNeighborNAro = 0;
			numNeighborAro = 0;
			chargeOffset = 0.000;
			chargeString = "";

			alreadyHasOneDoubleBond = false;
			isAnAllene = false;
			currentMaxBondIndex = 0;
			numBondsToNonHAtoms = 0;
			phobic = false;
			extra = 0.000;
		}

		public void setAromatic() {
			aromatic = true;
		}

		public boolean getAromatic() {
			return aromatic;
		}

		public void setAmide() {
			amide = true;
		}

		public boolean getAmide() {
			return amide;
		}

		public void setElement(String type) {
			element = type;
		}

		public String getElement() {
			return element;
		}

		public void setCharge(int value) {
			charge = value;
		}

		public int getCharge() {
			return charge;
		}

		public void incNumNeighborPi() {
			numNeighborPi++;
		}

		public void updateHybridization(int bondIndex) {
			if (isAnAllene) { // if we already know it's a =C=, no need to update
				return;
			}

			if ((bondIndex == 2) && (alreadyHasOneDoubleBond)) { // second = to make
				// allene
				isAnAllene = true;
				currentMaxBondIndex = 3;
				hybridization = 1;
				return;
			}

			if (bondIndex == 2) {
				alreadyHasOneDoubleBond = true;
			}

			if (bondIndex > currentMaxBondIndex) {
				currentMaxBondIndex = bondIndex;
				hybridization = 4 - currentMaxBondIndex;
			}
		}

		public int getHybridization() {
			return hybridization;
		}
		
		public void addBondToNonHAtom(int bondIndex) {
			numBondsToNonHAtoms = numBondsToNonHAtoms + bondIndex;
		}

		public int getNumBondsToNonHAtoms() {
			return numBondsToNonHAtoms;
		}
		
		public void processNeighbor(String element, int bondIndex, int charge,
				boolean aromatic) {

			if (element.equals("O") && (bondIndex == 2)) {
				isACarbonyl = true;
			}

			if (aromatic) {
				numNeighborAro++;
			}

			if (element.equals("N") && aromatic) {
				numNeighborNAro++;
			}

			if (bondIndex == 2) {
				if (doubleBondedNeighbor.equals("O")) { // if you aready have a =O
					doubleBondedNeighbor = "O2";
				} else {
					doubleBondedNeighbor = element;
				}
			}

			if (element.equals("C")) {
				numNeighborCs++;
				return;
			}

			if (element.equals("N") || 
					element.equals("O") || 
					element.equals("S") ||
					element.equals("Cl") ||
					element.equals("F") ||
					element.equals("Br") ||
					element.equals("I")){
				numNeighborXs++;
			}

			return;
		}

		public int getNumNeighborHs() {
			if (element.equals("C")) {
				numNeighborHs = hybridization + 1 - numNeighborCs - numNeighborXs;
				return numNeighborHs;
			}

			if (element.equals("N")) {
				numNeighborHs = hybridization - numNeighborCs - numNeighborXs
						+ charge;
				return numNeighborHs;
			}

			if ((element.equals("O")) || (element.equals("S"))) {
				numNeighborHs = hybridization - 1 - numNeighborCs - numNeighborXs
						+ charge;
				return numNeighborHs;
			}

			numNeighborHs = 1 - numNeighborCs - numNeighborXs;
			return numNeighborHs;
		}

		public int getNumNeighborCs() {
			return numNeighborCs;
		}

		public int getNumNeighborXs() {
			return numNeighborXs;
		}

		public int getNumNeighborPi() {
			return numNeighborPi;
		}

		public String toString() {
			StringBuffer stringBuffer = new StringBuffer(element + " " + charge
					+ " sp" + hybridization + " ");
			if (aromatic) {
				stringBuffer.append("aro ");
			}
			if (amide) {
				stringBuffer.append("amide ");
			}
			stringBuffer.append("nH=" + this.getNumNeighborHs() + " ");
			stringBuffer.append("nC=" + numNeighborCs + " ");
			stringBuffer.append("nX=" + numNeighborXs + " ");
			stringBuffer.append("nPi=" + numNeighborPi + " ");
			stringBuffer.append("nAro=" + numNeighborAro + " ");
			stringBuffer.append("nNar=" + numNeighborNAro + " ");
			stringBuffer.append("dbN=" + doubleBondedNeighbor + " ");
			stringBuffer.append("bTNHA=" + numBondsToNonHAtoms + " ");
			stringBuffer.append("logp="
					+ Double.toString(this.getAtomSpec().getLogp()));

			return stringBuffer.toString();
		}

		public AtomSpec getAtomSpec() {
			chargeOffset = 0.000;
			chargeString = "";

			if (element.equals("C")) {
				switch (hybridization) {
				case 3: //**sp3 carbons**
					switch (numNeighborHs) {
					case 4:
						return new AtomSpec(0.528, "C: sp3, H4", false, false);
					case 3:
						if (numNeighborXs == 0) {
							if (numNeighborPi == 0) {
								return new AtomSpec(0.528, "C: sp3; C H H H, no pi", false, false);
							} else {
								return new AtomSpec(0.267, "C: sp3; C H H H, pi", false, false);
							}
						} else {
							return new AtomSpec(-0.032, "C: sp3; X H H H", false, false);
						}
					case 2:
						if (numNeighborXs == 0) {
							switch (numNeighborPi) {
							case 0:
								return new AtomSpec(0.358, "C: sp3; C C H H, no pi", false, false);
							case 1:
								return new AtomSpec(-0.008, "C: sp3; C C H H, 1 pi", false, false);
							case 2:
								return new AtomSpec(-0.185, "C: sp3; C C H H, 2 pi", false, false);
							default:
								return new AtomSpec(0.000, "C: sp3; ? ? H H", false, false);
							}
						} else {
							switch (numNeighborPi) {
							case 0:
								return new AtomSpec(-0.137,
										"C: sp3; C X H H, no pi", false, false);
							case 1:
								return new AtomSpec(-0.303, "C: sp3; C X H H, 1 pi", false, false);
							case 2:
								return new AtomSpec(-0.815, "C: sp3; C X H H, 2 pi", false, false);
							default:
								return new AtomSpec(0.000, "C: sp3; ? X H H, H2", false, false);
							}
						}
					case 1:
						if (numNeighborXs == 0) {
							switch (numNeighborPi) {
							case 0:
								return new AtomSpec(0.127, "C: sp3; C C C H, no pi", false, false);
							case 1:
								return new AtomSpec(-0.243, "C: sp3; C C C H, 1 pi", false, false);
							default:
								return new AtomSpec(-0.499,
										"C: sp3; C C C H, >1 pi", false, false);
							}
						} else {
							switch (numNeighborPi) {
							case 0:
								return new AtomSpec(-0.205,
										"C: sp3; C C/X X H, no pi", false, false);
							case 1:
								return new AtomSpec(-0.305,
										"C: sp3; C C/X X H, 1 pi", false, false);
							default:
								return new AtomSpec(-0.709,
										"C: sp3; C C/X X H, >1 pi", false, false);
							}
						}
					case 0:
						if (numNeighborXs == 0) {
							switch (numNeighborPi) {
							case 0:
								return new AtomSpec(-0.006,
										"C: sp3; C C C C, no pi", false, false);
							case 1:
								return new AtomSpec(-0.570, "C: sp3; C C C C, 1 pi", false, false);
							default:
								return new AtomSpec(-0.317,
										"C: sp3; C C C C, >1 pi", false, false);
							}
						} else {
							switch (numNeighborPi) {
							case 0:
								return new AtomSpec(-0.316,
										"C: sp3; C C/X C/X X, no pi", false, false);
							default:
								return new AtomSpec(-0.723,
										"C: sp3; C C/X C/X X, pi", false, false);
							}
						}
					default:
						return new AtomSpec(0.000, "C: sp3; unknown", false, false);

					} // done with **sp3 carbons**
				case 2: //**sp2 carbons**
					if (!aromatic) {
						switch (numNeighborHs) {
						case 2:
							return new AtomSpec(0.420, "C: sp2; =? H H", false, false);
						case 1:
							switch (numNeighborXs) {
							case 1:
								if (numNeighborPi == 0) {
									return new AtomSpec(0.001,
											"C: sp2; =? X H, no pi", false, false);
								} else {
									return new AtomSpec(-0.310,
											"C: sp2; =? X H, 1 pi", false, false);
								}
							case 0:
								if (numNeighborPi == 0) {
									return new AtomSpec(0.466,
											"C: sp2; =? C H, no pi", false, false);
								} else {
									return new AtomSpec(0.136,
											"C: sp2; =? C H, 1 pi", false, false);
								}
							default:
								return new AtomSpec(0.000, "C: sp2; =? H, unknown", false, false);
							}
						case 0:
							switch (numNeighborXs) {
							case 2:
								if (numNeighborPi == 0) {
									return new AtomSpec(0.005,
											"C: sp2; =? X X, no pi", false, false);
								} else {
									return new AtomSpec(-0.315,
											"C: sp2; =? X X, pi", false, false);
								}
							case 1:
								if (numNeighborPi == 0) {
									return new AtomSpec(-0.030,
											"C: sp2; =? C X, no pi", false, false);
								} else {
									return new AtomSpec(-0.027,
											"C: sp2; =? C X, pi", false, false);
								}
							case 0:
								if (numNeighborPi == 0) {
									return new AtomSpec(0.050,
											"C: sp2; =? C C, no pi", false, false);
								} else {
									return new AtomSpec(0.013, "C: sp2; =? C C, pi", false, false);
								}
							default:
								return new AtomSpec(0.000,
										"C: sp2; =? C C, unknown", false, false);
							}
						default:
							return new AtomSpec(0.000, "C: sp2; unknown", false, false);
						}
					} // done with non-aromatic sp2 c's - these must be aromatics
					switch (numNeighborHs) {
					case 1:
						if (numNeighborNAro == 0) {
							return new AtomSpec(0.337, "C: aromatic; C C H", false, false);
						} else {
							return new AtomSpec(0.126, "C: aromatic; aro-N C H", false, false);
						}
					case 0:
						switch (numNeighborXs) {
						case 2:
							if (numNeighborNAro == 0) {
								return new AtomSpec(0.000,
										"C: aromatic; unknown C X X", false, false);
							} else {
								return new AtomSpec(0.366, "C: aromatic; aro-N C X", false, false);
							}
						case 1:
							if (numNeighborNAro == 0) {
								return new AtomSpec(-0.151, "C: aromatic; C C X", false, false);
							} else {
								return new AtomSpec(0.174, "C: aromatic; aro-N C C", false, false);
							}
						case 0:
							return new AtomSpec(0.296, "C: aromatic; C C C", false, false);
						default:
							return new AtomSpec(0.000, "C: aromatic; unknown", false, false);

						}
					default:
						return new AtomSpec(0.000, "C: aromatic; unknown", false, false);
					}

				case 1: //**sp carbons**
					if (isAnAllene)
						return new AtomSpec(2.073, "C: =C=", false, false);
					if (numNeighborHs == 0) {
						return new AtomSpec(0.330, "C: sp; ? ?", false, false);
					} else {
						return new AtomSpec(0.209, "C: sp; ? H", false, false);
					}
				default:
					return new AtomSpec(0.000, "C: unknown", false, false);
				} // done with switching on carbon hybridization
			} // done with carbon possibilities

			if (element.equals("N")) {
				boolean charged = false;

				if (charge == 1) {
					charged = true;
					if (aromatic) {
						chargeOffset = -1.000;
						chargeString = " (N+Aro)";
					} else {
						switch (numNeighborCs) {
						case 4:
							chargeOffset = -4.500;
							chargeString = " (N+quat)";
							break;
						case 3:
							chargeOffset = -3.000;
							chargeString = " (N+tert)";
							break;
						case 2:
							chargeOffset = -2.500;
							chargeString = " (N+sec)";
							break;
						case 1:
							chargeOffset = -2.000;
							chargeString = " (N+pri)";
							break;
						default:
							chargeOffset = 0.000;
							chargeString = " (N+???)";
							break;
						}
					}

				}
				/* now, process what type of N 
				 * independent of charge
				 *
				 * if these are tertiary or quaternary N's (in general, when NumNeighborHs == 0)
				 *   if uncharged (tertiary), they have a lone pair that can H-bond
				 *   if charged (quat), they have no lone pair
				 *   hence the H-bond potential is "!charged"
				 */


				if (amide) {
					switch (numNeighborHs) {
					case 2:
						return new AtomSpec(-0.646 + chargeOffset,
								"N: amide; C=O H H" + chargeString, true, charged);
					case 1:
						if (numNeighborXs == 0) {
							return new AtomSpec(-0.096 + chargeOffset,
									"N: amide; C=O C H" + chargeString, true, charged);
						} else {
							return new AtomSpec(-0.044 + chargeOffset,
									"N: amide; C=O X H" + chargeString, true, charged);
						}
					case 0:
						if (numNeighborXs == 0) {
							return new AtomSpec(0.078 + chargeOffset,
									"N: amide; C=O C C" + chargeString, !charged, charged);
						} else {
							return new AtomSpec(-0.118 + chargeOffset,
									"N: amide; C=O C X" + chargeString, !charged, charged);
						}
					}
				}

				// not an amide
				switch (hybridization) {
				case 3:
					switch (numNeighborHs) {
					case 2:
						if (numNeighborXs != 0)
							return new AtomSpec(-1.082 + chargeOffset,
									"N: sp3; X H H" + chargeString, true, charged);
						if (numNeighborPi == 0) {
							return new AtomSpec(-0.534 + chargeOffset,
									"N: sp3; C H H, no pi" + chargeString, true, charged);
						} else {
							return new AtomSpec(-0.329 + chargeOffset,
									"N: sp3; C H H, pi" + chargeString, true, charged);
						}
					case 1:
						if (numNeighborXs != 0)
							return new AtomSpec(0.324 + chargeOffset,
									"N: sp3; C X H" + chargeString, true, charged);
						if (numNeighborPi == 0) {
							return new AtomSpec(-0.112 + chargeOffset,
									"N: sp3; C C H, no pi" + chargeString, true, charged);
						} else {
							return new AtomSpec(0.166 + chargeOffset,
									"N: sp3; C C H, pi" + chargeString, true, charged);
						}
					case 0:
						if (numNeighborXs != 0)
							return new AtomSpec(-0.239 + chargeOffset,
									"N: sp3; C C X" + chargeString, !charged, charged);
						if (numNeighborPi == 0) {
							return new AtomSpec(0.159 + chargeOffset,
									"N: sp3; C C C, no pi" + chargeString, !charged, charged);
						} else {
							return new AtomSpec(0.761 + chargeOffset,
									"N: sp3; C C C, pi" + chargeString, !charged, charged);
						}
					default:
						return new AtomSpec(0.000 + chargeOffset, "N: sp3; unknown"
								+ chargeString, true, charged);
					}
				case 2:
					if (aromatic) {
						return new AtomSpec(-0.493 + chargeOffset, "N: aromatic"
								+ chargeString, !charged, charged);
					}
					if (doubleBondedNeighbor.equals("C")) {
						if (numNeighborXs == 0) {
							if (numNeighborPi == 0) {
								if (numNeighborHs == 0) {
									return new AtomSpec(0.007 + chargeOffset,
											"N: sp2; =C ? no H, no pi" + chargeString, !charged, charged);
								}  else {
									return new AtomSpec(0.007 + chargeOffset,
											"N: sp2; =C ? H, no pi" + chargeString, true, charged);
								}
							} else {
								if (numNeighborHs == 0) {
									return new AtomSpec(-0.275 + chargeOffset,
											"N: sp2; =C ? no H, pi" + chargeString, !charged, charged);
								} else {
									return new AtomSpec(-0.275 + chargeOffset,
											"N: sp2; =C ? H, pi" + chargeString, true, charged);
								}
							}
						} else {
							if (numNeighborPi == 0) {
								if (numNeighborHs == 0) {
									return new AtomSpec(0.366 + chargeOffset,
											"N: sp2; =C X no H, no pi" + chargeString, !charged, charged);
								} else {
									return new AtomSpec(0.366 + chargeOffset,
											"N: sp2; =C X H, no pi" + chargeString, true, charged);
								}
							} else {
								if (numNeighborHs == 0) {
									return new AtomSpec(0.251 + chargeOffset,
											"N: sp2; =C X no H, pi" + chargeString, !charged, charged);
								} else {
									return new AtomSpec(0.251 + chargeOffset,
											"N: sp2; =C X H, pi" + chargeString, true, charged);
								}
							}
						}
					}
					if (doubleBondedNeighbor.equals("N")) {
						if (numNeighborXs == 1) { // not 0 because X
							//is always at least 1
							//due to N neighbor
							if (numNeighborHs == 0) {
								return new AtomSpec(0.536 + chargeOffset,
										"N: sp2; =N ? no H" + chargeString, !charged, charged);
							} else {
								return new AtomSpec(0.536 + chargeOffset,
										"N: sp2; =N ? H" + chargeString, true, charged);
							}
						} else {
							if (numNeighborHs == 0) {
								return new AtomSpec(-0.597 + chargeOffset,
										"N: sp2; =N X no H" + chargeString, !charged, charged);
							} else {
								return new AtomSpec(-0.597 + chargeOffset,
										"N: sp2; =N X H" + chargeString, true, charged);
							}
						}
					}
					if (doubleBondedNeighbor.equals("O"))
						return new AtomSpec(0.427 + chargeOffset, "N: nitroso"
								+ chargeString, true, charged);
					if (numNeighborXs != 0)
						return new AtomSpec(0.427, "N: sp2; =? ?", true, charged);
					return new AtomSpec(0.000 + chargeOffset, "N: sp2; unknown"
							+ chargeString, true, charged);
				case 1:
					if (doubleBondedNeighbor.equals("O2")) {
						return new AtomSpec(1.178, "N: nitro", true, false);
					} else {
						if (numNeighborHs == 0) {
							return new AtomSpec(-0.566 + chargeOffset, "N: sp no H"
									+ chargeString, !charged, charged);
						} else {
							return new AtomSpec(-0.566 + chargeOffset, "N: sp H"
									+ chargeString, true, charged);
						}
					}
				default:
					return new AtomSpec(0.000 + chargeOffset, "N: unknown"
							+ chargeString, true, charged);
				} //done with switching on nitrogen hybridization
			} //done with nitrogen possibilities

			if (element.equals("O")) {
				boolean charged = false;
				if (charge == -1) {
					charged = true;
					chargeOffset = -3.5;
					chargeString = " (-)";
				}
				switch (hybridization) {
				case 3:
					switch (numNeighborHs) {
					case 1:
						if (numNeighborXs != 0)
							return new AtomSpec(-0.522 + chargeOffset,
									"O: sp3; X H" + chargeString, true, charged);
						if (numNeighborPi == 0) {
							return new AtomSpec(-0.467 + chargeOffset,
									"O: sp3; C H, no pi" + chargeString, true, charged);
						} else {
							return new AtomSpec(0.082 + chargeOffset,
									"O: sp3; C H, pi" + chargeString, true, charged);
						}
					case 0:
						if (numNeighborXs != 0)
							return new AtomSpec(0.105 + chargeOffset, "O: sp3; C X"
									+ chargeString, true, charged);
						if (numNeighborPi == 0) {
							return new AtomSpec(0.084 + chargeOffset,
									"O: sp3; C C, no pi" + chargeString, true, charged);
						} else {
							return new AtomSpec(0.435 + chargeOffset,
									"O: sp3; C C, pi" + chargeString, true, charged);
						}
					default:
						return new AtomSpec(0.000 + chargeOffset, "O: sp3; unknown"
								+ chargeString, true, charged);
					}
				case 2:
					return new AtomSpec(-0.399 + chargeOffset, "O: sp2"
							+ chargeString, true, charged);

				default:
					return new AtomSpec(0.000 + chargeOffset, "O: unknown"
							+ chargeString, true, charged);

				} //done with switching on oxygen hybridization
			} //done with oxygen possibilities

			if (element.equals("S")) {
				boolean charged = false;
				if (charge == -1) {
					chargeOffset = -0.750;
					chargeString = " (-)";
					charged = true;
				}

				switch (hybridization) {
				case 3:
					if (numNeighborHs == 0) {
						return new AtomSpec(0.255 + chargeOffset, "S: sp3; C/X C/X"
								+ chargeString, false, charged);
					} else {
						return new AtomSpec(0.419 + chargeOffset, "S: sp3; C/X H"
								+ chargeString, false, charged);
					}
				case 2:
					if (doubleBondedNeighbor.equals("O")) {
						return new AtomSpec(-1.375, "S: sulfoxide", false, charged);
					} else {
						return new AtomSpec(-0.148 + chargeOffset, "S: sp2"
								+ chargeString, false, charged);
					}
				case 1:
					if (doubleBondedNeighbor.equals("O2")) {
						return new AtomSpec(-0.168, "S: sulfone", false, charged);
					}
				default:
					return new AtomSpec(0.000 + chargeOffset, "S: unknown"
							+ chargeString, false, charged);

				} //done with sulfur hybridization switch
			} //done with sulfur possibilities

			if (element.equals("P") && (hybridization == 2)) {
				if (doubleBondedNeighbor.equals("O")) {
					return new AtomSpec(-0.447, "P: in phosphate", false, false);
				}
				if (doubleBondedNeighbor.equals("S")) {
					return new AtomSpec(1.253, "P: in thio-phosphate", false, false);
				}
				return new AtomSpec(0.000, "P: unknown", false, false);
			}

			if (element.equals("F")) {
				if (numNeighborPi == 0) {
					return new AtomSpec(0.375, "F; no pi", false, false);
				} else {
					return new AtomSpec(0.202, "F; pi", false, false);
				}
			}

			if (element.equals("Cl")) {
				if (numNeighborPi == 0) {
					return new AtomSpec(0.512, "Cl; no pi", false, false);
				} else {
					return new AtomSpec(0.663, "Cl; pi", false, false);
				}
			}

			if (element.equals("Br")) {
				if (numNeighborPi == 0) {
					return new AtomSpec(0.850, "Br; no pi", false, false);
				} else {
					return new AtomSpec(0.839, "Br; pi", false, false);
				}
			}

			if (element.equals("I")) {
				if (numNeighborPi == 0) {
					return new AtomSpec(1.050, "I; no pi", false, false);
				} else {
					return new AtomSpec(1.109, "I; pi", false, false);
				}
			}

			return new AtomSpec(0.000, "unknown atom", false, false);
		} //done calculating logp contribution

	}

	private class AtomSpec {

		private String type;
		private double logp;
		private boolean canMakeHbonds;
		private boolean canMakeIonicBonds;


		public AtomSpec(double logp, String type, boolean canMakeHbonds, boolean canMakeIonicBonds) {
			this.type = type;
			this.logp = logp;
			this.canMakeHbonds = canMakeHbonds;
			this.canMakeIonicBonds = canMakeIonicBonds; 
		}

		public String getType() {
			return type;
		}

		public double getLogp() {
			return logp;
		}

		public boolean canMakeHbonds() {
			return canMakeHbonds;
		}

		public boolean canMakeIonicBonds() {
			return canMakeIonicBonds;
		}

	}

	/*
	 * this stores the result of the analysis
	 * 	info is the specifications of the molecule in html
	 *  targets is a list of possible modification targets
	 */
	private class InfoAndTargets {
		public String info;
		public String targets;

		public InfoAndTargets(String info, String targets) {
			this.info = info;
			this.targets = targets;
		}	
	}
}
