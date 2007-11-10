package protex;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

public class TargetShapeDisplayDialog extends JDialog {

	private Protex protex;

	private JPanel buttonPanel;
	private JButton checkUpperShapeButton;
	private JButton checkLowerShapeButton;
	private JButton cancelButton;

	public TargetShapeDisplayDialog(final Protex protex) {
		super(protex, "Try to Match this Target Shape", false);
		this.protex = protex;
		
		JPanel masterPanel = new JPanel();
		masterPanel.setLayout(new BoxLayout(masterPanel, BoxLayout.Y_AXIS));
		
		masterPanel.add(new PicturePanel());
		
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));

		JPanel checkButtonPanel = new JPanel();
		checkButtonPanel.setLayout(new BoxLayout(checkButtonPanel, BoxLayout.Y_AXIS));
		checkUpperShapeButton = new JButton("Check Upper Protein for Matching Shape");
		checkButtonPanel.add(checkUpperShapeButton);
		checkLowerShapeButton = new JButton("Check Lower Protein for Matching Shape");
		checkButtonPanel.add(checkLowerShapeButton);

		buttonPanel.add(checkButtonPanel);

		buttonPanel.add(Box.createHorizontalGlue());

		cancelButton = new JButton("Cancel");
		buttonPanel.add(cancelButton);

		masterPanel.add(buttonPanel);
		
		this.getContentPane().add(masterPanel);

		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				goAway();
			}
		});

		checkUpperShapeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				protex.checkUpperAgainstTarget();
			}
		});

		checkLowerShapeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				protex.checkLowerAgainstTarget();
			}
		});

		
	}

	public void showTargetShapeDisplayDialog() {
		
		//calculate appropriate bounds
		int buttonPanelWidth = buttonPanel.getPreferredSize().width;
		int imageWidth = protex.getCurrentTargetShape().getBigPic().getIconWidth();
		
		this.setBounds(300, 300, Math.max(buttonPanelWidth, imageWidth), 
				protex.getCurrentTargetShape().getBigPic().getIconHeight() + 60);
		
		this.setTitle(protex.getCurrentTargetShape().getName() 
				+ "; " 
				+ protex.getCurrentTargetShape().getNumAcids() 
				+ " amino acids.");

		this.setVisible(true);
	}

	public void goAway() {
		this.setVisible(false);
	}

	private class PicturePanel extends JPanel {
				
		PicturePanel() {
			
		}
		
		public void paint(Graphics g) {
			g.drawImage(protex.getCurrentTargetShape().getBigPic().getImage(), 0, 0, null);
		}
	}
}
