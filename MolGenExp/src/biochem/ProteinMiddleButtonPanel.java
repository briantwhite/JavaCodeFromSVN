package biochem;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class ProteinMiddleButtonPanel extends JPanel {
	
	final Protex protex;
	
	private JLabel colorChipLabel;
	private JLabel colorChip;
	private JButton toUPButton;
	private JButton toLPButton;
	private JButton compareButton;
	
	public ProteinMiddleButtonPanel(final Protex protex){
		super();
		this.protex = protex;
		
		colorChipLabel = new JLabel("<html><center>Combined<br>Color:"
				+ "</center></html>");
		colorChip = new JLabel("<html><pre>         </pre></html>");
		colorChip.setOpaque(true);
		colorChip.setBackground(Color.WHITE);
		colorChip.setBorder(new LineBorder(Color.BLACK));
		toUPButton = new JButton("> Upper");
		toLPButton = new JButton("> Lower");
		compareButton = new JButton("Compare");
		
		this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		this.add(colorChipLabel);
		this.add(colorChip);
		this.add(toUPButton);
		this.add(toLPButton);
		this.add(compareButton);
		
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

		compareButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				protex.computeDifference();
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
