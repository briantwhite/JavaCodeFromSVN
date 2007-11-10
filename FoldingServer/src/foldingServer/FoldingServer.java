package foldingServer;

import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class FoldingServer extends JFrame {
		
	public static final Color SS_BONDS_OFF_BACKGROUND = new Color((float) 0.7,
			(float) 0.7, (float) 1.0);
	
	public static final Color SS_BONDS_ON_BACKGROUND = new Color((float) 0.7,
			(float) 1.0, (float) 1.0);
	
	private int aaRadius = 20;
	
	private AminoAcidPalette aap;
	
	public FoldingServer() {
		super("Folding Server");
		addWindowListener(new ApplicationCloser());
		setupUI();
	}
	
	public static void main(String[] args) {
		FoldingServer foldingServer = new FoldingServer();
		foldingServer.pack();
		foldingServer.setVisible(true);
	}
	
	class ApplicationCloser extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			System.exit(0);
		}
	}
	
	private void setupUI() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		aap = new AminoAcidPalette(aaRadius, 225, 180, 4, 5);
		panel.add(aap);
		FoldingWindow fw = new FoldingWindow(this);
		panel.add(fw);
		this.getContentPane().add(panel);
	}
	
	private AminoAcidPalette getAAPalette() {
		return aap;
	}
	

}
