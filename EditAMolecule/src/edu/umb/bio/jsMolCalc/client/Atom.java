package edu.umb.bio.jsMolCalc.client;

public class Atom {

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
