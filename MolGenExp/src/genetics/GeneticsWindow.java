package genetics;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GeneticsWindow extends JPanel {
	
	private GeneticsWorkshop gw;
	
	private JLabel upperLabel;
	
	private JPanel trayPanel;
	
	private JPanel bottomButtonPanel;
	private JButton crossTwoButton;
	private JButton selfCrossButton;
	private JButton mutateButton;
	
	public GeneticsWindow(GeneticsWorkshop gw) {
		super();
		this.gw = gw;
		setupUI();
	}
	
	private void setupUI() {
		setLayout(new BorderLayout());
		
		upperLabel = new JLabel("Ready...");
		add(upperLabel, BorderLayout.NORTH);
		
		trayPanel = new JPanel();
		trayPanel.setMinimumSize(new Dimension(500,200));
		add(trayPanel, BorderLayout.CENTER);
		
		bottomButtonPanel = new JPanel();
		bottomButtonPanel.setLayout(
				new BoxLayout(bottomButtonPanel, BoxLayout.X_AXIS));
		crossTwoButton = new JButton("Cross Two Organisms");
		crossTwoButton.setEnabled(false);
		bottomButtonPanel.add(crossTwoButton);
		selfCrossButton = new JButton("Self-Cross One Organism");
		selfCrossButton.setEnabled(false);
		bottomButtonPanel.add(selfCrossButton);
		mutateButton = new JButton("Mutate One Organism");
		mutateButton.setEnabled(false);
		bottomButtonPanel.add(mutateButton);
		
		add(bottomButtonPanel);
	}

	public void setCurrentTray(Tray tray) {
		
	}
}
