package SE;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class Configuration {
	
	private String configFileName;

	private int portNum;	// for communication with server
	
	/*
	 * don't do an evolution run, just do all the 
	 * 	single base pair mutational neighbors
	 * 
	 * activated by setting DNAforNNAFileName to something other than ""
	 */
	private boolean nearestNeighborMode;
	private String DNAforNNAFileName;

	private double numUnboundStates;	
	/*
	 * in the dGbinding calculation, you need a number of unbound states
	 * - this is inversely related to the concentration
	 * - the higher you make it, the less stringent the fitness wrt binding 
	 *   energy
	 */

	private int minAcceptableProteinLength;	// see Square Evolution Log 03 p 13
	
	private String geneticCodeFileName;
	private GeneticCode geneticCode;

	private int populationSize; // the size in each generation

	private String startingOrganismDNA;		// if a defined sequence
	private int startingRandomDNALength;	// if a random sequence, startinOrgDNA = "Random"

	private String ligandSequence;
	private String ligandStructure;
	private int ligandRotamer;

	private double pointMutRate;
	private Mutator mutator;

	private double neutrality;
	
	private boolean pickSingleBestOrganism; //testing alternative to pick in proportion to fitness

	private int numRuns;
	private int stopAtGeneration;
	private int miniReportInterval;
	private int bigReportInterval;
	private int fitnessReportInterval;
	
	private boolean outputWorldEachGeneration;

	private String logFileName;
	private String fitnessFileName;
	private String proteinFoldingFileName;
	private String badProteinFileName;

	public Configuration(String configFileName) {
		
		this.configFileName = configFileName;
		
		// defaults
		portNum = 5;
		
		nearestNeighborMode = false;
		DNAforNNAFileName = "";

		numUnboundStates = 0;
		
		minAcceptableProteinLength = 50; // no restriction

		geneticCodeFileName = "StandardCode.xml";

		populationSize = 1000;

		// MAAAAAAAAAAAAAAAAF (an arbitrary gene)
		startingOrganismDNA = "CCATGGCCGCCGCGGCAGCGGCTGCCGCGGCGGCCGCTGCTGCTGCTGCCGCCTTTTAAGGC";
		startingRandomDNALength = 100;

		ligandSequence = "AKGKEGDH"; 	// use default from Bloom et al.
		ligandStructure = "RRDDDLD";
		ligandRotamer = -1; // best of all 4

		pointMutRate = 0.0001;

		neutrality = 0.0f;
		
		pickSingleBestOrganism = false;

		stopAtGeneration = 1000;
		numRuns = 10;
		miniReportInterval = 10;
		bigReportInterval = 100;
		fitnessReportInterval = 1;
		
		outputWorldEachGeneration = false;

		logFileName = "output.txt";
		fitnessFileName = "fitness.txt";
		proteinFoldingFileName = "";
		badProteinFileName = "badProteins.txt";

		if (configFileName != null) {
			// read from file
			try {
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(configFileName);
				doc.getDocumentElement().normalize();
				processConfigFile(doc);
			} catch (Exception e1) {
				System.err.println(e1.getClass() + " " + e1.getMessage());
			}

		}

		mutator = new Mutator(pointMutRate, populationSize, startingOrganismDNA.length());
		geneticCode = new GeneticCode(geneticCodeFileName);
	}


	private void processConfigFile(Document doc) {


		NodeList nl = doc.getElementsByTagName("Data").item(0).getChildNodes();
		for (int i = 0; i < nl.getLength(); i++) {
			Node n = nl.item(i);
			if (n.getNodeType() == Node.ELEMENT_NODE) {
				String name = n.getNodeName();
				String value = n.getChildNodes().item(0).getNodeValue();

				if (name.equals("PortNum")) portNum = Integer.parseInt(value);
				if (name.equals("DNAforNNAFileName")) {
					if (!value.equals("")) {
						DNAforNNAFileName = value;
						nearestNeighborMode = true;
					}
				}
				if (name.equals("NumUnboundStates")) numUnboundStates = Double.parseDouble(value);
				if (name.equals("MinAcceptableProteinLength")) minAcceptableProteinLength = Integer.parseInt(value);
				if (name.equals("GeneticCodeFileName")) geneticCodeFileName = value;
				if (name.equals("PopulationSize")) populationSize = 
					Integer.parseInt(value);
				/*
				 * if a DNA string, all start with this seq
				 * if a number, it's the length of the random n-mer
				 * that is rnadomly generated for each org in starting pop
				 */
				if (name.equals("StartingDNA")) {
					if (value.matches("[AGCT]+") && (value.length() == 100)) {
						startingOrganismDNA = value;				
					} else if (value.matches("[0-9]+")) {
						startingRandomDNALength = Integer.parseInt(value);
						startingOrganismDNA = "Random";
					} else { 
						System.out.println("StartingDNA in file: " + configFileName + " contains bad characters and/or is wrong length; substituting default DNA.");
					}
				}
				if (name.equals("LigandSequence")) ligandSequence = value;
				if (name.equals("LigandStructure")) ligandStructure = value;
				if (name.equals("LigandRotamer")) ligandRotamer = 
					Integer.parseInt(value);
				if (name.equals("PointMutRate")) pointMutRate = 
					Double.parseDouble(value);
				if (name.equals("Neutrality")) neutrality = 
					Double.parseDouble(value);
				if (name.equals("PickSingleBestOrganism")) {
					if (Integer.parseInt(value) == 1) pickSingleBestOrganism = true;
				}
				if (name.equals("NumberOfRuns")) numRuns = 
					Integer.parseInt(value);
				if (name.equals("NumGenerationsPerRun")) stopAtGeneration =
					Integer.parseInt(value);
				if (name.equals("MiniReportInterval")) miniReportInterval =
					Integer.parseInt(value);
				if (name.equals("BigReportInterval")) bigReportInterval =
					Integer.parseInt(value);
				if (name.equals("FitnessReportInterval")) fitnessReportInterval =
					Integer.parseInt(value);
				if (name.equals("OutputWorldEachGeneration")) {
					if (Integer.parseInt(value) == 1) outputWorldEachGeneration = true;
				}
				if (name.equals("LogFileName")) logFileName = value;
				if (name.equals("FitnessFileName")) fitnessFileName = value;
				if (name.equals("ProteinFoldingFileName")) proteinFoldingFileName = value;
				if (name.equals("BadProteinFileName")) badProteinFileName = value;
			}
		}


	}


	public int getPortNum() {
		return portNum;
	}
	
	public boolean isNearestNeighborMode() {
		return nearestNeighborMode;
	}
	
	public String getDNAforNNAFileName() {
		return DNAforNNAFileName;
	}

	public double getNumUnboundStates() {
		return numUnboundStates;
	}
	
	public int getMinAcceptableProteinLength() {
		return minAcceptableProteinLength;
	}

	public String getGeneticCodeFileName() {
		return geneticCodeFileName;
	}


	public GeneticCode getGeneticCode() {
		return geneticCode;
	}


	public int getPopulationSize() {
		return populationSize;
	}


	public String getStartingOrganismDNA() {
		return startingOrganismDNA;
	}

	public int getStartingRandomDNASequenceLength() {
		return startingRandomDNALength;
	}

	public String getLigandSequence() {
		return ligandSequence;
	}


	public String getLigandStructure() {
		return ligandStructure;
	}


	public int getLigandRotamer() {
		return ligandRotamer;
	}

	public Mutator getMutator() {
		return mutator;
	}

	public Double getNeutrality() {
		return neutrality;
	}
	
	public boolean isPickSingleBestOrganism() {
		return pickSingleBestOrganism;
	}

	public int getNumRuns() {
		return numRuns;
	}

	public int getStopAtGeneration() {
		return stopAtGeneration;
	}

	public int getMiniReportInterval() {
		return miniReportInterval;
	}

	public int getBigReportInterval() {
		return bigReportInterval;
	}

	public int getFitnessReportInterval() {
		return fitnessReportInterval;
	}

	public boolean outputWorldEachGeneration() {
		return outputWorldEachGeneration;
	}

	public String getLogFileName() {
		return logFileName;
	}


	public String getFitnessFileName() {
		return fitnessFileName;
	}

	public String getProteinFoldingFileName() {
		return proteinFoldingFileName;
	}
	
	public String getBadProteinFileName() {
		return badProteinFileName;
	}

	public String toString() {
		StringBuffer b = new StringBuffer();
		b.append("Port Number = " + portNum + "\n");
		b.append("Number of unbound states in dGbinding calculation = " 
				+ numUnboundStates + "\n");
		b.append("Minimum acceptable protein length = " + minAcceptableProteinLength + "\n");
		b.append("Genetic Code File Name = " + geneticCodeFileName + "\n");
		b.append("Population Size = " + populationSize + "\n");
		if (startingOrganismDNA.equals("Random")) {
			b.append("Starting Organism DNA = Random " + startingRandomDNALength + "-mer\n");
		} else {
			b.append("Starting Organism DNA = " + startingOrganismDNA + "\n");
		}
		b.append("Starting Organism Protein = " 
				+ geneticCode.translateWithCache(startingOrganismDNA) + "\n");
		b.append("Ligand Sequence = " + ligandSequence + "\n");
		b.append("Ligand Structure = " + ligandStructure + "\n");
		b.append("Ligand Rotamer = " + ligandRotamer + "\n");
		b.append("Point Mutation rate = " + pointMutRate + "\n");
		b.append("Neutrality = " + neutrality + "\n");
		if (pickSingleBestOrganism) {
			b.append("Natural selection = Pick Single Best Organism\n");
		} else {
			b.append("Natural selection = Pick in proportion to fitness\n");
		}
		b.append("Number of runs = " + numRuns + "\n");
		b.append("Number of Generations per run = " + stopAtGeneration + "\n");
		b.append("Give a mini report every " 
				+ miniReportInterval + " generations.\n");
		b.append("Give a big report every " 
				+ bigReportInterval + " generations.\n");
		b.append("Give a fitness report every "
				+ fitnessReportInterval + " generations.\n");
		b.append("Log File Name = " + logFileName + "\n");
		b.append("Fitness Log File Name = " + fitnessFileName + "\n");
		b.append("Protein Folding File Name = " + proteinFoldingFileName + "\n");
		b.append("Bad Proteins File Name = " + badProteinFileName + "\n");

		return b.toString();
	}

}
