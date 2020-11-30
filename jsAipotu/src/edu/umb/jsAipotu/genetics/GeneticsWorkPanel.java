package edu.umb.jsAipotu.genetics;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.Timer;

import edu.umb.jsAipotu.molGenExp.ExpressedAndFoldedGene;
import edu.umb.jsAipotu.molGenExp.Organism;
import edu.umb.jsAipotu.molGenExp.OrganismFactory;
import edu.umb.jsAipotu.molGenExp.WorkPanel;

public class GeneticsWorkPanel extends WorkPanel {

	private String title;

	private String upperOrLower;

	private int trayNum; 	//current tray number
	private String parentInfo;	//info on the parent

	private GeneticsWorkbench gw;

	private JLabel upperLabel;

	private JPanel trayPanel;
	private OffspringList offspringList;

	private JButton crossTwoButton;
	private JButton selfCrossButton;
	private JButton mutateButton;

	private OrganismFactory organismFactory;
	
	private MutantGenerator mutantGenerator;
	private Timer timer;

	public GeneticsWorkPanel(String upperOrLower, GeneticsWorkbench gw) {
		super();
		this.upperOrLower = upperOrLower;
		title = upperOrLower + " GeneticsWindow";
		this.gw = gw;
		organismFactory = new OrganismFactory();
		setupUI();
	}

	private void setupUI() {
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createTitledBorder(title));

		upperLabel = new JLabel("Ready...");
		upperLabel.setOpaque(true);
		add(upperLabel, BorderLayout.NORTH);

		trayPanel = new JPanel();
		trayPanel.setLayout(new BoxLayout(trayPanel, BoxLayout.Y_AXIS));
		trayPanel.add(Box.createRigidArea(new Dimension(550,1)));
		offspringList = new OffspringList(
				new DefaultListModel(), 
				gw.getMGE());
		JScrollPane offspringListPane = new JScrollPane(offspringList);
		offspringListPane.setPreferredSize(new Dimension(500,200));
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
				if ((gw.getMGE().getOrg1() == null) ||
						(gw.getMGE().getOrg2() == null)) {
					return;
				}
				crossTwo(gw.getMGE().getOrg1(),
						gw.getMGE().getOrg2());
			}
		});

		selfCrossButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (gw.getMGE().getOrg1() == null) {
					return;
				}
				crossTwo(gw.getMGE().getOrg1(),
						gw.getMGE().getOrg1());
			}
		});

		mutateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (gw.getMGE().getOrg1() == null) {
					return;
				}
				mutateOrganism(gw.getMGE().getOrg1());
			}
		});

		timer = new Timer(100, new TimerListener());
	}

	public void crossTwo(Organism o1, Organism o2) {		
		trayNum = gw.getNextTrayNum();		
		offspringList.clearList();

		ExpressedAndFoldedGene efg1 = null;
		ExpressedAndFoldedGene efg2 = null;

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
				efg1 = o1.getGene1();
			} else {
				efg1 = o1.getGene2();
			}

			//contribution from other parent
			if (random.nextInt(2) == 0) {
				efg2 = o2.getGene1();
			} else {
				efg2 = o2.getGene2(); 
			}

			Organism o = 
				organismFactory.createOrganism(trayNum + "-" + i, efg1, efg2);

			offspringList.add(o);
		}

		// add tray to hist list
		Tray tray = new Tray(trayNum, parentInfo, offspringList);
		gw.addToHistoryList(tray);
	}

	public void setCurrentTray(Tray tray) {
		offspringList.clearList();
		Organism[] organisms = tray.getAllOrganisms();
		for (int i = 0; i < organisms.length; i++) {
			Organism o = organismFactory.createOrganism(organisms[i].getName(),
					organisms[i]);
			offspringList.add(o);
		}
		upperLabel.setText("<html><h1>" 
				+ "Tray " + tray.getNumber() + ": "
				+ tray.getParentInfo()
				+ "</h1></html");

	}

	public void mutateOrganism(Organism o) {
		//figure out how many mutants to make
		Random random = new Random();
		int mutantCount = 10 + random.nextInt(10);

		trayNum = gw.getNextTrayNum();		
		offspringList.clearList();

		mutantGenerator = new MutantGenerator(
				o,
				mutantCount,
				trayNum,
				offspringList,
				gw);

		Thread t = new Thread(mutantGenerator);
		t.start();
		timer.start();
		upperLabel.setText("Making " + mutantCount + " mutant versions of "
				+ "Organism " + o.getName() + ".");
		this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		gw.setSelfCrossButtonsEnabled(false);
		gw.setMutateButtonsEnabled(false);

		gw.getMGE().setStatusLabelText("Making mutants of Organism " 
				+ o.getName());
		JProgressBar progressBar = gw.getMGE().getProgressBar();
		progressBar.setMinimum(0);
		progressBar.setMaximum(mutantGenerator.getLengthOfTask());
		progressBar.setValue(0);
	}

	private class TimerListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			if (mutantGenerator.done()) {

				timer.stop();
				GeneticsWorkPanel.this.setCursor(
						Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				gw.setSelfCrossButtonsEnabled(true);
				gw.setMutateButtonsEnabled(true);
				gw.getMGE().setStatusLabelText("Ready");
				gw.getMGE().getProgressBar().setValue(0);
				parentInfo = "Mutant variants of " 
					+ mutantGenerator.getOrganism().getName();
				upperLabel.setText("<html><h1>" 
						+ "Tray " + trayNum + ": "
						+ parentInfo
						+ "</h1></html");

				// add tray to hist list
				Tray tray = new Tray(trayNum, parentInfo, offspringList);
				gw.addToHistoryList(tray);
			} else {

				gw.getMGE().setStatusLabelText("Making Mutant number " 
						+ (mutantGenerator.getCurrent() + 1) 
						+ " of "
						+ mutantGenerator.getLengthOfTask());
				gw.getMGE().getProgressBar().setValue(mutantGenerator.getCurrent() + 1);

			}
		}
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

	public BufferedImage takeSnapshot() {
		BufferedImage imageBuffer = new BufferedImage(
				this.getWidth(),
				this.getHeight(),
				BufferedImage.TYPE_INT_RGB);
		Graphics g = imageBuffer.getGraphics();
		this.paint(g);
		return imageBuffer;
	}
}
