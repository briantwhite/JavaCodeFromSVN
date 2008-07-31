package phylogenySurvey;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class SurveyUI {
	
	private Container masterContainer;
	private JPanel workPanel;
	
	// location of where clicked in the dragged item
	//  prevents jerky movement
	private int xAdjustment;
	private int yAdjustment;
	private Component dragComponent;
	
	private JLabel testLabel;
	
	public SurveyUI(Container masterContainer) {
		this.masterContainer = masterContainer;
	}
	
	public void setupUI() {
		workPanel = new JPanel();
		workPanel.setLayout(null);
		workPanel.addMouseListener(new MoveLabelHandler());
		workPanel.addMouseMotionListener(new MoveLabelHandler());
		
		testLabel = new JLabel("HI FRED");
		testLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		workPanel.add(testLabel);
		testLabel.setBounds(100, 100, 70, 30);

		
		masterContainer.add(workPanel, BorderLayout.CENTER);
	}
	
	private class MoveLabelHandler implements MouseMotionListener, MouseListener {
		
		public void mouseDragged(MouseEvent e) {
			if (dragComponent == null) return;
			dragComponent.setLocation(e.getX() + xAdjustment, e.getY() + yAdjustment);
		}

		public void mouseMoved(MouseEvent e) {}

		public void mouseClicked(MouseEvent e) {}

		public void mouseEntered(MouseEvent e) {}

		public void mouseExited(MouseEvent e) {}

		public void mousePressed(MouseEvent e) {
			dragComponent = null;
			Component c = workPanel.findComponentAt(e.getX(), e.getY());
			
			if (c instanceof JPanel) return;
			
			dragComponent = c;
			xAdjustment = dragComponent.getLocation().x - e.getX();
			yAdjustment = dragComponent.getLocation().y - e.getY();
			dragComponent.setLocation(e.getX() + xAdjustment, e.getY() + yAdjustment);
		}

		public void mouseReleased(MouseEvent e) {
			dragComponent = null;
		}
	}

}
