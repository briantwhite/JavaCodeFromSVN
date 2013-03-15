package SE;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;



public class GeneticCode {

	private String[] codeTable;
	private boolean[] isStartCodon;
	private boolean[] isStopCodon;
	private HashMap<String, ArrayList<String>> reverseTranslationTable;

	private HashSet<String> all20;

	private HashMap<String, String> translatedSequenceCache;

	private Random r;

	public GeneticCode() {
		this("StandardCode.xml");
	}

	public GeneticCode(String fileName) {

		// set up set of legal amino acid names
		String[] aaList = {
				"A", "C", "D", "E", "F", 
				"G", "H", "I", "K", "L", 
				"M", "N", "P", "Q", "R", 
				"S", "T", "V", "W", "Y"};
		all20 = new HashSet<String>();
		for (String aa : aaList) {
			all20.add(aa);
		}

		translatedSequenceCache = new HashMap<String, String>();

		Element root = null;
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fileName);
			doc.getDocumentElement().normalize();
			root = doc.getDocumentElement();
		} catch (Exception e1) {
			System.err.println("Error reading " + fileName + " perhaps it is improperly formatted.\n" + e1.getMessage());
		}

		if (root != null) {
			r = new Random();

			// set up the tables
			codeTable = new String[64];
			isStartCodon = new boolean[64];
			isStopCodon = new boolean[64];
			for (int i = 0; i < isStartCodon.length; i++) {
				codeTable[i] = null;
				isStartCodon[i] = false;
				isStopCodon[i] = false;
			}
			reverseTranslationTable = new HashMap<String, ArrayList<String>>();

			// first the codons
			NodeList nl = root.getElementsByTagName("Codon");
			for (int i = 0; i < nl.getLength(); i++) {
				Node n = nl.item(i);
				if (n.getNodeType() == Node.ELEMENT_NODE) {
					// decode the xml
					Element e = (Element) n;
					NodeList tL = e.getElementsByTagName("Triplet");
					String triplet = tL.item(0).getChildNodes().item(0).getNodeValue();

					// see if illegal triplet
					if (codonToIndex(triplet) == -1) break;

					tL = e.getElementsByTagName("AA");
					String aa = tL.item(0).getChildNodes().item(0).getNodeValue();

					//save in various tables
					int index = codonToIndex(triplet);

					if (index != -1) {
						if (aa.equals("STOP")) {
							isStopCodon[index] = true;
						} else {
							// be sure it's a legal amino acid name 
							if (!all20.contains(aa)) {
								System.err.println(
										"Your genetic code includes an illegal amino acid abbreviation:" 
												+ aa 
												+ " for codon "
												+ triplet
												+ " ; it won't work properly");
								codeTable[index] = null;				
							} else {
								// be sure it's empty first (no duplicates)
								if ((codeTable[index] == null) && !isStopCodon[index]) {
									codeTable[index] = aa;
								} else {
									System.err.println(
											"Your genetic code includes duplicate entries for one codon; it won't work properly");
									codeTable[index] = null;
								}
							}
						}
						if (!reverseTranslationTable.containsKey(aa)) {
							reverseTranslationTable.put(aa, new ArrayList<String>());
						} 
						reverseTranslationTable.get(aa).add(triplet);
					}
				}
			}

			// now the start codon(s)
			nl = root.getElementsByTagName("StartCodon");
			for (int i = 0; i < nl.getLength(); i++) {
				Node n = nl.item(i);
				isStartCodon[codonToIndex(n.getFirstChild().getNodeValue())] = true;
			}

			// now check it
			//  be sure >= 1 start
			//  		>= 1 stop
			//			all 64 encode something
			//			all 20 amino acids have at least one codon
			boolean hasStartCodon = false;
			boolean hasStopCodon = false;
			boolean missingACodon = false;
			boolean missingAnAA = false;
			StringBuffer missingCodonBuffer = new StringBuffer();
			HashSet<String> aminoAcidsEncoded = new HashSet<String>();
			for (int i = 0; i < codeTable.length; i++) {
				if ((codeTable[i] == null) && (!isStopCodon[i])) missingACodon = true;
				if (isStartCodon[i]) hasStartCodon = true;
				if (isStopCodon[i]) hasStopCodon = true;
				if (codeTable[i] != null) {
					aminoAcidsEncoded.add(codeTable[i]);
				}
			}

			StringBuffer missingAABuffer = new StringBuffer();
			for (String aa : all20) {
				if (!aminoAcidsEncoded.contains(aa)) {
					missingAABuffer.append(aa + " ");
					missingAnAA = true;
				}
			}

			if (missingACodon) System.err.println("Your code has no amino acid for one or more codons; it won't work properly!");
			if (!hasStartCodon) System.err.println("Your code is missing a start codon; it won't work properly!");
			if (!hasStopCodon) System.err.println("Your code is missing a start codon; it won't work properly!");
			if (missingAnAA) {
				System.err.println(
						"Your code is missing codons for these amino acids " 
								+ missingAABuffer.toString()
								+ "; it won't work properly!");
			}

		}
	}

	public void clearTranslatedSequenceCache() {
		translatedSequenceCache = new HashMap<String, String>();
	}

	/*
	 * converts codon strings to 0-63 index to codon table
	 * uses ascii values for AGCT to make index:
	 * char		hex		bin   **
	 *	A		41		0100 0001
	 *	C		43		0100 0011
	 *	G		47		0100 0111
	 *	T		54		0101 0100
	 **
	 *	the * bits are different for each
	 *	A 00
	 *	C 01
	 *	G 11
	 *	T 10
	 *
	 */
	public int codonToIndex(byte b1, byte b2, byte b3) {
		return ((b1 & 0x06) >> 1) + ((b2 & 0x06) << 1) + ((b3 & 0x06) << 3);
	}

	public int codonToIndex(String codon) {
		Pattern p = Pattern.compile("[^AGCT]+");
		Matcher m = p.matcher(codon);
		if (m.find() || (codon.length() != 3)) {
			System.err.println(codon + " is not a proper codon. Ignoring it.");
			return -1;
		}
		byte[] bytes = codon.getBytes();
		return codonToIndex(bytes[0], bytes[1], bytes[2]);
	}

	public String indexToCodon(int index) {
		byte b = (byte)index;
		byte b1 = (byte) (b & 0x03);
		byte b2 = (byte) ((b & 0xC0) >>> 2);
		byte b3 = (byte) ((b & 0x30) >>> 4);
		return byteToLetter(b1) + byteToLetter(b2) + byteToLetter(b3);
	}

	public String byteToLetter(byte b) {
		if (b == 0x00) return "A";
		if (b == 0x01) return "C";
		if (b == 0x02) return "T";
		if (b == 0x03) return "G";
		return "";
	}


	/*
	 * used by TestCode
	 * 	if stop codon, return "*"
	 *  if unassigned, return "!"
	 *  if start codon, show in lower case
	 *  elsewise, show in upper case
	 */
	public String getAminoAcidByCodon(String codon) {
		int index = codonToIndex(codon);
		String aa = codeTable[index];
		if (aa == null) {
			if (isStopCodon[index]) {
				return "*";
			} else {
				return "!";
			}
		} else {
			if (isStartCodon[index]) {
				return aa.toLowerCase();
			} else {
				return aa;
			}
		}
	}


	/*
	 * returns protein string with no separation between aas
	 * uses the translated sequence cache - not always faster
	 */
	public String translateWithCache(String DNA) {
		if (DNA.length() < 6) return "";

		if (translatedSequenceCache.get(DNA) == null) {
			StringBuffer b = new StringBuffer();  
			byte[] DNAbytes = DNA.getBytes();
			int length = DNAbytes.length;
			boolean allDone = false;
			for (int i = 0; ((i < length - 2) && !allDone); i++) {
				// move along DNA until you find start codon
				if (isStartCodon[codonToIndex(DNAbytes[i], DNAbytes[i + 1], DNAbytes[i + 2])]) {
					// found one; now start translating
					for(int j = i; j < length - 2; j = j + 3) {
						int index = codonToIndex(DNAbytes[j], DNAbytes[j + 1], DNAbytes[j + 2]);
						if (isStopCodon[index]) {
							allDone = true;
							i = j;
							break;
						}
						b.append(codeTable[index]);
					}
					// you've run out of DNA
					break;
				}
			}
			translatedSequenceCache.put(DNA, b.toString());
		} 
		return translatedSequenceCache.get(DNA);
	}


	/*
	 *  returns pretty string with DNA on one line and protein below
	 *  aligned with DNA
	 */
	public String prettyTranslate(String DNA) {
		StringBuffer b = new StringBuffer();
		String protein = translateWithCache(DNA);
		b.append(DNA);
		b.append("\n");
		if (protein.equals("")) return b.toString();

		for (int i = 0; i < DNA.length() - 2; i++) {
			String codon = DNA.substring(i, i + 3);
			b.append(" ");
			if (isStartCodon[codonToIndex(codon)]) {
				for (int x = 0; x < protein.length(); x++) {
					b.append(protein.substring(x, x + 1));
					b.append("  ");
				}
				break;
			}
		}
		return b.toString();
	}

	public String reverseTranslate(String protein) {
		StringBuffer b = new StringBuffer();
		// be sure first aa is encoded by a start codon
		ArrayList<String> codonsForFirstAA = 
				reverseTranslationTable.get(protein.substring(0,1));

		// make a list of the start codons that could encode first aa
		ArrayList<String> possibleStartCodons = new ArrayList<String>();
		for (int i = 0; i < codonsForFirstAA.size(); i++) {
			if (isStartCodon[codonToIndex(codonsForFirstAA.get(i))]) {
				possibleStartCodons.add(codonsForFirstAA.get(i));
			}
		}
		if (possibleStartCodons.size() == 0) return b.toString();

		//ok, it's a good start codon
		b.append(possibleStartCodons.get(r.nextInt(possibleStartCodons.size())));

		//now, the rest
		for (int i = 1; i < protein.length(); i++) {
			ArrayList<String> codonsForThisAA =
					reverseTranslationTable.get(protein.substring(i, i + 1));
			b.append(codonsForThisAA.get(r.nextInt(codonsForThisAA.size())));
		}
		return b.toString();
	}


	// test case for benchmarking translation
	public static void main(String[] args) {
		GeneticCode gc = new GeneticCode();
		int numRuns = Integer.parseInt(args[0]);
		String DNA = "AAAATGCCCCCTTACGGGGGAGCTAACAGACTACTACTACTACTACTACTACTATGGTGGTGGTGGTACAACAAAAGAAATTGGTGGTGGTGGTAATTTT";
		System.out.println("Translating a " + DNA.length() + "-mer " + numRuns + " times");
		Date start = new Date();
		for (int i = 0; i < numRuns; i++) {
			gc.translateWithCache(DNA);
		}
		Date end = new Date();
		long elapsedTime = end.getTime() - start.getTime();
		System.out.println("It took " + elapsedTime + " mSec");
	}

	public String getRandomStopCodon() {
		boolean foundStopCodon = false;
		int index = -1;
		while (!foundStopCodon) {
			index = r.nextInt(64);
			foundStopCodon = isStopCodon[index];
		}
		return indexToCodon(index);
	}
}
