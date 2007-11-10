package protex;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class TargetShapeSelectionDialog extends JDialog {
	
	private Protex protex;
	
	private TargetShapeList targetShapeList;
	private JButton cancelButton;
	
	public TargetShapeSelectionDialog(Protex protex) {
		super(protex, "Double-click a Target Shape", true);
		this.protex = protex;
		this.getContentPane().setLayout(new BorderLayout());
		targetShapeList = new TargetShapeList(new DefaultListModel(), this.protex);
		JScrollPane sp = new JScrollPane(targetShapeList);
		this.getContentPane().add(sp, BorderLayout.CENTER);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
		buttonPanel.add(Box.createHorizontalGlue());
		cancelButton = new JButton("Cancel");
		buttonPanel.add(cancelButton);
		
		this.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		
		this.setBounds(300,300,300,300);
		
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				goAway();
			}
		});
	}
	
	public void showTargetShapeSelectionDialog() {
		File targetShapeFile = new File(Protex.targetShapeFileName);
		if (!targetShapeFile.exists()) {
			JOptionPane.showMessageDialog(
					null,
					"Could Not Find Target Shape File.",
					"File Not Found",
					JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		targetShapeList.clearList();
		
		BufferedReader input = null;
		try {
			input = new BufferedReader(new FileReader(Protex.targetShapeFileName));
			String line = null;
			while ((line = input.readLine()) != null) {
				if (line.indexOf(";") != -1) {
					line = line.trim();
					String[] parts = line.split(",");
					ProteinImageSet imageSet = 
						ProteinImageFactory.buildProtein(
							parts[1],
							protex.getStrictMatchingMode());
					targetShapeList.add(new TargetShape(
							imageSet.getFullScaleImage(),
							imageSet.getThumbnailImage(),
							parts[0],
							parts[1]));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			try {
				if (input!= null) {
					input.close();
				}
			}
			catch (IOException ex) {
				ex.printStackTrace();
			}
		}
				
		this.setVisible(true);

	}
	
	public void goAway() {
		this.setVisible(false);
	}

}
