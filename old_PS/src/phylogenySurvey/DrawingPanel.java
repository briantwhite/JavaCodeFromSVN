package phylogenySurvey;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.util.Iterator;

import javax.swing.JPanel;

public class DrawingPanel extends JPanel implements Printable {
	
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

	public int print(Graphics g, PageFormat pageFormat, int pageIndex) {
		if (pageIndex > 0) {
			return(NO_SUCH_PAGE);
		} else {
			Graphics2D g2d = (Graphics2D)g;
			g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
			paint(g2d);
			return(PAGE_EXISTS);
		}
	}

}
