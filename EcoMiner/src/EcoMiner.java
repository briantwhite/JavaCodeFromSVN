import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.Timer;
import javax.swing.text.html.HTMLDocument;

public class EcoMiner extends JFrame {

	static final String versionString = "1.0.1";

	static final String htmlStart = "<html><body>";
	static final String htmlEnd = "</html></body>";
	static final String step1String = 
		"<li><font color=black>Select Species Distribution arff File</font></li>";
	static final String step2String =
		"<li><font color=black>Select Climate Data File</font></li>";
	static final String step3String =
		"<li><font color=black>Save Results as csv File</font></li>";
	static final String step4String =
		"<li><font color=black>All Done</font></li>";

	private Point centerOfScreen;

	private File wekaJar;
	private String wekaJarFilename;

	private String resultPaneText;

	private int currentStep;

	private JButton wekaButton;
	private JButton printButton;
	private JButton useLastClassifierButton;

	private JLabel stepsLabel;
	private JTextPane resultPane;
	private JScrollPane rpScrollPane;
	private JButton nextButton;
	private JButton backButton;

	private File speciesDistributionFile;
	private String speciesName;

	private DocumentRenderer docRenderer;

	private File classifier;

	private ClassifierRunner cr;

	private File climateDistributionFile;
	private ArrayList climateAttributes;
	private ArrayList climateInstances;

	private File resultFile;

	public EcoMiner() {		
		super("EcoMiner version " + versionString);
		getContentPane().setLayout(new BorderLayout());

		JPanel topButtonPanel = new JPanel();
		wekaButton = new JButton("Run WEKA directly");
		printButton = new JButton("Print");
		useLastClassifierButton = new JButton("Use Last Saved Classifier");
		topButtonPanel.add(wekaButton);
		topButtonPanel.add(printButton);
		topButtonPanel.add(useLastClassifierButton);
		getContentPane().add(topButtonPanel, BorderLayout.NORTH); 

		stepsLabel = new JLabel();
		stepsLabel.setVerticalAlignment(JLabel.TOP);
		stepsLabel.setBorder(BorderFactory.createEtchedBorder());
		stepsLabel.setPreferredSize(new Dimension(150,300));
		getContentPane().add(stepsLabel, BorderLayout.WEST);

		resultPane = new JTextPane();
		resultPane.setContentType("text/html");
		resultPane.setDragEnabled(false);
		resultPane.setEditable(false);

		rpScrollPane = new JScrollPane(resultPane);
		rpScrollPane.setBorder(BorderFactory.createEtchedBorder());
		rpScrollPane.setPreferredSize(new Dimension(500,300));
		getContentPane().add(rpScrollPane, BorderLayout.CENTER);

		nextButton = new JButton("NEXT");
		backButton = new JButton("BACK");
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(backButton);
		buttonPanel.add(nextButton);
		buttonPanel.setBorder(BorderFactory.createEtchedBorder());
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);

		wekaJarFilename = "workspace/weka.jar";
		wekaJar = new File (wekaJarFilename);
		if (!wekaJar.exists()) {
			MoveableJFileChooser fc = new MoveableJFileChooser();
			fc.setDialogTitle("File weka.jar not found; please locate!");
			int returnVal = fc.showOpenDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				wekaJar = fc.getSelectedFile();
				wekaJarFilename = wekaJar.getAbsolutePath();
				System.out.println(wekaJarFilename);
				if (!wekaJar.getName().equals("weka.jar")) {
					JOptionPane.showMessageDialog(null, "Incorrect file selected; exiting.", 
							"Incorrect file", JOptionPane.ERROR_MESSAGE);
					System.exit(0);
				}
			} else {
				System.exit(0);
			}

		}

		docRenderer = new DocumentRenderer();

		speciesName = "";

		resultPaneText = "";

		currentStep = 0;
		doStep();

		wekaButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Runtime rt = Runtime.getRuntime();
				try {
					Process proc = rt.exec("java -Xmx300m -jar " + wekaJarFilename);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		printButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				HTMLDocument htdoc = (HTMLDocument)resultPane.getDocument();
				docRenderer.print(htdoc);
			}
		});

		useLastClassifierButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				//find first (and hopefully only) .classifier file
				File workspaceDir = new File("workspace");
				File[] workspaceFiles = workspaceDir.listFiles();
				for (int i = 0; i < workspaceFiles.length; i++) {
					File file = (File)workspaceFiles[i];
					if (file.getName().endsWith(".classifier")) {
						classifier = file;
						break;
					}
				}

				speciesName = classifier.getName().replaceAll(".arff.classifier", "");
				currentStep = 2;
				doStep();
			}
		});

		nextButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				currentStep++;
				doStep();
			}
		});

		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				currentStep--;
				doStep();
			}
		});

		speciesDistributionFile = null;
		climateDistributionFile = null;
		resultFile = null;

	}

	public static void main(String[] args) {
		EcoMiner eM = new EcoMiner();

		eM.pack();
		eM.setVisible(true);

		eM.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

	}

	private void doStep() {

		switch (currentStep) {

		case 0:
			doStep0();
			break;

		case 1:
			doStep1();			
			break;

		case 2:
			doStep2();
			break;

		case 3:
			doStep3();
			break;

		case 4:
			doStep4();
			break;

		case 5:
			System.exit(0);
		}

	}

	private void doStep0() {
		
		resultPaneText = "";

		printButton.setEnabled(false);
		useLastClassifierButton.setEnabled(true);

		nextButton.setEnabled(true);
		backButton.setEnabled(false);

		stepsLabel.setText(htmlStart
				+ "<ol>"
				+ step1String
				+ step2String
				+ step3String
				+ step4String
				+ "</ol>"
				+ htmlEnd);

		resultPane.setText(htmlStart
				+ "<center><h3>Ready to Start; click &quot;NEXT&quot;</center></h3>"
				+ htmlEnd);
	}

	private void doStep1() {

		if (!resultPaneText.equals("")) {
			resultPane.setText(resultPaneText);
			printButton.setEnabled(true);
			return;
		} else {
			printButton.setEnabled(false);
			nextButton.setEnabled(true);
			backButton.setEnabled(true);

			stepsLabel.setText(htmlStart
					+ "<ol>"
					+ step1String.replaceAll("black", "red")
					+ step2String
					+ step3String
					+ step4String
					+ "</ol>"
					+ htmlEnd);

			resultPane.setText(htmlStart
					+ "Select arff file for a particular species.<br>"
					+ "This will be used to make a classifier."
					+ htmlEnd);

			MoveableJFileChooser fc = new MoveableJFileChooser();
			fc.setFileFilter(new ArffFileFilter());
			fc.setDialogTitle("Choose Species Distribution .arff file");
			fc.setCurrentDirectory(new File("workspace/SpeciesDistributionFiles"));
			fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
			fc.setApproveButtonText("Select");
			int returnVal = fc.showOpenDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				speciesDistributionFile = fc.getSelectedFile();
				speciesName = speciesDistributionFile.getName().replaceAll(".arff", "");

				//clean up old classifiers
				File workspaceDir = new File("workspace/");
				File[] workspaceFiles = workspaceDir.listFiles();
				for (int i = 0; i < workspaceFiles.length; i++) {
					File file = (File)workspaceFiles[i];
					if (file.getName().endsWith(".classifier")) {
						file.delete();
					}
				}

				wekaButton.setEnabled(false);
				useLastClassifierButton.setEnabled(false);
				backButton.setEnabled(false);
				nextButton.setEnabled(false);

				String[] commands = {
						//first make a version of the arff without
						// site #, lat, or long - you shouln't train on these
						"java -cp " + wekaJarFilename 
						+ " weka.filters.unsupervised.attribute.Remove -R 1-3"
						+ " -i workspace/SpeciesDistributionFiles/" 
						+ speciesDistributionFile.getName()
						+ " -o workspace/temp.arff",
						//then make the classifier
						//  use the same file for training and testing
						//  so it doesn't waste time doing cross validation
						"java -cp " + wekaJarFilename 
						+ " weka.classifiers.rules.JRip -F 3 -N 2.0 -O 2 -S 1 "
						+ "-t workspace/temp.arff "  
						+ "-T workspace/temp.arff "  
						+ "-i -k "
						+ "-d workspace/" 
						+ speciesDistributionFile.getName()
						+ ".classifier"};

				ClassifierGenerator cg = new ClassifierGenerator(commands);
				LongTaskRunner classifierGenerator = new LongTaskRunner(cg,
						this,
						"Generating Classifier, please be patient.",
				"Classifier generated.");

				classifier = new File("workspace"
						+ System.getProperty("file.separator")
						+ speciesDistributionFile.getName()
						+ ".classifier");

				classifierGenerator.go();
				classifierGenerator.getLongTaskTimer().start();

			} else {
				currentStep = 0;
				doStep();
			}
		}
	}

	private void doStep2() {

		printButton.setEnabled(false);
		nextButton.setEnabled(true);
		backButton.setEnabled(true);

		stepsLabel.setText(htmlStart
				+ "<ol>"
				+ step1String
				+ step2String.replaceAll("black", "red")
				+ step3String
				+ step4String
				+ "</ol>"
				+ htmlEnd);

		resultPane.setText(htmlStart
				+ "Select arff climate file .<br>"
				+ "The classifier will predict the distribution of the species<br>" 
				+"using this climate distribution."
				+ htmlEnd);

		MoveableJFileChooser fc = new MoveableJFileChooser();
		fc.setFileFilter(new ArffFileFilter());
		fc.setDialogTitle("Choose Climate arff file");
		fc.setCurrentDirectory(new File("workspace/ClimateFiles/"));
		fc.setApproveButtonText("Select");
		int returnVal = fc.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			climateDistributionFile = fc.getSelectedFile();

			ArffReader afr = new ArffReader(climateDistributionFile);
			LongTaskRunner climateDataReader = new LongTaskRunner(afr,
					this,
					"Reading Climate Data File, Please be patient..<br>",
			"Climate Data file loaded.<br>");
			climateDataReader.go();
			climateDataReader.getLongTaskTimer().start();
			climateAttributes = afr.getAttributes();
			climateInstances = afr.getInstances();

		} else {
			currentStep = 1;
			doStep();
		}
	}


	private void doStep3() {

		printButton.setEnabled(false);
		nextButton.setEnabled(true);
		backButton.setEnabled(true);

		stepsLabel.setText(htmlStart
				+ "<ol>"
				+ step1String
				+ step2String
				+ step3String.replaceAll("black", "red")
				+ step4String
				+ "</ol>"
				+ htmlEnd);

		resultPane.setText(htmlStart
				+ "Name a csv file for your results.<br>"
				+ "The classifier will put the results there.<br>"
				+ "You can then open the csv file with MyWorld."
				+ htmlEnd);

		MoveableJFileChooser fc = new MoveableJFileChooser();
		fc.setFileFilter(new CsvFileFilter());
		fc.setDialogTitle("Name Result file");
		int returnVal = fc.showSaveDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			resultFile = fc.getSelectedFile();

			wekaButton.setEnabled(false);
			useLastClassifierButton.setEnabled(false);
			backButton.setEnabled(false);
			nextButton.setEnabled(false);

			String[] commands = {
					//first, make temporary version of the climate file
					// without the attributes: site, lat, and long
					"java -cp " + wekaJarFilename 
					+ " weka.filters.unsupervised.attribute.Remove -R 1-3"
					+ " -i workspace/ClimateFiles/" 
					+ climateDistributionFile.getName()
					+ " -o workspace/temp.arff",
					//now build classifier on reduced file
					"java -cp " 
					+ wekaJarFilename
					+ " weka.classifiers.rules.JRip -l workspace/"
					+ classifier.getName() 
					+ " -T workspace/temp.arff"
					+ " -p 1"};

			ClassifierRunner cr = new ClassifierRunner(commands, 
					climateAttributes,
					climateInstances,
					speciesName,
					resultFile);
			LongTaskRunner classifierRunner = new LongTaskRunner(cr,
					this,
					"Running classifier, please be patient...",
			"Climate file successfully classified.");
			classifierRunner.go();
			classifierRunner.getLongTaskTimer().start();

		} else {
			currentStep = 1;
			doStep();
		}
	}


	private void doStep4() {

		printButton.setEnabled(false);
		nextButton.setEnabled(true);
		backButton.setEnabled(true);

		stepsLabel.setText(htmlStart
				+ "<ol>"
				+ step1String
				+ step2String
				+ step3String
				+ step4String.replaceAll("black", "red")
				+ "</ol>"
				+ htmlEnd);

		resultPane.setText(htmlStart
				+ "You are all done.<br>" 
				+ "Click &quot;NEXT&quot to exit."
				+ htmlEnd);
	}

	public void updateResultPane(String text) {
		resultPane.setText(text);
	}
	
	public void setResultPaneTextString() {
		resultPaneText = resultPane.getText();
	}

	public void resetButtonStatus() {
		printButton.setEnabled(true);
		wekaButton.setEnabled(true);
		useLastClassifierButton.setEnabled(true);
		backButton.setEnabled(true);
		nextButton.setEnabled(true);
	}
}
