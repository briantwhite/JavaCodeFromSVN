package molBiol;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class DNAHistListControlPanel extends JPanel {
	
	final Genex genex;
	
	private JButton toUPButton;
	private JButton toLPButton;
	
	public DNAHistListControlPanel(final Genex genex){
		super();
		this.genex = genex;
		
		toUPButton = new JButton(">Upper");
		toLPButton = new JButton(">Lower");
		
		this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		this.add(toUPButton);
		this.add(toLPButton);
		
		toUPButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				genex.sendSelectedGenetoUP();
			}
			
		});
		
		toLPButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				genex.sendSelectedGenetoLP();
			}
			
		});
		
	}
		
	public void setButtonsEnabled(boolean b) {
		toLPButton.setEnabled(b);
		toUPButton.setEnabled(b);
	}

}
