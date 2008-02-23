package genetics;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.Timer;


import molBiol.ExpressedGene;
import molGenExp.Organism;
import molGenExp.WorkPanel;

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

	private MutantGenerator mutantGenerator;
	private Timer timer;

	private JDialog mutantsBeingMadeDialog;
	private JLabel mutantProgressLabel;
	private JProgressBar mutantProgressBar;

	public GeneticsWorkPanel(String upperOrLower, GeneticsWorkbench gw) {
		super();
		this.upperOrLower = upperOrLower;
		title = upperOrLower + " GeneticsWindow";
		this.gw = gw;
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

		ExpressedGene eg1 = null;
		ExpressedGene eg2 = null;

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
				eg2 = o2.getGene2(); 
			}

			Organism o = new Organism(trayNum + "-" + i, eg1, eg2);

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
			Organism o = new Organism(organisms[i].getName(),
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

		mutantsBeingMadeDialog = new JDialog(gw.getMGE(),
				"Making mutants of Organism " + o.getName(),
				true);
		mutantsBeingMadeDialog.setDefaultCloseOperation(
				JDialog.DO_NOTHING_ON_CLOSE);
		mutantProgressLabel = new JLabel("Starting up...");
		mutantProgressBar = new JProgressBar(0, mutantGenerator.getLengthOfTask());
		mutantProgressBar.setValue(0);
		Container cp = mutantsBeingMadeDialog.getContentPane();
		cp.setLayout(
				new BoxLayout(cp, BoxLayout.Y_AXIS));
		cp.add(mutantProgressLabel);
		cp.add(mutantProgressBar);
		JButton cancelButton = new JButton("Cancel");
		cp.add(cancelButton);
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				mutantGenerator.stop();
			}
		});
		mutantsBeingMadeDialog.setSize(new Dimension(400,100));
		mutantsBeingMadeDialog.setLocation(200,200);
		mutantsBeingMadeDialog.setVisible(true);

	}

	private class TimerListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			if (mutantGenerator.done()) {

				timer.stop();
				GeneticsWorkPanel.this.setCursor(
						Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				gw.setSelfCrossButtonsEnabled(true);
				gw.setMutateButtonsEnabled(true);
				mutantsBeingMadeDialog.dispose();

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

				mutantProgressLabel.setText("Making Mutant number " 
						+ (mutantGenerator.getCurrent() + 1) 
						+ " of "
						+ mutantGenerator.getLengthOfTask());
				mutantProgressBar.setValue(mutantGenerator.getCurrent() + 1);

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
}
