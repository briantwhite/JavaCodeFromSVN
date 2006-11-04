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

import molBiol.ExpressedGene;
import molGenExp.Organism;

public class GeneticsWindow extends JPanel {
	
	private String title;
	
	private GeneticsWorkshop gw;
	
	private JLabel upperLabel;
	
	private JPanel trayPanel;
	private OffspringList offspringList;
	
	private JButton crossTwoButton;
	private JButton selfCrossButton;
	private JButton mutateButton;

		
	public GeneticsWindow(String title, GeneticsWorkshop gw) {
		super();
		this.title = title;
		this.gw = gw;
		setupUI();
	}
	
	private void setupUI() {
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createTitledBorder(title));

		upperLabel = new JLabel("Ready...");
		add(upperLabel, BorderLayout.NORTH);
		
		trayPanel = new JPanel();
//		trayPanel.setMinimumSize(new Dimension(500,200));
		offspringList = new OffspringList(
				new DefaultListModel(), 
				gw.getMolGenExp());
		trayPanel.add(offspringList);
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
				crossTwo();
			}
		});

	}

	public void crossTwo() {
		if ((gw.getMolGenExp().getOrg1() == null) ||
				(gw.getMolGenExp().getOrg2() == null)) {
			return;
		}
		Organism o1 = gw.getMolGenExp().getOrg1();
		Organism o2 = gw.getMolGenExp().getOrg2();
		
		ExpressedGene eg1 = null;
		ExpressedGene eg2 = null;
		
		Random random = new Random();
		for (int i = 1; i < 11; i++) {
			
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
			
			Organism o = new Organism(1,
					"1-" + i,
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
}
