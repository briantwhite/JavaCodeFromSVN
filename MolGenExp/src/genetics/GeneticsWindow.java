package genetics;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GeneticsWindow extends JPanel {
	
	private String title;
	
	private GeneticsWorkshop gw;
	
	private JLabel upperLabel;
	
	private JPanel trayPanel;
		
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
		trayPanel.setMinimumSize(new Dimension(500,200));
		add(trayPanel, BorderLayout.CENTER);
		
	}

	public void setCurrentTray(Tray tray) {
		
	}
	
}
