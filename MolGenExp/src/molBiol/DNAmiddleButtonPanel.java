package molBiol;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class DNAmiddleButtonPanel extends JPanel {
	
	final Genex genex;
	
	private JLabel colorChipLabel;
	private JLabel colorChip;
	private JButton toUPButton;
	private JButton toLPButton;
	private JButton saveOrganismToGreenhouseButton;
	
	public DNAmiddleButtonPanel(final Genex genex){
		super();
		this.genex = genex;
		
		colorChipLabel = new JLabel("<html><center>Combined<br>Color:"
				+ "</center></html>");
		colorChip = new JLabel("     ");
		colorChip.setOpaque(true);
		colorChip.setBackground(Color.LIGHT_GRAY);
		colorChip.setBorder(new LineBorder(Color.BLACK));
		toUPButton = new JButton(">Upper");
		toLPButton = new JButton(">Lower");
		saveOrganismToGreenhouseButton = new JButton("Save");
		
		this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		this.add(colorChipLabel);
		this.add(colorChip);
		this.add(toUPButton);
		this.add(toLPButton);
		this.add(saveOrganismToGreenhouseButton);
		
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
		
		saveOrganismToGreenhouseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				genex.saveOrganismToGreenhouse();
			}
		});

	}
	
	public void setCombinedColor(Color c) {
		colorChip.setBackground(c);
	}
	
	public void setButtonsEnabled(boolean b) {
		toLPButton.setEnabled(b);
		toUPButton.setEnabled(b);
	}

}
