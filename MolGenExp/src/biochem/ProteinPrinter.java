package biochem;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

import javax.swing.JOptionPane;

public class ProteinPrinter implements Printable {
	
	PageFormat pformat;
	PrinterJob pJob;
		
	String upperAASeq;
	String lowerAASeq;
	
	BufferedImage upperProtein;
	BufferedImage lowerProtein;
	
	FoldingWindow ufw;
	FoldingWindow lfw;
	
	private static int printedWidth = 500;
	private static int printedHeight = 250;
	
	public ProteinPrinter() {
		pformat = new PageFormat();
		pformat.setOrientation(PageFormat.PORTRAIT);
		pJob = PrinterJob.getPrinterJob();
		upperAASeq = "";
		lowerAASeq = "";
		ufw = null;
		lfw = null;
		upperProtein = null;
		lowerProtein = null;
	}

	public void printProteins(FoldingWindow ufw, FoldingWindow lfw) {
		if ((ufw.getFullSizePic() == null) && (lfw.getFullSizePic() == null)) {
			JOptionPane.showMessageDialog(null, "No Proteins to Print!", 
					"Printer Warning", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		if (ufw.getFullSizePic() != null) {
			this.ufw = ufw;
			upperAASeq = ufw.getAaSeq();
			upperProtein = ufw.getFullSizePic();			
		}
		
		if (lfw.getFullSizePic() != null) {
			this.lfw = lfw;
			lowerAASeq = lfw.getAaSeq();
			lowerProtein = lfw.getFullSizePic();
		}
		
		if (pJob.printDialog()) {
			pJob.setPrintable(this, pformat);
			try {
				pJob.print();
			} catch (PrinterException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	public int print(Graphics g, PageFormat pf, int pageIndex) throws PrinterException {
		Graphics2D g2d = (Graphics2D)g;
		
		if (pageIndex == 0) {
			g2d.translate(pf.getImageableX(), pf.getImageableY());
			
			if (ufw != null) {
				g2d.drawString(upperAASeq, 0, 20);
				
				g2d.drawImage(upperProtein, 0, 40, 
						(int)(upperProtein.getWidth()/getScale(upperProtein)),
								(int)(upperProtein.getHeight()/getScale(upperProtein)), null);
			}
			
			if (lfw != null) {
				g2d.drawString(lowerAASeq, 0, 320);
				
				g2d.drawImage(lowerProtein, 0, 340, 
						(int)(lowerProtein.getWidth()/getScale(lowerProtein)),
								(int)(lowerProtein.getHeight()/getScale(lowerProtein)), null);		
			}
			
			return Printable.PAGE_EXISTS;
		}
		return Printable.NO_SUCH_PAGE;
	}
	
	private double getScale(BufferedImage image) {
		int imageWidth = image.getWidth(null);
		int imageHeight = image.getHeight(null);
		double horizontalScale = (double) imageWidth/printedWidth;
		double verticalScale = (double) imageHeight/printedHeight;

		if (horizontalScale > verticalScale) {
			return horizontalScale;
		} else {
			return verticalScale;
		}
	}

}
