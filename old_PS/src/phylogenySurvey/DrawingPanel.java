package phylogenySurvey;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Iterator;

import javax.swing.JPanel;

public class DrawingPanel extends JPanel {
	
	private SurveyUI surveyUI;
	
	public DrawingPanel(SurveyUI surveyUI) {
		this.surveyUI = surveyUI;
	}
	
	public void paint(Graphics g) {
		super.paint(g);

		g.setColor(Color.BLUE);
		Iterator<Link> it = surveyUI.getLinks().iterator();
		while (it.hasNext()) {
			Link link = it.next();
			g.drawLine(link.getOneLabel().getCenter().x, link.getOneLabel().getCenter().y, 
					link.getOtherLabel().getCenter().x, link.getOtherLabel().getCenter().y);
		}
	}

}
