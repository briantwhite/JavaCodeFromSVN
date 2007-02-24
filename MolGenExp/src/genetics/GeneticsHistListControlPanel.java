package genetics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class GeneticsHistListControlPanel extends JPanel {
	
	final GeneticsWorkshop genWksp;
	
	private JButton toUPButton;
	private JButton toLPButton;
	
	public GeneticsHistListControlPanel(final GeneticsWorkshop gw){
		super();
		genWksp = gw;
		
		toUPButton = new JButton(">Upper");
		toUPButton.setEnabled(false);
		toLPButton = new JButton(">Lower");
		toLPButton.setEnabled(false);
		
		this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		this.add(toUPButton);
		this.add(toLPButton);
		
		toUPButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				genWksp.sendSelectedTrayToUP();
			}
			
		});
		
		toLPButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				genWksp.sendSelectedTrayToLP();
			}
			
		});
		
	}
	
	public void setSendToGeneticsWindowButtonEnabled(boolean b){
		toUPButton.setEnabled(b);
		toLPButton.setEnabled(b);
	}
	
}
