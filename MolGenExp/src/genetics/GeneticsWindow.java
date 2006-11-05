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
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import molBiol.ExpressedGene;
import molGenExp.Organism;

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

	}

	public void crossTwo(Organism o1, Organism o2) {		
		int trayNum = gw.getNextTrayNum();		
		offspringList.clearList();

		ExpressedGene eg1 = null;
		ExpressedGene eg2 = null;

		if (o1.equals(o2)) {
			upperLabel.setText("<html><h1>"
					+ "Tray " + trayNum + ": "
					+ o1.getName() + " self-cross"
					+ "</h1></html>");
		} else {
			upperLabel.setText("<html><h1>" 
					+ "Tray " + trayNum + ": "
					+ o1.getName() + " X " + o2.getName()
					+ "</h1></html");
		}

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
	}

	public void setCurrentTray(Tray tray) {

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
