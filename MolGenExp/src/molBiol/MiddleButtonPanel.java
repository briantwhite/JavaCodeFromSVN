package molBiol;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class MiddleButtonPanel extends JPanel {
	
	final Genex genex;
	
	private JLabel colorChipLabel;
	private JLabel colorChip;
	private JButton toUPButton;
	private JButton toLPButton;
	
	public MiddleButtonPanel(final Genex genex){
		super();
		this.genex = genex;
		
		colorChipLabel = new JLabel("<html><center>Combined<br>Color:"
				+ "</center></html>");
		colorChip = new JLabel("<html><pre>         </pre></html>");
		colorChip.setOpaque(true);
		colorChip.setBackground(Color.LIGHT_GRAY);
		colorChip.setBorder(new LineBorder(Color.BLACK));
		toUPButton = new JButton("> Upper");
		toLPButton = new JButton("> Lower");
		
		this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		this.add(colorChipLabel);
		this.add(colorChip);
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
	
	public void setCombinedColor(Color c) {
		colorChip.setBackground(c);
	}

}
