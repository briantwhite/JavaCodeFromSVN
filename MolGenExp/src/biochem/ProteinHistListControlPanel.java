package biochem;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class ProteinHistListControlPanel extends JPanel {
	
	final Protex protex;
	
	private JButton toUPButton;
	private JButton toLPButton;
	
	public ProteinHistListControlPanel(final Protex protex){
		super();
		this.protex = protex;
		
		toUPButton = new JButton("> Upper");
		toLPButton = new JButton("> Lower");
		
		this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		this.add(toUPButton);
		this.add(toLPButton);
		
		toUPButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				protex.sendSelectedFPtoUP();
			}
		});
		
		toLPButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				protex.sendSelectedFPtoLP();
			}
		});
	}
		
	public void setButtonsEnabled(boolean b) {
		toLPButton.setEnabled(b);
		toUPButton.setEnabled(b);
	}

}
