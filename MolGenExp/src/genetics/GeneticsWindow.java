package genetics;

import java.awt.BorderLayout;
import java.awt.Color;
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
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ProgressMonitor;
import javax.swing.Timer;

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
	
	private int trayNum; 	//current tray number
	private String parentInfo;	//info on the parent

	private GeneticsWorkshop gw;

	private JLabel upperLabel;

	private JPanel trayPanel;
	private OffspringList offspringList;

	private JButton crossTwoButton;
	private JButton selfCrossButton;
	private JButton mutateButton;
	
	private MutantGenerator mutantGenerator;
	private ProgressMonitor mutantProgressMonitor;
	private Timer timer;


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
		
		timer = new Timer(500, new TimerListener());
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
		trayNum = gw.getNextTrayNum();		
		offspringList.clearList();

		parentInfo = "Mutant variants of " + o.getName();
		upperLabel.setText("<html><h1>" 
				+ "Tray " + trayNum + ": "
				+ parentInfo
				+ "</h1></html");
		
		this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		gw.setSelfCrossButtonsEnabled(false);
		gw.setMutateButtonsEnabled(false);
		mutantGenerator = new MutantGenerator(
				o,
				trayNum,
				location,
				offspringList,
				gw);
		
		mutantProgressMonitor = new ProgressMonitor(
				GeneticsWindow.this,
				"Generating mutants, please be patient...",
				null,
				0,
				mutantGenerator.getLengthOfTask());
		mutantProgressMonitor.setProgress(0);
		mutantProgressMonitor.setMillisToDecideToPopup(100);
		mutantProgressMonitor.setMillisToPopup(100);
		mutantGenerator.go();
		timer.start();
	}
	
	private class TimerListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			if (mutantProgressMonitor.isCanceled() ||
					mutantGenerator.done()) {
				mutantProgressMonitor.close();
				mutantGenerator.stop();
				Toolkit.getDefaultToolkit().beep();
				timer.stop();
				GeneticsWindow.this.setCursor(
						Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				gw.setSelfCrossButtonsEnabled(true);
				gw.setMutateButtonsEnabled(true);
				// add tray to hist list
				Tray tray = new Tray(trayNum, parentInfo, offspringList);
				gw.addTrayToHistoryList(tray);
			} else {
				mutantProgressMonitor.setProgress(
						mutantGenerator.getCurrent());
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
