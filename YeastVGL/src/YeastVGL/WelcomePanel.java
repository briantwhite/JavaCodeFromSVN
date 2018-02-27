package YeastVGL;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class WelcomePanel extends JPanel {
	
	private YeastVGL_GUI yeastVGLgui;
	
	public WelcomePanel(YeastVGL_GUI yeastVGLgui) {
		this.yeastVGLgui = yeastVGLgui;
		setupUI();
	}
	
	private void setupUI() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		JLabel welcomeLabel = new JLabel("Welcome to Yeast VGL. Please click one of the buttons below:");
		add(welcomeLabel);
		JButton newProblemButton = new JButton("Start new problem");
		add(newProblemButton);
		JButton openWorkButton = new JButton("Open a problem you have saved");
		add(openWorkButton);
		JButton quitButton = new JButton("Quit Yeast VGL");
		add(quitButton);
		
		newProblemButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				yeastVGLgui.enableTabs();
				yeastVGLgui.goToComplementationTestPane();
			}			
		});
		
		openWorkButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				yeastVGLgui.openWork();
			}			
		});
		
		quitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}			
		});


	}

}
