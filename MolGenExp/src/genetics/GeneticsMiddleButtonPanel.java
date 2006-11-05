package genetics;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class GeneticsMiddleButtonPanel extends JPanel {
	
	final GeneticsWorkshop genWksp;
	
	private JButton toUPButton;
	private JButton toLPButton;
	private JButton saveOrganismToGreenhouseButton;
	
	public GeneticsMiddleButtonPanel(final GeneticsWorkshop gw){
		super();
		genWksp = gw;
		
		toUPButton = new JButton(">UGW");
		toUPButton.setEnabled(false);
		toLPButton = new JButton(">LGW");
		toLPButton.setEnabled(false);
		saveOrganismToGreenhouseButton = new JButton("Save");
		saveOrganismToGreenhouseButton.setEnabled(false);
		
		this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		this.add(toUPButton);
		this.add(toLPButton);
		this.add(saveOrganismToGreenhouseButton);
		
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
		
		saveOrganismToGreenhouseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				genWksp.getMolGenExp().saveSelectedOrganismToGreenhouse();
			}
		});

	}
	
	public void setSendToGeneticsWindowButtonEnabled(boolean b){
		toUPButton.setEnabled(b);
		toLPButton.setEnabled(b);
	}
	
	public void setSaveButtonEnabled(boolean b) {
		saveOrganismToGreenhouseButton.setEnabled(b);
	}
	
}
