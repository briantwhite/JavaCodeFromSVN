package genetics;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import biochem.Attributes;
import biochem.FoldedPolypeptide;
import biochem.FoldingException;
import biochem.FoldingManager;
import biochem.OutputPalette;

import molBiol.ExpressedGene;
import molBiol.Gene;
import molBiol.GeneExpressionWindow;
import molGenExp.Organism;
import molGenExp.ProteinImageFactory;
import molGenExp.ProteinImageSet;
import molGenExp.RYBColorModel;

public class GeneticsWindow extends JPanel {

	private String title;

	private int location;

	private GeneticsWorkshop gw;

	private JLabel upperLabel;

	private JPanel trayPanel;
	private OffspringList offspringList;

	private JButton crossTwoButton;
	private JButton selfCrossButton;
	private JButton mutateButton;


	public GeneticsWindow(int location, GeneticsWorkshop gw) {
		super();
		this.location = location;
		if (location == Organism.LOWER_GENETICS_WINDOW) {
			title = "Lower GeneticsWindow";
		} else {
			title = "Upper Genetics Window";
		}
		this.gw = gw;
		setupUI();
	}

	private void setupUI() {
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createTitledBorder(title));

		upperLabel = new JLabel("Ready...");
		add(upperLabel, BorderLayout.NORTH);

		trayPanel = new JPanel();
		offspringList = new OffspringList(
				new DefaultListModel(), 
				gw.getMolGenExp());
		JScrollPane offspringListPane = new JScrollPane(offspringList);
		offspringListPane.setPreferredSize(new Dimension(600,200));
		trayPanel.add(offspringListPane);
		add(trayPanel, BorderLayout.CENTER);

		JPanel lowerButtonPanel = new JPanel();
		lowerButtonPanel.setBorder(
				BorderFactory.createLineBorder(Color.BLACK));
		lowerButtonPanel.setLayout(new BoxLayout(
				lowerButtonPanel, 
				BoxLayout.X_AXIS));
		lowerButtonPanel.add(Box.createHorizontalGlue());
		crossTwoButton = new JButton("Cross Two Organisms");
		crossTwoButton.setEnabled(false);
		lowerButtonPanel.add(crossTwoButton);
		selfCrossButton = new JButton("Self-cross One Organism");
		selfCrossButton.setEnabled(false);
		lowerButtonPanel.add(selfCrossButton);
		mutateButton = new JButton("Mutate One Organism");
		mutateButton.setEnabled(false);
		lowerButtonPanel.add(mutateButton);
		add(lowerButtonPanel, BorderLayout.SOUTH);

		crossTwoButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if ((gw.getMolGenExp().getOrg1() == null) ||
						(gw.getMolGenExp().getOrg2() == null)) {
					return;
				}
				crossTwo(gw.getMolGenExp().getOrg1(),
						gw.getMolGenExp().getOrg2());
			}
		});

		selfCrossButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (gw.getMolGenExp().getOrg1() == null) {
					return;
				}
				crossTwo(gw.getMolGenExp().getOrg1(),
						gw.getMolGenExp().getOrg1());
			}
		});
		
		mutateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (gw.getMolGenExp().getOrg1() == null) {
					return;
				}
				mutateOrganism(gw.getMolGenExp().getOrg1());
			}
		});
	}

	public void crossTwo(Organism o1, Organism o2) {		
		int trayNum = gw.getNextTrayNum();		
		offspringList.clearList();

		ExpressedGene eg1 = null;
		ExpressedGene eg2 = null;

		String parentInfo = "";
		if (o1.equals(o2)) {
			parentInfo = o1.getName() + " self-cross";
		} else {
			parentInfo = o1.getName() + " X " + o2.getName();
		}
		upperLabel.setText("<html><h1>" 
				+ "Tray " + trayNum + ": "
				+ parentInfo
				+ "</h1></html");

		Random random = new Random();
		int count = 20 + random.nextInt(10);
		for (int i = 1; i < count; i++) {

			//contribution from first parent
			if (random.nextInt(2) == 0) {
				eg1 = o1.getGene1();
			} else {
				eg1 = o1.getGene2();
			}

			//contribution from other parent
			if (random.nextInt(2) == 0) {
				eg2 = o2.getGene1();
			} else {
				eg2 =o2.getGene2(); 
			}

			Organism o = new Organism(location,
					trayNum + "-" + i,
					eg1,
					eg2,
					gw.getProteinColorModel());

			offspringList.add(o);
		}
		
		// add tray to hist list
		Tray tray = new Tray(trayNum, parentInfo, offspringList);
		gw.addTrayToHistoryList(tray);
	}

	public void setCurrentTray(Tray tray) {
		offspringList.clearList();
		Organism[] organisms = tray.getAllOrganisms();
		for (int i = 0; i < organisms.length; i++) {
			Organism o = new Organism(location, 
					organisms[i].getName(),
					organisms[i]);
			offspringList.add(o);
		}
		upperLabel.setText("<html><h1>" 
				+ "Tray " + tray.getNumber() + ": "
				+ tray.getParentInfo()
				+ "</h1></html");

	}

	public void mutateOrganism(Organism o) {
		int trayNum = gw.getNextTrayNum();		
		offspringList.clearList();

		String parentInfo = "Mutant variants of " + o.getName();
		upperLabel.setText("<html><h1>" 
				+ "Tray " + trayNum + ": "
				+ parentInfo
				+ "</h1></html");
		Random random = new Random();
		int count = 10 + random.nextInt(10);
		for (int i = 1; i < count; i++) {
			offspringList.add(new Organism(
					location,
					trayNum + "-" + i,
					mutateGene(o.getGene1()),
					mutateGene(o.getGene2()),
					gw.getProteinColorModel()));
		}
		
		
		// add tray to hist list
		Tray tray = new Tray(trayNum, parentInfo, offspringList);
		gw.addTrayToHistoryList(tray);
	}
	
	public ExpressedGene mutateGene(ExpressedGene eg) {
		//change one base in the DNA
		Gene gene = eg.getGene();
		if (gene.getDNASequenceLength() == 0) {
			return eg;
		}
		String DNASequence = gene.getDNASequence();
		StringBuffer DNABuffer = new StringBuffer(DNASequence);
		Random random = new Random();
		int targetBase = random.nextInt(DNABuffer.length());
		int base = random.nextInt(4);
		String newBase = "AGCT".substring(base, base + 1);
		DNASequence = (DNABuffer.replace(
				targetBase, 
				targetBase + 1, 
				newBase)).toString();
		Gene newGene = 
			new Gene(DNASequence, gw.getMolGenExp().getGenex().getParams());
		newGene.transcribe();
		newGene.process();
		newGene.translate();
		String html = newGene.generateHTML(0);

		String proteinSequence = newGene.getProteinString();

		if (proteinSequence.indexOf("none") != -1) {
			proteinSequence = "";
		} else {
			//remove leading/trailing spaces and the N- and C-
			proteinSequence = 
				proteinSequence.replaceAll(" ", "");
			proteinSequence = 
				proteinSequence.replaceAll("N-", "");
			proteinSequence = 
				proteinSequence.replaceAll("-C", "");

			//insert spaces between amino acid codes
			StringBuffer psBuffer = new StringBuffer(proteinSequence);
			for (int i = 3; i < psBuffer.length(); i = i + 4) {
				psBuffer = psBuffer.insert(i, " ");
			}
			proteinSequence = psBuffer.toString();
		}
		
		//fold it
		Attributes attributes = new Attributes(
				proteinSequence, 
				3,
				new RYBColorModel(),
				"straight",
				"test");
		FoldingManager manager = FoldingManager.getInstance(
				gw.getMolGenExp().getOverallColorModel());
		try {
			manager.fold(attributes);
		} catch (FoldingException e) {
			e.printStackTrace();
		}

		//make an icon and display it in a dialog
		OutputPalette op = new OutputPalette(
				gw.getMolGenExp().getOverallColorModel());
		manager.createCanvas(op);
		Dimension requiredCanvasSize = 
			op.getDrawingPane().getRequiredCanvasSize();

		ProteinImageSet images = 
			ProteinImageFactory.generateImages(op, requiredCanvasSize);

		FoldedPolypeptide fp = new FoldedPolypeptide(
				proteinSequence,
				op.getDrawingPane().getGrid(), 
				new ImageIcon(images.getFullScaleImage()),
				new ImageIcon(images.getThumbnailImage()), 
				op.getProteinColor());

		ExpressedGene newEg = new ExpressedGene(html, newGene);
		newEg.setFoldedPolypeptide(fp);
		return newEg;
	}
	
	public void setCrossTwoButtonEnabled(boolean b) {
		crossTwoButton.setEnabled(b);
	}

	public void setSelfCrossButtonEnabled(boolean b) {
		selfCrossButton.setEnabled(b);
	}

	public void setMutateButtonEnabled(boolean b) {
		mutateButton.setEnabled(b);
	}

	public OffspringList getGeneticsWorkPanelList() {
		return offspringList;
	}
}
